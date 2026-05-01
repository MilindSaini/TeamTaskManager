import { cn } from '../../lib/utils.js';

export function Card({ className, ...props }) {
  return (
    <div
      className={cn(
        'rounded-xl border border-dark-tertiary bg-dark-secondary shadow-lg shadow-black/20 p-6 transition-all hover:shadow-xl hover:shadow-black/30',
        className
      )}
      {...props}
    />
  );
}
