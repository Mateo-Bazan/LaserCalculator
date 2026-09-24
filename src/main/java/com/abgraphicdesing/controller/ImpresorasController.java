package com.abgraphicdesing.controller;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.UUID;

import com.abgraphicdesing.controller.util.AsyncExecutor;
import com.abgraphicdesing.model.dto.ImpresorasReqDto;
import com.abgraphicdesing.model.entity.Impresoras;
import com.abgraphicdesing.service.ImpresorasService;

public class ImpresorasController{
  private final ImpresorasService service;

  public ImpresorasController(ImpresorasService service){
    this.service = service;
  }

  
  public void getAll(
    Consumer <List<Impresoras>> onSuccess,
    Consumer <Throwable> onError
  ){
    AsyncExecutor.execute(
      () -> service.getAll(), 
      onSuccess, 
      onError
    );
  }
  
  public void create(
    ImpresorasReqDto dto,
    Consumer <Impresoras> onSuccess,
    Consumer <Throwable> onError
  ){
    AsyncExecutor.execute(
      () -> service.create(dto), 
      onSuccess, 
      onError
    );
  }
  
  public void update(){}
  
  public void getOne(
    UUID id,
    Consumer <Optional<Impresoras>> onSuccess,
    Consumer <Throwable> onError
  ){
    AsyncExecutor.execute(
      () -> service.getOne(id), 
      onSuccess, 
      onError
    );
  }
  
  public void getResume(){}

  public void delete(String id){}
}
