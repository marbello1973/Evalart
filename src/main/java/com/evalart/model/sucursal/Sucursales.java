package com.evalart.model.sucursal;

import com.evalart.model.franquisia.Franquisia;
import com.evalart.model.producto.Productos;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity(name = "sucursales")
public class Sucursales {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;

    @ManyToOne
    @JoinColumn(name = "franquisia_id")
    @JsonBackReference
    private Franquisia franquisia;

    @OneToMany(mappedBy = "sucursales", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Productos> producto;
}
