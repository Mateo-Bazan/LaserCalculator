package com.abgraphicdesing.view.ui;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import com.abgraphicdesing.model.dto.ImpresorasReqDto;
import com.abgraphicdesing.model.dto.ImpresorasResDto;
import com.abgraphicdesing.view.util.TiposLookup;

import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * Panel de alta/edicion de impresora segun la referencia (grid de 3 columnas
 * + GUARDAR / CANCELAR). En edicion el tipo se conserva (el backend lo ignora
 * en el merge) y se muestra deshabilitado.
 */
public class ImpresoraFormPanel extends VBox {

  private final TextField txtMarca = new TextField();
  private final TextField txtModelo = new TextField();
  private final TextField txtTitulo = new TextField();
  private final ComboBox<String> cbTipo = new ComboBox<>();
  private final TextField txtVidaUtil = new TextField();
  private final TextField txtWatts = new TextField();
  private final TextField txtPrecioRepuesto = new TextField();
  private final TextField txtPrecioCompra = new TextField();
  private final Label error = new Label();

  private final boolean modoEdicion;
  private final Map<String, Integer> tiposConocidos;
  private Consumer<ImpresorasReqDto> onGuardar = dto -> { };
  private Runnable onCancelar = () -> { };

  public ImpresoraFormPanel(List<String> tipos, ImpresorasResDto existente) {
    this.modoEdicion = existente != null;
    this.tiposConocidos = new TiposLookup().cargar();

    cbTipo.setEditable(true);
    cbTipo.getItems().setAll(tipos);
    cbTipo.setMaxWidth(Double.MAX_VALUE);

    txtMarca.setPromptText("Marca...");
    txtModelo.setPromptText("Modelo...");
    txtTitulo.setPromptText("Título...");
    txtVidaUtil.setPromptText("Vida útil...");
    txtWatts.setPromptText("Gasto energético...");
    txtPrecioRepuesto.setPromptText("Precio de repuestos...");
    txtPrecioCompra.setPromptText("Precio de Compra...");

    for (TextField t : List.of(txtMarca, txtModelo, txtTitulo, txtVidaUtil,
        txtWatts, txtPrecioRepuesto, txtPrecioCompra)) {
      t.getStyleClass().add("form-input");
      t.setMaxWidth(Double.MAX_VALUE);
    }
    cbTipo.getStyleClass().add("form-input");

    if (existente != null) {
      txtMarca.setText(orEmpty(existente.marca()));
      txtModelo.setText(orEmpty(existente.modelo()));
      txtTitulo.setText(orEmpty(existente.titulo()));
      cbTipo.setValue(existente.tipo());
      cbTipo.setDisable(true);
      txtVidaUtil.setText(String.valueOf(existente.vidaUtil()));
      txtWatts.setText(String.valueOf(existente.watts()));
      txtPrecioRepuesto.setText(String.valueOf(existente.precioRespuesto()));
      txtPrecioCompra.setText(String.valueOf(existente.precioCompra()));
    }

    GridPane grid = new GridPane();
    grid.setHgap(24);
    grid.setVgap(12);
    for (int i = 0; i < 3; i++) {
      ColumnConstraints cc = new ColumnConstraints();
      cc.setHgrow(Priority.ALWAYS);
      cc.setPercentWidth(33.3);
      cc.setHalignment(HPos.LEFT);
      grid.getColumnConstraints().add(cc);
    }

    grid.add(campo("Marca", txtMarca), 0, 0);
    grid.add(campo("Modelo", txtModelo), 1, 0);
    grid.add(campo("Título", txtTitulo), 2, 0);
    grid.add(campo("Tipo", cbTipo), 0, 1);
    grid.add(campo("Vida útil", txtVidaUtil), 1, 1);
    grid.add(campo("Gasto energético", txtWatts), 2, 1);
    grid.add(campo("Precio de repuestos", txtPrecioRepuesto), 0, 2);
    grid.add(campo("Precio de Compra", txtPrecioCompra), 1, 2);

    Button btnGuardar = new Button("GUARDAR");
    btnGuardar.getStyleClass().add("btn-primary");
    Button btnCancelar = new Button("CANCELAR");
    btnCancelar.getStyleClass().add("btn-danger");
    HBox botones = new HBox(12, btnGuardar, btnCancelar);
    botones.setAlignment(Pos.CENTER_RIGHT);

    error.getStyleClass().add("form-error");
    error.setVisible(false);
    error.setWrapText(true);

    btnGuardar.setOnAction(e -> intentarGuardar());
    btnCancelar.setOnAction(e -> onCancelar.run());

    getStyleClass().add("form-panel");
    setSpacing(16);
    getChildren().addAll(grid, error, botones);
  }

  private VBox campo(String etiqueta, javafx.scene.Node input) {
    Label l = new Label(etiqueta);
    l.getStyleClass().add("form-label");
    VBox box = new VBox(6, l, input);
    box.setMaxWidth(Double.MAX_VALUE);
    return box;
  }

  private String orEmpty(String s) {
    return s == null ? "" : s;
  }

  private void intentarGuardar() {
    error.setVisible(false);

    String marca = txtMarca.getText().trim();
    String modelo = txtModelo.getText().trim();
    String titulo = txtTitulo.getText().trim();
    String tipo = cbTipo.getValue() == null ? "" : cbTipo.getValue().trim();

    if (marca.isEmpty() || modelo.isEmpty() || titulo.isEmpty() || tipo.isEmpty()) {
      mostrarError("Completá marca, modelo, título y tipo.");
      return;
    }

    int vidaUtil, watts;
    float precioRepuesto, precioCompra;
    try {
      vidaUtil = Integer.parseInt(txtVidaUtil.getText().trim());
      watts = Integer.parseInt(txtWatts.getText().trim());
      precioRepuesto = Float.parseFloat(txtPrecioRepuesto.getText().trim());
      precioCompra = Float.parseFloat(txtPrecioCompra.getText().trim());
      if (vidaUtil <= 0 || watts < 0 || precioRepuesto < 0 || precioCompra < 0) {
        throw new NumberFormatException();
      }
    } catch (NumberFormatException ex) {
      mostrarError("Vida útil, gasto energético y precios deben ser números válidos (vida útil mayor a 0).");
      return;
    }

    int idTipo;
    if (modoEdicion) {
      idTipo = 0; // el backend conserva el tipo existente en la edicion
    } else {
      var resuelto = new TiposLookup().idPara(tiposConocidos, tipo);
      if (resuelto.isEmpty()) {
        mostrarError("El tipo \"" + tipo + "\" no existe en el catálogo y el backend "
          + "no permite crear tipos nuevos: elegí uno de la lista.");
        return;
      }
      idTipo = resuelto.get();
    }

    onGuardar.accept(new ImpresorasReqDto(
      idTipo, marca, modelo, titulo, vidaUtil, watts, precioCompra, precioRepuesto));
  }

  private void mostrarError(String mensaje) {
    error.setText(mensaje);
    error.setVisible(true);
  }

  public void setOnGuardar(Consumer<ImpresorasReqDto> action) {
    this.onGuardar = action;
  }

  public void setOnCancelar(Runnable action) {
    this.onCancelar = action;
  }
}
