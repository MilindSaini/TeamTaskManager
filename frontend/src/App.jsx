import { Navigate, Outlet, Route, Routes } from 'react-router-dom';
import Navbar from './components/Navbar.jsx';
import Login from './pages/Login.jsx';
import Register from './pages/Register.jsx';
import Dashboard from './pages/Dashboard.jsx';
import Projects from './pages/Projects.jsx';
import TaskBoard from './pages/TaskBoard.jsx';
import { useAuthStore } from './stores/authStore.js';

function RequireAuth() {
  const token = useAuthStore((state) => state.token);
  if (!token) {
    return <Navigate to="/login" replace />;
  }
  return (
    <div className="app-shell">
      <Navbar />
      <main className="mx-auto w-full max-w-7xl flex-1 px-6 pb-16 pt-6">
        <Outlet />
      </main>
    </div>
  );
}

function PublicOnly({ children }) {
  const token = useAuthStore((state) => state.token);
  if (token) {
    return <Navigate to="/dashboard" replace />;
  }
  return children;
}

export default function App() {
  return (
    <Routes>
      <Route
        path="/login"
        element={
          <PublicOnly>
            <Login />
          </PublicOnly>
        }
      />
      <Route
        path="/register"
        element={
          <PublicOnly>
            <Register />
          </PublicOnly>
        }
      />
      <Route element={<RequireAuth />}>
        <Route path="/" element={<Navigate to="/dashboard" replace />} />
        <Route path="/dashboard" element={<Dashboard />} />
        <Route path="/projects" element={<Projects />} />
        <Route path="/projects/:projectId/board" element={<TaskBoard />} />
      </Route>
      <Route path="*" element={<Navigate to="/dashboard" replace />} />
    </Routes>
  );
}
