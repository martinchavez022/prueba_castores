package com.example.demo.common.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idUsuario")
    private Integer idUsuario;

    @Column(name = "nombre", length = 100)
    private String nombre;

    @Column(name = "correo", length = 50, unique = true)
    private String correo;

    @Column(name = "contrasena", length = 25)
    private String contrasena;

    @ManyToOne
    @JoinColumn(name = "idRol", foreignKey = @ForeignKey(
            foreignKeyDefinition = "FOREIGN KEY (idRol) REFERENCES roles(idRol) ON UPDATE CASCADE ON DELETE RESTRICT"
    ))
    private Rol rol;

    @Column(name = "estatus", columnDefinition = "INT(1) DEFAULT 1")
    private Integer estatus = 1;
}