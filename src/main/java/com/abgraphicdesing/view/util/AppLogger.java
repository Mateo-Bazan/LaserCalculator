package com.abgraphicdesing.view.util;

import java.util.logging.Level;
import java.util.logging.Logger;

public final class AppLogger {
  private static final Logger LOGGER = Logger.getLogger("com.abgraphicdesing");

  private AppLogger() {}

  public static void info(String msg) {
    LOGGER.log(Level.INFO, msg);
  }

  public static void warn(String msg) {
    LOGGER.log(Level.WARNING, msg);
  }

  public static void error(String msg, Throwable t) {
    LOGGER.log(Level.SEVERE, msg, t);
  }
} 
