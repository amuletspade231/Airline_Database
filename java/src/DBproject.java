/*
 * Template JAVA User Interface
 * =============================
 *
 * Database Management Systems
 * Department of Computer Science &amp; Engineering
 * University of California - Riverside
 *
 * Target DBMS: 'Postgres'
 *
 */


import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.ArrayList;

/**
 * This class defines a simple embedded SQL utility class that is designed to
 * work with PostgreSQL JDBC drivers.
 *
 */

public class DBproject{
	//reference to physical database connection
	private Connection _connection = null;
	static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

	public DBproject(String dbname, String dbport, String user, String passwd) throws SQLException {
		System.out.print("Connecting to database...");
		try{
			// constructs the connection URL
			String url = "jdbc:postgresql://localhost:" + dbport + "/" + dbname;
			System.out.println ("Connection URL: " + url + "\n");

			// obtain a physical connection
	        this._connection = DriverManager.getConnection(url, user, passwd);
	        System.out.println("Done");
		}catch(Exception e){
			System.err.println("Error - Unable to Connect to Database: " + e.getMessage());
	        System.out.println("Make sure you started postgres on this machine");
	        System.exit(-1);
		}
	}

	/**
	 * Method to execute an update SQL statement.  Update SQL instructions
	 * includes CREATE, INSERT, UPDATE, DELETE, and DROP.
	 *
	 * @param sql the input SQL string
	 * @throws java.sql.SQLException when update failed
	 * */
	public void executeUpdate (String sql) throws SQLException {
		// creates a statement object
		Statement stmt = this._connection.createStatement ();

		// issues the update instruction
		stmt.executeUpdate (sql);

		// close the instruction
	    stmt.close ();
	}//end executeUpdate

	/**
	 * Method to execute an input query SQL instruction (i.e. SELECT).  This
	 * method issues the query to the DBMS and outputs the results to
	 * standard out.
	 *
	 * @param query the input query string
	 * @return the number of rows returned
	 * @throws java.sql.SQLException when failed to execute the query
	 */
	public int executeQueryAndPrintResult (String query) throws SQLException {
		//creates a statement object
		Statement stmt = this._connection.createStatement ();

		//issues the query instruction
		ResultSet rs = stmt.executeQuery (query);

		/*
		 *  obtains the metadata object for the returned result set.  The metadata
		 *  contains row and column info.
		 */
		ResultSetMetaData rsmd = rs.getMetaData ();
		int numCol = rsmd.getColumnCount ();
		int rowCount = 0;

		//iterates through the result set and output them to standard out.
		boolean outputHeader = true;
		while (rs.next()){
			if(outputHeader){
				for(int i = 1; i <= numCol; i++){
					System.out.print(rsmd.getColumnName(i) + "\t");
			    }
			    System.out.println();
			    outputHeader = false;
			}
			for (int i=1; i<=numCol; ++i)
				System.out.print (rs.getString (i) + "\t");
			System.out.println ();
			++rowCount;
		}//end while
		stmt.close ();
		return rowCount;
	}

	/**
	 * Method to execute an input query SQL instruction (i.e. SELECT).  This
	 * method issues the query to the DBMS and returns the results as
	 * a list of records. Each record in turn is a list of attribute values
	 *
	 * @param query the input query string
	 * @return the query result as a list of records
	 * @throws java.sql.SQLException when failed to execute the query
	 */
	public List<List<String>> executeQueryAndReturnResult (String query) throws SQLException {
		//creates a statement object
		Statement stmt = this._connection.createStatement ();

		//issues the query instruction
		ResultSet rs = stmt.executeQuery (query);

		/*
		 * obtains the metadata object for the returned result set.  The metadata
		 * contains row and column info.
		*/
		ResultSetMetaData rsmd = rs.getMetaData ();
		int numCol = rsmd.getColumnCount ();
		int rowCount = 0;

		//iterates through the result set and saves the data returned by the query.
		boolean outputHeader = false;
		List<List<String>> result  = new ArrayList<List<String>>();
		while (rs.next()){
			List<String> record = new ArrayList<String>();
			for (int i=1; i<=numCol; ++i)
				record.add(rs.getString (i));
			result.add(record);
		}//end while
		stmt.close ();
		return result;
	}//end executeQueryAndReturnResult

	/**
	 * Method to execute an input query SQL instruction (i.e. SELECT).  This
	 * method issues the query to the DBMS and returns the number of results
	 *
	 * @param query the input query string
	 * @return the number of rows returned
	 * @throws java.sql.SQLException when failed to execute the query
	 */
	public int executeQuery (String query) throws SQLException {
		//creates a statement object
		Statement stmt = this._connection.createStatement ();

		//issues the query instruction
		ResultSet rs = stmt.executeQuery (query);

		int rowCount = 0;

		//iterates through the result set and count nuber of results.
		if(rs.next()){
			rowCount++;
		}//end while
		stmt.close ();
		return rowCount;
	}

	/**
	 * Method to fetch the last value from sequence. This
	 * method issues the query to the DBMS and returns the current
	 * value of sequence used for autogenerated keys
	 *
	 * @param sequence name of the DB sequence
	 * @return current value of a sequence
	 * @throws java.sql.SQLException when failed to execute the query
	 */

	public int getCurrSeqVal(String sequence) throws SQLException {
		Statement stmt = this._connection.createStatement ();

		ResultSet rs = stmt.executeQuery (String.format("Select currval('%s')", sequence));
		if (rs.next()) return rs.getInt(1);
		return -1;
	}

	/**
	 * Method to close the physical connection if it is open.
	 */
	public void cleanup(){
		try{
			if (this._connection != null){
				this._connection.close ();
			}//end if
		}catch (SQLException e){
	         // ignored.
		}//end try
	}//end cleanup

	/**
	 * The main execution method
	 *
	 * @param args the command line arguments this inclues the <mysql|pgsql> <login file>
	 */
	public static void main (String[] args) {
		if (args.length != 3) {
			System.err.println (
				"Usage: " + "java [-classpath <classpath>] " + DBproject.class.getName () +
		            " <dbname> <port> <user>");
			return;
		}//end if

		DBproject esql = null;

		try{
			System.out.println("(1)");

			try {
				Class.forName("org.postgresql.Driver");
			}catch(Exception e){

				System.out.println("Where is your PostgreSQL JDBC Driver? " + "Include in your library path!");
				e.printStackTrace();
				return;
			}

			System.out.println("(2)");
			String dbname = args[0];
			String dbport = args[1];
			String user = args[2];

			esql = new DBproject (dbname, dbport, user, "");

			//creating indexes
			createIndexes(esql);

			boolean keepon = true;
			while(keepon){
				System.out.println("MAIN MENU");
				System.out.println("---------");
				System.out.println("1. Add Plane");
				System.out.println("2. Add Pilot");
				System.out.println("3. Add Flight");
				System.out.println("4. Add Technician");
				System.out.println("5. Book Flight");
				System.out.println("6. List number of available seats for a given flight.");
				System.out.println("7. List total number of repairs per plane in descending order");
				System.out.println("8. List total number of repairs per year in ascending order");
				System.out.println("9. Find total number of passengers with a given status");
				System.out.println("10. < EXIT");

				switch (readChoice()){
					case 1: AddPlane(esql); break;
					case 2: AddPilot(esql); break;
					case 3: AddFlight(esql); break;
					case 4: AddTechnician(esql); break;
					case 5: BookFlight(esql); break;
					case 6: ListNumberOfAvailableSeats(esql); break;
					case 7: ListsTotalNumberOfRepairsPerPlane(esql); break;
					case 8: ListTotalNumberOfRepairsPerYear(esql); break;
					case 9: FindPassengersCountWithStatus(esql); break;
					case 10: keepon = false; break;
				}
			}
		}catch(Exception e){
			System.err.println (e.getMessage ());
		}finally{
			try{
				if(esql != null) {
					System.out.print("Disconnecting from database...");
					esql.cleanup ();
					System.out.println("Done\n\nBye !");
				}//end if
			}catch(Exception e){
				// ignored.
			}
		}
	}

	public static int readChoice() {
		int input;
		// returns only if a correct value is given.
		do {
			System.out.print("Please make your choice: ");
			try { // read the integer, parse it and break.
				input = Integer.parseInt(in.readLine());
				break;
			}catch (Exception e) {
				System.out.println("Your input is invalid!");
				continue;
			}//end try
		}while (true);
		return input;
	}//end readChoice

	public static void AddPlane(DBproject esql) {// Choice 1
		try {
			//get next id in sequence
			String id_query = "SELECT COUNT(*) FROM Plane";
			int id = Integer.parseInt(esql.executeQueryAndReturnResult(id_query).get(0).get(0));

			System.out.println("What is the plane's make? $");
			String make = in.readLine();

			System.out.println("What is the plane's model? $");
			String model = in.readLine();

			System.out.println("What is the plane's age? $");
			String age = in.readLine();

			System.out.println("How many seats does the plane have? $");
			String seats = in.readLine();

			String query = "INSERT INTO Plane VALUES ("
			+ id + ", \'"
			+ make + "\', \'"
			+ model + "\', "
			+ age + ", "
			+ seats + ");";
			esql.executeUpdate(query);

			// Test if insertion worked
			// String test_query = "SELECT MAX(id) FROM Plane";
			// esql.executeQueryAndPrintResult(test_query);

			System.out.println("Plane added!");

		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	public static void AddPilot(DBproject esql) {// Choice 2
		try {
			//get next id in sequence
			String id_query = "SELECT COUNT(*) FROM Pilot";
			int id = Integer.parseInt(esql.executeQueryAndReturnResult(id_query).get(0).get(0));

			System.out.println("What is the pilot's full name? $");
			String name = in.readLine();

			System.out.println("What is the pilot's nationality? $");
			String nationality = in.readLine();

			String query = "INSERT INTO Pilot VALUES ("
			+ id + ", \'"
			+ name + "\', \'"
			+ nationality + "\');";
			esql.executeUpdate(query);
			System.out.println("Pilot added!");

			// Test if insertion worked
			// String test_query = "SELECT MAX(id) FROM Pilot;";
			// esql.executeQueryAndPrintResult(test_query);

		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	// Given a pilot, plane and flight info, adds a flight in the DB
	public static void AddFlight(DBproject esql) {// Choice 3
		try {

		//------------------------ADD TO FLIGHT-------------------------

			//get next id in sequence
			String id_query = "SELECT COUNT(*) FROM Flight";
			int fnum = Integer.parseInt(esql.executeQueryAndReturnResult(id_query).get(0).get(0));

			System.out.println("How much does the flight cost? $");
			String cost = in.readLine();

			System.out.println("How many seats are sold? $");
			String seats = in.readLine();

			System.out.println("How mnay stops does this flight make? $");
			String stops = in.readLine();

			System.out.println("What is the departure time? (use format yyyy-mm-dd hh:mm) $");
			String dep_time = in.readLine();

			System.out.println("What is the arrival time? (use format yyyy-mm-dd hh:mm) $");
			String arv_time = in.readLine();

			System.out.println("Where is the flight arriving to? $");
			String ariv = in.readLine();

			System.out.println("Where is the flight departing from? $");
			String dept = in.readLine();

			String query = "INSERT INTO Flight VALUES ("
			+ fnum + ", "
			+ cost + ", "
			+ seats + ", "
			+ stops + ", \'"
			+ dep_time + "\', \'"
			+ arv_time + "\', \'"
			+ ariv + "\', \'"
			+ dept + "\');";
			System.out.println(query);
			esql.executeUpdate(query);

			// Test if insertion worked
			// String test_query = "SELECT MAX(fnum) FROM Flight;";
			// esql.executeQueryAndPrintResult(test_query);

			//------------------------ADD TO FLIGHT INFO-------------------------

			//get next id in sequence
			id_query = "SELECT COUNT(*) FROM FlightInfo";
			int finfo = Integer.parseInt(esql.executeQueryAndReturnResult(id_query).get(0).get(0));

			System.out.println("What's the pilot's id? $");
			String pilot = in.readLine();

			System.out.println("What's the plane's id? $");
			String plane = in.readLine();

			String query2 = "INSERT INTO FlightInfo VALUES ("
			+ finfo + ", "
			+ fnum + ", "
			+ pilot + ", "
			+ plane + ");";
			esql.executeUpdate(query2);

			// Test if insertion worked
			// test_query = "SELECT MAX(flight_id) FROM FlightInfo;";
			// esql.executeQueryAndPrintResult(test_query);

			//------------------------ADD TO SCHEDULE-------------------------

			//get next id in sequence
			id_query = "SELECT COUNT(*) FROM Schedule";
			int schedule = Integer.parseInt(esql.executeQueryAndReturnResult(id_query).get(0).get(0));

			//insert into schedule
			String query3 = "INSERT INTO Schedule VALUES ("
			+ schedule + ", "
			+ fnum + ", \'"
			+ dep_time + "\', \'"
			+ arv_time + "\');";
			esql.executeUpdate(query3);

			// Test if insertion worked
			// test_query = "SELECT MAX(flightNum) FROM Schedule;";
			// esql.executeQueryAndPrintResult(test_query);

			System.out.println("Flight added!");
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	public static void AddTechnician(DBproject esql) {// Choice 4
		try {
			//get next id in sequence
			String id_query = "SELECT COUNT(*) FROM Technician";
			int id = Integer.parseInt(esql.executeQueryAndReturnResult(id_query).get(0).get(0)) +1;

			System.out.println("What is the technician's full name? $");
			String name = in.readLine();

			String query = "INSERT INTO Technician VALUES ("
			+ id + ", \'"
			+ name + "\');";
			esql.executeUpdate(query);
			System.out.println("Technician added!");

			// Test if technician was properly inserted
			// String test_query = "SELECT MAX(id) FROM Technician;";
			// esql.executeQueryAndPrintResult(test_query);

		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	// Given a customer and a flight that he/she wants to book, add a reservation to the DB
	// This assumes the customer is already registered in the system
	public static void BookFlight(DBproject esql) {// Choice 5
		try {
			System.out.println("Please enter your customer id. $");
			String customer = in.readLine();
			System.out.println("Please enter the flight number of the flight you wish to book. $");
			String flight = in.readLine();

			//see if flight is already full

			// get plane capacity
			String max = "SELECT p.seats FROM FlightInfo fi, Plane p WHERE fi.flight_id = "
			+ flight + " AND fi.plane_id = p.id;";
			int max_seats = Integer.parseInt(esql.executeQueryAndReturnResult(max).get(0).get(0)) ;

			// get seats sold
			String seats_sold = "SELECT f.num_sold FROM Flight f WHERE f.fnum = "
			+ flight + ";";
			int sold = Integer.parseInt(esql.executeQueryAndReturnResult(seats_sold).get(0).get(0)) ;

			// get next rnum
			String id_query = "SELECT COUNT(*) FROM Reservation";
			int id = Integer.parseInt(esql.executeQueryAndReturnResult(id_query).get(0).get(0));
			String query = "INSERT INTO Reservation VALUES ("
			+ id + ", "
			+ customer + ", "
			+ flight + ", ";

			//if full, put on waitlist
			if(sold >= max_seats) {
				System.out.println("Sorry, this flight is already full. Adding to waitlist...");
				query += "\'W\');";
			} else { //else, see if paying now or later
				System.out.println("Will this be paid now? (y or n) $");
				String op = in.readLine();
				switch(op) {
					case "y": //Can pay now so
					case "Y": //confirm reservation
						query += "\'C\');";
						System.out.println("We will confirm your reservation now...");
						break;
					case "n": //Cant pay now so
					case "N": //only reserve
						query += "\'R\');";
						System.out.println("Please pay before seats get full. We will reserve your flight now...");
						break;
					default:
						System.out.println("Your input \'" + op + "\' is invalid!");
						return;
				}
			}

			esql.executeUpdate(query);
			System.out.println("Your reservation has been added!");
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}


	// For flight number and date, find the number of available seats (i.e. total plane capacity minus booked seats )
	public static void ListNumberOfAvailableSeats(DBproject esql) {// Choice 6
		try {
			System.out.println("Please enter a flight number: $");
			String fnum = in.readLine();

			System.out.println("Please enter a date (use format yyyy-mm-dd hh:mm): $");
			String date = in.readLine();

			String query = "SELECT (p.seats - f.num_sold) AS available_seats "
			+ "FROM FlightInfo fi, Flight f, Plane p "
			+ "WHERE fi.flight_id = f.fnum AND fi.plane_id = p.id "
			+ "AND f.fnum = " + fnum
			+ " AND f.actual_departure_date = \'" + date + "\';";

			esql.executeQueryAndPrintResult(query);

		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	// Count number of repairs per planes and list them in descending order
	// Assume repairs is descending
	public static void ListsTotalNumberOfRepairsPerPlane(DBproject esql) {// Choice 7
		try {
			String query = "SELECT plane_id, COUNT(rid) as num_repairs "
			+ "FROM Repairs "
			+ "GROUP BY plane_id "
			+ "ORDER BY num_repairs DESC;";

			esql.executeQueryAndPrintResult(query);

		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	// Count repairs per year and list them in ascending order
	// Assume year is ascending
	public static void ListTotalNumberOfRepairsPerYear(DBproject esql) {// Choice 8
		try {
			String query = "SELECT R.r_year, COUNT(*) "
			+ "FROM ( "
			+ "SELECT EXTRACT (YEAR from repair_date) AS r_year "
			+ "FROM Repairs "
			+ ") AS R "
			+ "GROUP BY R.r_year "
			+ "ORDER BY R.r_year ASC;";

			esql.executeQueryAndPrintResult(query);

		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	// Find how many passengers there are with a status (i.e. W,C,R) and list that number.
	public static void FindPassengersCountWithStatus(DBproject esql) {// Choice 9
		try {
			System.out.println("Would you like to find the number of waitlisted, confirmed, or reserved passengers? (w, c, or r) $");
			String status = in.readLine();

			String query = "SELECT COUNT(*) AS Number_Of_Passengers FROM Reservation ";

			switch(status) {
				case "w" :
					query += "WHERE status = 'W';";
					break;
				case "W" :
					query += "WHERE status = 'W';";
					break;
				case "c" :
					query += "WHERE status = 'C';";
					break;
				case "C" :
					query += "WHERE status = 'C';";
					break;
				case "r" :
					query += "WHERE status = 'R';";
					break;
				case "R" :
					query += "WHERE status = 'R';";
					break;
				default:
					System.out.println("Your input \'" + status + "\' is invalid!");
					return;
			}

			esql.executeQueryAndPrintResult(query);

		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	public static void createIndexes(DBproject esql) {
		try {
			System.out.println("Creating indexes...");
			String query = "DROP INDEX IF EXISTS Customer_index; "
			+ "DROP INDEX IF EXISTS Flight_index; "
			+ "DROP INDEX IF EXISTS Pilot_index; "
			+ "DROP INDEX IF EXISTS Plane_index; "
			+ "DROP INDEX IF EXISTS Technician_index; "
			+ "DROP INDEX IF EXISTS Reservation_index; "
			+ "DROP INDEX IF EXISTS FlightInfo_index; "
			+ "DROP INDEX IF EXISTS Repairs_index; "
			+ "DROP INDEX IF EXISTS Schedule_index; "
			+ "CREATE INDEX Customer_index ON Customer USING BTREE (id); "
			+ "CREATE INDEX Flight_index ON Flight USING BTREE (fnum); "
			+ "CREATE INDEX Pilot_index ON Pilot USING BTREE (id); "
			+ "CREATE INDEX Plane_index ON Plane USING BTREE (id); "
			+ "CREATE INDEX Technician_index ON Technician USING BTREE (id); "
			+ "CREATE INDEX Reservation_index ON Reservation USING BTREE (rnum); "
			+ "CREATE INDEX FlightInfo_index ON FlightInfo USING BTREE (fiid); "
			+ "CREATE INDEX Repairs_index ON Repairs USING BTREE (rid); "
			+ "CREATE INDEX Schedule_index ON Repairs USING BTREE (id); ";
			esql.executeUpdate(query);
			System.out.println("Indexes Created!");
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}
}
