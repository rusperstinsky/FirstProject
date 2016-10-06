-- Table: clientes

-- DROP TABLE clientes;

CREATE TABLE clientes
(
  id_cliente integer NOT NULL DEFAULT nextval(('clientes_id_cliente_seq'::text)::regclass),
  localidad text,
  estado text,
  fecha_alta_cli date NOT NULL DEFAULT date(now()),
  nombre_cli text,
  apellido_pat_cli text,
  apellido_mat_cli text,
  rfc_cli character(13),
  direccion_cli text,
  colonia_cli text,
  codigo character(5),
  tel_casa_cli character(15),
  tel_trab_cli character(15),
  ext_trab_cli character(5),
  tel_adi_cli character(15),
  ext_adi_cli character(5),
  email_cli text,
  fecha_mod timestamp without time zone NOT NULL DEFAULT now(),
  id_sucursal integer NOT NULL DEFAULT esta_sucursal(),
  udf1 text,
  udf2 text,
  udf3 text,
  udf4 text,
  udf5 text,
  udf6 text,
  obs text,
  fecha_nac date,
  hora_alta time without time zone,
  CONSTRAINT clientes_pkey PRIMARY KEY (id_cliente)
)
WITH ( OIDS=FALSE );
ALTER TABLE clientes OWNER TO postgres;

-- Index: apellido_pat_index

-- DROP INDEX apellido_pat_index;

CREATE INDEX apellido_pat_index
  ON clientes
  USING btree
  (apellido_pat_cli);

-- Index: clientes_apellido_mat_cli_idx

-- DROP INDEX clientes_apellido_mat_cli_idx;

CREATE INDEX clientes_apellido_mat_cli_idx
  ON clientes
  USING btree
  (apellido_mat_cli);

-- Index: clientes_id_cliente_key

-- DROP INDEX clientes_id_cliente_key;

CREATE UNIQUE INDEX clientes_id_cliente_key
  ON clientes
  USING btree
  (id_cliente);

-- Index: clientes_nombre_cli_idx

-- DROP INDEX clientes_nombre_cli_idx;

CREATE INDEX clientes_nombre_cli_idx
  ON clientes
  USING btree
  (nombre_cli);

-- Index: clientes_rfc_cli_idx

-- DROP INDEX clientes_rfc_cli_idx;

CREATE INDEX clientes_rfc_cli_idx
  ON clientes
  USING btree
  (rfc_cli);


-- Sequence: clientes_id_cliente_seq

-- DROP SEQUENCE clientes_id_cliente_seq;

CREATE SEQUENCE clientes_id_cliente_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 2147483647
  START 1
  CACHE 1;
ALTER TABLE clientes_id_cliente_seq OWNER TO postgres;



INSERT INTO clientes(nombre_cli,apellido_pat_cli,apellido_mat_cli) VALUES('PUBLICO','GENERAL','');
