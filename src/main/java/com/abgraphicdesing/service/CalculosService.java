package com.abgraphicdesing.service;

import java.util.List;

import com.abgraphicdesing.model.dto.CalculosReqDto;
import com.abgraphicdesing.model.dto.CalculosResDto;
import com.abgraphicdesing.model.entity.Ajustes;
import com.abgraphicdesing.model.entity.Calculos;
import com.abgraphicdesing.model.entity.Impresoras;
import com.abgraphicdesing.model.entity.Materiales;
import com.abgraphicdesing.repository.AjustesRepository;
import com.abgraphicdesing.repository.ImpresorasRepository;
import com.abgraphicdesing.repository.MaterialesRepository;

public class CalculosService {
  private final AjustesRepository ajustesRepository;
  private final MaterialesRepository materialesRepository;
  private final ImpresorasRepository impresorasRepository;

  public CalculosService(
    AjustesRepository aRepository, 
    MaterialesRepository mRepository, 
    ImpresorasRepository iRepository
  ){
    this.ajustesRepository = aRepository;
    this.materialesRepository = mRepository;
    this.impresorasRepository = iRepository;
  }

  public CalculosResDto create (CalculosReqDto dto){
    int gastos = 0, costoBase = 0, precioFinal = 0;
    List<Ajustes> ajustes = this.ajustesRepository.getAll();
    Materiales material = new Materiales(); // costo 0 por defecto
    if (dto.material() != null && dto.material().getId() != null) {
      material = this.materialesRepository
        .findById(dto.material().getId())
        .orElse(new Materiales());
    }

    Impresoras impresora = this.impresorasRepository.findById(dto.impresora().getId()).orElse(new Impresoras());

    // CALCULAR Costo Materiales
    costoBase += material.getPrecioCosto() * dto.cantidad();

    // CALCULAR Amortizacion Impresora
    costoBase += impresora.getAmortizacion() * dto.hsImpresion();

    // CALCULAR GASTOS FIJOS, Mano de obra y costo de electricidad
    for(Ajustes a : ajustes){
      int gasto = a.getGasto();
      if(a.getNombre().equals("Mano de obra")){
        precioFinal += (int)(gasto * dto.hsManoObra());
      }
      else if(a.getNombre().equals("kWh")){
        costoBase += (int)( (impresora.getWatts()/1000.0) * (gasto / 26.0 / 8.0) * dto.hsImpresion());
        gastos += (int)(gasto / 26.0 / 8.0);
      }
      else{
        switch (a.getPeriodo()) {
          case "Hora" -> gasto *= 1;
          case "Diario" -> gasto /= 8;
          case "Semanal" -> gasto = (int)((gasto/6.0)/8.0);
          case "Mensual" -> gasto = (int)(gasto / 26.0 / 8.0);
          case "Anual" -> gasto = (int)(((gasto/12.0) /26.0) /8.0);
          default -> throw new IllegalArgumentException("Período desconocido: " + a.getPeriodo());
        }
        gastos += gasto;
      }
    }
    gastos *= dto.hsImpresion()+dto.hsManoObra();
    costoBase += gastos;
    // CALCULAR GASTOS FIJOS

    precioFinal += costoBase + costoBase*((float)(dto.porcentajeGanancia()/100.0));
    
    Calculos guardar = new Calculos(
      dto.hsManoObra(), 
      dto.hsImpresion(), 
      dto.material(), 
      dto.impresora(), 
      dto.cantidad(), 
      dto.porcentajeGanancia(), 
      costoBase, 
      precioFinal
    );

    return new CalculosResDto(guardar);
  }
}
