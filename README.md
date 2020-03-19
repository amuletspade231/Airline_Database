# Airline_Database

## How to Start the Database
1. Download all files by forking or cloning the repository.
2. cd .\postgresql\
3. source startPostgreDB.sh
4. source createPostgreDB.sh
5. (Note: if step 4 gives an error, change the port number in startPostreDB.sh and restart at step 3.)
6. cd ..\java\
7. source compile.sh
8. source run.sh $USER_DB $PORT $USER

## How to Stop the Database
1. cd .\postgresql\
2. source stopPostgreDB.sh