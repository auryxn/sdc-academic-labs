# SQL and Data Processing - Lab 1: ETL Process

## Task Goal
Create an ETL process to transfer data from a Staging table to the DWH tables.

## Requirements
1. **Initial Setup**: Use `HT1_part1.sql` to create DWH tables (`Fact_Visit`, `Dim_Member`, `Dim_Gym`, `Dim_Date`) and the `Staging_Gym_visit` table.
2. **ETL Implementation**: Write a script/procedure to transfer data from Staging to DWH:
    - Populate missing `Dim_date` values.
    - Handle duplicates.
    - Calculate duration in minutes (`Time_out` - `Time_in`).
    - Calculate `Day_part`:
        - Morning: Up to 10:00
        - Day: 10:01 to 17:00
        - Evening: After 17:01
    - **Gym_1 Logic**: Data is in a single row.
    - **Gym_2 Logic**: Data is in two rows (one for `Time_in`, one for `Time_out`).
    - Only transfer **completed visits** (`Time_out` is not null).
    - Validate `personal_code` matches `visitor_name`.
    - **Name Formats**: 
        - Gym_1: `<last name> <first name>`
        - Gym_2: `<first name> <last name>`
    - Mark or store error rows.
    - Remove processed rows from the Staging table.
3. **Incremental Loading**: Use `HT1_part_2.sql` to test the ETL with new data.

## Expected Result
One SQL script containing the ETL process.
