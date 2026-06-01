import EntityPage from './shared/EntityPage';
import { cartsApi } from '../services/api';

const columns = [
  { key: 'id',           label: 'ID' },
  { key: 'userId',       label: 'User ID' },
  { key: 'userFullName', label: 'Usuario' },
  { key: 'status',       label: 'Estado' },
];

const fields = [
  { key: 'userId', label: 'ID Usuario', type: 'integer', required: true,  placeholder: '1' },
  { key: 'status', label: 'Estado',     type: 'text',    required: true,  placeholder: 'ACTIVE' },
];

export default function CartsPage() {
  return (
    <EntityPage
      title="Carts"
      endpoint="/cart"
      api={cartsApi}
      columns={columns}
      createFields={fields}
      updateFields={fields}
    />
  );
}
