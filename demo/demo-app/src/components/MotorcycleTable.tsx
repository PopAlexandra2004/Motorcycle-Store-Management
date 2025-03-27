// components/MotorcycleTable.tsx
import DataTable, { TableColumn } from 'react-data-table-component';
import Motorcycle from '../model/motorcycle.model';

interface MotorcycleTableProps {
    data: Motorcycle[];
    loading: boolean;
    isError: boolean;
    onRowSelected: (state: { selectedRows: Motorcycle[] }) => void;
    theme: 'light' | 'dark';
}

function MotorcycleTable({ data, loading, isError, onRowSelected, theme }: MotorcycleTableProps) {
    const columns: TableColumn<Motorcycle>[] = [
        { name: 'ID', selector: (row: Motorcycle) => row.id, sortable: true },
        { name: 'Brand', selector: (row: Motorcycle) => row.brand, sortable: true },
        { name: 'Model', selector: (row: Motorcycle) => row.model, sortable: true },
        { name: 'Year', selector: (row: Motorcycle) => row.year, sortable: true },
        { name: 'Price', selector: (row: Motorcycle) => `$${row.price.toFixed(2)}`, sortable: true },
        { name: 'Engine Capacity', selector: (row: Motorcycle) => `${row.engineCapacity} cc`, sortable: true },
        { name: 'Available Quantity', selector: (row: Motorcycle) => row.availableQuantity.toString(), sortable: true }, // ✅ fixed here
        { name: 'Color', selector: (row: Motorcycle) => row.color || '-', sortable: true },
    ];

    return (
        <>
            {loading ? (
                <p className="loading-text">Loading...</p>
            ) : isError ? (
                <p className="error-text">An error occurred while fetching data</p>
            ) : (
                <div className="table-container">
                    <DataTable
                        title="Motorcycles"
                        columns={columns}
                        data={data}
                        pagination
                        highlightOnHover
                        selectableRows
                        onSelectedRowsChange={onRowSelected}
                        theme={theme === "dark" ? "dark" : "default"}
                    />
                </div>
            )}
        </>
    );
}

export default MotorcycleTable;