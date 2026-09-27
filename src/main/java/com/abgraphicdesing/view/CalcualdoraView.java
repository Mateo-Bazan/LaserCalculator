package com.abgraphicdesing.view;

import com.abgraphicdesing.controller.ImpresorasController;
import com.abgraphicdesing.view.ui.ImpresorasCard;
import com.abgraphicdesing.model.dto.ImpresorasResDto;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class CalcualdoraView extends HBox{
  private final ImpresorasController controller;

  public CalcualdoraView(ImpresorasController controller){  
    this.controller = controller;
    VBox leftSide = new VBox();
    Label impresorasTitulos = new Label("Impresoras");


    leftSide.getChildren().addAll(
      impresorasTitulos 
    );

    impresorasTitulos.getStyleClass().add("leftSideTitle");

    leftSide.setAlignment(Pos.TOP_CENTER);    
    leftSide.getStyleClass().add("imprCardContainer");

    getChildren().addAll(leftSide);
    cargarImpresoras(leftSide);
  }

  private void cargarImpresoras(VBox container){
    controller.getAll(
      impresoras ->{
        if(impresoras != null && !impresoras.isEmpty()){
          for(ImpresorasResDto impresora : impresoras){
            ImpresorasCard card = new ImpresorasCard(impresora);
  
            container.getChildren().add(card);
          }
        }else{
          Label label = new Label("No hay impresoras");
          container.getChildren().add(label);
        }
      }, 
      error ->{
        System.out.println("Error cargando impresoras: " + error.getMessage());
      });
  }

}
