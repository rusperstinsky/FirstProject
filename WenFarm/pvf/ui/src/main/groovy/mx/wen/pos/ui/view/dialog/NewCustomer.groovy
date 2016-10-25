package mx.wen.pos.ui.view.dialog

import groovy.swing.SwingBuilder
import mx.wen.pos.ui.controller.CustomerController
import mx.wen.pos.ui.model.Customer
import mx.wen.pos.ui.model.UpperCaseDocument
import mx.wen.pos.ui.resources.UI_Standards
import net.miginfocom.swing.MigLayout
import org.apache.commons.lang.StringUtils

import javax.swing.*
import java.awt.*
import java.awt.event.FocusEvent
import java.awt.event.FocusListener
import java.awt.image.BufferedImage
import java.awt.image.WritableRaster

class NewCustomer extends JDialog {

  private def sb = new SwingBuilder()

  private JTextField txtName
  private JTextField txtApPat
  private JTextField txtApMat
  private JTextField txtAddress
  private JTextField txtPhone
  private JTextField txtRfc
  private JLabel lblWarning
  private Customer customer
  private Boolean edit

  NewCustomer( Customer customer, Boolean edit ) {
    this.edit = edit
    this.customer = customer
    buildUI()
    doBindings()
  }

  // UI Layout Definition
  void buildUI( ) {
    sb.dialog( this,
        title: "Nuevo CLiente",
        resizable: true,
        pack: true,
        modal: true,
        preferredSize: [ 600, 300 ],
        location: [ 200, 250 ],
    ) {
      panel() {
        borderLayout()
        panel( constraints: BorderLayout.CENTER, layout: new MigLayout( "wrap 2", "10[fill][fill,grow]10", "[]5[]" ) ) {
          def displayFont = new Font('', Font.BOLD, 12)
          label( " ", constraints: "span 2")
          label("Nombre: ")
          txtName = textField( maximumSize: [400,30], document: new UpperCaseDocument() )
          label("Apellido Paterno: ")
          txtApPat = textField( maximumSize: [400,30], document: new UpperCaseDocument() )
          label("Apellido Mterno: ")
          txtApMat = textField( maximumSize: [400,30], document: new UpperCaseDocument() )
          label( "Direccion: " )
          txtAddress = textField()
          label( "Telefono: " )
          txtPhone = textField(maximumSize: [150,30])
          txtPhone.addFocusListener( new FocusListener() {
            @Override
            void focusGained(FocusEvent e) {

            }

            @Override
            void focusLost(FocusEvent e) {
              if( !StringUtils.trimToEmpty(txtPhone.text).isNumber() ){
                txtPhone.text = ""
                lblWarning.text = "Numero telefonico invalido"
              } else {
                lblWarning.text = ""
              }
            }
          })
          label( "RFC: " )
          txtRfc = textField(maximumSize: [150,30])
          txtRfc.addFocusListener( new FocusListener() {
            @Override
            void focusGained(FocusEvent e) {

            }

            @Override
            void focusLost(FocusEvent e) {
              if( CustomerController.validRfc(StringUtils.trimToEmpty(txtRfc.text)) ){
                lblWarning.text = ""
              } else if(StringUtils.trimToEmpty(txtRfc.text).length() > 0) {
                txtRfc.text = ""
                lblWarning.text = "RFC invalido"
              }
            }
          })
          lblWarning = label( "", foreground: UI_Standards.WARNING_FOREGROUND, constraints: "span ")
        }
        panel( constraints: BorderLayout.PAGE_END ) {
          borderLayout()
          panel( constraints: BorderLayout.LINE_END ) {
            button( text: "Guardar", preferredSize: UI_Standards.BUTTON_SIZE,
                actionPerformed: { onButtonOk() }
            )
            button( text: "Cerrar", preferredSize: UI_Standards.BUTTON_SIZE,
                    actionPerformed: { dispose() }
            )
          }
        }
      }

    }
  }

  // UI Management
  private void doBindings() {
    sb.build {
      bean(txtName, text: bind { customer?.name })
      bean(txtApPat, text: bind { customer?.fathersName })
      bean(txtApMat, text: bind { customer?.mothersName })
      bean(txtAddress, text: bind { customer?.adress })
      bean(txtPhone, text: bind { customer?.telHome })
      bean(txtRfc, text: bind { customer?.rfc })
    }
  }


  // Public Methods

  // UI Response
  protected void onButtonOk( ) {
    if( validData() ){
      setData()
      customer = CustomerController.addCustomer(customer, edit )
      if( customer.id != null ){
        sb.optionPane( message: 'Cliente guardado correctamente', messageType: JOptionPane.INFORMATION_MESSAGE)
                .createDialog(this, 'Cliente Guardado').show()
        dispose()
      } else {
        sb.optionPane( message: 'Error al guardar cliente', messageType: JOptionPane.ERROR_MESSAGE)
                .createDialog(this, 'Error').show()
      }
    }
  }


  private Boolean validData( ) {
    Boolean valid = true
    if( StringUtils.trimToEmpty(txtName.text).length() <= 0 ){
      valid = false
    } else if( StringUtils.trimToEmpty(txtAddress.text).length() <= 0 ){
      valid = false
    } else if( StringUtils.trimToEmpty(txtPhone.text).length() <= 0 ){
      valid = false
    } else if( StringUtils.trimToEmpty(txtApPat.text).length() <= 0 ){
      valid = false
    }
    return valid
  }

  public String getTipo( ){
    return type
  }

  private setData( ){
    if( customer == null ){
      customer = new Customer()
    }
    customer.name = StringUtils.trimToEmpty(txtName.text)
    customer.fathersName = StringUtils.trimToEmpty(txtApPat.text)
    customer.mothersName = StringUtils.trimToEmpty(txtApMat.text)
    customer.adress = StringUtils.trimToEmpty(txtAddress.text)
    customer.telHome = StringUtils.trimToEmpty(txtPhone.text)
    customer.rfc = StringUtils.trimToEmpty(txtRfc.text)
  }
}
