import React, { useState, useEffect } from 'react';
import { useAuth } from '../../contexts/AuthContext';
import './styles.css';

interface Product {
    idProducto: number;
    nombre: string;
    cantidad: number;
    estatus: number;
}

const InventoryPage = () => {
    const { user } = useAuth();
    const [products, setProducts] = useState<Product[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const [filter, setFilter] = useState(1); // 1 for Active, 0 for Inactive
    const [showAddModal, setShowAddModal] = useState(false);
    const [showIncreaseModal, setShowIncreaseModal] = useState(false);
    const [selectedProduct, setSelectedProduct] = useState<Product | null>(null);
    const [quantityToAdd, setQuantityToAdd] = useState(1);
    const [newProductName, setNewProductName] = useState('');

    useEffect(() => {
        const fetchProducts = async () => {
            try {
                const response = await fetch('http://localhost:8080/api/producto/obtener');
                const data = await response.json();
                if (data.success) {
                    setProducts(data.data);
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

    const handleToggleStatus = async (product: Product) => {
        if (!user) return;
        const newStatus = product.estatus === 1 ? 0 : 1;
        try {
            const response = await fetch(`http://localhost:8080/api/producto/${product.idProducto}/${user.idUsuario}?estatus=${newStatus}`, {
                method: 'GET',
            });
            if (response.ok) {
                setProducts(products.map(p => p.idProducto === product.idProducto ? { ...p, estatus: newStatus } : p));
            } else {
                alert('Failed to update product status');
            }
        } catch (err) {
            alert('Failed to update product status');
        }
    };

    const handleIncreaseInventory = async (e: React.FormEvent) => {
        e.preventDefault();
        if (!selectedProduct || !user) return;

        if (quantityToAdd <= 0) {
            alert("Quantity must be greater than zero.");
            return;
        }

        try {
            // Note: Sending a raw number with Content-Type 'application/json' is unconventional.
            // The backend with @RequestBody int might not handle this without specific configuration.
            // The standard approach is to send a JSON object like { "cantidad": quantityToAdd }.
            const response = await fetch(`http://localhost:8080/api/producto/${selectedProduct.idProducto}/${user.idUsuario}/cantidad`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(quantityToAdd),
            });

            if (response.ok) {
                setProducts(products.map(p => 
                    p.idProducto === selectedProduct.idProducto 
                        ? { ...p, cantidad: p.cantidad + quantityToAdd } 
                        : p
                ));
                setShowIncreaseModal(false);
                setQuantityToAdd(1);
            } else {
                alert('Failed to increase inventory');
            }
        } catch (err) {
            alert('Failed to increase inventory');
        }
    };

    const handleAddNewProduct = async (e: React.FormEvent) => {
        e.preventDefault();
        if (!user) return;

        try {
            const response = await fetch(`http://localhost:8080/api/producto/${user.idUsuario}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ nombre: newProductName, cantidad: 0, estatus: 1 }),
            });

            const data = await response.json();

            if (response.ok && data.success) {
                setProducts([...products, data.data]);
                setShowAddModal(false);
                setNewProductName('');
            } else {
                alert(data.message || 'Failed to add new product');
            }
        } catch (err) {
            alert('Failed to add new product');
        }
    };

    const filteredProducts = products.filter(p => p.estatus === filter);

    if (loading) {
        return <div>Loading...</div>;
    }

    if (error) {
        return <div>Error: {error}</div>;
    }

    return (
        <div className="inventory-page">
            <h1>Modulo de Inventario</h1>

            <div className="controls">
                <div className="filters">
                    <button onClick={() => setFilter(1)} className={filter === 1 ? 'active' : ''}>Activo</button>
                    <button onClick={() => setFilter(0)} className={filter === 0 ? 'active' : ''}>Inactivo</button>
                </div>
            </div>
            <div className="add-product-container">
                <button onClick={() => setShowAddModal(true)} className="add-button">Agregar nuevo producto</button>
            </div>

            <table className="inventory-table">
                <thead>
                    <tr>
                        <th>Nombre</th>
                        <th>Cantidad</th>
                        <th>Estatus</th>
                        <th></th>
                    </tr>
                </thead>
                <tbody>
                    {filteredProducts.map(product => (
                        <tr key={product.idProducto}>
                            <td>{product.nombre}</td>
                            <td>{product.cantidad}</td>
                            <td>{product.estatus === 1 ? 'Activo' : 'Inactivo'}</td>
                            <td className="actions">
                                {product.estatus === 1 && (
                                    <button onClick={() => {
                                        setSelectedProduct(product);
                                        setShowIncreaseModal(true);
                                    }}>Agregar</button>
                                )}
                                <button onClick={() => handleToggleStatus(product)}>
                                    {product.estatus === 1 ? 'Desactivar' : 'Activar'}
                                </button>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>

            {showAddModal && (
                <div className="modal">
                    <div className="modal-content">
                        <h2>Agregar nuevo producto</h2>
                        <form onSubmit={handleAddNewProduct}>
                            <div className="form-group">
                                <label>Nombre del producto</label>
                                <input 
                                    type="text" 
                                    value={newProductName}
                                    onChange={(e) => setNewProductName(e.target.value)}
                                    required
                                />
                            </div>
                            <div className="form-actions">
                                <button type="submit">Agregar</button>
                                <button type="button" onClick={() => setShowAddModal(false)}>Cancelar</button>
                            </div>
                        </form>
                    </div>
                </div>
            )}

            {showIncreaseModal && selectedProduct && (
                <div className="modal">
                    <div className="modal-content">
                        <h2>Incrementar inventario {selectedProduct.nombre}</h2>
                        <form onSubmit={handleIncreaseInventory}>
                            <div className="form-group">
                                <label>Cantidad a agregar</label>
                                <input 
                                    type="number" 
                                    min="1" 
                                    value={quantityToAdd}
                                    onChange={(e) => setQuantityToAdd(Number(e.target.value))}
                                />
                            </div>
                            <div className="form-actions">
                                <button type="submit">Agregar Stock</button>
                                <button type="button" onClick={() => setShowIncreaseModal(false)}>Cancel</button>
                            </div>
                        </form>
                    </div>
                </div>
            )}
        </div>
    );
};

export default InventoryPage;
