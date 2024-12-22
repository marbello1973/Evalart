package com.evalart.model.sucursal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SucursalesRepository extends JpaRepository<Sucursales, Long> {
}
