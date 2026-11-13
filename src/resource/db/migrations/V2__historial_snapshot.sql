PRAGMA foreign_keys = off;


ALTER TABLE historial ADD COLUMN impresora_marca TEXT;
ALTER TABLE historial ADD COLUMN impresora_modelo TEXT;
ALTER TABLE historial ADD COLUMN impresora_tipo TEXT;
ALTER TABLE historial ADD COLUMN impresora_titulo TEXT;
ALTER TABLE historial ADD COLUMN impresora_vida_util INTEGER;
ALTER TABLE historial ADD COLUMN impresora_watts INTEGER;
ALTER TABLE historial ADD COLUMN impresora_precio_compra REAL;
ALTER TABLE historial ADD COLUMN impresora_precio_repuesto REAL;
ALTER TABLE historial ADD COLUMN impresora_amortizacion REAL;

ALTER TABLE historial ADD COLUMN material_nombre TEXT;
ALTER TABLE historial ADD COLUMN material_precio_compra REAL;
ALTER TABLE historial ADD COLUMN material_merma INTEGER;
ALTER TABLE historial ADD COLUMN material_precio_costo REAL;
ALTER TABLE historial ADD COLUMN material_dimensiones TEXT;

ALTER TABLE historial ADD COLUMN id_impresora_ref TEXT;
ALTER TABLE historial ADD COLUMN id_material_ref TEXT;

UPDATE historial SET 
  id_impresora_ref = id_impresora,
  id_material_ref = id_material
WHERE id_impresora_ref IS NULL OR id_material_ref IS NULL;


PRAGMA foreign_keys = on;
