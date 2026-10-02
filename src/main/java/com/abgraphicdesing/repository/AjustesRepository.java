package com.abgraphicdesing.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.abgraphicdesing.database.DbConfig;
import com.abgraphicdesing.model.entity.Ajustes;

public class AjustesRepository {
  private final DbConfig dbConfig;

  public AjustesRepository (DbConfig dbConfig){
    this.dbConfig = dbConfig;
  }

  public List<Ajustes> getAll(){
    String sql = "SELECT * FROM ajustes ORDER BY nombre";

    List<Ajustes> listaAjustes = new ArrayList<>();

    try (Connection conn = dbConfig.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
      while (rs.next()) {
        listaAjustes.add(new Ajustes(
            rs.getString("nombre"),
            rs.getInt("gasto"),
            UUID.fromString(rs.getString("idPeriodo")),
            UUID.fromString(rs.getString("id"))
        ));
      }
      return listaAjustes;
    }
    catch(SQLException err){
      System.err.println("Error al leer Ajustes: " + err.getMessage());
      throw new RuntimeException();
    }
  }

  public Optional<Ajustes> findById(UUID id){
    String sql = "SELECT * FROM ajustes WHERE id = ?";

    try (Connection conn = dbConfig.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setString(1, id.toString()); // UUID a estring

      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return Optional.of(new Ajustes(
            rs.getString("nombre"),
            rs.getInt("gasto"),
            UUID.fromString(rs.getString("idPeriodo")),
            UUID.fromString(rs.getString("id"))
          ));
        }
        return Optional.empty();
      }
    }
    catch(SQLException err){
      System.err.println("Error al buscar Ajustes por ID: " + err.getMessage());
      throw new RuntimeException();
    }
  }

  public boolean create(Ajustes a){
    String sql = "INSERT INTO ajustes (id, nombre, gasto, id_periodo) VALUES (?, ?, ?, ?)";

    try (Connection conn = dbConfig.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

      ps.setString(1, m.getId().toString());
      ps.setString(2, m.getNombre());
      ps.setInt(3, m.getPrecioCompra());
      ps.setInt(4, m.getMerma());
      ps.setInt(5, m.getPrecioCosto());
      ps.setString(6, m.getDimensiones());
      ps.setInt(7, m.getStock());
      return ps.executeUpdate() > 0;
    }
    catch(SQLException err){
      System.err.println("Error al crear Material: " + err.getMessage());
      throw new RuntimeException();
    }
  }
}
