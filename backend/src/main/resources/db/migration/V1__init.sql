create extension if not exists "pgcrypto";

create table if not exists users (
  id uuid primary key default gen_random_uuid(),
  name varchar(120) not null,
  email varchar(255) not null unique,
  password_hash varchar(255) not null,
  created_at timestamptz not null default now()
);

create table if not exists projects (
  id uuid primary key default gen_random_uuid(),
  name varchar(200) not null,
  description text,
  owner_id uuid not null references users(id) on delete cascade,
  created_at timestamptz not null default now()
);

create table if not exists project_members (
  id uuid primary key default gen_random_uuid(),
  project_id uuid not null references projects(id) on delete cascade,
  user_id uuid not null references users(id) on delete cascade,
  role varchar(20) not null check (role in ('ADMIN', 'MEMBER')),
  joined_at timestamptz not null default now(),
  unique (project_id, user_id)
);

create table if not exists tasks (
  id uuid primary key default gen_random_uuid(),
  project_id uuid not null references projects(id) on delete cascade,
  assignee_id uuid references users(id) on delete set null,
  created_by uuid not null references users(id) on delete restrict,
  title varchar(240) not null,
  description text,
  status varchar(30) not null check (status in ('TODO', 'IN_PROGRESS', 'BLOCKED', 'DONE')),
  priority varchar(20) not null check (priority in ('LOW', 'MEDIUM', 'HIGH', 'URGENT')),
  due_date date,
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now()
);

create table if not exists task_comments (
  id uuid primary key default gen_random_uuid(),
  task_id uuid not null references tasks(id) on delete cascade,
  author_id uuid not null references users(id) on delete cascade,
  content text not null,
  created_at timestamptz not null default now()
);

create index if not exists idx_project_members_user on project_members(user_id);
create index if not exists idx_tasks_project on tasks(project_id);
create index if not exists idx_tasks_assignee on tasks(assignee_id);
create index if not exists idx_task_comments_task on task_comments(task_id);
