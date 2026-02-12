import React, { useState, useEffect } from 'react';
import { useAuth } from '../../contexts/AuthContext';
import './styles.css';

interface Product {
    idProducto: number;
    nombre: string;
    cantidad: number;
    estatus: number;
}

const SubtractPage = () => {
    const { user } = useAuth();
    const [products, setProducts] = useState<Product[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const [subtractQuantities, setSubtractQuantities] = useState<{ [key: number]: number }>({});

    useEffect(() => {
        const fetchProducts = async () => {
            try {
                const response = await fetch('http://localhost:8080/api/producto/obtener');
                const data = await response.json();
                if (data.success) {
                    setProducts(data.data.filter((p: Product) => p.estatus === 1));
                } else {
                    setError(data.message);
                }
            } catch (err) {
                setError('Failed to fetch products');
            } finally {
                setLoading(false);
            }
        };

        fetchProducts();
    }, []);

    const handleQuantityChange = (productId: number, quantity: number) => {
        setSubtractQuantities(prev => ({
            ...prev,
            [productId]: quantity,
        }));
    };

    const handleSubtract = async (product: Product) => {
        if (!user) return;

        const quantityToSubtract = subtractQuantities[product.idProducto] || 0;

        if (quantityToSubtract <= 0) {
            alert('Please enter a quantity greater than zero.');
            return;
        }

        if (quantityToSubtract > product.cantidad) {
            alert('You cannot subtract more than the available quantity.');
            return;
        }

        try {
            const response = await fetch(`http://localhost:8080/api/producto/restar/${product.idProducto}/${user.idUsuario}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(quantityToSubtract),
            });

            const data = await response.json();

            if (response.ok && data.success) {
                setProducts(products.map(p => 
                    p.idProducto === product.idProducto 
                        ? { ...p, cantidad: p.cantidad - quantityToSubtract } 
                        : p
                ));
                setSubtractQuantities(prev => ({ ...prev, [product.idProducto]: 0 }));
            } else {
                alert(data.message || 'Failed to subtract from inventory.');
            }
        } catch (err) {
            alert('Failed to subtract from inventory.');
        }
    };

    if (loading) {
        return <div>Loading...</div>;
    }

    if (error) {
        return <div>Error: {error}</div>;
    }

    return (
        <div className="subtract-page">
            <h1>Restar Productos</h1>
            <table className="subtract-table">
                <thead>
                    <tr>
                        <th>Nombre</th>
                        <th>Cantidad Actual</th>
                        <th>Cantidad A Restar</th>
                        <th></th>
                    </tr>
                </thead>
                <tbody>
                    {products.map(product => (
                        <tr key={product.idProducto}>
                            <td>{product.nombre}</td>
                            <td>{product.cantidad}</td>
                            <td>
                                <input 
                                    type="number" 
                                    min="0"
                                    max={product.cantidad}
                                    value={subtractQuantities[product.idProducto] || ''}
                                    onChange={(e) => handleQuantityChange(product.idProducto, Number(e.target.value))}
                                />
                            </td>
                            <td>
                                <button onClick={() => handleSubtract(product)}>Restar</button>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
};

export default SubtractPage;
