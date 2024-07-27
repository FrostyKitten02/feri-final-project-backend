create view person_list as
(
select p.id       as id,
       p.name     as name,
       p.lastname as lastname,
       p.email    as email,
       (SELECT amount
        FROM salary
        WHERE person_id = p.id
          AND start_date < CURRENT_DATE
          AND (end_date IS NULL OR end_date > CURRENT_DATE)
        LIMIT 1 --we should only get 1 but just in case so query still works
       )          as salary,
       (SELECT pt.max_availability
        FROM person_type as pt
        WHERE pt.person_id = p.id
          AND pt.start_date < CURRENT_DATE
          AND (pt.end_date IS NULL OR pt.end_date > CURRENT_DATE)
        LIMIT 1 --we should only get 1 but just in case so query still works
       )  as availability
from person as p
    )