-- Function: esta_sucursal()

-- DROP FUNCTION esta_sucursal();

CREATE OR REPLACE FUNCTION esta_sucursal()
  RETURNS integer AS
$BODY$ SELECT valor::integer FROM gparametro WHERE id_parametro = 'id_sucursal' $BODY$
  LANGUAGE sql VOLATILE
  COST 100;
ALTER FUNCTION esta_sucursal() OWNER TO postgres;
