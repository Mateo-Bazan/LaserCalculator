package com.abgraphicdesing.view.ui;

import java.util.UUID;
import java.util.function.Consumer;

import com.abgraphicdesing.model.dto.ImpresorasResDto;
import com.abgraphicdesing.view.util.Format;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/** Tarjeta de impresora segun la referencia (badge, titulo, filas, seleccion). */
public class ImpresoraFullCard extends VBox {

  private final StackPane overlay = new StackPane();
  private final VBox card = new VBox(4);
  private final Button btnEditar = new Button("✎");
  private final Button btnEliminar = new Button("🗑");

  public ImpresoraFullCard(ImpresorasResDto dto) {
    card.getStyleClass().add("printer-card");
    card.setPrefWidth(270);

    Label marca = new Label(Format.texto(dto.marca()).toUpperCase());
    marca.getStyleClass().add("brand-badge");
    HBox badgeRow = new HBox(marca);
    badgeRow.setAlignment(Pos.CENTER);

    Label titulo = new Label(Format.texto(dto.titulo()));
    titulo.getStyleClass().add("printer-card-title");
    titulo.setWrapText(true);
    titulo.setAlignment(Pos.CENTER);
    titulo.setMaxWidth(Double.MAX_VALUE);
    titulo.setAlignment(Pos.CENTER);

    card.getChildren().addAll(
      badgeRow,
      titulo,
      fila("Modelo:", Format.texto(dto.modelo())),
      fila("Tipo:", Format.texto(dto.tipo())),
      separador(),
      fila("Vida útil:", Format.hs(dto.vidaUtil())),
      fila("Precio Repuestos:", Format.moneda(dto.precioRespuesto())),
      fila("Precio de Compra:", Format.moneda(dto.precioCompra())),
      fila("Amortización (hs):", Format.moneda(dto.amortizacion())),
      fila("Gasto energético:", Format.watts(dto.watts()))
    );

    btnEditar.getStyleClass().add("btn-icon-edit");
    btnEditar.setVisible(false);
    btnEliminar.getStyleClass().add("btn-icon-delete");
    btnEliminar.setVisible(false);

    overlay.getChildren().add(card);
    overlay.getChildren().addAll(btnEditar, btnEliminar);
    StackPane.setAlignment(btnEditar, Pos.BOTTOM_LEFT);
    StackPane.setAlignment(btnEliminar, Pos.BOTTOM_RIGHT);
    btnEditar.setTranslateY(14);
    btnEliminar.setTranslateY(14);

    setPadding(new Insets(6, 2, 16, 2));
    getChildren().add(overlay);

    UUID id = dto.id();
    setOnMouseClicked(e -> {
      getProperties().put("impresoraId", id);
      // La vista padre escucha esta propiedad para gestionar la seleccion.
      fireEvent(new SelectionEvent(id));
      e.consume();
    });
  }

  private HBox fila(String etiqueta, String valor) {
    Label l = new Label(etiqueta);
    l.getStyleClass().add("printer-row-label");
    Label v = new Label(valor);
    v.getStyleClass().add("printer-row-value");
    Region spacer = new Region();
    HBox.setHgrow(spacer, Priority.ALWAYS);
    HBox row = new HBox(l, spacer, v);
    row.setAlignment(Pos.CENTER_LEFT);
    return row;
  }

  private Separator separador() {
    Separator s = new Separator();
    s.getStyleClass().add("card-separator");
    return s;
  }

  public void setSelected(boolean selected) {
    if (selected) {
      if (!card.getStyleClass().contains("printer-card-selected")) {
        card.getStyleClass().add("printer-card-selected");
      }
    } else {
      card.getStyleClass().remove("printer-card-selected");
    }
    btnEditar.setVisible(selected);
    btnEliminar.setVisible(selected);
  }

  public void setOnEdit(Consumer<UUID> action) {
    btnEditar.setOnAction(e -> {
      e.consume();
      action.accept((UUID) getProperties().get("impresoraId"));
    });
  }

  public void setOnDelete(Consumer<UUID> action) {
    btnEliminar.setOnAction(e -> {
      e.consume();
      action.accept((UUID) getProperties().get("impresoraId"));
    });
  }

  /** Evento simple para avisar a la vista que esta tarjeta fue clickeada. */
  public static class SelectionEvent extends javafx.event.Event {
    public static final javafx.event.EventType<SelectionEvent> TYPE =
      new javafx.event.EventType<>("impresora-selected");
    private final UUID id;

    public SelectionEvent(UUID id) {
      super(TYPE);
      this.id = id;
    }

    public UUID getImpresoraId() {
      return id;
    }
  }
}
