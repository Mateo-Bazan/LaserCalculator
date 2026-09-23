package com.abgraphicdesing.controller;

import java.util.List;

import com.abgraphicdesing.model.dto.ImpresorasReqDto;
import com.abgraphicdesing.model.entity.Impresoras;
import com.abgraphicdesing.service.ImpresorasService;

public class ImpresorasController{
  private final ImpresorasService service;

  public ImpresorasController(ImpresorasService service){
    this.service = service;
  }

  
  public List<Impresoras> getAll(){
    return this.service.getAll();
  }
  
  public Impresoras create(ImpresorasReqDto dto){
    return  this.service.create(dto);
  }
  
  public void update(){}
  
  public void getOne(String id){}
  
  public void getResume(){}

  public void delete(String id){}
}
