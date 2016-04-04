package cookie;

import java.util.Date;

public class Pallet {
	public int palletID;
	public String cName;
	public Date made;
	public boolean blocked;
	
	public Pallet(int ID, String name, Date date, boolean block){
		this.palletID = ID;
		this.cName = name;
		this.made = date;
		this.blocked = block;
	}

}
