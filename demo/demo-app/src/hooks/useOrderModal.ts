// hooks/useOrderModal.ts
import { useState } from 'react';
import Order, { OrderedMotorcycle } from '../model/order.model';
import Person from '../model/person.model';

const defaultPerson: Person = {
    id: '',
    name: '',
    age: 0,
    email: ''
};

const defaultOrder: Order = {
    id: '',
    person: defaultPerson,
    motorcycles: [] as OrderedMotorcycle[],
    date: new Date().toISOString().slice(0, 16), // datetime-local format
    totalCost: 0,
};

interface UseOrderModalProps {
    selectedOrder: Order | null;
}

const useOrderModal = ({ selectedOrder }: UseOrderModalProps) => {
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [isUpdateMode, setIsUpdateMode] = useState(false);
    const [newOrder, setNewOrder] = useState<Order>(defaultOrder);

    const openModal = (update = false) => {
        setIsModalOpen(true);
        setIsUpdateMode(update);
        if (update && selectedOrder) {
            setNewOrder({ ...selectedOrder });
        } else {
            setNewOrder(defaultOrder);
        }
    };

    const closeModal = () => {
        setIsModalOpen(false);
        setIsUpdateMode(false);
        setNewOrder(defaultOrder);
    };

    return {
        isModalOpen,
        isUpdateMode,
        newOrder,
        openModal,
        closeModal,
        setNewOrder,
    };
};

export default useOrderModal;
