package mx.wen.pos.ui.view.dialog

import groovy.swing.SwingBuilder
import mx.wen.pos.ui.controller.ItemController
import mx.wen.pos.ui.model.Item
import mx.wen.pos.ui.model.TypeProd
import mx.wen.pos.ui.model.UpperCaseDocument
import mx.wen.pos.ui.resources.UI_Standards
import net.miginfocom.swing.MigLayout
import org.apache.commons.lang.StringUtils

import javax.swing.*
import java.awt.*
import java.awt.event.FocusEvent
import java.awt.event.FocusListener
import java.text.NumberFormat
import java.text.ParseException

class DeleteItemDialog extends JDialog {

  private def sb = new SwingBuilder()


  private JTextField txtSku
  private JTextField txtArt
  private JTextField txtDesc
  private JTextField txtPrice
  private JTextField txtQty
  private JComboBox cbCategoria
  private JLabel lblWarning
  private java.util.List<TypeProd> lstTypes

  DeleteItemDialog( ) {
    buildUI()
  }

  void buildUI( ) {
    sb.dialog( this,
        title: "Alta de Articulo",
        resizable: true,
        pack: true,
        modal: true,
        preferredSize: [ 450, 350 ],
        location: [ 300, 300 ],
    ) {
      panel() {
        borderLayout()
        panel( layout: new MigLayout( "fill,wrap 2", "[fill][fill,grow]" ) ) {
            NumberFormat formatter = NumberFormat.getCurrencyInstance( Locale.US )
            label( text: 'Sku:' )
            txtSku = textField( maximumSize: [100,30] )
            txtSku.addFocusListener( new FocusListener() {
                @Override
                void focusGained(FocusEvent e) {

                }

                @Override
                void focusLost(FocusEvent e) {
                  try{
                    Item item = ItemController.findItem( NumberFormat.getInstance().parse(txtSku.text).intValue() )
                    if( item != null ){
                      txtArt.text = StringUtils.trimToEmpty(item.name)
                      txtArt.enabled = false
                      txtDesc.text = StringUtils.trimToEmpty(item.desc)
                      txtDesc.enabled = false
                      txtPrice.text = StringUtils.trimToEmpty(item.price.toString())
                      txtPrice.enabled = false
                      txtQty.text = StringUtils.trimToEmpty(item.stock.toString())
                      txtQty.enabled = false
                    }
                  } catch ( ParseException ex ){
                    println ex.message
                  }
                }
            })
            label( text: 'Articulo: ')
            txtArt = textField( maximumSize: [300,30], document: new UpperCaseDocument() )
            label( text: 'Descripcion:' )
            txtDesc = textField( document: new UpperCaseDocument() )
            label( text: 'Precio:' )
            txtPrice = textField( maximumSize: [100,30] )
            label( text: 'Cantidad:' )
            txtQty = textField( maximumSize: [50,30] )
            label( text: 'Categoria:' )
            lblWarning = label( "No existe articulo", visible: false, foreground: UI_Standards.WARNING_FOREGROUND )
        }
      }

      panel( constraints: BorderLayout.PAGE_END ) {
        borderLayout()
        panel( constraints: BorderLayout.LINE_END ) {
          button( text: "Cancelar", preferredSize: UI_Standards.BUTTON_SIZE, actionPerformed: {dispose()} )
          button( text: "Aceptar", preferredSize: UI_Standards.BUTTON_SIZE, actionPerformed: {saveItem()} )
        }
      }

    }
  }

  private void saveItem( ){
    if( validData() ){
      
    } else {
      lblWarning.visible = true
    }
  }


  private Boolean validData( ){
    Boolean valid = true
    if(StringUtils.trimToEmpty(txtSku.text).length() <= 0 ){
      valid = false
    } else if(StringUtils.trimToEmpty(txtArt.text).length() <= 0 ){
      valid = false
    } else if(StringUtils.trimToEmpty(txtQty.text).length() <= 0 ){
      valid = false
    } else if(StringUtils.trimToEmpty(txtPrice.text).length() <= 0 ){
      valid = false
    } else if(StringUtils.trimToEmpty(txtDesc.text).length() <= 0 ){
      valid = false
    } else if(StringUtils.trimToEmpty(cbCategoria.selectedItem as String).length() <= 0 ){
        valid = false
    }
    return valid
  }



}
