-- Table: trans_inv

-- DROP TABLE trans_inv;

CREATE TABLE trans_inv
(
  num_reg serial NOT NULL,
  id_tipo_trans text NOT NULL,
  folio integer NOT NULL,
  fecha date NOT NULL,
  id_sucursal integer NOT NULL,
  id_sucursal_destino integer,
  referencia text,
  observaciones text,
  id_empleado integer NOT NULL,
  fecha_mod timestamp without time zone DEFAULT now(),
  CONSTRAINT trans_inv_pkey PRIMARY KEY (id_tipo_trans, folio,id_sucursal),
  CONSTRAINT trans_inv_id_empleado_fkey FOREIGN KEY (id_empleado)
      REFERENCES empleado (id_empleado) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT trans_inv_id_sucursal_fkey FOREIGN KEY (id_sucursal)
      REFERENCES sucursales (id_sucursal) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT trans_inv_id_tipo_trans_fkey FOREIGN KEY (id_tipo_trans)
      REFERENCES tipo_trans_inv (id_tipo_trans) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT trans_inv_num_reg_key UNIQUE (num_reg)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE trans_inv OWNER TO postgres;

-- Index: trans_inv_fecha_idx

-- DROP INDEX trans_inv_fecha_idx;

CREATE INDEX trans_inv_fecha_idx
  ON trans_inv
  USING btree
  (fecha);

-- Index: trans_inv_id_empleado_idx

-- DROP INDEX trans_inv_id_empleado_idx;

CREATE INDEX trans_inv_id_empleado_idx
  ON trans_inv
  USING btree
  (id_empleado, fecha);

-- Index: trans_inv_id_sucursal_destino_idx

-- DROP INDEX trans_inv_id_sucursal_destino_idx;

CREATE INDEX trans_inv_id_sucursal_destino_idx
  ON trans_inv
  USING btree
  (id_sucursal_destino);


