package com.abgraphicdesing.service;

import java.util.List;

import com.abgraphicdesing.model.entity.Periodos;
import com.abgraphicdesing.repository.PeriodosRepository;

public class PeriodosService {
  private final PeriodosRepository repository;

  public PeriodosService(PeriodosRepository repository){
    this.repository = repository;
  }

  public List<Periodos> getAll(){
    return this.repository.getAll();
  }
}