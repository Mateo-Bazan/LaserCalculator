package com.abgraphicdesing.view.ui;

import com.abgraphicdesing.model.dto.ImpresorasReqDto;
import com.abgraphicdesing.model.dto.ImpresorasResDto;
import com.abgraphicdesing.model.entity.Tipos;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

public class ImpresoraFormDialog extends Dialog<ImpresorasReqDto> {

  private final ComboBox<Tipos> tipoCombo = new ComboBox<>();
  private final TextField marcaField = new TextField();
  private final TextField modeloField = new TextField();
  private final TextField tituloField = new TextField();
  private final TextField vidaUtilField = new TextField();
  private final TextField wattsField = new TextField();
  private final TextField precioCompraField = new TextField();
  private final TextField precioRepuestoField = new TextField();
  private final Label errorLabel = new Label();

  // existente == null -> modo CREAR | existente != null -> modo EDITAR
  public ImpresoraFormDialog(ImpresorasResDto existente){
    boolean editando = existente != null;
    setTitle(editando ? "Editar impresora" : "Nueva impresora");

    ButtonType guardarType = new ButtonType("Guardar", ButtonData.OK_DONE);
    getDialogPane().getButtonTypes().addAll(guardarType, ButtonType.CANCEL);

    configurarTipos(existente);
    if(editando){ precargar(existente); }

    GridPane grid = new GridPane();
    grid.setHgap(10);
    grid.setVgap(8);
    agregarFila(grid, 0, "Tipo", tipoCombo);
    agregarFila(grid, 1, "Marca", marcaField);
    agregarFila(grid, 2, "Modelo", modeloField);
    agregarFila(grid, 3, "Título", tituloField);
    agregarFila(grid, 4, "Vida útil (hs)", vidaUtilField);
    agregarFila(grid, 5, "Watts", wattsField);
    agregarFila(grid, 6, "Precio compra", precioCompraField);
    agregarFila(grid, 7, "Precio repuesto", precioRepuestoField);

    errorLabel.setStyle("-fx-text-fill: red;");
    getDialogPane().setContent(new VBox(10, grid, errorLabel));

    // Validación: si hay error, consumimos el evento y el diálogo NO se cierra
    Button okButton = (Button) getDialogPane().lookupButton(guardarType);
    okButton.addEventFilter(ActionEvent.ACTION, event -> {
      try{
        armarDto();
        errorLabel.setText("");
      }catch(IllegalArgumentException e){
        errorLabel.setText(e.getMessage());
        event.consume();
      }
    });

    // Lo que devuelve showAndWait(): el DTO si apretó Guardar, null si canceló
    setResultConverter(button -> button == guardarType ? armarDto() : null);
  }

  private void configurarTipos(ImpresorasResDto existente){
    tipoCombo.getItems().addAll(new Tipos(1, "Diodo"), new Tipos(2, "CO2"));
    tipoCombo.setConverter(new StringConverter<>(){
      @Override public String toString(Tipos t){ return t == null ? "" : t.getTipo(); }
      @Override public Tipos fromString(String s){ return null; }
    });
    if(existente != null){
      tipoCombo.getItems().stream()
        .filter(t -> t.getTipo().equals(existente.tipo()))
        .findFirst()
        .ifPresent(tipoCombo::setValue);
    }
  }

  private void precargar(ImpresorasResDto d){
    marcaField.setText(d.marca());
    modeloField.setText(d.modelo());
    tituloField.setText(d.titulo());
    vidaUtilField.setText(String.valueOf(d.vidaUtil()));
    wattsField.setText(String.valueOf(d.watts()));
    precioCompraField.setText(String.valueOf(d.precioCompra()));
    precioRepuestoField.setText(String.valueOf(d.precioRespuesto()));
  }

  private void agregarFila(GridPane grid, int fila, String texto, javafx.scene.Node campo){
    grid.add(new Label(texto), 0, fila);
    grid.add(campo, 1, fila);
  }

  private ImpresorasReqDto armarDto(){
    Tipos tipo = tipoCombo.getValue();
    if(tipo == null){ throw new IllegalArgumentException("Elegí un tipo"); }
    if(marcaField.getText().isBlank()){ throw new IllegalArgumentException("La marca es obligatoria"); }
    if(modeloField.getText().isBlank()){ throw new IllegalArgumentException("El modelo es obligatorio"); }
    if(tituloField.getText().isBlank()){ throw new IllegalArgumentException("El título es obligatorio"); }

    int vidaUtil = leerInt(vidaUtilField, "Vida útil");
    int watts = leerInt(wattsField, "Watts");
    float precioCompra = leerFloat(precioCompraField, "Precio compra");
    float precioRepuesto = leerFloat(precioRepuestoField, "Precio repuesto");

    return new ImpresorasReqDto(
      tipo.getId(),
      marcaField.getText().trim(),
      modeloField.getText().trim(),
      tituloField.getText().trim(),
      vidaUtil, watts, precioCompra, precioRepuesto
    );
  }

  private int leerInt(TextField f, String nombre){
    try{
      int v = Integer.parseInt(f.getText().trim());
      if(v <= 0){ throw new NumberFormatException(); }
      return v;
    }catch(NumberFormatException e){
      throw new IllegalArgumentException(nombre + " debe ser un número entero mayor a 0");
    }
  }

  private float leerFloat(TextField f, String nombre){
    try{
      float v = Float.parseFloat(f.getText().trim().replace(',', '.'));
      if(v <= 0){ throw new NumberFormatException(); }
      return v;
    }catch(NumberFormatException e){
      throw new IllegalArgumentException(nombre + " debe ser un número mayor a 0");
    }
  }
}