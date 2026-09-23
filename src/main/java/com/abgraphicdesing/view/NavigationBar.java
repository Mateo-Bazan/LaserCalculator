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
  public NavigationBar(){
    HBox buttonsContainer = new HBox();
    Label label = new Label("AB Graphic Desing");
    Image image = new Image(
      getClass().getResourceAsStream("/assets/iconoAbGD.png")
    );
    ImageView icon = new ImageView(image);
    icon.setFitHeight(25);
    icon.setFitWidth(25);

    HBox item = new HBox(8);
    item.getChildren().addAll(icon, label);
    item.setAlignment(Pos.CENTER);


    Button inicio = new Button("Calculadora");
    Button impresoras = new Button("Impresoras");
    Button materiales = new Button("Materiales");
    Button ajustes = new Button("Ajustes");
    Button historial = new Button("Historial");
    
    getStyleClass().add("navigation-bar");

    buttonsContainer.getChildren().addAll(inicio, impresoras, materiales, ajustes, historial);
    buttonsContainer.setAlignment(Pos.CENTER);
    buttonsContainer.getStyleClass().add("nav-button");

    Region spacer = new Region();
    HBox.setHgrow(spacer, Priority.ALWAYS);

    this.setAlignment(Pos.CENTER_LEFT);

    getChildren().addAll(item, spacer, buttonsContainer);
  }
}
