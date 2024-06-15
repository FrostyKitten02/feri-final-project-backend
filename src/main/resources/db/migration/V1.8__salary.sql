CREATE TABLE salary
(
    id         UUID                     NOT NULL,
    version    INTEGER,
    created_at TIMESTAMP with time zone NOT NULL,
    updated_at TIMESTAMP with time zone NOT NULL,
    person_id  UUID                     NOT NULL,
    amount     DECIMAL(10, 2)           NOT NULL,
    start_date DATE                     NOT NULL,
    end_date   DATE,
    PRIMARY KEY (id)
);

alter table salary add constraint fk_salary_person foreign key (person_id) references person;