-- Table: detalle_nota_ven

-- DROP TABLE detalle_nota_ven;

CREATE TABLE detalle_nota_ven
(
  id_factura text NOT NULL,
  id_articulo integer NOT NULL,
  cantidad_fac double precision,
  precio_unit_lista money,
  precio_unit_final money,
  fecha_mod timestamp without time zone NOT NULL DEFAULT now(),
  id_sucursal integer NOT NULL,
  id serial NOT NULL,
  CONSTRAINT detalle_nota_ven_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=TRUE
);
ALTER TABLE detalle_nota_ven OWNER TO postgres;

-- Index: detalle_nota_ven_id_idx

-- DROP INDEX detalle_nota_ven_id_idx;

CREATE INDEX detalle_nota_ven_id_idx
  ON detalle_nota_ven
  USING btree
  (id);

-- Index: id_art_on_dnv

-- DROP INDEX id_art_on_dnv;

CREATE INDEX id_art_on_dnv
  ON detalle_nota_ven
  USING btree
  (id_articulo);

-- Index: id_fac_on_dnv

-- DROP INDEX id_fac_on_dnv;

CREATE INDEX id_fac_on_dnv
  ON detalle_nota_ven
  USING btree
  (id_factura);



-- Sequence: empleado_id_empleado_seq

-- DROP SEQUENCE empleado_id_empleado_seq;

CREATE SEQUENCE detalle_nota_ven_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE detalle_nota_ven_id_seq OWNER TO postgres;

