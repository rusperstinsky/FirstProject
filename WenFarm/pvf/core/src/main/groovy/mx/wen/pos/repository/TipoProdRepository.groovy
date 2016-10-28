package mx.wen.pos.repository

import mx.wen.pos.model.Articulo
import mx.wen.pos.model.TipoProd
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.querydsl.QueryDslPredicateExecutor
import org.springframework.transaction.annotation.Transactional

interface TipoProdRepository extends JpaRepository<TipoProd, String>, QueryDslPredicateExecutor<TipoProd> {

  @Transactional
  @Query( value = "SELECT * FROM tipo_prod ORDER BY id_tipo", nativeQuery = true )
  List<TipoProd> findTypesOfItem( )

}
