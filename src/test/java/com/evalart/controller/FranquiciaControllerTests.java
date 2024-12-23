package com.evalart.controller;

import com.evalart.model.franquicia.Franquicia;
import com.evalart.model.franquicia.FranquiciaRepository;
import com.evalart.model.producto.Productos;
import com.evalart.model.sucursal.Sucursales;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.mockito.MockitoAnnotations;
import org.mockito.Mockito;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@AutoConfigureMockMvc
public class FranquiciaControllerTests {

    @MockitoBean
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

        assertEquals(HttpStatus.OK, result.getStatusCode());

        Franquicia franquiciaBody = result.getBody();
        assertNotNull(franquiciaBody);
        assertEquals(franquiciaEsperada.getId(), franquiciaBody.getId());
        assertEquals(franquiciaEsperada.getNombre(), franquiciaResultado.getNombre());
        assertEquals(franquiciaEsperada.getSucursales().isEmpty(), franquiciaBody.getSucursales().isEmpty());

        verify(mockFranquiciaRepository, times(1)).save(any(Franquicia.class));

    }


    @Test
    public void testGetAllFranquisias() {
        Productos producto = new Productos(1L, "producto 1", 100, null);
        Sucursales sucursal = new Sucursales(1L, "Sucursal 1", List.of(producto));
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
        Franquicia franquiciaResult = franquiciaPage.getContent().getFirst();
        assertEquals(1L, franquiciaResult.getId());
        assertEquals("McDonald's", franquiciaResult.getNombre());

        verify(mockFranquiciaRepository, times(1)).findAll(any(Pageable.class));

    }

    @Test
    public void testUpdateFranquicia() throws Exception {
        Franquicia franquiciaExiste = new Franquicia(1L, "McDonald's", List.of());
        when(mockFranquiciaRepository.findById(1L)).thenReturn(Optional.of(franquiciaExiste));

        Franquicia franquicia = new Franquicia(1L, "McDonald's", List.of());
        when(mockFranquiciaRepository.save(any(Franquicia.class))).thenReturn(franquiciaExiste);

        mockMvc.perform(put("/franquicia/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\": \"McDonald's\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value(franquicia.getNombre()));
        verify(mockFranquiciaRepository, times(1)).save(franquiciaExiste);

    }

}
