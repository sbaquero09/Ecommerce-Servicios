import EntityPage from './shared/EntityPage';
import { orderItemsApi } from '../services/api';

const columns = [
  { key: 'id',                 label: 'ID' },
  { key: 'orderId',            label: 'Order ID' },
  { key: 'productId',          label: 'Product ID' },
  { key: 'productName',        label: 'Producto' },
  { key: 'quantity',           label: 'Cantidad' },
  { key: 'unitPriceSnapshot',  label: 'Precio Unit.' },
  { key: 'lineTotal',          label: 'Total Línea' },
];

const fields = [
  { key: 'orderId',           label: 'ID Orden',        type: 'integer', required: true,  placeholder: '1' },
  { key: 'productId',         label: 'ID Producto',     type: 'integer', required: true,  placeholder: '1' },
  { key: 'quantity',          label: 'Cantidad',        type: 'integer', required: true,  placeholder: '1' },
  { key: 'unitPriceSnapshot', label: 'Precio Unitario', type: 'decimal', required: true,  placeholder: '19.99' },
  { key: 'lineTotal',         label: 'Total Línea',     type: 'decimal', required: true,  placeholder: '39.98' },
];

export default function OrderItemsPage() {
  return (
    <EntityPage
      title="OrderItems"
      endpoint="/order-item"
      api={orderItemsApi}
      columns={columns}
      createFields={fields}
      updateFields={fields}
    />
  );
}
