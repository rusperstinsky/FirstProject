package mx.wen.pos.model;

import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Cacheable
@Table( name = "tipo_prod", schema = "public" )
public class TipoProd implements Serializable {

    private static final long serialVersionUID = 3995665184294475132L;

    @Id
    @Column( name = "id_tipo" )
    private String id;

    @Column( name = "descripcion" )
    private String descripcion;

    @PostLoad
    private void trim() {
        descripcion = StringUtils.trimToEmpty( descripcion );
    }

    public String getId() {
        return id;
    }

    public void setId( String id ) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion( String descripcion ) {
        this.descripcion = descripcion;
    }
}
