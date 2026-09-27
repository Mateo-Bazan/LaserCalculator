package com.abgraphicdesing.scripts;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.abgraphicdesing.database.DbConfig;

public class SeederScript {
  private final DbConfig dbConfig;

  public  SeederScript(DbConfig db){
    this.dbConfig = db;
  }

  public void loadSeeder(){
    // String sqlLoad = """
    //   INSERT INTO tipo(tipo)
    //   VALUES (?)
    //   """;

    String sqlGet = """
      SELECT * FROM impresoras
    """;

    try(
      Connection conn = dbConfig.getConnection();
      //PreparedStatement pstmt = conn.prepareStatement(sqlLoad);
      PreparedStatement pstmt2 = conn.prepareStatement(sqlGet);
    ){
      //pstmt.setObject(1, "Diodo");
      //pstmt.executeUpdate();
      ResultSet rs = pstmt2.executeQuery();
      System.out.println(rs.getObject("id"));
      System.out.println("Se hizo las queries ");
    }catch(SQLException e){
      throw new RuntimeException("error en el seeder: " + e.getMessage());
    }
  }

  public void createImpresora(){
    String querySaveImpresora = """
      INSERT INTO impresoras(id, id_tipo, marca, modelo, titulo, vida_util, watts, precio_compra, precio_respuesto, amortizacion)
      VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)    
    """;
    try(
      Connection conn = dbConfig.getConnection();
      PreparedStatement pstmt = conn.prepareStatement(querySaveImpresora);
    ){
      pstmt.setObject(1, "");
      pstmt.setObject(2, "1");
      pstmt.setObject(3, "Algolaser");
      pstmt.setObject(4, "Alpha Diodo");
      pstmt.setObject(5, "Impresora favorita");
      pstmt.setObject(6, 1000);
      pstmt.setObject(7, 114);
      pstmt.setObject(8, 200);
      pstmt.setObject(9, 170);
      pstmt.setObject(10, 10);

      int rowAffected = pstmt.executeUpdate();
      System.out.println("Filas afectadas: " + rowAffected);
    } catch (SQLException e) {
      throw new RuntimeException("Error en el create: " + e.getMessage());
    }
  }

  public void update(){
    try(
      Connection conn = dbConfig.getConnection();
      PreparedStatement pstmt = conn.prepareStatement("UPDATE impresoras SET id = ? WHERE id = ?");
    ){
      pstmt.setObject(1, "1af1eb30-6e2b-4b5a-8c95-bc8f10896e9a");
      pstmt.setObject(2, "some-uuid");
      int rowAffected = pstmt.executeUpdate();
      System.out.println("Filas afectadas: " + rowAffected);
    } catch (SQLException e) {
      throw new RuntimeException("Error en el update: " + e.getMessage());
    }
  }
}
