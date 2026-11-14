package com.abgraphicdesing.database;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DbConfig {
  private static final HikariDataSource dataSource;

  static {
    // 1. Obtener la carpeta personal del usuario (ej: C:\Users\TuUsuario)
    String userHome = System.getProperty("user.home");

    // 2. Apuntar a la carpeta "Documentos" y dentro crear la carpeta con el nombre del proyecto
    File documentsDir = new File(userHome, "Documents");
    File appDir = new File(documentsDir, "abgraphicdesing");

    // Si no existe la carpeta Documentos o la del proyecto, las creamos
    if (!appDir.exists()) {
        appDir.mkdirs();
    }

    // 3. Definir la ruta del archivo de la base de datos
    File dbFile = new File(appDir, "app.db");
    String url = "jdbc:sqlite:" + dbFile.getAbsolutePath();

    // Mensaje útil para saber exactamente dónde se está guardando
    System.out.println("Base de datos ubicada en: " + dbFile.getAbsolutePath());

    HikariConfig config = new HikariConfig();
    config.setJdbcUrl(url);
    config.setMaximumPoolSize(5);
    config.setMinimumIdle(1);

    config.addDataSourceProperty("foreign_keys", "true"); 
    dataSource = new HikariDataSource(config);
  }

  public Connection getConnection() throws SQLException {
    return dataSource.getConnection();
  }
}