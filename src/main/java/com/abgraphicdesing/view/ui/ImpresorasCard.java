package com.abgraphicdesing.view.ui;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class ImpresorasCard extends HBox{
  public ImpresorasCard(){
    HBox cardContainer = new HBox();
    Label titulo = new Label("Titulo");
    Label modelo = new Label("Modelo");
    Label tipo = new Label("tipo: ");
    Label costoHora = new Label("Costo Hora: ");

    cardContainer.getChildren().addAll(
      titulo,
      modelo,
      tipo,
      costoHora
    );    

    getChildren().add(cardContainer);

  }
}
