package com.abgraphicdesing.view.ui;

import com.abgraphicdesing.model.dto.MaterialesReqDto;
import com.abgraphicdesing.model.dto.MaterialesResDto;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

/** Alta/edicion de material. El precio de costo lo calcula el backend. */
public class MaterialDialog extends Dialog<MaterialesReqDto> {

  private final TextField txtNombre = new TextField();
  private final TextField txtPrecioCompra = new TextField();
  private final TextField txtMerma = new TextField();
  private final TextField txtDimensiones = new TextField();
  private final TextField txtStock = new TextField();
  private final Label errorLabel = new Label();

  public MaterialDialog(MaterialesResDto existente) {
    setTitle(existente == null ? "Agregar material" : "Editar material");

    ButtonType guardar = new ButtonType("Guardar", ButtonData.OK_DONE);
    getDialogPane().getButtonTypes().addAll(guardar, ButtonType.CANCEL);

    Button guardarBtn = (Button) getDialogPane().lookupButton(guardar);
    guardarBtn.setDisable(true);

    if (existente != null) {
      txtNombre.setText(orEmpty(existente.nombre()));
      txtPrecioCompra.setText(String.valueOf(existente.precioCompra()));
      txtMerma.setText(String.valueOf(existente.merma()));
      txtDimensiones.setText(orEmpty(existente.dimensiones()));
      txtStock.setText(String.valueOf(existente.stock()));
    }

    errorLabel.getStyleClass().add("form-error");
    errorLabel.setWrapText(true);
    errorLabel.setVisible(false);
    errorLabel.setManaged(false);

    GridPane grid = new GridPane();
    grid.setHgap(10);
    grid.setVgap(10);
    grid.setPadding(new Insets(20, 20, 10, 20));
    grid.addRow(0, new Label("Nombre"), txtNombre);
    grid.addRow(1, new Label("Precio de compra"), txtPrecioCompra);
    grid.addRow(2, new Label("Merma (%)"), txtMerma);
    grid.addRow(3, new Label("Dimensiones"), txtDimensiones);
    grid.addRow(4, new Label("Stock"), txtStock);

    VBox content = new VBox(10, grid, errorLabel);
    content.setPadding(new Insets(0, 0, 10, 0));
    getDialogPane().setContent(content);

    Runnable validar = () -> {
      String error = validarCampos();
      if (error == null) {
        errorLabel.setVisible(false);
        errorLabel.setManaged(false);
        guardarBtn.setDisable(false);
      } else {
        errorLabel.setText(error);
        errorLabel.setVisible(true);
        errorLabel.setManaged(true);
        guardarBtn.setDisable(true);
      }
    };

    txtNombre.textProperty().addListener((o, oldV, newV) -> validar.run());
    txtPrecioCompra.textProperty().addListener((o, oldV, newV) -> validar.run());
    txtMerma.textProperty().addListener((o, oldV, newV) -> validar.run());
    txtDimensiones.textProperty().addListener((o, oldV, newV) -> validar.run());
    txtStock.textProperty().addListener((o, oldV, newV) -> validar.run());

    validar.run();

    setResultConverter(boton -> {
      if (boton != guardar) return null;
      String error = validarCampos();
      if (error != null) {
        return null;
      }
      try {
        String nombre = txtNombre.getText().trim();
        int precioCompra = Integer.parseInt(txtPrecioCompra.getText().trim());
        int merma = Integer.parseInt(txtMerma.getText().trim());
        String dimensiones = txtDimensiones.getText().trim();
        int stock = Integer.parseInt(txtStock.getText().trim());
        return new MaterialesReqDto(nombre, precioCompra, merma,
          dimensiones.isEmpty() ? null : dimensiones, stock);
      } catch (NumberFormatException e) {
        return null;
      }
    });
  }

  private String validarCampos() {
    String nombre = txtNombre.getText() == null ? "" : txtNombre.getText().trim();
    if (nombre.isEmpty()) {
      return "El nombre es obligatorio.";
    }
    try {
      String pc = txtPrecioCompra.getText() == null ? "" : txtPrecioCompra.getText().trim();
      int precioCompra = Integer.parseInt(pc);
      if (precioCompra < 0) {
        return "El precio de compra debe ser mayor o igual a 0.";
      }
    } catch (NumberFormatException e) {
      return "El precio de compra debe ser un número válido.";
    }
    try {
      String m = txtMerma.getText() == null ? "" : txtMerma.getText().trim();
      int merma = Integer.parseInt(m);
      if (merma < 0) {
        return "La merma debe ser mayor o igual a 0.";
      }
    } catch (NumberFormatException e) {
      return "La merma debe ser un número válido.";
    }
    try {
      String s = txtStock.getText() == null ? "" : txtStock.getText().trim();
      int stock = Integer.parseInt(s);
      if (stock < 0) {
        return "El stock debe ser mayor o igual a 0.";
      }
    } catch (NumberFormatException e) {
      return "El stock debe ser un número válido.";
    }
    return null;
  }

  private String orEmpty(String s) {
    return s == null ? "" : s;
  }
}
