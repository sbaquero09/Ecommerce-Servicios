import axios from 'axios';

const BASE_URL = 'http://localhost:8080';

const client = axios.create({ baseURL: BASE_URL });

// Generic error extractor — returns a readable message from API errors
const handleError = (err) => {
  const data = err?.response?.data;
  if (data) {
    if (typeof data === 'string') throw new Error(data);
    if (data.message) throw new Error(`[${data.status || err.response.status}] ${data.message}`);
    throw new Error(JSON.stringify(data));
  }
  throw new Error(err.message || 'Error de red');
};

const get    = (url)       => client.get(url).then(r => r.data).catch(handleError);
const post   = (url, data) => client.post(url, data).then(r => r.data).catch(handleError);
const put    = (url, data) => client.put(url, data).then(r => r.data).catch(handleError);
const del    = (url)       => client.delete(url).then(r => r.data).catch(handleError);

// ─── Users ─────────────────────────────────────────────────────
export const usersApi = {
  getAll:      ()           => get('/user/all'),
  getById:     (id)         => get(`/user/${id}`),
  getByEmail:  (email)      => get(`/user/email/${email}`),
  create:      (data)       => post('/user', data),
  update:      (id, data)   => put(`/user/${id}`, data),
  delete:      (id)         => del(`/user/${id}`),
};

// ─── Products ──────────────────────────────────────────────────
export const productsApi = {
  getAll:  ()           => get('/product/all'),
  getById: (id)         => get(`/product/${id}`),
  create:  (data)       => post('/product', data),
  update:  (id, data)   => put(`/product/${id}`, data),
  delete:  (id)         => del(`/product/${id}`),
};

// ─── Categories ────────────────────────────────────────────────
export const categoriesApi = {
  getAll:  ()           => get('/category/all'),
  getById: (id)         => get(`/category/${id}`),
  create:  (data)       => post('/category', data),
  update:  (id, data)   => put(`/category/${id}`, data),
  delete:  (id)         => del(`/category/${id}`),
};

// ─── Orders ────────────────────────────────────────────────────
export const ordersApi = {
  getAll:  ()           => get('/order/all'),
  getById: (id)         => get(`/order/${id}`),
  create:  (data)       => post('/order', data),
  update:  (id, data)   => put(`/order/${id}`, data),
  delete:  (id)         => del(`/order/${id}`),
};

// ─── Carts ─────────────────────────────────────────────────────
export const cartsApi = {
  getAll:  ()           => get('/cart/all'),
  getById: (id)         => get(`/cart/${id}`),
  create:  (data)       => post('/cart', data),
  update:  (id, data)   => put(`/cart/${id}`, data),
  delete:  (id)         => del(`/cart/${id}`),
};

// ─── CartItems ─────────────────────────────────────────────────
export const cartItemsApi = {
  getAll:  ()           => get('/cart-item/all'),
  getById: (id)         => get(`/cart-item/${id}`),
  create:  (data)       => post('/cart-item', data),
  update:  (id, data)   => put(`/cart-item/${id}`, data),
  delete:  (id)         => del(`/cart-item/${id}`),
};

// ─── OrderItems ────────────────────────────────────────────────
export const orderItemsApi = {
  getAll:  ()           => get('/order-item/all'),
  getById: (id)         => get(`/order-item/${id}`),
  create:  (data)       => post('/order-item', data),
  update:  (id, data)   => put(`/order-item/${id}`, data),
  delete:  (id)         => del(`/order-item/${id}`),
};

// ─── Payments ──────────────────────────────────────────────────
export const paymentsApi = {
  getAll:  ()           => get('/payment/all'),
  getById: (id)         => get(`/payment/${id}`),
  create:  (data)       => post('/payment', data),
  update:  (id, data)   => put(`/payment/${id}`, data),
  delete:  (id)         => del(`/payment/${id}`),
};

// ─── Inventory ─────────────────────────────────────────────────
export const inventoryApi = {
  getAll:  ()           => get('/inventory/all'),
  getById: (id)         => get(`/inventory/${id}`),
  create:  (data)       => post('/inventory', data),
  update:  (id, data)   => put(`/inventory/${id}`, data),
  delete:  (id)         => del(`/inventory/${id}`),
};

// ─── InventoryMovement ─────────────────────────────────────────
export const inventoryMovementApi = {
  getAll:  ()           => get('/inventory-movement/all'),
  getById: (id)         => get(`/inventory-movement/${id}`),
  create:  (data)       => post('/inventory-movement', data),
  update:  (id, data)   => put(`/inventory-movement/${id}`, data),
  delete:  (id)         => del(`/inventory-movement/${id}`),
};

// ─── DocumentType (read-only) ──────────────────────────────────
export const documentTypeApi = {
  getAll:  ()           => get('/document-type/all'),
  getById: (id)         => get(`/document-type/${id}`),
};

// ─── ProductCategory ───────────────────────────────────────────
export const productCategoryApi = {
  getAll:  ()           => get('/product-category/all'),
  getById: (id)         => get(`/product-category/${id}`),
  create:  (data)       => post('/product-category', data),
  // PUT returns 400 by design — no update method exposed
  delete:  (id)         => del(`/product-category/${id}`),
};
