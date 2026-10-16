package com.abgraphicdesing.view.ui;

import com.abgraphicdesing.model.dto.MaterialesResDto;
import com.abgraphicdesing.model.dto.MaterialesReqDto;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class MaterialesFormDialog extends Dialog<MaterialesReqDto> {

  private final TextField txtNombre = new TextField();
  private final TextField txtPrecioCompra = new TextField();
  private final TextField txtMerma = new TextField();
  private final TextField txtDimensiones = new TextField();
  private final TextField txtStock = new TextField();

  // existente == null -> Agregar; existente != null -> Editar
  public MaterialesFormDialog(MaterialesResDto existente) {
    setTitle(existente == null ? "Agregar material" : "Editar material");

    ButtonType guardar = new ButtonType("Guardar", ButtonData.OK_DONE);
    getDialogPane().getButtonTypes().addAll(guardar, ButtonType.CANCEL);

    precargar(existente);

    GridPane grid = new GridPane();
    grid.setHgap(10);
    grid.setVgap(10);
    grid.setPadding(new Insets(20));
    grid.addRow(0, new Label("Nombre"), txtNombre);
    grid.addRow(1, new Label("Precio compra ($)"), txtPrecioCompra);
    grid.addRow(2, new Label("Merma (%)"), txtMerma);
    grid.addRow(3, new Label("Dimensiones"), txtDimensiones);
    grid.addRow(4, new Label("Stock"), txtStock);
    getDialogPane().setContent(grid);

    Node btnGuardar = getDialogPane().lookupButton(guardar);
    btnGuardar.disableProperty().bind(formularioInvalido());

    setResultConverter(boton -> {
      if (boton != guardar) return null;
      return new MaterialesReqDto(
        txtNombre.getText().trim(),
        Integer.parseInt(txtPrecioCompra.getText().trim()),
        Integer.parseInt(txtMerma.getText().trim()),
        txtDimensiones.getText().trim(),
        Integer.parseInt(txtStock.getText().trim())
      );
    });
  }

  private void precargar(MaterialesResDto m) {
    if (m == null) return;
    txtNombre.setText(m.nombre());
    txtPrecioCompra.setText(String.valueOf(m.precioCompra()));
    txtMerma.setText(String.valueOf(m.merma()));
    txtDimensiones.setText(m.dimensiones());
    txtStock.setText(String.valueOf(m.stock()));
  }

  private BooleanBinding formularioInvalido() {
    return Bindings.createBooleanBinding(() ->
      txtNombre.getText().isBlank()
        || !esEnteroNoNegativo(txtPrecioCompra.getText())
        || !esEnteroNoNegativo(txtMerma.getText())
        || !esEnteroNoNegativo(txtStock.getText()),
      txtNombre.textProperty(),
      txtPrecioCompra.textProperty(),
      txtMerma.textProperty(),
      txtStock.textProperty()
    );
  }

  private boolean esEnteroNoNegativo(String texto) {
    try {
      return Integer.parseInt(texto.trim()) >= 0;
    } catch (NumberFormatException e) {
      return false;
    }
  }
}