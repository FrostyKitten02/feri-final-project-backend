DROP VIEW project_list;
CREATE VIEW project_list AS
(
SELECT p.id,
       p.owner_id,
       p.created_at,
       p.title,
       p.start_date,
       p.end_date,
       (SELECT count(pon.id)
        FROM person_on_project AS pon
        WHERE pon.project_id = p.id)
           AS people_count,
       (SELECT COUNT(wp.id)
        FROM work_package AS wp
        WHERE wp.project_id = p.id)
           AS work_package_count
FROM Project AS p
);
