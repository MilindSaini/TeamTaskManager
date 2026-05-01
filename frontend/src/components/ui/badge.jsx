import { cn } from '../../lib/utils.js';

export function Badge({ tone = 'neutral', className, ...props }) {
  const tones = {
    neutral: 'bg-dark-tertiary text-neutral-light',
    accent: 'bg-accent/20 text-accent-light',
    success: 'bg-success/20 text-success',
    warning: 'bg-warning/20 text-warning',
    danger: 'bg-danger/20 text-danger'
  };
  return (
    <span
      className={cn('rounded-full px-3 py-1.5 text-xs font-semibold tracking-wide', tones[tone] || tones.neutral, className)}
      {...props}
    />
  );
}
