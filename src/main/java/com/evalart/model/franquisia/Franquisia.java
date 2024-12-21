package com.evalart.model.franquisia;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import com.evalart.model.sucursal.Sucursales;

import jakarta.persistence.*;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity(name = "franquisia")
public class Franquisia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;

    @Getter
    @OneToMany(mappedBy = "franquisia", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Sucursales> sucursales;

}
