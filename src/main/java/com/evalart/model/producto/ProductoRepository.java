package com.evalart.model.producto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Productos, Long> {
    @Query("""
                SELECT p FROM producto p 
                WHERE p.sucursales.franquicia.id = :franquisiaId 
                ORDER BY p.stock DESC
            """)
    List<Productos> findBySucursalIdOrderByStockDesc(@Param("franquisiaId") Long sucursalId);
}
