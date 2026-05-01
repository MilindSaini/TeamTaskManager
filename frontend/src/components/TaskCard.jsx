import { Badge } from './ui/badge.jsx';
import { Button } from './ui/button.jsx';

const priorityTone = {
  LOW: 'neutral',
  MEDIUM: 'accent',
  HIGH: 'warning',
  URGENT: 'danger'
};

const statuses = ['TODO', 'IN_PROGRESS', 'BLOCKED', 'DONE'];

export default function TaskCard({ task, canDelete, onDelete, onStatusChange }) {
  return (
    <div className="rounded-lg border border-dark-tertiary bg-dark-tertiary/50 p-4 shadow-md shadow-black/10 hover:border-accent/50 hover:shadow-accent/20 transition-all duration-200 group">
      <div className="flex items-start justify-between gap-2 mb-3">
        <div className="flex-1">
          <h4 className="text-sm font-semibold text-ink line-clamp-2">{task.title}</h4>
          <p className="text-xs text-neutral line-clamp-2 mt-1">{task.description || 'No description'}</p>
        </div>
        <Badge tone={priorityTone[task.priority] || 'neutral'} className="shrink-0">
          {task.priority}
        </Badge>
      </div>

      <div className="space-y-2 py-3 border-y border-dark-tertiary text-xs">
        <div className="flex items-center justify-between">
          <span className="text-neutral">Assignee:</span>
          <span className="text-neutral-light font-medium">{task.assignee?.name || 'Unassigned'}</span>
        </div>
        <div className="flex items-center justify-between">
          <span className="text-neutral">Due Date:</span>
          <span className="text-neutral-light font-medium">{task.dueDate || 'No date'}</span>
        </div>
      </div>

      <div className="mt-3 flex items-center gap-2">
        <select
          value={task.status}
          onChange={(event) => onStatusChange(task.id, event.target.value)}
          className="flex-1 rounded-lg border border-dark-tertiary bg-dark-secondary px-2 py-2 text-xs text-ink outline-none transition focus:border-accent focus:ring-2 focus:ring-accent/20"
        >
          {statuses.map((status) => (
            <option key={status} value={status}>
              {status.replaceAll('_', ' ')}
            </option>
          ))}
        </select>
        {canDelete ? (
          <Button
            type="button"
            variant="ghost"
            className="shrink-0 px-2 py-2 text-danger hover:bg-danger/10 opacity-0 group-hover:opacity-100 transition-opacity"
            onClick={() => onDelete(task.id)}
            title="Delete task"
          >
            ✕
          </Button>
        ) : null}
      </div>
    </div>
  );
}
