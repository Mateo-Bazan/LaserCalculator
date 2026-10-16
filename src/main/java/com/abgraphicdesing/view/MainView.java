package com.abgraphicdesing.view;

import com.abgraphicdesing.controller.AjustesController;
import com.abgraphicdesing.controller.ImpresorasController;
import com.abgraphicdesing.controller.MaterialesController;
import com.abgraphicdesing.controller.PeriodosController;

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
  private final NavigationBar navigationBar;

  public MainView(
    ImpresorasController controller,
    MaterialesController materialController,
    AjustesController ajustesController,
    PeriodosController periodosController
  ) {
    this.impresoraController = controller;
    this.materialesController = materialController;
    this.ajustesController = ajustesController;
    this.periodosController = periodosController;

    getStyleClass().add("app-background");

    navigationBar = new NavigationBar();
    setTop(navigationBar);

    navigationBar.setOnCalculadora(() -> show("calculadora",
      new CalcualdoraView(impresoraController)));
    navigationBar.setOnImpresora(() -> show("impresoras",
      new ImpresorasView(impresoraController)));
    navigationBar.setOnMateriales(() -> show("materiales",
      new MaterialesView(materialesController)));
    navigationBar.setOnAjustes(() -> show("ajustes",
      new AjustesView(this.ajustesController, this.periodosController)));
    navigationBar.setOnHistorial(() -> show("historial", buildHistorialPlaceholder()));

    // Vista inicial
    show("calculadora", new CalcualdoraView(impresoraController));
  }

  private void show(String key, javafx.scene.Node view) {
    navigationBar.setActive(key);
    setCenter(view);
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
