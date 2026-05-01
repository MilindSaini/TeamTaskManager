import { useQuery } from '@tanstack/react-query';
import api from '../api/axios.js';
import { Card } from '../components/ui/card.jsx';
import { Badge } from '../components/ui/badge.jsx';

export default function Dashboard() {
  const { data: summary } = useQuery({
    queryKey: ['dashboardSummary'],
    queryFn: async () => (await api.get('/api/dashboard/summary')).data
  });

  const { data: perUser } = useQuery({
    queryKey: ['dashboardPerUser'],
    queryFn: async () => (await api.get('/api/dashboard/per-user')).data
  });

  const statusEntries = summary?.tasksByStatus ? Object.entries(summary.tasksByStatus) : [];

  return (
    <div className="grid gap-8">
      <div>
        <h1 className="text-4xl font-semibold mb-2">Dashboard</h1>
        <p className="text-neutral">Monitor team progress and task metrics in real-time</p>
      </div>

      <section className="grid gap-6 md:grid-cols-3">
        <Card>
          <div className="text-xs font-semibold uppercase tracking-wider text-neutral-light mb-3">Total Tasks</div>
          <div className="text-5xl font-bold text-accent">{summary?.totalTasks ?? 0}</div>
          <div className="mt-4 text-xs text-neutral">Tasks across all projects</div>
        </Card>
        <Card>
          <div className="text-xs font-semibold uppercase tracking-wider text-neutral-light mb-3">Overdue</div>
          <div className="text-5xl font-bold text-danger">{summary?.overdueTasks ?? 0}</div>
          <div className="mt-4 text-xs text-neutral">Tasks requiring attention</div>
        </Card>
        <Card>
          <div className="text-xs font-semibold uppercase tracking-wider text-neutral-light mb-3">Status Distribution</div>
          <div className="mt-2 flex flex-wrap gap-2">
            {statusEntries.length === 0 ? (
              <span className="text-sm text-neutral">No tasks yet</span>
            ) : (
              statusEntries.slice(0, 3).map(([status, count]) => (
                <Badge key={status} tone="accent">
                  {status.replaceAll('_', ' ')}: {count}
                </Badge>
              ))
            )}
          </div>
        </Card>
      </section>

      <section className="grid gap-6 md:grid-cols-2">
        <Card>
          <h2 className="text-xl font-semibold mb-6">Workload by Teammate</h2>
          <div className="space-y-4">
            {perUser?.items?.length ? (
              perUser.items.map((item) => (
                <div key={item.user.id} className="flex items-center justify-between">
                  <span className="text-sm text-neutral-light">{item.user.name}</span>
                  <div className="flex items-center gap-3">
                    <div className="w-32 h-2 bg-dark-tertiary rounded-full overflow-hidden">
                      <div
                        className="h-full bg-gradient-to-r from-accent to-accent-dark"
                        style={{
                          width: `${Math.min((item.count / Math.max(...perUser.items.map((i) => i.count), 1)) * 100, 100)}%`
                        }}
                      />
                    </div>
                    <span className="text-sm font-semibold text-accent w-8 text-right">{item.count}</span>
                  </div>
                </div>
              ))
            ) : (
              <span className="text-sm text-neutral">Assign tasks to see per-user trends.</span>
            )}
          </div>
        </Card>

        <Card>
          <h2 className="text-xl font-semibold mb-6">Priority Reference</h2>
          <p className="text-sm text-neutral mb-6">
            Use priority labels to communicate urgency and keep task alignment.
          </p>
          <div className="space-y-3">
            <div className="flex items-center gap-3">
              <Badge tone="danger">Urgent</Badge>
              <span className="text-xs text-neutral">Critical path tasks</span>
            </div>
            <div className="flex items-center gap-3">
              <Badge tone="warning">High</Badge>
              <span className="text-xs text-neutral">Important deliverables</span>
            </div>
            <div className="flex items-center gap-3">
              <Badge tone="success">Medium</Badge>
              <span className="text-xs text-neutral">Standard workflow</span>
            </div>
            <div className="flex items-center gap-3">
              <Badge tone="neutral">Low</Badge>
              <span className="text-xs text-neutral">Backlog items</span>
            </div>
          </div>
        </Card>
      </section>
    </div>
  );
}
