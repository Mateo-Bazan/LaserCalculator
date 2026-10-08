package com.abgraphicdesing.view;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import com.abgraphicdesing.controller.AjustesController;
import com.abgraphicdesing.controller.PeriodosController;
import com.abgraphicdesing.model.entity.Ajustes;
import com.abgraphicdesing.view.ui.AjustesFormDialog;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class AjustesView extends VBox {

  private final AjustesController controller;
  private final PeriodosController periodosController;
  private final ListView<Ajustes> listView = new ListView<>();

  private final Button btnAgregar = new Button("Agregar");
  private final Button btnEditar = new Button("Editar");
  private final Button btnEliminar = new Button("Eliminar");

  public AjustesView(AjustesController controller, PeriodosController periodosController) {
    this.controller = controller;
    this.periodosController = periodosController;

    Label titulo = new Label("Ajustes");
    titulo.getStyleClass().add("ajustesTitle");

    configurarLista();

    HBox botones = new HBox(10, btnAgregar, btnEditar, btnEliminar);
    botones.setAlignment(Pos.CENTER_RIGHT);

    // Editar y Eliminar solo si hay algo seleccionado
    btnEditar.disableProperty().bind(
      listView.getSelectionModel().selectedItemProperty().isNull()
    );
    btnEliminar.disableProperty().bind(
      listView.getSelectionModel().selectedItemProperty().isNull()
    );

    btnAgregar.setOnAction(e -> onAgregar());
    btnEditar.setOnAction(e -> onEditar());
    btnEliminar.setOnAction(e -> onEliminar());

    VBox.setVgrow(listView, Priority.ALWAYS);
    setSpacing(10);
    setPadding(new Insets(15));
    getChildren().addAll(titulo, listView, botones);
    getStyleClass().add("ajustesView");

    cargarAjustes();
  }

  // ---------- Lista ----------

  private void configurarLista() {
    NumberFormat moneda = NumberFormat.getCurrencyInstance(Locale.of("es", "AR"));
    moneda.setMaximumFractionDigits(0); // gasto es int: sin decimales

    listView.setCellFactory(lv -> new ListCell<>() {
      @Override
      protected void updateItem(Ajustes item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
          setGraphic(null);
          return;
        }

        Label nombre = new Label(item.getNombre());
        Label costo = new Label(moneda.format(item.getGasto()));
        Label periodo = new Label(item.getPeriodo());

        Region espaciador = new Region();
        HBox.setHgrow(espaciador, Priority.ALWAYS);

        HBox fila = new HBox(15, nombre, espaciador, costo, periodo);
        fila.setAlignment(Pos.CENTER_LEFT);
        setGraphic(fila);
      }
    });
  }

  // ---------- Datos ----------

  private void cargarAjustes() {
    controller.getAll(
      lista -> listView.getItems().setAll(lista),
      err -> mostrarError("No se pudieron cargar los ajustes", err)
    );
  }

  // ---------- Acciones ----------
  private void onAgregar() {
    periodosController.getAll(
      periodos -> new AjustesFormDialog(periodos, null)
        .showAndWait()
        .ifPresent(dto -> controller.create(
          dto,
          ok -> cargarAjustes(),
          err -> mostrarError("No se pudo crear", err)
        )),
      err -> mostrarError("No se pudieron cargar los periodos", err)
    );
  }

  private void onEditar() {
    Ajustes seleccionado = listView.getSelectionModel().getSelectedItem();
    if (seleccionado == null) return;

    periodosController.getAll(
      periodos -> new AjustesFormDialog(periodos, seleccionado)
        .showAndWait()
        .ifPresent(dto -> controller.update(
          seleccionado.getId(),
          dto,
          ok -> cargarAjustes(),
          err -> mostrarError("No se pudo actualizar", err)
          )),
      err -> mostrarError("No se pudieron cargar los periodos", err)
    );
  }

  private void onEliminar() {
    Ajustes seleccionado = listView.getSelectionModel().getSelectedItem();
    if (seleccionado == null) return;

    Alert confirmar = new Alert(
      AlertType.CONFIRMATION,
      "¿Eliminar \"" + seleccionado.getNombre() + "\"?",
      ButtonType.OK, ButtonType.CANCEL
    );

    confirmar.showAndWait()
      .filter(r -> r == ButtonType.OK)
      .ifPresent(r -> controller.delete(
        seleccionado.getId(),
        msg -> {
          System.out.println("Borrando: " + seleccionado.getId());
          cargarAjustes();},
        err -> mostrarError("No se pudo eliminar", err)
      ));
  }

  private void mostrarError(String mensaje, Throwable err) {
    new Alert(AlertType.ERROR, mensaje + ": " + err.getMessage()).show();
  }
}