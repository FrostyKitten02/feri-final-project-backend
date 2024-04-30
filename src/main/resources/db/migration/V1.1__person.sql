CREATE TABLE person
(
    id         UUID                     NOT NULL,
    version    INTEGER,
    created_at TIMESTAMP with time zone NOT NULL,
    updated_at TIMESTAMP with time zone NOT NULL,
    name       TEXT                     ,
    lastname   TEXT                     ,
    email      TEXT                     ,
    clerk_id   TEXT                     ,
    PRIMARY KEY (id)
)