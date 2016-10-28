package mx.wen.pos.service.impl;

import mx.wen.pos.model.*;
import mx.wen.pos.repository.*;
import mx.wen.pos.service.ReportService;
import mx.wen.pos.service.business.Registry;
import mx.wen.pos.service.business.ReportBusiness;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.File;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service( "reportService" )
@Transactional( readOnly = true )
public class ReportServiceImpl implements ReportService {

    private static Logger log = LoggerFactory.getLogger( ReportServiceImpl.class );
    private static String TICKET_VENTA = "reports/ticket-venta.jrxml";
    private static String INGRESOS_POR_SUCURSAL = "reports/Ingresos_Sucursal.jrxml";
    private static String INGRESOS_POR_VENDEDOR_RESUMIDO = "reports/Ingresos_Vendedor_Resumido.jrxml";
    private static String INGRESOS_POR_VENDEDOR_COMPLETO = "reports/Ingresos_Vendedor_Completo.jrxml";
    private static String VENTAS = "reports/Ventas.jrxml";
    private static String VENTAS_COMPLETO = "reports/Ventas_Completo.jrxml";
    private static String VENTAS_POR_VENDEDOR_COMPLETO = "reports/Venta_Por_Vendedor_Completo.jrxml";
    private static String VENTAS_POR_VENDEDOR_RESUMIDO = "reports/Venta_Por_Vendedor_Resumido.jrxml";
    private static String TRABAJOS_SIN_ENTREGAR = "reports/Trabajos_Sin_Entregar.jrxml";
    private static String CANCELACIONES_RESUMIDO = "reports/Cancelaciones.jrxml";
    private static String CANCELACIONES_COMPLETO = "reports/Cancelaciones_Completo.jrxml";
    private static String VENTA_POR_LINEA_FACTURA = "reports/Venta_Por_Linea.jrxml";
    private static String VENTA_POR_LINEA_ARTICULO = "reports/Venta_Por_Linea_Articulo.jrxml";
    private static String VENTA_POR_MARCA = "reports/Ventas_Por_Marca.jrxml";
    private static String VENTA_POR_VENDEDOR_MARCA = "reports/Ventas_Por_Vendedor_Por_Marca.jrxml";
    private static String EXISTENCIAS_POR_MARCA = "reports/Existencias_Por_Marca.jrxml";
    private static String EXISTENCIAS_POR_MARCA_RESUMIDO = "reports/Existencias_Por_Marca_Resumido.jrxml";
    private static String EXISTENCIAS_POR_ARTICULO = "reports/Existencias_Por_Articulo.jrxml";
    private static String CONTROL_DE_TRABAJOS = "reports/Control_de_Trabajos.jrxml";
    private static String TRABAJOS_ENTREGADOS = "reports/Trabajos_Entregados.jrxml";
    private static String TRABAJOS_ENTREGADOS_POR_EMPLEADO = "reports/Trabajos_Entregados_Por_Empleado.jrxml";
    private static String FACTURAS_FISCALES = "reports/Facturas_Fiscales.jrxml";
    private static String DESCUENTOS = "reports/Descuentos.jrxml";
    private static String PROMOCIONES_APLICADAS = "reports/Promociones.jrxml";
    private static String PAGOS = "reports/Pagos.jrxml";
    private static String COTIZACIONES = "reports/Cotizaciones.jrxml";
    private static String EXAMENES_RESUMIDO = "reports/Examenes.jrxml";
    private static String EXAMENES_COMPLETO = "reports/Examenes_Completo.jrxml";
    private static String VENTAS_POR_OPTOMETRISTA_COMPLETO = "reports/Ventas_Por_Optometrista_Completo.jrxml";
    private static String VENTAS_POR_OPTOMETRISTA_RESUMIDO = "reports/Ventas_Por_Optometrista_Resumido.jrxml";
    private static String PROMOCIONES = "reports/Lista_de_Promociones.jrxml";
    private static String KARDEX = "reports/Kardex.jrxml";
    private static String VENTAS_DEL_DIA = "reports/Ventas_Del_Dia.jrxml";
    private static String INGRESOS_POR_PERIODO = "reports/Ingresos_Por_Periodo.jrxml";


    @Resource
    private ReportBusiness reportBusiness;

    @Resource
    private ArticuloRepository articuloRepository;

    @Resource
    private ParametroRepository parametroRepository;

    @Resource
    private NotaVentaRepository notaVentaRepository;

    @Resource
    private SucursalRepository sucursalRepository;


    @Override
    public String obtenerTicketVenta( String idOrder ) throws ParseException {
      log.info( "obtenerTicketVenta" );
      NotaVenta nota = notaVentaRepository.findOne(StringUtils.trimToEmpty(idOrder));
      if ( nota != null ) {
        File report = new File( System.getProperty( "java.io.tmpdir" ), "Ticket-Venta.pdf" );
        org.springframework.core.io.Resource template = new ClassPathResource( TICKET_VENTA );
        log.info( "Ruta:{}", report.getAbsolutePath() );

        //Date feVchaInicio = DateUtils.truncate( fecha, Calendar.DAY_OF_MONTH );
        //Date fechaFin = new Date( DateUtils.ceiling( fecha, Calendar.DAY_OF_MONTH ).getTime() - 1 );
        Sucursal suc = sucursalRepository.findOne(Registry.getCurrentSite());
        Parametro ivaVigenteParam = parametroRepository.findOne( TipoParametro.IVA_VIGENTE.getValue() );
        String strValorIva = ivaVigenteParam != null ? ivaVigenteParam.getValor() : "";
        Double iva = NumberFormat.getInstance().parse(StringUtils.trimToEmpty(strValorIva)).doubleValue();
        BigDecimal valorIva = nota.getVentaNeta().multiply(new BigDecimal(iva/100));
        List<DetalleVenta> lstDet = reportBusiness.obtieneDetalleVenta(nota);

        Map<String, Object> parametros = new HashMap<String, Object>();
        parametros.put( "fecha", new SimpleDateFormat( "dd/MM/yyyy" ).format( nota.getFechaHoraFactura() ) );
        parametros.put( "hora", new SimpleDateFormat( "hh:mm" ).format( nota.getFechaHoraFactura() ) );
        parametros.put( "folio", StringUtils.trimToEmpty(nota.getId()) );
        parametros.put( "ticket", StringUtils.trimToEmpty(nota.getFactura()) );
        parametros.put( "vendedor", StringUtils.trimToEmpty(nota.getEmpleado().getNombreCompleto()) );
        parametros.put( "detalle", lstDet );
        parametros.put( "total", NumberFormat.getCurrencyInstance(Locale.US).format(nota.getVentaNeta()) );
        parametros.put( "totalSinIva", NumberFormat.getCurrencyInstance(Locale.US).format(nota.getVentaNeta().subtract(valorIva)) );
        parametros.put( "iva", NumberFormat.getCurrencyInstance(Locale.US).format(valorIva) );

        String reporte = reportBusiness.CompilayGeneraReporte( template, parametros, report, true );
        log.info( "reporte:{}", reporte );

      }
      return null;
    }



    public String obtenerReporteVentasCompleto( Date fechaInicio, Date fechaFin ) {
        log.info( "obtenerReporteVentasPorDia()" );

        File report = new File( System.getProperty( "java.io.tmpdir" ), "Ventas-Completo.pdf" );
        org.springframework.core.io.Resource template = new ClassPathResource( VENTAS_COMPLETO );
        log.info( "Ruta:{}", report.getAbsolutePath() );

        fechaInicio = DateUtils.truncate( fechaInicio, Calendar.DAY_OF_MONTH );
        fechaFin = new Date( DateUtils.ceiling( fechaFin, Calendar.DAY_OF_MONTH ).getTime() - 1 );

        Double iva = Registry.getCurrentIVA();
        BigDecimal ivaTasa = new BigDecimal( iva ).divide( new BigDecimal( 100 ) );

        List<VentasPorDia> lstVentas = reportBusiness.obtieneVentasPorDias( fechaInicio, fechaFin, ivaTasa );

        Integer totalFacturas = 0;
        BigDecimal totalVentas = BigDecimal.ZERO;
        BigDecimal totalVentasSinIva = BigDecimal.ZERO;
        BigDecimal totalVentasCanc = BigDecimal.ZERO;
        BigDecimal totalVentasCancSinIva = BigDecimal.ZERO;
        BigDecimal totalNotasCredito = BigDecimal.ZERO;
        BigDecimal totalNotasCreditoSinIva = BigDecimal.ZERO;
        BigDecimal total = BigDecimal.ZERO;
        BigDecimal totalSinIva = BigDecimal.ZERO;

        total = totalVentas.subtract(totalVentasCanc.abs().add(totalNotasCredito.abs()));
        totalSinIva = totalVentasSinIva.subtract(totalVentasCancSinIva.abs().add(totalNotasCreditoSinIva.abs()));

        Map<String, Object> parametros = new HashMap<String, Object>();
        parametros.put( "fechaActual", new SimpleDateFormat( "hh:mm" ).format( new Date() ) );
        parametros.put( "fechaInicio", new SimpleDateFormat( "dd/MM/yyyy" ).format( fechaInicio ) );
        parametros.put( "fechaFin", new SimpleDateFormat( "dd/MM/yyyy" ).format( fechaFin ) );
        parametros.put( "lstVentas", lstVentas );
        parametros.put( "ivaTasa", ivaTasa );
        parametros.put( "totalFacturas", totalFacturas );
        parametros.put( "totalVentas", total );
        parametros.put( "totalVentasSinIva", totalSinIva );
        String reporte = reportBusiness.CompilayGeneraReporte( template, parametros, report, true );
        log.info( "reporte:{}", reporte );

        return null;
    }


}
