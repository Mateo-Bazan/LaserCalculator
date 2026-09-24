package com.abgraphicdesing.view;

import com.abgraphicdesing.view.ui.ImpresorasCard;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class ImpresorasView extends HBox{
  public ImpresorasView(){  
    Label label = new Label("Impresoras view");
    ImpresorasCard imprCard = new ImpresorasCard();
    getChildren().addAll(imprCard, label);

  }
}
