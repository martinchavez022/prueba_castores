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