-- Function: curr_folio(text)

-- DROP FUNCTION curr_folio(text);

CREATE OR REPLACE FUNCTION curr_folio(text)
  RETURNS text AS
'SELECT value FROM folios WHERE name = $1 AND id_sucursal = esta_sucursal();'
  LANGUAGE sql VOLATILE
  COST 100;
ALTER FUNCTION curr_folio(text) OWNER TO postgres;
