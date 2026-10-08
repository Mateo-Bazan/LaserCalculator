package com.abgraphicdesing.controller;

import java.util.function.Consumer;
import java.util.List;
import java.util.Optional;

import com.abgraphicdesing.controller.util.AsyncExecutor;
import com.abgraphicdesing.model.dto.AjustesReqDto;
import com.abgraphicdesing.model.entity.Ajustes;
import com.abgraphicdesing.service.AjustesService;

public class AjustesController {
  private final AjustesService service;

  public AjustesController(AjustesService service){
    this.service = service;
  }

  public void getAll(
    Consumer <List<Ajustes>> onSuccess,
    Consumer <Throwable> onError
  ){
    AsyncExecutor.execute(
      () -> service.getAll(),
      onSuccess,
      onError
    );
  }

  public void create(
    AjustesReqDto dto,
    Consumer <Boolean> onSuccess,
    Consumer <Throwable> onError
  ){
    AsyncExecutor.execute(
      () -> service.create(dto),
      onSuccess,
      onError
    );
  }

  public void update(
    int id,
    AjustesReqDto dto,
    Consumer <Boolean> onSuccess,
    Consumer <Throwable> onError
  ){
    AsyncExecutor.execute(
      () -> service.update(id, dto),
      onSuccess,
      onError
    );
  }

  public void findById(
    int id,
    Consumer <Optional<Ajustes>> onSuccess,
    Consumer <Throwable> onError
  ){
    AsyncExecutor.execute(
      () -> service.findById(id),
      onSuccess,
      onError
    );
  }

  public void delete(
    int id,
    Consumer <String> onSuccess,
    Consumer <Throwable> onError
  ){
    AsyncExecutor.execute(
      () -> service.delete(id),
      onSuccess,
      onError
    );
  }
}
