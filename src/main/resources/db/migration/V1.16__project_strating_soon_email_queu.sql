create table project_starting_soon_email_queue
(
    id         UUID                     NOT NULL,
    version    INTEGER,
    created_at TIMESTAMP with time zone NOT NULL,
    updated_at TIMESTAMP with time zone NOT NULL,
    project_id UUID                     NOT NULL,
    attempts   INTEGER                  NOT NULL DEFAULT 0,
    send_at    DATE,
    sent    BOOLEAN                  NOT NULL DEFAULT FALSE,
    PRIMARY KEY (id)
);

ALTER TABLE project_starting_soon_email_queue ADD CONSTRAINT fk_project_starting_soon_email_queue_project_id FOREIGN KEY (project_id) REFERENCES project (id);
