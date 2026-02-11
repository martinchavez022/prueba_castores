package com.example.demo.common.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "productos")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idProducto")
    private Integer idProducto;

    @Column(name = "nombre", length = 40)
    private String nombre;

    @Column(name = "cantidad", columnDefinition = "INT(6) DEFAULT 0")
    private Integer cantidad = 0;

    @Column(name = "estatus", columnDefinition = "INT(1) DEFAULT 1")
    private Integer estatus = 1;
}