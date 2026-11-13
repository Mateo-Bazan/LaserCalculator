package com.abgraphicdesing.view.mapper;

import com.abgraphicdesing.model.dto.ImpresorasResDto;
import com.abgraphicdesing.model.dto.MaterialesResDto;
import com.abgraphicdesing.model.entity.Impresoras;
import com.abgraphicdesing.model.entity.Materiales;
import com.abgraphicdesing.model.entity.Tipos;

public final class CalculatorMapper {
  private CalculatorMapper() {}

  public static Materiales toMaterial(MaterialesResDto m) {
    if (m == null) return null;
    return new Materiales(
      m.nombre(), m.precioCompra(), m.merma(), m.dimensiones(), m.stock(), m.id());
  }

  public static Impresoras toImpresora(ImpresorasResDto i) {
    if (i == null) return null;
    return new Impresoras(
      new Tipos(i.tipo()), i.marca(), i.modelo(), i.titulo(),
      i.vidaUtil(), i.watts(), i.precioCompra(), i.precioRespuesto(), i.id());
  }
} 
