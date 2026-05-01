/** @type {import('tailwindcss').Config} */
export default {
  content: ['./index.html', './src/**/*.{js,jsx}'],
  theme: {
    extend: {
      fontFamily: {
        display: ['"Space Grotesk"', '"Segoe UI"', 'sans-serif'],
        body: ['"DM Sans"', '"Segoe UI"', 'sans-serif']
      },
      colors: {
        dark: '#0a0f1f',
        'dark-secondary': '#1a2332',
        'dark-tertiary': '#2d3748',
        accent: '#06b6d4',
        'accent-dark': '#0891b2',
        'accent-light': '#22d3ee',
        neutral: '#94a3b8',
        'neutral-light': '#cbd5e1',
        success: '#10b981',
        warning: '#f59e0b',
        danger: '#ef4444',
        'slate-900': '#0f172a',
        'slate-800': '#1e293b',
        'slate-700': '#334155',
        'slate-600': '#475569'
      }
    }
  },
  plugins: []
};
