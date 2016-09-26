package mx.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.querydsl.QueryDslPredicateExecutor

import model.Sucursal

interface SucursalRepository extends JpaRepository<Sucursal, Integer>, QueryDslPredicateExecutor<Sucursal> {

  @Query( value = "SELECT esta_sucursal()", nativeQuery = true )
  Integer getCurrentSucursalId( )



}
