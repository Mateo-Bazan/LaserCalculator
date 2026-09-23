package com.abgraphicdesing.model.entity;

import java.util.UUID;

public class Periodos {
  private UUID id;
  private String periodo;

  public Periodos(){ }
  public Periodos(String periodo){
    this.setPeriodo(periodo);
  }
  public Periodos(UUID id, String periodo){
    this.id = id;
    this.periodo = periodo;
  }

  public UUID getId(){
    return this.id;
  }
  public String getPeriodo(){
    return this.periodo;
  }

  public void setId(UUID id){
    this.id = id;
  }
  public void setPeriodo(String periodo){
    this.periodo = periodo;
  }
}
