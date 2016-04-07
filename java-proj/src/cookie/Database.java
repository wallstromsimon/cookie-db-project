package cookie;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;

/**
 * Database is a class that specifies the interface to the movie database. Uses
 * JDBC and the MySQL Connector/J driver.
 */
public class Database {
	/**
	 * The database connection.
	 */
	private Connection conn;

	/**
	 * Create the database interface object. Connection to the database is
	 * performed later.
	 */
	public Database() {
		conn = null;
	}

	/**
	 * Open a connection to the database, using the specified user name and
	 * password.
	 *
	 * @param userName
	 *            The user name.
	 * @param password
	 *            The user's password.
	 * @return true if the connection succeeded, false if the supplied user name
	 *         and password were not recognized. Returns false also if the JDBC
	 *         driver isn't found.
	 */
	public boolean openConnection(String userName, String password) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(
					"jdbc:mysql://puccini.cs.lth.se/" + userName, userName,
					password);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * Close the connection to the database.
	 */
	public void closeConnection() {
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
		}
		conn = null;
	}

	/**
	 * Check if the connection to the database has been established
	 *
	 * @return true if the connection has been established
	 */
	public boolean isConnected() {
		return conn != null;
	}

	/* --- insert own code here --- remember to close conn!*/

	public ArrayList<String> getCookies(){
		//SQL To get name of all cookies.

		ArrayList<String> cookieList = new ArrayList<String>();
		Statement listCookie = null;
		try {
			listCookie = conn.createStatement();
			ResultSet cookies = listCookie.executeQuery("SELECT cookieName FROM Cookie");
			while (cookies.next()) {
				cookieList.add(cookies.getString("cookieName"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			try {
				listCookie.close();                       //<-------------------Close like this!
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return cookieList;
	}

	public ArrayList<Ingredient> getIngredientList(){
		//SQL to get all ingredients
		ArrayList<Ingredient> ingredientList = new ArrayList<Ingredient>();
		Statement listIngredient = null;
		try {
			listIngredient = conn.createStatement();
			ResultSet ingredients = listIngredient.executeQuery("SELECT * FROM Ingredient");
			while (ingredients.next()) {
				ingredientList.add(new Ingredient(ingredients.getString("IngredientName"), ingredients.getInt("AmountLeft")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			try {
				listIngredient.close();                       //<-------------------Close like this!
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return ingredientList;
	}

	public ArrayList<Ingredient> getIngredients(String cookieName) {
		ArrayList<Ingredient> ingredientList = new ArrayList<Ingredient>();
		Statement listIngredient = null;
		try {
			listIngredient = conn.createStatement();
			ResultSet ingredients = listIngredient.executeQuery("SELECT * FROM RecipeItem WHERE CookieName = '" + cookieName + "'");
			while (ingredients.next()) {
				ingredientList.add(new Ingredient(ingredients.getString("IngredientName"), ingredients.getInt("Amount")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			try {
				listIngredient.close();                       //<-------------------Close like this!
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return ingredientList;
	}

	public ArrayList<Pallet> getPalletList() { //Only IDs enough?
		ArrayList<Pallet> pList = new ArrayList<Pallet>();
		Statement listIngredient = null;
		try {
			listIngredient = conn.createStatement();
			ResultSet pl = listIngredient.executeQuery("SELECT * FROM Pallet");
			while (pl.next()) {
				pList.add(new Pallet(pl.getInt("PalletID"), pl.getString("CookieName"), pl.getDate("DateMade"), pl.getBoolean("Blocked")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			try {
				listIngredient.close();                       //<-------------------Close like this!
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return pList;
	}

	public Ingredient getIngredient(String ingrName) {
		Ingredient ingr = null;
		Statement listIngredient = null;
		try {
			listIngredient = conn.createStatement();
			ResultSet ingredients = listIngredient.executeQuery("SELECT * FROM Ingredient WHERE IngredientName = '" + ingrName + "'");
			while (ingredients.next()) {
				ingr = new Ingredient(ingredients.getString("IngredientName"), ingredients.getInt("AmountLeft"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			try {
				listIngredient.close();                       //<-------------------Close like this!
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return ingr;
	}

	public boolean bakePallet(String cookieName) {
		// Check if there is enough Ingr in storage to bake a pallet
		// (15 cookies in each bag, with 10 bags in each box, each pallet contains 36 boxes.)
		// Update Ingr storage
		// Make new Pallet with cookieName as cookie type
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Statement transaction = null;
		boolean baked = false;
		try {
			conn.setAutoCommit(false); //Does this affect getIngredient() ?
			
			ArrayList<Ingredient> in = getIngredients(cookieName);
			LinkedList<Integer> diff = new LinkedList<Integer>();
			
			for(Ingredient i : in){
				Ingredient storage = getIngredient(i.iName);
				// 15 cookies in each bag, with 10 bags in each box, each pallet contains 36 boxes.
				if((i.iAmount*15*10*36) > storage.iAmount){
					//rollback?? :O
					return false;
				}else{
					diff.add(storage.iAmount - (i.iAmount*15*10*36));
				}
			}
			
			transaction = conn.createStatement();
			
			for(Ingredient i : in){
				transaction.executeUpdate("UPDATE Ingredient SET AmountLeft = " + diff.remove() + " WHERE IngredientName = '" + i.iName + "'");

			}
			transaction.executeUpdate("INSERT into Pallet values(0, '" + cookieName + "', '" + dateFormat.format(new Date()) + " " + "', '" + 0 + "')");
			conn.commit();
			baked = true;
			//conn.rollback(); 
			conn.setAutoCommit(true);
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				transaction.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return baked;
	}

	public boolean block(String pallet) {
		// Block update a pallet to blocked.
		return false;
	}

	public ArrayList<Integer> getOrderNbrs() {
		// Return arralist with all order nbrs
		return null;
	}

	public Order getOrderInfo(int oID) {
		// return a order obj with oID
		return null;
	}




	/* Old code below, Keep as ref and then remove

	public boolean logIn(String userId) {
		boolean exists = false; //User exist in db
		try {
			String sql = "SELECT * FROM Users WHERE uName = ?";
			PreparedStatement login = conn.prepareStatement(sql);
			login.setString(1, userId);
			ResultSet users = login.executeQuery();
			exists =  users.next();
		} catch (SQLException e) {
			System.out.println("No connection to database");
			e.printStackTrace();
		}

		if(exists){
//			CurrentUser.instance().loginAs(userId);
		}else{
//			CurrentUser.instance().loginAs(null);
		}
		return exists;
	}

	public ArrayList<String> getMovies() {
		ArrayList<String> movieList = new ArrayList<String>();
		Statement listMovies = null;
		try {
			listMovies = conn.createStatement();
			ResultSet movies = listMovies.executeQuery("SELECT mName FROM Movies");
			while (movies.next()) {
				movieList.add(movies.getString("mName"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			try {
				listMovies.close();                       <-------------------Close like this!
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return movieList;
	}

	public ArrayList<String> getDates(String movieName) {
		ArrayList<String> dateList = new ArrayList<String>();
		try {
			Statement listDate;
			listDate = conn.createStatement();
			ResultSet dates = listDate.executeQuery("SELECT pDate FROM Performance WHERE mName = '" + movieName + "'");
			while (dates.next()) {
				dateList.add(dates.getString("pDate"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return dateList;
	}

	public Performance getPerformance(String mName, String date) {
		try {
			Statement theaterSQL = conn.createStatement();
			ResultSet theater = theaterSQL.executeQuery("SELECT tName, seatCount FROM Performance WHERE pDate = '" + date + "'" + " AND mName = '" + mName + "'");
			theater.next();
			return new Performance(date, mName, theater.getString("tName"), theater.getInt("seatCount"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public int book(String movieName, String date) {
		int ticketNbr = -1;
		try {
			conn.setAutoCommit(false);
			Statement transaction = conn.createStatement();
			ResultSet booking = transaction.executeQuery("SELECT tName, seatCount FROM Performance WHERE pDate = '" + date + "'" + " AND mName = '"	+ movieName + "' FOR UPDATE");
			booking.next();
			int seatsLeft = booking.getInt("seatCount");
			if(seatsLeft > 0){
				transaction.executeUpdate("UPDATE Performance SET seatCount = " + (seatsLeft - 1) + " WHERE pDate = '" + date + "'" + " AND mName = '" + movieName + "'");
//				transaction.executeUpdate("INSERT into Tickets values(0, '" + CurrentUser.instance().getCurrentUserId() + "', '" + movieName + "', '" + date + "')");
				booking = transaction.executeQuery("SELECT last_insert_id() AS id");
				booking.next();
				ticketNbr = booking.getInt("id");
				conn.commit();
			}else{
				conn.rollback();
			}
			conn.setAutoCommit(true);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ticketNbr;
	}
 */


}
