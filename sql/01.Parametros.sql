-- Table: gparametro

-- DROP TABLE gparametro;

CREATE TABLE gparametro
(
  id_parametro text NOT NULL,
  descr text,
  valor text,
  CONSTRAINT gparametro_pkey PRIMARY KEY (id_parametro)
)
WITH ( OIDS=FALSE );
ALTER TABLE gparametro OWNER TO postgres;

-- Index: gparametro_id_parametro_idx

-- DROP INDEX gparametro_id_parametro_idx;

CREATE INDEX gparametro_id_parametro_idx
  ON gparametro
  USING btree
  (id_parametro);

INSERT INTO gparametro VALUES('cli_gen','Id de cliente publico general','1');
INSERT INTO gparametro VALUES('id_sucursal','ID de sucursal actual','1');
INSERT INTO gparametro VALUES('tipo_pago','Tipos de pago activos','EF,TD,TC');
INSERT INTO gparametro VALUES('iva_vigente','Iva vigente','16');
