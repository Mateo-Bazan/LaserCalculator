package com.abgraphicdesing.service;

import java.util.List;

import com.abgraphicdesing.model.dto.TiposResDto;
import com.abgraphicdesing.repository.TiposRepository;

public class TiposService {
  private final TiposRepository repository;

  public TiposService(TiposRepository repository) {
    this.repository = repository;
  }

  public List<TiposResDto> getAll() {
    List<TiposResDto> tiposList = this.repository.getAll().stream()
      .map(TiposResDto::new)
      .toList();
    if (tiposList.isEmpty()) {
      return null;
    }
    return tiposList;
  }

  public TiposResDto create(String nombre) {
    if (nombre == null || nombre.trim().isEmpty()) {
      throw new IllegalArgumentException("El nombre del tipo es obligatorio");
    }
    com.abgraphicdesing.model.entity.Tipos tipo = repository.save(nombre.trim());
    return new TiposResDto(tipo);
  }
} 
