package com.abgraphicdesing.model.dto;

import com.abgraphicdesing.model.entity.Ajustes;

public record AjustesResDto(
  int id,
  String nombre,
  int gasto,
  int idPeriodo,
  String periodo
){
  public AjustesResDto(Ajustes ajuste){
    this(
      ajuste.getId(),
      ajuste.getNombre(),
      ajuste.getGasto(),
      ajuste.getIdPeriodo(),
      ajuste.getPeriodo()
    );
  }
}
