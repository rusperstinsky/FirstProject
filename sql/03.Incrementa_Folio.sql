-- Function: incr_folio(text)

-- DROP FUNCTION incr_folio(text);

CREATE OR REPLACE FUNCTION incr_folio(text)
  RETURNS text AS
$BODY$
DECLARE
numero int;
letra char;
BEGIN
numero = substr($1, 2)::int + 1;
letra = substr($1, 1, 1)::char;
return letra || trim(to_char(numero, '00000'));
END
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION incr_folio(text) OWNER TO postgres;
