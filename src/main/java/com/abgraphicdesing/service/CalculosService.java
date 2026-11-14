package com.abgraphicdesing.service;

import java.util.List;

import com.abgraphicdesing.model.dto.CalculosReqDto;
import com.abgraphicdesing.model.dto.CalculosResDto;
import com.abgraphicdesing.model.entity.Ajustes;
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

  public CalculosResDto create(CalculosReqDto dto){
    List<Ajustes> ajustes = this.ajustesRepository.getAll();

    Materiales material = new Materiales(); // costo 0 por defecto ("Ninguno")
    if (dto.material() != null && dto.material().getId() != null) {
      material = this.materialesRepository
        .findById(dto.material().getId())
        .orElse(new Materiales());
    }
    Impresoras impresora = this.impresorasRepository
      .findById(dto.impresora().getId())
      .orElse(new Impresoras());

    // ---------- 1) Valores POR HORA que salen de Ajustes ----------
    double costoHoraManoObra = 0;      
    double costoHoraKwh = 0;           
    double gastosFijosPorHora = 0;     

    for (Ajustes a : ajustes) {
      double gasto = a.getGasto();
      if (a.getNombre().equals("Mano de obra")) {
        costoHoraManoObra = gasto;
      } else if (a.getNombre().equals("kWh")) {
        costoHoraKwh = gasto / 26.0 / 8.0;
        gastosFijosPorHora += costoHoraKwh;   // la luz también es gasto fijo (como en el Excel)
      } else {
        gastosFijosPorHora += aCostoPorHora(gasto, a.getPeriodo());
      }
    }

    // ---------- 2) Un concepto = una variable (igual que filas 36 a 39) ----------
    double hsImpresion = dto.hsImpresion();
    double hsManoObra = dto.hsManoObra();

    int costoManoObra = redondearArriba(costoHoraManoObra * hsManoObra);

    int costoMateriales = redondearArriba((double) material.getPrecioCosto() * dto.cantidad());

    double costoHoraMaquina = impresora.getAmortizacion()
      + (impresora.getWatts() / 1000.0) * costoHoraKwh;
    int costoMaquina = redondearArriba(costoHoraMaquina * hsImpresion);

    int gastosFijos = redondearArriba(gastosFijosPorHora * (hsImpresion + hsManoObra));

    // ---------- 3) Totales: se suman las filas YA redondeadas ----------
    int costoTotal = costoManoObra + costoMateriales + costoMaquina + gastosFijos;

    // La ganancia NO se aplica a la mano de obra
    int baseConGanancia = costoMateriales + costoMaquina + gastosFijos;
    int montoGanancia = redondearArriba(baseConGanancia * dto.porcentajeGanancia() / 100.0);

    int precioFinal = costoTotal + montoGanancia;

    // ---------- 4) Por unidad ----------
    int unidades = Math.max(dto.cantidad(), 1);   // evita dividir por 0
    int costoUnitario = redondearArriba((double) costoTotal / unidades);
    int precioUnitario = redondearArriba((double) precioFinal / unidades);

    return new CalculosResDto(
      0,
      dto.hsManoObra(),
      dto.hsImpresion(),
      dto.material(),
      dto.impresora(),
      dto.cantidad(),
      dto.porcentajeGanancia(),
      costoTotal,
      precioFinal,
      costoManoObra,
      costoMateriales,
      costoMaquina,
      gastosFijos,
      montoGanancia,
      costoUnitario,
      precioUnitario
    );
  }

  /** Convierte un gasto de cualquier período a "pesos por hora de trabajo". */
  private double aCostoPorHora(double gasto, String periodo){
    return switch (periodo) {
      case "Hora" -> gasto;
      case "Diario" -> gasto / 8.0;
      case "Semanal" -> gasto / 6.0 / 8.0;
      case "Mensual" -> gasto / 26.0 / 8.0;
      case "Anual" -> gasto / 12.0 / 26.0 / 8.0;
      default -> throw new IllegalArgumentException("Período desconocido: " + periodo);
    };
  }

  /**
   * Redondea hacia arriba, pero primero "limpia" el ruido de los float/double.
   * Sin esto, algo que matemáticamente es 300 pero la computadora guarda como
   * 300.0000001 terminaría como 301.
   */
  private static int redondearArriba(double valor){
    return (int) Math.ceil(Math.round(valor * 1_000_000d) / 1_000_000d);
  }
}