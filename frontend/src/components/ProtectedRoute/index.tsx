import React from 'react';
import { Navigate, Outlet } from 'react-router-dom';
import { useAuth } from '../../contexts/AuthContext';

interface ProtectedRouteProps {
    allowedRoles?: string[];
}

const ProtectedRoute: React.FC<ProtectedRouteProps> = ({ allowedRoles }) => {
    const { isAuthenticated, isLoading, user } = useAuth();

    if (isLoading) {
        return <div>Loading...</div>;
    }

    if (!isAuthenticated) {
        return <Navigate to="/login" />;
    }

    if (allowedRoles && user && !allowedRoles.includes(user.rol.nombre)) {
        return <Navigate to="/" />; // Redirect to main page if role is not allowed
    }

    return <Outlet />;
};

export default ProtectedRoute;
