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
    - We also make the FlightInfo entry and prompt the user for the pilot's id and plane's id. We add this into the FlightInfo table. We assume the pilot and plane have already been added and have valid id's. 
    - We also make the Schedule entry based on the previous prompts. We add this into the Schedule table.
4. Query 4: Add Technician
    - Here we make an id based off the number of entries, and prompt the user for the technician's full name. 
5. Query 5: Book Flight
    - Here we assume that the customer and flight have already been added, and prompt the user for the customer id and the flight number they want to book.
    - We make query to get both the plane's maximum capacity and the current number of seats sold for that flight. 
    - If the number of seats sold is equal to the maximum capacity of the plane, we automatically add the user to the waitlist. 
    - If the number of seats sold is less than the maximum capacity, as prompt the user if the customer will be paying now or later. If paying now, we add the customer to the confirmed list. If paying later, we add the customer to the reserved list.
6. Query 6: Number of Available Seats For a Given Flight
    - Here we prompt the user for the flight number and the date we wish to find the number of seats for. We connect the Flight to the Plane through FlightInfo and return the maximum capacity of the Plane minus the number of seats sold on the Flight.
7. Query 7: List total number of repairs per plane in descending order
    - Here we use the Repairs table. We group by plane_id and COUNT the entries as num_repairs. 
    - We assume that the number of repairs is descending order.
8. Query 8: List total number of repairs per year in ascending order
    - Here we use the Repairs table. We make a table with just the year, and count the number of entries by year. 
    - We assume that the year is ascending.
9. Query 9: Find total number of passengers with a given status
    - Here we prompt the user for which passenger status we wish to find, (waitlisted, confirmed, or reserved). 
    - We check for casing and query based on the user input.
    - If the user does not put in a valid status, we exit. We assume the user is smart.