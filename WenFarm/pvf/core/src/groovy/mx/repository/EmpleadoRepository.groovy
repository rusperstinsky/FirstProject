package mx.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QueryDslPredicateExecutor

import model.Empleado

interface EmpleadoRepository extends JpaRepository<Empleado, String>, QueryDslPredicateExecutor<Empleado> {

  Empleado findByIdAndPasswd( String id, String password )

  Empleado findById( String id )

}
