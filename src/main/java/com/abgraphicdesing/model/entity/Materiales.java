package com.abgraphicdesing.model.entity;

import java.util.UUID;
import com.abgraphicdesing.model.dto.MaterialesReqDto;;

public class Materiales {
  private UUID id;
  private String nombre;
  private int precioCompra;
  private int merma;
  private int precioCosto;
  private String dimensiones;
  private int stock;

  public Materiales(){}
  
  public Materiales(
    String nombre, 
    int precioCompra, 
    int merma, 
    String dimensiones, 
    int stock){
      this.setNombre(nombre);
      this.setPrecioCompra(precioCompra);
      this.setMerma(merma);
      this.setPrecioCosto(precioCompra, merma);
      this.setDimensiones(dimensiones);
      this.setStock(stock);
    }
  
  public Materiales(
    String nombre, 
    int precioCompra, 
    int merma, 
    String dimensiones, 
    int stock, UUID id){
      this.nombre = nombre;
      this.precioCompra = precioCompra;
      this.merma = merma;
      this.setPrecioCosto(precioCompra, merma);
      this.dimensiones = dimensiones;
      this.stock = stock;
      this.id = id;
    }

  
  public UUID getId(){
    return id;
  }
  public String getNombre(){
    return nombre;
  }
  public int getPrecioCompra(){
    return precioCompra;
  }
  public int getMerma(){
    return merma;
  }
  public int getPrecioCosto(){
    return precioCosto;
  }
  public String getDimensiones(){
    return dimensiones;
  }
  public int getStock(){
    return stock;
  }

  public void setId(){
    this.id = UUID.randomUUID();
  }
  public void setNombre(String nombre){
    this.nombre = nombre;
  }
  public void setPrecioCompra(int precioCompra){
    this.precioCompra = precioCompra;
  }
  public void setMerma(int merma){
    this.merma = merma;
  }
  public void setPrecioCosto(int precioCompra, int merma){
    this.precioCosto = precioCompra + ((precioCompra*merma)/100);
  }
  public void setDimensiones(String dimensiones){
    this.dimensiones = dimensiones;
  }
  public void setStock(int stock){
    this.stock = stock;
  }

  public void merge(MaterialesReqDto dto){
    if(dto.nombre() != null){ this.setNombre(dto.nombre()); }
    if(dto.precioCompra() > 0){ this.setPrecioCompra(dto.precioCompra());}
    if(dto.merma() > 0){ this.setMerma(dto.merma());}
    setPrecioCosto(this.precioCompra, this.merma);
    if(dto.dimensiones() != null){ this.setDimensiones(dto.dimensiones()); }
    if(dto.stock() > 0){ this.setStock(dto.stock()); }
  };
}
