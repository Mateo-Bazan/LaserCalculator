package com.abgraphicdesing.service;

import java.util.List;

import com.abgraphicdesing.model.entity.Historial;
import com.abgraphicdesing.repository.HistorialRepository;

public class HistorialService {
  private final HistorialRepository repository;

  public HistorialService(HistorialRepository repository) {
    this.repository = repository;
  }

  public boolean save(Historial h) {
    return repository.save(h);
  }

  public List<Historial> getAll() {
    return repository.getAll();
  }
} 
