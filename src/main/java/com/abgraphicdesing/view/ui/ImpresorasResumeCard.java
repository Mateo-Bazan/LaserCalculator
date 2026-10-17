package com.abgraphicdesing.view.ui;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.function.Consumer;

import com.abgraphicdesing.model.dto.ImpresorasResDto;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class ImpresorasResumeCard extends VBox {
  private static final String SELECTED = "printer-card-selected";
  private static final NumberFormat MONEDA =
    NumberFormat.getIntegerInstance(Locale.forLanguageTag("es-AR"));

  private final ImpresorasResDto data;

  public ImpresorasResumeCard(ImpresorasResDto data, Consumer<ImpresorasResDto> onClick) {
    super(6);
    this.data = data;
    getStyleClass().add("printer-card");
    setPrefWidth(230);
    setMaxWidth(230);

    // Marca como "badge"
    Label marca = new Label(data.marca() == null ? "" : data.marca().toUpperCase());
    marca.getStyleClass().add("brand-badge");
    HBox cabecera = new HBox(marca);

    // Modelo como título
    Label titulo = new Label(data.modelo());
    titulo.getStyleClass().add("printer-card-title");

    getChildren().addAll(
      cabecera,
      titulo,
      fila("Tipo:", data.tipo()),
      fila("Costo Hora:", "$" + MONEDA.format(data.amortizacion()))
    );

    setOnMouseClicked(e -> onClick.accept(data));
  }

  /** Etiqueta a la izquierda, valor a la derecha. */
  private HBox fila(String etiqueta, String valor) {
    Label izq = new Label(etiqueta);
    izq.getStyleClass().add("printer-row-label");
    Label der = new Label(valor);
    der.getStyleClass().add("printer-row-value");
    Region espacio = new Region();
    HBox.setHgrow(espacio, Priority.ALWAYS);
    return new HBox(izq, espacio, der);
  }

  public ImpresorasResDto getData() {
    return data;
  }

  /** Agrega o quita el borde azul. */
  public void setSelected(boolean seleccionada) {
    getStyleClass().remove(SELECTED); // evita duplicados
    if (seleccionada) {
      getStyleClass().add(SELECTED);
    }
  }
}