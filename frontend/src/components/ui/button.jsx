import { cn } from '../../lib/utils.js';

export function Button({ variant = 'primary', className, ...props }) {
  const base =
    'inline-flex items-center justify-center rounded-lg px-4 py-2.5 text-sm font-semibold transition-all duration-200 disabled:cursor-not-allowed disabled:opacity-50 focus:outline-none focus:ring-2 focus:ring-offset-2';
  const variants = {
    primary: 'bg-accent text-dark hover:bg-accent-dark focus:ring-accent/50 shadow-lg shadow-accent/20',
    outline: 'border border-accent text-accent hover:bg-accent hover:text-dark focus:ring-accent/50',
    ghost: 'text-neutral hover:bg-dark-tertiary hover:text-accent focus:ring-accent/50'
  };
  return <button className={cn(base, variants[variant], className)} {...props} />;
}
