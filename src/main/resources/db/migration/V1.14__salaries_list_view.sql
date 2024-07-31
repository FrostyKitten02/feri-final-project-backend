CREATE VIEW salary_list AS
(
SELECT id         AS id,
       person_id  AS person_id,
       amount     AS amount,
       start_date AS start_date,
       end_date   AS end_date
FROM salary
)