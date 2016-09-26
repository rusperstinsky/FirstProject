-- Table: empleado

-- DROP TABLE empleado;

CREATE TABLE empleado
(
  id_empleado serial NOT NULL,
  nombre_empleado text,
  ap_paterno text,
  ap_materno text,
  id_puesto integer,
  passwd character(10),
  fecha_alta timestamp without time zone NOT NULL DEFAULT now(),
  fecha_mod timestamp without time zone NOT NULL DEFAULT now(),
  id_sucursal integer NOT NULL DEFAULT esta_sucursal(),
  CONSTRAINT empleado_pkey PRIMARY KEY (id_empleado)
)
WITH ( OIDS=FALSE );
ALTER TABLE empleado OWNER TO postgres;

-- Index: empleado_ap_mat_empleado_idx

-- DROP INDEX empleado_ap_mat_empleado_idx;

CREATE INDEX empleado_ap_mat_empleado_idx
  ON empleado
  USING btree
  (ap_materno);

-- Index: empleado_ap_pat_empleado_idx

-- DROP INDEX empleado_ap_pat_empleado_idx;

CREATE INDEX empleado_ap_pat_empleado_idx
  ON empleado
  USING btree
  (ap_paterno);

-- Index: empleado_id_empleado_idx

-- DROP INDEX empleado_id_empleado_idx;

CREATE INDEX empleado_id_empleado_idx
  ON empleado
  USING btree
  (id_empleado);

-- Index: empleado_id_puesto_idx

-- DROP INDEX empleado_id_puesto_idx;

CREATE INDEX empleado_id_puesto_idx
  ON empleado
  USING btree
  (id_puesto);

-- Index: empleado_id_sucursal_idx

-- DROP INDEX empleado_id_sucursal_idx;

CREATE INDEX empleado_id_sucursal_idx
  ON empleado
  USING btree
  (id_sucursal);

-- Index: empleado_nombre_empleado_idx

-- DROP INDEX empleado_nombre_empleado_idx;

CREATE INDEX empleado_nombre_empleado_idx
  ON empleado
  USING btree
  (nombre_empleado);
