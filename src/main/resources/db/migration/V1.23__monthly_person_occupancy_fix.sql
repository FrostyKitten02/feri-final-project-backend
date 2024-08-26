drop view monthly_person_occupancy;
create view monthly_person_occupancy as
(
select gen_random_uuid()                                 as id,
       res.month,
       res.person_id,
       res.estimated_pm,
       (select pt.max_availability
        from person_type as pt
        where pt.person_id = res.person_id
          and start_date >= "month"
          and (end_date is null or end_date <= "month")) as max_availability
from (SELECT cast(date_trunc('month', month_series) AS Date) AS "month",
             person_on_project_per_month.person_id,
             SUM(person_on_project_per_month.estimated_pm)   AS estimated_pm
      FROM (SELECT person_occupancy_on_project.from_date,
                   person_occupancy_on_project.to_date,
                   person_occupancy_on_project.estimated_pm,
                   person_occupancy_on_project.person_id,
                   generate_series(
                           date_trunc('month', from_date),
                           date_trunc('month', to_date),
                           '1 month'::interval
                   ) AS month_series
            FROM (
                     SELECT p.id as person_id,
                            pop.from_date as from_date,
                            pop.estimated_pm as estimated_pm,
                            coalesce(pop.to_date, pro.end_date) as to_date
                     FROM person p
                              LEFT JOIN person_on_project pop on pop.person_id = p.id
                              LEFT JOIN project pro on pop.project_id = pro.id

                 ) as person_occupancy_on_project) as person_on_project_per_month
      GROUP BY month, person_on_project_per_month.person_id) as res
order by month
    );

