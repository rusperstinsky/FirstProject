package mx.service

import model.Empleado

import model.Empleado

interface EmpleadoService {

  Empleado obtenerEmpleado( String id )

  void actualizarPass( Empleado empleado )

}
