import EntityPage from './shared/EntityPage';
import { cartItemsApi } from '../services/api';

const columns = [
  { key: 'id',          label: 'ID' },
  { key: 'cartId',      label: 'Cart ID' },
  { key: 'productId',   label: 'Product ID' },
  { key: 'productName', label: 'Producto' },
  { key: 'quantity',    label: 'Cantidad' },
];

const fields = [
  { key: 'cartId',    label: 'ID Carrito',  type: 'integer', required: true, placeholder: '1' },
  { key: 'productId', label: 'ID Producto', type: 'integer', required: true, placeholder: '1' },
  { key: 'quantity',  label: 'Cantidad',    type: 'integer', required: true, placeholder: '1' },
];

export default function CartItemsPage() {
  return (
    <EntityPage
      title="CartItems"
      endpoint="/cart-item"
      api={cartItemsApi}
      columns={columns}
      createFields={fields}
      updateFields={fields}
    />
  );
}
