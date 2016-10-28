package mx.wen.pos.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QTipoProd is a Querydsl query type for TipoProd
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QTipoProd extends EntityPathBase<TipoProd> {

    private static final long serialVersionUID = -987426373;

    public static final QTipoProd tipoProd = new QTipoProd("tipoProd");

    public final StringPath descripcion = createString("descripcion");

    public final StringPath id = createString("id");

    public QTipoProd(String variable) {
        super(TipoProd.class, forVariable(variable));
    }

    public QTipoProd(Path<? extends TipoProd> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public QTipoProd(PathMetadata<?> metadata) {
        super(TipoProd.class, metadata);
    }

}

