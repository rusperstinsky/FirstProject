package mx.wen.pos.service

import mx.wen.pos.model.Empleado


interface EmpleadoService {

  Empleado obtenerEmpleado( String id )

  String gerente( )

  void actualizarPass( Empleado empleado )

  Empleado insertaEmpleado( String id, String pass, String name )

}
