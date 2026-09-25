package com.abgraphicdesing.view.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class ImpresorasCard extends VBox{
  public ImpresorasCard(){
    VBox cardContainer = new VBox(8);

    HBox marcaContainer = new HBox();
    HBox modeloContainer = new HBox();
    VBox dataContainer = new VBox(); 
    HBox rowImpresoraTipo = new HBox();
    HBox rowCostoHora = new HBox();

    Region spacingTipo = new Region();
    HBox.setHgrow(spacingTipo, Priority.ALWAYS);
    Region spacingCosto = new Region();
    HBox.setHgrow(spacingCosto, Priority.ALWAYS);

    Label marca = new Label("Marca que llega");
    Label modelo = new Label("Modelo");
    Label impresoraTipo = new Label("tipo: ");
    Label impresoraTipoGet = new Label("Tipo que llega");
    
    Label costoHora = new Label("Costo hora: ");
    Label costoHoraGet = new Label("Costo por hora en la db");

    rowImpresoraTipo.setSpacing(10);
    rowCostoHora.setSpacing(10);


    impresoraTipo.getStyleClass().add("presentationLabel");
    costoHora.getStyleClass().add("presentationLabel");

    impresoraTipoGet.getStyleClass().add("dataLabel");
    costoHoraGet.getStyleClass().add("dataLabel");

    rowImpresoraTipo.getChildren().addAll(
      impresoraTipo,
      spacingTipo,
      impresoraTipoGet
    );

    rowCostoHora.getChildren().addAll(
      costoHora,
      spacingCosto,
      costoHoraGet
    );

    marcaContainer.getChildren().add(marca);
    marcaContainer.getStyleClass().add("marcaContainerTitle");
    marca.getStyleClass().add("marcaTitle");

    modeloContainer.getChildren().add(modelo);
    modelo.getStyleClass().add("modeloTitle");


    dataContainer.getChildren().addAll(
      rowImpresoraTipo,
      rowCostoHora
    );

    dataContainer.getStyleClass().add("dataContainer");
    cardContainer.setPadding(new Insets(10));
    cardContainer.setAlignment(Pos.CENTER);
    cardContainer.getStyleClass().add("dataContainerCard");
    cardContainer.getChildren().addAll(
      marcaContainer,
      modeloContainer,
      dataContainer
    );

    getChildren().add(cardContainer);
  }
}
