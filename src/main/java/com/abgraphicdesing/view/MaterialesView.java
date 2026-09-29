package com.abgraphicdesing.view;

import com.abgraphicdesing.controller.MaterialesController;
import com.abgraphicdesing.model.entity.Materiales;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

public class MaterialesView extends VBox {

  private final MaterialesController controller;
  private final TableView<Materiales> tabla = new TableView<>();
  private final Label mensaje = new Label();

  public MaterialesView(MaterialesController controller) {
    this.controller = controller;

    Label titulo = new Label("Materiales");
    titulo.getStyleClass().add("leftSideTitle");

    configurarColumnas();
    tabla.setPlaceholder(new Label("No hay materiales cargados"));

    getChildren().addAll(titulo, mensaje, tabla);

    setAlignment(Pos.TOP_CENTER);
    setPadding(new Insets(20));
    setSpacing(10);

    cargarMateriales();
  }

  private void configurarColumnas() {
    TableColumn<Materiales, String> colNombre = new TableColumn<>("Nombre");
    colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));

    TableColumn<Materiales, Integer> colPrecioCompra = new TableColumn<>("Precio compra");
    colPrecioCompra.setCellValueFactory(new PropertyValueFactory<>("precioCompra"));

    TableColumn<Materiales, Integer> colMerma = new TableColumn<>("Merma %");
    colMerma.setCellValueFactory(new PropertyValueFactory<>("merma"));

    TableColumn<Materiales, Integer> colPrecioCosto = new TableColumn<>("Precio costo");
    colPrecioCosto.setCellValueFactory(new PropertyValueFactory<>("precioCosto"));

    TableColumn<Materiales, String> colDimensiones = new TableColumn<>("Dimensiones");
    colDimensiones.setCellValueFactory(new PropertyValueFactory<>("dimensiones"));

    TableColumn<Materiales, Integer> colStock = new TableColumn<>("Stock");
    colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));

    tabla.getColumns().addAll(
      colNombre, colPrecioCompra, colMerma,
      colPrecioCosto, colDimensiones, colStock
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
      error -> mensaje.setText("Error al cargar los materiales")
    );
  }
}