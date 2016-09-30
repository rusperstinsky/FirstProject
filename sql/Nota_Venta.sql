-- Table: nota_venta

-- DROP TABLE nota_venta;

CREATE TABLE nota_venta
(
  id_factura text NOT NULL DEFAULT next_folio('nota_venta_id_factura'::text),
  id_empleado integer,
  id_cliente integer,
  venta_total money DEFAULT '$0.00'::money,
  venta_neta money DEFAULT '$0.00'::money,
  suma_pagos money DEFAULT '$0.00'::money,
  fecha_hora_factura timestamp without time zone NOT NULL DEFAULT now(),
  por100_descuento integer,
  monto_descuento money,
  tipo_descuento character(1),
  s_factura character(1) NOT NULL DEFAULT 'N'::bpchar,
  observaciones_nv text,
  fecha_mod timestamp without time zone NOT NULL DEFAULT now(),
  id_sucursal integer NOT NULL DEFAULT esta_sucursal(),
  factura text,
  udf2 text,
  udf3 text,
  udf4 text,
  udf5 text,
  CONSTRAINT nota_venta_pkey PRIMARY KEY (id_factura)
)
WITH ( OIDS=FALSE );
ALTER TABLE nota_venta OWNER TO postgres;

-- Index: factura_on_nv

-- DROP INDEX factura_on_nv;

CREATE INDEX factura_on_nv
  ON nota_venta
  USING btree
  (factura);

-- Index: id_cli_on_nv

-- DROP INDEX id_cli_on_nv;

CREATE INDEX id_cli_on_nv
  ON nota_venta
  USING btree
  (id_cliente);

-- Index: nota_venta_id_factura_idx

-- DROP INDEX nota_venta_id_factura_idx;

CREATE INDEX nota_venta_id_factura_idx
  ON nota_venta
  USING btree
  (id_factura);


-- Sequence: factura_seq

-- DROP SEQUENCE factura_seq;

CREATE SEQUENCE factura_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 1000000
  START 1
  CACHE 1;
ALTER TABLE factura_seq OWNER TO postgres;

