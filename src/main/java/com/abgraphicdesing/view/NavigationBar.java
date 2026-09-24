package com.abgraphicdesing.view;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

public class NavigationBar extends HBox{
  private final Button calculadoraButton;
  private final Button impresorasButton;
  private final Button materialesButton;
  private final Button ajustesButton;
  private final Button historialButton;

  public NavigationBar(){
    HBox buttonsContainer = new HBox();
    Label label = new Label("AB Graphic Desing");
    Image image = new Image(
      getClass().getResourceAsStream("/assets/iconoAbGD.png")
    );
    ImageView icon = new ImageView(image);
    icon.setFitHeight(40);
    icon.setFitWidth(40);

    HBox item = new HBox(8);
    item.getChildren().addAll(icon, label);
    item.setAlignment(Pos.CENTER);


    calculadoraButton = new Button("Calculadora");
    impresorasButton = new Button("Impresoras");
    materialesButton = new Button("Materiales");
    ajustesButton = new Button("Ajustes");
    historialButton = new Button("Historial");
    
    getStyleClass().add("navigation-bar");

    buttonsContainer.getChildren().addAll(
      calculadoraButton, 
      impresorasButton, 
      materialesButton, 
      ajustesButton, 
      historialButton
    );

    buttonsContainer.setAlignment(Pos.CENTER);
    buttonsContainer.getStyleClass().add("nav-button");

    Region spacer = new Region();
    HBox.setHgrow(spacer, Priority.ALWAYS);

    this.setAlignment(Pos.CENTER_LEFT);

    getChildren().addAll(item, spacer, buttonsContainer);
  }

  public void setOnCalculadora(Runnable action){
    calculadoraButton.setOnAction(event -> action.run());
  }

  public void setOnImpresora(Runnable action){
    impresorasButton.setOnAction(event -> action.run());
  }

  public void setOnMateriales(Runnable action){
    materialesButton.setOnAction(event -> action.run());
  }

  public void setOnAjustes(Runnable action){
    ajustesButton.setOnAction(event -> action.run());
  }

  public void setOnHistorial(Runnable action){
    historialButton.setOnAction(event -> action.run());
  }

}
