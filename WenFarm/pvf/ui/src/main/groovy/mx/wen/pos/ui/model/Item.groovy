package mx.wen.pos.ui.model

import groovy.beans.Bindable
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.util.logging.Slf4j
import mx.wen.pos.model.Articulo


@Slf4j
@Bindable
@ToString
@EqualsAndHashCode
class Item {
  Integer id
  BigDecimal price
  BigDecimal listPrice
  String name
  String color
  String colorDesc
  String reference
  String type
  String brand
  String typ
  String typeArticle
  String subtype
  Integer stock
  String surte

  static String manualPriceTypeList

  String getDescription( ) {
    "${reference ? "${reference} " : ''}${color ? "[${color}] " : ''}${colorDesc ?: ''}"
  }

  String getDescriptionColor( ){
    "[${color? "${color} " : ''}]${reference? "${reference} " : colorDesc}"
  }

  static Item toItem( Articulo articulo ) {
    if ( articulo?.id ) {
      Item item = new Item(
          id: articulo.id,
          price: articulo.precio,
          listPrice: articulo.precio,
          name: articulo.articulo,
          reference: articulo.descripcion,
          brand: articulo.marca,
          typ: articulo.tipo,
          typeArticle: articulo.tipo,
          subtype: articulo.subtipo,
          stock: articulo.cantExistencia,
      )
      return item
    }
    return null
  }
}
