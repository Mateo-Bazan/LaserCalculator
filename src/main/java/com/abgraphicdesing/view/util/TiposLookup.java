package com.abgraphicdesing.view.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

import com.abgraphicdesing.database.DbConfig;

/**
 * Lectura de solo-lectura del catalogo de tipos (nombre -> id) para el
 * formulario de impresoras.
 *
 * <p>Lee directamente la tabla {@code tipo}, asi aparecen todos los tipos
 * cargados en la base (por SQL), tenga o no impresoras asociadas.</p>
 *
 * <p>DEUDA TECNICA: lo ideal seria una capa Repository/Service/Controller de
 * tipos. Mientras los tipos se administren por SQL, esta clase alcanza.</p>
 */
public class TiposLookup {

  private final DbConfig dbConfig;

  public TiposLookup() {
    this.dbConfig = new DbConfig();
  }

  /** Mapa nombre -> id con TODOS los tipos de la tabla {@code tipo}. */
  public Map<String, Integer> cargar() {
    Map<String, Integer> mapa = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    String sql = "SELECT id, tipo FROM tipo ORDER BY tipo";

    try (
      Connection conn = dbConfig.getConnection();
      PreparedStatement ps = conn.prepareStatement(sql);
      ResultSet rs = ps.executeQuery()
    ) {
      while (rs.next()) {
        String nombre = rs.getString("tipo");
        if (nombre != null && !nombre.isBlank()) {
          mapa.put(nombre.trim(), rs.getInt("id"));
        }
      }
    } catch (SQLException e) {
      // Sin tipos conocidos: el formulario lo informa y no guarda.
      System.err.println("Error al leer los tipos: " + e.getMessage());
    }
    return mapa;
  }

  public Optional<Integer> idPara(Map<String, Integer> conocidos, String nombre) {
    if (nombre == null || nombre.isBlank()) return Optional.empty();
    return Optional.ofNullable(conocidos.get(nombre.trim()));
  }
}
