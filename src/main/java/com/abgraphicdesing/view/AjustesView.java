package com.abgraphicdesing.view;

import java.util.ArrayList;
import java.util.List;
import java.text.NumberFormat;
import java.util.Locale;

import com.abgraphicdesing.controller.AjustesController;
import com.abgraphicdesing.controller.PeriodosController;
import com.abgraphicdesing.model.dto.AjustesResDto;
import com.abgraphicdesing.view.ui.AjustesFormDialog;
import com.abgraphicdesing.view.util.Format;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/**
 * Ajustes segun la referencia: una fila por parametro (nombre + valor
 * editable + pill de periodo) y un unico boton Guardar Cambios.
 */
public class AjustesView extends VBox {

  private final AjustesController controller;
  private final PeriodosController periodosController;
  private final VBox filas = new VBox(16);
  private final Label status = new Label();
  private final Label footerInfo = new Label();
  private final Button btnGuardar = new Button("Guardar Cambios  →");
  private final List<FilaAjuste> filaAjustes = new ArrayList<>();

  public AjustesView(AjustesController controller, PeriodosController periodosController) {
    this.controller = controller;
    this.periodosController = periodosController;
    getStyleClass().add("app-background");
    setPadding(new Insets(28, 36, 28, 36));

    VBox card = new VBox(12);
    card.getStyleClass().add("content-card");
    card.setMaxWidth(1100);

    Label titulo = new Label("Ajustes");
    titulo.getStyleClass().add("page-title");
    Label subtitulo = new Label("Configuración general de costos y parámetros de producción");
    subtitulo.getStyleClass().add("page-subtitle");
    VBox encabezado = new VBox(2, titulo, subtitulo);

    Region spacer = new Region();
    HBox.setHgrow(spacer, Priority.ALWAYS);

    Button btnAgregar = new Button("+");
    btnAgregar.getStyleClass().add("btn-add");
    btnAgregar.setOnAction(e -> onAgregar());

    HBox header = new HBox(encabezado, spacer, btnAgregar);
    header.setAlignment(Pos.CENTER_LEFT);

    Separator separador = new Separator();
    separador.getStyleClass().add("header-separator");

    Label seccion = new Label("Costos de Producción");
    seccion.getStyleClass().add("section-title");
    Label seccionDesc = new Label(
      "Establezca los valores monetarios base para el cálculo automático de cotizaciones.");
    seccionDesc.getStyleClass().add("page-subtitle");
    VBox seccionBox = new VBox(2, seccion, seccionDesc);

    status.getStyleClass().add("page-subtitle");

    Separator separadorFooter = new Separator();
    separadorFooter.getStyleClass().add("header-separator");

    footerInfo.getStyleClass().add("footer-info");
    btnGuardar.getStyleClass().add("btn-primary");
    btnGuardar.setOnAction(e -> onGuardarCambios());
    Region footerSpacer = new Region();
    HBox.setHgrow(footerSpacer, Priority.ALWAYS);
    HBox footer = new HBox(footerInfo, footerSpacer, btnGuardar);
    footer.setAlignment(Pos.CENTER_LEFT);

    card.getChildren().addAll(header, separador, seccionBox,
      status, filas, separadorFooter, footer);

    HBox centrado = new HBox(card);
    centrado.setAlignment(Pos.TOP_CENTER);
    HBox.setHgrow(card, Priority.ALWAYS);

    ScrollPane scroll = new ScrollPane(centrado);
    scroll.setFitToWidth(true);
    scroll.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
    scroll.getStyleClass().add("clean-scroll");
    VBox.setVgrow(scroll, Priority.ALWAYS);

    getChildren().add(scroll);
    cargarAjustes();
  }

  // ---------- Datos ----------

  private void cargarAjustes() {
    status.setText("Cargando...");
    controller.getAll(
      lista -> Platform.runLater(() -> {
        filaAjustes.clear();
        filas.getChildren().clear();
        if (lista != null) {
          for (Ajustes a : lista) {
            FilaAjuste fila = new FilaAjuste(a, this::onEliminar, this::actualizarEstado);
            filaAjustes.add(fila);
            filas.getChildren().add(fila);
          }
        }
        status.setText(filaAjustes.isEmpty() ? "No hay parámetros cargados." : "");
        actualizarEstado();
      }),
      err -> Platform.runLater(() ->
        status.setText("No se pudieron cargar los ajustes."))
    );
  }

  private void actualizarEstado() {
    footerInfo.setText("ⓘ " + filaAjustes.size() + " parámetros de producción");
    boolean valido = filaAjustes.stream().allMatch(FilaAjuste::esValido);
    btnGuardar.setDisable(!valido);
  }

  // ---------- Acciones ----------

  private void onGuardarCambios() {
    List<FilaAjuste> modificadas = filaAjustes.stream()
      .filter(FilaAjuste::fueModificada).toList();
    if (modificadas.isEmpty()) {
      status.setText("No hay cambios para guardar.");
      return;
    }
    guardarSiguiente(modificadas, 0);
  }

  private void guardarSiguiente(List<FilaAjuste> pendientes, int indice) {
    if (indice >= pendientes.size()) {
      cargarAjustes();
      return;
    }
    FilaAjuste fila = pendientes.get(indice);
    controller.update(
      fila.getAjusteId(), fila.comoDto(),
      ok -> Platform.runLater(() -> guardarSiguiente(pendientes, indice + 1)),
      err -> Platform.runLater(() ->
        status.setText("No se pudo guardar \"" + fila.getNombre() + "\"."))
    );
  }

  private void onAgregar() {
    periodosController.getAll(
      periodos -> Platform.runLater(() ->
        new AjustesFormDialog(periodos, null).showAndWait()
          .ifPresent(dto -> controller.create(
            dto,
            ok -> Platform.runLater(this::cargarAjustes),
            err -> Platform.runLater(() ->
              status.setText("No se pudo crear el ajuste."))))),
      err -> Platform.runLater(() ->
        status.setText("No se pudieron cargar los periodos."))
    );
  }

  private void onEliminar(FilaAjuste fila) {
    Alert confirmar = new Alert(AlertType.CONFIRMATION,
      "¿Eliminar \"" + fila.getNombre() + "\"?",
      ButtonType.OK, ButtonType.CANCEL);
    confirmar.setHeaderText(null);
    confirmar.showAndWait()
      .filter(r -> r == ButtonType.OK)
      .ifPresent(r -> controller.delete(
        fila.getAjusteId(),
        msg -> Platform.runLater(this::cargarAjustes),
        err -> Platform.runLater(() ->
          status.setText("No se pudo eliminar el ajuste."))));
  }

  // ---------- Fila ----------

  private static class FilaAjuste extends HBox {
    private final Ajustes ajuste;
    private final TextField txtGasto = new TextField();
    private final int gastoOriginal;

    FilaAjuste(Ajustes ajuste, java.util.function.Consumer<FilaAjuste> onEliminar,
        Runnable onCambio) {
      super(12);
      this.ajuste = ajuste;
      this.gastoOriginal = ajuste.getGasto();
      setAlignment(Pos.CENTER_LEFT);

      Label nombre = new Label(Format.texto(ajuste.getNombre()));
      nombre.getStyleClass().add("ajuste-name");
      nombre.setMaxWidth(420);
      nombre.setWrapText(true);

      Region spacer = new Region();
      HBox.setHgrow(spacer, Priority.ALWAYS);

      txtGasto.setText(Format.miles(ajuste.getGasto()));
      txtGasto.getStyleClass().add("form-input");
      txtGasto.setPrefWidth(200);
      txtGasto.setMaxWidth(200);
      txtGasto.textProperty().addListener((obs, o, n) -> {
        txtGasto.getStyleClass().remove("form-input-error");
        if (!esValido()) {
          txtGasto.getStyleClass().add("form-input-error");
        }
        onCambio.run();
      });

      Label pill = new Label(formatoPeriodo(ajuste.getPeriodo()));
      pill.getStyleClass().add("unit-pill");

      Button del = new Button("🗑");
      del.getStyleClass().add("btn-icon-delete");
      del.setOnAction(e -> onEliminar.accept(this));

      getChildren().addAll(nombre, spacer, txtGasto, pill, del);
    }

    private static String formatoPeriodo(String periodo) {
      if (periodo == null || periodo.isBlank()) return "--";
      String p = periodo.trim();
      return p.startsWith("/") ? p : "/" + p;
    }

    boolean esValido() {
      return valorActual() >= 0;
    }

    boolean fueModificada() {
      int actual = valorActual();
      return actual >= 0 && actual != gastoOriginal;
    }

    /** Acepta el formato con separador de miles que se muestra. */
    private int valorActual() {
      try {
        return Integer.parseInt(txtGasto.getText().trim().replace(".", ""));
      } catch (NumberFormatException e) {
        return -1;
      }
    }

    int getAjusteId() {
      return ajuste.getId();
    }

    String getNombre() {
      return ajuste.getNombre();
    }

    com.abgraphicdesing.model.dto.AjustesReqDto comoDto() {
      return new com.abgraphicdesing.model.dto.AjustesReqDto(
        ajuste.getNombre(),
        valorActual(),
        ajuste.getIdPeriodo());
    }
  }
}
