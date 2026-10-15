package com.abgraphicdesing.controller;

import java.util.List;
import java.util.function.Consumer;

import com.abgraphicdesing.controller.util.AsyncExecutor;
import com.abgraphicdesing.model.entity.Periodos;
import com.abgraphicdesing.service.PeriodosService;

public class PeriodosController {
  private final PeriodosService service;

  public PeriodosController(PeriodosService service){
    this.service = service;
  }

  public void getAll(
    Consumer<List<Periodos>> onSuccess,
    Consumer<Throwable> onError
  ){
    AsyncExecutor.execute(
      () -> service.getAll(),
      onSuccess,
      onError
    );
  }
}