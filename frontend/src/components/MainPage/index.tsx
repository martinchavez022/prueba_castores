import React from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '../../contexts/AuthContext';
import './styles.css';

const MainPage = () => {
    const { user, logout } = useAuth();

    return (
        <div className="main-page">
            <div className="header">
                <h1></h1>
                <div className="user-info">
                    {user && <p>{user.rol.nombre}:  {user.nombre}</p>}
                    <button onClick={logout} className="logout-button">Salir</button>
                </div>
            </div>
            <div className="navigation-buttons">
                <Link to="/inventory" className="nav-button">Inventario</Link>
                {user && user.rol.nombre !== 'Almacenista' && (
                    <Link to="/history" className="nav-button">Historial</Link>
                )}
                {user && user.rol.nombre !== 'Administrador' && (
                    <Link to="/subtract" className="nav-button">Manejo de Productos</Link>
                )}
            </div>
        </div>
    );
};

export default MainPage;
