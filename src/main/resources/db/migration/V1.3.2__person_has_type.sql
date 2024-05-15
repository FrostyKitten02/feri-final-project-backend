CREATE TABLE person_has_type
(
    id         UUID                     NOT NULL,
    version    INTEGER,
    created_at TIMESTAMP with time zone NOT NULL,
    updated_at TIMESTAMP with time zone NOT NULL,
    person_id  UUID                     NOT NULL,
    type_id    UUID                     NOT NULL,
    start_date DATE                     NOT NULL,
    end_date   DATE,
    PRIMARY KEY (id)
);


alter table person_has_type add constraint fk_person_has_type_person_id foreign key (person_id) references person;
alter table person_has_type add constraint fk_person_has_type_type_id foreign key (type_id) references person_type;