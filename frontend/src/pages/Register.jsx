import { useState } from 'react';
import { Link } from 'react-router-dom';
import { Button } from '../components/ui/button.jsx';
import { Card } from '../components/ui/card.jsx';
import { Input } from '../components/ui/input.jsx';
import { Label } from '../components/ui/label.jsx';
import { useAuthStore } from '../stores/authStore.js';

export default function Register() {
  const register = useAuthStore((state) => state.register);
  const [form, setForm] = useState({ name: '', email: '', password: '' });
  const [error, setError] = useState('');
  const [busy, setBusy] = useState(false);

  const handleChange = (event) => {
    const { name, value } = event.target;
    setForm((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    setError('');
    setBusy(true);
    try {
      await register(form.name, form.email, form.password);
    } catch (err) {
      setError(err.response?.data?.message || 'Registration failed.');
    } finally {
      setBusy(false);
    }
  };

  return (
    <div className="min-h-screen flex flex-col items-center justify-center px-6 relative overflow-hidden">
      <div className="absolute inset-0 bg-gradient-to-br from-dark via-dark-secondary to-dark-tertiary" />
      <div className="absolute top-0 right-0 w-96 h-96 bg-accent/5 rounded-full blur-3xl" />
      <div className="absolute bottom-0 left-0 w-96 h-96 bg-accent/5 rounded-full blur-3xl" />

      <div className="w-full max-w-md relative z-10">
        <Card>
          <div className="text-center mb-8">
            <div className="flex h-14 w-14 items-center justify-center rounded-lg bg-gradient-to-br from-accent to-accent-dark font-bold text-dark font-display text-2xl mx-auto mb-4">
              ◆
            </div>
            <h1 className="text-3xl font-semibold mb-2">Create your workspace</h1>
            <p className="text-sm text-neutral">
              Join your team in coordinated task execution
            </p>
          </div>

          <form className="grid gap-5" onSubmit={handleSubmit}>
            <div>
              <Label>Full Name</Label>
              <Input
                name="name"
                value={form.name}
                onChange={handleChange}
                placeholder="John Doe"
                required
              />
            </div>
            <div>
              <Label>Email Address</Label>
              <Input
                name="email"
                type="email"
                value={form.email}
                onChange={handleChange}
                placeholder="you@example.com"
                required
              />
            </div>
            <div>
              <Label>Password</Label>
              <Input
                name="password"
                type="password"
                value={form.password}
                onChange={handleChange}
                placeholder="••••••••"
                required
              />
            </div>
            {error ? (
              <div className="text-sm text-danger bg-danger/10 border border-danger/20 rounded-lg p-3">
                {error}
              </div>
            ) : null}
            <Button disabled={busy} type="submit" className="w-full">
              {busy ? 'Creating...' : 'Create account'}
            </Button>
          </form>

          <div className="mt-6 pt-6 border-t border-dark-tertiary text-center text-xs text-neutral">
            Already onboarded?{' '}
            <Link className="text-accent-light font-semibold hover:text-accent-light/80 transition-colors" to="/login">
              Sign in here
            </Link>
          </div>
        </Card>
      </div>
    </div>
  );
}
