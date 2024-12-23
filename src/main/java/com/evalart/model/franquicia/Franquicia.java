package com.evalart.model.franquicia;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.evalart.model.sucursal.Sucursales;
import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Data
@Entity(name = "franquicia")
public class Franquicia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;

    @OneToMany(mappedBy = "franquicia", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Sucursales> sucursales;


    public Franquicia() { }
    public Franquicia(Long id, String nombre, List<Sucursales> sucursales) {
        this.id = id;
        this.nombre = nombre;
        this.sucursales = sucursales;
    }
    public Long getId() { return id; }
    public void setId(Long id) {
        this.id = id;
    }
    public List<Sucursales> getSucursales() {
        return sucursales;
    }
    public void setSucursales(List<Sucursales> sucursales) {
        this.sucursales = sucursales;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return "Franquicia{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", sucursales=" + sucursales +
                '}';
    }
}
