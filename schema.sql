-- Database setup

-- Posts
create table public.posts (
    id uuid not null primary key default gen_random_uuid (),
    user_id uuid not null references auth.users (id),
    content text not null,
    likes_count integer not null default 0,
    comments_count integer not null default 0,
    created_at timestamp with time zone not null default now(),
    updated_at timestamp with time zone not null default now()
  ) tablespace pg_default;

-- Attachments
create table public.attachments (
    id uuid not null primary key default gen_random_uuid (),
    post_id uuid not null references public.posts (id),
    file_path text not null,
    file_size integer not null,
    public_url text not null,
    created_at timestamp with time zone not null default now(),
    updated_at timestamp with time zone not null default now()
  ) tablespace pg_default;

-- Comments
create table public.comments (
    id uuid not null primary key default gen_random_uuid (),
    user_id uuid not null references auth.users (id),
    post_id uuid not null references public.posts (id),
    content text not null,
    likes_count integer not null default 0,
    created_at timestamp with time zone not null default now(),
    updated_at timestamp with time zone not null default now()
  ) tablespace pg_default;

-- Messages
create table public.messages (
    id uuid not null primary key default gen_random_uuid (),
    sender_id uuid not null references auth.users (id),
    receiver_id uuid not null references auth.users (id),
    content text not null,
    created_at timestamp with time zone not null default now()
  ) tablespace pg_default;


-- Set up Storage!
insert into storage.buckets (id, name)
  values ('Product Image', 'Product Image');

-- Set up access controls for storage.
-- See https://supabase.com/docs/guides/storage/security/access-control#policy-examples for more details.
CREATE POLICY "Enable read access for all users" ON "storage"."objects"
AS PERMISSIVE FOR SELECT
TO public
USING (true)

CREATE POLICY "Enable insert for all users" ON "storage"."objects"
AS PERMISSIVE FOR INSERT
TO authenticated, anon
WITH CHECK (true)

CREATE POLICY "Enable update for all users" ON "storage"."objects"
AS PERMISSIVE FOR UPDATE
TO public
USING (true)
WITH CHECK (true)