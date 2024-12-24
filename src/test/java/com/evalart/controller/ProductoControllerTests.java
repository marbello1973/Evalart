package com.evalart.controller;

import com.evalart.model.sucursal.SucursalesRepository;
import com.evalart.model.franquicia.FranquiciaRepository;
import com.evalart.model.producto.ProductoRepository;
import com.evalart.model.franquicia.Franquicia;
import com.evalart.model.sucursal.Sucursales;
import com.evalart.model.producto.Productos;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductoControllerTests {

    @MockitoBean
    private ProductoRepository mockProductoRepository;

    @MockitoBean
    private FranquiciaRepository mockFranquiciaRepository;

    @MockitoBean
    private SucursalesRepository mockSucursalesRepository;

    @InjectMocks
    private ProductoController productoController;


    @BeforeEach
    void setUp() {

        mockProductoRepository = mock(ProductoRepository.class);
        mockFranquiciaRepository = mock(FranquiciaRepository.class);
        mockSucursalesRepository = mock(SucursalesRepository.class);

        productoController = new ProductoController(
                mockSucursalesRepository,
                mockFranquiciaRepository,
                mockProductoRepository
        );

        Franquicia franquicia = new Franquicia();
        franquicia.setId(1L);
        Sucursales sucursales = new Sucursales();
        sucursales.setId(1L);
        Productos productos = new Productos();
        productos.setId(1L);
        sucursales.setProducto(new ArrayList<>());
        franquicia.setSucursales(new ArrayList<>(List.of(sucursales)));
        when(mockFranquiciaRepository.findById(1L)).thenReturn(java.util.Optional.of(franquicia));
        when(mockSucursalesRepository.findById(1L)).thenReturn(java.util.Optional.of(sucursales));
        when(mockProductoRepository.findById(1L)).thenReturn(java.util.Optional.of(productos));

    }

    @Test
    public void testAddProducto(){
        Productos productos = new Productos(1L, "Producto 1", 1589, null);
        when(mockProductoRepository.save(any(Productos.class))).thenReturn(productos);

        Productos productosResul = new Productos();
        productosResul.setNombre("Producto 1");
        productosResul.setStock(130);

        ResponseEntity<Productos> response = productoController.addProducto(1L, 1L, productosResul);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        Productos productosBody = response.getBody();
        assertNotNull(productosBody);
        assertEquals(productos.getId(), productosBody.getId());
        assertEquals(productos.getNombre(), productosBody.getNombre());
        assertEquals(productos.getStock(), productosBody.getStock());

        verify(mockProductoRepository, times(1)).save(any(Productos.class));
        System.out.println("Test1 passed");
    }

    @Test
    public void testUpdateProductoInsucursal(){
        Productos productosExistentes = new Productos();
        productosExistentes.setId(1L);
        productosExistentes.setNombre("Producto 1");
        productosExistentes.setStock(1000);

        Sucursales sucursalExistentes = new Sucursales();
        sucursalExistentes.setId(1L);
        sucursalExistentes.setProducto(List.of(productosExistentes));

        Franquicia franquiciaExistente = new Franquicia();
        franquiciaExistente.setId(1L);
        franquiciaExistente.setNombre("McDonald's");
        franquiciaExistente.setSucursales(List.of(sucursalExistentes));

        when(mockFranquiciaRepository.findById(1L)).thenReturn(Optional.of(franquiciaExistente));

        Productos newProductoActualizar = new Productos();
        newProductoActualizar.setNombre("Nuevo nombre de prodcuto");
        newProductoActualizar.setStock(10001);

        ResponseEntity<?> response = productoController.updateProductoInsucursal(1L, 1L, 1L, newProductoActualizar);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        Productos productosBody = (Productos) response.getBody();
        assertNotNull(productosBody);
        assertEquals(1L, productosBody.getId());
        assertEquals("Nuevo nombre de prodcuto", productosBody.getNombre());
        assertEquals(10001, productosBody.getStock());

        verify(mockProductoRepository, times(1)).save(any(Productos.class));
        System.out.println("Test2 passed");

    }

    @Test
    public void testDeleteProductoFromSucursal() {

        //Creamos los objetos necesarios para el test
        Productos nuevoProducto = new Productos(1L, "Producto Name", 150, null);
        Sucursales nuevaSucursal = new Sucursales(1L, "Sucursal 1", List.of(nuevoProducto));
        Franquicia nuevaFranquicia = new Franquicia(1L, "McDonald's", List.of(nuevaSucursal));

        //buscamos los objetos en la base de datos
        when(mockFranquiciaRepository.findById(1L)).thenReturn(Optional.of(nuevaFranquicia));
        when(mockSucursalesRepository.findById(1L)).thenReturn(Optional.of(nuevaSucursal));
        when(mockProductoRepository.findById(1L)).thenReturn(Optional.of(nuevoProducto));

        //Invocamos al controlador
        ResponseEntity<Productos> response = productoController.deleteProductoFromSucursal(1L, 1L, 1L);

        //Verifico la respuesta
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        //Verifico que se haya llamado al metodo deleteById y que el producto se haya eliminado
        verify(mockProductoRepository, times(1)).deleteById(1L);

        Optional<Productos> productosEliminado = nuevaSucursal.getProducto()
                .stream()
                .filter(p -> p.getId() == 1L)
                .findFirst();
        assertTrue(productosEliminado.isEmpty());
    }


    @Test
    public void testGetProductoConMasStockPorSucursal() {
        Productos producto0 = new Productos(1L, "Producto 1", 680, null);
        Productos producto1 = new Productos(1L, "Producto 1", 100, null);
        Productos producto2 = new Productos(2L, "Producto 2", 200, null);
        Productos producto3 = new Productos(3L, "Producto 3", 300, null);
        List<Productos> productos = List.of(producto0, producto1, producto2, producto3);

        when(mockProductoRepository.findBySucursalIdOrderByStockDesc(1L))
                .thenReturn(productos);

        ResponseEntity<List<Productos>> response = productoController.getProductoConMasStockPorSucursal(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        List<Productos> productoConMasStock = response.getBody();
        assertNotNull(productoConMasStock);
        assertFalse(productoConMasStock.isEmpty());

        assertEquals(productos.getFirst().getId(), productoConMasStock.getFirst().getId());
        assertEquals(productos.getFirst().getNombre(), productoConMasStock.getFirst().getNombre());
        assertEquals(productos.getFirst().getStock(), productoConMasStock.getFirst().getStock());

        System.out.println("Test3 passed");
    }

    @Test
    public void testGetProductoConMasStockPorSucursal_NoProductsFound() {
        when(mockFranquiciaRepository.findById(1L)).thenReturn(Optional.empty());
        ResponseEntity<List<Productos>> response = productoController.getProductoConMasStockPorSucursal(1L);
        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

}



