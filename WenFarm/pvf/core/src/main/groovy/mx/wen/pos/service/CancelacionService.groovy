package mx.wen.pos.service

import mx.wen.pos.model.*

interface CancelacionService {

  Devolucion registrarDevolucionesDeNotaVenta( String idNotaVenta, String causaDev )

  Boolean registrarCancelacionDeNotaVenta( String idNotaVenta )

  /*List<CausaCancelacion> listarCausasCancelacion( )

  boolean permitirCancelacionExtemporanea( String idNotaVenta )

  List<Devolucion> listarDevolucionesDeNotaVenta( String idNotaVenta )

  List<Pago> registrarTransferenciasParaNotaVenta( String idOrigen, String idDestino, Map<Integer, BigDecimal> transferenciasPagos )

  List<NotaVenta> listarNotasVentaOrigenDeNotaVenta( String idNotaVenta )

  BigDecimal obtenerCreditoDeNotaVenta( String idNotaVenta )

  void restablecerValoresDeCancelacion( String idNotaVenta )

  Boolean validandoTransferencia( String idNotaVenta )

  void restablecerMontoAlBorrarPago( Integer idPago )

  Modificacion registrarCambiodeEmpleado(Modificacion modificacion, String idEmpleadoAnterior, String idEmpleadoFinal)

  CausaCancelacion causaCancelacion( Integer id )

  String cancelaVoucherTpv( Integer idPago, String idEmpleado )*/
}
