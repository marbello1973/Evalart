package com.evalart.model.producto;

import com.evalart.model.sucursal.Sucursales;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import lombok.Data;

@Data
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

    public Productos() { }
    public Productos(Long id, String nombre, Integer stock, Sucursales sucursales) {
        this.id = id;
        this.nombre = nombre;
        this.stock = stock;
        this.sucursales = sucursales;
    }
    public Productos(long l, String productoName, int i) { }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Sucursales getSucursales() {
        return sucursales;
    }
    public void setSucursales(Sucursales sucursales) {
        this.sucursales = sucursales;
    }
    public Integer getStock() {
        return stock;
    }
    public void setStock(Integer stock) {
        this.stock = stock;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return "Productos{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", stock=" + stock +
                ", sucursales=" + sucursales +
                '}';
    }
}
