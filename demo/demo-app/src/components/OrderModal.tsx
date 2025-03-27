// components/OrderModal.tsx
import React, { useState, useEffect } from 'react';
import Order from "../model/order.model";
import Person from "../model/person.model";
import Motorcycle from "../model/motorcycle.model";

interface OrderModalProps {
    isOpen: boolean;
    isUpdateMode: boolean;
    initialOrder: Order;
    people: Person[];
    motorcycles: Motorcycle[];
    onClose: () => void;
    onAdd: (order: Order) => Promise<void>;
    onUpdate: (order: Order) => Promise<void>;
}

function OrderModal({ isOpen, isUpdateMode, initialOrder, people, motorcycles, onClose, onAdd, onUpdate }: OrderModalProps) {
    const [order, setOrder] = useState<Order>(initialOrder);
    const [selectedMotorcycleId, setSelectedMotorcycleId] = useState('');
    const [quantity, setQuantity] = useState(1);

    useEffect(() => {
        setOrder(initialOrder);
    }, [initialOrder]);

    const handlePersonChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
        const selectedPerson = people.find(p => p.id === e.target.value);
        if (selectedPerson) {
            setOrder(prev => ({ ...prev, person: selectedPerson }));
        }
    };

    const handleAddMotorcycle = () => {
        if (!selectedMotorcycleId || quantity < 1) return;

        const selectedMoto = motorcycles.find(m => m.id === selectedMotorcycleId);
        if (!selectedMoto) return;

        // ✅ Check against available quantity
        if (quantity > selectedMoto.availableQuantity) {
            alert(`Only ${selectedMoto.availableQuantity} available for ${selectedMoto.brand} ${selectedMoto.model}`);
            return;
        }

        const alreadyAdded = order.motorcycles.find(m => m.motorcycle.id === selectedMotorcycleId);
        if (alreadyAdded) {
            setOrder(prev => ({
                ...prev,
                motorcycles: prev.motorcycles.map(m =>
                    m.motorcycle.id === selectedMotorcycleId
                        ? { ...m, quantity: m.quantity + quantity }
                        : m
                )
            }));
        } else {
            setOrder(prev => ({
                ...prev,
                motorcycles: [...prev.motorcycles, { motorcycle: selectedMoto, quantity }],
            }));
        }

        setSelectedMotorcycleId('');
        setQuantity(1);
    };

    const handleRemoveMotorcycle = (id: string) => {
        setOrder(prev => ({
            ...prev,
            motorcycles: prev.motorcycles.filter(m => m.motorcycle.id !== id),
        }));
    };

    const handleSubmit = async () => {
        if (!order.person || order.motorcycles.length === 0) {
            alert("Please select a person and add at least one motorcycle.");
            return;
        }

        if (isUpdateMode) {
            await onUpdate(order);
        } else {
            await onAdd(order);
        }

        onClose();
    };


    if (!isOpen) {
        return null;
    }

    return (
        <div className="modal">
            <div className="modal-content">
                <h2>{isUpdateMode ? 'Update Order' : 'Add Order'}</h2>
                {isUpdateMode && (
                    <input
                        type="text"
                        name="id"
                        placeholder="ID"
                        value={order.id}
                        disabled
                    />
                )}
                <select value={order.person?.id || ''} onChange={handlePersonChange}>
                    <option value="">Select Person</option>
                    {people.map((person) => (
                        <option key={person.id} value={person.id}>
                            {person.name} ({person.email})
                        </option>
                    ))}
                </select>


                <div className="motorcycle-form">
                    <select
                        value={selectedMotorcycleId}
                        onChange={(e) => setSelectedMotorcycleId(e.target.value)}
                    >
                        <option value="">Select Motorcycle</option>
                        {motorcycles.map(m => (
                            <option key={m.id} value={m.id}>
                                {m.brand} {m.model}
                            </option>
                        ))}
                    </select>

                    <input
                        type="number"
                        min={1}
                        value={quantity}
                        onChange={(e) => setQuantity(parseInt(e.target.value))}
                        placeholder="Quantity"
                    />

                    <button onClick={handleAddMotorcycle}>Add</button> {/* ✅ Moved outside */}
                </div>


                <ul className="selected-motorcycles">
                    {order.motorcycles.map(({ motorcycle, quantity }) => (
                        <li key={motorcycle.id}>
                            {motorcycle.brand} {motorcycle.model} — x{quantity}
                            <button onClick={() => handleRemoveMotorcycle(motorcycle.id)}>Remove</button>
                        </li>
                    ))}
                </ul>

                <input
                    type="datetime-local"
                    name="date"
                    value={order.date?.slice(0, 16) || ''} // strip seconds + Z
                    onChange={(e) => setOrder(prev => ({ ...prev, date: e.target.value }))}
                    required
                />


                <div className="modal-buttons">
                    <button onClick={handleSubmit}>{isUpdateMode ? 'Update' : 'Add'}</button>
                    <button onClick={onClose}>Cancel</button>
                </div>
            </div>
        </div>
    );
}

export default OrderModal;