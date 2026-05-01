import TaskCard from './TaskCard.jsx';

export default function KanbanColumn({ title, tasks, canDelete, onDelete, onStatusChange }) {
  return (
    <div className="flex min-h-96 flex-col gap-4 rounded-lg border border-dark-tertiary bg-dark-secondary/50 p-5 shadow-lg shadow-black/10">
      <div className="flex items-center justify-between pb-3 border-b border-dark-tertiary">
        <h3 className="text-sm font-semibold uppercase tracking-widest text-neutral-light">{title}</h3>
        <span className="inline-flex items-center justify-center w-6 h-6 rounded-full bg-dark-tertiary text-xs font-semibold text-accent">
          {tasks.length}
        </span>
      </div>
      <div className="flex flex-col gap-3 flex-1">
        {tasks.length > 0 ? (
          tasks.map((task) => (
            <TaskCard
              key={task.id}
              task={task}
              canDelete={canDelete}
              onDelete={onDelete}
              onStatusChange={onStatusChange}
            />
          ))
        ) : (
          <div className="flex items-center justify-center py-12 text-neutral text-sm">
            No tasks yet
          </div>
        )}
      </div>
    </div>
  );
}
