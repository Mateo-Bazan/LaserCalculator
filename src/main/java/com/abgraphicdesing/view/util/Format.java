package com.abgraphicdesing.view.util;

import java.text.NumberFormat;
import java.util.Locale;

/** Formato de numeros compartido por las vistas (solo presentacion). */
public final class Format {

  private static final NumberFormat MILES =
    NumberFormat.getNumberInstance(Locale.of("es", "AR"));

  static {
    MILES.setMaximumFractionDigits(0);
  }

  private Format() { }

  public static String miles(Number valor) {
    if (valor == null) return "--";
    return MILES.format(valor);
  }

  public static String moneda(Number valor) {
    if (valor == null) return "--";
    return "$" + MILES.format(valor);
  }

  public static String hs(Number valor) {
    if (valor == null) return "--";
    return MILES.format(valor) + " hs";
  }

  public static String watts(Number valor) {
    if (valor == null) return "--";
    return MILES.format(valor) + "W";
  }

  public static String texto(String valor) {
    return valor == null || valor.isBlank() ? "--" : valor;
  }
}
