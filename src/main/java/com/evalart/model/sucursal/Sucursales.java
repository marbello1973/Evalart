package com.evalart.model.sucursal;

import com.evalart.model.franquicia.Franquicia;
import com.evalart.model.producto.Productos;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity(name = "sucursales")
public class Sucursales {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;

    @ManyToOne
    @JoinColumn(name = "franquicia_id")
    @JsonBackReference
    private Franquicia franquicia;

    @OneToMany(mappedBy = "sucursales", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Productos> producto;

    public Sucursales() { }

    public <E> Sucursales(long l, String s, List<Productos> producto) {
        this.id = l;
        this.nombre = s;
        this.producto = producto != null ? new ArrayList<>(producto) : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Productos> getProducto() { return producto; }

    public void setProducto(List<Productos> producto) {
        this.producto = producto;
    }

    public Franquicia getFranquicia() {
        return franquicia;
    }

    public void setFranquicia(Franquicia franquicia) {
        this.franquicia = franquicia;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return "Sucursales{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", franquicia=" + franquicia +
                ", producto=" + producto +
                '}';
    }
}
