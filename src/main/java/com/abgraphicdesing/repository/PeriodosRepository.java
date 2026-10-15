package com.abgraphicdesing.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.abgraphicdesing.database.DbConfig;
import com.abgraphicdesing.model.entity.Periodos;

public class PeriodosRepository{
  private final DbConfig dbConfig;

  public PeriodosRepository(DbConfig dbConfig) {
    this.dbConfig = dbConfig;
  }

  public List<Periodos> getAll() {
    String sql = "Select * FROM periodo";
    List<Periodos> listaPeriodos = new ArrayList<>();

    try(Connection conn = dbConfig.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()){
      while(rs.next()) {
        listaPeriodos.add(new Periodos(
          rs.getInt("id"),
          rs.getString("periodo")
        ));
      }
      return listaPeriodos;
    }
    catch(SQLException err){
      System.err.println("Error al leer Periodos: "+ err.getMessage());
      throw new RuntimeException();
    }
  }
}
