package com.abgraphicdesing.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.abgraphicdesing.database.DbConfig;
import com.abgraphicdesing.model.entity.Historial;

public class HistorialRepository {
  private final DbConfig dbConfig;

  public HistorialRepository(DbConfig dbConfig) {
    this.dbConfig = dbConfig;
  }

  public boolean save(Historial h) {
    String sql = """
      INSERT INTO historial (
        id, titulo, id_impresora, tiempo_grabado, tiempo_obra, id_material,
        cantidad, ganancia, precio_final, fecha_guardado,
        impresora_marca, impresora_modelo, impresora_tipo, impresora_titulo,
        impresora_vida_util, impresora_watts, impresora_precio_compra, impresora_precio_repuesto, impresora_amortizacion,
        material_nombre, material_precio_compra, material_merma, material_precio_costo, material_dimensiones,
        id_impresora_ref, id_material_ref, precio_costo
      ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
    """;
    try (Connection conn = dbConfig.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setObject(1, h.getId());
      ps.setString(2, h.getTitulo());
      ps.setString(3, h.getIdImpresora() == null ? null : h.getIdImpresora().toString());
      ps.setString(4, h.getTiempoGrabado());
      ps.setString(5, h.getTiempoObra());
      ps.setString(6, h.getIdMaterial() == null ? null : h.getIdMaterial().toString());
      ps.setInt(7, h.getCantidad() == null ? 0 : h.getCantidad());
      ps.setInt(8, h.getGanancia() == null ? 0 : h.getGanancia());
      ps.setInt(9, h.getPrecioFinal() == null ? 0 : h.getPrecioFinal());
      ps.setString(10, h.getFechaGuardado());

      ps.setString(11, h.getImpresoraMarca());
      ps.setString(12, h.getImpresoraModelo());
      ps.setString(13, h.getImpresoraTipo());
      ps.setString(14, h.getImpresoraTitulo());
      if (h.getImpresoraVidaUtil() == null) ps.setObject(15, null); else ps.setInt(15, h.getImpresoraVidaUtil());
      if (h.getImpresoraWatts() == null) ps.setObject(16, null); else ps.setInt(16, h.getImpresoraWatts());
      if (h.getImpresoraPrecioCompra() == null) ps.setObject(17, null); else ps.setDouble(17, h.getImpresoraPrecioCompra());
      if (h.getImpresoraPrecioRepuesto() == null) ps.setObject(18, null); else ps.setDouble(18, h.getImpresoraPrecioRepuesto());
      if (h.getImpresoraAmortizacion() == null) ps.setObject(19, null); else ps.setDouble(19, h.getImpresoraAmortizacion());

      ps.setString(20, h.getMaterialNombre());
      if (h.getMaterialPrecioCompra() == null) ps.setObject(21, null); else ps.setDouble(21, h.getMaterialPrecioCompra());
      if (h.getMaterialMerma() == null) ps.setObject(22, null); else ps.setInt(22, h.getMaterialMerma());
      if (h.getMaterialPrecioCosto() == null) ps.setObject(23, null); else ps.setDouble(23, h.getMaterialPrecioCosto());
      ps.setString(24, h.getMaterialDimensiones());

      ps.setString(25, h.getIdImpresoraRef());
      ps.setString(26, h.getIdMaterialRef());
      if (h.getPrecioCosto() == null) ps.setObject(27, null); else ps.setInt(27, h.getPrecioCosto());

      return ps.executeUpdate() > 0;
    } catch (SQLException e) {
      throw new RuntimeException("Error al guardar historial: " + e.getMessage(), e);
    }
  }

  public List<Historial> getAll() {
    String sql = """
      SELECT * FROM historial ORDER BY fecha_guardado DESC, id DESC
    """;
    List<Historial> list = new ArrayList<>();
    try (Connection conn = dbConfig.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {
      while (rs.next()) {
        Historial h = new Historial();
        Object idObj = rs.getObject("id");
        if (idObj != null) h.setId(((Number) idObj).intValue());
        h.setTitulo(rs.getString("titulo"));
        String idImp = rs.getString("id_impresora");
        if (idImp != null) h.setIdImpresora(UUID.fromString(idImp));
        h.setTiempoGrabado(rs.getString("tiempo_grabado"));
        h.setTiempoObra(rs.getString("tiempo_obra"));
        String idMat = rs.getString("id_material");
        if (idMat != null) h.setIdMaterial(UUID.fromString(idMat));
        h.setCantidad(rs.getInt("cantidad"));
        h.setGanancia(rs.getInt("ganancia"));
        Object v = rs.getObject("precio_costo");
        if (v != null) h.setPrecioCosto(((Number)v).intValue());
        h.setPrecioFinal(rs.getInt("precio_final"));
        h.setFechaGuardado(rs.getString("fecha_guardado"));

        h.setImpresoraMarca(rs.getString("impresora_marca"));
        h.setImpresoraModelo(rs.getString("impresora_modelo"));
        h.setImpresoraTipo(rs.getString("impresora_tipo"));
        h.setImpresoraTitulo(rs.getString("impresora_titulo"));
        v = rs.getObject("impresora_vida_util");
        if (v != null) h.setImpresoraVidaUtil(((Number)v).intValue());
        v = rs.getObject("impresora_watts");
        if (v != null) h.setImpresoraWatts(((Number)v).intValue());
        v = rs.getObject("impresora_precio_compra");
        if (v != null) h.setImpresoraPrecioCompra(((Number)v).doubleValue());
        v = rs.getObject("impresora_precio_repuesto");
        if (v != null) h.setImpresoraPrecioRepuesto(((Number)v).doubleValue());
        v = rs.getObject("impresora_amortizacion");
        if (v != null) h.setImpresoraAmortizacion(((Number)v).doubleValue());

        h.setMaterialNombre(rs.getString("material_nombre"));
        v = rs.getObject("material_precio_compra");
        if (v != null) h.setMaterialPrecioCompra(((Number)v).doubleValue());
        v = rs.getObject("material_merma");
        if (v != null) h.setMaterialMerma(((Number)v).intValue());
        v = rs.getObject("material_precio_costo");
        if (v != null) h.setMaterialPrecioCosto(((Number)v).doubleValue());
        h.setMaterialDimensiones(rs.getString("material_dimensiones"));

        h.setIdImpresoraRef(rs.getString("id_impresora_ref"));
        h.setIdMaterialRef(rs.getString("id_material_ref"));
        list.add(h);
      }
      return list;
    } catch (SQLException e) {
      throw new RuntimeException("Error al leer historial: " + e.getMessage(), e);
    }
  }
} 
