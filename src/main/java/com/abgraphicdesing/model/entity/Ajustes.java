package com.abgraphicdesing.model.entity;

import com.abgraphicdesing.model.dto.AjustesReqDto;

public class Ajustes {
  private int id;
  private String nombre;
  private int gasto;
  private int idPeriodo;
  private String periodo;

  public Ajustes(){ }

  public Ajustes(
    String nombre,
    int gasto,
    int idPeriodo
  ){
    this.setNombre(nombre);
    this.setGasto(gasto);
    this.setIdPeriodo(idPeriodo);
  }

  public Ajustes(
    String nombre,
    int gasto,
    int idPeriodo,
    String periodo,
    int id
  ){
    this.nombre = nombre;
    this.gasto = gasto;
    this.idPeriodo = idPeriodo;
    this.periodo = periodo;
    this.id = id;
  }

  public int getId(){ 
    return this.id; 
  }
  public String getNombre(){ 
    return this.nombre; 
  }
  public int getGasto(){ 
    return this.gasto; 
  }
  public int getIdPeriodo(){ 
    return this.idPeriodo; 
  }
  public String getPeriodo(){
    return this.periodo;
  }

  /*public void setId(){
    this.id = UUID.randomUUID();
  }*/
  public void setNombre(String nombre){
    this.nombre = nombre;
  }
  public void setGasto(int gasto){
    this.gasto = gasto;
  }
  public void setIdPeriodo(int idPeriodo){
    if (idPeriodo == 0){
      throw new RuntimeException("idPeriodo es totalmente necesaria");
    }
    this.idPeriodo = idPeriodo;
  }

  public void merge(AjustesReqDto dto){
    if(dto.nombre() != null){ this.setNombre(dto.nombre()); }
    if(dto.gasto() >= 0){ this.setGasto(dto.gasto());}
    if(dto.idPeriodo() > 0){ this.setIdPeriodo(dto.idPeriodo());}
  };
  
}
