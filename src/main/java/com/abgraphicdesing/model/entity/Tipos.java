package com.abgraphicdesing.model.entity;

public class Tipos {
  private int id;
  private String tipo;

  public Tipos(){ }

  public Tipos(String tipo){
    this.setTipo(tipo);
  }
  
  public Tipos(int id, String tipo){
    this.id = id;
    this.tipo = tipo;
  }

  public int getId(){
    return this.id;
  }
  public String getTipo(){
    return this.tipo;
  }

  public void setId(int id){
    this.id = id;
  }
  public void setTipo(String tipo){
    this.tipo = tipo;
  }
}
