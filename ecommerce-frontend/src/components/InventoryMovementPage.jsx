import EntityPage from './shared/EntityPage';
import { inventoryMovementApi } from '../services/api';

const columns = [
  { key: 'id',          label: 'ID' },
  { key: 'productId',   label: 'Product ID' },
  { key: 'productName', label: 'Producto' },
  { key: 'orderId',     label: 'Order ID' },
  { key: 'type',        label: 'Tipo' },
  { key: 'qty',         label: 'Cantidad' },
];

const fields = [
  { key: 'productId', label: 'ID Producto', type: 'integer', required: true,  placeholder: '1' },
  { key: 'orderId',   label: 'ID Orden',    type: 'integer', required: false, placeholder: '1' },
  { key: 'type',      label: 'Tipo',        type: 'text',    required: true,  placeholder: 'IN / OUT' },
  { key: 'qty',       label: 'Cantidad',    type: 'integer', required: true,  placeholder: '10' },
];

export default function InventoryMovementPage() {
  return (
    <EntityPage
      title="InventoryMovement"
      endpoint="/inventory-movement"
      api={inventoryMovementApi}
      columns={columns}
      createFields={fields}
      updateFields={fields}
    />
  );
}
