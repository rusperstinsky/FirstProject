package mx.wen.pos.model;

import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.util.Date;

public class DetalleVenta {

    private String desc;
    private Integer cant;
    private BigDecimal amount;

    public DetalleVenta(String desc, Integer cant, BigDecimal amount ) {
      this.desc = desc;
      this.cant = cant;
      this.amount = amount;
    }



    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Integer getCant() {
        return cant;
    }

    public void setCant(Integer cant) {
        this.cant = cant;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
