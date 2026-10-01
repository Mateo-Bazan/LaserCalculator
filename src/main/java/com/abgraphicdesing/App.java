package com.abgraphicdesing;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.abgraphicdesing.controller.ImpresorasController;
import com.abgraphicdesing.database.DbConfig;
import com.abgraphicdesing.database.InitializerDatabase;
import com.abgraphicdesing.repository.ImpresorasRepository;
import com.abgraphicdesing.service.ImpresorasService;
import com.abgraphicdesing.view.MainView;
import com.abgraphicdesing.scripts.SeederScript;

import com.abgraphicdesing.controller.MaterialesController;
import com.abgraphicdesing.service.MaterialesService;
import com.abgraphicdesing.repository.MaterialesRepository;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application{
  public static void main( String[] args ){
    DbConfig dbConfig = new DbConfig();
    InitializerDatabase initializer = new InitializerDatabase(dbConfig);

    // SeederScript seederExe = new SeederScript(dbConfig);
    // seederExe.update();

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
    DbConfig configDb = new DbConfig();
    ImpresorasRepository impRepository = new ImpresorasRepository(configDb);
    ImpresorasService impService = new ImpresorasService(impRepository);
    ImpresorasController impController = new ImpresorasController(impService);

    MaterialesRepository matRepository = new MaterialesRepository(configDb);
    MaterialesService matService = new MaterialesService(matRepository);
    MaterialesController matController = new MaterialesController(matService);



    MainView mainView = new MainView(impController, matController);
    Scene scene = new Scene(mainView, 1200, 800);


    scene.getStylesheets().clear();
    scene.getStylesheets().add(
      getClass().getResource("/css/styles.css").toExternalForm()
    );

    primaryStage.setTitle("AB Graphic Desing App");
    primaryStage.setScene(scene);
    primaryStage.show();
  }
}
