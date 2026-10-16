package com.abgraphicdesing.view;

import com.abgraphicdesing.controller.ImpresorasController;
import com.abgraphicdesing.model.dto.ImpresorasResDto;
import com.abgraphicdesing.view.ui.ImpresoraFormDialog;
import com.abgraphicdesing.view.ui.ImpresoraFullCard;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;


public class ImpresorasView extends HBox {
  private final ImpresorasController controller;
  private final HBox contentImpresoras = new HBox();

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

    buttonAddImpresora.setOnAction(e -> onAgregar());

    //Boolean showForm = false;
    
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
          ImpresoraFullCard card = new ImpresoraFullCard(
            impresora,
            this::onEditar,
            this::onEliminar
          );
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

  private void onAgregar(){
    new ImpresoraFormDialog(null)
      .showAndWait()
      .ifPresent(dto -> controller.create(
        dto,
        onSucces -> recargar(),
        onError -> mostrarError("No se pudo agregar la impresora", onError)
      ));
  }
  private void onEditar(ImpresorasResDto impresora){
    new ImpresoraFormDialog(impresora)
      .showAndWait()
      .ifPresent(dto -> controller.update(
        impresora.id(), 
        dto,
        onSucces -> recargar(),
        onError -> mostrarError("No se pudo Editar la impresora", onError)
      )
    );
  }
  private void onEliminar(ImpresorasResDto impresora){
    Alert confirm = new Alert(
      Alert.AlertType.CONFIRMATION,
      "¿Eliminar " + impresora.titulo() + "?",
      ButtonType.OK, ButtonType.CANCEL
    );
    confirm.showAndWait()
      .filter(b -> b == ButtonType.OK)
      .ifPresent(b -> controller.delete(
        impresora.id(),
        onSucces -> recargar(),
        onError -> mostrarError("No se pudo Eliminar la impresora", onError)
      ));
  }

  private void recargar(){
    contentImpresoras.getChildren().clear();
    cargarImpresoras(contentImpresoras);
  }
  private void mostrarError(String texto, Throwable err) {
    new Alert(AlertType.ERROR, texto + ": " + err.getMessage()).show();
  }
}