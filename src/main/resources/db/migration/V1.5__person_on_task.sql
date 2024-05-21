CREATE TABLE person_on_task
(
    id         UUID                     NOT NULL,
    version    INTEGER,
    created_at TIMESTAMP with time zone NOT NULL,
    updated_at TIMESTAMP with time zone NOT NULL,
    person_id  UUID                     NOT NULL,
    task_id    UUID                     NOT NULL,
    occupancy  NUMERIC(4, 3)            NOT NULL,
    start_date DATE                     NOT NULL,
    end_date   DATE                     NOT NULL,
    PRIMARY KEY (id)
);


alter table person_on_task add constraint fk_person_on_project_person_id_fk foreign key (person_id) references person;
alter table person_on_task add constraint fk_person_on_project_task_id_fk foreign key (task_id) references task;