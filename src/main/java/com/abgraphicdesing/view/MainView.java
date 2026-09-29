package com.abgraphicdesing.view;

import com.abgraphicdesing.controller.ImpresorasController;

import javafx.scene.layout.BorderPane;

public class MainView extends BorderPane{
  private final ImpresorasController impresoraController;
  public MainView(ImpresorasController controller){
    this.impresoraController = controller;

    NavigationBar navigationBar = new NavigationBar();

    setTop(navigationBar);


    navigationBar.setOnCalculadora(
      () -> setCenter(new CalcualdoraView(impresoraController))
    );

    navigationBar.setOnImpresora(
      () -> setCenter(new ImpresorasView(impresoraController))
    );
    
  }
}
