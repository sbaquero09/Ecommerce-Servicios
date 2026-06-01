import EntityPage from './shared/EntityPage';
import { documentTypeApi } from '../services/api';

const columns = [
  { key: 'id',   label: 'ID' },
  { key: 'code', label: 'Código' },
  { key: 'name', label: 'Nombre' },
];

export default function DocumentTypePage() {
  return (
    <EntityPage
      title="DocumentType"
      endpoint="/document-type"
      api={documentTypeApi}
      columns={columns}
      createFields={[]}
      readOnly={true}
      infoBanner="Este recurso es de solo lectura. La API no expone endpoints de creación, edición ni eliminación."
    />
  );
}
