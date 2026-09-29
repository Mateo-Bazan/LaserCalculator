package com.abgraphicdesing.view;

import javafx.scene.layout.BorderPane;

public class MainView extends BorderPane{
  public MainView(){
    NavigationBar navigationBar = new NavigationBar();

    setTop(navigationBar);


    navigationBar.setOnImpresora(
      () -> setCenter(new CalcualdoraView())
    );

  }
}
