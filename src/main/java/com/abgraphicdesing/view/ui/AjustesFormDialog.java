package com.abgraphicdesing.view.ui;

import java.util.List;

import com.abgraphicdesing.model.dto.AjustesReqDto;
import com.abgraphicdesing.model.dto.AjustesResDto;
import com.abgraphicdesing.model.entity.Periodos;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.util.StringConverter;

public class AjustesFormDialog extends Dialog<AjustesReqDto> {

  private final TextField txtNombre = new TextField();
  private final TextField txtGasto = new TextField();
  private final ComboBox<Periodos> cbPeriodo = new ComboBox<>();

  // existente == null -> modo Agregar; existente != null -> modo Editar
  public AjustesFormDialog(List<Periodos> periodos, AjustesResDto existente) {
    setTitle(existente == null ? "Agregar ajuste" : "Editar ajuste");

    ButtonType guardar = new ButtonType("Guardar", ButtonData.OK_DONE);
    getDialogPane().getButtonTypes().addAll(guardar, ButtonType.CANCEL);

    configurarCombo(periodos);
    precargar(periodos, existente);

    GridPane grid = new GridPane();
    grid.setHgap(10);
    grid.setVgap(10);
    grid.setPadding(new Insets(20));
    grid.addRow(0, new Label("Nombre"), txtNombre);
    grid.addRow(1, new Label("Gasto ($)"), txtGasto);
    grid.addRow(2, new Label("Periodo"), cbPeriodo);
    getDialogPane().setContent(grid);

    // Guardar deshabilitado mientras el formulario sea inválido
    Node btnGuardar = getDialogPane().lookupButton(guardar);
    btnGuardar.disableProperty().bind(formularioInvalido());

    // Qué devuelve el diálogo al cerrarse
    setResultConverter(boton -> {
      if (boton != guardar) return null;
      return new AjustesReqDto(
        txtNombre.getText().trim(),
        Integer.parseInt(txtGasto.getText().trim()),
        cbPeriodo.getValue().getId()
      );
    });
  }

  private void configurarCombo(List<Periodos> periodos) {
    cbPeriodo.getItems().setAll(periodos);
    cbPeriodo.setConverter(new StringConverter<>() {
      @Override
      public String toString(Periodos p) {
        return p == null ? "" : p.getPeriodo();
      }
      @Override
      public Periodos fromString(String s) {
        return null; // el combo no es editable
      }
    });
  }

  private void precargar(List<Periodos> periodos, AjustesResDto existente) {
    if (existente == null) return;
    txtNombre.setText(existente.nombre());
    txtGasto.setText(String.valueOf(existente.gasto()));
    periodos.stream()
      .filter(p -> p.getId() == existente.idPeriodo())
      .findFirst()
      .ifPresent(cbPeriodo::setValue);
  }

  private BooleanBinding formularioInvalido() {
    return Bindings.createBooleanBinding(() -> {
      if (txtNombre.getText().isBlank()) return true;
      if (cbPeriodo.getValue() == null) return true;
      try {
        return Integer.parseInt(txtGasto.getText().trim()) < 0;
      } catch (NumberFormatException e) {
        return true;
      }
    }, txtNombre.textProperty(), txtGasto.textProperty(), cbPeriodo.valueProperty());
  }
}