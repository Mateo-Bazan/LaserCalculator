package com.abgraphicdesing;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.abgraphicdesing.database.DbConfig;
import com.abgraphicdesing.database.InitializerDatabase;

public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        DbConfig dbConfig = new DbConfig();
        InitializerDatabase initializer = new InitializerDatabase(dbConfig);

        try{
          initializer.initializer();
        } catch(Exception e){
          e.printStackTrace();
        }
        System.out.println();

        try(Connection conn = dbConfig.getConnection();
          PreparedStatement stmt = conn.prepareStatement("Select count(*) from materiales");
          ResultSet result = stmt.executeQuery()
        ){
          if(result.next()){
            System.out.println("Materiales: " + result.getInt(1));
          }
        }catch(SQLException e){
          System.out.println(e.getMessage());
        }
    }
}
