CREATE TABLE project_file
(
    id                 UUID                     NOT NULL,
    version            INTEGER,
    created_at         TIMESTAMP with time zone NOT NULL,
    updated_at         TIMESTAMP with time zone NOT NULL,
    size_MB            int                      not null,
    project_id         UUID                     not null,
    original_file_name TEXT                     not null,
    stored_file_path   TEXT                     not null,
    PRIMARY KEY (id)
);

alter table project_file add constraint project_file_project_FK foreign key (project_id) references project(id);
