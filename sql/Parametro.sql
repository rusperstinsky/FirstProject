-- Table: gparametro

-- DROP TABLE gparametro;

CREATE TABLE parametro
(
  id_parametro text NOT NULL,
  descr text,
  valor text,
  CONSTRAINT gparametro_pkey PRIMARY KEY (id_parametro)
)
WITH (
  OIDS=TRUE
);
ALTER TABLE parametro OWNER TO postgres;

-- Index: gparametro_id_parametro_idx

-- DROP INDEX gparametro_id_parametro_idx;

CREATE INDEX parametro_id_parametro_idx
  ON parametro
  USING btree
  (id_parametro);

INSERT INTO parametro VALUES('id_sucursal','ID de sucursal actual','1');
