package mx.wen.pos.ui.model

import groovy.beans.Bindable
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import mx.wen.pos.model.Sucursal
import mx.wen.pos.model.TipoProd
import org.apache.commons.lang.StringUtils

@Bindable
@ToString
@EqualsAndHashCode
class TypeProd {
  String id
  String desc


  static TypeProd toTypeProd(TipoProd tipoProd ) {
    if (StringUtils.trimToEmpty(tipoProd?.id).length() > 0 ) {
      TypeProd typeProd = new TypeProd(
          id: tipoProd.id,
          desc: tipoProd.descripcion
      )
      return typeProd
    }
    return null
  }

}
