package mx.wen.pos.ui.controller

import mx.wen.pos.model.Articulo
import mx.wen.pos.model.InvAdjustLine
import mx.wen.pos.model.InvTrRequest
import mx.wen.pos.model.Parametro
import mx.wen.pos.model.Shipment
import mx.wen.pos.model.ShipmentLine
import mx.wen.pos.model.Sucursal
import mx.wen.pos.model.TipoParametro
import mx.wen.pos.model.TransInv
import mx.wen.pos.repository.impl.RepositoryFactory
import mx.wen.pos.service.ArticuloService
import mx.wen.pos.service.InventarioService
import mx.wen.pos.service.SucursalService
import mx.wen.pos.service.business.InventorySearch
import mx.wen.pos.ui.MainWindow
import mx.wen.pos.ui.model.InvTr
import mx.wen.pos.ui.model.InvTrSku
import mx.wen.pos.ui.model.InvTrViewMode
import mx.wen.pos.ui.model.Session
import mx.wen.pos.ui.model.SessionItem
import mx.wen.pos.ui.model.User
import mx.wen.pos.ui.model.adapter.InvTrFilter
import mx.wen.pos.ui.model.adapter.RequestAdapter
import mx.wen.pos.ui.resources.ServiceManager
import mx.wen.pos.ui.view.component.NavigationBar.Command
import mx.wen.pos.ui.view.dialog.InboundDialog
import mx.wen.pos.ui.view.dialog.InvTrSelectorDialog
import mx.wen.pos.ui.view.dialog.PartSelectionDialog
import mx.wen.pos.ui.view.panel.InvTrView
import org.apache.commons.lang.StringUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import javax.annotation.Resource
import javax.swing.JDialog
import javax.swing.JFileChooser
import javax.swing.JOptionPane
import javax.swing.JTextField
import javax.swing.SwingUtilities
import mx.wen.pos.model.InvAdjustSheet
import mx.wen.pos.service.business.Registry

import java.nio.channels.FileChannel

class InvTrController {

  private static final Logger log = LoggerFactory.getLogger( InvTrController.class )

  private static InvTrController instance
  private PartSelectionDialog dlgPartSelection
  private InvTrSelectorDialog dlgSelector
  private JFileChooser dlgFile

  private static final String MSJ_ARCHIVO_GENERADO = 'El archivo IN2 fue generado correctamente en %s'
  private static final String TXT_ARCHIVO_GENERADO = 'Archivo IN2'
  private static final String MSJ_ARCHIVO_NO_GENERADO = 'No se genero correctamente el archivo de inventario'
  private static final String TAG_REMESA = 'ENTRADA'

 // private static SucursalService sucursalService

  private InvTrController( ) { }

  static InvTrController getInstance( ) {
    if ( instance == null ) {
      instance = new InvTrController()
    }
    return instance
  }

  // Initialize
  private JFileChooser getDialogFile( ) {
    if ( dlgFile == null ) {
      dlgFile = new JFileChooser()
    }
    return dlgFile
  }

  // Dispatch Actions
  protected void dispatchDocument( InvTrView pView, Shipment pDocument ) {
    log.debug( "[Controller] Dispatch receiving document" )
    pView.notifyDocument( pDocument )
  }

  protected void dispatchDocument( InvTrView pView, InvAdjustSheet pDocument ) {
    log.debug( "[Controller] Dispatch receiving document" )
    pView.notifyDocument( pDocument )
  }

  protected void dispatchDocumentEmpty( InvTrView pView, Boolean fileAlreadyProccessed, String articleNotFound ) {
    log.debug( "[Controller] Dispatch document unavailable" )
    InvTrController controller = this
    SwingUtilities.invokeLater( new Runnable() {
      public void run( ) {
        controller.dispatchViewModeQuery( pView )
        if( fileAlreadyProccessed ){
          pView.data.txtStatus = pView.panel.MSG_DOCUMENT_ALREADY_PROCCESED
        } else if( articleNotFound != '' ){
          pView.data.txtStatus = String.format( pView.panel.MSG_ARTICLE_NOT_FOUND, articleNotFound )
        } else {
          pView.data.txtStatus = pView.panel.MSG_NO_DOCUMENT_AVAILABLE
        }
        pView.fireRefreshUI()
      }
    } )
  }

  protected void dispatchPartMasterUpdate( Shipment pDocument ) {
    log.debug( "[Controller] Update Part Master with Parts in Receiving Document" )
    if ( ( pDocument != null ) && ( pDocument.partShadows.size() > 0 ) ) {
      ArticuloService partMaster = ServiceManager.partService

      partMaster.actualizarArticulosConSombra( pDocument.partShadows )
    }
  }

  protected void dispatchPartsSelected( InvTrView pView, List<Articulo> pPartList ) {
    for ( Articulo part in pPartList ) {
      pView.data.addPart( part )
    }
    pView.fireConsumePartSeed()
    pView.fireRefreshUI()
  }

  protected void dispatchPrintTransaction( String pTrType, Integer pTrNbr ) {
    TransInv tr = ServiceManager.inventoryService.obtenerTransaccion( pTrType, pTrNbr )
    if ( tr != null ) {
      //ServiceManager.ticketService.imprimeTransInv( tr )
    }
  }
   /*
    protected static String replaceCharAt(String s, int pos, char c) {
        StringBuffer buf = new StringBuffer( s );
        buf.setCharAt( pos, c );
        return buf.toString( );
    }

    protected  String claveAleatoria(Integer sucursal, Integer folio) {
    String folioAux = "" + folio.intValue();
    String sucursalAux = "" + sucursal.intValue()
    String abc = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    if (folioAux.size() < 4) {
        folioAux = folioAux?.padLeft( 4, '0' )
    }
    else {
        folioAux = folioAux.substring(0,4);
    }
    String resultado = sucursalAux?.padLeft( 3, '0' ) + folioAux


    for (int i = 0; i < resultado.size(); i++) {
        int numAleatorio = (int) (Math.random() * abc.size());
        if (resultado.charAt(i) == '0') {
            resultado = replaceCharAt(resultado, i, abc.charAt(numAleatorio))
        }
        else {
            int numero = Integer.parseInt ("" + resultado.charAt(i));
            numero = 10 - numero
            char diff = Character.forDigit(numero, 10);
            resultado = replaceCharAt(resultado, i, diff)
        }


    }
    return resultado;
  }

  protected String generaSalida( InvTrViewMode viewMode, InvTrView pView ) {
      String url = Registry.getURL( viewMode.trType.idTipoTrans );
      if ( StringUtils.trimToNull( url ) != null ) {
          User user = Session.get( SessionItem.USER ) as User

         String variable = pView.data.qryDataset.dataset[0].sucursal + '>' + pView.data.postSiteTo.id + '>' +
                          pView.data.postTrType.ultimoFolio + '>' +
                          claveAleatoria(pView.data.qryDataset.dataset[0].sucursal, pView.data.postTrType.ultimoFolio)  +
                          '>' + user.username + '>'

         for (int i = 0; i < pView.data.skuList.size(); i++) {
             variable += pView.data.skuList[i].part.id + '|'
         }
         url += String.format( '?arg=%s', URLEncoder.encode( String.format( '%s', variable ), 'UTF-8' ) )
         String response = url.toURL().text
         response = response?.find( /<XX>\s*(.*)\s*<\/XX>/ ) {m, r -> return r}
         log.debug( "resultado solicitud: ${response}" )
         return response
      }
  }
       */

  protected String confirmaEntrada(InvTrViewMode viewMode, InvTrView pView, Boolean onlyGenerateFile){
      String url = Registry.getURLConfirmacion( viewMode.trType.idTipoTrans );
      if( TAG_REMESA.equalsIgnoreCase(viewMode.trType.idTipoTrans.trim()) ){
        if( !onlyGenerateFile ){
          ServiceManager.getIoServices().updateRemesa( viewMode.trType.idTipoTrans.trim() )
        }
        ServiceManager.getIoServices().logRemittanceNotification( viewMode.trType.idTipoTrans.trim(), viewMode.trType.ultimoFolio+1,
                pView.data.receiptDocument.code, onlyGenerateFile )
      } else if ( StringUtils.trimToNull( url ) != null ) {
        String variable = pView.data.claveCodificada + ">" + pView.data.postTrType.ultimoFolio
        url += String.format( '?arg=%s', URLEncoder.encode( String.format( '%s', variable ), 'UTF-8' ) )
        String response = url.toURL().text
        response = response?.find( /<XX>\s*(.*)\s*<\/XX>/ ) {m, r -> return r}
        log.debug( "resultado solicitud: ${response}" )
        return response
      }
  }

  protected void dispatchViewModeAdjust( InvTrView pView ) {
    pView.data.clear()
    pView.fireResetUI()
    pView.notifyViewMode( InvTrViewMode.ADJUST )
    pView.fireDisplay()
  }

  protected void dispatchViewModeFileAdjust( InvTrView pView ) {
    pView.data.clear()
    pView.fireResetUI()
    pView.notifyViewMode( InvTrViewMode.FILE_ADJUST )
    pView.fireDisplay()
    this.requestAdjustFile( pView )
  }

  protected void dispatchViewModeIssue( InvTrView pView ) {
    pView.data.clear()
    pView.fireResetUI()
    pView.notifyViewMode( InvTrViewMode.ISSUE )
    pView.data.postSiteTo = null
    pView.fireDisplay()
  }

  List<Sucursal> listaAlmacenes(){
      //List<Sucursal> lstAlmacenes = ServiceManager.getInventoryService().listarAlmacenes()
      //lstAlmacenes.add( 0, new Sucursal() )
      //return lstAlmacenes
    return new ArrayList<Sucursal>()
  }

    List<Sucursal> listaSoloSucursales(){
        List<Sucursal> lstAlmacenes = ServiceManager.getInventoryService().listarSoloSucursales()
        lstAlmacenes.add( 0, null )
        return lstAlmacenes
    }

  protected void dispatchViewModeQuery( InvTrView pView ) {
    pView.data.clear()
    pView.fireResetUI()
    pView.notifyViewMode( InvTrViewMode.QUERY )
    if ( pView.data.qryDataset.size > 0 ) {
      pView.data.qryInvTr = pView.data.qryDataset.first
    }
    pView.fireDisplay()
  }

  protected void dispatchViewModeReceipt( InvTrView pView ) {
    pView.data.clear()
    pView.fireResetUI()
    pView.notifyViewMode( InvTrViewMode.RECEIPT )
    pView.fireDisplay()
    //requestReceipt( pView )
  }

  protected void dispatchViewModeReturn( InvTrView pView ) {
    pView.data.clear()
    pView.fireResetUI()
    pView.notifyViewMode( InvTrViewMode.RETURN )
    pView.fireDisplay()
  }


  protected void dispatchViewModeOutBound( InvTrView pView ) {
    pView.data.clear()
    pView.fireResetUI()
    pView.notifyViewMode( InvTrViewMode.OUTBOUND )
    pView.data.postSiteTo = null
    pView.fireDisplay()
  }

  protected void dispatchViewModeInBound( InvTrView pView ) {
    pView.data.clear()
    pView.fireResetUI()
    pView.notifyViewMode( InvTrViewMode.INBOUND )
    pView.fireDisplay()
    requestInBound( pView )
  }
    // Actions started
  protected void fireChangeViewMode( InvTrView pView, InvTrViewMode pNewMode ) {
    Boolean confirmed = true
    if ( !pNewMode.equals( pView.data.viewMode ) ) {
      if ( pView.data.dirty ) {
        String msg = String.format( pView.panel.MSG_CONFIRM_TO_PROCEED, pNewMode.toString() )
        Integer confirm = JOptionPane.showConfirmDialog( pView.panel, msg, pView.panel.TXT_CONFIRM_TITLE,
            JOptionPane.YES_NO_OPTION )
        confirmed = ( confirm == JOptionPane.YES_OPTION )
      }
      if ( confirmed ) {
        if ( pNewMode.equals( InvTrViewMode.ISSUE ) ) {
          dispatchViewModeIssue( pView )
        } else if ( pNewMode.equals( InvTrViewMode.RECEIPT ) ) {
          dispatchViewModeReceipt( pView )
        } else if ( pNewMode.equals( InvTrViewMode.QUERY ) ) {
          dispatchViewModeQuery( pView )
        } else if ( pNewMode.equals( InvTrViewMode.ADJUST ) ) {
          dispatchViewModeAdjust( pView )
        } else if ( pNewMode.equals( InvTrViewMode.RETURN ) ) {
          dispatchViewModeReturn( pView )
        } else if ( pNewMode.equals( InvTrViewMode.FILE_ADJUST ) ) {
            dispatchViewModeFileAdjust( pView )
        } else if ( pNewMode.equals( InvTrViewMode.OUTBOUND) ) {
            dispatchViewModeOutBound( pView )
        } else if ( pNewMode.equals( InvTrViewMode.INBOUND) ) {
            dispatchViewModeInBound( pView )
        }
      } else {
        pView.notifyViewModeChangeCancelled()
      }
    }
  }

  // Requests
  void requestAdjustFile( InvTrView pView ) {
    log.debug( "[Controller] Request Adjust File" )
    InvAdjustSheet document = null
    JFileChooser dialog = this.getDialogFile()
    dialog.currentDirectory = new File(SettingsController.instance.incomingPath)
    dialog.fileSelectionMode = JFileChooser.FILES_ONLY
    String filename = null
    int fileAction = getDialogFile().showOpenDialog( pView.panel )
    if ( fileAction == JFileChooser.APPROVE_OPTION ) {
      filename = dlgFile.getSelectedFile().absolutePath
      log.debug( String.format( "[Controller] File chosen: %s", filename ) )
      document = ServiceManager.getInventoryService().leerArchivoAjuste( filename )
    }
    if ( document != null ) {
      dispatchDocument( pView, document )
      pView.data.inFile = new File( filename )
      log.debug ( String.format('Adjust File: %s', document.headerToString()) )
    } else {
      dispatchDocumentEmpty( pView, false, '' )
      log.debug ( 'No document' )
    }

  }

  void requestItem( InvTrView pView, Command pCommand ) {
    log.debug( String.format( "[Controller] Navigate to <%s>", pCommand.toString() ) )
    pView.data.qryInvTr = pView.data.qryDataset.get( pCommand )
    pView.fireRefreshUI()
  }

  void requestNewSearch( InvTrView pView ) {
    log.debug( "[Controller] New Search" )
    if ( dlgSelector == null ) {
      dlgSelector = new InvTrSelectorDialog( pView.data.qryDataset )
    }
    dlgSelector.activate()
    if ( pView.data.qryDataset.currentIndex == null ) {
      pView.data.qryInvTr = pView.data.qryDataset.first
    } else {
      pView.data.qryInvTr = pView.data.qryDataset.getCurrent()
    }
    pView.fireRefreshUI()
  }

  void requestPart( InvTrView pView ) {
      String[] part = pView.data.partSeed.split(',')
      log.debug( String.format( "[Controller] Request Part with seed <%s>", part[0] ) )
    String seed = part[0]
    List<Articulo> partList = ItemController.findPartsQuery( seed, "" )
    if ( ( partList.size() == 0 ) && ( seed.length() > 6 ) ) {
      seed = seed.substring( 0, 6 )
      partList = ItemController.findPartsQuery( seed, false )
    }
    if ( partList?.any() ) {
      if ( partList.size() == 1 )  {
        if( partList.first().cantExistencia <= 0 && pView.data.viewMode.trType.tipoMov.trim().equalsIgnoreCase('S') &&
        ItemController.isInventoried(partList.first().id)){
          Integer question =JOptionPane.showConfirmDialog( new JDialog(), pView.panel.MSG_NO_STOCK, pView.panel.TXT_NO_STOCK,
                  JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE )
          if( question == 0){
            dispatchPartsSelected( pView, partList )
          } else {
            pView.panel.stock = false
          }
        } else {
          dispatchPartsSelected( pView, partList )
        }

      } else {
        if ( dlgPartSelection == null ) {
          dlgPartSelection = new PartSelectionDialog( pView.panel )
        }
        dlgPartSelection.setItems( partList )
        dlgPartSelection.setSeed( seed )
        if ( InvTrViewMode.ADJUST.equals( pView.data.viewMode ) ) {
          dlgPartSelection.multiSelection = false
        }
        dlgPartSelection.activate()
        List<Articulo> selection = dlgPartSelection.getSelection()
        if ( selection != null ) {
          if( selection.first().cantExistencia <= 0 && pView.data.viewMode.trType.tipoMov.trim().equalsIgnoreCase('S') &&
                  ItemController.isInventoried(partList.first().id)){
              Integer question =JOptionPane.showConfirmDialog( new JDialog(), pView.panel.MSG_NO_STOCK, pView.panel.TXT_NO_STOCK,
                      JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE )
              if( question == 0){
                  dispatchPartsSelected( pView, selection )
              } else {
                pView.panel.stock = false
              }
          } else {
            log.debug( String.format( "[Controller] %d Selected, (%d) %s", selection.size(), selection[ 0 ].id, selection[ 0 ].descripcion ) )
            dispatchPartsSelected( pView, selection )
          }
        }
      }
    } else {
      JOptionPane.showMessageDialog( pView.panel, String.format( pView.panel.MSG_NO_RESULTS_FOUND, seed ),
          String.format( pView.panel.TXT_QUERY_TITLE, seed ), JOptionPane.INFORMATION_MESSAGE )
    }
  }

  void requestReceipt( InvTrView pView ) {
    log.debug( "[Controller] Request Receipt" )
    Shipment document = null
    int fileAction = getDialogFile().showOpenDialog( pView.panel )
    if ( fileAction == JFileChooser.APPROVE_OPTION ) {
      log.debug( String.format( "[Controller] File chosen: %s", dlgFile.getSelectedFile().absolutePath ) )
      document = ServiceManager.getInventoryService().leerArchivoRemesa( dlgFile.getSelectedFile().absolutePath )
      pView.data.inFile = new File( dlgFile.getSelectedFile().absolutePath )
    }
    if ( document != null ) {
      InventarioService service = ServiceManager.inventoryService
      List<TransInv> trList = service.listarTransaccionesPorTipoAndReferencia( InvTrViewMode.RECEIPT.trType.idTipoTrans,
              document?.ref.trim() )
      if( trList.size() <= 0 ){
          dispatchPartMasterUpdate( document )
          dispatchDocument( pView, document )
      } else {
          dispatchDocumentEmpty( pView, true, '' )
      }
    } else {
      dispatchDocumentEmpty( pView, false, '' )
    }

  }


  void requestInBound( InvTrView pView ) {
    log.debug( "[Controller] Request Inbound" )
    Shipment document = null
      InboundDialog inboundDialog = new InboundDialog()
      inboundDialog.activate()
      log.debug(inboundDialog.getTxtClave())
      if (inboundDialog.button) {
          Boolean claveNoCargada = ServiceManager.inventoryService.transaccionCargada( inboundDialog.getTxtClave() )
              pView.data.claveCodificada = inboundDialog.getTxtClave()
              Sucursal sucursal = ServiceManager.inventoryService.sucursalActual()
              log.debug("" + sucursal.id)
              document = ServiceManager.getInventoryService().obtieneArticuloEntrada(inboundDialog.getTxtClave(),sucursal.id, pView.data.viewMode.trType.idTipoTrans)
              Boolean articleExist = true
              String articles = ''
                if( document != null ){
                    for(ShipmentLine line : document.lines ){
                        if(line.partCode == null || line.partCode == ''){
                            articleExist = false
                            articles = articles+"["+line.sku.toString()+"]"+" "
                        }
                    }
                }
               if ( document != null && !claveNoCargada && articleExist ) {
                  dispatchPartMasterUpdate( document )
                  dispatchDocument( pView, document )
              } else {
                   dispatchDocumentEmpty( pView, false, articles )
              }
      }
    }

  void requestSaveAndPrint( InvTrView pView ) {
    log.debug( "[Controller] Save and Print" )
    InvTrRequest request = RequestAdapter.getRequest( pView.data )
    if ( request != null ) {
     Boolean onlyGenerateFile = false
        request.remarks = request.remarks.replaceAll("[^a-zA-Z0-9]+"," ");
      Integer trNbr = ServiceManager.getInventoryService().solicitarTransaccion( request )
      if ( trNbr != null && trNbr > -1 ) {
        if ( pView.data.inFile != null ) {
          try {
            File moved = new File( SettingsController.instance.processedPath, pView.data.inFile.name )
            if (InvTrViewMode.OUTBOUND.equals( pView.data.viewMode ) || InvTrViewMode.FILE_ADJUST.equals( pView.data.viewMode )) {
                pView.data.inFile.delete();
            } else {
              List<File> lstFiles = new ArrayList<>();
              if(moved.exists()) {
                moved.delete()
              }
              try {
                Integer iLine = 0
                pView.data.inFile.eachLine() {
                  if( it.contains(",") ){
                    onlyGenerateFile = true
                  }
                }
                FileInputStream inFile = new FileInputStream(pView.data.inFile);
                FileOutputStream outFile = new FileOutputStream(moved);
                Integer c;
                lstFiles.add(pView.data.inFile)
                while( (c = inFile.read() ) != -1)
                outFile.write(c);
                inFile.close();
                outFile.close();
              } catch(IOException e) {
                System.out.println( e )
              }
              for(File files : lstFiles){
                files.delete()
              }
                //pView.data.inFile.renameTo( moved )
            }
          } catch (Exception e) {
            this.log.debug( e.getMessage() )
          }
        }
        InvTrViewMode viewMode = pView.data.viewMode
        if ( InvTrViewMode.ISSUE.equals( viewMode ) || InvTrViewMode.ADJUST.equals( viewMode )
            || InvTrViewMode.RETURN.equals( viewMode ) || InvTrViewMode.FILE_ADJUST.equals( viewMode )
            || InvTrViewMode.RECEIPT.equals( viewMode ) || InvTrViewMode.OUTBOUND.equals( viewMode )
            || InvTrViewMode.INBOUND.equals( viewMode )) {
          dispatchPrintTransaction( viewMode.trType.idTipoTrans, trNbr )
          if (InvTrViewMode.INBOUND.equals( viewMode ) || InvTrViewMode.RECEIPT.equals( viewMode )) {
            //String resultado = confirmaEntrada(viewMode, pView, onlyGenerateFile)
          }
          /*if( InvTrViewMode.FILE_ADJUST.equals( viewMode ) ){
            generaAcuseAjusteInventario(viewMode, pView)
          }*/
          /*if( ServiceManager.getInventoryService().isReceiptDuplicate() ){
            dispatchPrintTransaction( viewMode.trType.idTipoTrans, trNbr )
          }*/
        }
        pView.fireResetUI()
        pView.data.clear()
        if ( InvTrViewMode.RECEIPT.equals( viewMode ) || InvTrViewMode.FILE_ADJUST.equals( viewMode ) || InvTrViewMode.ISSUE.equals( viewMode )) {
          InvTrController controller = this
          SwingUtilities.invokeLater( new Runnable() {
            void run( ) {
              pView.uiDisabled = true
              controller.dispatchViewModeQuery( pView )
              InvTrFilter filter = pView.data.qryDataset.filter
              filter.reset()
              filter.setDateRange( new Date() )
              pView.data.qryDataset.requestTransactions()
              pView.data.txtStatus = pView.panel.MSG_TRANSACTION_POSTED
              pView.fireRefreshUI()
              pView.uiDisabled = false
            }
          } )
        } else {
          pView.data.txtStatus = pView.panel.MSG_TRANSACTION_POSTED
          pView.fireRefreshUI()
        }
      } else if( trNbr == -1 ){
          JOptionPane.showMessageDialog( pView.panel, String.format(pView.panel.MSG_GENERIC_INVALID, ServiceManager.getInventoryService().genericoInvalidoTransEntrada()),
                  pView.panel.TXT_POST_TITLE, JOptionPane.ERROR_MESSAGE )
      } else if( trNbr == -2 ){
          JOptionPane.showMessageDialog( pView.panel, pView.panel.MSG_POST_FAILED, pView.panel.TXT_POST_TITLE, JOptionPane.ERROR_MESSAGE )
      }
    } else {
      log.debug( "[Controller] Request not available" )
    }
  }

  void requestPrint( String pIdTipoTrans, Integer pTrNbr ) {
    log.debug( "[Controller] Print Transaction" )
    dispatchPrintTransaction( pIdTipoTrans, pTrNbr )
  }

  void requestViewModeChange( InvTrView pView ) {
    log.debug( String.format( "[Controller] View Mode change: <%s>", pView.panel.comboViewMode.selection ) )
    fireChangeViewMode( pView, pView.panel.comboViewMode.selection )
  }

  void requestPrintTransactions( Date fechaTicket ){
    log.debug( "requestPrintTransactions" )
    ServiceManager.ticketService.imprimeTransaccionesInventario( fechaTicket )
  }

  protected void generaAcuseAjusteInventario(File inFile){
    String[] dataFileName = inFile.name.split(/\./)
    String newFileName = ""
    if( dataFileName.length >= 3 ){
      newFileName = dataFileName[0]+"."+dataFileName[1]+"."+"aja"
    } else {
      newFileName = inFile.name
    }
    File deleted = new File( SettingsController.instance.processedPath, inFile.name )
    FileChannel source = null;
    FileChannel destination = null;
    source = new FileInputStream(inFile).getChannel();
    destination = new FileOutputStream(deleted).getChannel();
    if (destination != null && source != null) {
      destination.transferFrom(source, 0, source.size());
    }
    if (source != null) {
      source.close();
    }
    if (destination != null) {
      destination.close();
    }
    File moved = new File( Registry.archivePath, newFileName )
    inFile.renameTo( moved )
  }

  void generateIN2( Date dateStart, Date dateFinish ){
    Calendar cal=Calendar.getInstance();
    cal.setTime( dateStart )
    cal.set(Calendar.DAY_OF_MONTH,Calendar.getInstance().getActualMinimum(Calendar.DAY_OF_MONTH));
    Boolean generado = InventorySearch.generaIN2( dateStart, dateFinish, cal.getTime() )
    if( generado ){
      JOptionPane.showMessageDialog( new JDialog(), String.format(MSJ_ARCHIVO_GENERADO, Registry.dailyClosePath), TXT_ARCHIVO_GENERADO, JOptionPane.INFORMATION_MESSAGE )
    } else {
      JOptionPane.showMessageDialog( new JDialog(), MSJ_ARCHIVO_NO_GENERADO, TXT_ARCHIVO_GENERADO, JOptionPane.INFORMATION_MESSAGE )
    }
  }


    void readAdjutFile(){
        Parametro ubicacion = Registry.find( TipoParametro.RUTA_POR_RECIBIR )
        Parametro parametro = RepositoryFactory.registry.findOne( TipoParametro.RUTA_RECIBIDOS.value )
        String ubicacionSource = ubicacion.valor
        String ubicacionsDestination = parametro.valor
        File source = new File( ubicacionSource )
        File destination = new File( ubicacionsDestination )
        if ( source.exists() && destination.exists() ) {
            source.eachFile() { file ->
                if ( file.getName().endsWith( ".ajs" ) ) {
                    InvTr data = new InvTr()
                    InvAdjustSheet document = null
                    String filename = null
                    filename = file.absolutePath
                    log.debug( String.format( "[Controller] File found: %s", filename ) )
                    document = ServiceManager.getInventoryService().leerArchivoAjuste( filename )
                    data.inFile = new File( filename )
                    data.postRemarks = document.trReason
                    data.postReference = document.ref
                    data.postSiteTo = null
                    data.postTrType = RepositoryFactory.trTypes.findOne("AJUSTE")
                    Integer contador = 1
                    for ( InvAdjustLine det in document.lines ) {
                        Articulo part = ServiceManager.partService.obtenerArticulo( det.sku, false )
                        data.skuList.add( new InvTrSku( data, contador, part, det.qty ) )
                        contador = contador+1
                    }
                    InvTrRequest request = RequestAdapter.getRequest( data )
                    request.remarks = request.remarks.replaceAll("[^a-zA-Z0-9]+"," ");
                    Integer trNbr = null
                    if( validReference(StringUtils.trimToEmpty(data.postTrType.idTipoTrans), StringUtils.trimToEmpty(data.postReference)) ){
                        trNbr = ServiceManager.getInventoryService().solicitarTransaccion( request )
                    }
                    if ( trNbr != null ) {
                        //File moved = new File( SettingsController.instance.processedPath, data.inFile.name )
                        //data.inFile.renameTo( moved )
                        generaAcuseAjusteInventario(data.inFile)
                        dispatchPrintTransaction( data.postTrType.idTipoTrans, trNbr )
                    }
                }
            }
        }
    }


    Boolean validReference( String idTipoTrans, String ref ){
      List<TransInv> lstTrans = RepositoryFactory.inventoryMaster.findByIdTipoTransAndReferencia( idTipoTrans, ref )
      if( lstTrans.size() > 0 ){
        return false
      } else {
        return true
      }
    }


}

