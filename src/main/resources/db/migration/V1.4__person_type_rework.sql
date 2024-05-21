drop table person_has_type;

alter table person_type add column start_date DATE NOT NULL;
alter table person_type add column end_date DATE;


alter table person_type add column person_id UUID NOT NULL;

alter table person_type add constraint person_type_person_id_fk foreign key (person_id) references person(id);