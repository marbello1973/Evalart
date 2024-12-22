package com.evalart.model.franquicia;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import java.util.Optional;


@Repository
public interface FranquiciaRepository extends JpaRepository<Franquicia, Long> {
    Page<Franquicia> findAll(Pageable pageable);

    @Query("""
        SELECT f FROM franquicia f
        JOIN FETCH f.sucursales s
        JOIN FETCH s.producto p
        WHERE f.id = :franquiciaId
        AND s.id = :sucursalId
        AND p.id = :productoId
    """)
    Optional<Franquicia> findFranquisiaWithSucursalesAndProductosById
            (
                @Param("franquiciaId") Long id,
                @Param("sucursalId") Long sucursalId,
                @Param("productoId") Long productoId
            );
}
