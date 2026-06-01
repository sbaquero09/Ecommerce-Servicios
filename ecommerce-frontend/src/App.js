import { useState } from 'react';
import './App.css';

import UsersPage              from './components/UsersPage';
import ProductsPage           from './components/ProductsPage';
import CategoriesPage         from './components/CategoriesPage';
import OrdersPage             from './components/OrdersPage';
import CartsPage              from './components/CartsPage';
import CartItemsPage          from './components/CartItemsPage';
import OrderItemsPage         from './components/OrderItemsPage';
import PaymentsPage           from './components/PaymentsPage';
import InventoryPage          from './components/InventoryPage';
import InventoryMovementPage  from './components/InventoryMovementPage';
import DocumentTypePage       from './components/DocumentTypePage';
import ProductCategoryPage    from './components/ProductCategoryPage';

const SECTIONS = [
  {
    label: 'Usuarios & Auth',
    items: [
      { id: 'users',        label: 'Users',            icon: '👤', component: UsersPage,             endpoint: '/user' },
      { id: 'doctype',      label: 'Document Types',   icon: '🪪', component: DocumentTypePage,      endpoint: '/document-type', readOnly: true },
    ],
  },
  {
    label: 'Catálogo',
    items: [
      { id: 'products',     label: 'Products',         icon: '📦', component: ProductsPage,          endpoint: '/product' },
      { id: 'categories',   label: 'Categories',       icon: '🏷', component: CategoriesPage,        endpoint: '/category' },
      { id: 'prodcat',      label: 'Product Category', icon: '🔗', component: ProductCategoryPage,   endpoint: '/product-category' },
    ],
  },
  {
    label: 'Inventario',
    items: [
      { id: 'inventory',    label: 'Inventory',        icon: '🗄', component: InventoryPage,         endpoint: '/inventory' },
      { id: 'invmov',       label: 'Inv. Movement',    icon: '🔄', component: InventoryMovementPage, endpoint: '/inventory-movement' },
    ],
  },
  {
    label: 'Ventas',
    items: [
      { id: 'carts',        label: 'Carts',            icon: '🛒', component: CartsPage,             endpoint: '/cart' },
      { id: 'cartitems',    label: 'Cart Items',       icon: '➕', component: CartItemsPage,         endpoint: '/cart-item' },
      { id: 'orders',       label: 'Orders',           icon: '📋', component: OrdersPage,            endpoint: '/order' },
      { id: 'orderitems',   label: 'Order Items',      icon: '📝', component: OrderItemsPage,        endpoint: '/order-item' },
      { id: 'payments',     label: 'Payments',         icon: '💳', component: PaymentsPage,          endpoint: '/payment' },
    ],
  },
];

const ALL_ITEMS = SECTIONS.flatMap(s => s.items);

export default function App() {
  const [activeId, setActiveId] = useState('users');
  const active = ALL_ITEMS.find(i => i.id === activeId);
  const ActiveComponent = active?.component;

  return (
    <div className="app-shell">
      {/* ── SIDEBAR ── */}
      <aside className="sidebar">
        <div className="sidebar-logo">
          <h1>⚡ EcommerceUSB</h1>
          <p>Admin Panel · Spring Boot API</p>
        </div>

        <nav className="sidebar-nav">
          {SECTIONS.map(section => (
            <div key={section.label}>
              <div className="nav-section-label">{section.label}</div>
              {section.items.map(item => (
                <div
                  key={item.id}
                  className={`nav-item ${activeId === item.id ? 'active' : ''}`}
                  onClick={() => setActiveId(item.id)}
                >
                  <span className="nav-icon">{item.icon}</span>
                  <span>{item.label}</span>
                  {item.readOnly && (
                    <span style={{ marginLeft: 'auto', fontSize: 9, color: 'var(--text-dim)', fontFamily: 'var(--mono)' }}>
                      R/O
                    </span>
                  )}
                </div>
              ))}
            </div>
          ))}
        </nav>

        <div style={{ padding: '14px 20px', borderTop: '1px solid var(--border)' }}>
          <div style={{ fontFamily: 'var(--mono)', fontSize: 10, color: 'var(--text-dim)' }}>
            API Base URL
          </div>
          <div style={{ fontFamily: 'var(--mono)', fontSize: 11, color: 'var(--text-sec)', marginTop: 4 }}>
            localhost:8080
          </div>
        </div>
      </aside>

      {/* ── MAIN CONTENT ── */}
      <main className="main-content">
        <div className="topbar">
          <div style={{ display: 'flex', alignItems: 'center', gap: 12 }}>
            <span style={{ fontSize: 20 }}>{active?.icon}</span>
            <h2>{active?.label}</h2>
          </div>
          <div style={{ display: 'flex', alignItems: 'center', gap: 10 }}>
            <span className="api-url">
              http://localhost:8080{active?.endpoint}
            </span>
            <a
              href="http://localhost:8080/swagger-ui/index.html"
              target="_blank"
              rel="noreferrer"
              className="btn btn-ghost"
              style={{ fontSize: 11, padding: '4px 10px' }}
            >
              Swagger UI ↗
            </a>
          </div>
        </div>

        <div className="page-body">
          {/* ── HTTP METHOD LEGEND ── */}
          <div style={{
            display: 'flex', gap: 10, flexWrap: 'wrap', marginBottom: 20,
            padding: '10px 14px',
            background: 'var(--surface)',
            border: '1px solid var(--border)',
            borderRadius: 'var(--radius)',
            alignItems: 'center',
          }}>
            <span style={{ fontSize: 11, color: 'var(--text-dim)', fontFamily: 'var(--mono)', marginRight: 6 }}>
              HTTP METHODS:
            </span>
            {['GET','POST','PUT','DELETE'].map(m => (
              <span key={m} className={`badge badge-${m}`}>{m}</span>
            ))}
            <span style={{ fontSize: 11, color: 'var(--text-dim)', marginLeft: 'auto', fontFamily: 'var(--mono)' }}>
              Swagger: localhost:8080/swagger-ui/index.html
            </span>
          </div>

          {ActiveComponent && <ActiveComponent />}
        </div>
      </main>
    </div>
  );
}
