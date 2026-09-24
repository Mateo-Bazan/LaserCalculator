package com.abgraphicdesing.model.dto;

import java.util.UUID;

public record ImpresorasReqDto(
  UUID idTipo,
  String marca,
  String modelo,
  String titulo,
  int vidaUtil,
  int watts,
  float precioCompra,
  float precioRespuesto
){}
