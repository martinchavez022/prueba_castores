package com.example.demo.common.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "historial")
@EntityListeners(AuditingEntityListener.class)
public class Historial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idHistorico")
    private Integer idHistorico;

    @ManyToOne
    @JoinColumn(name = "idProducto", foreignKey = @ForeignKey(
            foreignKeyDefinition = "FOREIGN KEY (idProducto) REFERENCES productos(idProducto) ON UPDATE CASCADE ON DELETE RESTRICT"
    ))
    private Producto producto;

    @Column(name = "tipo", length = 50)
    private String tipo;

    @ManyToOne
    @JoinColumn(name = "idUsuario", foreignKey = @ForeignKey(
            foreignKeyDefinition = "FOREIGN KEY (idUsuario) REFERENCES usuarios(idUsuario) ON UPDATE CASCADE ON DELETE RESTRICT"
    ))
    private Usuario usuario;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}