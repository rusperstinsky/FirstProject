-- Table: tipo_trans_inv

-- DROP TABLE tipo_trans_inv;

CREATE TABLE tipo_trans_inv
(
  id_tipo_trans text NOT NULL,
  descripcion text,
  tipo_mov text NOT NULL,
  ultimo_folio integer DEFAULT 0,
  CONSTRAINT tipo_trans_inv_pkey PRIMARY KEY (id_tipo_trans)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE tipo_trans_inv OWNER TO postgres;


INSERT INTO tipo_trans_inv VALUES('ENTRADA','RECIBE MERCANCIA','E',0);
INSERT INTO tipo_trans_inv VALUES('ENTRADA_TIENDA','ENTRADA A TIENDA','E',0);
INSERT INTO tipo_trans_inv VALUES('SALIDA','ENVIO A ALMACEN','S',0);
INSERT INTO tipo_trans_inv VALUES('SALIDA_TIENDA','SALIDA DE TIENDA','S',0);
INSERT INTO tipo_trans_inv VALUES('VENTA','SALIDA POR VENTA','S',0);
