package com.evalart.controller;

import com.evalart.model.franquicia.FranquiciaRepository;
import com.evalart.model.franquicia.FranquiciaDTO;
import com.evalart.model.franquicia.Franquicia;
import com.evalart.model.sucursal.Sucursales;
import com.evalart.model.producto.Productos;

import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Page;

import java.util.stream.Collectors;
import java.util.Optional;
import java.util.List;

@RestController
@RequestMapping("/franquicia")
public class FranquisiaController {

    private final FranquiciaRepository repository;

    public FranquisiaController(FranquiciaRepository repository)
    {
        this.repository = repository;
    }

    // Endpoint para agregar una franquisia
    @PostMapping("/add")
    public ResponseEntity<Franquicia> addFranquisia(@RequestBody Franquicia franquicia)
    {
        List<Sucursales> sucursalesList = Optional.ofNullable(franquicia.getSucursales())
                .orElse(List.of());

        sucursalesList = sucursalesList.stream()
                .map(sucursal -> {
                    List<Productos> productosList = Optional
                            .ofNullable(sucursal.getProducto())
                            .orElse(List.of());
                    productosList.forEach(producto -> producto.setSucursales(sucursal));
                    sucursal.setFranquicia(franquicia);
                    return sucursal;
                }).collect(Collectors.toList());
        franquicia.setSucursales(sucursalesList);
        return ResponseEntity.ok(repository.save(franquicia));
    }

    // Endpoint para obtener todas las franquisias
    @GetMapping("/all")
    public ResponseEntity<Page<Franquicia>> getAllFranquisias(Pageable pageable)
    {
        Page<Franquicia> franquicias = repository.findAll(pageable);
        return ResponseEntity.ok(franquicias);
    }

    // Endpoint para actualizar o cambiar de nombre una franquicia por id
    @PutMapping("/update/{id}")
    public ResponseEntity<Franquicia> updateFranquicia
            (
                @PathVariable Long id,
                @RequestBody FranquiciaDTO franquiciaDTO
            )
    {
        Franquicia franquicia = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Franquicia no encontrada"));

        franquicia.setNombre(franquiciaDTO.nombre());
        return ResponseEntity.ok(repository.save(franquicia));
    }
}
