package com.abgraphicdesing.model.dto;

import java.util.UUID;

import com.abgraphicdesing.model.entity.Materiales;



public record MaterialesResDto(
  UUID id,
  String nombre,
  int precioCompra,
  int merma,
  int precioCosto,
  String dimensiones,
  int stock
){
  public MaterialesResDto(Materiales m){
    this(
      m.getId(),
      m.getNombre(),
      m.getPrecioCompra(),
      m.getMerma(),
      m.getPrecioCosto(),
      m.getDimensiones(),
      m.getStock()
    );
  }
}
