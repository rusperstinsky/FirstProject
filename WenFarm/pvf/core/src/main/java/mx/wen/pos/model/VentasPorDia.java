package mx.wen.pos.model;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class VentasPorDia {

    private String factura;
    private String estatus;
    private Date fecha;
    private BigDecimal montoTotal;
    private Double montoSinIva;

    public VentasPorDia( String factura,  Date fecha ) {
      this.factura = factura;
      this.fecha = fecha;
    }

    public void acumulaVentasPorDia( NotaVenta notaVenta, BigDecimal iva ){
      estatus = StringUtils.trimToEmpty(notaVenta.getsFactura()).equalsIgnoreCase("T") ? "CANCELADA" : "";
      montoTotal = notaVenta.getVentaNeta();
      montoSinIva = notaVenta.getVentaNeta().multiply(iva).doubleValue();
    }



    public String getFactura() {
        return factura;
    }

    public void setFactura(String factura) {
        this.factura = factura;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public BigDecimal getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(BigDecimal montoTotal) {
        this.montoTotal = montoTotal;
    }

    public Double getMontoSinIva() {
        return montoSinIva;
    }

    public void setMontoSinIva(Double montoSinIva) {
        this.montoSinIva = montoSinIva;
    }
}
