package com.evalart.model.franquicia;

import com.evalart.model.sucursal.SucursalDTO;

import java.util.List;

public record FranquiciaDTO(
        Long id,
        String nombre,
        List<SucursalDTO> sucursalDTO
) {}
