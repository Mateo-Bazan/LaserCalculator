-- El consumo de kWh se registra como gasto mensual.
-- Se actualizan también las bases existentes que lo tenían como semanal.
UPDATE ajustes
SET id_periodo = COALESCE(
  (SELECT id FROM periodo WHERE periodo = 'Mensual' LIMIT 1),
  id_periodo
)
WHERE nombre = 'kWh';
