CREATE TABLE occupancy
(
    id         UUID                     NOT NULL,
    version    INTEGER,
    created_at TIMESTAMP with time zone NOT NULL,
    updated_at TIMESTAMP with time zone NOT NULL,
    project_id UUID                     NOT NULL,
    person_id  UUID                     NOT NULL,
    month      DATE                     NOT NULL,
    value      NUMERIC(4, 3)            NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE occupancy
    ADD CONSTRAINT fk_occupancy_project_id FOREIGN KEY (project_id) REFERENCES project (id);
ALTER TABLE occupancy
    ADD CONSTRAINT fk_occupancy_person_id FOREIGN KEY (person_id) REFERENCES person (id);

