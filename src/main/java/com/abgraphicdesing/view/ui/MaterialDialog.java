package com.abgraphicdesing.view.ui;

import com.abgraphicdesing.model.dto.MaterialesReqDto;
import com.abgraphicdesing.model.dto.MaterialesResDto;

import javafx.geometry.Insets;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

/** Alta/edicion de material. El precio de costo lo calcula el backend. */
public class MaterialDialog extends Dialog<MaterialesReqDto> {

  private final TextField txtNombre = new TextField();
  private final TextField txtPrecioCompra = new TextField();
  private final TextField txtMerma = new TextField();
  private final TextField txtDimensiones = new TextField();
  private final TextField txtStock = new TextField();

  public MaterialDialog(MaterialesResDto existente) {
    setTitle(existente == null ? "Agregar material" : "Editar material");

    ButtonType guardar = new ButtonType("Guardar", ButtonData.OK_DONE);
    getDialogPane().getButtonTypes().addAll(guardar, ButtonType.CANCEL);

    if (existente != null) {
      txtNombre.setText(orEmpty(existente.nombre()));
      txtPrecioCompra.setText(String.valueOf(existente.precioCompra()));
      txtMerma.setText(String.valueOf(existente.merma()));
      txtDimensiones.setText(orEmpty(existente.dimensiones()));
      txtStock.setText(String.valueOf(existente.stock()));
    }

    GridPane grid = new GridPane();
    grid.setHgap(10);
    grid.setVgap(10);
    grid.setPadding(new Insets(20));
    grid.addRow(0, new Label("Nombre"), txtNombre);
    grid.addRow(1, new Label("Precio de compra"), txtPrecioCompra);
    grid.addRow(2, new Label("Merma (%)"), txtMerma);
    grid.addRow(3, new Label("Dimensiones"), txtDimensiones);
    grid.addRow(4, new Label("Stock"), txtStock);
    getDialogPane().setContent(grid);

    setResultConverter(boton -> {
      if (boton != guardar) return null;
      try {
        String nombre = txtNombre.getText().trim();
        int precioCompra = Integer.parseInt(txtPrecioCompra.getText().trim());
        int merma = Integer.parseInt(txtMerma.getText().trim());
        String dimensiones = txtDimensiones.getText().trim();
        int stock = Integer.parseInt(txtStock.getText().trim());
        if (nombre.isEmpty() || precioCompra < 0 || merma < 0 || stock < 0) {
          return null;
        }
        return new MaterialesReqDto(nombre, precioCompra, merma,
          dimensiones.isEmpty() ? null : dimensiones, stock);
      } catch (NumberFormatException e) {
        return null;
      }
    });
  }

  private String orEmpty(String s) {
    return s == null ? "" : s;
  }
}
