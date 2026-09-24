package com.abgraphicdesing.model.entity;

import java.util.UUID;

public class Ajustes {
  private UUID id;
  private String nombre;
  private int gasto;
  private UUID idPeriodo;

  public Ajustes(){ }

  public Ajustes(
    String nombre,
    int gasto,
    UUID idPeriodo
  ){
    this.setNombre(nombre);
    this.setGasto(gasto);
    this.setIdPeriodo(idPeriodo);
  }

  public Ajustes(
    String nombre,
    int gasto,
    UUID idPeriodo,
    UUID id
  ){
    this.nombre = nombre;
    this.gasto = gasto;
    this.idPeriodo = idPeriodo;
    this.id = id;
  }

  public UUID getId(){ 
    return this.id; 
  }
  public String getNombre(){ 
    return this.nombre; 
  }
  public int getGasto(){ 
    return this.gasto; 
  }
  public UUID getIdPeriodo(){ 
    return this.idPeriodo; 
  }

  public void setId(UUID id){
    this.id = id;
  }
  public void setNombre(String nombre){
    this.nombre = nombre;
  }
  public void setGasto(int gasto){
    this.gasto = gasto;
  }
  public void setIdPeriodo(UUID idPeriodo){
    if (idPeriodo == null){
      throw new RuntimeException("idPeriodo es totalmente necesaria");
    }
    this.idPeriodo = idPeriodo;
  }
  
}
