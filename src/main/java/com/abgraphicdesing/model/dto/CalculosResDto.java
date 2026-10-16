package com.abgraphicdesing.model.dto;

import com.abgraphicdesing.model.entity.Calculos;
import com.abgraphicdesing.model.entity.Impresoras;
import com.abgraphicdesing.model.entity.Materiales;

public record CalculosResDto(
  int id,
  float hsManoObra,
  float hsImpresion,
  Materiales material,
  Impresoras impresora,
  int cantidad,
  int porcentajeGanancia,
  int costo,
  int precioFinal
){
  public CalculosResDto(Calculos c){
    this(
      c.getId(),
      c.getHsManoObra(),
      c.getHsImpresion(),
      c.getMaterial(),
      c.getImpresora(),
      c.getCantidad(),
      c.getPorcentajeGanancia(),
      c.getCosto(),
      c.getPrecioFinal()
    );
  }
}
