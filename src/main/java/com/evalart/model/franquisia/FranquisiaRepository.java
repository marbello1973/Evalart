package com.evalart.model.franquisia;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import java.util.Optional;


@Repository
public interface FranquisiaRepository extends JpaRepository<Franquisia, Long> {
    Page<Franquisia> findAll(Pageable pageable);

    @Query("""
        SELECT f FROM franquisia f
        JOIN FETCH f.sucursales s
        JOIN FETCH s.producto p
        WHERE f.id = :franquisiaId
        AND s.id = :sucursalId
        AND p.id = :productoId
    """)
    Optional<Franquisia> findFranquisiaWithSucursalesAndProductosById
            (
                @Param("franquisiaId") Long id,
                @Param("sucursalId") Long sucursalId,
                @Param("productoId") Long productoId
            );
}
