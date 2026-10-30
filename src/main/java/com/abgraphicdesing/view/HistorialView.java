package com.abgraphicdesing.view;


import com.abgraphicdesing.controller.HistorialController;
import com.abgraphicdesing.model.entity.Historial;
import com.abgraphicdesing.view.util.AppLogger;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class HistorialView extends VBox {
  private final HistorialController controller;
  private final VBox lista = new VBox(12);
  private final Label status = new Label();

  public HistorialView(HistorialController controller) {
    this.controller = controller;
    getStyleClass().add("app-background");
    setPadding(new Insets(28, 36, 28, 36));

    Label titulo = new Label("Historial");
    titulo.getStyleClass().add("page-title");
    Label subtitulo = new Label("Registros guardados con fotografía histórica.");
    subtitulo.getStyleClass().add("page-subtitle");
    VBox encabezado = new VBox(2, titulo, subtitulo);

    status.getStyleClass().add("page-subtitle");

    ScrollPane scroll = new ScrollPane(lista);
    scroll.setFitToWidth(true);
    scroll.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
    scroll.getStyleClass().add("clean-scroll");
    VBox.setVgrow(scroll, Priority.ALWAYS);

    getChildren().addAll(encabezado, status, scroll);
    cargar();
  }

  private void cargar() {
    status.setText("Cargando...");
    controller.getAll(
      items -> Platform.runLater(() -> {
        lista.getChildren().clear();
        if (items == null || items.isEmpty()) {
          status.setText("No hay registros en el historial.");
          return;
        }
        status.setText("");
        for (Historial h : items) {
          lista.getChildren().add(crearTarjeta(h));
        }
      }),
      err -> Platform.runLater(() -> {
        status.setText("Error al cargar historial.");
        AppLogger.warn("Error al cargar historial: " + (err == null ? "" : err.getMessage()));
      })
    );
  }

  private VBox crearTarjeta(Historial h) {
    VBox card = new VBox(6);
    card.getStyleClass().add("content-card");

    String nombreImp = h.getImpresoraMarca() != null && h.getImpresoraModelo() != null
      ? h.getImpresoraMarca() + " - " + h.getImpresoraModelo()
      : (h.getImpresoraTitulo() != null ? h.getImpresoraTitulo() : "Impresora");
    if (h.getImpresoraTipo() != null && !h.getImpresoraTipo().isBlank()) {
      nombreImp = nombreImp + " (" + h.getImpresoraTipo() + ")";
    }

    String material = h.getMaterialNombre() != null ? h.getMaterialNombre() : "Sin material";
    if (h.getMaterialPrecioCosto() != null) {
      material = material + " - $/costo hist.: " + h.getMaterialPrecioCosto();
    }

    Label titulo = new Label(h.getTitulo() != null ? h.getTitulo() : "Sin título");
    titulo.getStyleClass().add("section-title");
    Label fecha = new Label(h.getFechaGuardado() != null ? h.getFechaGuardado() : "");
    fecha.getStyleClass().add("page-subtitle");
    Label imp = new Label("Impresora: " + nombreImp);
    Label mat = new Label("Material: " + material);
    Label tiempos = new Label("Tiempos: Grabado " + (h.getTiempoGrabado() != null ? h.getTiempoGrabado() : "-") + " / Mano obra " + (h.getTiempoObra() != null ? h.getTiempoObra() : "-"));
    Label datos = new Label("Cantidad: " + h.getCantidad() + " | Ganancia: " + h.getGanancia() + "% | Precio final: $" + h.getPrecioFinal());

    HBox header = new HBox(titulo);
    header.setAlignment(Pos.CENTER_LEFT);
    HBox top = new HBox(header);
    top.setAlignment(Pos.CENTER_LEFT);

    card.getChildren().addAll(top, fecha, imp, mat, tiempos, datos);
    return card;
  }
} 
