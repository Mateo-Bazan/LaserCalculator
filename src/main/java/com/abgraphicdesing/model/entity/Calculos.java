package com.abgraphicdesing.model.entity;

import com.abgraphicdesing.model.dto.CalculosResDto;


public class Calculos {
  private int id;
  private float hsManoObra;
  private float hsImpresion;
  private Materiales material;
  private Impresoras impresora;
  private int cantidad; //Representa cuantos productos queres manofacturar.
  private int porcentajeGanancia;
  private int costo;
  private int precioFinal;

  public Calculos(){ };

  public Calculos(
    float hsManoObra,
    float hsImpresion,
    Materiales material,
    Impresoras impresora,
    int cantidad,
    int porcentajeGanancia
  ){
    this.hsManoObra = hsManoObra;
    this.hsImpresion = hsImpresion;
    this.material = material;
    this.impresora = impresora;
    this.cantidad = cantidad;
    this.porcentajeGanancia = porcentajeGanancia;
  }

  public Calculos(
    float hsManoObra,
    float hsImpresion,
    Materiales material,
    Impresoras impresora,
    int cantidad,
    int porcentajeGanancia,
    int costo,
    int precioFinal
  ){
    this.hsManoObra = hsManoObra;
    this.hsImpresion = hsImpresion;
    this.material = material;
    this.impresora = impresora;
    this.cantidad = cantidad;
    this.porcentajeGanancia = porcentajeGanancia;
    this.costo = costo;
    this.precioFinal = precioFinal;
  }

  public int getId() {
    return id;
  }

  public void setHsManoObra(float hsManoObra) {
    this.hsManoObra = hsManoObra;
  }
  public float getHsManoObra() {
    return hsManoObra;
  }

  public void setHsImpresion(float hsImpresion) {
    this.hsImpresion = hsImpresion;
  }
  public float getHsImpresion() {
    return hsImpresion;
  }

  public void setIdMaterial(Materiales material) {
    this.material = material;
  }
  public Materiales getMaterial() {
    return material;
  }

  public void setImpresora(Impresoras impresora) {
    this.impresora = impresora;
  }
  public Impresoras getImpresora() {
    return impresora;
  }

  public void setCantidad(int cantidad) {
    this.cantidad = cantidad;
  }
  public int getCantidad() {
    return cantidad;
  }

  public void setPorcentajeGanancia(int porcentajeGanancia) {
    this.porcentajeGanancia = porcentajeGanancia;
  }
  public int getPorcentajeGanancia() {
    return porcentajeGanancia;
  }

  public void setCosto(int costo) {
    this.costo = costo;
  }
  public int getCosto() {
    return costo;
  }

  public void setPrecioFinal(int precioFinal) {
    this.precioFinal = precioFinal;
  }
  public int getPrecioFinal() {
    return precioFinal;
  }

  public void merge(CalculosResDto dto){
    if(dto.hsManoObra() >= 0){ this.hsManoObra = dto.hsManoObra();}
    if(dto.hsImpresion() >= 0){ this.hsImpresion = dto.hsImpresion();}
    if(dto.material() != null){ this.material = dto.material();}
    if(dto.impresora() != null){ this.impresora = dto.impresora();}
    if(dto.cantidad() > 0){ this.cantidad = dto.cantidad();}
    if(dto.porcentajeGanancia() > 0){ this.porcentajeGanancia = dto.porcentajeGanancia();}
    if(dto.costo() > 0){ this.costo = dto.costo();}
    if(dto.precioFinal() > 0){ this.precioFinal = dto.precioFinal();}
  }
}


