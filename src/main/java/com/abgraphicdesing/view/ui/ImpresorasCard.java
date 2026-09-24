package com.abgraphicdesing.view.ui;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ImpresorasCard extends VBox{
  public ImpresorasCard(){
    VBox cardContainer = new VBox(8);

    HBox titlesContainer = new HBox();
    HBox responseContainer = new HBox();

    Label titulo = new Label("Titulo");
    Label modelo = new Label("Modelo");
    Label tipo = new Label("tipo: ");
    Label costoHora = new Label("Costo Hora: ");

    Label tituloRes = new Label("123");
    Label modeloRes = new Label("abc");
    Label tipoRes = new Label("Impresora diodo ");
    Label costoHoraRes = new Label("830");

    titlesContainer.setAlignment(Pos.CENTER_LEFT);
    titlesContainer.setSpacing(10);

    titlesContainer.setAlignment(Pos.CENTER_RIGHT);
    responseContainer.setSpacing(10);

    cardContainer.setAlignment(Pos.CENTER);
    


    responseContainer.getChildren().addAll(
      tituloRes,
      modeloRes,
      tipoRes,
      costoHoraRes
    );


    titlesContainer.getChildren().addAll(
      titulo,
      modelo,
      tipo,
      costoHora
    );    

    cardContainer.getChildren().addAll(titlesContainer, responseContainer);

    getChildren().add(cardContainer);

  }
}
