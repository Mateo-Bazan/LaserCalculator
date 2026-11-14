package com.abgraphicdesing.view;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.abgraphicdesing.controller.CalculosController;
import com.abgraphicdesing.controller.HistorialController;
import com.abgraphicdesing.controller.ImpresorasController;
import com.abgraphicdesing.controller.MaterialesController;
import com.abgraphicdesing.model.dto.CalculosReqDto;
import com.abgraphicdesing.model.dto.ImpresorasResDto;
import com.abgraphicdesing.model.dto.MaterialesResDto;
import com.abgraphicdesing.model.entity.Historial;
import com.abgraphicdesing.view.mapper.CalculatorMapper;
import com.abgraphicdesing.view.ui.ImpresorasResumeCard;
import com.abgraphicdesing.view.util.AppLogger;
import com.abgraphicdesing.view.util.TimeFormatterUtil;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

public class CalcualdoraView extends HBox {
  private final ImpresorasController impresorasController;
  private final MaterialesController materialesController;
  private final CalculosController calculosController;
  private HistorialController historialController;

  //Tarjetas Izquierda
  private final List<ImpresorasResumeCard> tarjetas = new ArrayList<>();

  // Campos del formulario
  private final TextField nombreTrabajo = new TextField();
  private final ComboBox<ImpresorasResDto> impresoraCombo = new ComboBox<>();
  private final TextField tiempoGrabado = new TextField();
  private final TextField horasManoObra = new TextField();
  private final ComboBox<MaterialesResDto> materialCombo = new ComboBox<>();
  private final TextField cantidad = new TextField();
  private final TextField ganancia = new TextField();

  // Mensajes y resultado
  private final Label errorLabel = new Label();
  private final Label manoObraValor = new Label();
  private final Label materialesValor = new Label();
  private final Label maquinaValor = new Label();
  private final Label gastosFijosValor = new Label();
  private final Label costoUnitarioValor = new Label();
  private final Label precioUnitarioValor = new Label();
  private final Label costoValor = new Label();
  private final Label gananciaValor = new Label();
  private final Label precioFinalValor = new Label();
  private final VBox resultBox = new VBox(8);
  private final Button btnCalcular = new Button("Calcular Presupuesto →");

  private final NumberFormat formatoMoneda =
    NumberFormat.getIntegerInstance(Locale.forLanguageTag("es-AR"));

  // Material "centinela": no existe en la base, solo representa "sin material"
  private static final MaterialesResDto NINGUNO =
    new MaterialesResDto(null, "Ninguno", 0, 0, 0, "", 0);

  private boolean esNinguno(MaterialesResDto m) {
    return m != null && m.id() == null;
  }

  public CalcualdoraView(
    ImpresorasController impresorasController,
    MaterialesController materialesController,
    CalculosController calculosController
  ) {
    this(impresorasController, materialesController, calculosController, null);
  }

  public CalcualdoraView(
    ImpresorasController impresorasController,
    MaterialesController materialesController,
    CalculosController calculosController,
    HistorialController historialController
  ) {
    this.impresorasController = impresorasController;
    this.materialesController = materialesController;
    this.calculosController = calculosController;
    this.historialController = historialController;

    getStyleClass().add("app-background");
    setPadding(new Insets(28, 36, 28, 36));
    setSpacing(24);

    VBox leftSide = construirLadoIzquierdo();
    VBox formCard = construirFormulario();
    HBox.setHgrow(formCard, Priority.ALWAYS);

    getChildren().addAll(leftSide, formCard);

    impresoraCombo.valueProperty().addListener((obs, anterior, actual) -> {
      for (ImpresorasResumeCard t : tarjetas) {
        t.setSelected(t.getData().equals(actual));
      }
    });

    cargarImpresoras(leftSide);
    cargarMateriales();
  }

  private VBox construirLadoIzquierdo() {
    VBox leftSide = new VBox(10);
    Label titulo = new Label("Impresoras");
    titulo.getStyleClass().add("leftSideTitle");
    leftSide.getChildren().add(titulo);
    leftSide.setAlignment(Pos.TOP_CENTER);
    leftSide.setMinWidth(260);
    leftSide.getStyleClass().add("imprCardContainer");
    return leftSide;
  }

  private VBox construirFormulario() {
    VBox card = new VBox(14);
    card.getStyleClass().add("content-card");

    Label titulo = new Label("Cálculo de Producción Láser");
    titulo.getStyleClass().add("page-title");
    Label subtitulo = new Label(
      "Configure los parámetros de impresión para estimar los costes de producción.");
    subtitulo.getStyleClass().add("page-subtitle");

    nombreTrabajo.setPromptText("Ej: Logo Laser Calculator");
    impresoraCombo.setPromptText("Elegí una impresora");
    GridPane fila1 = fila(50, 50);
    fila1.add(campo("Nombre del Trabajo", nombreTrabajo), 0, 0);
    fila1.add(campo("Impresora", impresoraCombo), 1, 0);

    tiempoGrabado.setPromptText("00h 00min 00seg");
    horasManoObra.setPromptText("00h 00min 00seg");
    TimeFormatterUtil.aplicarFormatoTiempo(tiempoGrabado);
    TimeFormatterUtil.aplicarFormatoTiempo(horasManoObra);
    GridPane fila2 = fila(50, 50);
    fila2.add(campo("Tiempo de Grabado", tiempoGrabado), 0, 0);
    fila2.add(campo("Horas de mano de obra", horasManoObra), 1, 0);

    materialCombo.setPromptText("Elegí un material");
    cantidad.setPromptText("1");
    ganancia.setPromptText("50");
    GridPane fila3 = fila(50, 25, 25);
    fila3.add(campo("Material", materialCombo), 0, 0);
    fila3.add(campo("Cantidad", cantidad), 1, 0);
    fila3.add(campo("Ganancia (%)", ganancia), 2, 0);

    configurarCombos();

    errorLabel.getStyleClass().add("form-error");

    card.getChildren().addAll(
      titulo, subtitulo, separador(),
      fila1, fila2, separador(),
      fila3, separador(),
      construirZonaResultado(),
      errorLabel
    );
    return card;
  }

  private HBox construirZonaResultado() {
    resultBox.getStyleClass().add("result-box");
    resultBox.setPrefWidth(400);
    resultBox.setMaxWidth(400);
    resultBox.getChildren().addAll(
      filaDetalle("Mano de obra", manoObraValor),
      filaDetalle("Materiales", materialesValor),
      filaDetalle("Costo de máquina", maquinaValor),
      filaDetalle("Gastos fijos", gastosFijosValor),
      separador(),
      filaResultado("Costo total", costoValor),
      filaDetalle("Ganancia (+ mano de obra)", gananciaValor),
      filaResultado("PRECIO FINAL", precioFinalValor),
      separador(),
      filaDetalle("Costo por unidad", costoUnitarioValor),
      filaDetalle("Precio por unidad", precioUnitarioValor)
    );
    resultBox.setVisible(false);
    resultBox.setManaged(false);

    btnCalcular.getStyleClass().add("btn-primary");
    btnCalcular.setOnAction(e -> calcular());

    Region espacio = new Region();
    HBox.setHgrow(espacio, Priority.ALWAYS);

    VBox botones = new VBox(btnCalcular);
    botones.setAlignment(Pos.CENTER);

    HBox zona = new HBox(resultBox, espacio, botones);
    zona.setAlignment(Pos.CENTER_LEFT);
    zona.setPadding(new Insets(6, 40, 6, 0));
    return zona;
  }

  private GridPane fila(double... porcentajes) {
    GridPane grid = new GridPane();
    grid.setHgap(20);
    for (double p : porcentajes) {
      ColumnConstraints col = new ColumnConstraints();
      col.setPercentWidth(p);
      grid.getColumnConstraints().add(col);
    }
    return grid;
  }

  private VBox campo(String texto, Region control) {
    Label label = new Label(texto);
    label.getStyleClass().add("form-label");
    control.setMaxWidth(Double.MAX_VALUE);
    control.getStyleClass().add("form-input");
    return new VBox(4, label, control);
  }

  private HBox filaResultado(String texto, Label valor) {
    Label etiqueta = new Label(texto);
    etiqueta.getStyleClass().add("result-label");
    valor.getStyleClass().add("result-label");
    Region espacio = new Region();
    HBox.setHgrow(espacio, Priority.ALWAYS);
    return new HBox(etiqueta, espacio, valor);
  }
  private HBox filaDetalle(String texto, Label valor) {
    Label etiqueta = new Label(texto);
    etiqueta.getStyleClass().add("result-detail");
    valor.getStyleClass().add("result-detail");
    Region espacio = new Region();
    HBox.setHgrow(espacio, Priority.ALWAYS);
    return new HBox(etiqueta, espacio, valor);
  }

  private String moneda(int valor) {
    return "$" + formatoMoneda.format(valor);
  }

  private Region separador() {
    Region sep = new Region();
    sep.getStyleClass().add("header-separator");
    return sep;
  }

  private void configurarCombos() {
    impresoraCombo.setConverter(new StringConverter<>() {
      @Override public String toString(ImpresorasResDto i) {
        return i == null ? "" : i.marca() + " - " + i.modelo();
      }
      @Override public ImpresorasResDto fromString(String s) { return null; }
    });

    materialCombo.setConverter(new StringConverter<>() {
      @Override public String toString(MaterialesResDto m) {
        if (m == null) return "";
        if (esNinguno(m)) return "Ninguno";
        return m.nombre() + " $" + m.precioCosto();
      }
      @Override public MaterialesResDto fromString(String s) { return null; }
    });
  }

  private VBox leftSideRef;

  private void cargarImpresoras(VBox container) {
    this.leftSideRef = container;
    refrescarImpresoras();
  }

  public void refrescarImpresoras() {
    if (leftSideRef == null) return;
    tarjetas.clear();
    leftSideRef.getChildren().removeIf(n -> n instanceof ImpresorasResumeCard);
    impresorasController.getAll(
      impresoras -> javafx.application.Platform.runLater(() -> {
        if (impresoras != null && !impresoras.isEmpty()) {
          for (ImpresorasResDto impresora : impresoras) {
            ImpresorasResumeCard card =
              new ImpresorasResumeCard(impresora, impresoraCombo::setValue);
            tarjetas.add(card);
            leftSideRef.getChildren().add(card);
          }
          impresoraCombo.getItems().setAll(impresoras);
        } else {
          impresoraCombo.getItems().clear();
        }
      }),
      error -> AppLogger.warn("Error cargando impresoras: " + (error == null ? "" : error.getMessage()))
    );
  }

  private void cargarMateriales() {
    materialesController.getAll(
      materiales -> {
        materialCombo.getItems().clear();
        materialCombo.getItems().add(NINGUNO);
        if (materiales != null) {
          materialCombo.getItems().addAll(materiales);
        }
      },
      error -> AppLogger.warn("Error cargando materiales: " + (error == null ? "" : error.getMessage()))
    );
  }

  private void calcular() {
    errorLabel.setText("");
    try {
      ImpresorasResDto imp = impresoraCombo.getValue();
      MaterialesResDto mat = materialCombo.getValue();
      if (imp == null || mat == null) {
        throw new IllegalArgumentException("Elegí una impresora y un material.");
      }

      float hsGrabado = TimeFormatterUtil.parsearHoras(tiempoGrabado.getText(), "Tiempo de grabado");
      float hsMano = TimeFormatterUtil.parsearHoras(horasManoObra.getText(), "Horas de mano de obra");
      int cant = Integer.parseInt(cantidad.getText().trim());
      int gan = Integer.parseInt(ganancia.getText().replace("%", "").trim());

      if (cant <= 0) throw new IllegalArgumentException("La cantidad debe ser mayor a 0.");
      if (gan < 0) throw new IllegalArgumentException("La ganancia no puede ser negativa.");

      CalculosReqDto dto = new CalculosReqDto(
        hsMano, hsGrabado,
        esNinguno(mat) ? null : CalculatorMapper.toMaterial(mat),
        CalculatorMapper.toImpresora(imp), cant, gan);

      btnCalcular.setDisable(true);
      calculosController.create(
        dto,
        res -> {
          manoObraValor.setText(moneda(res.costoManoObra()));
          materialesValor.setText(moneda(res.costoMateriales()));
          maquinaValor.setText(moneda(res.costoMaquina()));
          gastosFijosValor.setText(moneda(res.gastosFijos()));
          costoValor.setText(moneda(res.costo()));
          gananciaValor.setText(moneda(res.montoGanancia())
            + " (" + res.porcentajeGanancia() + "%) + " + moneda(res.costoManoObra()));
          precioFinalValor.setText(moneda(res.precioFinal()));
          costoUnitarioValor.setText(moneda(res.costoUnitario()));
          precioUnitarioValor.setText(moneda(res.precioUnitario()));

          resultBox.setVisible(true);
          resultBox.setManaged(true);
          btnCalcular.setDisable(false);
          guardarEnHistorial(imp, mat);
        },
        error -> {
          errorLabel.setText("No se pudo calcular: " + error.getMessage());
          btnCalcular.setDisable(false);
        }
      );
    } catch (NumberFormatException e) {
      errorLabel.setText("Cantidad y ganancia deben ser números enteros.");
    } catch (IllegalArgumentException e) {
      errorLabel.setText(e.getMessage());
    }
  }

  private void guardarEnHistorial(ImpresorasResDto imp, MaterialesResDto mat) {
    if (historialController == null) return;
    try {
      Historial h = new Historial();
      h.setTitulo(nombreTrabajo.getText() == null || nombreTrabajo.getText().trim().isEmpty()
        ? (imp == null ? "Cálculo" : imp.titulo())
        : nombreTrabajo.getText().trim());
      h.setTiempoGrabado(tiempoGrabado.getText());
      h.setTiempoObra(horasManoObra.getText());
      h.setIdImpresora(imp == null ? null : imp.id());
      h.setIdMaterial(mat == null || esNinguno(mat) ? null : mat.id());
      h.setIdImpresoraRef(imp == null ? null : (imp.id() == null ? null : imp.id().toString()));
      h.setIdMaterialRef(mat == null || esNinguno(mat) ? null : (mat.id() == null ? null : mat.id().toString()));
      try {
        h.setCantidad(Integer.parseInt(cantidad.getText().trim()));
      } catch (Exception ex) { h.setCantidad(0); }
      try {
        h.setGanancia(Integer.parseInt(ganancia.getText().replace("%", "").trim()));
      } catch (Exception ex) { h.setGanancia(0); }
      h.setPrecioFinal(0);
      try {
        String pf = precioFinalValor.getText().replace("$", "").replace(".", "").replace(",", "").trim();
        if (!pf.isEmpty()) h.setPrecioFinal(Integer.parseInt(pf));
      } catch (Exception ignored) {}
      h.setFechaGuardado(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

      if (imp != null) {
        h.setImpresoraMarca(imp.marca());
        h.setImpresoraModelo(imp.modelo());
        h.setImpresoraTipo(imp.tipo());
        h.setImpresoraTitulo(imp.titulo());
        h.setImpresoraVidaUtil(imp.vidaUtil());
        h.setImpresoraWatts(imp.watts());
        h.setImpresoraPrecioCompra((double) imp.precioCompra());
        h.setImpresoraPrecioRepuesto((double) imp.precioRespuesto());
        h.setImpresoraAmortizacion((double) imp.amortizacion());
      }

      if (mat != null && !esNinguno(mat)) {
        h.setMaterialNombre(mat.nombre());
        h.setMaterialPrecioCompra((double) mat.precioCompra());
        h.setMaterialMerma(mat.merma());
        h.setMaterialPrecioCosto((double) mat.precioCosto());
        h.setMaterialDimensiones(mat.dimensiones());
      }

      historialController.save(h, ok -> {}, err -> AppLogger.warn("No se pudo guardar historial"));
    } catch (Exception e) {
      AppLogger.warn("Error al preparar historial: " + e.getMessage());
    }
  }

}
