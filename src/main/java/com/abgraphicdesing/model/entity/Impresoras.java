package com.abgraphicdesing.model.entity;

import java.util.UUID;

public class Impresoras {
  private UUID id;
  private UUID idTipo;
  private String marca;
  private String modelo;
  private String titulo;
  private int vidaUtil;
  private int watts;
  private float precioCompra;
  private float precioRespuesto;
  private float amortizacion;

  public Impresoras(){ }

  public Impresoras(UUID idTipo, String marca, String modelo, 
    String titulo, int vidaUtil, int watts, 
    float precioCompra, float precioRespuesto, 
    float amortizacion){
    this.setId();
    this.setIdTipo(idTipo);
    this.setMarca(marca);
    this.setModelo(modelo);
    this.setTitulo(titulo);
    this.setVidaUtil(vidaUtil);
    this.setWatts(watts);
    this.setPrecioCompra(precioCompra);
    this.setPrecioRespuesto(precioRespuesto);
    this.setAmortizacion(amortizacion);
  }

  public Impresoras(
    UUID idTipo, String marca, String modelo, 
    String titulo, int vidaUtil, int watts, 
    float precioCompra, float precioRespuesto, 
    float amortizacion, UUID id
  ){
    this.idTipo = idTipo;
    this.marca = marca;
    this.modelo = modelo;
    this.titulo = titulo;
    this.vidaUtil = vidaUtil;
    this.watts = watts;
    this.precioCompra = precioCompra;
    this.precioRespuesto = precioRespuesto;
    this.amortizacion = amortizacion;
    this.id = id;
  }

  public UUID getId(){
    return id;
  }

  public void setId(){
    this.id = UUID.randomUUID();
  }

  public UUID getIdTipo(){
    return idTipo;
  }

  public void setIdTipo(UUID idTipo){
    if(idTipo == null){
      throw new RuntimeException("idTipo es totalmente necesaria");
    }

    this.idTipo = idTipo;
  }

  public String getMarca(){
    return marca;
  }

  public void setMarca(String marca){
    this.marca = marca;
  }

  public String getModelo(){
    return modelo;
  }

  public void setModelo(String modelo){
    this.modelo = modelo;
  }

  public String getTitulo(){
    return titulo;
  }

  public void setTitulo(String titulo){
    this.titulo = titulo;
  }

  public int getVidaUtil(){
    return vidaUtil;
  }

  public void setVidaUtil(int vidaUtil){
    this.vidaUtil = vidaUtil;
  }

  public int getWatts(){
    return watts;
  }

  public void setWatts(int watts){
    this.watts = watts;
  }

  public float getPrecioCompra(){
    return precioCompra;
  }

  public void setPrecioCompra(float precioCompra){
    this.precioCompra = precioCompra;
  }

  public float getPrecioRespuesto(){
    return precioRespuesto;
  }

  public void setPrecioRespuesto(float precioRespuesto){
    this.precioRespuesto = precioRespuesto;
  }

  public float getAmortizacion(){
    return amortizacion;
  }

  public void setAmortizacion(float amortizacion){
    this.amortizacion = amortizacion;
  }
}
