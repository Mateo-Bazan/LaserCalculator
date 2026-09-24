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
    Impresoras impresoraToSave = new Impresoras();
    this.repository.save(impresoraToSave);
    return impresoraToSave;
  }

  public Impresoras update(){
    Impresoras impresorasToUpdate = new Impresoras();
    this.repository.update(impresorasToUpdate);
    return impresorasToUpdate;
  }

  
}