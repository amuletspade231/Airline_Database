# Airline_Database

## How to Start the Database
1. Download all files by forking or cloning the repository.
2. `cd .\postgresql\`
3. `source startPostgreDB.sh`
4. `source createPostgreDB.sh`
5. (Note: if step 4 gives an error, change the port number in startPostreDB.sh and restart at step 3.)
6. `cd ..\java\`
7. `source compile.sh`
8. `source run.sh $USER_DB $PORT $USER`

## How to Stop the Database
1. `cd .\postgresql\`
2. `source stopPostgreDB.sh`

## About the Queries
For all queries, we assume that the id's or primary keys start at 0 and increment by 1.
1. Query 1: Add Plane
    - Here we make an id based off the number of entries, and prompt the user for the plane's make, model, age, and number of seats.
2. Query 2: Add Pilot
    - Here we make an id based off the number of entries, and prompt the user for the pilot's full name and nationality.
3. Query 3: Add Flight
    - Here we make an fnum (primary key) based off the number of entries, and prompt the user for all the flight information (cost, seats sold, stops, departure time, arrival time, arrival location and destination location).
    - We also make the FlightInfo entry and prompt the user for the pilot's id and plane's id. We add this into the FlightInfo table. We assume the pilot and plane have already been added and have id's. 
    - We also make the Schedule entry based on the previous prompts. We add this into the Schedule table.
4. Query 4: Add Technician
    - Here we make an id based off the number of entries, and prompt the user for the technician's full name. 
5. Query 5: Book Flight
    - 
6. Query 6: Number of Available Seats For a Given Flight
    -
7. Query 7: List total number of repairs per plane in descending order
    -
8. Query 8: List total number of repairs per year in ascending order
    -
9. Query 9: Find total number of passengers with a given status
    -