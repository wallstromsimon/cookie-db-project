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

	/**
	 * The list model for the performance date list.
	 */
	private DefaultListModel<String> dateListModel;

	/**
	 * The performance date list.
	 */
	private JList<String> dateList;

	/**
	 * The text fields where the movie data is shown.
	 */
	private JTextField[] fields;

	/**
	 * The number of the movie name field.
	 */
	private static final int COOKIE = 0;

	/**
	 * The number of the performance date field.
	 */
	private static final int INGREDIENTS = 1;

	/**
	 * The number of the movie theater field.
	 */
	private static final int THEATER_NAME = 2;

	/**
	 * The number of the 'number of free seats' field.
	 */
	private static final int FREE_SEATS = 3;

	/**
	 * The total number of fields.
	 */
	private static final int NBR_FIELDS = 4;

	/**
	 * Create the booking pane.
	 * 
	 * @param db
	 *            The database object.
	 */
	public BakingPane(Database db) {
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

		cookieList = new JList<String>(nameListModel);
		cookieList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		cookieList.setPrototypeCellValue("123456789012");
		cookieList.addListSelectionListener(new CookieSelectionListener());
		JScrollPane p1 = new JScrollPane(cookieList);

		/*don't need another list now
		dateListModel = new DefaultListModel<String>();

		dateList = new JList<String>(dateListModel);
		dateList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		dateList.setPrototypeCellValue("123456789012");
		dateList.addListSelectionListener(new DateSelectionListener());
		JScrollPane p2 = new JScrollPane(dateList);
		*/
		JPanel p = new JPanel();
		p.setLayout(new GridLayout(1, 2));
		p.add(p1);
		//p.add(p2);
		return p;
	}

	/**
	 * Create the top panel, containing the fields with the performance data.
	 * 
	 * @return The top panel.
	 */
	public JComponent createTopPanel() {
		String[] texts = new String[NBR_FIELDS];
		texts[COOKIE] = "Cookie";
		texts[INGREDIENTS] = "Ingredients";
		texts[THEATER_NAME] = "Plays at";
		texts[FREE_SEATS] = "Free seats";

		fields = new JTextField[NBR_FIELDS];
		for (int i = 0; i < fields.length; i++) {
			fields[i] = new JTextField(20);
			fields[i].setEditable(false);
		}

		JPanel input = new InputPanel(texts, fields);

		JPanel p1 = new JPanel();
		p1.setLayout(new FlowLayout(FlowLayout.LEFT));
		p1.add(new JLabel("Current user: "));
		currentUserNameLabel = new JLabel("");
		p1.add(currentUserNameLabel);

		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
		p.add(p1);
		p.add(input);
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
		buttons[0] = new JButton("Book ticket");
		return new ButtonAndMessagePanel(buttons, messageLabel,
				new ActionHandler());
	}

	/**
	 * Perform the entry actions of this pane: clear all fields, fetch the movie
	 * names from the database and display them in the name list.
	 */
	public void entryActions() {
		clearMessage();
		currentUserNameLabel.setText(CurrentUser.instance().getCurrentUserId());
		fillNameList();
		clearFields();
	}

	/**
	 * Fetch cookie names from the database and display them in the name list.
	 */
	private void fillNameList() {
		nameListModel.removeAllElements();
		/* --- insert own code here --- */
		System.out.println("filling names");
		ArrayList<String> movie = db.getCookies();
		for(String name : movie){
			nameListModel.addElement(name);
		}
	}

	/**
	 * Fetch performance dates from the database and display them in the date
	 * list.
	 */
	private void fillDateList() {
		dateListModel.removeAllElements();
		/* --- insert own code here --- */ //Whaaaaaat?
	}

	/**
	 * Clear all text fields.
	 */
	private void clearFields() {
		for (int i = 0; i < fields.length; i++) {
			fields[i].setText("");
		}
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
			if (cookieList.isSelectionEmpty()) {
				return;
			}
			dateListModel.removeAllElements();
			String movieName = cookieList.getSelectedValue();
			/* --- insert own code here --- */
			ArrayList<String> dates = db.getDates(movieName);
			for(String date : dates){
				dateListModel.addElement(date);
			}
		}
	}

	/**
	 * A class that listens for clicks in the date list.
	 */
	class DateSelectionListener implements ListSelectionListener {
		/**
		 * Called when the user selects a name in the date list. Fetches
		 * performance data from the database and displays it in the text
		 * fields.
		 * 
		 * @param e
		 *            The selected list item.
		 */
		public void valueChanged(ListSelectionEvent e) {
			if (cookieList.isSelectionEmpty() || dateList.isSelectionEmpty()) {
				return;
			}
			String movieName = cookieList.getSelectedValue();
			String date = dateList.getSelectedValue();
			/* --- insert own code here --- */
			clearFields();
			Performance p = db.getPerformance(movieName,date);
			fields[COOKIE].setText(p.mName);
			fields[THEATER_NAME].setText(p.tName);
			fields[INGREDIENTS].setText(p.date);
			fields[FREE_SEATS].setText(Integer.toString(p.seatsLeft));
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
			if (cookieList.isSelectionEmpty() || dateList.isSelectionEmpty()) {
				return;
			}
			if (!CurrentUser.instance().isLoggedIn()) {
				displayMessage("Must login first");
				return;
			}
			String movieName = cookieList.getSelectedValue();
			String date = dateList.getSelectedValue();
			/* --- insert own code here --- */
			int ticketNbr = db.book(movieName, date);
			if(ticketNbr >= 0){ //  -1 for error
				displayMessage("One ticket booked, Booking number: " + ticketNbr);
				fields[FREE_SEATS].setText(Integer.toString(Integer.parseInt(fields[FREE_SEATS].getText()) - 1));
				
			}else{
				displayMessage("Booking unsuccessfull, no seats available");
			}
		}
	}
}
