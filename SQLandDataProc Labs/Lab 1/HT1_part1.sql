/*
Use the script from the file HT1_part1.sql to crea
te the DWH tables and the staging table. 
Populate the Dim_Gym and Dim_Member tables, and ad
d initial data to the Staging table.
*/
 
drop table if exists Fact_Visit;
drop table if exists Dim_Member;
drop table if exists Dim_Gym;
drop table if exists Dim_Date;
drop table if exists Staging_Gym_visit;
drop type if exists day_part_enum;
 
create table Dim_Member(
    member_id serial primary key,
    personal_code text not null unique,
    last_name text not null,
    first_name text not null
);
 
create table Dim_Gym(
    gym_id serial primary key,
    gym_code text not null unique
);
 
create table Dim_Date(
    date_key date primary key,  
    day_number_of_month int not null,
    month_number_of_year int not null,
    year_number int not null,
    day_name text not null,
    month_name text not null    
);
 
create type day_part_enum as enum ('Morning', 'Day
', 'Evening');
 
create table Fact_Visit(
    visit_id serial primary key,
    gym_id int not null references Dim_Gym(gym_id),
    member_id int not null references Dim_Member(memb
er_id),
    visit_date_key date not null references Dim_Date(
date_key),
