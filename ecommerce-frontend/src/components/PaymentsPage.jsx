import EntityPage from './shared/EntityPage';
import { paymentsApi } from '../services/api';

const columns = [
  { key: 'id',             label: 'ID' },
  { key: 'orderId',        label: 'Order ID' },
  { key: 'status',         label: 'Estado' },
  { key: 'providerRef',    label: 'Ref. Proveedor' },
  { key: 'idempotencyKey', label: 'Idempotency Key' },
];

const fields = [
  { key: 'orderId',        label: 'ID Orden',         type: 'integer', required: true,  placeholder: '1' },
  { key: 'status',         label: 'Estado',           type: 'text',    required: true,  placeholder: 'PENDING' },
  { key: 'providerRef',    label: 'Ref. Proveedor',   type: 'text',    required: false, placeholder: 'stripe_ch_xxx' },
  { key: 'idempotencyKey', label: 'Idempotency Key',  type: 'text',    required: false, placeholder: 'uuid-v4-aqui' },
];

export default function PaymentsPage() {
  return (
    <EntityPage
      title="Payments"
      endpoint="/payment"
      api={paymentsApi}
      columns={columns}
      createFields={fields}
      updateFields={fields}
    />
  );
}
