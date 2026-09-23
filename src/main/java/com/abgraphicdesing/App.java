package com.abgraphicdesing;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.abgraphicdesing.database.DbConfig;
import com.abgraphicdesing.database.InitializerDatabase;
import com.abgraphicdesing.view.MainView;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class App extends Application{
  public static void main( String[] args ){
    System.out.println( "Hello World!" );
    DbConfig dbConfig = new DbConfig();
    InitializerDatabase initializer = new InitializerDatabase(dbConfig);
    
    try{
      initializer.initializer();
    } catch(Exception e){
      e.printStackTrace();
    }
    
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

    launch(args);
  }

  @Override
  public void start(Stage primaryStage){
    MainView mainView = new MainView();
    Scene scene = new Scene(mainView, 1200, 800);

    scene.getStylesheets().add(
      getClass().getResource("/css/styles.css").toExternalForm()
    );

    primaryStage.setTitle("Ejemplo inicializador");
    primaryStage.setScene(scene);
    primaryStage.show();
  }
}
