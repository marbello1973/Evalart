package com.evalart.controller;

import com.evalart.model.franquicia.Franquicia;
import com.evalart.model.franquicia.FranquiciaRepository;
import com.evalart.model.producto.ProductoRepository;
import com.evalart.model.producto.Productos;
import com.evalart.model.sucursal.Sucursales;
import com.evalart.model.sucursal.SucursalesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductoControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Mock
    private ProductoRepository mockProductoRepository;

    @Mock
    private FranquiciaRepository mockFranquiciaRepository;

    @Mock
    private SucursalesRepository mockSucursalesRepository;

    @InjectMocks
    private ProductoController productoController;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(productoController).build();

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
        franquicia.setSucursales(new ArrayList<>(Arrays.asList(sucursales)));
        when(mockFranquiciaRepository.findById(1L)).thenReturn(java.util.Optional.of(franquicia));
        when(mockSucursalesRepository.findById(1L)).thenReturn(java.util.Optional.of(sucursales));
        when(mockProductoRepository.findById(1L)).thenReturn(java.util.Optional.of(productos));

    }

    @Test
    public void testAddProducto(){
        Productos productos = new Productos(1L, "Producto 1", 1589 );
        when(mockProductoRepository.save(any(Productos.class))).thenReturn(productos);

        Productos productosResul = new Productos();
        productosResul.setNombre("Producto 1");
        productosResul.setStock(130);

        ResponseEntity<Productos> response = productoController.addProducto(1L, 1L, productosResul);

        assertEquals(200, response.getStatusCodeValue());

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
        sucursalExistentes.setProducto(Arrays.asList(productosExistentes));

        Franquicia franquiciaExistente = new Franquicia();
        franquiciaExistente.setId(1L);
        franquiciaExistente.setNombre("McDonald's");
        franquiciaExistente.setSucursales(Arrays.asList(sucursalExistentes));

        when(mockFranquiciaRepository.findById(1L)).thenReturn(Optional.of(franquiciaExistente));

        Productos newProductoActualizar = new Productos();
        newProductoActualizar.setNombre("Nuevo nombre de prodcuto");
        newProductoActualizar.setStock(10001);

        ResponseEntity<?> response = productoController.updateProductoInsucursal(1L, 1L, 1L, newProductoActualizar);

        assertEquals(200, response.getStatusCodeValue());

        Productos productosBody = (Productos) response.getBody();
        assertNotNull(productosBody);
        assertEquals(1L, productosBody.getId());
        assertEquals("Nuevo nombre de prodcuto", productosBody.getNombre());
        assertEquals(10001, productosBody.getStock());

        verify(mockProductoRepository, times(1)).save(any(Productos.class));
        System.out.println("Test2 passed");

    }

    @Test
    public void testDeleteProductoFromSucursal() throws Exception {
        long productoId = 1L;
        long sucursalId = 1L;
        long franquiciaId = 1L;

        given(mockFranquiciaRepository.findById(franquiciaId))
                .willReturn(Optional.of(new Franquicia(franquiciaId, "McDonald's", new ArrayList<>())));
        given(mockSucursalesRepository.findById(sucursalId))
                .willReturn(Optional.of(new Sucursales(sucursalId, "Sucursal 1", new ArrayList<>())));
        given(mockProductoRepository.existsById(productoId))
                .willReturn(true);

        willDoNothing().given(mockProductoRepository).deleteById(productoId);

        ResultActions result = mockMvc
                .perform(delete("/producto/delete/frq/{franquiciaId}/suc/{sucursalId}/prod/{productoId}",
                        franquiciaId, sucursalId, productoId));

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("Producto eliminado"));
    }

}




