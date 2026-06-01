import EntityPage from './shared/EntityPage';
import { productsApi } from '../services/api';

const columns = [
  { key: 'id',          label: 'ID' },
  { key: 'name',        label: 'Nombre' },
  { key: 'description', label: 'Descripción' },
  { key: 'price',       label: 'Precio' },
  { key: 'available',   label: 'Disponible' },
];

const fields = [
  { key: 'name',        label: 'Nombre',       type: 'text',    required: true,  placeholder: 'Producto XYZ' },
  { key: 'description', label: 'Descripción',  type: 'text',    required: false, placeholder: 'Descripción del producto' },
  { key: 'price',       label: 'Precio',       type: 'decimal', required: true,  placeholder: '19.99' },
  { key: 'available',   label: 'Disponible',   type: 'boolean', required: false },
];

export default function ProductsPage() {
  return (
    <EntityPage
      title="Products"
      endpoint="/product"
      api={productsApi}
      columns={columns}
      createFields={fields}
      updateFields={fields}
    />
  );
}
