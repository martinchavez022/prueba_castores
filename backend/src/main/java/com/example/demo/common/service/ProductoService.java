package com.example.demo.common.service;

import com.example.demo.common.entity.Producto;
import com.example.demo.common.entity.Usuario;
import com.example.demo.common.exception.UserCanNotAccess;
import com.example.demo.common.repository.ProductoRepository;
import com.example.demo.common.exception.InvalidQuantityException;
import com.example.demo.common.exception.ResourceNotFoundException;
import com.example.demo.usuario.UsuarioRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {
    private final ProductoRepository productoRepository;
    private final EntityManager entityManager;
    private final UsuarioRepository usuarioRepository;

    public ProductoService(ProductoRepository productoRepository, EntityManager entityManager, UsuarioRepository usuarioRepository) {
        this.productoRepository = productoRepository;
        this.entityManager = entityManager;
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * guarda un nuevo producto en la base de datos
     * @param producto
     * @param id_usuario
     * @return
     */
    public Producto saveProducto(Producto producto, int id_usuario) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(id_usuario);
        Usuario usuario = usuarioOptional.get();

        if (usuario.getRol().getNombre().equals("Almacenista")) {
            throw new UserCanNotAccess("EL usuario no tiene permiso!");
        }
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
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(userId);

        Usuario usuario = usuarioOptional.get();
        if (usuario.getRol().getNombre().equals("Almacenista")) {
            throw new UserCanNotAccess("EL usuario no tiene permiso!");
        }

        Optional<Producto> productoOptional = productoRepository.findById(productoId);

        if (productoOptional.isEmpty()) {
            throw new ResourceNotFoundException("Producto no encontrado con ID: " + productoId);
        }
        Producto producto = productoOptional.get();
        entityManager.createNativeQuery("SET @current_app_user_id = :userId")
                     .setParameter("userId", userId)
                     .executeUpdate();
        producto.setCantidad(producto.getCantidad() + nuevaCantida);
        return productoRepository.save(producto);
    }

    /**
     * Este servicio actualiza el estatus del producto,
     * si esta activo lo desactiva, si esta desactivado lo activa
     * @param productoId Identificador del producto
     * @param usuarioId Identificador del producto
     * @return Producto actualizado
     * @throws ResourceNotFoundException
     */
    public Producto actualizarEstatus(Long productoId, int usuarioId) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(usuarioId);

        Usuario usuario = usuarioOptional.get();
        if (usuario.getRol().getNombre().equals("Almacenista")) {
            throw new UserCanNotAccess("El usuario no tiene permiso!");
        }

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

    /**
     * Obtener los productos
     * @return List<Producto>
     */
    public List<Producto> obtenerProductos() {
        return productoRepository.findAll();
    }

    /**
     * Restar producto de la base de datos
     * @param productoId Producto al que se restara
     * @param cantidadRestada Cantidad de producto a restar
     * @param userId Identificador de usuario
     * @return Producto
     */
    @Transactional
    public Producto restarProducto(Long productoId, int cantidadRestada, int userId) {
        Optional<Producto> productoOptional = productoRepository.findById(productoId);

        if (productoOptional.isEmpty()) {
            throw new ResourceNotFoundException("Producto no encontrado con ID: " + productoId);
        }

        Producto producto = productoOptional.get();

        if (cantidadRestada > producto.getCantidad()) {
            throw new InvalidQuantityException("No hay suficiente existencia!");
        }

        entityManager.createNativeQuery("SET @current_app_user_id = :userId")
                .setParameter("userId", userId)
                .executeUpdate();
        producto.setCantidad(producto.getCantidad() - cantidadRestada);
        return productoRepository.save(producto);
    }
}
