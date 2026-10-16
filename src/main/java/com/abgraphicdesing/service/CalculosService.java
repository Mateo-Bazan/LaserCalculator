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
    Materiales material = this.materialesRepository.findById(dto.material().getId()).orElse(new Materiales());
    Impresoras impresora = this.impresorasRepository.findById(dto.impresora().getId()).orElse(new Impresoras());

    // CALCULAR Costo Materiales
    costoBase += material.getPrecioCosto() * dto.cantidad();
    // CALCULAR Costo Materiales

    // CALCULAR Amortizacion Impresora
    costoBase += impresora.getAmortizacion() * dto.hsImpresion();
    // CALCULAR Amortizacion Impresora



    // CALCULAR GASTOS FIJOS, Mano de obra y costo de electricidad
    for(Ajustes a : ajustes){
      int gasto = a.getGasto();
      if(a.getNombre().equals("Mano de obra")){
        precioFinal += (int)(a.getGasto() * dto.hsManoObra());
      }
      else if(a.getNombre().equals("kWh")){
        costoBase += (int)( (impresora.getWatts()/1000.0) * (a.getGasto()/26/8) * dto.hsImpresion());
      }
      else{
        switch (a.getPeriodo()) {
          case "Diario":
            gasto /= 8;
            break;
          case "Semanal":
            gasto = (gasto/6) /8;
            break;
          case "Mensual":
            gasto = (gasto/12) / 26 / 8;
            break;
          case "Anual":
              gasto = (gasto/26) /8;
            break;
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
