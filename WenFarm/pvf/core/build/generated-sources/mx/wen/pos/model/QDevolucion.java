package mx.wen.pos.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QDevolucion is a Querydsl query type for Devolucion
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QDevolucion extends EntityPathBase<Devolucion> {

    private static final long serialVersionUID = 431125080;

    public static final QDevolucion devolucion = new QDevolucion("devolucion");

    public final StringPath causaDev = createString("causaDev");

    public final DateTimePath<java.util.Date> fechaDev = createDateTime("fechaDev", java.util.Date.class);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath idFactura = createString("idFactura");

    public QDevolucion(String variable) {
        super(Devolucion.class, forVariable(variable));
    }

    public QDevolucion(Path<? extends Devolucion> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public QDevolucion(PathMetadata<?> metadata) {
        super(Devolucion.class, metadata);
    }

}

