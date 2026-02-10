CREATE DATABASE IF NOT EXISTS exercise_db;

-- Productos

CREATE TABLE IF NOT EXISTS exercise_db.productos (
    idProducto INT(6) AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(40),
    precio DECIMAL(16,2)
);

INSERT INTO exercise_db.productos (nombre, precio) VALUES 
("LAPTOP", 3000),
("PC", 4000),
("MOUSE", 100),
("TECLADO", 150),
("MONITOR", 2000),
("MICROFONO", 350),
("AUDIFONOS", 450);

-- Venta

CREATE TABLE IF NOT EXISTS exercise_db.ventas (
    idVenta INT(6) AUTO_INCREMENT PRIMARY KEY,
    idProducto INT(6),
    cantidad INT(6),
    FOREIGN KEY (idProducto) REFERENCES productos (idProducto)
);

INSERT INTO exercise_db.ventas (idProducto, cantidad) VALUES
(5,8), (1,15), (6,13), (6,4), (2,3), (5,1), (4,5), (2,5), (6,2), (1,8);