package com.abgraphicdesing.model.dto;

import java.util.UUID;

import com.abgraphicdesing.model.entity.Impresoras;

public record ImpresorasResDto(
  UUID id,
  int idTipo,
  String marca,
  String modelo,
  String titulo,
  int vidaUtil,
  int watts,
  float precioCompra,
  float precioRespuesto,
  float amortizacion
){
  public ImpresorasResDto(Impresoras impresora){
    this(
      impresora.getId(),
      impresora.getIdTipo(),
      impresora.getMarca(),
      impresora.getModelo(),
      impresora.getTitulo(),
      impresora.getVidaUtil(),
      impresora.getWatts(),
      impresora.getPrecioCompra(),
      impresora.getPrecioRespuesto(),
      impresora.getAmortizacion()
    );
  }  
}
