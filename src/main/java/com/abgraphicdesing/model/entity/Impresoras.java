package com.abgraphicdesing.model.entity;

import java.util.UUID;

import com.abgraphicdesing.model.dto.ImpresorasReqDto;

public class Impresoras {
  private UUID id;
  private Tipos tipo;
  private String marca;
  private String modelo;
  private String titulo;
  private int vidaUtil;
  private int watts;
  private float precioCompra;
  private float precioRespuesto;
  private float amortizacion;

  public Impresoras(){ }

  public Impresoras(Tipos tipo, String marca, String modelo, 
    String titulo, int vidaUtil, int watts, 
    float precioCompra, float precioRespuesto){
    this.setId();
    this.setTipo(tipo);
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
    Tipos tipo, String marca, String modelo, 
    String titulo, int vidaUtil, int watts, 
    float precioCompra, float precioRespuesto, UUID id
  ){
    this.tipo = tipo;
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
  public Tipos getTipo(){
    return tipo;
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

  public void setTipo(Tipos tipo){
    if(tipo == null){
      throw new RuntimeException("tipo es obligatorio");
    }
    this.tipo = tipo;
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

  public void merge(ImpresorasReqDto dto){
    //----------------------------
    //Como impresora recibe la entidad Tipo y no un int (para guardar solo el id)...
    //...este if no funciona
    //if(dto.idTipo() > 0){ this.setTipo(dto.idTipo()); } 
    if(dto.idTipo() > 0){ this.setTipo(new Tipos(dto.idTipo(), null)); }
    //----------------------------
    if(dto.marca() != null){ this.setMarca(dto.marca()); }
    if(dto.modelo() != null){ this.setModelo(dto.modelo());}
    if(dto.titulo() != null){ this.setTitulo(dto.titulo());}
    if(dto.precioCompra() > 0){
      this.setPrecioCompra(dto.precioCompra());
      this.setAmortizacion(dto.precioCompra(), this.precioRespuesto, this.vidaUtil);
    }
    if(dto.precioRespuesto() > 0){
      this.setPrecioRespuesto(dto.precioRespuesto());
      this.setAmortizacion(this.precioCompra, dto.precioRespuesto(), this.vidaUtil);
    }
    if(dto.vidaUtil() > 0){ 
      this.setVidaUtil(dto.vidaUtil());
      this.setAmortizacion(this.precioCompra, this.precioRespuesto, dto.vidaUtil());
    }
    if(dto.watts() > 0){this.setWatts(dto.watts());}
  };
}