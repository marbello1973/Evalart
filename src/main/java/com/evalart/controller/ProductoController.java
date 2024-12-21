package com.evalart.controller;

import com.evalart.model.franquisia.FranquisiaRepository;
import com.evalart.model.sucursal.SucursalesRepository;
import com.evalart.model.producto.ProductoRepository;
import com.evalart.model.franquisia.Franquisia;
import com.evalart.model.producto.Productos;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.Optional;
import java.util.List;

@RestController
@RequestMapping("/producto")
public class ProductoController {

    private final SucursalesRepository sucursalesRepository;
    private final FranquisiaRepository franquisiaRepository;
    private final ProductoRepository repository;

    public ProductoController
            (
                    SucursalesRepository sucursalesRepository,
                    FranquisiaRepository franquisiaRepository,
                    ProductoRepository repository
            )
    {
        this.sucursalesRepository = sucursalesRepository;
        this.repository = repository;
        this.franquisiaRepository = franquisiaRepository;
    }

    //Exponer endpoint que permita agregar un producto a una sucursal de una franquicia.
    @PostMapping("/add/frq/{franquisiaId}/suc/{sucursalId}")
    public ResponseEntity<Productos> addProducto
            (
                    @PathVariable Long franquisiaId,
                    @PathVariable Long sucursalId,
                    @RequestBody Productos producto
            )
    {
        Optional<Franquisia> franquisiaOptional = franquisiaRepository.findById(franquisiaId);
        if (franquisiaOptional.isPresent()) {
            Franquisia franquisia = franquisiaOptional.get();
            return franquisia.getSucursales()
                    .stream()
                    .filter(sucursales -> sucursales.getId().equals(sucursalId))
                    .findFirst()
                    .map(sucursales -> {
                        producto.setSucursales(sucursales);
                        Productos newProducto = repository.save(producto);
                        sucursales.getProducto().add(newProducto);
                        sucursalesRepository.save(sucursales);
                        return ResponseEntity.ok(newProducto);
                    }).orElseGet(() -> ResponseEntity.status(HttpStatus.NO_CONTENT).build());
        } else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }

    //Exponer endpoint que permita actualizar un producto de una sucursal de una franquicia.
    @PutMapping("/update/frq/{franquisiaId}/suc/{sucursalId}/prod/{productoId}")
    public ResponseEntity<Object> updateProductoInsucursal
            (
                    @PathVariable Long franquisiaId,
                    @PathVariable Long sucursalId,
                    @PathVariable Long productoId,
                    @RequestBody Productos producto
            )
    {
        Optional<Franquisia> franquisiaOptional = franquisiaRepository.findById(franquisiaId);
        if(franquisiaOptional.isPresent()){
            Franquisia franquisia = franquisiaOptional.get();
            return franquisia.getSucursales()
                    .stream()
                    .filter(sucursales -> sucursales.getId().equals(sucursalId))
                    .findFirst()
                    .map(sucursales -> {
                        boolean productoUpdated = sucursales.getProducto()
                                .stream()
                                .filter(productos -> productos.getId().equals(productoId))
                                .findFirst()
                                .map(productos -> {
                                    productos.setNombre(producto.getNombre());
                                    productos.setStock(producto.getStock());
                                    repository.save(productos);
                                    return true;
                                }).orElse(false);
                        if (productoUpdated) {
                            return ResponseEntity.status(HttpStatus.OK).build();
                        } else {
                            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
                        }
                    }).orElseGet(() -> ResponseEntity.status(HttpStatus.NO_CONTENT).build());
        }

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    //Exponer endpoint que permita eliminar un producto de una sucursal de una franquicia.
    @DeleteMapping("/delete/frq/{franquisiaId}/suc/{sucursalId}/prod/{productoId}")
    public ResponseEntity<Object> deleteProductoFromSucursal
            (
                @PathVariable Long franquisiaId,
                @PathVariable Long sucursalId,
                @PathVariable Long productoId
            )
    {
        Optional<Franquisia> franquisiaOptional = franquisiaRepository.findById(franquisiaId);
        if (franquisiaOptional.isPresent()) {
            Franquisia franquisia = franquisiaOptional.get();
            return franquisia.getSucursales()
                    .stream()
                    .filter(sucursales -> sucursales.getId().equals(sucursalId))
                    .findFirst()
                    .map(sucursales -> {
                        boolean productoRemoved = sucursales.getProducto()
                                .removeIf(productos -> productos.getId().equals(productoId));
                        if (productoRemoved) {
                            sucursalesRepository.save(sucursales);
                            return ResponseEntity.status(HttpStatus.OK).build();
                        } else {
                            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
                        }
                    }).orElseGet(() -> ResponseEntity.status(HttpStatus.NO_CONTENT).build());
        } else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }

    //Exponer endpoint que permita mostrar cual es el producto que más stock tiene por sucursal para una
    //franquicia puntual. Debe retornar un listado de productos que indique a que sucursal pertenece.

    @GetMapping("/prodstock/frq/{franquisiaId}")
    public ResponseEntity<List<Productos>> getProductosBySucursalId
            (
                    @PathVariable Long franquisiaId

            )
    {
        List<Productos> productos = repository.findBySucursalIdOrderByStockDesc(franquisiaId);

        if (productos.isEmpty()) ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();

        return ResponseEntity.ok(productos);
    }

}

