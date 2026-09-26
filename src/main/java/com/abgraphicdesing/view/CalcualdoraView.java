package com.abgraphicdesing.view;

import com.abgraphicdesing.view.ui.ImpresorasCard;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class CalcualdoraView extends HBox{
  public CalcualdoraView(){  
    VBox leftSide = new VBox();
    Label impresorasTitulos = new Label("Impresoras");

    ImpresorasCard imprCard = new ImpresorasCard();

    leftSide.getChildren().addAll(
      impresorasTitulos, 
      imprCard
    );

    impresorasTitulos.getStyleClass().add("leftSideTitle");

    leftSide.setAlignment(Pos.TOP_CENTER);    
    leftSide.getStyleClass().add("imprCardContainer");

    getChildren().addAll(leftSide);

  }
}
