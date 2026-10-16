package com.abgraphicdesing.view;

import com.abgraphicdesing.controller.ImpresorasController;
import com.abgraphicdesing.view.ui.ImpresorasResumeCard;
import com.abgraphicdesing.model.dto.ImpresorasResDto;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class CalcualdoraView extends HBox{
  private final ImpresorasController controller;

  public CalcualdoraView(ImpresorasController controller){  
    this.controller = controller;
    getStyleClass().add("app-background");
    setPadding(new Insets(28, 36, 28, 36));
    setSpacing(12);

    VBox leftSide = new VBox(10);
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
            ImpresorasResumeCard card = new ImpresorasResumeCard(impresora);
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
