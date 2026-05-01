import { create } from 'zustand';
import api from '../api/axios.js';

const tokenKey = 'ttm_token';
const userKey = 'ttm_user';

const readUser = () => {
  try {
    const stored = localStorage.getItem(userKey);
    return stored ? JSON.parse(stored) : null;
  } catch (error) {
    return null;
  }
};

export const useAuthStore = create((set) => ({
  token: localStorage.getItem(tokenKey),
  user: readUser(),
  async login(email, password) {
    const { data } = await api.post('/api/auth/login', { email, password });
    localStorage.setItem(tokenKey, data.token);
    localStorage.setItem(userKey, JSON.stringify(data.user));
    set({ token: data.token, user: data.user });
    return data;
  },
  async register(name, email, password) {
    const { data } = await api.post('/api/auth/register', { name, email, password });
    localStorage.setItem(tokenKey, data.token);
    localStorage.setItem(userKey, JSON.stringify(data.user));
    set({ token: data.token, user: data.user });
    return data;
  },
  logout() {
    localStorage.removeItem(tokenKey);
    localStorage.removeItem(userKey);
    set({ token: null, user: null });
  }
}));
