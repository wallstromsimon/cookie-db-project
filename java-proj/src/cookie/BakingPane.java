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
public class BakingPane extends BasicPane {
	private static final long serialVersionUID = 1;
	/**
	 * A label showing the name of the current user.
	 */
	private JLabel currentUserNameLabel;

	/**
	 * The list model for the movie name list.
	 */
	private DefaultListModel<String> nameListModel;

	/**
	 * The cookie name list.
	 */
	private JList<String> cookieList;

	//JPanel for Top
	private JPanel topField;


	/**
	 * Create the booking pane.
	 *
	 * @param db
	 *            The database object.
	 */
	public BakingPane(Database db) {
		super(db);
	}

	/**
	 * Create the left panel, containing the movie name list and the performance
	 * date list.
	 *
	 * @return The left panel.
	 */
	public JComponent createLeftPanel() {
		nameListModel = new DefaultListModel<String>();

		cookieList = new JList<String>(nameListModel);
		cookieList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		cookieList.setPrototypeCellValue("123456789012");
		cookieList.addListSelectionListener(new CookieSelectionListener());
		JScrollPane p1 = new JScrollPane(cookieList);

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
		p1.add(new JLabel("Ingredients needed for a pallet:"));
		//currentUserNameLabel = new JLabel("");

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
		buttons[0] = new JButton("Bake Pallet");
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
		ArrayList<String> cookie = db.getCookies();
		for(String name : cookie){
			nameListModel.addElement(name);
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
		public void valueChanged(ListSelectionEvent e) {
			if (cookieList.isSelectionEmpty()) {
				return;
			}
			String cookieName = cookieList.getSelectedValue();
			clearFields();
			ArrayList<Ingredient> ingr = db.getIngredients(cookieName);
			for(Ingredient i : ingr){
				topField.add(new JLabel(i.iName + "(amount*15*10*36) = " + Integer.toString(i.iAmount*15*10*36)));
				// 15 cookies in each bag, with 10 bags in each box, each pallet contains 36 boxes.
				// Maybe show how much we have in total of each ingr?
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
			if (cookieList.isSelectionEmpty()) {
				return;
			}
			if (!db.isConnected()){
				displayMessage("No database connection");
				return;
			}

			String cookieName = cookieList.getSelectedValue(); //Needed for db

			boolean baked = db.bakePallet(cookieName);

			if(baked){ //  enough ingredients
				displayMessage("BAKE THAT COOKIE BITCH");
				//DB call to update ingredient list and pallet storage
			}else{
				displayMessage("Baking unsuccessfull, ingredients unavailable");
			}
		}
	}
}
