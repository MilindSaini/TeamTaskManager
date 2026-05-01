import { Link } from 'react-router-dom';
import { Card } from './ui/card.jsx';
import { Badge } from './ui/badge.jsx';

export default function ProjectCard({ project }) {
  return (
    <Card className="flex flex-col gap-5 group hover:border-accent transition-colors duration-200">
      <div className="flex items-start justify-between gap-4">
        <div className="min-w-0 flex-1">
          <h3 className="truncate text-lg font-semibold text-ink mb-2">{project.name}</h3>
          <p className="text-sm text-neutral line-clamp-2">{project.description || 'No description yet.'}</p>
        </div>
        <Badge tone={project.currentUserRole === 'ADMIN' ? 'danger' : 'accent'}>
          {project.currentUserRole}
        </Badge>
      </div>
      <div className="pt-4 border-t border-dark-tertiary flex items-center justify-between gap-3">
        <span className="text-xs text-neutral min-w-0 truncate">Owner: <span className="font-semibold text-neutral-light">{project.owner?.name}</span></span>
        <Link
          className="shrink-0 text-xs font-semibold text-accent hover:text-accent-light transition-all duration-200 group-hover:translate-x-1"
          to={`/projects/${project.id}/board`}
        >
          Open board →
        </Link>
      </div>
    </Card>
  );
}
