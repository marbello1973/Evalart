package com.evalart.model.franquisia;

import com.evalart.model.sucursal.SucursalDTO;

import java.util.List;

public record FranquisiaDTO(
        Long id,
        String nombre,
        List<SucursalDTO> sucursalDTO
) {}
