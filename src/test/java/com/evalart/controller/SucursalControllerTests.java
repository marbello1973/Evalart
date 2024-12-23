package com.evalart.controller;

import com.evalart.model.franquicia.Franquicia;
import com.evalart.model.franquicia.FranquiciaRepository;
import com.evalart.model.sucursal.Sucursales;
import com.evalart.model.sucursal.SucursalesRepository;

import org.junit.jupiter.api.Test;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;


import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SucursalControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SucursalesRepository sucursalesRepository;

    @MockitoBean
    private FranquiciaRepository franquiciaRepository;

    @Autowired
    private SucursalController sucursalController;

    @Test
    public void testAddSucursal() throws Exception {

        Sucursales nuevaSucursal = new Sucursales(1L, "Sucursal Santa Marta", null);
        Franquicia franquiciaMock = new Franquicia(1L, "Unilever", new ArrayList<>());

        when(franquiciaRepository.findById(1L)).thenReturn(Optional.of(franquiciaMock));
        when(sucursalesRepository.save(any(Sucursales.class))).thenReturn(nuevaSucursal);

        mockMvc.perform(post("/sucursal/add/frq/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"id\": 1, \"nombre\": \"Sucursal Santa Marta\", \"franquicia\": null }"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Sucursal Santa Marta"))
                .andDo(print());
    }

    @Test
    public void testUpdateSucursal() throws Exception {

        Sucursales sucursal = new Sucursales(1L, "Sucursal Cali", null);
        Franquicia franquiciaMock = new Franquicia(1L, "Unilever", new ArrayList<>());
        sucursal.setFranquicia(franquiciaMock);

        when(franquiciaRepository.findById(1L)).thenReturn(Optional.of(franquiciaMock));
        when(sucursalesRepository.findById(1L)).thenReturn(Optional.of(sucursal));
        when(sucursalesRepository.save(any(Sucursales.class))).thenReturn(sucursal);

        mockMvc.perform(put("/sucursal/update/frq/1/sucursal/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"id\": 1, \"nombre\": \"Sucursal Cali\", \"franquicia\": null }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Sucursal Cali"))
                .andDo(print());
    }


}


























