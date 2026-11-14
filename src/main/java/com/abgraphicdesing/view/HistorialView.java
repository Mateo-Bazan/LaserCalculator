package com.abgraphicdesing.view;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import com.abgraphicdesing.controller.HistorialController;
import com.abgraphicdesing.model.entity.Historial;
import com.abgraphicdesing.view.util.AppLogger;
import com.abgraphicdesing.view.util.Format;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/** Historial de presupuestos: tarjetas con filtro por titulo y rango de fechas. */
public class HistorialView extends VBox {

  private static final DateTimeFormatter FORMATO_DB =
    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
  private static final DateTimeFormatter FORMATO_UI =
    DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

  private final HistorialController controller;
  private final VBox lista = new VBox(14);
  private final Label status = new Label();
  private final Label footerCount = new Label();

  private final TextField txtBuscar = new TextField();
  private final DatePicker dpDesde = new DatePicker();
  private final DatePicker dpHasta = new DatePicker();

  // Todos los registros traidos de la base. Los filtros trabajan sobre esta lista.
  private final List<Historial> todos = new ArrayList<>();

  public HistorialView(HistorialController controller) {
    this.controller = controller;
    getStyleClass().add("app-background");
    setPadding(new Insets(28, 36, 28, 36));

    // ----- Encabezado (igual que las otras vistas) -----
    Label titulo = new Label("Historial");
    titulo.getStyleClass().add("page-title");
    Label subtitulo = new Label("Registros guardados con fotografía histórica.");
    subtitulo.getStyleClass().add("page-subtitle");
    VBox encabezado = new VBox(2, titulo, subtitulo);

    Separator separador = new Separator();
    separador.getStyleClass().add("header-separator");

    // ----- Filtros -----
    txtBuscar.setPromptText("Buscar por título...");
    txtBuscar.getStyleClass().add("search-input");
    txtBuscar.setPrefWidth(260);

    configurarDatePicker(dpDesde, "Desde");
    configurarDatePicker(dpHasta, "Hasta");

    Button btnLimpiar = new Button("Limpiar");
    btnLimpiar.getStyleClass().add("btn-secondary");
    btnLimpiar.setOnAction(e -> limpiarFiltros());

    HBox filtros = new HBox(12, txtBuscar, dpDesde, dpHasta, btnLimpiar);
    filtros.setAlignment(Pos.CENTER_LEFT);

    // Cada vez que cambia un filtro, se vuelve a filtrar en memoria
    txtBuscar.textProperty().addListener((obs, o, n) -> aplicarFiltros());
    dpDesde.valueProperty().addListener((obs, o, n) -> aplicarFiltros());
    dpHasta.valueProperty().addListener((obs, o, n) -> aplicarFiltros());

    status.getStyleClass().add("page-subtitle");
    footerCount.getStyleClass().add("footer-count");

    // ----- Contenido centrado con ancho maximo -----
    VBox contenido = new VBox(12, encabezado, separador, filtros, status, lista, footerCount);
    contenido.setMaxWidth(1100);

    HBox centrado = new HBox(contenido);
    centrado.setAlignment(Pos.TOP_CENTER);
    HBox.setHgrow(contenido, Priority.ALWAYS);

    ScrollPane scroll = new ScrollPane(centrado);
    scroll.setFitToWidth(true);
    scroll.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
    scroll.getStyleClass().add("clean-scroll");
    VBox.setVgrow(scroll, Priority.ALWAYS);

    getChildren().add(scroll);
    cargar();
  }

  public void recargar() {
    cargar();
  }

  // ---------- Datos ----------

  private void cargar() {
    status.setText("Cargando...");
    controller.getAll(
      items -> Platform.runLater(() -> {
        todos.clear();
        if (items != null) todos.addAll(items);
        aplicarFiltros();
      }),
      err -> Platform.runLater(() -> {
        status.setText("Error al cargar historial.");
        AppLogger.warn("Error al cargar historial: " + (err == null ? "" : err.getMessage()));
      })
    );
  }

  // ---------- Filtros ----------

  private void configurarDatePicker(DatePicker dp, String prompt) {
    dp.setPromptText(prompt);
    dp.setEditable(false); // se elige desde el calendario: evita fechas mal tipeadas
    dp.getStyleClass().add("date-filter");
    dp.setPrefWidth(150);
  }

  private void limpiarFiltros() {
    txtBuscar.clear();
    dpDesde.setValue(null);
    dpHasta.setValue(null);
    // No hace falta llamar a aplicarFiltros(): los listeners ya lo hacen
  }

  private void aplicarFiltros() {
    String q = txtBuscar.getText() == null ? "" : txtBuscar.getText().trim().toLowerCase();
    LocalDate desde = dpDesde.getValue();
    LocalDate hasta = dpHasta.getValue();

    List<Historial> filtrados = todos.stream()
      .filter(h -> coincideTitulo(h, q))
      .filter(h -> enRango(h, desde, hasta))
      .toList();

    repintar(filtrados);
  }

  private boolean coincideTitulo(Historial h, String q) {
    if (q.isEmpty()) return true;
    return h.getTitulo() != null && h.getTitulo().toLowerCase().contains(q);
  }

  private boolean enRango(Historial h, LocalDate desde, LocalDate hasta) {
    if (desde == null && hasta == null) return true;
    LocalDateTime fecha = parsearFecha(h.getFechaGuardado());
    if (fecha == null) return false; // sin fecha valida no se puede ubicar en el rango
    LocalDate dia = fecha.toLocalDate();
    if (desde != null && dia.isBefore(desde)) return false;
    if (hasta != null && dia.isAfter(hasta)) return false;
    return true;
  }

  private static LocalDateTime parsearFecha(String texto) {
    if (texto == null || texto.isBlank()) return null;
    try {
      return LocalDateTime.parse(texto.trim(), FORMATO_DB);
    } catch (DateTimeParseException e) {
      return null;
    }
  }

  // ---------- Pintado ----------

  private void repintar(List<Historial> items) {
    lista.getChildren().clear();
    for (Historial h : items) {
      lista.getChildren().add(crearTarjeta(h));
    }

    if (todos.isEmpty()) {
      status.setText("No hay registros en el historial.");
    } else if (items.isEmpty()) {
      status.setText("Ningún registro coincide con los filtros.");
    } else {
      status.setText("");
    }
    footerCount.setText("Mostrando " + items.size() + " de " + todos.size() + " registros");
  }

  private VBox crearTarjeta(Historial h) {
    // --- Zona principal: titulo + fecha a la izquierda, precio a la derecha ---
    Label titulo = new Label(Format.texto(h.getTitulo()));
    titulo.getStyleClass().add("historial-title");
    titulo.setWrapText(true);

    LocalDateTime fechaDt = parsearFecha(h.getFechaGuardado());
    Label impresora = new Label(nombreImpresora(h));
    impresora.getStyleClass().add("historial-printer");

    Label fecha = new Label(fechaDt != null
      ? fechaDt.format(FORMATO_UI)
      : Format.texto(h.getFechaGuardado()));
    fecha.getStyleClass().add("historial-date");

    VBox izquierda = new VBox(4, titulo, impresora, fecha);

    Label precioCaption = new Label("PRECIO FINAL");
    precioCaption.getStyleClass().add("historial-price-caption");
    Label precio = new Label(Format.moneda(h.getPrecioFinal()));
    precio.getStyleClass().add("historial-price");
    VBox derecha = new VBox(0, precioCaption, precio);
    derecha.setAlignment(Pos.TOP_RIGHT);

    Region spacer = new Region();
    HBox.setHgrow(spacer, Priority.ALWAYS);
    HBox principal = new HBox(12, izquierda, spacer, derecha);
    principal.setAlignment(Pos.TOP_LEFT);

    Separator sep = new Separator();
    sep.getStyleClass().add("card-separator");

    // --- Pill de material ---
    FlowPane pills = new FlowPane(8, 6);
    Label pillMaterial = new Label(nombreMaterial(h));
    pillMaterial.getStyleClass().add("historial-material-pill");
    pills.getChildren().add(pillMaterial);

    // --- Datos secundarios ---
    FlowPane datos = new FlowPane(28, 8);
    datos.getChildren().addAll(
      dato("Cantidad", String.valueOf(h.getCantidad())),
      dato("Ganancia", h.getGanancia() + "%"),
      dato("Costo", h.getPrecioCosto() == null ? "--" : Format.moneda(h.getPrecioCosto())),
      dato("Grabado", Format.texto(h.getTiempoGrabado())),
      dato("Mano de obra", Format.texto(h.getTiempoObra()))
    );

    VBox card = new VBox(10, principal, sep, pills, datos);
    card.getStyleClass().add("historial-card");
    return card;
  }

  /** Mini bloque "etiqueta chica arriba, valor abajo". */
  private VBox dato(String etiqueta, String valor) {
    Label caption = new Label(etiqueta);
    caption.getStyleClass().add("stat-caption");
    Label value = new Label(valor);
    value.getStyleClass().add("stat-value");
    return new VBox(1, caption, value);
  }

  private String nombreImpresora(Historial h) {
    String base;
    if (h.getImpresoraMarca() != null && h.getImpresoraModelo() != null) {
      base = h.getImpresoraMarca().toUpperCase() + " - " + h.getImpresoraModelo();
    } else {
      base = h.getImpresoraTitulo() != null ? h.getImpresoraTitulo() : "Impresora";
    }
    if (h.getImpresoraTipo() != null && !h.getImpresoraTipo().isBlank()) {
      base += " (" + h.getImpresoraTipo() + ")";
    }
    return base;
  }

  private String nombreMaterial(Historial h) {
    return h.getMaterialNombre() != null ? h.getMaterialNombre() : "Sin material";
  }
}