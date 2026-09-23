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
import com.abgraphicdesing.model.entity.Impresoras;

public class ImpresorasRepository{
  private final DbConfig dbConfig;

  public ImpresorasRepository(DbConfig dbConfig){
    this.dbConfig = dbConfig;
   }
  
  public boolean save(Impresoras impresora){
    String querySaveImpresora = """
      INSERT INTO impresoras(id, id_tipo, marca, modelo, titulo, vida_util, watts, precio_compra, precio_respuesto, amortizacion)
      VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)    
    """;
    try(
      Connection conn = dbConfig.getConnection();
      PreparedStatement pstmt = conn.prepareStatement(querySaveImpresora)
    ){
      pstmt.setObject(1, impresora.getId().toString());
      pstmt.setObject(2, impresora.getIdTipo().toString());
      pstmt.setObject(3, impresora.getMarca());
      pstmt.setObject(4, impresora.getModelo());
      pstmt.setObject(5, impresora.getTitulo());
      pstmt.setObject(6, impresora.getVidaUtil());
      pstmt.setObject(7, impresora.getWatts());
      pstmt.setObject(8, impresora.getPrecioCompra());
      pstmt.setObject(9, impresora.getPrecioRespuesto());
      pstmt.setObject(10, impresora.getAmortizacion());

      int rowAffected = pstmt.executeUpdate();

      return rowAffected > 0;
    }catch(SQLException e){
      System.err.println("Error al crear impresora: " + e.getMessage());
      throw new RuntimeException("Error al crear impresora: " + e.getMessage());
    }
  }

  public boolean update(Impresoras impresora){
    String queryUpdateImpresora = """
      UPDATE impresoras
      SET 
        id_tipo = ?,
        marca = ?,
        modelo = ?,
        titulo = ?,
        vida_util = ?,
        watts = ?,
        precio_compra = ?,
        precio_respuesto = ?,
        amortizacion = ?
      WHERE id = ?
    """;

    try(
      Connection conn = dbConfig.getConnection();
      PreparedStatement pstmt = conn.prepareStatement(queryUpdateImpresora)
    ){
      pstmt.setObject(1, impresora.getIdTipo());
      pstmt.setObject(2, impresora.getMarca());
      pstmt.setObject(3, impresora.getModelo());
      pstmt.setObject(4, impresora.getTitulo());
      pstmt.setObject(5, impresora.getVidaUtil());
      pstmt.setObject(6, impresora.getWatts());
      pstmt.setObject(7, impresora.getPrecioCompra());
      pstmt.setObject(8, impresora.getPrecioRespuesto());
      pstmt.setObject(9, impresora.getAmortizacion());
      pstmt.setObject(10, impresora.getId());

      int rowAffected = pstmt.executeUpdate();
      return rowAffected > 0;
    } catch (SQLException e) {
      System.err.println("Error al actualizar impresora: " + e.getMessage());
      throw new RuntimeException("Error al actualizar impresora: " + e.getMessage());
    }
  }

  public Optional<Impresoras> findById(UUID id){
    String queryGetOne = """
      SELECT * FROM impresoras
      WHERE id = ?
    """;
    try(
      Connection conn = dbConfig.getConnection();
      PreparedStatement pstmt = conn.prepareStatement(queryGetOne);
    ){
      pstmt.setObject(1, id.toString());
      ResultSet rs = pstmt.executeQuery();
      
      if(rs.next()){
        return Optional.of(new Impresoras(
          rs.getObject("tipo_id", UUID.class),
          rs.getString("marca"),
          rs.getString("modelo"),
          rs.getString("titulo"),
          rs.getInt("vida_util"),
          rs.getInt("watts"),
          rs.getFloat("precio_compra"),
          rs.getFloat("precio_respuesto"),
          rs.getObject("id", UUID.class)
        ));
      }
      return Optional.empty();
    }catch(SQLException e){
      System.err.println("Error al obtener la impresora: " + e.getMessage());
      throw new RuntimeException("Error al obtener la impresora: " + e.getMessage());
    }
  }

  public List<Impresoras> getAll(){
    String queryGetAll = """
      SELECT * FROM impresoras
    """;
    List<Impresoras> impresoras = new ArrayList<>();
    try(
      Connection conn = dbConfig.getConnection();
      PreparedStatement pstmt = conn.prepareStatement(queryGetAll);
      ResultSet rs = pstmt.executeQuery();
    ){
      while(rs.next()) {
        UUID tipoId = rs.getObject("tipo_id", UUID.class);
        String marca = rs.getString("marca");
        String modelo = rs.getString("modelo");
        String titulo = rs.getString("titulo");
        int vidaUtil = rs.getInt("vida_util");
        int watts = rs.getInt("watts");
        float precioCompra = rs.getFloat("precio_compra");
        float precioPresupuesto = rs.getFloat("precio_respuesto");
        UUID id = rs.getObject("id", UUID.class);
        
        Impresoras impresora = new Impresoras(tipoId, marca, modelo, titulo, vidaUtil, watts, precioCompra, precioPresupuesto, id);
        impresoras.add(impresora);
      }
      return impresoras;
    }catch(SQLException e) {
      System.err.println("Error al obtener las impresoras: " + e.getMessage());
      throw new RuntimeException("Error al obtener las impresoras: " + e.getMessage());
    }
  }
}