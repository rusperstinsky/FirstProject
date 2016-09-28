-- Table: tipo_pago

-- DROP TABLE tipo_pago;

CREATE TABLE tipo_pago
(
  id_pago text NOT NULL,
  descripcion text,
  f1 character(20),
  f2 character(20),
  f3 character(20),
  f4 character(20),
  f5 character(20),
  CONSTRAINT tipo_pago_pk PRIMARY KEY (id_pago)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE tipo_pago OWNER TO postgres;

INSERT INTO tipo_pago VALUES('EF','EFECTIVO','','','','','');
INSERT INTO tipo_pago VALUES('TD','TARJETA DE DEBITO','Num. Tarjeta','Autorizacion','Id.','Term','');
INSERT INTO tipo_pago VALUES('TC','TARJETA DE CREDITO','Num. Tarjeta','Autorizacion','Id.','Term','Plan');
