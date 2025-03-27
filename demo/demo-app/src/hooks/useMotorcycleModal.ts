// hooks/useMotorcycleModal.ts
import { useState } from 'react';
import Motorcycle from '../model/motorcycle.model';

interface UseMotorcycleModalProps {
    selectedMotorcycle: Motorcycle | null;
}

const useMotorcycleModal = ({ selectedMotorcycle }: UseMotorcycleModalProps) => {
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [isUpdateMode, setIsUpdateMode] = useState(false);
    const [newMotorcycle, setNewMotorcycle] = useState<Motorcycle>({
        id: '',
        brand: '',
        model: '',
        year: new Date().getFullYear(),
        price: 0,
        engineCapacity: 0,
        availableQuantity: 0,
        color: '',
    });

    const openModal = (update = false) => {
        setIsModalOpen(true);
        setIsUpdateMode(update);
        if (update && selectedMotorcycle) {
            setNewMotorcycle({ ...selectedMotorcycle });
        } else {
            setNewMotorcycle({
                id: '',
                brand: '',
                model: '',
                year: new Date().getFullYear(),
                price: 0,
                engineCapacity: 0,
                availableQuantity: 0,
                color: '',
            });
        }
    };

    const closeModal = () => {
        setIsModalOpen(false);
        setNewMotorcycle({
            id: '',
            brand: '',
            model: '',
            year: new Date().getFullYear(),
            price: 0,
            engineCapacity: 0,
            availableQuantity: 0,
            color: '',
        });
        setIsUpdateMode(false);
    };

    return {
        isModalOpen,
        isUpdateMode,
        newMotorcycle,
        openModal,
        closeModal,
        setNewMotorcycle,
    };
};

export default useMotorcycleModal;