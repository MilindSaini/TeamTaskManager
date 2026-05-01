import { NavLink } from 'react-router-dom';
import { useAuthStore } from '../stores/authStore.js';
import { Button } from './ui/button.jsx';

export default function Navbar() {
  const { user, logout } = useAuthStore();

  return (
    <header className="border-b border-dark-tertiary bg-dark-secondary shadow-lg shadow-black/30 px-6 sticky top-0 z-50">
      <div className="mx-auto flex max-w-7xl items-center justify-between py-4">
        <div className="flex items-center gap-3">
          <div className="flex h-11 w-11 items-center justify-center rounded-lg bg-gradient-to-br from-accent to-accent-dark font-bold text-dark font-display text-lg">
            ◆
          </div>
          <div>
            <div className="text-lg font-semibold text-ink font-display">Team Task Manager</div>
            <div className="text-xs text-neutral tracking-wide">Human-aligned task coordination</div>
          </div>
        </div>
        <nav className="flex items-center gap-8 text-sm font-semibold">
          <NavLink
            className={({ isActive }) =>
              `transition-colors duration-200 ${isActive ? 'text-accent-light' : 'text-neutral hover:text-ink'}`
            }
            to="/dashboard"
          >
            Dashboard
          </NavLink>
          <NavLink
            className={({ isActive }) =>
              `transition-colors duration-200 ${isActive ? 'text-accent-light' : 'text-neutral hover:text-ink'}`
            }
            to="/projects"
          >
            Projects
          </NavLink>
        </nav>
        <div className="flex items-center gap-4">
          <div className="text-xs text-neutral">{user?.name}</div>
          <Button variant="ghost" onClick={logout}>
            Logout
          </Button>
        </div>
      </div>
    </header>
  );
}
