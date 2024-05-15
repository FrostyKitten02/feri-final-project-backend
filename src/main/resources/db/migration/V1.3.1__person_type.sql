CREATE TABLE person_type
(
    id               UUID                     NOT NULL,
    version          INTEGER,
    created_at       TIMESTAMP with time zone NOT NULL,
    updated_at       TIMESTAMP with time zone NOT NULL,
    name             TEXT,
    research         NUMERIC(3, 2),
    educate          NUMERIC(3, 2),
    max_availability NUMERIC(3, 2),
    PRIMARY KEY (id)
)