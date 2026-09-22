package com.abgraphicdesing.database;

import java.sql.Statement;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;

public class InitializerDatabase{
  private final DbConfig dbConfig;

  public InitializerDatabase(DbConfig dbConfig){
    this.dbConfig = dbConfig;
  }

  public void initializer() throws Exception{
    try(Connection conn = dbConfig.getConnection()){
      executeSchema(conn);
    }catch(Exception e){
      System.out.println(e.getMessage());
    }
  }

  private void executeSchema(Connection conn) throws Exception{
    try(InputStream input = getClass()
      .getClassLoader()
      .getResourceAsStream("db/V1__init_database.sql")
    ){
      if(input == null){
        throw new IllegalStateException("No se encontró el la migración");
      }

      String sql = new String(
        input.readAllBytes(),
        StandardCharsets.UTF_8
      );

      try(Statement statement = conn.createStatement()){
        for(String query : sql.split(";")){
          if(!query.isBlank()){
            statement.execute(query.trim());
          }
        }
      }
    }
  }
}
