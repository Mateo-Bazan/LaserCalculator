package com.abgraphicdesing.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.abgraphicdesing.model.dto.ImpresorasReqDto;
import com.abgraphicdesing.model.entity.Impresoras;
import com.abgraphicdesing.model.entity.Tipos;
import com.abgraphicdesing.repository.ImpresorasRepository;
import com.abgraphicdesing.model.dto.ImpresorasResDto;

public class ImpresorasService{
  private final ImpresorasRepository repository;

  public ImpresorasService(ImpresorasRepository repository){
    this.repository = repository;
  }

  public Optional<ImpresorasResDto>  getOne(UUID id){
    Optional<Impresoras> impresora = this.repository.findById(id);
    if(!impresora.isEmpty()){
      return null;
    }
    return impresora.map(ImpresorasResDto::new);
  }

  public List<ImpresorasResDto> getAll(){
    List<Impresoras> impresorasList = this.repository.getAll();
    if(impresorasList.isEmpty()){
      return null;
    }
    return impresorasList.stream().map(ImpresorasResDto::new).toList();
  }

  public ImpresorasResDto create(ImpresorasReqDto dto){
    Tipos newTipo = new Tipos(dto.idTipo(), null);

    Impresoras impresoraToSave = new Impresoras(
      newTipo,
      dto.marca(),
      dto.modelo(),
      dto.titulo(),
      dto.vidaUtil(),
      dto.watts(),
      dto.precioCompra(),
      dto.precioRespuesto()
    );

    this.repository.save(impresoraToSave);
    return new ImpresorasResDto(impresoraToSave);
  }

  public ImpresorasResDto update(UUID id, ImpresorasReqDto dto){
    Optional<Impresoras> impresoraOnDb = this.repository.findById(id);
    if(impresoraOnDb.isEmpty()){
      throw new IllegalArgumentException("Impresora no encontrada");
    }

    Impresoras impresoraToUpdate = impresoraOnDb.orElse(new Impresoras());
    impresoraToUpdate.merge(dto);

    this.repository.update(impresoraToUpdate);
    return new ImpresorasResDto(impresoraToUpdate);
  }

  public String delete(UUID id){
    Optional<Impresoras> impresoraOnDb = this.repository.findById(id);
    if (impresoraOnDb.isEmpty()){
      return "La impresora no existe";
    }

    this.repository.delete(id);
    return  "La impresora fue eliminada con exito";
  }
}