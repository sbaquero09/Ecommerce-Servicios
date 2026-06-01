import EntityPage from './shared/EntityPage';
import { usersApi } from '../services/api';

const columns = [
  { key: 'id',               label: 'ID' },
  { key: 'fullName',         label: 'Nombre completo' },
  { key: 'email',            label: 'Email' },
  { key: 'documentTypeId',   label: 'Tipo Doc. ID' },
  { key: 'documentTypeName', label: 'Tipo Doc.' },
  { key: 'documentNumber',   label: 'N° Documento' },
];

const fields = [
  { key: 'fullName',       label: 'Nombre completo',  type: 'text',    required: true,  placeholder: 'Juan Pérez' },
  { key: 'phone',          label: 'Teléfono',         type: 'text',    required: false, placeholder: '+573001234567' },
  { key: 'email',          label: 'Email',            type: 'text',    required: true,  placeholder: 'juan@email.com' },
  { key: 'documentTypeId', label: 'ID Tipo Documento',type: 'integer', required: true,  placeholder: '1' },
  { key: 'documentNumber', label: 'N° Documento',     type: 'text',    required: true,  placeholder: '123456789' },
  { key: 'birthDate',      label: 'Fecha Nacimiento', type: 'text',    required: false, placeholder: 'YYYY-MM-DD' },
  { key: 'country',        label: 'País',             type: 'text',    required: false, placeholder: 'Colombia' },
  { key: 'address',        label: 'Dirección',        type: 'text',    required: false, placeholder: 'Calle 1 # 2-3' },
];

export default function UsersPage() {
  return (
    <EntityPage
      title="Users"
      endpoint="/user"
      api={usersApi}
      columns={columns}
      createFields={fields}
      updateFields={fields}
    />
  );
}
