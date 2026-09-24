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
import com.abgraphicdesing.model.entity.Materiales;

public class MaterialesRepository {

  private final DbConfig dbConfig;

  public MaterialesRepository(DbConfig dbConfig) {
    this.dbConfig = dbConfig;
  }

  public List<Materiales> read(){
    String sql = "SELECT id, nombre, precio_compra, merma, precio_costo, dimensiones, stock FROM materiales ORDER BY nombre";

    List<Materiales> listaMateriales = new ArrayList<>();

    try (Connection conn = dbConfig.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
      while (rs.next()) {
        listaMateriales.add(new Materiales(
            rs.getString("nombre"),
            rs.getInt("precio_compra"),
            rs.getInt("merma"),
            rs.getString("dimensiones"),
            rs.getInt("stock"),
            UUID.fromString(rs.getString("id"))));
      }
      return listaMateriales;
    }
    catch(SQLException err){
      System.err.println("Error al leer Materiales: " + err.getMessage());
      throw new RuntimeException();
    }
  }

  public Optional<Materiales> findById(UUID id){
    String sql = "SELECT id, nombre, precio_compra, merma, precio_costo, dimensiones, stock FROM materiales WHERE id = ?";

    try (Connection conn = dbConfig.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setString(1, id.toString()); // UUID a estring

      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return Optional.of(new Materiales(
              rs.getString("nombre"),
              rs.getInt("precio_compra"),
              rs.getInt("merma"),
              rs.getString("dimensiones"),
              rs.getInt("stock"),
              UUID.fromString(rs.getString("id"))));
        }
        return Optional.empty();
      }
    }
    catch(SQLException err){
      System.err.println("Error al buscar Material por ID: " + err.getMessage());
      throw new RuntimeException();
    }
  }

  public boolean create(Materiales m){
    String sql = "INSERT INTO materiales (id, nombre, precio_compra, merma, precio_costo, dimensiones, stock) VALUES (?, ?, ?, ?, ?, ?, ?)";

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

  public boolean update(Materiales m){
    String sql = "UPDATE materiales SET nombre = ?, precio_compra = ?, merma = ?, precio_costo = ?, dimensiones = ?, stock = ? WHERE id = ?";

    try (Connection conn = dbConfig.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

      ps.setString(1, m.getNombre());
      ps.setInt(2, m.getPrecioCompra());
      ps.setInt(3, m.getMerma());
      ps.setInt(4, m.getPrecioCosto());
      ps.setString(5, m.getDimensiones());
      ps.setInt(6, m.getStock());
      ps.setString(7, m.getId().toString());
      return ps.executeUpdate() > 0;
    }
    catch(SQLException err){
      System.err.println("Error al actualizar Material: " + err.getMessage());
      throw new RuntimeException();
    }
  }

  
  public boolean delete(UUID id) throws SQLException {
    String sql = "DELETE FROM materiales WHERE id = ?";

    try (Connection conn = dbConfig.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

      ps.setString(1, id.toString());
      return ps.executeUpdate() > 0;
    }
    catch(SQLException err){
      System.err.println("Error al eliminar Material: " + err.getMessage());
      throw new RuntimeException();
    }
  }
}