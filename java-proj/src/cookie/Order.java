package cookie;

import java.util.Date;
import java.util.Map;

public class Order {
	public int orderID;
	public String uName;
	public String dDate;
	public String address;
	public Map<String, Integer> oItems;
	
	public Order(int orderID, String uName, String dDate, String address, Map<String, Integer> oItems){
		this.orderID = orderID;
		this.uName = uName;
		this.dDate = dDate;
		this.address = address;
		this.oItems = oItems;
	}
	

}
