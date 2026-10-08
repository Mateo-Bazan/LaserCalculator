package com.abgraphicdesing.view;

import com.abgraphicdesing.controller.AjustesController;
import com.abgraphicdesing.controller.ImpresorasController;
import com.abgraphicdesing.controller.MaterialesController;
import com.abgraphicdesing.controller.PeriodosController;

import javafx.scene.layout.BorderPane;

public class MainView extends BorderPane{
  private final ImpresorasController impresoraController;
  private final MaterialesController materialesController;
  private final AjustesController ajustesController;
  private final PeriodosController periodosController;

  public MainView(
    ImpresorasController controller, 
    MaterialesController materialController,
    AjustesController ajustesController,
    PeriodosController periodosController
  ){
    this.impresoraController = controller;
    this.materialesController = materialController;
    this.ajustesController = ajustesController;
    this.periodosController = periodosController;

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

    navigationBar.setOnAjustes(
      () -> setCenter(new AjustesView(this.ajustesController, this.periodosController))
    );
    
  }
}
