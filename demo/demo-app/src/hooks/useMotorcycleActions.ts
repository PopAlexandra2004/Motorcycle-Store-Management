// hooks/useMotorcycleActions.ts
import Motorcycle from '../model/motorcycle.model';
import { MotorcycleService } from '../service/MotorcycleService';

interface UseMotorcycleActionsProps {
    setData: React.Dispatch<React.SetStateAction<Motorcycle[]>>;
    setSelectedMotorcycle: React.Dispatch<React.SetStateAction<Motorcycle | null>>;
    selectedMotorcycle: Motorcycle | null;
}

const useMotorcycleActions = ({ setData, setSelectedMotorcycle, selectedMotorcycle }: UseMotorcycleActionsProps) => {
    const handleAddMotorcycle = async (motorcycle: Motorcycle) => {
        try {
            const addedMotorcycle = await MotorcycleService.addMotorcycle(motorcycle);
            setData(prevData => [...prevData, addedMotorcycle]);
        } catch (error: unknown) {
            console.error('❌ Error adding motorcycle:', error);
            if (error instanceof Error) {
                alert(error.message || '❌ Failed to add motorcycle.');
            } else {
                alert('❌ Unknown error while adding motorcycle.');
            }
        }
    };

    const handleUpdateMotorcycle = async (motorcycle: Motorcycle) => {
        if (!selectedMotorcycle) return;
        try {
            await MotorcycleService.updateMotorcycle(motorcycle);
            setData(prevData =>
                prevData.map(m => (m.id === selectedMotorcycle.id ? motorcycle : m))
            );
        } catch (error: unknown) {
            console.error('❌ Error updating motorcycle:', error);
            if (error instanceof Error) {
                alert(error.message || '❌ Failed to update motorcycle.');
            } else {
                alert('❌ Unknown error while updating motorcycle.');
            }
        }
    };

    const handleDeleteMotorcycle = async () => {
        if (!selectedMotorcycle) return;
        try {
            await MotorcycleService.deleteMotorcycle(selectedMotorcycle.id);
            setData(prevData => prevData.filter(m => m.id !== selectedMotorcycle.id));
            setSelectedMotorcycle(null);
        } catch (error: unknown) {
            console.error('❌ FULL DELETE ERROR:', error);
            if (error instanceof Error) {
                alert(error.message || '❌ Failed to delete motorcycle.');
            } else {
                alert('❌ Unknown error occurred while deleting motorcycle.');
            }
        }
    };

    return { handleAddMotorcycle, handleUpdateMotorcycle, handleDeleteMotorcycle };
};

export default useMotorcycleActions;