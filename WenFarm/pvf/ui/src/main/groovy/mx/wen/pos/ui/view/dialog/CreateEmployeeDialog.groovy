package mx.wen.pos.ui.view.dialog

import groovy.swing.SwingBuilder
import mx.wen.pos.ui.controller.AccessController
import mx.wen.pos.ui.model.UpperCaseDocument
import mx.wen.pos.ui.resources.UI_Standards
import mx.wen.pos.ui.view.verifier.DateVerifier
import net.miginfocom.swing.MigLayout

import javax.swing.*
import java.awt.*
import java.text.DateFormat
import java.text.SimpleDateFormat

class CreateEmployeeDialog extends JDialog {

  private DateFormat df = new SimpleDateFormat( "dd/MM/yyyy" )
  private DateVerifier dv = DateVerifier.instance
  private def sb = new SwingBuilder()

  private JTextField txtUsuario
  private JPasswordField txtPassword
  private JTextField txtName
  private JLabel lblWarning
  private String usuario
  private String password
  private String name

  public boolean button = false

    CreateEmployeeDialog( ) {
    buildUI()
  }

  // UI Layout Definition
  void buildUI( ) {
    sb.dialog( this,
        title: "Alta de Vendedor",
        resizable: true,
        pack: true,
        modal: true,
        preferredSize: [ 500, 350 ],
        location: [ 200, 250 ],
    ) {
      panel() {
        borderLayout()
        panel( constraints: BorderLayout.CENTER, layout: new MigLayout( "wrap 2", "20[][grow,fill]60", "20[]10[]" ) ) {
          label( text: "Inserte los datos de el vendedor", constraints: "span 2" )
          label( text: " ", constraints: "span 2" )
          label( text: "Nombre Completo:" )
          txtName = textField( document: new UpperCaseDocument() )
          label( text: "Usuario:" )
          txtUsuario = textField( document: new UpperCaseDocument() )
          label( text: "Password:" )
          txtPassword = passwordField(  )
          lblWarning = label( visible: false, constraints: 'span 2', foreground: UI_Standards.WARNING_FOREGROUND )
        }

        panel( constraints: BorderLayout.PAGE_END ) {
          borderLayout()
          panel( constraints: BorderLayout.LINE_END ) {
            button( text: "Aplicar", preferredSize: UI_Standards.BUTTON_SIZE,
                actionPerformed: { onButtonOk() }
            )
            button( text: "Cancelar", preferredSize: UI_Standards.BUTTON_SIZE,
                actionPerformed: { onButtonCancel() }
            )
          }
        }
      }

    }
  }

  // Public Methods
  void activate( ) {
    setVisible( true )
  }

  // UI Response
  protected void onButtonCancel( ) {
    button = false
    dispose()
  }

  protected void onButtonOk( ) {
    usuario = txtUsuario.getText().trim()
    password = txtPassword.getText().trim()
    name = txtName.getText().trim()
    if( usuario.length() > 0 && password.length() > 0 && name.length() > 0 ){
        button = true
        Boolean existEmpleado = AccessController.userExist( usuario )
        if( !existEmpleado ){
            Boolean actualizo = AccessController.insertEmployee( usuario, password, name )
            if( !actualizo ){
              println 'error al actualizar'
            } else {
              dispose()
            }
        } else {
            lblWarning.visible = true
            lblWarning.text = 'El usuario ya existe'
        }
    } else {
        lblWarning.visible = true
        lblWarning.text = 'Verifique los datos'
    }
  }
}
