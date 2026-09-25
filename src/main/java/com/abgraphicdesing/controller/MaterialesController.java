package com.abgraphicdesing.controller;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.UUID;

import com.abgraphicdesing.controller.util.AsyncExecutor;
import com.abgraphicdesing.model.dto.MaterialesReqDto;
import com.abgraphicdesing.model.entity.Materiales;
import com.abgraphicdesing.service.MaterialesService;


public class MaterialesController{
  private final MaterialesService service;

  public MaterialesController(MaterialesService service){
    this.service = service;
  }

  public void getAll(
    Consumer <List<Materiales>> onSuccess,
    Consumer <Throwable> onError
  ){
    AsyncExecutor.execute(
      () -> service.getAll(),
      onSuccess,
      onError
    );
  }

  public void create(
    MaterialesReqDto dto,
    Consumer <Materiales> onSuccess,
    Consumer <Throwable> onError
  ){
    AsyncExecutor.execute(
      () -> service.create(dto),
      onSuccess,
      onError
    );
  }

  public void update(
    UUID id, MaterialesReqDto dto,
    Consumer<Materiales> onSucces,
    Consumer<Throwable> onError
  ){
    AsyncExecutor.execute(
      () -> service.update(id, dto),
      onSucces,
      onError
    );
  }

  public void findById(
    UUID id,
    Consumer <Optional<Materiales>> onSuccess,
    Consumer <Throwable> onError
  ){
    AsyncExecutor.execute(
      () -> service.findById(id), 
      onSuccess, 
      onError);
  }

  public void getResume(){}

  public void delete(
    UUID id,
    Consumer <String> onSuccess,
    Consumer <Throwable> onError
  ){
    AsyncExecutor.execute(
      () -> service.delete(id), 
      onSuccess, 
      onError);
  }
}