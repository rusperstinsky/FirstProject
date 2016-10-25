package mx.wen.pos.ui.view.panel

import groovy.model.DefaultTableModel
import groovy.swing.SwingBuilder
import mx.wen.pos.ui.controller.AccessController
import mx.wen.pos.ui.controller.CancellationController
import mx.wen.pos.ui.controller.OrderController
import mx.wen.pos.ui.model.Order
import mx.wen.pos.ui.model.OrderItem
import mx.wen.pos.ui.model.Payment
import mx.wen.pos.ui.resources.UI_Standards
//import mx.wen.pos.ui.view.dialog.AuthorizationDialog
import mx.wen.pos.ui.view.dialog.CancellationDialog
import mx.wen.pos.ui.view.dialog.RefundDialog

//import mx.wen.pos.ui.view.dialog.RefundDialog
import mx.wen.pos.ui.view.renderer.MoneyCellRenderer
import net.miginfocom.swing.MigLayout

import java.awt.Font
import java.awt.event.ActionEvent
import java.text.DateFormat
import java.text.NumberFormat
import javax.swing.*
import org.apache.commons.lang.StringUtils

import java.text.SimpleDateFormat

class ShowOrderPanel extends JPanel {


  private SwingBuilder sb
  private Order order
  private JLabel customerName
  private JButton printButton
  private JButton cancelButton
  private JButton returnButton
  private JButton printReturnButton
  private JTextArea comments
  private DefaultTableModel itemsModel
  private DefaultTableModel paymentsModel
  private DefaultTableModel dealsModel
  private JLabel folio
  private JLabel bill
    private JLabel status
  private JLabel date
  private JLabel employee
  private JLabel total
  private JLabel paid
  private JLabel due
  private JPanel navigatorPanel
  private BigDecimal sumaPagos = BigDecimal.ZERO
  private static final BigDecimal montoCentavos = BigDecimal.ZERO

  private static final String MSJ_FECHA_INCORRECTA = 'Verifique la fecha de la computadora.'
  private static final String TXT_FECHA_INCORRECTA_TITULO = 'Error al crear orden'
  private static final String MSJ_CANCELAR = '¿Esta seguro que desea cancelar la nota %s?'
  private static final String TXT_CANCELAR = 'Cancelar Factura'
  private SimpleDateFormat df = new SimpleDateFormat("ddMMyyyy")


  ShowOrderPanel( ) {
    sb = new SwingBuilder()
    order = new Order()
    buildUI()
    doBindings()
    //prom = [ ] as ObservableList
    //prom.addAll( CancellationController.findPromotionsbyidFactura( order.id ) )
  }

  private void buildUI( ) {
    sb.panel( this, layout: new MigLayout( 'insets 5,fill,wrap', '[fill]', '[fill]' ) ) {
      panel( layout: new MigLayout( 'insets 0,fill', '[fill,300][fill,200][fill,240!]', '[fill]' ) ) {
        panel( border: loweredEtchedBorder(), layout: new MigLayout( 'wrap 2', '[][fill,220!]', '[top]' ) ) {
          label( 'Cliente' )
          customerName = label( enabled: false )

          navigatorPanel = panel( constraints: 'skip' )
        }

        panel( border: loweredEtchedBorder(), layout: new MigLayout( 'wrap 2', '[][grow,right]', '[top]' ) ) {
          /*label( 'Folio' )
          folio = label()*/
          label( 'Factura' )
          bill = label()
          label( 'Fecha' )
          date = label()
          employee = label( constraints: 'span 2', maximumSize: [ 210, 30 ] )
            status = label( text: 'CANCELADA', foreground: UI_Standards.WARNING_FOREGROUND, visible: false )

        }

        panel( border: loweredEtchedBorder(), layout: new MigLayout( 'wrap 2', '[][grow,right]', '[top]' ) ) {
          def displayFont = new Font( '', Font.BOLD, 22 )
          label( 'Venta' )
          total = label( font: displayFont )
          label( 'Pagado' )
          paid = label( font: displayFont )
          label( 'Saldo' )
          due = label( font: displayFont )
        }
      }

      scrollPane( border: titledBorder( title: 'Art\u00edculos' ) ) {
        table( selectionMode: ListSelectionModel.SINGLE_SELECTION ) {
          itemsModel = tableModel( list: order.items ) {
            closureColumn(
                header: 'Art\u00edculo',
                read: {OrderItem tmp -> "${tmp?.item?.name}"},
                minWidth: 80,
                maxWidth: 100
            )
            closureColumn(
                header: 'Descripci\u00f3n',
                read: {OrderItem tmp -> tmp?.description}
            )
            closureColumn(
                header: 'Cantidad',
                read: {OrderItem tmp -> tmp?.quantity},
                minWidth: 70,
                maxWidth: 70
            )
            closureColumn(
                header: 'Precio',
                read: {OrderItem tmp -> tmp?.item?.price},
                minWidth: 80,
                maxWidth: 100,
                cellRenderer: new MoneyCellRenderer()
            )
            closureColumn(
                header: 'Total',
                read: {OrderItem tmp -> tmp?.item?.price * tmp?.quantity},
                minWidth: 80,
                maxWidth: 100,
                cellRenderer: new MoneyCellRenderer()
            )
          } as DefaultTableModel
        }
      }

      panel( layout: new MigLayout( 'insets 0,fill', '[fill][fill,240!]', '[fill]' ) ) {

       scrollPane( border: titledBorder( title: 'Pagos' ) ) {
          table( selectionMode: ListSelectionModel.SINGLE_SELECTION ) {
            paymentsModel = tableModel( list: order.payments ) {
              closureColumn( header: 'Descripci\u00f3n', read: {Payment tmp -> tmp?.description} )
              closureColumn( header: 'Monto', read: {Payment tmp -> tmp?.amount}, maxWidth: 100, cellRenderer: new MoneyCellRenderer() )
            } as DefaultTableModel
          }
        }
      }

      scrollPane( border: titledBorder( title: 'Observaciones' ) ) {
        comments = textArea( lineWrap: true, enabled: false )
      }

      panel( layout: new MigLayout( 'insets 0,right', '[grow,fill,145!]', '[grow,fill,40!]' ) ) {
        cancelButton = button( 'Cancelar', actionPerformed: doRefund, constraints: 'hidemode 3' )
        //returnButton = button( 'Devoluci\u00f3n', actionPerformed: doRefund, constraints: 'hidemode 3' )
        //printReturnButton = button( 'Imprimir Cancelaci\u00f3n', actionPerformed: doPrintRefund, constraints: 'hidemode 3' )
        printButton = button( 'Imprimir', actionPerformed: doPrint )
      }
    }
    navigatorPanel.add( new OrderNavigatorPanel( order, {doBindings()} ) )
  }

  private void doBindings( ) {
    sb.build {
      bean( customerName, text: bind {order.customer?.fullName} )
      //bean( folio, text: bind {order.id} )
      bean( bill, text: bind {order.bill} )
      bean( employee, text: bind {order.employee} )
      bean( status, visible: bind {'T'.equalsIgnoreCase( order.status )} )
      bean( date, text: bind( source: order, sourceProperty: 'date', converter: dateConverter ) )
      bean( total, text: bind( source: order, sourceProperty: 'totalSell', converter: currencyConverter ) )
      bean( paid, text: bind( source: order, sourceProperty: 'paid', converter: currencyConverter ) )
      bean( due, text: bind( source: order, sourceProperty: 'due', converter: currencyConverter ) )
      bean( itemsModel.rowsModel, value: bind( source: order, sourceProperty: 'items', mutual: true ) )
      bean( paymentsModel.rowsModel, value: bind( source: order, sourceProperty: 'payments', mutual: true ) )
      bean( comments, text: bind( source: order, sourceProperty: 'comments', mutual: true ) )
      bean( cancelButton, visible: bind {!'T'.equalsIgnoreCase( order.status ) && StringUtils.trimToEmpty(df.format(order.date)).equalsIgnoreCase(StringUtils.trimToEmpty(df.format(new Date())))} )
      sumaPagos = BigDecimal.ZERO
      for ( Payment payment : order.payments ) {
        sumaPagos = sumaPagos.add( payment.amount )
      }
      //bean( returnButton, visible: bind {( 'T'.equalsIgnoreCase( order.status ) ) && ( sumaPagos.compareTo( montoCentavos ) > 0 ) } )
      //bean( printReturnButton, visible: bind {( 'T'.equalsIgnoreCase( order.status ) ) && ( sumaPagos.compareTo( montoCentavos ) <= 0 )} )
    }
    itemsModel.fireTableDataChanged()
    paymentsModel.fireTableDataChanged()
  }

  private def dateConverter = { Date val ->
    val?.format( 'dd-MM-yyyy' )
  }

  private def currencyConverter = {
    NumberFormat.getCurrencyInstance( Locale.US ).format( it ?: 0 )
  }

  private def doCancel = { ActionEvent ev ->
    JButton source = ev.source as JButton
    source.enabled = false
    if( OrderController.validDate() ){
      if ( 'T'.equalsIgnoreCase( order.status ) ) {
        sb.optionPane( message: "La venta ya ha sido cancelada, estado: ${order?.status}", optionType: JOptionPane.DEFAULT_OPTION )
          .createDialog( this, "No se puede cancelar" )
          .show()
      } else {
          String fechaVenta = order.date.format( 'dd/MM/yyyy' )
          String hoy = new Date().format( 'dd/MM/yyyy' )
          if( hoy.equalsIgnoreCase(fechaVenta) ){
            Integer question = JOptionPane.showConfirmDialog(new JDialog(), String.format(MSJ_CANCELAR, order.bill), TXT_CANCELAR,
                  JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE)
            if( question == 0 ){
              String causa = CancellationController.findCancellationReasonById( 16 )
              CancellationController.cancelOrder( order.id, causa, '', true )
              CancellationController.printCancellationPlan( order.id )

              Map<Integer, String> creditRefunds = [ : ]
              order.payments.each { Payment pmt ->
                creditRefunds.put( pmt?.id, 'ORIGINAL' )
              }
              if ( CancellationController.refundPaymentsCreditFromOrder( order.id, creditRefunds ) ) {
                CancellationController.printOrderCancellation( order.id )
              } else {
                sb.optionPane(
                        message: 'Ocurrio un error al registrar devoluciones',
                        messageType: JOptionPane.ERROR_MESSAGE
                ).createDialog( this, 'No se registran devoluciones' )
                        .show()
              }
              CancellationController.refreshOrder( order )
              doBindings()
            }
          } else {
            new CancellationDialog( this, order.id ).show()
            CancellationController.refreshOrder( order )
            doBindings()
          }
      }
    } else {
        sb.optionPane( message: MSJ_FECHA_INCORRECTA, messageType: JOptionPane.ERROR_MESSAGE, )
                .createDialog( this, TXT_FECHA_INCORRECTA_TITULO )
                .show()
    }
    source.enabled = true
  }

  private def doRefund = { ActionEvent ev ->
    JButton source = ev.source as JButton
    source.enabled = false
    new RefundDialog( this, order.id ).show()
    CancellationController.refreshOrder( order )
    doBindings()
    source.enabled = true
  }

  private def doPrintRefund = { ActionEvent ev ->
    JButton source = ev.source as JButton
    source.enabled = false
    CancellationController.resetValuesofCancellation( order.id )
    CancellationController.printOrderCancellation( order.id )
    source.enabled = true
  }

  private def doPrint = { ActionEvent ev ->
    JButton source = ev.source as JButton
    source.enabled = false
    OrderController.printOrder( order.id )
    source.enabled = true
  }
}
