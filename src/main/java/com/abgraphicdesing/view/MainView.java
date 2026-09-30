package com.abgraphicdesing.view;

import com.abgraphicdesing.controller.ImpresorasController;
import com.abgraphicdesing.controller.MaterialesController;

import javafx.scene.layout.BorderPane;

public class MainView extends BorderPane{
  private final ImpresorasController impresoraController;
  private final MaterialesController materialesController;

  public MainView(ImpresorasController controller, MaterialesController materialController){
    this.impresoraController = controller;
    this.materialesController = materialController;

    NavigationBar navigationBar = new NavigationBar();

    setTop(navigationBar);


    navigationBar.setOnCalculadora(
      () -> setCenter(new CalcualdoraView(impresoraController))
    );

    navigationBar.setOnImpresora(
      () -> setCenter(new ImpresorasView(impresoraController))
    );

    navigationBar.setOnMateriales(
      () -> setCenter(new MaterialesView(materialesController))
    );
    
  }
}
