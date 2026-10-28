package com.abgraphicdesing.view.ui;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Consumer;

import com.abgraphicdesing.controller.TiposController;
import com.abgraphicdesing.model.dto.ImpresorasReqDto;
import com.abgraphicdesing.model.dto.ImpresorasResDto;
import com.abgraphicdesing.model.dto.TiposResDto;

import javafx.application.Platform;

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
 * + GUARDAR / CANCELAR). En edicion los campos se precargan con los datos
 * de la impresora y el tipo puede cambiarse (el backend lo aplica en merge).
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

  private final Map<String, Integer> tiposConocidos = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
  private Consumer<ImpresorasReqDto> onGuardar = dto -> { };
  private Runnable onCancelar = () -> { };

  public ImpresoraFormPanel(ImpresorasResDto existente, TiposController tiposController) {
    cbTipo.setEditable(false);
    cbTipo.setMaxWidth(Double.MAX_VALUE);
    cbTipo.setPromptText("Cargando tipos...");

    if (tiposController != null) {
      tiposController.getAll(
        lista -> Platform.runLater(() -> {
          tiposConocidos.clear();
          if (lista != null) {
            for (TiposResDto t : lista) {
              if (t != null && t.tipo() != null && !t.tipo().isBlank()) {
                tiposConocidos.put(t.tipo().trim(), t.id());
              }
            }
          }
          cbTipo.getItems().setAll(tiposConocidos.keySet());
          cbTipo.setPromptText(tiposConocidos.isEmpty() ? "No hay tipos en la base" : "Elegí un tipo");
          if (existente != null) {
            cbTipo.setValue(existente.tipo());
          }
        }),
        err -> Platform.runLater(() -> {
          cbTipo.setPromptText("No hay tipos en la base");
        })
      );
    } else {
      cbTipo.setPromptText("No hay tipos en la base");
    }

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
      txtVidaUtil.setText(String.valueOf(existente.vidaUtil()));
      txtWatts.setText(String.valueOf(existente.watts()));
      txtPrecioRepuesto.setText(formatearDecimal(existente.precioRespuesto()));
      txtPrecioCompra.setText(formatearDecimal(existente.precioCompra()));
      // El valor se setea tras cargar tipos si es necesario
      if (cbTipo.getItems().contains(existente.tipo())) {
        cbTipo.setValue(existente.tipo());
      }
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
    grid.add(campo("Vida útil (hs)", txtVidaUtil), 1, 1);
    grid.add(campo("Gasto energético (W)", txtWatts), 2, 1);
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
    error.setManaged(false);
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

  // ---------- Numeros ----------

  /**
   * Float -> texto legible: sin notacion cientifica y con coma decimal
   * (15000000 en vez de 1.5E7, 1,5 en vez de 1.5).
   */
  private static String formatearDecimal(float valor) {
    return new BigDecimal(Float.toString(valor))
      .stripTrailingZeros()
      .toPlainString()
      .replace('.', ',');
  }

  /**
   * Texto -> entero. Acepta separador de miles con punto ("1.500" = 1500).
   * Lanza NumberFormatException si no es un entero valido (o es negativo).
   */
  private static int parsearEntero(String texto) {
    String t = texto.trim().replace(".", "");
    if (!t.matches("\\d+")) throw new NumberFormatException(texto);
    return Integer.parseInt(t);
  }

  /**
   * Texto -> decimal, pensado para es-AR:
   * "1.500,50" -> 1500.5 | "1500,5" -> 1500.5 | "1.500" -> 1500 | "12.5" -> 12.5
   * Lanza NumberFormatException si no es valido (o es negativo).
   */
  private static float parsearDecimal(String texto) {
    String t = texto.trim().replace("$", "").replace(" ", "");
    if (t.contains(",")) {
      t = t.replace(".", "").replace(",", ".");       // coma = decimal
    } else if (t.matches("\\d{1,3}(\\.\\d{3})+")) {
      t = t.replace(".", "");                          // puntos = miles
    }
    if (!t.matches("\\d+(\\.\\d+)?")) throw new NumberFormatException(texto);
    return Float.parseFloat(t);
  }

  // ---------- Guardado ----------

  private void intentarGuardar() {
    ocultarError();

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
      vidaUtil = parsearEntero(txtVidaUtil.getText());
      watts = parsearEntero(txtWatts.getText());
      precioRepuesto = parsearDecimal(txtPrecioRepuesto.getText());
      precioCompra = parsearDecimal(txtPrecioCompra.getText());
      if (vidaUtil <= 0) throw new NumberFormatException("vida util");
    } catch (NumberFormatException ex) {
      mostrarError("Vida útil, gasto energético y precios deben ser números válidos "
        + "(la vida útil debe ser mayor a 0).");
      return;
    }

    Integer resuelto = tiposConocidos.get(tipo);
    if (resuelto == null) {
      // Buscar case-insensitive
      for (Map.Entry<String, Integer> entry : tiposConocidos.entrySet()) {
        if (entry.getKey() != null && entry.getKey().equalsIgnoreCase(tipo)) {
          resuelto = entry.getValue();
          break;
        }
      }
    }
    if (resuelto == null) {
      mostrarError("Elegí un tipo de la lista.");
      return;
    }
    int idTipo = resuelto;

    onGuardar.accept(new ImpresorasReqDto(
      idTipo, marca, modelo, titulo, vidaUtil, watts, precioCompra, precioRepuesto));
  }

  private void mostrarError(String mensaje) {
    error.setText(mensaje);
    error.setVisible(true);
    error.setManaged(true);
  }

  private void ocultarError() {
    error.setVisible(false);
    error.setManaged(false);
  }

  public void setOnGuardar(Consumer<ImpresorasReqDto> action) {
    this.onGuardar = action;
  }

  public void setOnCancelar(Runnable action) {
    this.onCancelar = action;
  }
}
