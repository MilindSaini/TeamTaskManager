import { useState } from 'react';
import { Button } from './ui/button.jsx';
import { Input } from './ui/input.jsx';
import { Label } from './ui/label.jsx';
import { Textarea } from './ui/textarea.jsx';
import { Card } from './ui/card.jsx';

const statuses = ['TODO', 'IN_PROGRESS', 'BLOCKED', 'DONE'];
const priorities = ['LOW', 'MEDIUM', 'HIGH', 'URGENT'];

export default function TaskForm({ members = [], onSubmit, busy }) {
  const [form, setForm] = useState({
    title: '',
    description: '',
    assigneeId: '',
    status: 'TODO',
    priority: 'MEDIUM',
    dueDate: ''
  });

  const handleChange = (event) => {
    const { name, value } = event.target;
    setForm((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = (event) => {
    event.preventDefault();
    onSubmit({
      title: form.title,
      description: form.description,
      assigneeId: form.assigneeId || null,
      status: form.status,
      priority: form.priority,
      dueDate: form.dueDate || null
    });
    setForm({
      title: '',
      description: '',
      assigneeId: '',
      status: 'TODO',
      priority: 'MEDIUM',
      dueDate: ''
    });
  };

  return (
    <form onSubmit={handleSubmit}>
      <Card className="w-full">
      <h2 className="text-xl font-semibold mb-6">Create New Task</h2>
      <div className="grid gap-5 md:grid-cols-2">
        <div className="md:col-span-2">
          <Label>Task Title</Label>
          <Input name="title" value={form.title} onChange={handleChange} placeholder="Enter task title..." required />
        </div>
        <div className="md:col-span-2">
          <Label>Description</Label>
          <Textarea name="description" rows="3" value={form.description} onChange={handleChange} placeholder="Add task details..." />
        </div>
        <div>
          <Label>Status</Label>
          <select
            name="status"
            value={form.status}
            onChange={handleChange}
            className="w-full rounded-lg border border-dark-tertiary bg-dark-secondary px-3 py-2.5 text-sm text-ink outline-none transition focus:border-accent focus:ring-2 focus:ring-accent/20"
          >
            {statuses.map((status) => (
              <option key={status} value={status}>
                {status.replaceAll('_', ' ')}
              </option>
            ))}
          </select>
        </div>
        <div>
          <Label>Priority</Label>
          <select
            name="priority"
            value={form.priority}
            onChange={handleChange}
            className="w-full rounded-lg border border-dark-tertiary bg-dark-secondary px-3 py-2.5 text-sm text-ink outline-none transition focus:border-accent focus:ring-2 focus:ring-accent/20"
          >
            {priorities.map((priority) => (
              <option key={priority} value={priority}>
                {priority}
              </option>
            ))}
          </select>
        </div>
        <div>
          <Label>Due date</Label>
          <Input type="date" name="dueDate" value={form.dueDate} onChange={handleChange} />
        </div>
        <div>
          <Label>Assignee</Label>
          <select
            name="assigneeId"
            value={form.assigneeId}
            onChange={handleChange}
            className="w-full rounded-lg border border-dark-tertiary bg-dark-secondary px-3 py-2.5 text-sm text-ink outline-none transition focus:border-accent focus:ring-2 focus:ring-accent/20"
          >
            <option value="">Unassigned</option>
            {members.map((member) => (
              <option key={member.user.id} value={member.user.id}>
                {member.user.name} ({member.role})
              </option>
            ))}
          </select>
        </div>
      </div>
      <div className="mt-6 pt-6 border-t border-dark-tertiary">
        <Button disabled={busy} type="submit">
          {busy ? 'Saving...' : 'Create task'}
        </Button>
      </div>
      </Card>
    </form>
  );
}
