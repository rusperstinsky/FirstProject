package mx.wen.pos.ui.view.component

import mx.wen.pos.ui.view.component.NavigationBar.Command

interface NavigationBarListener {

  void requestItem( Command pCommand )
  
  void requestNewSearch( )
  
}
