package mx.wen.pos.model;

import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Cacheable
@Table( name = "devolucion", schema = "public" )
public class Devolucion implements Serializable {

    private static final long serialVersionUID = -2908809749506678285L;

    @Id
    @GeneratedValue( strategy = GenerationType.AUTO, generator = "devolucion_id_seq" )
    @SequenceGenerator( name = "devolucion_id_seq", sequenceName = "devolucion_id_seq" )
    @Column( name = "id")
    private Integer id;

    @Column( name = "id_factura" )
    private String idFactura;

    @Column( name = "causa_dev" )
    private String causaDev;

    @Temporal( TemporalType.TIMESTAMP )
    @Column( name = "fecha_dev" )
    private Date fechaDev;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIdFactura() {
        return idFactura;
    }

    public void setIdFactura(String idFactura) {
        this.idFactura = idFactura;
    }

    public String getCausaDev() {
        return causaDev;
    }

    public void setCausaDev(String causaDev) {
        this.causaDev = causaDev;
    }

    public Date getFechaDev() {
        return fechaDev;
    }

    public void setFechaDev(Date fechaDev) {
        this.fechaDev = fechaDev;
    }
}
