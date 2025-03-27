// App.tsx
import { useState, useEffect } from 'react';
import './App.css';

import Person from './model/person.model';
import Motorcycle from './model/motorcycle.model';
import Order from './model/order.model';

import { PersonService } from './service/PersonService';
import { MotorcycleService } from './service/MotorcycleService';
import { OrderService } from './service/OrderService';

import PersonTable from './components/PersonTable';
import MotorcycleTable from './components/MotorcycleTable';
import OrderTable from './components/OrderTable';

import PersonModal from './components/PersonModal';
import MotorcycleModal from './components/MotorcycleModal';
import OrderModal from './components/OrderModal';

import usePersonActions from './hooks/usePersonActions';
import useMotorcycleActions from './hooks/useMotorcycleActions';
import useOrderActions from './hooks/useOrderActions';

import usePersonModal from './hooks/usePersonModal';
import useMotorcycleModal from './hooks/useMotorcycleModal';
import useOrderModal from './hooks/useOrderModal';

import ThemeSwitcher from './components/ThemeSwitcher';
import { LIGHT_THEME, DARK_THEME } from './constants/theme';

function App() {
    const [people, setPeople] = useState<Person[]>([]);
    const [motorcycles, setMotorcycles] = useState<Motorcycle[]>([]);
    const [orders, setOrders] = useState<Order[]>([]);

    const [loading, setLoading] = useState(true);
    const [isError, setIsError] = useState(false);

    const [selectedPerson, setSelectedPerson] = useState<Person | null>(null);
    const [selectedMotorcycle, setSelectedMotorcycle] = useState<Motorcycle | null>(null);
    const [selectedOrder, setSelectedOrder] = useState<Order | null>(null);

    const [currentTheme, setCurrentTheme] = useState<typeof LIGHT_THEME | typeof DARK_THEME>(LIGHT_THEME);

    const { handleAddPerson, handleUpdatePerson, handleDeletePerson } = usePersonActions({ setData: setPeople, setSelectedPerson, selectedPerson });
    const { handleAddMotorcycle, handleUpdateMotorcycle, handleDeleteMotorcycle } = useMotorcycleActions({ setData: setMotorcycles, setSelectedMotorcycle, selectedMotorcycle });
    const { handleAddOrder, handleUpdateOrder, handleDeleteOrder } = useOrderActions({ setData: setOrders, setSelectedOrder, selectedOrder });

    const personModal = usePersonModal({ selectedPerson });
    const motorcycleModal = useMotorcycleModal({ selectedMotorcycle });
    const orderModal = useOrderModal({ selectedOrder });

    useEffect(() => {
        fetchData();
    }, []);

    const fetchData = async () => {
        setLoading(true);
        setIsError(false);
        try {
            const [pData, mData, oData] = await Promise.all([
                PersonService.getPersons(),
                MotorcycleService.getMotorcycles(),
                OrderService.getOrders(),
            ]);
            setPeople(pData);
            setMotorcycles(mData);
            setOrders(oData);
        } catch (error) {
            console.error('Error fetching data:', error);
            setIsError(true);
        } finally {
            setLoading(false);
        }
    };

    const handleThemeChange = (newTheme: 'light' | 'dark') => {
        setCurrentTheme(newTheme);
    };

    return (
        <div className="app-container">
            <h1>Entity Management</h1>
            <ThemeSwitcher onThemeChange={handleThemeChange} />

            {/* People Section */}
            <section>
                <h2>People</h2>
                <div className="button-group">
                    <button onClick={() => personModal.openModal()}>Add</button>
                    <button onClick={() => personModal.openModal(true)} disabled={!selectedPerson}>Update</button>
                    <button onClick={handleDeletePerson} disabled={!selectedPerson}>Delete</button>
                </div>
                <PersonTable
                    data={people}
                    loading={loading}
                    isError={isError}
                    onRowSelected={(state) => setSelectedPerson(state.selectedRows[0] || null)}
                    theme={currentTheme}
                />
                <PersonModal
                    isOpen={personModal.isModalOpen}
                    isUpdateMode={personModal.isUpdateMode}
                    initialPerson={personModal.newPerson}
                    onClose={personModal.closeModal}
                    onAdd={handleAddPerson}
                    onUpdate={handleUpdatePerson}
                />
            </section>

            {/* Motorcycles Section */}
            <section>
                <h2>Motorcycles</h2>
                <div className="button-group">
                    <button onClick={() => motorcycleModal.openModal()}>Add</button>
                    <button onClick={() => motorcycleModal.openModal(true)} disabled={!selectedMotorcycle}>Update</button>
                    <button onClick={handleDeleteMotorcycle} disabled={!selectedMotorcycle}>Delete</button>
                </div>
                <MotorcycleTable
                    data={motorcycles}
                    loading={loading}
                    isError={isError}
                    onRowSelected={(state) => setSelectedMotorcycle(state.selectedRows[0] || null)}
                    theme={currentTheme}
                />
                <MotorcycleModal
                    isOpen={motorcycleModal.isModalOpen}
                    isUpdateMode={motorcycleModal.isUpdateMode}
                    initialMotorcycle={motorcycleModal.newMotorcycle}
                    onClose={motorcycleModal.closeModal}
                    onAdd={handleAddMotorcycle}
                    onUpdate={handleUpdateMotorcycle}
                />
            </section>

            {/* Orders Section */}
            <section>
                <h2>Orders</h2>
                <div className="button-group">
                    <button onClick={() => orderModal.openModal()}>Add</button>
                    <button onClick={() => orderModal.openModal(true)} disabled={!selectedOrder}>Update</button>
                    <button onClick={handleDeleteOrder} disabled={!selectedOrder}>Delete</button>
                </div>
                <OrderTable
                    data={orders}
                    loading={loading}
                    isError={isError}
                    onRowSelected={(state) => setSelectedOrder(state.selectedRows[0] || null)}
                    theme={currentTheme}
                />
                <OrderModal
                    isOpen={orderModal.isModalOpen}
                    isUpdateMode={orderModal.isUpdateMode}
                    initialOrder={orderModal.newOrder}
                    onClose={orderModal.closeModal}
                    onAdd={handleAddOrder}
                    onUpdate={handleUpdateOrder}
                    people={people}
                    motorcycles={motorcycles}
                />
            </section>
        </div>
    );
}

export default App;