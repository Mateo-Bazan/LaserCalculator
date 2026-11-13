package com.abgraphicdesing.view.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

public final class TimeFormatterUtil {
  private TimeFormatterUtil() {}

  public static void aplicarFormatoTiempo(TextField campo) {
    campo.setTextFormatter(new TextFormatter<String>(cambio -> {
      if (!cambio.isContentChange()) return cambio;

      String anteriores = cambio.getControlText().replaceAll("\\D", "");
      String digitos;

      if (cambio.isDeleted() && !cambio.isAdded()) {
        digitos = anteriores.isEmpty() ? "" : anteriores.substring(0, anteriores.length() - 1);
      } else {
        digitos = cambio.getControlNewText().replaceAll("\\D", "");
      }

      digitos = digitos.replaceFirst("^0+", "");
      if (digitos.length() > 6) return null;

      String texto = "";
      if (!digitos.isEmpty()) {
        String d = String.format("%6s", digitos).replace(' ', '0');
        texto = d.substring(0, 2) + "h " + d.substring(2, 4) + "min " + d.substring(4, 6) + "seg";
      }

      cambio.setRange(0, cambio.getControlText().length());
      cambio.setText(texto);
      cambio.selectRange(texto.length(), texto.length());
      return cambio;
    }));
  }

  public static float parsearHoras(String texto, String campo) {
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
} 
