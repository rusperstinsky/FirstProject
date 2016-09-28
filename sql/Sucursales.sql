-- Table: sucursales

-- DROP TABLE sucursales;

CREATE TABLE sucursales
(
  nombre text,
  calle text,
  colonia text,
  numero text,
  localidad text,
  id_estado text,
  cp text,
  telefonos text,
  id_gerente integer,
  udf1 text,
  udf2 text,
  udf3 text,
  fecha_mod timestamp without time zone NOT NULL DEFAULT now(),
  id_sucursal integer NOT NULL,
  centro_costos text,
  CONSTRAINT sucursales_pkey PRIMARY KEY (id_sucursal)
)
WITH ( OIDS=FALSE );
ALTER TABLE sucursales OWNER TO postgres;

-- Index: sucursales_cp_idx

-- DROP INDEX sucursales_cp_idx;

CREATE INDEX sucursales_cp_idx
  ON sucursales
  USING btree
  (cp);

-- Index: sucursales_id_estado_idx

-- DROP INDEX sucursales_id_estado_idx;

CREATE INDEX sucursales_id_estado_idx
  ON sucursales
  USING btree
  (id_estado);

-- Index: sucursales_id_sucursal_idx

-- DROP INDEX sucursales_id_sucursal_idx;

CREATE INDEX sucursales_id_sucursal_idx
  ON sucursales
  USING btree
  (id_sucursal);

-- Index: sucursales_localidad_idx

-- DROP INDEX sucursales_localidad_idx;

CREATE INDEX sucursales_localidad_idx
  ON sucursales
  USING btree
  (localidad);

-- Index: sucursales_nombre_idx

-- DROP INDEX sucursales_nombre_idx;

CREATE INDEX sucursales_nombre_idx
  ON sucursales
  USING btree
  (nombre);
