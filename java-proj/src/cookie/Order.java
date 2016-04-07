package cookie;

import java.util.ArrayList;
import java.util.Date;

public class Order {
	public int orderID;
	public String uName;
	public Date dDate;
	public String address;
	public ArrayList<Pallet> oItems;
	
	public Order(int orderID, String uName, Date dDate, String address, ArrayList<Pallet> oItems){
		this.orderID = orderID;
		this.uName = uName;
		this.dDate = dDate;
		this.address = address;
		this.oItems = oItems;
	}
	

}
