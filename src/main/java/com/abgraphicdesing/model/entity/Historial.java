package com.abgraphicdesing.model.entity;

import java.util.UUID;

public class Historial {
  private Integer id;
  private String titulo;
  private UUID idImpresora;
  private String tiempoGrabado;
  private String tiempoObra;
  private UUID idMaterial;
  private int cantidad;
  private int ganancia;
  private Integer precioCosto;
  private int precioFinal;
  private String fechaGuardado;

  // Snapshot impresora
  private String impresoraMarca;
  private String impresoraModelo;
  private String impresoraTipo;
  private String impresoraTitulo;
  private Integer impresoraVidaUtil;
  private Integer impresoraWatts;
  private Double impresoraPrecioCompra;
  private Double impresoraPrecioRepuesto;
  private Double impresoraAmortizacion;

  // Snapshot material
  private String materialNombre;
  private Double materialPrecioCompra;
  private Integer materialMerma;
  private Double materialPrecioCosto;
  private String materialDimensiones;

  // Referencias opcionales
  private String idImpresoraRef;
  private String idMaterialRef;

  public Historial(
    Integer id,
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

  public Historial() {}



    public Integer getId() {
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
    public Integer getPrecioCosto() {
        return precioCosto;
    }
    public Integer getPrecioFinal() {
        return precioFinal;
    }
    public String getFechaGuardado() {
        return fechaGuardado;
    }

    public void setId(Integer id) {
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
    public void setPrecioCosto(Integer precioCosto) {
        this.precioCosto = precioCosto;
    }
    public void setPrecioFinal(Integer precioFinal) {
        this.precioFinal = precioFinal;
    }
    public void setFechaGuardado(String fechaGuardado) {
        this.fechaGuardado = fechaGuardado;
    }

    public String getImpresoraMarca() { return impresoraMarca; }
    public void setImpresoraMarca(String impresoraMarca) { this.impresoraMarca = impresoraMarca; }
    public String getImpresoraModelo() { return impresoraModelo; }
    public void setImpresoraModelo(String impresoraModelo) { this.impresoraModelo = impresoraModelo; }
    public String getImpresoraTipo() { return impresoraTipo; }
    public void setImpresoraTipo(String impresoraTipo) { this.impresoraTipo = impresoraTipo; }
    public String getImpresoraTitulo() { return impresoraTitulo; }
    public void setImpresoraTitulo(String impresoraTitulo) { this.impresoraTitulo = impresoraTitulo; }
    public Integer getImpresoraVidaUtil() { return impresoraVidaUtil; }
    public void setImpresoraVidaUtil(Integer impresoraVidaUtil) { this.impresoraVidaUtil = impresoraVidaUtil; }
    public Integer getImpresoraWatts() { return impresoraWatts; }
    public void setImpresoraWatts(Integer impresoraWatts) { this.impresoraWatts = impresoraWatts; }
    public Double getImpresoraPrecioCompra() { return impresoraPrecioCompra; }
    public void setImpresoraPrecioCompra(Double impresoraPrecioCompra) { this.impresoraPrecioCompra = impresoraPrecioCompra; }
    public Double getImpresoraPrecioRepuesto() { return impresoraPrecioRepuesto; }
    public void setImpresoraPrecioRepuesto(Double impresoraPrecioRepuesto) { this.impresoraPrecioRepuesto = impresoraPrecioRepuesto; }
    public Double getImpresoraAmortizacion() { return impresoraAmortizacion; }
    public void setImpresoraAmortizacion(Double impresoraAmortizacion) { this.impresoraAmortizacion = impresoraAmortizacion; }

    public String getMaterialNombre() { return materialNombre; }
    public void setMaterialNombre(String materialNombre) { this.materialNombre = materialNombre; }
    public Double getMaterialPrecioCompra() { return materialPrecioCompra; }
    public void setMaterialPrecioCompra(Double materialPrecioCompra) { this.materialPrecioCompra = materialPrecioCompra; }
    public Integer getMaterialMerma() { return materialMerma; }
    public void setMaterialMerma(Integer materialMerma) { this.materialMerma = materialMerma; }
    public Double getMaterialPrecioCosto() { return materialPrecioCosto; }
    public void setMaterialPrecioCosto(Double materialPrecioCosto) { this.materialPrecioCosto = materialPrecioCosto; }
    public String getMaterialDimensiones() { return materialDimensiones; }
    public void setMaterialDimensiones(String materialDimensiones) { this.materialDimensiones = materialDimensiones; }

    public String getIdImpresoraRef() { return idImpresoraRef; }
    public void setIdImpresoraRef(String idImpresoraRef) { this.idImpresoraRef = idImpresoraRef; }
    public String getIdMaterialRef() { return idMaterialRef; }
    public void setIdMaterialRef(String idMaterialRef) { this.idMaterialRef = idMaterialRef; }
}
