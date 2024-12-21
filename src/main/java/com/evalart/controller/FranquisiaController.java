package com.evalart.controller;

import com.evalart.model.franquisia.FranquisiaRepository;
import com.evalart.model.franquisia.FranquisiaDTO;
import com.evalart.model.franquisia.Franquisia;
import com.evalart.model.sucursal.Sucursales;
import com.evalart.model.producto.Productos;

import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/franquisia")
public class FranquisiaController {

    private final FranquisiaRepository repository;

    public FranquisiaController
            (
                FranquisiaRepository repository
            )
    {
        this.repository = repository;
    }

    // Endpoint para agregar una franquisia
    @PostMapping("/add")
    public ResponseEntity<Franquisia> addFranquisia(@RequestBody Franquisia franquisia) {

        List<Sucursales> sucursalesList = Optional.ofNullable(franquisia.getSucursales())
                .orElse(List.of());

        sucursalesList = sucursalesList.stream()
                .map(sucursal -> {
                    List<Productos> productosList = Optional.ofNullable(sucursal.getProducto())
                            .orElse(List.of());
                    productosList.forEach(producto -> producto.setSucursales(sucursal));
                    sucursal.setFranquisia(franquisia);
                    return sucursal;
                }).collect(Collectors.toList());

        franquisia.setSucursales(sucursalesList);

        return ResponseEntity.ok(repository.save(franquisia));
    }


    // Endpoint para obtener todas las franquisias
    @GetMapping("/all")
    public ResponseEntity<List<Franquisia>> getAllFranquisias(Pageable pageable) {

        Page<Franquisia> franquisias = repository
                .findAll(pageable);

        return ResponseEntity.ok()
                .body(franquisias.getContent());
    }

    // Endpoint para actualizar o cambiar de nombre una franquisia por id
    @PutMapping("/update/{id}")
    public ResponseEntity<Franquisia> updateFranquisia
            (
                @PathVariable Long id,
                @RequestBody FranquisiaDTO franquisiaDTO
            )
    {
        Franquisia franquisia = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Franquisia no encontrada"));

        franquisia.setNombre(franquisiaDTO.nombre());
        return ResponseEntity.ok(repository.save(franquisia));
    }
}
