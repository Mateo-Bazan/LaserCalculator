package com.abgraphicdesing.database;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class MigrationManager {
  private final DbConfig dbConfig;

  public MigrationManager(DbConfig dbConfig) {
    this.dbConfig = dbConfig;
  }

  public void migrate() throws Exception {
    try (Connection conn = dbConfig.getConnection()) {
      conn.setAutoCommit(false);
      try {
        createMigrationTableIfNotExists(conn);
        ensureV1AppliedIfSchemaExists(conn);
        List<MigrationFile> pending = getPendingMigrations(conn);
        for (MigrationFile migration : pending) {
          if (isAlreadySatisfied(conn, migration)) {
            // La columna ya fue agregada por una ejecución anterior o por
            // ensureHistorialColumns; se registra la migración sin re-ejecutarla.
            markMigrationAsApplied(conn, migration.version, migration.fileName);
            System.out.println("Migración ya satisfecha: " + migration.version + " - " + migration.fileName);
            continue;
          }
          applyMigration(conn, migration);
        }
        ensureHistorialColumns(conn);
        conn.commit();
      } catch (Exception e) {
        conn.rollback();
        throw e;
      } finally {
        conn.setAutoCommit(true);
      }
    }
  }

  private void createMigrationTableIfNotExists(Connection conn) throws SQLException {
    String sql = """
      CREATE TABLE IF NOT EXISTS schema_migrations (
        version TEXT PRIMARY KEY,
        script_name TEXT NOT NULL,
        applied_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
      )
    """;
    try (Statement stmt = conn.createStatement()) {
      stmt.execute(sql);
    }
  }

  private void ensureV1AppliedIfSchemaExists(Connection conn) throws SQLException {
    if (isMigrationApplied(conn, "V1")) {
      return;
    }
    if (schemaAlreadyExists(conn)) {
      markMigrationAsApplied(conn, "V1", "V1__init_database.sql");
    }
  }

  private boolean schemaAlreadyExists(Connection conn) throws SQLException {
    String sql = "SELECT name FROM sqlite_master WHERE type='table' AND name IN ('tipo','periodo','impresoras','materiales','ajustes','historial')";
    try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
      int count = 0;
      while (rs.next()) {
        count++;
      }
      return count >= 6;
    }
  }

  private boolean isMigrationApplied(Connection conn, String version) throws SQLException {
    String sql = "SELECT 1 FROM schema_migrations WHERE version = ?";
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setString(1, version);
      try (ResultSet rs = ps.executeQuery()) {
        return rs.next();
      }
    }
  }

  private void markMigrationAsApplied(Connection conn, String version, String scriptName) throws SQLException {
    String sql = "INSERT OR IGNORE INTO schema_migrations (version, script_name, applied_at) VALUES (?, ?, ?)";
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setString(1, version);
      ps.setString(2, scriptName);
      ps.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
      ps.executeUpdate();
    }
  }

  private List<MigrationFile> getPendingMigrations(Connection conn) throws Exception {
    List<MigrationFile> allMigrations = loadMigrationFiles();
    List<MigrationFile> pending = new ArrayList<>();
    for (MigrationFile migration : allMigrations) {
      if (!isMigrationApplied(conn, migration.version)) {
        pending.add(migration);
      }
    }
    pending.sort(Comparator.comparing(m -> m.version));
    return pending;
  }

  private boolean isAlreadySatisfied(Connection conn, MigrationFile migration) throws SQLException {
    // La migración de precio de costo solo agrega la columna precio_costo.
    // Si ya existe (por ejemplo, en bases creadas antes de registrar la migración),
    // no debe volver a ejecutarse porque SQLite no admite IF NOT EXISTS en ADD COLUMN.
    if (!migration.fileName.contains("historial_precio_costo")) {
      return false;
    }
    return historialHasColumn(conn, "precio_costo");
  }

  private boolean historialHasColumn(Connection conn, String columnName) throws SQLException {
    String sql = "PRAGMA table_info(historial)";
    try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
      while (rs.next()) {
        if (columnName.equalsIgnoreCase(rs.getString("name"))) {
          return true;
        }
      }
    }
    return false;
  }

  private List<MigrationFile> loadMigrationFiles() throws Exception {
    List<String> resourceNames = findMigrationResources();
    List<MigrationFile> migrations = new ArrayList<>();
    for (String resourceName : resourceNames) {
      String fileName = resourceName.substring(resourceName.lastIndexOf('/') + 1);
      String version = extractVersion(fileName);
      if (version == null) {
        continue;
      }
      String content = readResource(resourceName);
      migrations.add(new MigrationFile(version, fileName, content));
    }
    return migrations;
  }

  private List<String> findMigrationResources() {
    // Try to find all V* files in db/migrations
    List<String> found = new ArrayList<>();
    // Check common locations
    String[] possiblePaths = {
      "db/migrations/",
      "migrations/",
      "db/"
    };
    for (String basePath : possiblePaths) {
      try {
        // For classpath resources, we need to scan - simplified approach: try known patterns
      } catch (Exception ignored) {}
    }
    // Fallback: try to load known migrations
    String[] candidates = {
      "db/migrations/V1__init_database.sql",
      "db/migrations/V2__historial_snapshot.sql",
      "db/migrations/V3__historial_precio_costo.sql",
      "db/migrations/V4__ajustes_kwh_mensual.sql",
      "db/V1__init_database.sql"
    };
    for (String candidate : candidates) {
      InputStream is = getClass().getClassLoader().getResourceAsStream(candidate);
      if (is != null) {
        try {
          is.close();
        } catch (Exception ignored) {}
        if (!found.contains(candidate)) {
          found.add(candidate);
        }
      }
    }
    return found;
  }

  private String extractVersion(String fileName) {
    if (fileName == null) return null;
    if (fileName.startsWith("V") && fileName.contains("__")) {
      return fileName.substring(0, fileName.indexOf("__"));
    }
    return null;
  }

  private String readResource(String resourceName) throws Exception {
    try (InputStream input = getClass().getClassLoader().getResourceAsStream(resourceName)) {
      if (input == null) {
        throw new IllegalStateException("No se encontró el recurso de migración: " + resourceName);
      }
      try (BufferedReader reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8))) {
        return reader.lines().collect(Collectors.joining("\n"));
      }
    }
  }

  private void applyMigration(Connection conn, MigrationFile migration) throws SQLException {
    String sql = migration.content;
    // Execute statements separated by semicolons, respecting basic cases
    String[] statements = sql.split(";");
    try (Statement stmt = conn.createStatement()) {
      for (String query : statements) {
        String trimmed = query.trim();
        if (trimmed.isEmpty()) {
          continue;
        }
        // Quitar comentarios iniciales para no omitir la sentencia que sigue
        trimmed = stripLeadingComments(trimmed);
        if (trimmed.isEmpty()) {
          continue;
        }
        stmt.execute(trimmed);
      }
    }
    markMigrationAsApplied(conn, migration.version, migration.fileName);
    System.out.println("Migración aplicada: " + migration.version + " - " + migration.fileName);
  }

  private void ensureHistorialColumns(Connection conn) throws SQLException {
    // Compatibilidad: bases creadas con versiones parciales de la migración V2
    // pueden no tener todas las columnas snapshot. Se agregan solo las faltantes.
    String[][] columns = {
      {"impresora_marca", "TEXT"},
      {"impresora_modelo", "TEXT"},
      {"impresora_tipo", "TEXT"},
      {"impresora_titulo", "TEXT"},
      {"impresora_vida_util", "INTEGER"},
      {"impresora_watts", "INTEGER"},
      {"impresora_precio_compra", "REAL"},
      {"impresora_precio_repuesto", "REAL"},
      {"impresora_amortizacion", "REAL"},
      {"material_nombre", "TEXT"},
      {"material_precio_compra", "REAL"},
      {"material_merma", "INTEGER"},
      {"material_precio_costo", "REAL"},
      {"material_dimensiones", "TEXT"},
      {"id_impresora_ref", "TEXT"},
      {"id_material_ref", "TEXT"},
      {"precio_costo", "INTEGER"}
    };

    java.util.Set<String> existing = new java.util.HashSet<>();
    try (Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery("PRAGMA table_info(historial)")) {
      while (rs.next()) {
        existing.add(rs.getString("name"));
      }
    }
    if (existing.isEmpty()) {
      return;
    }
    try (Statement stmt = conn.createStatement()) {
      for (String[] column : columns) {
        if (!existing.contains(column[0])) {
          stmt.execute("ALTER TABLE historial ADD COLUMN " + column[0] + " " + column[1]);
        }
      }
    }
  }

  private String stripLeadingComments(String sql) {
    String s = sql;
    while (true) {
      s = s.trim();
      if (s.startsWith("--")) {
        int nl = s.indexOf('\n');
        if (nl < 0) {
          return "";
        }
        s = s.substring(nl + 1);
      } else if (s.startsWith("/*")) {
        int end = s.indexOf("*/");
        if (end < 0) {
          return "";
        }
        s = s.substring(end + 2);
      } else {
        return s;
      }
    }
  }

  private static class MigrationFile {
    String version;
    String fileName;
    String content;

    MigrationFile(String version, String fileName, String content) {
      this.version = version;
      this.fileName = fileName;
      this.content = content;
    }
  }
} 
