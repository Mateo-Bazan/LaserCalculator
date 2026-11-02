package com.abgraphicdesing.view;

import com.abgraphicdesing.controller.AjustesController;
import com.abgraphicdesing.controller.CalculosController;
import com.abgraphicdesing.controller.HistorialController;
import com.abgraphicdesing.controller.ImpresorasController;
import com.abgraphicdesing.controller.MaterialesController;
import com.abgraphicdesing.controller.PeriodosController;
import com.abgraphicdesing.controller.TiposController;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class MainView extends BorderPane {
  private final ImpresorasController impresoraController;
  private final MaterialesController materialesController;
  private final AjustesController ajustesController;
  private final PeriodosController periodosController;
  private final CalculosController calculosController;
  private final TiposController tiposController;
  private final HistorialController historialController;
  private final NavigationBar navigationBar;
  private final java.util.Map<String, javafx.scene.Node> viewCache = new java.util.LinkedHashMap<>();

  public MainView(
    ImpresorasController controller,
    MaterialesController materialController,
    AjustesController ajustesController,
    PeriodosController periodosController,
    CalculosController calculosController,
    TiposController tiposController,
    HistorialController historialController
  ) {
    this.impresoraController = controller;
    this.materialesController = materialController;
    this.ajustesController = ajustesController;
    this.periodosController = periodosController;
    this.calculosController = calculosController;
    this.tiposController = tiposController;
    this.historialController = historialController;

    getStyleClass().add("app-background");

    navigationBar = new NavigationBar();
    setTop(navigationBar);

    navigationBar.setOnCalculadora(() -> showCalculadora());

    navigationBar.setOnImpresora(() -> showImpresoras());
    navigationBar.setOnMateriales(() -> showMateriales());
    navigationBar.setOnAjustes(() -> showAjustes());
    navigationBar.setOnHistorial(() -> showHistorial());

    // Vista inicial
    showCalculadora();
  }

  private void show(String key, javafx.scene.Node view) {
    navigationBar.setActive(key);
    setCenter(view);
  }

  private void showCalculadora() {
    String key = "calculadora";
    javafx.scene.Node view = viewCache.get(key);
    if (view == null) {
      view = nuevaCalculadora();
      viewCache.put(key, view);
    } else if (view instanceof com.abgraphicdesing.view.CalcualdoraView cv) {
      try {
        cv.refrescarImpresoras();
      } catch (Exception ignored) {}
    }
    show(key, view);
  }

  private void showImpresoras() {
    String key = "impresoras";
    javafx.scene.Node view = viewCache.get(key);
    if (view == null) {
      view = new ImpresorasView(impresoraController, tiposController);
      viewCache.put(key, view);
    }
    show(key, view);
  }

  private void showMateriales() {
    String key = "materiales";
    javafx.scene.Node view = viewCache.get(key);
    if (view == null) {
      view = new MaterialesView(materialesController);
      viewCache.put(key, view);
    }
    show(key, view);
  }

  private void showAjustes() {
    String key = "ajustes";
    javafx.scene.Node view = viewCache.get(key);
    if (view == null) {
      view = new AjustesView(this.ajustesController, this.periodosController);
      viewCache.put(key, view);
    }
    show(key, view);
  }

  private void showHistorial() {
    String key = "historial";
    javafx.scene.Node view = viewCache.get(key);
    if (view == null) {
      try {
        view = new com.abgraphicdesing.view.HistorialView(historialController);
      } catch (Exception e) {
        view = buildHistorialPlaceholder();
      }
      viewCache.put(key, view);
    }
    show(key, view);
  }

  private CalcualdoraView nuevaCalculadora() {
    return new CalcualdoraView(impresoraController, materialesController, calculosController, historialController);
  }

  /**
   * Marcador de posicion para Historial: el backend aun no expone
   * datos de historial (HistorialController vacio), asi que no se
   * muestran datos ficticios.
   */
  private VBox buildHistorialPlaceholder() {
    VBox box = new VBox(8);
    box.setAlignment(Pos.CENTER);
    box.setPadding(new Insets(60));
    box.getStyleClass().add("app-background");

    Label titulo = new Label("Historial");
    titulo.getStyleClass().add("page-title");
    Label subtitulo = new Label("Esta sección estará disponible próximamente.");
    subtitulo.getStyleClass().add("page-subtitle");

    box.getChildren().addAll(titulo, subtitulo);
    return box;
  }
}
