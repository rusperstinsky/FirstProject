package mx.wen.pos.ui.view.driver

import mx.wen.pos.model.Articulo
import mx.wen.pos.model.ShipmentLine
import mx.wen.pos.model.TransInv
import mx.wen.pos.service.ArticuloService
import mx.wen.pos.service.InventarioService
import mx.wen.pos.ui.model.InvTrSku
import mx.wen.pos.ui.model.InvTrViewMode
import mx.wen.pos.ui.model.adapter.InvTrAdapter
import mx.wen.pos.ui.resources.ServiceManager
import mx.wen.pos.ui.resources.UI_Standards
import mx.wen.pos.ui.view.panel.InvTrView
import org.apache.commons.lang3.StringUtils

import javax.swing.JOptionPane

class InvTrReceiptDriver extends InvTrDriver {

  // Public methods
  Boolean assign( InvTrView pView ) {
    Boolean validated
    // Validate
    validated = ( pView.data.getSkuList().size() > 0 )

    // Assign
    if ( validated ) {
      // No assign, no modifications allowed, 
      // As the remission was processed a transaction is generated
    }

    return validated
  }

  Boolean isPartSeedValid( InvTrView pView ) {
    String seed = pView.panel.txtPartSeed.getText( ).trim( ).toUpperCase( )
    Boolean valid = (! StringUtils.isEmpty( seed ) )
    if (valid) {
      valid = ! seed.equals( pView.panel.TXT_SEED_ISSUE_PROMPT.trim( ).toUpperCase( ) )
    }
    return valid
  }


  void assignPartSeed( InvTrView pView ) {
    pView.data.flagOnSiteTo &= ( pView.panel.comboSiteTo.selection == null )
    pView.data.flagOnRemarks &= ( ! isRemarksValid( pView ) )
    super.assignPartSeed( pView )
  }



  void onSkuDoubleClicked( InvTrView pView ) {
    logger.debug( "[Driver] Double clicked on sku table" )
    if ( pView.panel.tBrowser.selectedRow >= 0 ) {
      InvTrSku line = pView.data.skuList[ pView.panel.tBrowser.selectedRow ]
      String msg = String.format( pView.panel.MSG_CONFIRM_REMOVE_ISSUE, line.sku, line.description )
      Integer selection = JOptionPane.showConfirmDialog( pView.panel, msg, pView.panel.TXT_CONFIRM_TITLE,
              JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE )
      if ( selection.equals( JOptionPane.OK_OPTION ) ) {
        pView.data.skuList.remove( line )
        pView.data.postSiteTo = pView.panel.comboSiteTo.getSelection()
        pView.panel.newTransaction = false
        pView.fireRefreshUI( )
      }
    }
  }


  void enableUI( InvTrView pView ) {
    super.enableUI( pView )
    UI_Standards.setLocked( pView.panel.txtPartSeed, false )
    UI_Standards.setLocked( pView.panel.txtRemarks, false )
    pView.panel.btnPrint.setEnabled( !pView.data.flagOnDocument )
    pView.panel.btnPrint.setText( pView.panel.TXT_BTN_REGISTER_CAPTION )
    pView.panel.selector.setVisible( false )
  }

  void flagRemission( InvTrView pView ) {
    pView.panel.lblStatus.setText( pView.data.documentWarning )
  }

  public void processRemission( InvTrView pView ) {
    Boolean unknownArticle = false
    ArticuloService partMaster = ServiceManager.partService
    pView.data.postReference = pView.data.receiptDocument.fullRef
    for ( ShipmentLine det in pView.data.receiptDocument.lines ) {
      Articulo part = partMaster.obtenerArticulo( det.sku, false )
      if ( part != null ) {
        pView.data.addPart( part, det.qty )
      } else {
          unknownArticle = true
          pView.data.claveCodificada = det.sku.toString()
      }
    }
    if( unknownArticle ){
        pView.data.skuList.clear()
    }
  }

  public void refreshUI( InvTrView pView ) {
      Integer quantity = 0
      for(InvTrSku article : pView.data.skuList){
          quantity = quantity+article.qty
      }
    if( pView.data.postReference != "" && pView.data.skuList.size() <= 0 ){
        pView.panel.lblStatus.setText( 'Articulo '+'['+pView.data.claveCodificada+']'+' no existe' )
    } else {
        pView.panel.lblStatus.setText( pView.data.accessStatus() )
    }
    pView.panel.txtEffDate.setText( pView.adapter.getText( pView.data, InvTrAdapter.FLD_TODAY ) )
    pView.panel.txtRef.setText( pView.data.postReference )
      pView.panel.txtType.setText( String.format( '%d', quantity ) )
    pView.panel.txtUser.setText( pView.adapter.getText( pView.data.currentUser ) )
    pView.panel.browserSku.fireTableDataChanged()
  }

  Boolean searchRemission( InvTrView pView ) {
    InventarioService service = ServiceManager.inventoryService
    List<TransInv> trList = service.listarTransaccionesPorTipoAndReferencia( InvTrViewMode.RECEIPT.trType.idTipoTrans,
        pView.data.receiptDocument.fullRef )
    if ( trList.size > 0 ) {
      pView.data.flagOnDocument = true
      pView.data.documentWarning = String.format( pView.panel.MSG_RECEIPT_WARNING,
          trList.size, InvTrViewMode.RECEIPT.trType.idTipoTrans, pView.data.receiptDocument.fullRef,
          pView.adapter.getText( trList[ 0 ].fecha ), pView.adapter.getText( trList[ trList.size - 1 ].fecha ) )
    }
    return ( trList.size() > 0 )
  }


  private Boolean isRemarksValid( InvTrView pView ) {
    String rmks = pView.panel.txtRemarks.getText( ).trim( )
    Boolean valid = (! StringUtils.isEmpty( rmks ) )
    if (valid) {
      valid = ! rmks.equalsIgnoreCase( pView.panel.TXT_REMARKS_PROMPT.trim( ) )
    }
    return valid
  }


}
