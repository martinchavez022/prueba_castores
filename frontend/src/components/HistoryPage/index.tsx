import React, { useState, useEffect } from 'react';
import './styles.css';

interface Historial {
    idHistorial: number;
    producto: { nombre: string };
    cantidad: number;
    tipo: string;
    usuario: { nombre: string };
    createdAt: string;
}

const HistoryPage = () => {
    const [movements, setMovements] = useState<Historial[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const [filter, setFilter] = useState('All'); 

    useEffect(() => {
        const fetchHistory = async () => {
            setLoading(true);
            setError(null);
            let url = 'http://localhost:8080/api/historial/obtener';
            if (filter !== 'All') {
                url = `http://localhost:8080/api/historial/obtener/${filter.toLowerCase()}`;
            }

            try {
                const response = await fetch(url);
                const data = await response.json();
                if (data.success) {
                    console.log(data);
                    setMovements(data.data);
                } else {
                    setError(data.message);
                }
            } catch (err) {
                setError('Failed to fetch history');
            } finally {
                setLoading(false);
            }
        };

        fetchHistory();
    }, [filter]);


    if (error) {
        return <div>Error: {error}</div>;
    }

    return (
        <div className="history-page">
            <h1>Historial de Movimientos</h1>

            <div className="controls">
                <div className="filters">
                    <button onClick={() => setFilter('All')} className={filter === 'All' ? 'active' : ''}>Todos</button>
                    <button onClick={() => setFilter('Entrada')} className={filter === 'Entrada' ? 'active' : ''}>Entrada</button>
                    <button onClick={() => setFilter('Salida')} className={filter === 'Salida' ? 'active' : ''}>Salida</button>
                </div>
            </div>

            {loading ? (
                <div>Cargando...</div>
            ) : (
                <table className="history-table">
                    <thead>
                        <tr>
                            <th>Producto</th>
                            <th>Cantidad</th>
                            <th>Tipo</th>
                            <th>Usuario</th>
                            <th>Fecha y Hora</th>
                        </tr>
                    </thead>
                    <tbody>
                        {movements.map(movement => (
                            <tr key={movement.idHistorial}>
                                <td>{movement.producto.nombre}</td>
                                <td>{movement.cantidad}</td>
                                <td>{movement.tipo}</td>
                                <td>{movement.usuario.nombre}</td>
                                <td>{new Date(movement.createdAt).toLocaleString()}</td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            )}
        </div>
    );
};

export default HistoryPage;
