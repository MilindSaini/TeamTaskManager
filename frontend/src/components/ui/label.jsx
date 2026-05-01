import { cn } from '../../lib/utils.js';

export function Label({ className, ...props }) {
  return (
    <label className={cn('block text-xs font-semibold uppercase tracking-widest text-neutral-light mb-2', className)} {...props} />
  );
}
