create view project_list as
(
    select id, owner_id, created_at, title, start_date, end_date
    from project
);