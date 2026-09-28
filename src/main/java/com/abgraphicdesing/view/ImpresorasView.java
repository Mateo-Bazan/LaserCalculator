package com.abgraphicdesing.view;

import com.abgraphicdesing.controller.ImpresorasController;
import com.abgraphicdesing.model.dto.ImpresorasResDto;
import com.abgraphicdesing.view.ui.ImpresoraFullCard;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;


public class ImpresorasView extends HBox {
  private final ImpresorasController controller;

  public ImpresorasView(ImpresorasController controller){
    this.controller = controller;
    VBox container = new VBox();

    HBox presentationContainer = new HBox();
    VBox presentationcontentImpresoras = new VBox();
    Label presentationTitle = new Label("Mis Impresoras");
    Label presentationSubTitle = new Label("Catalogo de impresoras y sus configuraciones.");
    Region spacingPresentation = new Region();
    Button buttonAddImpresora = new Button("+");
    HBox separatorLine = new HBox();

    //Boolean showForm = false;
  
    HBox contentImpresoras = new HBox();
    contentImpresoras.setAlignment(Pos.CENTER);
    container.setMaxWidth(Double.MAX_VALUE);    

    contentImpresoras.getStyleClass().add("imprViewContentImpresora");
    contentImpresoras.setMaxWidth(Double.MAX_VALUE);
    contentImpresoras.setAlignment(Pos.CENTER);
    VBox.setVgrow(contentImpresoras, Priority.ALWAYS);

    HBox.setHgrow(container, Priority.ALWAYS);
    HBox.setHgrow(spacingPresentation, Priority.ALWAYS);


    presentationTitle.getStyleClass().add("imprViewTitle");
    presentationSubTitle.getStyleClass().add("imprViewSubtitle");

    presentationcontentImpresoras.getChildren().addAll(
      presentationTitle,
      presentationSubTitle
    );
    
    
    presentationContainer.setAlignment(Pos.CENTER_LEFT);
    presentationContainer.getChildren().addAll(
      presentationcontentImpresoras, 
      spacingPresentation,
      buttonAddImpresora
    );

    VBox.setMargin(separatorLine, new Insets(5, 0, 5, 0));
    separatorLine.getStyleClass().add("separator-line-impView");

    container.getChildren().addAll(presentationContainer, separatorLine, contentImpresoras);
    container.getStyleClass().add("imprViewContainer");


    getChildren().add(container);
    cargarImpresoras(contentImpresoras);
  }

  private void cargarImpresoras(HBox container){
  controller.getAll(
    impresoras ->{
      if(impresoras != null && !impresoras.isEmpty()){
        for(ImpresorasResDto impresora : impresoras){
          ImpresoraFullCard card = new ImpresoraFullCard(impresora);
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