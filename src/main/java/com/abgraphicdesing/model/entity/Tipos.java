package com.abgraphicdesing.model.entity;

import java.util.UUID;

public class Tipos {
  private UUID id;
  private String tipo;

  public Tipos(){ }
  public Tipos(String tipo){
    this.setTipo(tipo);
  }
  public Tipos(UUID id, String tipo){
    this.id = id;
    this.tipo = tipo;
  }

  public UUID getId(){
    return this.id;
  }
  public String getTipo(){
    return this.tipo;
  }

  public void setId(UUID id){
    this.id = id;
  }
  public void setTipo(String tipo){
    this.tipo = tipo;
  }
}
