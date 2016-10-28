package mx.wen.pos.ui.model

import mx.wen.pos.model.TransInv
import mx.wen.pos.service.InventarioService
import mx.wen.pos.ui.model.adapter.InvTrFilter
import mx.wen.pos.ui.resources.ServiceManager

class InvTrDataset extends Dataset<TransInv> {

  InvTrDataset() {
    filter = new InvTrFilter()
  }

  InvTrFilter getFilter() {
    return (InvTrFilter) super.filter
  }

  void requestTransactions( ) {
    List<TransInv> rawList = new ArrayList<TransInv>()
    InventarioService service = ServiceManager.inventoryService
    if ( filter.isDateRangeActive( ) ) {
      rawList = service.listarTransaccionesPorRangoFecha( filter.dateFrom, filter.dateTo )
    } else if ( filter.isSiteToActive( ) ) {
      rawList = service.listarTransaccionesPorSucursalDestino( filter.siteTo )
    } else if ( filter.isTrTypeActive( )( ) ) {
      rawList = service.listarTransaccionesPorTipo( filter.trType )
    } else if ( filter.isSkuActive( ) ) {
      rawList = service.listarTransaccionesPorSku( filter.sku )
    } else if ( filter.isPartCodeActive( ) ) {
      rawList = service.listarTransaccionesPorArticulo( filter.partCode )
    }
    setItems( rawList )
  }

  public void setItems( List<TransInv> pRawList ) {
    super.setItems( pRawList )
    Collections.sort( dataset )
  }
  
}
