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

    public <E> Sucursales(long l, String s, List<Productos> producto) { }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Productos> getProducto() {
        return producto;
    }

    public void setProducto(List<Productos> producto) {
        this.producto = producto;
    }

    public Franquisia getFranquisia() {
        return franquisia;
    }

    public void setFranquisia(Franquisia franquisia) {
        this.franquisia = franquisia;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
