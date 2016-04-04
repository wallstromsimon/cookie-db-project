package cookie;

import javax.swing.*;
import javax.swing.event.*;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * The GUI pane where a user books tickets for movie performances. It contains
 * one list of movies and one of performance dates. The user selects a
 * performance by clicking in these lists. The performance data is shown in the
 * fields in the right panel. The bottom panel contains a button which the user
 * can click to book a ticket to the selected performance.
 */
public class PalletStoragePane extends BasicPane {
	private static final long serialVersionUID = 1;

	/**
	 * The list model for the movie name list.
	 */
	private DefaultListModel<String> nameListModel;

	/**
	 * The cookie name list.
	 */
	private JList<String> palletList;
	
	//JPanel for Top
	private JPanel topField;  


	/**
	 * Create the booking pane.
	 * 
	 * @param db
	 *            The database object.
	 */
	public PalletStoragePane(Database db) {
		super(db);
		entryActions();
	}

	/**
	 * Create the left panel, containing the movie name list and the performance
	 * date list.
	 * 
	 * @return The left panel.
	 */
	public JComponent createLeftPanel() {
		nameListModel = new DefaultListModel<String>();

		palletList = new JList<String>(nameListModel);
		palletList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		palletList.setPrototypeCellValue("123456789012");
		palletList.addListSelectionListener(new CookieSelectionListener());
		JScrollPane p1 = new JScrollPane(palletList);
		JPanel p = new JPanel();
		p.setLayout(new GridLayout(1, 2));
		p.add(p1);
		return p;
	}

	/**
	 * Create the top panel, containing the fields with the performance data.
	 * 
	 * @return The top panel.
	 */
	public JComponent createTopPanel() {
		topField = new JPanel();
		topField.setLayout(new BoxLayout(topField, BoxLayout.PAGE_AXIS));
		
		JPanel p1 = new JPanel();
		p1.setLayout(new FlowLayout(FlowLayout.LEFT));
		//p1.add(new JLabel("Ingredients needed for a pallet:"));

		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
		p.add(p1);
		p.add(topField);
		return p;
	}

	/**
	 * Create the bottom panel, containing the book ticket-button and the
	 * message line.
	 * 
	 * @return The bottom panel.
	 */
	public JComponent createBottomPanel() {
		JButton[] buttons = new JButton[1];
		buttons[0] = new JButton("Nothing");
		return new ButtonAndMessagePanel(buttons, messageLabel,
				new ActionHandler());
	}

	/**
	 * Perform the entry actions of this pane: clear all fields, fetch the movie
	 * names from the database and display them in the name list.
	 */
	public void entryActions() {
		clearMessage();
		fillNameList();
		clearFields();
	}

	/**
	 * Fetch cookie names from the database and display them in the name list.
	 */
	private void fillNameList() {
		nameListModel.removeAllElements();
		/* --- insert own code here --- */
		ArrayList<Pallet> pa = db.getPalletList();
		for(Pallet p : pa){
			nameListModel.addElement(p.palletID + " : " + p.cName);
		}
	}

	/**
	 * Clear all text fields.
	 */
	private void clearFields() {
		topField.removeAll();
		topField.revalidate();
	}

	/**
	 * A class that listens for clicks in the name list.
	 */
	class CookieSelectionListener implements ListSelectionListener {
		/**
		 * Called when the user selects a name in the name list. Fetches
		 * performance dates from the database and displays them in the date
		 * list.
		 * 
		 * @param e
		 *            The selected list item.
		 */
		public void valueChanged(ListSelectionEvent e) {
			if (palletList.isSelectionEmpty()) {
				return;
			}
			String ingrName = palletList.getSelectedValue();
			/* --- insert own code here --- */
			clearFields();
			ArrayList<Ingredient> ingr = db.getIngredientList(); //Change this to get amount for one ingr?
			for(Ingredient i : ingr){
				if(ingrName.equals(i.iName)){
					topField.add(new JLabel("We have " + Integer.toString(i.iAmount) + " *units* of " + i.iName +"."));
				}
			}
		}
	}

	/**
	 * A class that listens for button clicks.
	 */
	class ActionHandler implements ActionListener {
		/**
		 * Called when the user clicks the Book ticket button. Books a ticket
		 * for the current user to the selected performance (adds a booking to
		 * the database).
		 * 
		 * @param e
		 *            The event object (not used).
		 */
		public void actionPerformed(ActionEvent e) {
			if (palletList.isSelectionEmpty()) {
				return;
			}
			if (!db.isConnected()){
				displayMessage("No database connection");
				return;
			}
			
			String cookieName = palletList.getSelectedValue(); //Needed for db
			
			if(true){ //  enough ingredients
				displayMessage("Nothing");
				//DB call to update ingredient list and pallet storage
			}else{
				displayMessage("Nothing");
			}
		}
	}
}
