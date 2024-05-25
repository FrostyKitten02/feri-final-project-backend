CREATE TABLE project_budget_schema
(
    id              UUID                     NOT NULL,
    version         INTEGER,
    created_at      TIMESTAMP with time zone NOT NULL,
    updated_at      TIMESTAMP with time zone NOT NULL,
    name            TEXT                     NOT NULL,
    sofinancing     NUMERIC(3, 2)            NOT NULL,
    indirect_budget NUMERIC(3, 2)            NOT NULL,
    PRIMARY KEY (id)
);

INSERT INTO project_budget_schema (id, version, created_at, updated_at, name, sofinancing, indirect_budget)
VALUES ('b744e2d8-7907-4185-949c-62ab247d5af2', 0, NOW(), NOW(), 'Horizon', 0, 0.2);

INSERT INTO project_budget_schema (id, version, created_at, updated_at, name, sofinancing, indirect_budget)
VALUES ('7edcd0eb-2b8b-4403-a6b0-014d5be09d65', 0, NOW(), NOW(), 'Inherreg', 0.25, 0.15);

INSERT INTO project_budget_schema (id, version, created_at, updated_at, name, sofinancing, indirect_budget)
VALUES ('fb788335-e914-473c-8929-105e60210cd9', 0, NOW(), NOW(), 'ARIS', 0, 0.3);

INSERT INTO project_budget_schema (id, version, created_at, updated_at, name, sofinancing, indirect_budget)
VALUES ('597f0f18-876b-4481-b795-da1d111d0de0', 0, NOW(), NOW(), 'Ministrstvo', 0, 0.3);

INSERT INTO project_budget_schema (id, version, created_at, updated_at, name, sofinancing, indirect_budget)
VALUES ('e4dc5ae1-a0eb-4700-bd8c-b033cf9eb580', 0, NOW(), NOW(), 'Digital', 0.5, 0.07);


ALTER TABLE project
    ADD COLUMN project_budget_schema_id UUID NOT NULL DEFAULT 'b744e2d8-7907-4185-949c-62ab247d5af2';
ALTER TABLE project
    ALTER COLUMN project_budget_schema_id DROP DEFAULT;
ALTER TABLE project
    ADD CONSTRAINT project_project_budget_schema_id_fk FOREIGN KEY (project_budget_schema_id) REFERENCES project_budget_schema (id);