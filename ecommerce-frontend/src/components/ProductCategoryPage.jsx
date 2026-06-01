import EntityPage from './shared/EntityPage';
import { productCategoryApi } from '../services/api';

const columns = [
  { key: 'id',           label: 'ID' },
  { key: 'productId',    label: 'Product ID' },
  { key: 'productName',  label: 'Producto' },
  { key: 'categoryId',   label: 'Category ID' },
  { key: 'categoryName', label: 'Categoría' },
];

const createFields = [
  { key: 'productId',  label: 'ID Producto',   type: 'integer', required: true, placeholder: '1' },
  { key: 'categoryId', label: 'ID Categoría',  type: 'integer', required: true, placeholder: '1' },
];

export default function ProductCategoryPage() {
  return (
    <EntityPage
      title="ProductCategory"
      endpoint="/product-category"
      api={productCategoryApi}
      columns={columns}
      createFields={createFields}
      noUpdate={true}
      infoBanner="PUT no disponible: la combinación product_id + category_id es única e inmutable. Para cambiar la relación, elimina este registro y crea uno nuevo."
    />
  );
}
