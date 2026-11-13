package com.abgraphicdesing.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.abgraphicdesing.controller.ImpresorasController;
import com.abgraphicdesing.controller.TiposController;
import com.abgraphicdesing.model.dto.ImpresorasReqDto;
import com.abgraphicdesing.model.dto.ImpresorasResDto;
import com.abgraphicdesing.view.ui.ImpresoraFormPanel;
import com.abgraphicdesing.view.ui.ImpresoraFullCard;
import com.abgraphicdesing.view.ui.ImpresoraFullCard.SelectionEvent;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class ImpresorasView extends VBox {

  private final ImpresorasController controller;
  private final TiposController tiposController;
  private final FlowPane cards = new FlowPane();
  private final VBox formSlot = new VBox();
  private final Label status = new Label();
  private final List<ImpresorasResDto> cache = new ArrayList<>();
  private final List<ImpresoraFullCard> cardNodes = new ArrayList<>();
  private UUID seleccionada = null;

  public ImpresorasView(ImpresorasController controller, TiposController tiposController) {
    this.controller = controller;
    this.tiposController = tiposController;
    getStyleClass().add("app-background");
    setPadding(new Insets(28, 36, 28, 36));
    setSpacing(12);

    Label titulo = new Label("Mis Impresoras");
    titulo.getStyleClass().add("page-title");
    Label subtitulo = new Label("Catálogo de impresoras y sus configuraciones.");
    subtitulo.getStyleClass().add("page-subtitle");
    VBox encabezado = new VBox(2, titulo, subtitulo);

    Region spacer = new Region();
    HBox.setHgrow(spacer, Priority.ALWAYS);

    Button btnAgregar = new Button("+");
    btnAgregar.getStyleClass().add("btn-add");
    btnAgregar.setOnAction(e -> mostrarFormulario(null));

    HBox header = new HBox(encabezado, spacer, btnAgregar);
    header.setAlignment(Pos.CENTER_LEFT);

    javafx.scene.control.Separator separador = new javafx.scene.control.Separator();
    separador.getStyleClass().add("header-separator");

    status.getStyleClass().add("page-subtitle");

    cards.setHgap(24);
    cards.setVgap(20);
    cards.setAlignment(Pos.TOP_LEFT);

    VBox contenido = new VBox(12, header, separador, formSlot, status, cards);
    contenido.setMaxWidth(1100);

    HBox centrado = new HBox(contenido);
    centrado.setAlignment(Pos.TOP_CENTER);
    HBox.setHgrow(contenido, Priority.ALWAYS);

    ScrollPane scroll = new ScrollPane(centrado);
    scroll.setFitToWidth(true);
    scroll.setFitToHeight(true);
    scroll.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
    scroll.getStyleClass().add("clean-scroll");
    VBox.setVgrow(scroll, Priority.ALWAYS);

    getChildren().add(scroll);
    cargarImpresoras();
  }

  // ---------- Datos ----------

  private void cargarImpresoras() {
    status.setText("Cargando...");
    controller.getAll(
      lista -> Platform.runLater(() -> {
        cache.clear();
        if (lista != null) cache.addAll(lista);
        seleccionada = null;
        repintar();
        status.setText(cache.isEmpty() ? "No hay impresoras cargadas." : "");
      }),
      err -> Platform.runLater(() ->
        status.setText("Error al cargar las impresoras."))
    );
  }

  private void repintar() {
    cards.getChildren().clear();
    cardNodes.clear();
    for (ImpresorasResDto dto : cache) {
      ImpresoraFullCard card = new ImpresoraFullCard(dto);
      card.setSelected(dto.id() != null && dto.id().equals(seleccionada));
      card.addEventHandler(SelectionEvent.TYPE, e -> seleccionar(e.getImpresoraId()));
      card.setOnEdit(this::confirmarEliminar_PedirEdicion);
      card.setOnDelete(this::pedirConfirmacionEliminar);
      cardNodes.add(card);
      cards.getChildren().add(card);
    }
  }

  private void seleccionar(UUID id) {
    seleccionada = id;
    for (ImpresoraFullCard c : cardNodes) {
      Object prop = c.getProperties().get("impresoraId");
      c.setSelected(prop != null && prop.equals(id));
    }
  }

  // ---------- Formulario ----------

  private void mostrarFormulario(ImpresorasResDto existente) {
    formSlot.getChildren().clear();
    ImpresoraFormPanel form = new ImpresoraFormPanel(existente, tiposController);
    form.setOnCancelar(() -> formSlot.getChildren().clear());
    if (existente == null) {
      form.setOnGuardar(dto -> guardarCreacion(dto));
    } else {
      form.setOnGuardar(dto -> guardarEdicion(existente.id(), dto));
    }
    formSlot.getChildren().add(form);
  }

  private void guardarCreacion(ImpresorasReqDto dto) {
    controller.create(
      dto,
      creada -> Platform.runLater(() -> {
        formSlot.getChildren().clear();
        cargarImpresoras();
        // Notificar a calculadora si está en cache
        try {
          javafx.scene.Node node = getScene() == null ? null : getScene().getRoot();
          if (node instanceof com.abgraphicdesing.view.MainView) {
            // intentar refrescar calculadora via reflexión no necesario; MainView cachea vistas
          }
        } catch (Exception ignored) {}
      }),
      err -> Platform.runLater(() ->
        status.setText("No se pudo guardar la impresora."))
    );
  }

  private void guardarEdicion(UUID id, ImpresorasReqDto dto) {
    controller.update(
      id, dto,
      actualizada -> Platform.runLater(() -> {
        formSlot.getChildren().clear();
        cargarImpresoras();
      }),
      err -> Platform.runLater(() ->
        status.setText("No se pudo actualizar la impresora."))
    );
  }

  private void confirmarEliminar_PedirEdicion(UUID id) {
    Optional<ImpresorasResDto> dto = cache.stream()
      .filter(i -> i.id() != null && i.id().equals(id)).findFirst();
    dto.ifPresent(this::mostrarFormulario);
  }

  private void pedirConfirmacionEliminar(UUID id) {
    Optional<ImpresorasResDto> dto = cache.stream()
      .filter(i -> i.id() != null && i.id().equals(id)).findFirst();
    String nombre = dto.map(ImpresorasResDto::titulo).orElse("esta impresora");

    Alert confirmar = new Alert(AlertType.CONFIRMATION,
      "¿Eliminar \"" + nombre + "\"?", ButtonType.OK, ButtonType.CANCEL);
    confirmar.setHeaderText(null);
    confirmar.showAndWait()
      .filter(r -> r == ButtonType.OK)
      .ifPresent(r -> controller.delete(
        id,
        msg -> Platform.runLater(this::cargarImpresoras),
        err -> Platform.runLater(() ->
          status.setText("No se pudo eliminar la impresora."))
      ));
  }
}
