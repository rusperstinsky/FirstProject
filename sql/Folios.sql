-- Table: folios

-- DROP TABLE folios;

CREATE TABLE folios
(
  "name" text NOT NULL,
  id_sucursal integer NOT NULL,
  "value" text,
  fecha_mod timestamp without time zone,
  CONSTRAINT folios_pkey PRIMARY KEY (name)
)
WITH ( OIDS=FALSE );
ALTER TABLE folios OWNER TO postgres;

INSERT INTO folios VALUES('nota_venta_id_factura',(SELECT esta_sucursal FROM esta_sucursal()),'A00000',NOW());
