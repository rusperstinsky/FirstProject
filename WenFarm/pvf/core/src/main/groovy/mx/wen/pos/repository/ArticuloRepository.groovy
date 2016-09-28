package mx.wen.pos.repository

import mx.wen.pos.model.Articulo
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QueryDslPredicateExecutor

interface ArticuloRepository extends JpaRepository<Articulo, Integer>, QueryDslPredicateExecutor<Articulo> {

}
