package com.abgraphicdesing.view;

import java.util.function.Function;

import com.abgraphicdesing.controller.MaterialesController;
import com.abgraphicdesing.model.dto.MaterialesResDto;
import com.abgraphicdesing.view.ui.MaterialesFormDialog;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class MaterialesView extends VBox {

  private final MaterialesController controller;
  private final TableView<MaterialesResDto> tabla = new TableView<>();
  private final Label mensaje = new Label();

  private final Button btnAgregar = new Button("Agregar");
  private final Button btnEditar = new Button("Editar");
  private final Button btnEliminar = new Button("Eliminar");

  public MaterialesView(MaterialesController controller) {
    this.controller = controller;

    Label titulo = new Label("Materiales");
    titulo.getStyleClass().add("leftSideTitle");
    mensaje.getStyleClass().add("estado");

    configurarColumnas();
    tabla.setPlaceholder(new Label("No hay materiales cargados"));
    VBox.setVgrow(tabla, Priority.ALWAYS);

    HBox botones = new HBox(10, btnAgregar, btnEditar, btnEliminar);
    botones.setAlignment(Pos.CENTER_RIGHT);

    btnEditar.disableProperty().bind(
      tabla.getSelectionModel().selectedItemProperty().isNull()
    );
    btnEliminar.disableProperty().bind(
      tabla.getSelectionModel().selectedItemProperty().isNull()
    );

    btnAgregar.setOnAction(e -> onAgregar());
    btnEditar.setOnAction(e -> onEditar());
    btnEliminar.setOnAction(e -> onEliminar());

    getChildren().addAll(titulo, mensaje, tabla, botones);

    setAlignment(Pos.TOP_CENTER);
    setPadding(new Insets(20));
    setSpacing(10);

    cargarMateriales();
  }
  
  private <T> TableColumn<MaterialesResDto, T> columna(
    String titulo,
    Function<MaterialesResDto, T> extractor
  ) {
    TableColumn<MaterialesResDto, T> col = new TableColumn<>(titulo);
    col.setCellValueFactory(c ->
      new ReadOnlyObjectWrapper<>(extractor.apply(c.getValue()))
    );
    return col;
  }

  private void configurarColumnas() {
    tabla.getColumns().addAll(
      columna("Nombre", MaterialesResDto::nombre),
      columna("Precio compra", MaterialesResDto::precioCompra),
      columna("Merma %", MaterialesResDto::merma),
      columna("Precio costo", MaterialesResDto::precioCosto),
      columna("Dimensiones", MaterialesResDto::dimensiones),
      columna("Stock", MaterialesResDto::stock)
    );

    tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
  }

  private void cargarMateriales() {
    mensaje.setText("Cargando...");

    controller.getAll(
      lista -> {
        tabla.setItems(FXCollections.observableArrayList(lista));
        mensaje.setText("");
      },
      error -> mensaje.setText("Error al cargar los materiales: " + error.getMessage())
    );
  }

  private void onAgregar() {
    new MaterialesFormDialog(null)
      .showAndWait()
      .ifPresent(dto -> controller.create(
        dto,
        ok -> cargarMateriales(),
        err -> mostrarError("No se pudo crear", err)
      ));
  }

  private void onEditar() {
    MaterialesResDto seleccionado = tabla.getSelectionModel().getSelectedItem();
    if (seleccionado == null) return;

    new MaterialesFormDialog(seleccionado)
      .showAndWait()
      .ifPresent(dto -> controller.update(
        seleccionado.id(),
        dto,
        ok -> cargarMateriales(),
        err -> mostrarError("No se pudo actualizar", err)
      ));
  }

  private void onEliminar() {
    MaterialesResDto seleccionado = tabla.getSelectionModel().getSelectedItem();
    if (seleccionado == null) return;

    Alert confirmar = new Alert(
      AlertType.CONFIRMATION,
      "¿Eliminar \"" + seleccionado.nombre() + "\"?",
      ButtonType.OK, ButtonType.CANCEL
    );

    confirmar.showAndWait()
      .filter(r -> r == ButtonType.OK)
      .ifPresent(r -> controller.delete(
        seleccionado.id(),
        msg -> cargarMateriales(),
        err -> mostrarError("No se pudo eliminar", err)
      ));
  }

  private void mostrarError(String texto, Throwable err) {
    new Alert(AlertType.ERROR, texto + ": " + err.getMessage()).show();
  }
}