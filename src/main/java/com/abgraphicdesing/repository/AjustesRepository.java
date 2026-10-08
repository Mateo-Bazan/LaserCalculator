package com.abgraphicdesing.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.abgraphicdesing.database.DbConfig;
import com.abgraphicdesing.model.entity.Ajustes;

public class AjustesRepository {
  private final DbConfig dbConfig;

  public AjustesRepository (DbConfig dbConfig){
    this.dbConfig = dbConfig;
  }

  public List<Ajustes> getAll(){
    String sql = """
      SELECT A.*, B.periodo 
      FROM ajustes AS A 
      JOIN periodo AS B 
      ON A.id_periodo = B.id 
      ORDER BY nombre """;

    List<Ajustes> listaAjustes = new ArrayList<>();

    try (Connection conn = dbConfig.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
      while (rs.next()) {
        listaAjustes.add(new Ajustes(
            rs.getString("nombre"),
            rs.getInt("gasto"),
            rs.getInt("id_periodo"),
            rs.getString("periodo"),
            rs.getInt("id")
        ));
      }
      return listaAjustes;
    }
    catch(SQLException err){
      System.err.println("Error al leer Ajustes: " + err.getMessage());
      throw new RuntimeException();
    }
  }

  public Optional<Ajustes> findById(int id){
    String sql = """
      SELECT A.*, B.periodo
      FROM ajustes AS A
      JOIN periodo AS B ON A.id_periodo = B.id
      WHERE A.id = ?
      """;
    
    try (Connection conn = dbConfig.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setInt(1, id);

      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return Optional.of(new Ajustes(
            rs.getString("nombre"),
            rs.getInt("gasto"),
            rs.getInt("id_periodo"),
            rs.getString("periodo"),
            rs.getInt("id")
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
    String sql = "INSERT INTO ajustes (nombre, gasto, id_periodo) VALUES (?, ?, ?)";

    try (
      Connection conn = dbConfig.getConnection(); 
      PreparedStatement ps = conn.prepareStatement(sql)
    ) {
      ps.setString(1, a.getNombre());
      ps.setInt(2, a.getGasto());
      ps.setInt(3, a.getIdPeriodo());
      return ps.executeUpdate() > 0;
    }
    catch(SQLException err){
      System.err.println("Error al crear Ajuste: " + err.getMessage());
      throw new RuntimeException();
    }
  }

  public boolean update(Ajustes a){
    String sql = "UPDATE ajustes SET nombre = ?, gasto = ?, id_periodo = ? WHERE id = ?";

    try(
      Connection conn = dbConfig.getConnection();
      PreparedStatement ps = conn.prepareStatement(sql)
    ){
      ps.setString(1, a.getNombre());
      ps.setInt(2, a.getGasto());
      ps.setInt(3, a.getIdPeriodo());
      ps.setInt(4, a.getId());

      return ps.executeUpdate() > 0;

    } catch (SQLException e) {
      System.err.println("Error al actualizar Ajustes: " + e.getMessage());
      throw new RuntimeException("Error al actualizar Ajustes: " + e.getMessage());
    }
  }

  public boolean delete(int id){
    String sql = " DELETE FROM ajustes WHERE id = ?";
    try(
      Connection conn = dbConfig.getConnection();
      PreparedStatement pstmt = conn.prepareStatement(sql);
    ){
      pstmt.setInt(1, id);
      return pstmt.executeUpdate() > 0;
    }catch(SQLException e){
      System.err.println("Error al eliminar Ajustes: " + e.getMessage());
      throw new RuntimeException("Error al eliminar Ajustes: " + e.getMessage());
    }
  }
}
