package com.abgraphicdesing.service;

import java.util.List;
import java.util.Optional;

import com.abgraphicdesing.repository.AjustesRepository;
import com.abgraphicdesing.model.dto.AjustesReqDto;
import com.abgraphicdesing.model.dto.AjustesResDto;
import com.abgraphicdesing.model.entity.Ajustes;

public class AjustesService{
  private final AjustesRepository repository;

  public AjustesService(AjustesRepository repository){
    this.repository = repository;
  }

  public List<AjustesResDto> getAll(){
    List<AjustesResDto> listaAjustes = this.repository.getAll().stream().map(AjustesResDto::new).toList();
    if(listaAjustes.isEmpty()){
      return null;
    }
    return listaAjustes;
  }

  public Optional<AjustesResDto> findById (int id){
    Optional<Ajustes> ajuste = this.repository.findById(id);
    if(ajuste.isEmpty()){
      return null;
    }
    return ajuste.map(AjustesResDto::new);
  }

  public AjustesResDto create (AjustesReqDto dto){
    Ajustes a = new Ajustes(
      dto.nombre(),
      dto.gasto(),
      dto.idPeriodo()
    );
    this.repository.create(a);
    return new AjustesResDto(a);
  }

  public AjustesResDto update(int id, AjustesReqDto dto){
    Ajustes ajuste = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Ajuste no encontrado"));
    ajuste.merge(dto);
    this.repository.update(ajuste);
    return new AjustesResDto(ajuste);
  }

  public String delete(int id){
    Optional<Ajustes> ajusteOnDb = this.repository.findById(id);
    if (ajusteOnDb.isEmpty()){
      return "El ajuste no existe";
    }

    this.repository.delete(id);
    return  "El ajuste fue eliminado con exito";
  }
}