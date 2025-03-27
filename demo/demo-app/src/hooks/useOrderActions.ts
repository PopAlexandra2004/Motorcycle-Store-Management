// hooks/useOrderActions.ts
import Order from '../model/order.model';
import { OrderService } from '../service/OrderService';

interface UseOrderActionsProps {
    setData: React.Dispatch<React.SetStateAction<Order[]>>;
    setSelectedOrder: React.Dispatch<React.SetStateAction<Order | null>>;
    selectedOrder: Order | null;
}

const useOrderActions = ({ setData, setSelectedOrder, selectedOrder }: UseOrderActionsProps) => {
    const handleAddOrder = async (order: Order) => {
        try {
            const addedOrder = await OrderService.addOrder(order);
            setData(prevData => [...prevData, addedOrder]);
        } catch (error) {
            console.error('Error adding order:', error);
            alert('Failed to add order.');
        }
    };

    const handleUpdateOrder = async (order: Order) => {
        if (!selectedOrder) return;
        try {
            await OrderService.updateOrder(order);
            setData(prevData =>
                prevData.map(o => (o.id === selectedOrder.id ? order : o))
            );
        } catch (error) {
            console.error('Error updating order:', error);
            alert('Failed to update order.');
        }
    };

    const handleDeleteOrder = async () => {
        if (!selectedOrder) return;
        try {
            await OrderService.deleteOrder(selectedOrder.id);
            setData(prevData => prevData.filter(o => o.id !== selectedOrder.id));
            setSelectedOrder(null);
        } catch (error) {
            console.error('Error deleting order:', error);
            alert('Failed to delete order.');
        }
    };

    return { handleAddOrder, handleUpdateOrder, handleDeleteOrder };
};

export default useOrderActions;