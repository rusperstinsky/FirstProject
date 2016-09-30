-- Table: pagos

-- DROP TABLE pagos;

CREATE TABLE pagos
(
  id_pago integer NOT NULL DEFAULT nextval(('pagos_id_pago_seq'::text)::regclass),
  id_factura text,
  referencia_pago text,
  monto_pago money,
  fecha_pago timestamp without time zone NOT NULL DEFAULT now(),
  id_empleado integer,
  fecha_mod timestamp without time zone NOT NULL DEFAULT now(),
  id_sucursal integer NOT NULL DEFAULT esta_sucursal(),
  id_f_pago character(5),
  id_plan character(20),
  id_banco text,
  CONSTRAINT pagos_pkey PRIMARY KEY (id_pago)
)
WITH ( OIDS=FALSE );
ALTER TABLE pagos OWNER TO postgres;

-- Index: id_fact_on_pag

-- DROP INDEX id_fact_on_pag;

CREATE INDEX id_fact_on_pag
  ON pagos
  USING btree
  (id_factura);

-- Index: pagos_fecha_pago_idx

-- DROP INDEX pagos_fecha_pago_idx;

CREATE INDEX pagos_fecha_pago_idx
  ON pagos
  USING btree
  (fecha_pago);

-- Index: pagos_id_pago_key

-- DROP INDEX pagos_id_pago_key;

CREATE UNIQUE INDEX pagos_id_pago_key
  ON pagos
  USING btree
  (id_pago);



-- Sequence: pagos_id_pago_seq

-- DROP SEQUENCE pagos_id_pago_seq;

CREATE SEQUENCE pagos_id_pago_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 2147483647
  START 1
  CACHE 1;
ALTER TABLE pagos_id_pago_seq OWNER TO postgres;
