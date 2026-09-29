package com.abgraphicdesing.model.dto;


public record ImpresorasReqDto(
  int idTipo,
  String marca,
  String modelo,
  String titulo,
  int vidaUtil,
  int watts,
  float precioCompra,
  float precioRespuesto
){}
