1. CONOCIMIENTOS SQL

**1.1)** *Describe el funcionamiento general de la sentencia JOIN.*

La sentencia JOIN permite aprovechar las relaciones entre tablas y consultar varias tablas al mismo tiempo. 

**1.2)** *¿Cuáles son los tipos de JOIN y cuál es el funcionamiento de los mismos?*

Existen cuatro tipos de JOIN en sql.

- INNER JOIN: Retorna solo los registros que coinciden en ambas tablas.

- LEFT JOIN: Retorna todos los registros de la primera tabla referenciada y los registros se la segunda tabla que coinciden con los de la primera tabla.

- RIGHT JOIN: Retorna todos los registros de la segunda tabla referenciada y los registros de la primera tabla que coincide con los de la segunda tabla.

- FULL JOIN: Retorna todos los registros cuando existe una coincidencia en la tabla izquierda o derecha.

**1.3)** *¿Cuál el funcionamiento general de los TRIGGER y qué propósito tienen?*

Un TRIGGER es un tipo especial de stored procedure que automaticamente se ejecuta en respuesta de un evento en especifico.

**1.4)** *¿Qué es y para qué sirve un STORED PROCEDURE?*

Un STORED PROCEDURE es un conjunto de precompiladas peticiones SQL que se almacenan en la base de datos y ayudan al usuario a ejecutar varias operaciones en una simple llamada.


### COSULTAS

```sql
-- Traer todos los productos que tengan un venta

SELECT DISTINCT
	productos.idProducto,
    productos.nombre,
    productos.precio
FROM exercise_db.productos
INNER JOIN exercise_db.ventas
ON productos.idProducto = ventas.idProducto;

-- Trear todos los productos que tengan ventas 
-- y la cantidad total de productos vendidos

SELECT 
	productos.idProducto,
    productos.nombre,
    productos.precio,
    SUM(ventas.cantidad) as total_vendidos
FROM exercise_db.productos
INNER JOIN exercise_db.ventas
ON productos.idProducto = ventas.idProducto
GROUP BY 
	productos.idProducto,
    productos.nombre,
    productos.precio;

-- Trear todos los productos (independiente de si tiene ventas o no)
-- y las suma total vendida por producto

SELECT 
	productos.idProducto,
    productos.nombre,
    productos.precio,
    COALESCE(SUM(ventas.cantidad * productos.precio),0) as suma_total
FROM exercise_db.productos
LEFT JOIN exercise_db.ventas
	ON productos.idProducto = ventas.idProducto
GROUP BY 
	productos.idProducto,
    productos.nombre,
    productos.precio;
```