// components/MotorcycleModal.tsx
import React, { useState, useEffect } from 'react';
import Motorcycle from "../model/motorcycle.model";

interface MotorcycleModalProps {
    isOpen: boolean;
    isUpdateMode: boolean;
    initialMotorcycle: Motorcycle;
    onClose: () => void;
    onAdd: (motorcycle: Motorcycle) => Promise<void>;
    onUpdate: (motorcycle: Motorcycle) => Promise<void>;
}

function MotorcycleModal({ isOpen, isUpdateMode, initialMotorcycle, onClose, onAdd, onUpdate }: MotorcycleModalProps) {
    const [motorcycle, setMotorcycle] = useState<Motorcycle>(initialMotorcycle);

    useEffect(() => {
        setMotorcycle(initialMotorcycle);
    }, [initialMotorcycle]);

    const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setMotorcycle((prev) => ({
            ...prev,
            [name]: ['year', 'price', 'engineCapacity', 'availableQuantity'].includes(name) ? parseFloat(value) : value,
        }));
    };

    const handleSubmit = async () => {
        if (isUpdateMode) {
            await onUpdate(motorcycle);
        } else {
            await onAdd(motorcycle);
        }
        onClose();
    };

    if (!isOpen) {
        return null;
    }

    return (
        <div className="modal">
            <div className="modal-content">
                <h2>{isUpdateMode ? 'Update Motorcycle' : 'Add Motorcycle'}</h2>
                {isUpdateMode && (
                    <input
                        type="text"
                        name="id"
                        placeholder="ID"
                        value={motorcycle.id}
                        disabled
                    />
                )}
                <input
                    type="text"
                    name="brand"
                    placeholder="Brand"
                    value={motorcycle.brand}
                    onChange={handleInputChange}
                />
                <input
                    type="text"
                    name="model"
                    placeholder="Model"
                    value={motorcycle.model}
                    onChange={handleInputChange}
                />
                <input
                    type="number"
                    name="year"
                    placeholder="Year"
                    value={motorcycle.year}
                    onChange={handleInputChange}
                />
                <input
                    type="number"
                    name="price"
                    placeholder="Price"
                    step="0.01"
                    value={motorcycle.price}
                    onChange={handleInputChange}
                />
                <input
                    type="number"
                    name="engineCapacity"
                    placeholder="Engine Capacity (cc)"
                    value={motorcycle.engineCapacity}
                    onChange={handleInputChange}
                />
                <input
                    type="number"
                    name="availableQuantity"
                    placeholder="Available Quantity"
                    value={motorcycle.availableQuantity}
                    onChange={handleInputChange}
                />
                <input
                    type="text"
                    name="color"
                    placeholder="Color (optional)"
                    value={motorcycle.color || ''}
                    onChange={handleInputChange}
                />
                <div className="modal-buttons">
                    <button onClick={handleSubmit}>
                        {isUpdateMode ? 'Update' : 'Add'}
                    </button>
                    <button onClick={onClose}>Cancel</button>
                </div>
            </div>
        </div>
    );
}

export default MotorcycleModal;