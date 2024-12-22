package com.evalart.controller;

import com.evalart.model.franquicia.Franquicia;
import com.evalart.model.franquicia.FranquiciaDTO;
import com.evalart.model.franquicia.FranquiciaRepository;
import com.evalart.model.producto.Productos;
import com.evalart.model.sucursal.Sucursales;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class FranquiciaControllerTests {

    @Mock
    private FranquiciaRepository mockFranquiciaRepository;

    private FranquisiaController franquisiaController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockFranquiciaRepository = mock(FranquiciaRepository.class);
        franquisiaController = new FranquisiaController(mockFranquiciaRepository);
        mockMvc = MockMvcBuilders.standaloneSetup(franquisiaController).build();
    }

    @Test
    public void testAddFranquisia() {
        Franquicia franquiciaEsperada = new Franquicia();
        franquiciaEsperada.setId(1L);
        franquiciaEsperada.setNombre("McDonald's");
        franquiciaEsperada.setSucursales(List.of());

        when(mockFranquiciaRepository.save(any(Franquicia.class))).thenReturn(franquiciaEsperada);

        Franquicia franquiciaResultado = new Franquicia();
        franquiciaResultado.setNombre("McDonald's");


        ResponseEntity<Franquicia> result = franquisiaController.addFranquisia(franquiciaResultado);

        assertEquals(200, result.getStatusCodeValue());

        Franquicia franquiciaBody = result.getBody();
        assertNotNull(franquiciaBody);
        assertEquals(franquiciaEsperada.getId(), franquiciaBody.getId());
        assertEquals(franquiciaEsperada.getNombre(), franquiciaResultado.getNombre());
        assertEquals(franquiciaEsperada.getSucursales().isEmpty(), franquiciaBody.getSucursales().isEmpty());

        verify(mockFranquiciaRepository, times(1)).save(any(Franquicia.class));
        System.out.println("Test1 passed");
    }


    @Test
    public void testGetAllFranquisias() {
        Productos producto = new Productos(1L, "producto 1", 1000.0);
        Sucursales sucursal = new Sucursales(1l, "Sucursal 1", List.of(producto));
        Franquicia franquiciaEsperada = new Franquicia(1L, "McDonald's", List.of(sucursal));

        Page<Franquicia> pageMock = Mockito.mock(Page.class);
        when(pageMock.getContent()).thenReturn(List.of(franquiciaEsperada));

        when(mockFranquiciaRepository.findAll(any(Pageable.class))).thenReturn(pageMock);

        Pageable pageable = PageRequest.of(0, 10);
        ResponseEntity<Page<Franquicia>> responseEntity = franquisiaController.getAllFranquisias(pageable);

        assertNotNull(responseEntity);
        Page<Franquicia> franquiciaPage = responseEntity.getBody();
        assertNotNull(franquiciaPage);
        assertEquals(1, franquiciaPage.getContent().size());
        Franquicia franquiciaRsult = franquiciaPage.getContent().get(0);
        assertEquals(1l, franquiciaRsult.getId());
        assertEquals("McDonald's", franquiciaRsult.getNombre());

        verify(mockFranquiciaRepository, times(1)).findAll(any(Pageable.class));

        System.out.println("Test2 passed");
    }

    @Test
    public void testUpdateFranquicia() throws Exception {
        Franquicia franquiciaExiste = new Franquicia(1L, "McDonald's", List.of());
        when(mockFranquiciaRepository.findById(1L)).thenReturn(java.util.Optional.of(franquiciaExiste));

        FranquiciaDTO franquiciaDTO = new FranquiciaDTO(1L, "McDonald's", List.of());
        when(mockFranquiciaRepository.save(any(Franquicia.class))).thenReturn(franquiciaExiste);

        mockMvc.perform(put("/franquicia/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\": \"McDonald's\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value(franquiciaDTO.nombre()));
        verify(mockFranquiciaRepository, times(1)).save(franquiciaExiste);
        System.out.println("Test3 passed");
    }

}
