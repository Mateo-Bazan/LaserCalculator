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
          applyMigration(conn, migration);
        }
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
        // Skip comments
        if (trimmed.startsWith("--") || trimmed.startsWith("/*")) {
          continue;
        }
        stmt.execute(trimmed);
      }
    }
    markMigrationAsApplied(conn, migration.version, migration.fileName);
    System.out.println("Migración aplicada: " + migration.version + " - " + migration.fileName);
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
