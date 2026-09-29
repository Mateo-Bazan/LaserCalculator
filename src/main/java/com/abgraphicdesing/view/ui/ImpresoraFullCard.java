package com.abgraphicdesing.view.ui;

import com.abgraphicdesing.model.dto.ImpresorasResDto;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class ImpresoraFullCard extends VBox{
  public ImpresoraFullCard(ImpresorasResDto dto){

  
    StackPane cardContainer = new StackPane();
    VBox contentContainer = new VBox();
    VBox firstDataContainer = new VBox();
    HBox modeloContainer = new HBox();
    HBox tipoContainer = new HBox();
  
    HBox separatorLine = new HBox();

    VBox dataContainer = new VBox();
    HBox vidaUtilContainer = new HBox();
    HBox precioRepuestoContainer = new HBox();
    HBox precioCompraContainer = new HBox();
    HBox amortizacionContainer = new HBox();
    HBox gastoEnergeticoContainer = new HBox();
  


    Label marca = new Label(dto.marca());
    Label modelo = new Label(dto.modelo());
  
    Label tipoTitle = new Label("Tipo: ");
    Label fetchTipo = new Label(dto.tipo());
  
    Label vidaUtilTitle = new Label("Vida util: ");
    Label fetchVidaUtil = new Label(String.valueOf(dto.vidaUtil()) +  "hs");
  
    Label precioRepuestoTitle = new Label("Precio Repuesto: ");
    Label fetchPrecioRepuesto = new Label("$ " + String.valueOf(dto.precioRespuesto()));
  
    Label precioCompraTitle = new Label("Precio Compra: ");
    Label fetchPrecioCompra = new Label("$ " + String.valueOf(dto.precioCompra()));
  
    Label amortizacionTitle = new Label("Amortiazacion: ");
    Label fetchAmortizacion = new Label("$ " + String.valueOf(dto.amortizacion()));
  
    Label gastoEnergeticoTitle = new Label("Gasto energetico: ");
    Label fetchGastoEnergetico = new Label(String.valueOf(dto.watts()) + "W");
    
    tipoContainer.getChildren().addAll(
      tipoTitle,
      fetchTipo
    );

    vidaUtilContainer.getChildren().addAll(
      vidaUtilTitle,
      fetchVidaUtil
    );

    precioRepuestoContainer.getChildren().addAll(
      precioRepuestoTitle,
      fetchPrecioRepuesto
    );

    precioCompraContainer.getChildren().addAll(
      precioCompraTitle,
      fetchPrecioCompra
    );

    amortizacionContainer.getChildren().addAll(
      amortizacionTitle,
      fetchAmortizacion
    );

    gastoEnergeticoContainer.getChildren().addAll(
      gastoEnergeticoTitle,
      fetchGastoEnergetico
    );

    separatorLine.getStyleClass().add("separator-line");
    VBox.setMargin(separatorLine, new Insets(10, 0, 10, 0));

    dataContainer.getStyleClass().add("rowDataContainerFullCard");
    dataContainer.getChildren().addAll(
      vidaUtilContainer,
      precioRepuestoContainer,
      precioCompraContainer,
      amortizacionContainer,
      gastoEnergeticoContainer
    );

    marca.getStyleClass().add("marcaTituloFullCard");

    modelo.getStyleClass().add("modeloTituloFullCard");
    modeloContainer.getStyleClass().add("modeloTituloContainerFC");
    modeloContainer.getChildren().add(modelo);
    
    
    firstDataContainer.getStyleClass().add("firstDataContainer");
    firstDataContainer.getChildren().addAll(
      modeloContainer,
      tipoContainer
    );

    contentContainer.getChildren().addAll(
      firstDataContainer,
      separatorLine,
      dataContainer
    );

    cardContainer.setMinWidth(100);
    cardContainer.setMinHeight(200);
    cardContainer.getStyleClass().add("impresoraFullCard");
    cardContainer.getChildren().addAll(contentContainer, marca);
    StackPane.setAlignment(marca, Pos.TOP_LEFT);
    getChildren().add(
      cardContainer
    );
  }
}
