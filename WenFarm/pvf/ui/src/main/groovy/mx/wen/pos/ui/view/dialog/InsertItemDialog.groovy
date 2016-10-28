package mx.wen.pos.ui.view.dialog

import groovy.swing.SwingBuilder
import mx.wen.pos.ui.resources.UI_Standards
import net.miginfocom.swing.MigLayout

import javax.swing.*
import java.awt.*
import java.text.NumberFormat

class InsertItemDialog extends JDialog {

  private def sb = new SwingBuilder()


  private JTextField txtSku
  private JTextField txtArt
  private JTextField txtDesc
  private JTextField txtPrice

    InsertItemDialog( ) {
    buildUI()
  }

  void buildUI( ) {
    sb.dialog( this,
        title: "Alta de Articulo",
        resizable: true,
        pack: true,
        modal: true,
        preferredSize: [ 350, 250 ],
        location: [ 300, 350 ],
    ) {
      panel() {
        borderLayout()
        panel( layout: new MigLayout( "fill,wrap 2", "[fill][fill,grow]" ) ) {
            NumberFormat formatter = NumberFormat.getCurrencyInstance( Locale.US )
            label( text: 'Sku:' )
            txtSku = textField( )
            label( text: 'Articulo: ')
            txtArt = textField( )
            label( text: 'Descripcion:' )
            txtDesc = textField( )
            label( text: 'Precio:' )
            txtPrice = textField( )
        }
      }

      panel( constraints: BorderLayout.PAGE_END ) {
        borderLayout()
        panel( constraints: BorderLayout.LINE_END ) {
          button( text: "Cancelar", preferredSize: UI_Standards.BUTTON_SIZE, actionPerformed: {dispose()} )
          button( text: "Aceptar", preferredSize: UI_Standards.BUTTON_SIZE, actionPerformed: {dispose()} )
        }
      }

    }
  }
}
