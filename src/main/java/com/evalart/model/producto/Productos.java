package com.evalart.model.producto;

import com.evalart.model.sucursal.Sucursales;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity(name = "producto")
public class Productos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private Integer stock;

    @ManyToOne
    @JoinColumn(name = "sucursal_id")
    @JsonBackReference
    private Sucursales sucursales;

}
