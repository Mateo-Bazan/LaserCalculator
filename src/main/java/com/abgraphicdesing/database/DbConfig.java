package com.abgraphicdesing.database;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DbConfig {
  private static final HikariDataSource dataSource;
  private static final String url = "jdbc:sqlite:data/app.db";

  static {
    File directory = new File("data");
    if (!directory.exists()) {
        directory.mkdirs(); 
    }

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
