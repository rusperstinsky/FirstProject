package mx.wen.pos.service.business;

import mx.wen.pos.model.*;
import mx.wen.pos.repository.*;
import mx.wen.pos.service.impl.ReportServiceImpl;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.List;

@Component
public class ReportBusiness {

    private static Logger log = LoggerFactory.getLogger( ReportServiceImpl.class );

    @Resource
    private SucursalRepository sucursalRepository;

    @Resource
    private NotaVentaRepository notaVentaRepository;

    private static final String TAG_CANCELADO = "T";

    public String CompilayGeneraReporte( org.springframework.core.io.Resource template, Map<String, Object> parametros,
                                         File report, Boolean ticket ) {

        try {
            JasperReport jasperReport = JasperCompileManager.compileReport( template.getInputStream() );
            JasperPrint jasperPrint = JasperFillManager.fillReport( jasperReport, parametros, new JREmptyDataSource() );
            JasperExportManager.exportReportToPdfFile( jasperPrint, report.getPath() );
            Desktop.getDesktop().open( report );
            log.info( "Mostrar Reporte" );
            return report.getPath();
        } catch ( JRException e ) {
            log.error( "error al compilar y generar reporte", e );
        } catch ( IOException e ) {
            log.error( "error al compilar y generar reporte", e );
        }
        return report.getPath();
    }


    public List<VentasPorDia> obtieneVentasPorDias( Date fechaInicio, Date fechaFin, BigDecimal ivaTasa ){
      List<VentasPorDia> lstVentas = new ArrayList<VentasPorDia>();

      List<NotaVenta> notas = notaVentaRepository.findByFechaHoraFacturaBetween( fechaInicio, fechaFin );
      for(NotaVenta nota : notas){
        VentasPorDia venta = new VentasPorDia( StringUtils.trimToEmpty(nota.getFactura()), nota.getFechaHoraFactura());
        venta.acumulaVentasPorDia( nota, ivaTasa );
        lstVentas.add( venta );
      }
      return lstVentas;
    }

    public List<DetalleVenta> obtieneDetalleVenta( NotaVenta nota ){
      List<DetalleVenta> lstDet = new ArrayList<DetalleVenta>();
      for(DetalleNotaVenta det : nota.getDetalles()){
        DetalleVenta detalle = new DetalleVenta(StringUtils.trimToEmpty(det.getArticulo().getArticulo()),
                det.getCantidadFac().intValue(),det.getPrecioUnitFinal().multiply(new BigDecimal(det.getCantidadFac())));
        lstDet.add(detalle);
      }
      return lstDet;
    }

}
