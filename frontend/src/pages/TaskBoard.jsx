import { useMemo, useState } from 'react';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { useParams } from 'react-router-dom';
import api from '../api/axios.js';
import KanbanColumn from '../components/KanbanColumn.jsx';
import TaskForm from '../components/TaskForm.jsx';
import { Badge } from '../components/ui/badge.jsx';
import { Button } from '../components/ui/button.jsx';
import { Card } from '../components/ui/card.jsx';
import { Input } from '../components/ui/input.jsx';
import { Label } from '../components/ui/label.jsx';

const columns = [
  { key: 'TODO', label: 'To do' },
  { key: 'IN_PROGRESS', label: 'In progress' },
  { key: 'BLOCKED', label: 'Blocked' },
  { key: 'DONE', label: 'Done' }
];

const priorities = ['', 'LOW', 'MEDIUM', 'HIGH', 'URGENT'];
const statuses = ['', ...columns.map((column) => column.key)];

export default function TaskBoard() {
  const { projectId } = useParams();
  const queryClient = useQueryClient();
  const [filters, setFilters] = useState({ search: '', status: '', priority: '' });
  const [memberForm, setMemberForm] = useState({ email: '', role: 'MEMBER' });

  const { data: project } = useQuery({
    queryKey: ['project', projectId],
    queryFn: async () => (await api.get(`/api/projects/${projectId}`)).data
  });

  const { data: members = [] } = useQuery({
    queryKey: ['members', projectId],
    queryFn: async () => (await api.get(`/api/projects/${projectId}/members`)).data
  });

  const { data: tasks = [] } = useQuery({
    queryKey: ['tasks', projectId, filters],
    queryFn: async () => {
      const params = new URLSearchParams();
      Object.entries(filters).forEach(([key, value]) => {
        if (value) {
          params.set(key, value);
        }
      });
      const query = params.toString();
      return (await api.get(`/api/projects/${projectId}/tasks${query ? `?${query}` : ''}`)).data;
    }
  });

  const isAdmin = project?.currentUserRole === 'ADMIN';

  const createTask = useMutation({
    mutationFn: async (payload) => (await api.post(`/api/projects/${projectId}/tasks`, payload)).data,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['tasks', projectId] });
    }
  });

  const updateStatus = useMutation({
    mutationFn: async ({ taskId, status }) =>
      (await api.patch(`/api/tasks/${taskId}/status`, { status })).data,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['tasks', projectId] });
      queryClient.invalidateQueries({ queryKey: ['dashboardSummary'] });
    }
  });

  const deleteTask = useMutation({
    mutationFn: async (taskId) => api.delete(`/api/tasks/${taskId}`),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['tasks', projectId] });
      queryClient.invalidateQueries({ queryKey: ['dashboardSummary'] });
    }
  });

  const addMember = useMutation({
    mutationFn: async (payload) => (await api.post(`/api/projects/${projectId}/members`, payload)).data,
    onSuccess: () => {
      setMemberForm({ email: '', role: 'MEMBER' });
      queryClient.invalidateQueries({ queryKey: ['members', projectId] });
    }
  });

  const updateMemberRole = useMutation({
    mutationFn: async ({ userId, role }) =>
      (await api.put(`/api/projects/${projectId}/members/${userId}`, { role })).data,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['members', projectId] });
    }
  });

  const removeMember = useMutation({
    mutationFn: async (userId) => api.delete(`/api/projects/${projectId}/members/${userId}`),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['members', projectId] });
      queryClient.invalidateQueries({ queryKey: ['tasks', projectId] });
    }
  });

  const grouped = useMemo(() => {
    const groups = new Map();
    columns.forEach((col) => groups.set(col.key, []));
    tasks.forEach((task) => {
      if (!groups.has(task.status)) {
        groups.set(task.status, []);
      }
      groups.get(task.status).push(task);
    });
    return groups;
  }, [tasks]);

  return (
    <div className="grid gap-6">
      <div className="flex flex-col justify-between gap-4 md:flex-row md:items-start">
        <div>
          <h1 className="text-4xl font-semibold mb-2">{project?.name || 'Task board'}</h1>
          <p className="text-neutral">
            {isAdmin ? 'Organize, assign, and track team work.' : 'View and manage your assigned tasks.'}
          </p>
        </div>
        <Badge tone={isAdmin ? 'danger' : 'accent'}>{project?.currentUserRole || 'MEMBER'}</Badge>
      </div>

      <Card>
        <div className="grid gap-5 md:grid-cols-3">
          <div>
            <Label>Search Tasks</Label>
            <Input
              value={filters.search}
              onChange={(event) => setFilters((prev) => ({ ...prev, search: event.target.value }))}
              placeholder="Search by title or description..."
            />
          </div>
          <div>
            <Label>Filter by Status</Label>
            <select
              value={filters.status}
              onChange={(event) => setFilters((prev) => ({ ...prev, status: event.target.value }))}
              className="w-full rounded-lg border border-dark-tertiary bg-dark-secondary px-3 py-2.5 text-sm text-ink outline-none transition duration-200 focus:border-accent focus:ring-2 focus:ring-accent/20"
            >
              {statuses.map((status) => (
                <option key={status || 'ALL'} value={status}>
                  {status ? status.replaceAll('_', ' ') : 'All statuses'}
                </option>
              ))}
            </select>
          </div>
          <div>
            <Label>Filter by Priority</Label>
            <select
              value={filters.priority}
              onChange={(event) => setFilters((prev) => ({ ...prev, priority: event.target.value }))}
              className="w-full rounded-lg border border-dark-tertiary bg-dark-secondary px-3 py-2.5 text-sm text-ink outline-none transition duration-200 focus:border-accent focus:ring-2 focus:ring-accent/20"
            >
              {priorities.map((priority) => (
                <option key={priority || 'ALL'} value={priority}>
                  {priority || 'All priorities'}
                </option>
              ))}
            </select>
          </div>
        </div>
      </Card>

      {isAdmin ? (
        <div className="grid gap-6 xl:grid-cols-[minmax(0,2fr)_minmax(320px,1fr)]">
          <TaskForm
            members={members}
            onSubmit={(payload) => createTask.mutate(payload)}
            busy={createTask.isPending}
          />
          <Card>
            <h2 className="text-xl font-semibold mb-6">Team Members</h2>
            <form
              className="grid gap-4 mb-6 pb-6 border-b border-dark-tertiary"
              onSubmit={(event) => {
                event.preventDefault();
                addMember.mutate(memberForm);
              }}
            >
              <div>
                <Label>Invite by Email</Label>
                <Input
                  type="email"
                  value={memberForm.email}
                  onChange={(event) => setMemberForm((prev) => ({ ...prev, email: event.target.value }))}
                  placeholder="colleague@example.com"
                  required
                />
              </div>
              <div className="grid grid-cols-[1fr_auto] gap-3">
                <select
                  value={memberForm.role}
                  onChange={(event) => setMemberForm((prev) => ({ ...prev, role: event.target.value }))}
                  className="rounded-lg border border-dark-tertiary bg-dark-secondary px-3 py-2.5 text-sm text-ink outline-none transition focus:border-accent focus:ring-2 focus:ring-accent/20"
                >
                  <option value="MEMBER">Member</option>
                  <option value="ADMIN">Admin</option>
                </select>
                <Button disabled={addMember.isPending} type="submit">
                  {addMember.isPending ? '...' : 'Add'}
                </Button>
              </div>
            </form>
            <div className="space-y-3">
              {members.length > 0 ? (
                members.map((member) => (
                  <div key={member.id} className="grid gap-3 rounded-lg border border-dark-tertiary bg-dark-secondary/50 p-4 hover:border-accent/50 transition-colors">
                    <div className="flex items-start justify-between gap-3">
                      <div className="min-w-0 flex-1">
                        <div className="truncate text-sm font-semibold text-ink">{member.user.name}</div>
                        <div className="truncate text-xs text-neutral">{member.user.email}</div>
                      </div>
                      <Button
                        type="button"
                        variant="ghost"
                        className="shrink-0 px-3 text-danger hover:text-danger hover:bg-danger/10"
                        onClick={() => removeMember.mutate(member.user.id)}
                      >
                        Remove
                      </Button>
                    </div>
                    <select
                      value={member.role}
                      onChange={(event) =>
                        updateMemberRole.mutate({ userId: member.user.id, role: event.target.value })
                      }
                      className="rounded-lg border border-dark-tertiary bg-dark-secondary px-3 py-2 text-xs text-ink outline-none focus:border-accent focus:ring-2 focus:ring-accent/20"
                    >
                      <option value="MEMBER">Member</option>
                      <option value="ADMIN">Admin</option>
                    </select>
                  </div>
                ))
              ) : (
                <p className="text-xs text-neutral py-4">No members yet. Add your first team member above.</p>
              )}
            </div>
          </Card>
        </div>
      ) : null}

      <div className="grid gap-6 lg:grid-cols-4">
        {columns.map((column) => (
          <KanbanColumn
            key={column.key}
            title={column.label}
            tasks={grouped.get(column.key) || []}
            canDelete={isAdmin}
            onDelete={(taskId) => deleteTask.mutate(taskId)}
            onStatusChange={(taskId, status) => updateStatus.mutate({ taskId, status })}
          />
        ))}
      </div>
    </div>
  );
}
