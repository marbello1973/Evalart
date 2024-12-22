package com.evalart.controller;

import com.evalart.model.franquicia.Franquicia;
import com.evalart.model.franquicia.FranquiciaRepository;
import com.evalart.model.sucursal.Sucursales;
import com.evalart.model.sucursal.SucursalesRepository;
import com.evalart.model.producto.ProductoRepository;
import com.evalart.model.producto.Productos;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.Optional;
import java.util.List;

@RestController
@RequestMapping("/producto")
public class ProductoController {

    private final SucursalesRepository sucursalesRepository;
    private final FranquiciaRepository franquiciaRepository;
    private final ProductoRepository repository;

    public ProductoController
            (
                    SucursalesRepository sucursalesRepository,
                    FranquiciaRepository franquiciaRepository,
                    ProductoRepository repository
            )
    {
        this.sucursalesRepository = sucursalesRepository;
        this.repository = repository;
        this.franquiciaRepository = franquiciaRepository;
    }

    //Exponer endpoint que permita agregar un producto a una sucursal de una franquicia.
    @PostMapping("/add/frq/{franquiciaId}/suc/{sucursalId}")
    public ResponseEntity<Productos> addProducto
            (
                    @PathVariable Long franquiciaId,
                    @PathVariable Long sucursalId,
                    @RequestBody Productos producto
            )
    {
        Optional<Franquicia> franquiciaOptional = franquiciaRepository.findById(franquiciaId);

        if (franquiciaOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        Franquicia franquicia = franquiciaOptional.get();

        Optional<Sucursales> sucursalesOptional = franquicia.getSucursales()
                .stream()
                .filter(sucursales -> sucursales.getId().equals(sucursalId))
                .findFirst();

        if (sucursalesOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        Sucursales sucursales = sucursalesOptional.get();

        producto.setSucursales(sucursales);
        Productos newProducto = repository.save(producto);

        sucursales.getProducto().add(newProducto);
        sucursalesRepository.save(sucursales);

        return ResponseEntity.status(HttpStatus.OK).body(newProducto);
    }

    //Exponer endpoint que permita actualizar un producto de una sucursal de una franquicia.
    @PutMapping("/update/frq/{franquiciaId}/suc/{sucursalId}/prod/{productoId}")
    public ResponseEntity<?> updateProductoInsucursal
            (
                    @PathVariable Long franquiciaId,
                    @PathVariable Long sucursalId,
                    @PathVariable Long productoId,
                    @RequestBody Productos producto
            )
    {
        Optional<Franquicia> franquiciaOptional = franquiciaRepository.findById(franquiciaId);
        if(franquiciaOptional.isPresent()){
            Franquicia franquicia = franquiciaOptional.get();
            return franquicia.getSucursales()
                    .stream()
                    .filter(sucursales -> sucursales.getId().equals(sucursalId))
                    .findFirst()
                    .map(sucursales -> {
                        Productos productoUpdated = sucursales.getProducto()
                                .stream()
                                .filter(productos -> productos.getId().equals(productoId))
                                .findFirst()
                                .map(productos -> {
                                    productos.setNombre(producto.getNombre());
                                    productos.setStock(producto.getStock());
                                    repository.save(productos);
                                    return productos;
                                }).orElse(null);
                        if (productoUpdated != null) {
                            return ResponseEntity.status(HttpStatus.OK).body(productoUpdated);
                        } else {
                            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
                        }
                    }).orElseGet(() -> ResponseEntity.status(HttpStatus.NO_CONTENT).build());
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    //Exponer endpoint que permita eliminar un producto de una sucursal de una franquicia.
    @DeleteMapping("/delete/frq/{franquiciaId}/suc/{sucursalId}/prod/{productoId}")
    public ResponseEntity<Productos> deleteProductoFromSucursal
            (
                @PathVariable Long franquiciaId,
                @PathVariable Long sucursalId,
                @PathVariable Long productoId
            )
    {
        Optional<Franquicia> franquiciaOptional = franquiciaRepository.findById(franquiciaId);
        if (franquiciaOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Franquicia franquicia = franquiciaOptional.get();

        Optional<Sucursales> sucursalOptional = franquicia.getSucursales().stream()
                .filter(s -> s.getId().equals(sucursalId))
                .findFirst();

        if (sucursalOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Sucursales sucursal = sucursalOptional.get();
        Optional<Productos> productoOptional = sucursal.getProducto().stream()
                .filter(p -> p.getId().equals(productoId))
                .findFirst();
        if(productoOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        sucursal.getProducto().remove(productoOptional.get());
        sucursalesRepository.save(sucursal);
        return ResponseEntity.status(HttpStatus.OK).build();

    }

    //Exponer endpoint que permita mostrar cual es el producto que más stock tiene por sucursal para una
    //franquicia puntual. Debe retornar un listado de productos que indique a que sucursal pertenece.

    @GetMapping("/prodstock/frq/{franquisiaId}")
    public ResponseEntity<List<Productos>> getProductosBySucursalId(@PathVariable Long franquisiaId )
    {
        List<Productos> productos = repository.findBySucursalIdOrderByStockDesc(franquisiaId);

        if (productos.isEmpty()) ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();

        return ResponseEntity.ok(productos);
    }

}

