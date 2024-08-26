-- this view has some issues but it should work just fine if person has person type defined for months you are querying
drop view monthly_person_occupancy;
create view monthly_person_occupancy as
(
select gen_random_uuid()                          as id,
       sum(estimated_pm)                          as max_availability,
       cast(date_trunc('month', "month") as Date) as "month",
       sum(res.value)                             as estimated_pm,
       res.person_id
from (select monthly_res.*, oc.value
      from (SELECT person_occupancy_on_project.estimated_pm,
                   person_occupancy_on_project.person_id,
                   cast(generate_series(
                           date_trunc('month', from_date),
                           date_trunc('month', to_date),
                           '1 month'::interval
                        ) as DATE) AS "month"
            FROM (SELECT p.id                                as person_id,
                         pop.from_date                       as from_date,
                         pop.estimated_pm                    as estimated_pm,
                         coalesce(pop.to_date, pro.end_date) as to_date
                  FROM person p
                           LEFT JOIN person_on_project pop on pop.person_id = p.id
                           LEFT JOIN project pro on pop.project_id = pro.id) as person_occupancy_on_project) as monthly_res
               LEFT JOIN occupancy oc
                         on monthly_res.person_id = oc.person_id and monthly_res."month" = oc."month") as res
group by res.person_id, res."month"
order by "month"
    );