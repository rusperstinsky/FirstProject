-- Table: articulos

-- DROP TABLE articulos;

CREATE TABLE articulos
(
  id_articulo integer NOT NULL,
  articulo character(30) NOT NULL,
  desc_articulo text,
  precio money,
  fecha_mod timestamp without time zone NOT NULL DEFAULT now(),
  id_sucursal integer NOT NULL DEFAULT esta_sucursal(),
  existencia integer,
  tipo text,
  subtipo text,
  marca text,
  proveedor text,
  CONSTRAINT articulos_pkey PRIMARY KEY (id_articulo,id_sucursal)
)
WITH ( OIDS=FALSE );
ALTER TABLE articulos OWNER TO postgres;

-- Index: artic_on_art

-- DROP INDEX artic_on_art;

CREATE INDEX artic_on_art
  ON articulos
  USING btree
  (articulo);

-- Index: articulos_id_articulo_idx

-- DROP INDEX articulos_id_articulo_idx;

CREATE INDEX articulos_id_articulo_idx
  ON articulos
  USING btree
  (id_articulo);

-- Index: sucursal_on_art

-- DROP INDEX sucursal_on_art;

CREATE INDEX sucursal_on_art
  ON articulos
  USING btree
  (id_sucursal);
