import { useState } from 'react';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import api from '../api/axios.js';
import ProjectCard from '../components/ProjectCard.jsx';
import { Button } from '../components/ui/button.jsx';
import { Card } from '../components/ui/card.jsx';
import { Input } from '../components/ui/input.jsx';
import { Label } from '../components/ui/label.jsx';
import { Textarea } from '../components/ui/textarea.jsx';

export default function Projects() {
  const queryClient = useQueryClient();
  const [form, setForm] = useState({ name: '', description: '' });

  const { data: projects } = useQuery({
    queryKey: ['projects'],
    queryFn: async () => (await api.get('/api/projects')).data
  });

  const mutation = useMutation({
    mutationFn: async (payload) => (await api.post('/api/projects', payload)).data,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['projects'] });
      setForm({ name: '', description: '' });
    }
  });

  const handleChange = (event) => {
    const { name, value } = event.target;
    setForm((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = (event) => {
    event.preventDefault();
    mutation.mutate(form);
  };

  return (
    <div className="grid gap-8">
      <div>
        <h1 className="text-4xl font-semibold mb-2">Projects</h1>
        <p className="text-neutral">Organize and manage team initiatives</p>
      </div>

      <Card>
        <h2 className="text-xl font-semibold mb-6">Create New Project</h2>
        <form className="grid gap-5" onSubmit={handleSubmit}>
          <div>
            <Label>Project Name</Label>
            <Input
              name="name"
              value={form.name}
              onChange={handleChange}
              placeholder="e.g., Q2 Product Launch"
              required
            />
          </div>
          <div>
            <Label>Description</Label>
            <Textarea
              name="description"
              value={form.description}
              onChange={handleChange}
              placeholder="Add details about this project..."
              rows="4"
            />
          </div>
          <div className="flex gap-3">
            <Button disabled={mutation.isPending} type="submit">
              {mutation.isPending ? 'Creating...' : 'Create project'}
            </Button>
            {mutation.isSuccess && (
              <div className="text-sm text-success">Project created successfully!</div>
            )}
          </div>
        </form>
      </Card>

      <section>
        <h2 className="text-xl font-semibold mb-4">Your Projects</h2>
        <div className="grid gap-4 md:grid-cols-2">
          {projects?.length ? (
            projects.map((project) => <ProjectCard key={project.id} project={project} />)
          ) : (
            <Card className="md:col-span-2">
              <div className="text-center py-8">
                <p className="text-sm text-neutral mb-3">No projects yet</p>
                <p className="text-xs text-neutral">Create your first project above to get started</p>
              </div>
            </Card>
          )}
        </div>
      </section>
    </div>
  );
}
