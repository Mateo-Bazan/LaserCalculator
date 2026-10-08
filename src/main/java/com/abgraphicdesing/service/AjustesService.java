package com.abgraphicdesing.service;

import java.util.List;
import java.util.Optional;

import com.abgraphicdesing.repository.AjustesRepository;
import com.abgraphicdesing.model.dto.AjustesReqDto;
import com.abgraphicdesing.model.entity.Ajustes;

public class AjustesService{
  private final AjustesRepository repository;

  public AjustesService(AjustesRepository repository){
    this.repository = repository;
  }

  public List<Ajustes> getAll(){
    List<Ajustes> listaAjustes = this.repository.getAll();
    return listaAjustes;
  }

  public Optional<Ajustes> findById (int id){
    Optional<Ajustes> ajuste = this.repository.findById(id);
    return ajuste;
  }

  public boolean create (AjustesReqDto dto){
    Ajustes a = new Ajustes(
      dto.nombre(),
      dto.gasto(),
      dto.idPeriodo()
    );
    return this.repository.create(a);
  }

  public boolean update(int id, AjustesReqDto dto){
    Optional<Ajustes> ajusteToUpdate = this.repository.findById(id);
    if(ajusteToUpdate.isPresent()){
      Ajustes updatedAjuste = ajusteToUpdate.orElse(new Ajustes());
      updatedAjuste.merge(dto);
      System.out.println(updatedAjuste);
      
      return this.repository.update(updatedAjuste);
      
    }
    return false;
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