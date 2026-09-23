package com.abgraphicdesing.view;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class NavigationBar extends HBox{
  public NavigationBar(){

    HBox buttonsContainer = new HBox();
    Label label = new Label("AB Graphic Desing");
    Image image = new Image(
      getClass().getResourceAsStream("/assets/iconoAbGD.png")
    );
    ImageView icon = new ImageView(image);
    icon.setFitHeight(10);
    icon.setFitWidth(10);

    HBox item = new HBox(8);
    item.getChildren().addAll(icon, label);
    
    Button inicio = new Button("Calculadora");
    Button impresoras = new Button("Impresoras");
    Button materiales = new Button("Materiales");
    Button ajustes = new Button("Ajustes");
    Button historial = new Button("Historial");
    
    getStyleClass().add("navigation-bar");
    inicio.getStyleClass().add("nav-button");

    buttonsContainer.getChildren().addAll(inicio, impresoras, materiales, ajustes, historial);

    getChildren().addAll(item, buttonsContainer);
  }
}
