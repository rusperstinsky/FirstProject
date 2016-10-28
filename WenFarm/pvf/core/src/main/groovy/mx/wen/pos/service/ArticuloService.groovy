package mx.wen.pos.service

import mx.wen.pos.model.Articulo
import mx.wen.pos.model.TipoProd

interface ArticuloService {

  Articulo obtenerArticulo( Integer id )

  List<Articulo> obtenerArticuloPorTipo( String tipo )

  List<Articulo> listarArticulosPorCodigo( String articulo )

  //List<Articulo> listarArticulosPorCodigo( String articulo, boolean incluyePrecio )

  List<Articulo> listarArticulosPorCodigoSimilar( String articulo )

  //List<Articulo> listarArticulosPorCodigoSimilar( String articulo, boolean incluyePrecio )

  List<Articulo> obtenerListaArticulosPorId( List<Integer> pListaId )

  List<Articulo> findArticuloyColor( String articulo, String color )

  Boolean validarArticulo( Integer id )

  Articulo buscaArticulo( Integer id )

  Boolean registrarListaArticulos( List<Articulo> pListaArticulo )

  List<TipoProd> listarTiposArticulo( )

  void registraArticulo( Articulo articulo )
}
