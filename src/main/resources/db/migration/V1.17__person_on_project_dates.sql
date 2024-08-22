alter table person_on_project add column "from" date;
alter table person_on_project add column "to" date;
alter table person_on_project add column estimated_pm numeric(4,3);

update person_on_project
set "from" = p.start_date
from project p
where p.id = project_id;

ALTER TABLE person_on_project ALTER COLUMN "from" SET NOT NULL;

update person_on_project
set estimated_pm = 0
where estimated_pm is null;

ALTER TABLE person_on_project ALTER COLUMN estimated_pm SET NOT NULL;