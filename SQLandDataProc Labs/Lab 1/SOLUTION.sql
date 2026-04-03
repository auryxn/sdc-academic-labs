-- ETL Process Solution for Lab 1

-- 1. Populate Dim_Date for all unique dates in Staging
insert into Dim_Date (date_key, day_number_of_month, month_number_of_year, year_number, day_name, month_name)
select 
    distinct visit_date,
    extract(day from visit_date),
    extract(month from visit_date),
    extract(year from visit_date),
    to_char(visit_date, 'Day'),
    to_char(visit_date, 'Month')
from Staging_Gym_visit
where visit_date is not null
  and not exists (select 1 from Dim_Date where date_key = Staging_Gym_visit.visit_date);

-- 2. Create a temporary table to store standardized and validated visits
drop table if exists temp_processed_visits;
create temp table temp_processed_visits as
with 
-- Step A: Validate name/code match and normalize names based on Gym
validated_staging as (
    select 
        s.*,
        m.member_id,
        case 
            when s.gym_code = 'Gym_1' and s.visitor_name = (m.last_name || ' ' || m.first_name) then true
            when s.gym_code = 'Gym_2' and s.visitor_name = (m.first_name || ' ' || m.last_name) then true
            else false
        end as is_valid_identity
    from Staging_Gym_visit s
    join Dim_Member m on s.personal_code = m.personal_code
),
-- Step B: Consolidation (Handle Gym_2 two-row logic)
consolidated_visits as (
    -- Gym_1 (Single row)
    select 
        member_id, gym_code, visit_date, time_in, time_out, v_id as original_v_id
    from validated_staging
    where gym_code = 'Gym_1' and is_valid_identity = true and time_in is not null and time_out is not null

    union all

    -- Gym_2 (Pairing In and Out rows)
    select 
        s_in.member_id, 
        s_in.gym_code, 
        s_in.visit_date, 
        s_in.time_in, 
        s_out.time_out,
        s_in.v_id -- just taking one id for tracking
    from validated_staging s_in
    join validated_staging s_out on 
        s_in.member_id = s_out.member_id and 
        s_in.visit_date = s_out.visit_date and 
        s_in.gym_code = s_out.gym_code
    where s_in.gym_code = 'Gym_2' 
      and s_in.is_valid_identity = true
      and s_in.time_in is not null and s_in.time_out is null
      and s_out.time_in is null and s_out.time_out is not null
)
select 
    member_id,
    (select gym_id from Dim_Gym where gym_code = consolidated_visits.gym_code) as gym_id,
    visit_date as visit_date_key,
    extract(epoch from (time_out - time_in))/60 as visit_duration,
    case 
        when time_in <= '10:00:00' then 'Morning'::day_part_enum
        when time_in > '10:00:00' and time_in <= '17:00:00' then 'Day'::day_part_enum
        else 'Evening'::day_part_enum
    end as day_part
from consolidated_visits
where time_out > time_in; -- Only completed and logical visits

-- 3. Load into Fact_Visit (avoiding duplicates)
insert into Fact_Visit (gym_id, member_id, visit_date_key, visit_duration, day_part)
select 
    gym_id, member_id, visit_date_key, visit_duration, day_part
from temp_processed_visits t
where not exists (
    select 1 from Fact_Visit f
    where f.gym_id = t.gym_id 
      and f.member_id = t.member_id 
      and f.visit_date_key = t.visit_date_key
      and f.visit_duration = t.visit_duration
);

-- 4. Mark errors in Staging (rows not processed)
update Staging_Gym_visit
set require_manual_processing = 1
where v_id not in (
    select v_id from validated_staging vs 
    join temp_processed_visits tp on vs.member_id = tp.member_id and vs.visit_date = tp.visit_date_key
);

-- 5. Cleanup: Remove processed rows from Staging (those that didn't get marked for manual processing)
delete from Staging_Gym_visit
where require_manual_processing = 0;

-- Optional: Drop temp table
drop table temp_processed_visits;
