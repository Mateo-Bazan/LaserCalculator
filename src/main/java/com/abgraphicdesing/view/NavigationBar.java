package com.abgraphicdesing.view;

import java.util.LinkedHashMap;
import java.util.Map;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;

public class NavigationBar extends HBox {

  private final Map<String, Button> buttons = new LinkedHashMap<>();

  public NavigationBar() {
    HBox brandBox = new HBox(8);
    Label label = new Label("AB Graphic Design");
    try {
      Image image = new Image(
        getClass().getResourceAsStream("/assets/iconoAbGD.png")
      );
      ImageView icon = new ImageView(image);
      icon.setFitHeight(36);
      icon.setFitWidth(36);
      brandBox.getChildren().add(icon);
    } catch (Exception ignored) {
      // Sin icono: se muestra solo el nombre de la marca.
    }
    brandBox.getChildren().add(label);
    brandBox.setAlignment(Pos.CENTER_LEFT);

    buttons.put("calculadora", new Button("Calculadora"));
    buttons.put("impresoras", new Button("Impresoras"));
    buttons.put("materiales", new Button("Materiales"));
    buttons.put("ajustes", new Button("Ajustes"));
    buttons.put("historial", new Button("Historial"));

    HBox buttonsContainer = new HBox(4);
    buttonsContainer.getChildren().addAll(buttons.values());
    buttonsContainer.setAlignment(Pos.CENTER);
    buttonsContainer.getStyleClass().add("nav-button");

    Region leftSpacer = new Region();
    Region rightSpacer = new Region();
    HBox.setHgrow(leftSpacer, Priority.ALWAYS);
    HBox.setHgrow(rightSpacer, Priority.ALWAYS);

    HBox userBox = new HBox(10);
    Label lang = new Label("ES");
    lang.getStyleClass().add("lang-pill");
    userBox.getChildren().addAll(lang, buildAvatar());
    userBox.setAlignment(Pos.CENTER_RIGHT);

    getStyleClass().add("navigation-bar");
    setAlignment(Pos.CENTER_LEFT);
    getChildren().addAll(brandBox, leftSpacer, buttonsContainer, rightSpacer, userBox);
  }

  private StackPane buildAvatar() {
    Circle background = new Circle(15);
    background.setFill(javafx.scene.paint.Color.WHITE);
    background.setStroke(javafx.scene.paint.Color.web("#111827"));
    background.setStrokeWidth(1.5);

    Circle head = new Circle(4.5);
    head.setFill(javafx.scene.paint.Color.TRANSPARENT);
    head.setStroke(javafx.scene.paint.Color.web("#111827"));
    head.setStrokeWidth(1.5);
    head.setTranslateY(-4);

    javafx.scene.shape.Arc shoulders = new javafx.scene.shape.Arc(0, 8, 7, 6, 20, 140);
    shoulders.setFill(javafx.scene.paint.Color.TRANSPARENT);
    shoulders.setStroke(javafx.scene.paint.Color.web("#111827"));
    shoulders.setStrokeWidth(1.5);

    StackPane avatar = new StackPane(background, head, shoulders);
    avatar.setPrefSize(32, 32);
    return avatar;
  }

  /** Marca como activo el boton de la vista indicada. */
  public void setActive(String key) {
    buttons.forEach((k, b) -> {
      if (k.equals(key)) {
        if (!b.getStyleClass().contains("button-active")) {
          b.getStyleClass().add("button-active");
        }
      } else {
        b.getStyleClass().remove("button-active");
      }
    });
  }

  public void setOnCalculadora(Runnable action) {
    buttons.get("calculadora").setOnAction(event -> action.run());
  }

  public void setOnImpresora(Runnable action) {
    buttons.get("impresoras").setOnAction(event -> action.run());
  }

  public void setOnMateriales(Runnable action) {
    buttons.get("materiales").setOnAction(event -> action.run());
  }

  public void setOnAjustes(Runnable action) {
    buttons.get("ajustes").setOnAction(event -> action.run());
  }

  public void setOnHistorial(Runnable action) {
    buttons.get("historial").setOnAction(event -> action.run());
  }
}
