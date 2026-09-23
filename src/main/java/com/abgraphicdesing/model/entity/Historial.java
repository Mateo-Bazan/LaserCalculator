package com.abgraphicdesing.model.entity;

import java.util.UUID;

public class Historial {
  private UUID id;
  private String titulo;
  private UUID idImpresora;
  private String tiempoGrabado;
  private String tiempoObra;
  private UUID idMaterial;
  private int cantidad;
  private int ganancia;
  private int precioFinal;
  private String fechaGuardado;

  public Historial(
    UUID id, 
    String titulo, 
    UUID idImpresora, 
    String tiempoGrabado, 
    String tiempoObra, 
    UUID idMaterial, 
    int cantidad, 
    int ganancia, 
    int precioFinal, 
    String fechaGuardado) {
      this.id = id;
      this.titulo = titulo;
      this.idImpresora = idImpresora;
      this.tiempoGrabado = tiempoGrabado;
      this.tiempoObra = tiempoObra;
      this.idMaterial = idMaterial;
      this.cantidad = cantidad;
      this.ganancia = ganancia;
      this.precioFinal = precioFinal;
      this.fechaGuardado = fechaGuardado;
    }
  public Historial(
    String titulo, 
    UUID idImpresora, 
    String tiempoGrabado, 
    String tiempoObra, 
    UUID idMaterial, 
    int cantidad, 
    int ganancia, 
    int precioFinal, 
    String fechaGuardado) {
      this.setTitulo(titulo);
      this.setIdImpresora(idImpresora);
      this.setTiempoGrabado(tiempoGrabado);
      this.setTiempoObra(tiempoObra);
      this.setIdMaterial(idMaterial);
      this.setCantidad(cantidad);
      this.setGanancia(ganancia);
      this.setPrecioFinal(precioFinal);
      this.setFechaGuardado(fechaGuardado);
    }



    public UUID getId() {
        return id;
    }
    public String getTitulo() {
        return titulo;
    }
    public UUID getIdImpresora() {
        return idImpresora;
    }
    public String getTiempoGrabado() {
        return tiempoGrabado;
    }
    public String getTiempoObra() {
        return tiempoObra;
    }
    public UUID getIdMaterial() {
        return idMaterial;
    }
    public Integer getCantidad() {
        return cantidad;
    }
    public Integer getGanancia() {
        return ganancia;
    }
    public Integer getPrecioFinal() {
        return precioFinal;
    }
    public String getFechaGuardado() {
        return fechaGuardado;
    }

    public void setId(UUID id) {
        this.id = id;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    public void setIdImpresora(UUID idImpresora) {
        this.idImpresora = idImpresora;
    }
    public void setTiempoGrabado(String tiempoGrabado) {
        this.tiempoGrabado = tiempoGrabado;
    }
    public void setTiempoObra(String tiempoObra) {
        this.tiempoObra = tiempoObra;
    }
    public void setIdMaterial(UUID idMaterial) {
        this.idMaterial = idMaterial;
    }
    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }
    public void setGanancia(Integer ganancia) {
        this.ganancia = ganancia;
    }
    public void setPrecioFinal(Integer precioFinal) {
        this.precioFinal = precioFinal;
    }
    public void setFechaGuardado(String fechaGuardado) {
        this.fechaGuardado = fechaGuardado;
    }
}
