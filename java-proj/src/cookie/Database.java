package cookie;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * JDBC and the MySQL Connector/J driver.
 */
public class Database {
	private Connection conn;

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

	public void closeConnection() {
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
		}
		conn = null;
	}

	public boolean isConnected() {
		return conn != null;
	}


	public ArrayList<String> getCookies(){
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
				listCookie.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return cookieList;
	}

	public ArrayList<Ingredient> getIngredientList(){
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
				listIngredient.close();
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
				listIngredient.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return ingredientList;
	}

	public ArrayList<Pallet> getPalletList() {
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
				listIngredient.close();   
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
			while (ingredients.next()) { //if??
				ingr = new Ingredient(ingredients.getString("IngredientName"), ingredients.getInt("AmountLeft"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			try {
				listIngredient.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return ingr;
	}

	public boolean bakePallet(String cookieName) {
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
					//conn.rollback(); //Nope, don't change here. Checks first.
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
			conn.setAutoCommit(true);
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				transaction.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return baked;
	}

	public boolean block(String palletID) {
		Statement transaction = null;
		int blocked = 0;

		try {
			conn.setAutoCommit(false);
			transaction = conn.createStatement();
			ResultSet blockQ = transaction.executeQuery("SELECT * FROM Pallet WHERE PalletID = '" + palletID + "' FOR UPDATE");
			blockQ.next();
			blocked = blockQ.getInt("Blocked");
			if(blocked == 0){ //BLOCK
				transaction.executeUpdate("UPDATE Pallet SET Blocked = " + 1 + " WHERE PalletID = '" + palletID + "'");
				blocked = 1;
				conn.commit();
			}else{
				conn.rollback();
			}
			conn.setAutoCommit(true);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return blocked == 1 ? true : false;
	}

	public ArrayList<Integer> getOrderNbrs() {
		ArrayList<Integer> orders = new ArrayList<Integer>();
		Statement ordQ = null;
		try {
			ordQ = conn.createStatement();
			ResultSet o = ordQ.executeQuery("SELECT OrderID FROM Orders");
			while (o.next()) {
				orders.add(o.getInt("OrderID"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			try {
				ordQ.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return orders;
	}

	public Order getOrderInfo(int oID) {
		Order order = null;
		Map<String, Integer> map = new HashMap<String, Integer>();
		Statement ordQ = null;
		try {
			ordQ = conn.createStatement();
			ResultSet o = ordQ.executeQuery("SELECT * FROM Orders");
			if(o.next()){
				int id = o.getInt("OrderID");
				String uName = o.getString("Customer");
				String date = o.getString("DeliveryDate");
				ResultSet c = ordQ.executeQuery("SELECT * FROM Customer WHERE UserName = '" + uName + "'");
				String addr = "";
				if(c.next()){
					addr = c.getString("Address");
				}
				ResultSet l = ordQ.executeQuery("SELECT * FROM OrderedItem WHERE OrderID = '" + id + "'");
				while(l.next()){
					map.put(l.getString("CookieName"), l.getInt("NbrPAllets"));
				}
				order = new Order(id, uName, date, addr, map);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			try {
				ordQ.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return order;
	}
}
