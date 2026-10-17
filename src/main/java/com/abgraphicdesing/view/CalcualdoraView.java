package com.abgraphicdesing.view;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.List;

import com.abgraphicdesing.controller.CalculosController;
import com.abgraphicdesing.controller.ImpresorasController;
import com.abgraphicdesing.controller.MaterialesController;
import com.abgraphicdesing.model.dto.CalculosReqDto;
import com.abgraphicdesing.model.dto.ImpresorasResDto;
import com.abgraphicdesing.model.dto.MaterialesResDto;
import com.abgraphicdesing.model.entity.Impresoras;
import com.abgraphicdesing.model.entity.Materiales;
import com.abgraphicdesing.model.entity.Tipos;
import com.abgraphicdesing.view.ui.ImpresorasResumeCard;

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
import javafx.scene.control.TextFormatter;
import javafx.util.StringConverter;

public class CalcualdoraView extends HBox {
  private final ImpresorasController impresorasController;
  private final MaterialesController materialesController;
  private final CalculosController calculosController;

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
    this.impresorasController = impresorasController;
    this.materialesController = materialesController;
    this.calculosController = calculosController;

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

  // ---------- Construcción de la pantalla ----------

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

    // Fila 1: nombre + impresora (50% / 50%)
    nombreTrabajo.setPromptText("Ej: Logo Laser Calculator");
    impresoraCombo.setPromptText("Elegí una impresora");
    GridPane fila1 = fila(50, 50);
    fila1.add(campo("Nombre del Trabajo", nombreTrabajo), 0, 0);
    fila1.add(campo("Impresora", impresoraCombo), 1, 0);

    // Fila 2: tiempos (50% / 50%)
    tiempoGrabado.setPromptText("00h 00min 00seg");
    horasManoObra.setPromptText("00h 00min 00seg");
    aplicarFormatoTiempo(tiempoGrabado);
    aplicarFormatoTiempo(horasManoObra);
    GridPane fila2 = fila(50, 50);
    fila2.add(campo("Tiempo de Grabado", tiempoGrabado), 0, 0);
    fila2.add(campo("Horas de mano de obra", horasManoObra), 1, 0);

    // Fila 3: material + cantidad + ganancia (50% / 25% / 25%)
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
    resultBox.setPrefWidth(330);
    resultBox.setMaxWidth(330);
    resultBox.getChildren().addAll(
      filaResultado("Precio de costo", costoValor),
      filaResultado("Porcentaje de ganancia", gananciaValor),
      filaResultado("PRECIO FINAL", precioFinalValor)
    );
    // Oculto hasta el primer cálculo
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

  // ---------- Helpers de UI ----------
  /** Formatea el campo solo: los dígitos entran por la derecha, como una calculadora. */
  private void aplicarFormatoTiempo(TextField campo) {
    campo.setTextFormatter(new TextFormatter<String>(cambio -> {
      // Si solo se movió el cursor, no hay nada que formatear
      if (!cambio.isContentChange()) return cambio;

      String anteriores = cambio.getControlText().replaceAll("\\D", "");
      String digitos;

      if (cambio.isDeleted() && !cambio.isAdded()) {
        // Backspace/Delete: quitamos el último dígito, sin importar qué carácter se borró
        digitos = anteriores.isEmpty() ? "" : anteriores.substring(0, anteriores.length() - 1);
      } else {
        // Al escribir o pegar: nos quedamos solo con los números del texto resultante
        digitos = cambio.getControlNewText().replaceAll("\\D", "");
      }

      digitos = digitos.replaceFirst("^0+", ""); // sacamos ceros a la izquierda
      if (digitos.length() > 6) return null;     // máximo 99h 99min 99seg: rechazamos el cambio

      String texto = "";
      if (!digitos.isEmpty()) {
        String d = String.format("%6s", digitos).replace(' ', '0'); // rellena con ceros a la izquierda
        texto = d.substring(0, 2) + "h " + d.substring(2, 4) + "min " + d.substring(4, 6) + "seg";
      }

      // Reemplazamos TODO el contenido por el texto formateado y dejamos el cursor al final
      cambio.setRange(0, cambio.getControlText().length());
      cambio.setText(texto);
      cambio.selectRange(texto.length(), texto.length());
      return cambio;
    }));
  }

  /** Un GridPane con columnas de ancho porcentual. */
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

  /** Etiqueta arriba + control abajo. */
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

  private Region separador() {
    Region sep = new Region();
    sep.getStyleClass().add("header-separator");
    return sep;
  }

  /** Un ComboBox necesita saber cómo mostrar cada objeto como texto. */
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

  // ---------- Carga de datos ----------

  private void cargarImpresoras(VBox container) {
    impresorasController.getAll(
      impresoras -> {
        if (impresoras != null && !impresoras.isEmpty()) {
          for (ImpresorasResDto impresora : impresoras) {
            ImpresorasResumeCard card =
              new ImpresorasResumeCard(impresora, impresoraCombo::setValue);
            tarjetas.add(card);
            container.getChildren().add(card);
          }
          impresoraCombo.getItems().setAll(impresoras);
        } else {
          container.getChildren().add(new Label("No hay impresoras"));
        }
      },
      error -> System.out.println("Error cargando impresoras: " + error.getMessage())
    );
  }

  private void cargarMateriales() {
    materialesController.getAll(
      materiales -> {
        materialCombo.getItems().clear();
        materialCombo.getItems().add(NINGUNO);
        materialCombo.getItems().addAll(materiales);
      },
      error -> System.out.println("Error cargando materiales: " + error.getMessage())
    );
  }

  // ---------- Lógica del botón ----------

  private void calcular() {
    errorLabel.setText("");
    try {
      ImpresorasResDto imp = impresoraCombo.getValue();
      MaterialesResDto mat = materialCombo.getValue();
      if (imp == null || mat == null) {
        throw new IllegalArgumentException("Elegí una impresora y un material.");
      }

      float hsGrabado = parsearHoras(tiempoGrabado.getText(), "Tiempo de grabado");
      float hsMano = parsearHoras(horasManoObra.getText(), "Horas de mano de obra");
      int cant = Integer.parseInt(cantidad.getText().trim());
      int gan = Integer.parseInt(ganancia.getText().replace("%", "").trim());

      if (cant <= 0) throw new IllegalArgumentException("La cantidad debe ser mayor a 0.");
      if (gan < 0) throw new IllegalArgumentException("La ganancia no puede ser negativa.");

      CalculosReqDto dto = new CalculosReqDto(
        hsMano, hsGrabado,
        esNinguno(mat) ? null : aMaterial(mat),
        aImpresora(imp), cant, gan);

      btnCalcular.setDisable(true);
      calculosController.create(
        dto,
        res -> {
          costoValor.setText("$" + formatoMoneda.format(res.costo()));
          gananciaValor.setText(res.porcentajeGanancia() + "%");
          precioFinalValor.setText("$" + formatoMoneda.format(res.precioFinal()));
          resultBox.setVisible(true);
          resultBox.setManaged(true);
          btnCalcular.setDisable(false);
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

  /** Convierte "01h 30min 15seg" a horas decimales (1.504...). */
  private float parsearHoras(String texto, String campo) {
    Pattern patron = Pattern.compile(
      "^\\s*(?:(\\d+)\\s*h)?\\s*(?:(\\d+)\\s*min)?\\s*(?:(\\d+)\\s*seg)?\\s*$");
    Matcher m = patron.matcher(texto == null ? "" : texto);
    if (texto == null || texto.isBlank()) {
      throw new IllegalArgumentException(campo + ": ingresá un tiempo.");
    }
    if (!m.matches()) {
      throw new IllegalArgumentException(campo + ": formato inválido.");
    }
    int h = m.group(1) == null ? 0 : Integer.parseInt(m.group(1));
    int min = m.group(2) == null ? 0 : Integer.parseInt(m.group(2));
    int seg = m.group(3) == null ? 0 : Integer.parseInt(m.group(3));
    if (min > 59 || seg > 59) {
      throw new IllegalArgumentException(campo + ": minutos y segundos deben ser menores a 60.");
    }
    return h + min / 60f + seg / 3600f;
  }

  // ---------- ResDto -> entidad (lo que pide CalculosReqDto) ----------

  private Materiales aMaterial(MaterialesResDto m) {
    return new Materiales(
      m.nombre(), m.precioCompra(), m.merma(), m.dimensiones(), m.stock(), m.id());
  }

  private Impresoras aImpresora(ImpresorasResDto i) {
    return new Impresoras(
      new Tipos(i.tipo()), i.marca(), i.modelo(), i.titulo(),
      i.vidaUtil(), i.watts(), i.precioCompra(), i.precioRespuesto(), i.id());
  }
}