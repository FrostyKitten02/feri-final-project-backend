CREATE VIEW person_type_list AS
(
SELECT id               AS id,
       name             AS name,
       research         AS research,
       educate          AS educate,
       max_availability AS max_availability,
       start_date       AS start_date,
       end_date         AS end_date,
       person_id        AS person_id
FROM person_type
)