package com.abgraphicdesing.model.dto;

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
  int precioFinal,
  // ---- desglose ----
  int costoManoObra,
  int costoMateriales,
  int costoMaquina,
  int gastosFijos,
  int montoGanancia,
  int costoUnitario,
  int precioUnitario
){ }