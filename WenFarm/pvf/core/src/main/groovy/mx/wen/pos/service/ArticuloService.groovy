package mx.wen.pos.service

import mx.wen.pos.model.Articulo

interface ArticuloService {

  Articulo obtenerArticulo( Integer id )

  List<Articulo> listarArticulosPorCodigo( String articulo )

  //List<Articulo> listarArticulosPorCodigo( String articulo, boolean incluyePrecio )

  List<Articulo> listarArticulosPorCodigoSimilar( String articulo )

  //List<Articulo> listarArticulosPorCodigoSimilar( String articulo, boolean incluyePrecio )

  List<Articulo> findArticuloyColor( String articulo, String color )

  String obtenerListaGenericosPrecioVariable( )

  Boolean useShortItemDescription( )

  Articulo buscaArticulo( Integer id )
}
