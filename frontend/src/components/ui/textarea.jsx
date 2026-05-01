import { cn } from '../../lib/utils.js';

export function Textarea({ className, ...props }) {
  return (
    <textarea
      className={cn(
        'w-full rounded-lg border border-dark-tertiary bg-dark-secondary px-3 py-2.5 text-sm text-ink outline-none transition duration-200 placeholder:text-neutral focus:border-accent focus:ring-2 focus:ring-accent/20 focus:ring-offset-dark focus:ring-offset-2 resize-none',
        className
      )}
      {...props}
    />
  );
}
