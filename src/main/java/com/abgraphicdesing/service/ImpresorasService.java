package com.abgraphicdesing.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.abgraphicdesing.model.dto.ImpresorasReqDto;
import com.abgraphicdesing.model.entity.Impresoras;
import com.abgraphicdesing.repository.ImpresorasRepository;

public class ImpresorasService{
  private final ImpresorasRepository repository;

  public ImpresorasService(ImpresorasRepository repository){
    this.repository = repository;
  }

  public Optional<Impresoras>  getOne(UUID id){
    Optional<Impresoras> impresora = this.repository.findById(id);
    if(!impresora.isEmpty()){
      return null;
    }
    return impresora;
  }

  public List<Impresoras> getAll(){
    List<Impresoras> impresorasList = this.repository.getAll();
    if(impresorasList.isEmpty()){
      return null;
    }
    return impresorasList;
  }

  public Impresoras create(ImpresorasReqDto dto){
    Impresoras impresoraToSave = new Impresoras(
      dto.idTipo(),
      dto.marca(),
      dto.modelo(),
      dto.titulo(),
      dto.vidaUtil(),
      dto.watts(),
      dto.precioCompra(),
      dto.precioRespuesto()
    );

    this.repository.save(impresoraToSave);
    return impresoraToSave;
  }

  public Impresoras update(UUID id, ImpresorasReqDto dto){
    Optional<Impresoras> impresoraOnDb = this.repository.findById(id);
    if(impresoraOnDb.isEmpty()){
      throw new IllegalArgumentException("Impresora no encontrada");
    }

    Impresoras impresoraToUpdate = impresoraOnDb.orElse(new Impresoras());
    impresoraToUpdate.merge(dto);

    this.repository.update(impresoraToUpdate);
    return impresoraToUpdate;
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