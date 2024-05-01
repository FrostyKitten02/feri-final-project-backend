CREATE TABLE person_on_project
(
    id         UUID                     NOT NULL,
    version    INTEGER,
    created_at TIMESTAMP with time zone NOT NULL,
    updated_at TIMESTAMP with time zone NOT NULL,
    person_id  UUID                     NOT NULL,
    project_id UUID                     NOT NULL,
    UNIQUE(person_id, project_id),
    PRIMARY KEY (id)
);

ALTER TABLE person_on_project ADD CONSTRAINT fk_person_on_project_person_id FOREIGN KEY (person_id) REFERENCES person (id);
ALTER TABLE person_on_project ADD CONSTRAINT fk_person_on_project_project_id FOREIGN KEY (project_id) REFERENCES project (id);
