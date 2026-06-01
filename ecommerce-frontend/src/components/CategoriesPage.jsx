import EntityPage from './shared/EntityPage';
import { categoriesApi } from '../services/api';

const columns = [
  { key: 'id',         label: 'ID' },
  { key: 'name',       label: 'Nombre' },
  { key: 'parentId',   label: 'Parent ID' },
  { key: 'parentName', label: 'Categoría padre' },
];

const fields = [
  { key: 'name',     label: 'Nombre',            type: 'text',    required: true,  placeholder: 'Electrónica' },
  { key: 'parentId', label: 'ID Categoría Padre', type: 'integer', required: false, placeholder: 'Dejar vacío si es raíz' },
];

export default function CategoriesPage() {
  return (
    <EntityPage
      title="Categories"
      endpoint="/category"
      api={categoriesApi}
      columns={columns}
      createFields={fields}
      updateFields={fields}
    />
  );
}
