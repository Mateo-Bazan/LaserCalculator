package com.abgraphicdesing.model.dto;

import com.abgraphicdesing.model.entity.Tipos;

public record TiposResDto(
  int id,
  String tipo
){
  public TiposResDto(Tipos tipo){
    this(
      tipo.getId(),
      tipo.getTipo()
    );
  }
} 
