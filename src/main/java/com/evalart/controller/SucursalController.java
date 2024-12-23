package com.evalart.controller;

import com.evalart.model.franquicia.FranquiciaRepository;
import com.evalart.model.sucursal.SucursalesRepository;
import com.evalart.model.producto.ProductoRepository;
import com.evalart.model.franquicia.Franquicia;
import com.evalart.model.sucursal.Sucursales;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

@RestController
@RequestMapping("/sucursal")
public class SucursalController {

    private final SucursalesRepository repository;
    private final FranquiciaRepository franquiciaRepository;
    private final ProductoRepository productoRepository;

    public SucursalController
            (
                SucursalesRepository repository,
                FranquiciaRepository franquiciaRepository,
                ProductoRepository productoRepository
            )
            {
                this.repository = repository;
                this.franquiciaRepository = franquiciaRepository;
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
        Optional<Franquicia> franquisiaId = franquiciaRepository.findById(id);
        if (franquisiaId.isPresent()){
            Franquicia franquicia = franquisiaId.get();
            return Optional.of(sucursal)
                    .map(sucursales -> {
                        sucursales.setFranquicia(franquicia);
                        Sucursales newSucursal = repository.save(sucursales);
                        franquicia.getSucursales().add(newSucursal);
                        franquiciaRepository.save(franquicia);
                        return ResponseEntity.status(HttpStatus.CREATED).body(newSucursal);
                    }).orElseGet(() -> ResponseEntity.notFound().build());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Endpoint para actualizar una sucursal dentro de una franquisia específica
    @PutMapping("/update/frq/{franquisiaId}/sucursal/{sucursalId}")
    public ResponseEntity<?> updateSucursal
            (
                    @PathVariable Long franquisiaId,
                    @PathVariable Long sucursalId,
                    @RequestBody Sucursales sucursal
            )
    {
        Optional<Franquicia> franquiciaOptional = franquiciaRepository.findById(franquisiaId);
        if(franquiciaOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Optional<Sucursales> sucursales = repository.findById(sucursalId);
        if(sucursales.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        Sucursales sucursalUpdate = sucursales.get();
        if (!sucursalUpdate.getFranquicia().getId().equals(franquisiaId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        sucursalUpdate.setNombre(sucursal.getNombre());
        Sucursales update = repository.save(sucursalUpdate);
        return ResponseEntity.status(HttpStatus.OK).body(update);
    }

    //Solo para pruebas
    @GetMapping("/getSucursal/{id}")
    public ResponseEntity<Sucursales> getSucursal(@PathVariable Long id) {
        Optional<Sucursales> sucursal = repository.findById(id);
        if (!sucursal.isPresent()) ResponseEntity.notFound().build();
        return ResponseEntity.ok(sucursal.get());
    }
}

