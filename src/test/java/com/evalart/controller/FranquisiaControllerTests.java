package com.evalart.controller;

import com.evalart.model.franquisia.Franquisia;
import com.evalart.model.franquisia.FranquisiaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class FranquisiaControllerTests {

    @Test
    public void testAddFranquisia() {

        FranquisiaRepository mockRepository = mock(FranquisiaRepository.class);
        Franquisia franquisiaMock= new Franquisia();
        franquisiaMock.setId(1L);
        franquisiaMock.setNombre("McDonald's");
        franquisiaMock.setSucursales(List.of());
        when(mockRepository.save(any(Franquisia.class))).thenReturn(franquisiaMock);

        FranquisiaController franquisiaController = new FranquisiaController(mockRepository);

        //Crear una nueva entrada
        Franquisia franquisia = new Franquisia();
        franquisia.setNombre("McDonald's");

        //llamar al metodo
        ResponseEntity<Franquisia> response = franquisiaController.addFranquisia(franquisia);

        //verificar que la respuesta no sea nula
        assertEquals(200, response.getStatusCodeValue());
        Franquisia franquisiaResponse = response.getBody();

        //verificar que la respuesta sea la esperada
        assertNotNull(franquisiaResponse);
        assertEquals(1L, franquisiaResponse.getId());
        assertEquals("McDonald's", franquisiaResponse.getNombre());
        assertNotNull(franquisiaResponse.getSucursales());
        assertTrue(franquisiaResponse.getSucursales().isEmpty());

        //verificar que el metodo save fue llamado
        verify(mockRepository, times(1)).save(any(Franquisia.class));

    }

}
