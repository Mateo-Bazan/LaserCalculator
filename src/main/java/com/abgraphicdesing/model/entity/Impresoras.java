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
    float precioCompra, float precioRespuesto){
    this.setId();
    this.setIdTipo(idTipo);
    this.setMarca(marca);
    this.setModelo(modelo);
    this.setTitulo(titulo);
    this.setVidaUtil(vidaUtil);
    this.setWatts(watts);
    this.setPrecioCompra(precioCompra);
    this.setPrecioRespuesto(precioRespuesto);
    this.setAmortizacion(precioCompra, precioRespuesto, vidaUtil);
  }

  public Impresoras(
    UUID idTipo, String marca, String modelo, 
    String titulo, int vidaUtil, int watts, 
    float precioCompra, float precioRespuesto, UUID id
  ){
    this.idTipo = idTipo;
    this.marca = marca;
    this.modelo = modelo;
    this.titulo = titulo;
    this.vidaUtil = vidaUtil;
    this.watts = watts;
    this.precioCompra = precioCompra;
    this.precioRespuesto = precioRespuesto;
    this.amortizacion = (precioCompra + precioRespuesto) / vidaUtil;
    this.id = id;
  }

  public UUID getId(){
    return id;
  }
  public UUID getIdTipo(){
    return idTipo;
  }
  public String getMarca(){
    return marca;
  }
  public String getModelo(){
    return modelo;
  }
  public String getTitulo(){
    return titulo;
  }
  public int getVidaUtil(){
    return vidaUtil;
  }
  public int getWatts(){
    return watts;
  }
  public float getPrecioCompra(){
    return precioCompra;
  }
  public float getPrecioRespuesto(){
    return precioRespuesto;
  }
  public float getAmortizacion(){
    return amortizacion;
  }


  public void setId(){
    this.id = UUID.randomUUID();
  }

  public void setIdTipo(UUID idTipo){
    if(idTipo == null){
      throw new RuntimeException("idTipo es totalmente necesaria");
    }
    this.idTipo = idTipo;
  }

  public void setMarca(String marca){
    this.marca = marca;
  }
  public void setModelo(String modelo){
    this.modelo = modelo;
  }
  public void setTitulo(String titulo){
    this.titulo = titulo;
  }
  public void setVidaUtil(int vidaUtil){
    this.vidaUtil = vidaUtil;
  }
  public void setWatts(int watts){
    this.watts = watts;
  }
  public void setPrecioCompra(float precioCompra){
    this.precioCompra = precioCompra;
  }
  public void setPrecioRespuesto(float precioRespuesto){
    this.precioRespuesto = precioRespuesto;
  }
  public void setAmortizacion(float precioCompra, float precioRepuesto, int vidaUtil){
    this.amortizacion = (precioCompra + precioRepuesto) / vidaUtil;
  }
}
