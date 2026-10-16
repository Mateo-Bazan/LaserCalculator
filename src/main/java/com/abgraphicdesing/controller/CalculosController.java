package com.abgraphicdesing.controller;

import java.util.function.Consumer;

import com.abgraphicdesing.controller.util.AsyncExecutor;
import com.abgraphicdesing.model.dto.CalculosReqDto;
import com.abgraphicdesing.model.dto.CalculosResDto;
import com.abgraphicdesing.service.CalculosService;

public class CalculosController {
  private final CalculosService service;
  public CalculosController(CalculosService s){
    this.service = s;
  }

  public void create(
    CalculosReqDto dto,
    Consumer <CalculosResDto> onSucces,
    Consumer <Throwable> onError
  ){
    AsyncExecutor.execute(
      () -> service.create(dto),
      onSucces,
      onError
    );
  }
}
