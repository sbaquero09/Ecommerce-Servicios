import EntityPage from './shared/EntityPage';
import { inventoryApi } from '../services/api';

const columns = [
  { key: 'id',          label: 'ID' },
  { key: 'productId',   label: 'Product ID' },
  { key: 'productName', label: 'Producto' },
  { key: 'stock',       label: 'Stock' },
];

const createFields = [
  { key: 'productId', label: 'ID Producto', type: 'integer', required: true, placeholder: '1' },
  { key: 'stock',     label: 'Stock',       type: 'integer', required: true, placeholder: '100' },
];

// UpdateInventoryRequest only has stock
const updateFields = [
  { key: 'stock', label: 'Stock', type: 'integer', required: true, placeholder: '100' },
];

export default function InventoryPage() {
  return (
    <EntityPage
      title="Inventory"
      endpoint="/inventory"
      api={inventoryApi}
      columns={columns}
      createFields={createFields}
      updateFields={updateFields}
    />
  );
}
