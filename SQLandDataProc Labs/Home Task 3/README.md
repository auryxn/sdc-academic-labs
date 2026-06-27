# SQL and Data Processing - Home Task 3: Document and Graph Databases

## Task Goal
Create two scripts based on the provided relational database scripts:

1. A document-oriented database for hotel room bookings based on `Booking.sql`.
2. A graph database for users, photos, likes, comments and followers based on `PhotoNet.sql`.

## Expected Result
Two script files:

- `SOLUTION_DOCUMENT_DATABASE.js` - MongoDB / mongosh script for the document-oriented database.
- `SOLUTION_GRAPH_DATABASE.cypher` - Neo4j Cypher script for the graph database.

## Document-oriented Database Requirements
The database must support these common queries:

- Display information about rooms in a hotel.
- Display all information about room reservations: dates, guests, room size and facilities.

The script must:

1. Insert all data from `Booking.sql`.
2. Retrieve information about reservations made by Lucas.
3. Remove Enrika's cancelled reservation.
4. Add a note to Lucas's reservation that he will arrive after 10 pm.
5. Replace all `TV` facilities with `Flat-screen TVs` in the Redisson hotel.
6. Calculate how many bookings were made in each hotel.

## Graph Database Requirements
The database must contain all data from `PhotoNet.sql`.

The script must:

1. Change Holly's comment under `file_name3` to `Very Good`.
2. Remove all users followed by John.
3. Display all photos of users whom Mary follows.
4. Return the count of likes placed on Claire's photos.
5. Return recommended users for Holly to follow.
