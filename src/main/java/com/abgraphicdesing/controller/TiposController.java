package com.abgraphicdesing.controller;

import java.util.List;
import java.util.function.Consumer;

import com.abgraphicdesing.controller.util.AsyncExecutor;
import com.abgraphicdesing.model.dto.TiposResDto;
import com.abgraphicdesing.service.TiposService;

public class TiposController {
  private final TiposService service;

  public TiposController(TiposService service) {
    this.service = service;
  }

  public void getAll(
    Consumer<List<TiposResDto>> onSuccess,
    Consumer<Throwable> onError
  ) {
    AsyncExecutor.execute(
      () -> service.getAll(),
      onSuccess,
      onError
    );
  }

  public void create(
    String nombre,
    Consumer<TiposResDto> onSuccess,
    Consumer<Throwable> onError
  ) {
    AsyncExecutor.execute(
      () -> service.create(nombre),
      onSuccess,
      onError
    );
  }
} 
