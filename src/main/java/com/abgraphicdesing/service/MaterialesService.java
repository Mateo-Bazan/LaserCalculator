package com.abgraphicdesing.service;


import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.abgraphicdesing.model.entity.Materiales;
import com.abgraphicdesing.repository.MaterialesRepository;
import com.abgraphicdesing.model.dto.MaterialesReqDto;


public class MaterialesService {

  private final MaterialesRepository repository;

  public MaterialesService(MaterialesRepository repository) {
    this.repository = repository;
  }

  public List<Materiales> getAll() throws SQLException {
    List<Materiales> materialesList = this.repository.getAll();
    if(materialesList.isEmpty()){
      return null;
    }
    return materialesList;
  }

  public Optional<Materiales> findById(UUID id) {
    Optional<Materiales> material = this.repository.findById(id);
    if(material.isEmpty()){
      return null;
    }
    return material;
  }

  public Materiales create(MaterialesReqDto dto) throws SQLException {
    Materiales materialToSave = new Materiales(
      dto.nombre(),
      dto.precioCompra(),
      dto.merma(),
      dto.dimensiones(),
      dto.stock()
    );

    this.repository.create(materialToSave);
    return materialToSave; 
  }

  public Materiales update(UUID id, MaterialesReqDto dto) {
    Optional<Materiales> materialOnDb = this.repository.findById(id);
    if (materialOnDb.isEmpty()) {
      throw new IllegalArgumentException("Material no encontrado");
    }

    Materiales materialToUpdate = materialOnDb.orElse(new Materiales());
    materialToUpdate.merge(dto);

    this.repository.update(materialToUpdate);
    return materialToUpdate;
  }

  public String delete(UUID id) {
    Optional<Materiales> existsMaterial = this.repository.findById(id);
    if (existsMaterial.isEmpty()) {
      return "El material no existe";
    }

    this.repository.delete(id);
    return "El material se elimino";
  }
}