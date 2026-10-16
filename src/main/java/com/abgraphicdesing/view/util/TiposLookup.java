package com.abgraphicdesing.view.util;

import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

import com.abgraphicdesing.database.DbConfig;
import com.abgraphicdesing.model.entity.Impresoras;
import com.abgraphicdesing.repository.ImpresorasRepository;

/**
 * Lectura de solo-lectura del catalogo de tipos (nombre -&gt; id) para el
 * formulario de impresoras.
 *
 * <p>DEUDA TECNICA (propuesta para ETAPA 2): el backend no expone un endpoint
 * de tipos ni incluye el id en {@code ImpresorasResDto}, asi que el frontend
 * resuelve el {@code idTipo} con los datos ya cargados. Lo correcto seria un
 * {@code GET /tipos} y exponer el id en el DTO.</p>
 */
public class TiposLookup {

  private final ImpresorasRepository repository;

  public TiposLookup() {
    this.repository = new ImpresorasRepository(new DbConfig());
  }

  /** Mapa nombre-&gt;id con los tipos que tienen al menos una impresora. */
  public Map<String, Integer> cargar() {
    Map<String, Integer> mapa = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    try {
      for (Impresoras imp : repository.getAll()) {
        if (imp.getTipo() != null && imp.getTipo().getTipo() != null) {
          mapa.putIfAbsent(imp.getTipo().getTipo(), imp.getTipo().getId());
        }
      }
    } catch (Exception ignored) {
      // Sin tipos conocidos: el formulario lo informa y no guarda.
    }
    return mapa;
  }

  public Optional<Integer> idPara(Map<String, Integer> conocidos, String nombre) {
    if (nombre == null || nombre.isBlank()) return Optional.empty();
    return Optional.ofNullable(conocidos.get(nombre.trim()));
  }
}
