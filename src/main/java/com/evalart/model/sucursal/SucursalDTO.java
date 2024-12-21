package com.evalart.model.sucursal;

import com.evalart.model.producto.ProductoDTO;

import java.util.List;

public record SucursalDTO(
        Long id,
        String nombre,
        List<ProductoDTO> productoDTO
){}
