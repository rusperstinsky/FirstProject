-- Table: trans_inv_det

-- DROP TABLE trans_inv_det;

CREATE TABLE trans_inv_det
(
  num_reg serial NOT NULL,
  id_tipo_trans text NOT NULL,
  folio integer NOT NULL,
  linea integer NOT NULL,
  sku integer NOT NULL,
  tipo_mov text NOT NULL,
  cantidad integer NOT NULL,
  id_sucursal integer NOT NULL,
  CONSTRAINT trans_inv_det_pkey PRIMARY KEY (id_tipo_trans, folio, sku, id_sucursal),
  CONSTRAINT trans_inv_det_id_tipo_trans_fkey FOREIGN KEY (id_tipo_trans, folio, id_sucursal)
      REFERENCES trans_inv (id_tipo_trans, folio, id_sucursal) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE,
  CONSTRAINT trans_inv_det_sku_fkey FOREIGN KEY (sku, id_sucursal)
      REFERENCES articulos (id_articulo, id_sucursal) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT trans_inv_det_num_reg_key UNIQUE (num_reg)
)
WITH ( OIDS=FALSE );
ALTER TABLE trans_inv_det OWNER TO postgres;

-- Index: trans_inv_det_sku_idx

-- DROP INDEX trans_inv_det_sku_idx;

CREATE INDEX trans_inv_det_sku_idx
  ON trans_inv_det
  USING btree
  (sku);
