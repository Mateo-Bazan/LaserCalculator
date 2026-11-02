package com.abgraphicdesing.view;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.abgraphicdesing.controller.MaterialesController;
import com.abgraphicdesing.model.dto.MaterialesResDto;
import com.abgraphicdesing.view.ui.MaterialDialog;
import com.abgraphicdesing.view.util.Format;

import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/** Tabla de materiales segun la referencia (buscador, pills, paginacion). */
public class MaterialesView extends VBox {

  private static final int PAGE_SIZE = 8;
  private static final int STOCK_ROJO_MAX = 5;
  private static final int STOCK_AMARILLO_MAX = 15;

  private final MaterialesController controller;
  private final TableView<MaterialesResDto> tabla = new TableView<>();
  private final TextField txtBuscar = new TextField();
  private final Label status = new Label();
  private final Label footerCount = new Label();
  private final Button btnPrev = new Button("<");
  private final Button btnNext = new Button(">");
  private final Label lblPagina = new Label("1");

  private final List<MaterialesResDto> todos = new ArrayList<>();
  private List<MaterialesResDto> filtrados = new ArrayList<>();
  private int pagina = 0;

  // private final Button btnAgregar = new Button("Agregar");
  // private final Button btnEditar = new Button("Editar");
  // private final Button btnEliminar = new Button("Eliminar");

  public MaterialesView(MaterialesController controller) {
    this.controller = controller;
    getStyleClass().add("app-background");
    setPadding(new Insets(28, 36, 28, 36));

    VBox card = new VBox(12);
    card.getStyleClass().add("content-card");

    Label titulo = new Label("Mis Materiales");
    titulo.getStyleClass().add("page-title");
    Label subtitulo = new Label("Control de stock de materiales");
    subtitulo.getStyleClass().add("page-subtitle");
    VBox encabezado = new VBox(2, titulo, subtitulo);

    Region spacer = new Region();
    HBox.setHgrow(spacer, Priority.ALWAYS);

    Button btnAgregar = new Button("+");
    btnAgregar.getStyleClass().add("btn-add");
    btnAgregar.setOnAction(e -> onAgregar());

    HBox header = new HBox(encabezado, spacer, btnAgregar);
    header.setAlignment(Pos.CENTER_LEFT);

    txtBuscar.setPromptText("Buscar material...");
    txtBuscar.getStyleClass().add("search-input");
    txtBuscar.setPrefWidth(260);
    txtBuscar.setMaxWidth(260);
    txtBuscar.textProperty().addListener((obs, o, n) -> {
      pagina = 0;
      aplicarFiltro();
    });

    status.getStyleClass().add("page-subtitle");

    configurarColumnas();
    tabla.setPlaceholder(new Label("No hay materiales cargados"));
    tabla.getStyleClass().add("materials-table");

    footerCount.getStyleClass().add("footer-count");
    btnPrev.getStyleClass().add("page-btn");
    btnNext.getStyleClass().add("page-btn");
    lblPagina.getStyleClass().add("page-btn-current");
    btnPrev.setOnAction(e -> { if (pagina > 0) { pagina--; repintarPagina(); } });
    btnNext.setOnAction(e -> {
      if ((pagina + 1) * PAGE_SIZE < filtrados.size()) { pagina++; repintarPagina(); }
    });
    HBox paginacion = new HBox(8, btnPrev, lblPagina, btnNext);
    paginacion.setAlignment(Pos.CENTER_RIGHT);
    Region footerSpacer = new Region();
    HBox.setHgrow(footerSpacer, Priority.ALWAYS);
    HBox footer = new HBox(footerCount, footerSpacer, paginacion);
    footer.setAlignment(Pos.CENTER_LEFT);

    card.getChildren().addAll(header, txtBuscar, status, tabla, footer);

    ScrollPane scroll = new ScrollPane(card);
    scroll.setFitToWidth(true);
    scroll.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
    scroll.getStyleClass().add("clean-scroll");
    VBox.setVgrow(scroll, Priority.ALWAYS);

    getChildren().add(scroll);
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

  // ---------- Columnas ----------

  private void configurarColumnas() {
    TableColumn<MaterialesResDto, MaterialesResDto> colNombre = new TableColumn<>("Nombre");
    colNombre.setCellValueFactory(d -> new ReadOnlyObjectWrapper<>(d.getValue()));
    colNombre.setCellFactory(c -> new TableCell<>() {
      private final HBox acciones = new HBox(8);
      private final Button edit = new Button("✎");
      private final Button del = new Button("🗑");
      {
        edit.getStyleClass().add("btn-icon-edit");
        del.getStyleClass().add("btn-icon-delete");
        acciones.getChildren().addAll(edit, del);
        acciones.setAlignment(Pos.CENTER_RIGHT);
        acciones.setVisible(false);
      }

      @Override
      protected void updateItem(MaterialesResDto item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
          setGraphic(null);
          return;
        }
        Label nombre = new Label(Format.texto(item.nombre()));
        HBox fila = new HBox(8, nombre, acciones);
        fila.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(nombre, Priority.ALWAYS);

        TableRow<MaterialesResDto> row = getTableRow();
        if (row != null) {
          acciones.visibleProperty().unbind();
          acciones.visibleProperty().bind(
            row.hoverProperty().or(row.selectedProperty()));
        }
        edit.setOnAction(e -> onEditar(item));
        del.setOnAction(e -> onEliminar(item));
        setGraphic(fila);
      }
    });

    TableColumn<MaterialesResDto, String> colCompra = new TableColumn<>("Precio de Compra");
    colCompra.setCellValueFactory(d ->
      new ReadOnlyObjectWrapper<>(Format.moneda(d.getValue().precioCompra())));

    TableColumn<MaterialesResDto, String> colMerma = new TableColumn<>("Merma");
    colMerma.setCellValueFactory(d ->
      new ReadOnlyObjectWrapper<>(d.getValue().merma() + "%"));

    TableColumn<MaterialesResDto, String> colCosto = new TableColumn<>("Precio de Costo");
    colCosto.setCellValueFactory(d ->
      new ReadOnlyObjectWrapper<>(Format.moneda(d.getValue().precioCosto())));
    colCosto.setCellFactory(c -> new TableCell<>() {
      @Override
      protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
          setText(null);
          getStyleClass().remove("cost-value");
        } else {
          setText(item);
          if (!getStyleClass().contains("cost-value")) {
            getStyleClass().add("cost-value");
          }
        }
      }
    });

    TableColumn<MaterialesResDto, String> colDim = new TableColumn<>("Dimensiones");
    colDim.setCellValueFactory(d -> {
      String v = d.getValue().dimensiones();
      return new ReadOnlyObjectWrapper<>(Format.texto(v));
    });

    TableColumn<MaterialesResDto, MaterialesResDto> colStock = new TableColumn<>("Stock");
    colStock.setCellValueFactory(d -> new ReadOnlyObjectWrapper<>(d.getValue()));
    colStock.setCellFactory(c -> new TableCell<>() {
      @Override
      protected void updateItem(MaterialesResDto item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
          setGraphic(null);
          return;
        }
        Label pill = new Label();
        int stock = item.stock();
        if (stock <= 0) {
          pill.setText("Sin Stock");
          pill.getStyleClass().add("stock-pill-red");
        } else if (stock <= STOCK_ROJO_MAX) {
          pill.setText(stock + " unidades");
          pill.getStyleClass().add("stock-pill-red");
        } else if (stock <= STOCK_AMARILLO_MAX) {
          pill.setText(stock + " unidades");
          pill.getStyleClass().add("stock-pill-yellow");
        } else {
          pill.setText(stock + " unidades");
          pill.getStyleClass().add("stock-pill-green");
        }
        setGraphic(pill);
      }
    });

    tabla.getColumns().addAll(colNombre, colCompra, colMerma, colCosto, colDim, colStock);
    tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
    colNombre.setPrefWidth(220);
    // Altura estable (una pagina completa) sin scroll interno
    tabla.setFixedCellSize(44);
    tabla.setPrefHeight(44 * PAGE_SIZE + 42);
    tabla.setMaxHeight(44 * PAGE_SIZE + 42);
  }

  // ---------- Datos ----------

  private void cargarMateriales() {
    status.setText("Cargando...");
    controller.getAll(
      lista -> Platform.runLater(() -> {
        todos.clear();
        if (lista != null) todos.addAll(lista);
        pagina = 0;
        aplicarFiltro();
        status.setText(todos.isEmpty()
          ? "No hay materiales cargados. Usá + para agregar uno." : "");
      }),
      err -> Platform.runLater(() ->
        status.setText("Error al cargar los materiales."))
    );
  }

  private void aplicarFiltro() {
    String q = txtBuscar.getText() == null ? "" : txtBuscar.getText().trim().toLowerCase();
    if (q.isEmpty()) {
      filtrados = new ArrayList<>(todos);
    } else {
      filtrados = todos.stream()
        .filter(m -> m.nombre() != null && m.nombre().toLowerCase().contains(q))
        .collect(Collectors.toList());
    }
    int maxPagina = Math.max(0, (filtrados.size() - 1) / PAGE_SIZE);
    if (pagina > maxPagina) pagina = maxPagina;
    repintarPagina();
  }

  private void repintarPagina() {
    int desde = pagina * PAGE_SIZE;
    int hasta = Math.min(desde + PAGE_SIZE, filtrados.size());
    List<MaterialesResDto> paginaActual = desde < hasta
      ? filtrados.subList(desde, hasta) : List.of();
    tabla.setItems(FXCollections.observableArrayList(paginaActual));

    footerCount.setText("Mostrando " + filtrados.size()
      + " de " + todos.size() + " Materiales");
    lblPagina.setText(String.valueOf(pagina + 1));
    btnPrev.setDisable(pagina == 0);
    btnNext.setDisable((pagina + 1) * PAGE_SIZE >= filtrados.size());
  }

  // ---------- Acciones ----------

  private void onAgregar() {
    new MaterialDialog(null).showAndWait().ifPresent(dto ->
      controller.create(
        dto,
        creado -> Platform.runLater(this::cargarMateriales),
        err -> Platform.runLater(() ->
          status.setText("No se pudo crear el material."))
      ));
  }

  private void onEditar(MaterialesResDto item) {
    new MaterialDialog(item).showAndWait().ifPresent(dto ->
      controller.update(
        item.id(), dto,
        actualizado -> Platform.runLater(this::cargarMateriales),
        err -> Platform.runLater(() ->
          status.setText("No se pudo actualizar el material."))
      ));
  }

  private void onEliminar(MaterialesResDto item) {
    Alert confirmar = new Alert(AlertType.CONFIRMATION,
      "¿Eliminar \"" + item.nombre() + "\"?",
      ButtonType.OK, ButtonType.CANCEL);
    confirmar.setHeaderText(null);
    confirmar.showAndWait()
      .filter(r -> r == ButtonType.OK)
      .ifPresent(r -> controller.delete(
        item.id(),
        msg -> Platform.runLater(this::cargarMateriales),
        err -> Platform.runLater(() ->
          status.setText("No se pudo eliminar el material."))
      ));
  }
}
