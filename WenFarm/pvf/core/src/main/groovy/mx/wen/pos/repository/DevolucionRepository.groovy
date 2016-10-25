package mx.wen.pos.repository

import mx.wen.pos.model.Devolucion
//import mx.wen.pos.repository.custom.DevolucionRepositoryCustom
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QueryDslPredicateExecutor

interface DevolucionRepository extends JpaRepository<Devolucion, Integer>, QueryDslPredicateExecutor<Devolucion> {


}
