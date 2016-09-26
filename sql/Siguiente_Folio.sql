-- Function: next_folio(text)

-- DROP FUNCTION next_folio(text);

CREATE OR REPLACE FUNCTION next_folio(text)
  RETURNS text AS
$BODY$UPDATE folios SET value = incr_folio(curr_folio($1)), fecha_mod = now()
	  WHERE name = $1 AND id_sucursal = esta_sucursal();
	  SELECT curr_folio($1);$BODY$
  LANGUAGE sql VOLATILE
  COST 100;
ALTER FUNCTION next_folio(text) OWNER TO postgres;
