package com.example.demo.common.service;

import com.example.demo.common.entity.Producto;
import com.example.demo.common.repository.ProductoRepository;
import com.example.demo.common.exception.InvalidQuantityException;
import com.example.demo.common.exception.ResourceNotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductoService {
    private final ProductoRepository productoRepository;
    private final EntityManager entityManager;

    public ProductoService(ProductoRepository productoRepository, EntityManager entityManager) {
        this.productoRepository = productoRepository;
        this.entityManager = entityManager;
    }

    /**
     * guarda un nuevo producto en la base de datos
     * @param producto
     * @return
     */
    public Producto saveProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    /**
     * Modifica la cantidad de producto de un registro pero ademas verifica que
     * la cantidad ingresada no sea menor a la existente.
     *
     * @param productoId Identificador del producto a actualizar
     * @param nuevaCantida Nueva cantidad
     * @param userId Identificador del usuario que hace la peticion
     * @return Producto actualizado
     * @throws ResourceNotFoundException Si no se encuentra el producto
     * @throws InvalidQuantityException Si la cantidad es menor a la actual
     */
    @Transactional
    public Producto actualizarCantidadProducto(Long productoId, int nuevaCantida, int userId) {
        Optional<Producto> productoOptional = productoRepository.findById(productoId);

        if (productoOptional.isEmpty()) {
            throw new ResourceNotFoundException("Producto no encontrado con ID: " + productoId);
        }

        Producto producto = productoOptional.get();
        if (nuevaCantida < producto.getCantidad()) {
            throw new InvalidQuantityException("La nueva cantidad no puede ser menor a la cantidad actual");
        }

        entityManager.createNativeQuery("SET @current_app_user_id = :userId")
                     .setParameter("userId", userId)
                     .executeUpdate();
        producto.setCantidad(nuevaCantida);
        return productoRepository.save(producto);
    }

    /**
     * Este servicio actualiza el estatus del producto,
     * si esta activo lo desactiva, si esta desactivado lo activa
     * @param productoId Identificador del producto
     * @return Producto actualizado
     * @throws ResourceNotFoundException
     */
    public Producto actualizarEstatus(Long productoId) {
        Optional<Producto> productoOptional = productoRepository.findById(productoId);
        if (productoOptional.isEmpty()) {
            throw new ResourceNotFoundException("Producto no encontrado");
        }

        Producto producto = productoOptional.get();
        if (producto.getEstatus() == 1) {
            producto.setEstatus(0);
        } else {
            producto.setEstatus(1);
        }
        return productoRepository.save(producto);
    }
}
