package com.abgraphicdesing.view;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class ImpresorasView extends HBox{
  public ImpresorasView(){  
    Label label = new Label("Impresoras view");
    getChildren().addAll(label);
  }
}
