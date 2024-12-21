package com.evalart.controller;

import com.evalart.model.franquisia.FranquisiaRepository;
import com.evalart.model.sucursal.SucursalesRepository;
import com.evalart.model.producto.ProductoRepository;
import com.evalart.model.franquisia.Franquisia;
import com.evalart.model.sucursal.Sucursales;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

@RestController
@RequestMapping("/sucursal")
public class SucursalController {

    private final SucursalesRepository repository;
    private final FranquisiaRepository franquisiaRepository;
    private final ProductoRepository productoRepository;

    public SucursalController
            (
                SucursalesRepository repository,
                FranquisiaRepository franquisiaRepository,
                ProductoRepository productoRepository
            )
            {
                this.repository = repository;
                this.franquisiaRepository = franquisiaRepository;
                this.productoRepository = productoRepository;
            }

    // Endpoint para agregar una sucursal a una franquisia
    @PostMapping("/add/frq/{id}")
    public ResponseEntity<Sucursales> addSucursal
            (
                    @PathVariable Long id,
                    @RequestBody Sucursales sucursal
            )
    {
        Optional<Franquisia> franquisiaId = franquisiaRepository.findById(id);
        if (franquisiaId.isPresent()){
            Franquisia franquisia = franquisiaId.get();
            return Optional.of(sucursal)
                    .map(sucursales -> {
                        sucursales.setFranquisia(franquisia);
                        Sucursales newSucursal = repository.save(sucursales);
                        franquisia.getSucursales().add(newSucursal);
                        franquisiaRepository.save(franquisia);
                        return ResponseEntity.ok(newSucursal);
                    }).orElseGet(() -> ResponseEntity.notFound().build());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/getSucursal/{id}")
    public ResponseEntity<Sucursales> getSucursal(@PathVariable Long id) {
        Optional<Sucursales> sucursal = repository.findById(id);
        if (!sucursal.isPresent()) ResponseEntity.notFound().build();
        return ResponseEntity.ok(sucursal.get());
    }

    // Endpoint para actualizar una sucursal dentro de una franquisia especifica
    @PutMapping("/update/frq/{franquisiaId}/sucursal/{sucursalId}")
    public ResponseEntity<?> updateSucursal
            (
                    @PathVariable Long franquisiaId,
                    @PathVariable Long sucursalId,
                    @RequestBody Sucursales sucursal
            )
    {
        return franquisiaRepository.findById(franquisiaId)
                .map(_ -> repository.findById(sucursalId)
                        .map(sucursalUpdate -> {
                            if(!sucursalUpdate.getFranquisia().getId().equals(franquisiaId)) {
                                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
                            }
                            sucursalUpdate.setNombre(sucursal.getNombre());
                            return ResponseEntity.ok(repository.save(sucursalUpdate));
                        }).orElseGet(() -> ResponseEntity.status(HttpStatus.NO_CONTENT).build()))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}

