pragma foreign_keys = on;

create table if not exists tipo(
    id integer primary key not null,
    tipo text unique
);

create table if not exists periodo(
    id integer primary key not null,
    periodo text unique
);

create table if not exists impresoras(
    id text primary key not null,
    id_tipo integer references tipo(id),
    marca text,
    modelo text,
    titulo text,
    vida_util integer,
    watts integer,
    precio_compra real(10, 2),
    precio_respuesto real(10, 2),
    amortizacion real(10, 2)
);

create table if not exists materiales(
    id integer primary key not null,
    nombre text,
    precio_compra real(10, 2),
    merma integer,
    precio_costo integer,
    dimensiones text,
    stock integer,
);

create table if not exists ajustes(
    id integer primary key not null,
    nombre text,
    gasto integer
    id_periodo integer references periodo(id),
);

create table if not exists historial(
    id integer primary key not null,
    titulo text,
    id_impresora integer references impresoras(id), 
    tiempo_grabado text,
    tiempo_obra text,
    id_material integer references materiales(id),
    cantidad integer, 
    ganancia integer,
    precio_final integer,
    fecha_guardado text,
);