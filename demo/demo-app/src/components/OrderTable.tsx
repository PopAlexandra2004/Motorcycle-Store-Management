// components/OrderTable.tsx
import DataTable, { TableColumn } from 'react-data-table-component';
import Order from '../model/order.model';

interface OrderTableProps {
    data: Order[];
    loading: boolean;
    isError: boolean;
    onRowSelected: (state: { selectedRows: Order[] }) => void;
    theme: 'light' | 'dark';
}

function OrderTable({ data, loading, isError, onRowSelected, theme }: OrderTableProps) {
    const columns: TableColumn<Order>[] = [
        { name: 'ID', selector: (row: Order) => row.id, sortable: true },
        { name: 'Person', selector: (row: Order) => row.person.name, sortable: true },
        { name: 'Motorcycles', selector: (row: Order) => row.motorcycles.map(m => `${m.motorcycle.brand} ${m.motorcycle.model} (x${m.quantity})`).join(', '), sortable: false },
        { name: 'Date', selector: (row: Order) => new Date(row.date).toLocaleString(), sortable: true },
        { name: 'Total Cost', selector: (row: Order) => `$${row.totalCost.toFixed(2)}`, sortable: true },
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
                        title="Orders"
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

export default OrderTable;