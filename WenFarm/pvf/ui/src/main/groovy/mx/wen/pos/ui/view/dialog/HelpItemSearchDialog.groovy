package mx.wen.pos.ui.view.dialog

import groovy.swing.SwingBuilder
import mx.wen.pos.ui.controller.ItemController
import mx.wen.pos.ui.model.TypeProd
import mx.wen.pos.ui.resources.UI_Standards
import net.miginfocom.swing.MigLayout
import org.apache.commons.lang.StringUtils

import javax.swing.*
import java.awt.*
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.image.BufferedImage
import java.awt.image.WritableRaster

class HelpItemSearchDialog extends JDialog {

  private def sb = new SwingBuilder()

  public boolean button = false
  private String generics
  private String type
  private JPanel mainPanel

  HelpItemSearchDialog( ) {
    BufferedImage image = new BufferedImage(60, 60, BufferedImage.TYPE_INT_ARGB);
    WritableRaster raster = image.getRaster();
    image.setData( raster )
    //fillGenericTable()
    buildUI()
    createButtons( )
  }

  // UI Layout Definition
  void buildUI( ) {
    sb.dialog( this,
        title: "Busqueda de Articulos",
        resizable: true,
        pack: true,
        modal: true,
        preferredSize: [ 600, 400 ],
        location: [ 200, 250 ],
    ) {
      panel( layout: new MigLayout( "wrap ", "[fill,grow]", "[fill,grow]" ) ) {
        borderLayout()
        mainPanel = panel( constraints: BorderLayout.CENTER, layout: new MigLayout( "wrap 3", "[fill,grow][fill,grow][fill,grow]", "[grow,fill]" ) ) {
          def displayFont = new Font('', Font.BOLD, 12)
          /*button( "Medicamento sin receta", minimumSize: [180,60], actionPerformed: { doSearch("1") } )
          button( "Medicamento con receta", minimumSize: [180,60], actionPerformed: { doSearch("2") } )
          button( "Prevencion", minimumSize: [180,60], actionPerformed: { doSearch("3") } )
          button( "Higiene", minimumSize: [180,60], actionPerformed: { doSearch("4") } )
          button( "Cosmeticos", minimumSize: [180,60], actionPerformed: { doSearch("5") } )
          button( "Alimentos", minimumSize: [180,60], actionPerformed: { doSearch("6") } )
          label( text: "Ejemplo", border: titledBorder(title: ''), font: displayFont, alignmentX: CENTER_ALIGNMENT )
          label( text: "Muestra articulos que coincidan con:", border: titledBorder(title: ''), font: displayFont, alignmentX: CENTER_ALIGNMENT )
            label( text: "RB*", border: titledBorder(title: '') )
            label( text: "Letras especificadas", border: titledBorder(title: '') )
            label( text: "RB*+A", border: titledBorder(title: ''), toolTipText: generics )
            label( text: "Letras especificadas+genericos.", border: titledBorder(title: ''), toolTipText: generics )
            label( text: "D+BIOMED", border: titledBorder(title: '') )
            label( text: "D+Descripcion", border: titledBorder(title: '') )
            label( text: "+Q", border: titledBorder(title: ''), toolTipText: generics )
            label( text: "Generico", border: titledBorder(title: ''), toolTipText: generics )*/
        }
        panel( constraints: BorderLayout.PAGE_END ) {
          borderLayout()
          panel( constraints: BorderLayout.LINE_END ) {
            button( text: "Cerrar", preferredSize: UI_Standards.BUTTON_SIZE,
                actionPerformed: { onButtonOk() }
            )
          }
        }
      }

    }
  }

  // UI Management
  protected void fillGenericTable(){
    generics = '<html><table border="1"> <tr><td>Letra</td><td>Generico</td></tr> <tr><td align="center">A</td><td>Armazones</td></tr> ' +
            '<tr><td align="center">B</td><td>Lentes</td></tr> <tr><td align="center">C</td><td>Lentes de Contacto</td></tr>' +
            '<tr><td align="center">E</td><td>Accesorios</td></tr> <tr><td align="center">F</td><td>Lentes HD</td></tr>' +
            '<tr><td align="center">G</td><td>Tratamientos</td></tr> <tr><td align="center">J</td><td>Seguros</td></tr>' +
            '<tr><td align="center">L</td><td>Especiales</td></tr> <tr><td align="center">M</td><td>Materiales</td></tr>' +
            '<tr><td align="center">Q</td><td>Paquetes</td></tr> <tr><td align="center">S</td><td>Servicios</td></tr>' +
            '<tr><td align="center">T</td><td>Color</td></tr> <html>'
  }


  private void createButtons( ){
    java.util.List<TypeProd> lstTypes = ItemController.findTypesOfItem( false )
    for(TypeProd type : lstTypes){
      JButton button = new JButton()
      String[] data = StringUtils.trimToEmpty(type.desc).split( " " )
      String tipo = ""
      for(String d : data){
        tipo =StringUtils.trimToEmpty(tipo).length() > 0 ? tipo+"<br>"+d : d
      }
      button.text = StringUtils.trimToEmpty("<html><center>${tipo}<html>")
      button.maximumSize = [160,65]
      button.minimumSize = [160,65]
      button.setToolTipText(StringUtils.trimToEmpty(type.id))
      button.addActionListener( new ActionListener() {
        @Override
        void actionPerformed(ActionEvent e) {
          doSearch(button.toolTipText)
        }
      })
      mainPanel.add( button )
    }
  }
  // Public Methods

  // UI Response
  protected void onButtonOk( ) {
    dispose()
  }


  private void doSearch( String type ) {
    this.type = StringUtils.trimToEmpty(type)
    dispose()
  }

  public String getTipo( ){
    return type
  }
}
