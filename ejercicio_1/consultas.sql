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
