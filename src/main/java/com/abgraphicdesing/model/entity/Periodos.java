package com.abgraphicdesing.model.entity;

public class Periodos {
  private int id;
  private String periodo;

  public Periodos(){ }
  public Periodos(String periodo){
    this.setPeriodo(periodo);
  }
  public Periodos(int id, String periodo){
    this.id = id;
    this.periodo = periodo;
  }

  public int getId(){
    return this.id;
  }
  public String getPeriodo(){
    return this.periodo;
  }

  public void setId(int id){
    this.id = id;
  }
  public void setPeriodo(String periodo){
    this.periodo = periodo;
  }
}
