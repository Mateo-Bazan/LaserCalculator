package com.abgraphicdesing.service;


import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.abgraphicdesing.model.entity.Materiales;
import com.abgraphicdesing.repository.MaterialesRepository;
import com.abgraphicdesing.model.dto.MaterialesReqDto;
import com.abgraphicdesing.model.dto.MaterialesResDto;


public class MaterialesService {

  private final MaterialesRepository repository;

  public MaterialesService(MaterialesRepository repository) {
    this.repository = repository;
  }

  public List<MaterialesResDto> getAll() throws SQLException {
    java.util.List<com.abgraphicdesing.model.entity.Materiales> list = this.repository.getAll();
    if (list == null || list.isEmpty()) {
      return java.util.Collections.emptyList();
    }
    List<MaterialesResDto> materialesList = list.stream().map(MaterialesResDto::new).toList();
    return materialesList;
  }

  public List<MaterialesResDto> getAllOrEmpty() throws SQLException {
    return getAll();
  }

  public Optional<MaterialesResDto> findById(UUID id) {
    Optional<com.abgraphicdesing.model.entity.Materiales> material = this.repository.findById(id);
    if(material.isEmpty()){
      return Optional.empty();
    }
    return material.map(MaterialesResDto::new);
  }

  public Optional<MaterialesResDto> findByIdOrEmpty(UUID id) {
    return findById(id);
  }

  public MaterialesResDto create(MaterialesReqDto dto) throws SQLException {
    Materiales materialToSave = new Materiales(
      dto.nombre(),
      dto.precioCompra(),
      dto.merma(),
      dto.dimensiones(),
      dto.stock()
    );
    materialToSave.setId();

    this.repository.create(materialToSave);
    return new MaterialesResDto(materialToSave); 
  }

  public MaterialesResDto update(UUID id, MaterialesReqDto dto) {
    Optional<Materiales> materialOnDb = this.repository.findById(id);
    if (materialOnDb.isEmpty()) {
      throw new IllegalArgumentException("Material no encontrado");
    }

    Materiales materialToUpdate = materialOnDb.orElse(new Materiales());
    materialToUpdate.merge(dto);

    this.repository.update(materialToUpdate);
    return new MaterialesResDto(materialToUpdate);
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