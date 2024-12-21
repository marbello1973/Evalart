package com.evalart.controller;

import com.evalart.model.franquisia.Franquisia;
import com.evalart.model.franquisia.FranquisiaRepository;
import com.evalart.model.producto.Productos;
import com.evalart.model.sucursal.Sucursales;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


public class FranquisiaControllerTests {

    @Mock
    private FranquisiaRepository mockFranquiciaRepository;

    private FranquisiaController franquisiaController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockFranquiciaRepository = mock(FranquisiaRepository.class);
        franquisiaController = new FranquisiaController(mockFranquiciaRepository);
    }

    @Test
    public void testAddFranquisia() {
        Franquisia franquisiaEsperada = new Franquisia();
        franquisiaEsperada.setId(1L);
        franquisiaEsperada.setNombre("McDonald's");
        franquisiaEsperada.setSucursales(List.of());

        when(mockFranquiciaRepository.save(any(Franquisia.class))).thenReturn(franquisiaEsperada);

        Franquisia franquisiaResultado = new Franquisia();
        franquisiaResultado.setNombre("McDonald's");


        ResponseEntity<Franquisia> result = franquisiaController.addFranquisia(franquisiaResultado);

        assertEquals(200, result.getStatusCodeValue());

        Franquisia franquisiaBody = result.getBody();
        assertNotNull(franquisiaBody);
        assertEquals(franquisiaEsperada.getId(), franquisiaBody.getId());
        assertEquals(franquisiaEsperada.getNombre(), franquisiaResultado.getNombre());
        assertEquals(franquisiaEsperada.getSucursales().isEmpty(), franquisiaBody.getSucursales().isEmpty());

        verify(mockFranquiciaRepository, times(1)).save(any(Franquisia.class));
        System.out.println("Test1 passed");
    }


    @Test
    public void testGetAllFranquisias() {
        Productos producto = new Productos(1L, "producto 1", 1000.0);
        Sucursales sucursal = new Sucursales(1l, "Sucursal 1", List.of(producto));
        Franquisia franquiciaEsperada = new Franquisia(1L, "McDonald's", List.of(sucursal));

        Page<Franquisia> pageMock = Mockito.mock(Page.class);
        when(pageMock.getContent()).thenReturn(List.of(franquiciaEsperada));

        when(mockFranquiciaRepository.findAll(any(Pageable.class))).thenReturn(pageMock);

        Pageable pageable = PageRequest.of(0, 10);
        ResponseEntity<Page<Franquisia>> responseEntity = franquisiaController.getAllFranquisias(pageable);

        assertNotNull(responseEntity);
        Page<Franquisia> franquiciaPage = responseEntity.getBody();
        assertNotNull(franquiciaPage);
        assertEquals(1, franquiciaPage.getContent().size());
        Franquisia franquisiaRsult = franquiciaPage.getContent().get(0);
        assertEquals(1l, franquisiaRsult.getId());
        assertEquals("McDonald's", franquisiaRsult.getNombre());

        verify(mockFranquiciaRepository, times(1)).findAll(any(Pageable.class));

        System.out.println("Test2 passed");
    }



}
