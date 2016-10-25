package mx.wen.pos.service.impl

import com.mysema.query.BooleanBuilder
import com.mysema.query.types.OrderSpecifier
import com.mysema.query.types.Predicate
import groovy.util.logging.Slf4j
import mx.wen.pos.service.CancelacionService
import mx.wen.pos.service.NotaVentaService
import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.time.DateUtils
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import javax.annotation.Resource

import mx.wen.pos.model.*
import mx.wen.pos.repository.*
import mx.wen.pos.service.business.Registry
import java.text.NumberFormat

@Slf4j
@Service('cancelacionService')
@Transactional(readOnly = true)
class CancelacionServiceImpl implements CancelacionService {

    /*@Resource
    private CausaCancelacionRepository causaCancelacionRepository*/

    @Resource
    private NotaVentaRepository notaVentaRepository

    @Resource
    private NotaVentaService notaVentaService

    @Resource
    private ParametroRepository parametroRepository

    /*@Resource
    private ModificacionRepository modificacionRepository

    @Resource
    private LogTpvRepository logTpvRepository

    @Resource
    private ModificacionEmpRepository modificacionEmpRepository

    @Resource
    private ModificacionCanRepository modificacionCanRepository

    @Resource
    private PagoRepository pagoRepository

    */
    @Resource
    private DevolucionRepository devolucionRepository

    private String TAG_TC = "TC"
    private String TAG_TD = "TD"
    private String TAG_UV = "UV"


    @Override
    @Transactional
    Devolucion registrarDevolucionesDeNotaVenta(String idNotaVenta, String causaDev) {
      log.info("registrando devolucion de notaVenta id: ${idNotaVenta}")
      if ( StringUtils.isNotBlank(idNotaVenta) ) {
        Devolucion devolucion = null
        try {
          devolucion = new Devolucion(
            idFactura: idNotaVenta,
            causaDev: StringUtils.trimToEmpty(causaDev),
            fechaDev: new Date()
          )
          devolucion = devolucionRepository.saveAndFlush(devolucion)
          //devolucionRepository.flush()
          log.debug("devolucion registrada: ${devolucion.id}")
          return devolucion
        } catch (ex) {
          log.warn("no se registran devoluciones, ${ex.message}")
        }
      } else {
        log.warn('no se registran devoluciones, parametros invalidos')
      }
      return []
    }


    @Override
    @Transactional
    Boolean registrarCancelacionDeNotaVenta(String idNotaVenta) {
      log.info("registrando transaccion de cancelacion para notaVenta id: ${idNotaVenta}")
      if (StringUtils.isNotBlank(idNotaVenta)) {
        NotaVenta notaVenta = notaVentaService.obtenerNotaVenta(idNotaVenta)
        if (ServiceFactory.inventory.solicitarTransaccionDevolucion(notaVenta)) {
          notaVenta.setsFactura('T')
          notaVentaRepository.saveAndFlush( notaVenta )
          log.warn("no se registra el movimiento, error al registrar devolucion")
          return true
        }
      } else {
        log.warn('no se registra cancelacion, parametros invalidos')
      }
      return false
    }


    /*@Override
    List<CausaCancelacion> listarCausasCancelacion() {
        log.info("listando causas de cancelacion")
        List<CausaCancelacion> causas = causaCancelacionRepository.findByDescripcionNotNullOrderByDescripcionAsc()
        log.debug("obtiene causas: ${causas*.id}")
        return causas?.any() ? causas : []
    }

    @Override
    boolean permitirCancelacionExtemporanea(String idNotaVenta) {
        log.info("determinando autorizacion para cancelacion extemporanea de notaVenta id: ${idNotaVenta}")
        if (Registry.isCancellationLimitedToSameDay()) {
            log.info('requiere cancelar dia de venta')
            NotaVenta notaVenta = notaVentaService.obtenerNotaVenta(idNotaVenta)
            if (notaVenta?.fechaHoraFactura && DateUtils.isSameDay(new Date(), notaVenta.fechaHoraFactura)) {
                return true
            } else {
                log.debug('fechaHoraFactura es distinta al dia actual')
            }
            return false
        } else {
            log.debug('parametro cancelacion mismo dia inactivo')
        }
        return true
    }

    @Override
    List<Devolucion> listarDevolucionesDeNotaVenta(String idFactura) {
        log.info("listando devoluciones por idFactura ${idFactura}")
        if (StringUtils.isNotBlank(idFactura)) {
            List<Modificacion> modificaciones = modificacionRepository.findByIdFacturaOrderByFechaAsc(idFactura)
            log.debug("obtiene modificaciones: ${modificaciones*.id}")
            if (modificaciones?.any()) {
                List<Devolucion> devoluciones = devolucionRepository.findByIdModInOrderByFechaAsc(modificaciones*.id)
                log.debug("obtiene devoluciones: ${devoluciones*.id}")
                return devoluciones?.any() ? devoluciones : []
            } else {
                log.warn('no se listan devoluciones, no existen modificaciones')
            }
        } else {
            log.warn('no se listan devoluciones, parametros invalidos')
        }
        return []
    }

    @Override
    @Transactional
    List<Pago> registrarTransferenciasParaNotaVenta(String idOrigen, String idDestino, Map<Integer, BigDecimal> transferenciasPagos) {
        log.info("registrando transferencias: ${transferenciasPagos} de notaVenta origen: ${idOrigen}, destino: ${idDestino}")
        boolean tieneElementos = transferenciasPagos?.any() && transferenciasPagos?.keySet()?.any()
        if (StringUtils.isNotBlank(idOrigen) && StringUtils.isNotBlank(idDestino) && tieneElementos) {
            NotaVenta destino = notaVentaService.obtenerNotaVenta(idDestino)
            List<Modificacion> mods = modificacionRepository.findByIdFacturaAndTipo(idOrigen, 'can')
            log.debug("modificaciones: ${mods*.id}")
            Modificacion modificacion = mods?.any() ? mods.first() : null
            log.debug("obtiene modificacion: ${modificacion?.id}")
            if (StringUtils.isNotBlank(destino?.id) && modificacion?.id) {
                List<Pago> pagos = []
                List<Pago> transferencias = []
                List<Devolucion> devoluciones = []
                Date fechaActual = new Date()
                try {
                    transferenciasPagos.each { Integer pagoId, BigDecimal valor ->
                        Pago pago = pagoRepository.findOne(pagoId)
                        log.debug("obtiene pago: ${pago?.id}")
                        if (pago?.id && valor) {
                            pago.porDevolver -= valor
                            Pago transferencia = new Pago(
                                    idFactura: idDestino,
                                    idFormaPago: pago.idFPago,
                                    referenciaPago: idOrigen,
                                    monto: valor,
                                    idEmpleado: destino.idEmpleado,
                                    idSucursal: pago.idSucursal,
                                    idFPago: 'TR',
                                    clave: pago.idFPago,
                                    referenciaClave: "${idOrigen}:${pagoId}",
                                    idSync: '2',
                                    tipoPago: DateUtils.isSameDay(destino.fechaHoraFactura ?: fechaActual, fechaActual) ? 'a' : 'l',
                                    idBancoEmisor: pago.idBancoEmisor,
                                    idTerminal: pago.idTerminal,
                                    idPlan: pago.idPlan
                            )
                            log.debug("genera transferencia: ${transferencia.dump()}")
                            Devolucion devolucion = new Devolucion(
                                    idMod: modificacion.id,
                                    idPago: pagoId,
                                    idFormaPago: pago.idFPago,
                                    idBanco: pago.idBancoEmisor?.isInteger() ? pago.idBancoEmisor.toInteger() : null,
                                    monto: valor,
                                    tipo: 't',
                                    transf: idDestino
                            )
                            log.debug("genera devolucion: ${devolucion.dump()}")
                            pagos.add(pago)
                            transferencias.add(transferencia)
                            devoluciones.add(devolucion)
                        } else {
                            throw new Exception("no se encuentra el pago con id: ${pagoId} o el monto es invalido: ${valor}")
                        }
                    }
                    pagoRepository.save(pagos)
                    transferencias = pagoRepository.save(transferencias)
                    log.debug("transferencias de pago registradas: ${transferencias*.id}")
                    devoluciones.each { Devolucion dev ->
                        Pago pago = transferencias.find { Pago tmp ->
                            tmp?.referenciaClave?.equalsIgnoreCase("${idOrigen}:${dev?.idPago}")
                        }
                        if (pago?.id) {
                            dev.referencia = pago.id
                        } else {
                            throw new Exception("no se encuentra transferencia con pago origen id: ${dev.idPago}")
                        }
                    }
                    devoluciones = devolucionRepository.save(devoluciones)
                    log.debug("devoluciones registradas: ${devoluciones*.id}")
                    notaVentaService.registrarNotaVenta(destino)
                    return transferencias
                } catch (ex) {
                    log.warn("no se registran transferencias, ${ex.message}")
                }
            } else {
                log.warn('no se registran transferencias, notaVenta origen y/o destino invalidas, y/o sin cancelacion')
            }
        } else {
            log.warn('no se registran transferencias, parametros invalidos')
        }
        return []
    }

    @Override
    List<NotaVenta> listarNotasVentaOrigenDeNotaVenta(String idNotaVenta) {
        log.info("obteniendo notaVenta origen de notaVenta id: ${idNotaVenta}")
        if (StringUtils.isNotBlank(idNotaVenta)) {
            BooleanBuilder builder = new BooleanBuilder(QPago.pago.idFPago.eq('TR'))
            builder.and(QPago.pago.idFactura.eq(idNotaVenta))
            List<Pago> transferencias = pagoRepository.findAll(builder) as List<Pago>
            log.debug("obtiene pagos tipo transferencia: ${transferencias*.id}")
            if (transferencias?.any()) {
                Predicate predicate = QNotaVenta.notaVenta.id.in(transferencias*.referenciaPago)
                OrderSpecifier order = QNotaVenta.notaVenta.fechaHoraFactura.asc()
                List<NotaVenta> notas = notaVentaRepository.findAll(predicate, order) as List<NotaVenta>
                log.debug("obtiene notas origen: ${notas*.id}")
                return notas?.any() ? notas : []
            } else {
                log.warn('no se obtiene notasVenta origen, notaVenta no ha recibido transferencias')
            }
        } else {
            log.warn('no se obtienen notasVenta origen, parametros invalidos')
        }
        return []
    }

    @Override
    BigDecimal obtenerCreditoDeNotaVenta(String idNotaVenta) {
        log.info("obteniendo credito de notaVenta id: ${idNotaVenta}")
        if (StringUtils.isNotBlank(idNotaVenta)) {
            List<Modificacion> mods = modificacionRepository.findByIdFacturaAndTipo(idNotaVenta, 'can')
            log.debug("modificaciones: ${mods*.id}")
            Modificacion modificacion = mods?.any() ? mods.first() : null
            log.debug("obtiene modificacion: ${modificacion?.id}")
            if (modificacion?.id) {
                BigDecimal porDevolver = BigDecimal.ZERO
                List<Pago> pagos = pagoRepository.findByIdFactura(idNotaVenta) ?: []
                pagos?.each { Pago pmt ->
                    porDevolver += pmt?.porDevolver ?: BigDecimal.ZERO
                }
                log.debug("obtiene credito: ${porDevolver}")
                return porDevolver
            } else {
                log.warn('no se obtiene credito de notaVenta, notaVenta sin cancelacion')
            }
        } else {
            log.warn('no se obtiene credito de notaVenta, parametros invalidos')
        }
        return null
    }


    @Override
    Boolean validandoTransferencia(String idNotaVenta) {
        Boolean transfer = true
        List<Pago> lstPagos = pagoRepository.findByReferenciaPago(idNotaVenta)
        List<Pago> lstPagosTransf = pagoRepository.findByIdFactura(idNotaVenta)
        BigDecimal sumaPagos = BigDecimal.ZERO
        BigDecimal sumaPagosTransf = BigDecimal.ZERO
        for (Pago pagoTransf : lstPagosTransf) {
            sumaPagosTransf = sumaPagosTransf.add(pagoTransf.monto)
        }
        for (Pago pago : lstPagos) {
            sumaPagos = sumaPagos.add(pago.monto)
        }
        if ( ( sumaPagos < sumaPagosTransf ) ) {
            transfer = false
        }
        return transfer
    }

    @Override
    @Transactional
    void restablecerValoresDeCancelacion(String idNotaVenta) {
        log.info("restableciendo valores de cancelacion")
      QPago payment = QPago.pago
        List<Pago> lstPagos = pagoRepository.findByReferenciaPago(idNotaVenta)
        List<Pago> lstPagosTransf = pagoRepository.findByIdFactura(idNotaVenta)
        BigDecimal sumaPagos = BigDecimal.ZERO
        BigDecimal sumaPagosTransf = BigDecimal.ZERO
        for (Pago pagoTransf : lstPagosTransf) {
            sumaPagosTransf = sumaPagosTransf.add(pagoTransf.monto)
        }
        for (Pago pago : lstPagos) {
            sumaPagos = sumaPagos.add(pago.monto)
        }
        for( Pago pagoTransf : lstPagos ){
          if( StringUtils.trimToEmpty( pagoTransf.notaVenta?.factura ).isEmpty() && !StringUtils.trimToEmpty(pagoTransf.referenciaClave).isEmpty() ){
            String[] idPagoTransf = pagoTransf.referenciaClave.split( ':' )
            Pago pagoFuente = pagoRepository.findOne( Integer.parseInt( idPagoTransf[1].trim() ) )
            QDevolucion dev = QDevolucion.devolucion
            Devolucion devolucion = devolucionRepository.findOne( dev.idPago.eq(pagoFuente.getId()).and(dev.monto.eq(pagoTransf.monto)).
                and(dev.transf.eq(pagoTransf.idFactura)))
            if( devolucion != null ){
              BigDecimal montoTotal = pagoTransf.monto.add( pagoFuente.porDevolver )
              devolucionRepository.delete( devolucion.id )
              pagoFuente.setPorDevolver( montoTotal )
              pagoRepository.save( pagoFuente )
              pagoRepository.flush()
            }
          }
        }
    }


    @Override
    @Transactional
    void restablecerMontoAlBorrarPago( Integer idPago ) {
        Pago pago = pagoRepository.findOne( idPago )
        if( pago != null && !StringUtils.trimToEmpty(pago.referenciaClave).isEmpty() ){
            String[] idPagoTransf = pago.referenciaClave.split( ':' )
          if( idPagoTransf.length > 1 ){
            Pago pagoDeTransf = pagoRepository.findOne( Integer.parseInt( idPagoTransf[1].trim() ) )
            BigDecimal porDevolver = pago.monto.add(pagoDeTransf.porDevolver)
            pagoDeTransf.setPorDevolver( porDevolver )
            QDevolucion dev = QDevolucion.devolucion
            Devolucion devolucion = devolucionRepository.findOne( dev.idPago.eq(pagoDeTransf.getId()).and(dev.monto.eq(pago.monto)).
                and(dev.transf.eq(pago.idFactura)))
            devolucionRepository.delete( devolucion.id )
            pagoRepository.save( pagoDeTransf )
            pagoRepository.flush()
          }
        }
    }

    @Override
    @Transactional
    Modificacion registrarCambiodeEmpleado(Modificacion modificacion, String idEmpAnterior, String idEmpleadoFinal) {
        log.info("registrando cambio de empleado")
        Modificacion mod = modificacionRepository.save( modificacion )
        ModificacionEmp modEmp = new ModificacionEmp()
        modEmp.id = mod.id
        modEmp.empleadoAnterior = idEmpAnterior
        modEmp.empleadoActual = idEmpleadoFinal
        modificacionEmpRepository.save( modEmp )
        return mod
    }


    @Override
    CausaCancelacion causaCancelacion( Integer id ) {
        log.info("cancelacion con id: ${id}")
        CausaCancelacion causa = causaCancelacionRepository.findOne( id )
        log.debug("obtiene causa: ${causa?.descripcion}")
        return causa != null ? causa : null
    }



  @Override
  String cancelaVoucherTpv( Integer idPago, String idEmpleado ){
    String transaccion = ""
    Pago pago = pagoRepository.findOne(idPago)
    if( pago != null && pago.idTerminal.contains("|") ){
      String host = Registry.hostTpv
      Integer puerto = Registry.portTpv
      Integer timeout = Registry.timeoutTpv
      String user = Registry.userTpv
      String pass = Registry.passTpv
      GPAYAPI ctx = new GPAYAPI();
      ctx.SetAttribute( "HOST", host );
      ctx.SetAttribute( "PORT", puerto )
      ctx.SetAttribute( "TIMEOUT", timeout );
      ctx.SetString( "trn_usr_id", user )
      ctx.SetString( "trn_password", pass )
      ctx.SetString( "dcs_reply_get", "localhost" )
      String[] data = pago.idTerminal.split(/\|/)
      String seg = ""
      if(data.length >= 5){
        seg = data[1]
      }
      if( StringUtils.trimToEmpty(pago.idFormaPago).equalsIgnoreCase(TAG_UV) ){
        ctx.SetInteger( "trn_cur_id1", 840 )

      } else {
        ctx.SetInteger( "trn_cur_id1", 484 )
      }
      Double montoDev = 0.00
      if( StringUtils.trimToEmpty(pago.idFormaPago).equalsIgnoreCase(TAG_UV) ){
        try{
          montoDev = NumberFormat.getInstance().parse(StringUtils.trimToEmpty(pago?.idPlan)).doubleValue()
        } catch ( NumberFormatException e ) { println e }
      } else {
        montoDev = pago.porDevolver.doubleValue() <= 0.00 ? pago.monto.doubleValue() : pago.porDevolver.doubleValue()
      }

      if( StringUtils.trimToEmpty(pago.fecha.format("dd/MM/yyyy")).equalsIgnoreCase(new Date().format("dd/MM/yyyy"))){
        ctx.SetString( "dcs_form", "T120S000" )
        ctx.SetString( "trn_orig_id", seg )
      } else {
        ctx.SetString( "dcs_form", "T040S000" )
        ctx.SetFloat( "trn_amount", montoDev )
        ctx.SetString( "trn_orig_id", seg )
        ctx.SetString("trn_auth_code", StringUtils.trimToEmpty(pago.referenciaClave))
      }
      println ctx.dump()
      Socket socket = ctx.TCP_Open();
      int execute = ctx.Execute()
      println "Respuesta de la ejecucion: "+execute
      if ( execute == 0 && StringUtils.trimToEmpty(ctx.GetString("trn_auth_code")).length() > 0 ){
        if(ctx.GetString("dcs_form").equalsIgnoreCase("T120S000")){
          transaccion = "CANCELACION"
        } else if(ctx.GetString("dcs_form").equalsIgnoreCase("T040S000")){
          transaccion = "DEVOLUCION"
        }
      }
      LogTpv logTpv = new LogTpv()
      Integer id = logTpvRepository.logTpvSequence
      logTpv.id = id != null ? id+1 : 1
      logTpv.idFactura = StringUtils.trimToEmpty(pago.idFactura)
      logTpv.fecha = new Date()
      logTpv.pagoSeleccionado = StringUtils.trimToEmpty(pago.idFPago)
      logTpv.pagoRecibido = StringUtils.trimToEmpty(pago.idFPago)
      logTpv.cadena = StringUtils.trimToEmpty(pago.idTerminal)
      logTpv.tarjeta = pago.clave
      logTpv.autorizacion = StringUtils.trimToEmpty(ctx.GetString("trn_auth_code")).length() > 0 ? StringUtils.trimToEmpty(ctx.GetString("trn_auth_code")).length() : ""
      logTpv.monto = pago.monto
      logTpv.empleado = idEmpleado
      logTpv.tipo = pago.fecha.format("dd/MM/yyyy").equalsIgnoreCase(new Date().format("dd/MM/yyyy")) ? "C" : "D"
      logTpv.plan = pago.idPlan
      try{
        logTpvRepository.saveAndFlush( logTpv )
      } catch ( Exception e ){ println e }
    }
    return transaccion
  }*/



}
