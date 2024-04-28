create table project
(
    id         UUID                     NOT NULL,
    version    INTEGER,
    created_at TIMESTAMP with time zone NOT NULL,
    updated_at TIMESTAMP with time zone NOT NULL,
    title      TEXT                     NOT NULL,
    owner_id    TEXT                     NOT NULL,
    start_date DATE                     NOT NULL,
    end_date   DATE                     NOT NULL,
    PRIMARY KEY (id)
);

create table work_package
(
    id          UUID                     NOT NULL,
    version     INTEGER,
    created_at  TIMESTAMP with time zone NOT NULL,
    updated_at  TIMESTAMP with time zone NOT NULL,
    title       TEXT                     NOT NULL,
    start_date  DATE                     NOT NULL,
    end_date    DATE                     NOT NULL,
    is_relevant BOOLEAN                  NOT NULL,
    project_id  UUID                     NOT NULL,
    PRIMARY KEY (id)
);
ALTER TABLE work_package ADD CONSTRAINT fk_work_package_on_project FOREIGN KEY (project_id) REFERENCES project (id);

create table task
(
    id              UUID                     NOT NULL,
    version         INTEGER,
    created_at      TIMESTAMP with time zone NOT NULL,
    updated_at      TIMESTAMP with time zone NOT NULL,
    title           TEXT                     NOT NULL,
    start_date      DATE                     NOT NULL,
    end_date        DATE                     NOT NULL,
    is_relevant     BOOLEAN                  NOT NULL,
    work_package_id UUID                     NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE task ADD CONSTRAINT fk_task_on_work_package FOREIGN KEY (work_package_id) REFERENCES work_package (id);