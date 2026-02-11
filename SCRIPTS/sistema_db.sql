CREATE DATABASE IF NOT EXISTS sistema_db;

USE sistema_db;

-- tabla para rol

CREATE TABLE IF NOT EXISTS sistema_db.roles (
    idRol INT(2) AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100)
);

INSERT INTO sistema_db.roles (nombre) VALUES
("Administrador"), ("Almacenista");

-- tabla para usarios

CREATE TABLE IF NOT EXISTS sistema_db.usuarios (
    idUsuario INT(6) AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100),
    correo VARCHAR(50) UNIQUE,
    contrasena VARCHAR(25),
    idRol INT(2),
    estatus INT(1) DEFAULT 1,
    FOREIGN KEY (idRol) REFERENCES roles (idRol) 
    ON UPDATE CASCADE ON DELETE RESTRICT
);

INSERT INTO sistema_db.usuarios (nombre, correo, contrasena, idRol) 
VALUES 
("Usuario 1", "user1@mail.com", "user123", 1),
("Usuario 2", "user2@mail.com", "user123", 1);

-- tabla para los productos

CREATE TABLE IF NOT EXISTS sistema_db.productos (
    idProducto INT(6) AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(40),
    cantidad INT(6) DEFAULT 0,
    estatus INT(1) DEFAULT 1
);

-- tabla para el historico

CREATE TABLE IF NOT EXISTS sistema_db.historial (
    idHistorico INT(6) AUTO_INCREMENT PRIMARY KEY,
    idProducto INT(6),
    tipo VARCHAR(50), -- esta puede ser 'entrada' 'salida'
    idUsuario INT(6),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (idUsuario) REFERENCES usuarios (idUsuario)
    ON UPDATE CASCADE ON DELETE RESTRICT,
    FOREIGN KEY (idProducto) REFERENCES productos (idProducto)
    ON UPDATE CASCADE ON DELETE RESTRICT
);

-- trigger para historial

DELIMITER //

CREATE TRIGGER sistema_db.trr_monitor_productos
AFTER UPDATE ON productos
FOR EACH ROW
BEGIN
    IF NEW.cantidad <> OLD.cantidad THEN
        
        IF NEW.cantidad > OLD.cantidad THEN
            INSERT INTO sistema_db.historial (idProducto, tipo, idUsuario)
            VALUES (NEW.idProducto, 'entrada', @current_app_user_id);
        
        ELSEIF NEW.cantidad < OLD.cantidad THEN
            INSERT INTO sistema_db.historial (idProducto, tipo, idUsuario)
            VALUES (NEW.idProducto, 'salida', @current_app_user_id);
        END IF;
        
    END IF;
END //

DELIMITER ;