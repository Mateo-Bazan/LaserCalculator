package com.abgraphicdesing.model.dto;

import com.abgraphicdesing.model.entity.Impresoras;
import com.abgraphicdesing.model.entity.Materiales;

public record CalculosReqDto(
  float hsManoObra,
  float hsImpresion,
  Materiales material,
  Impresoras impresora,
  int cantidad,
  int porcentajeGanancia
){}
