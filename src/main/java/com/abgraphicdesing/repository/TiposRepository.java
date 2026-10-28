package com.abgraphicdesing.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.abgraphicdesing.database.DbConfig;
import com.abgraphicdesing.model.entity.Tipos;

public class TiposRepository {
  private final DbConfig dbConfig;

  public TiposRepository(DbConfig dbConfig) {
    this.dbConfig = dbConfig;
  }

  public List<Tipos> getAll() {
    String sql = "SELECT id, tipo FROM tipo ORDER BY tipo";
    List<Tipos> listaTipos = new ArrayList<>();

    try (Connection conn = dbConfig.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {
      while (rs.next()) {
        listaTipos.add(new Tipos(
          rs.getInt("id"),
          rs.getString("tipo")
        ));
      }
      return listaTipos;
    } catch (SQLException err) {
      System.err.println("Error al leer Tipos: " + err.getMessage());
      throw new RuntimeException("Error al leer Tipos: " + err.getMessage());
    }
  }

  public java.util.Optional<Tipos> findById(int id) {
    String sql = "SELECT id, tipo FROM tipo WHERE id = ?";
    try (Connection conn = dbConfig.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setInt(1, id);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return java.util.Optional.of(new Tipos(
            rs.getInt("id"),
            rs.getString("tipo")
          ));
        }
        return java.util.Optional.empty();
      }
    } catch (SQLException err) {
      System.err.println("Error al buscar Tipo por ID: " + err.getMessage());
      throw new RuntimeException("Error al buscar Tipo por ID: " + err.getMessage());
    }
  }
} 
