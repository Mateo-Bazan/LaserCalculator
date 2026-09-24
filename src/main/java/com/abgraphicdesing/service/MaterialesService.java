package com.abgraphicdesing.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.abgraphicdesing.model.entity.Materiales;
import com.abgraphicdesing.repository.MaterialesRepository;


public class MaterialesService {

  private final MaterialesRepository repository;

  public MaterialesService(MaterialesRepository repository) {
    this.repository = repository;
  }

  public List<Materiales> read() throws SQLException {
    return repository.read();
  }

  public Optional<Materiales> findById(UUID id) throws SQLException {
    return repository.findById(id);
  }

  public boolean create(Materiales m) throws SQLException {
    if (m.getId() == null) {
      m.setId();
    }

    return repository.create(m); 
  }

  public boolean update(Materiales m) throws SQLException {
    if (m.getId() == null) {
      throw new IllegalArgumentException("No se puede actualizar un material sin id");
    }
    return repository.update(m);
  }

  public boolean delete(UUID id) throws SQLException {
    return repository.delete(id);
  }
}