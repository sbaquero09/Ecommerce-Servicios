import EntityPage from './shared/EntityPage';
import { ordersApi } from '../services/api';

const columns = [
  { key: 'id',           label: 'ID' },
  { key: 'userId',       label: 'User ID' },
  { key: 'userFullName', label: 'Cliente' },
  { key: 'status',       label: 'Estado' },
  { key: 'totalAmount',  label: 'Total' },
  { key: 'currency',     label: 'Moneda' },
];

const fields = [
  { key: 'userId',      label: 'ID Usuario',    type: 'integer', required: true,  placeholder: '1' },
  { key: 'status',      label: 'Estado',        type: 'text',    required: true,  placeholder: 'PENDING' },
  { key: 'totalAmount', label: 'Total',         type: 'decimal', required: true,  placeholder: '99.99' },
  { key: 'currency',    label: 'Moneda',        type: 'text',    required: true,  placeholder: 'COP' },
];

export default function OrdersPage() {
  return (
    <EntityPage
      title="Orders"
      endpoint="/order"
      api={ordersApi}
      columns={columns}
      createFields={fields}
      updateFields={fields}
    />
  );
}
