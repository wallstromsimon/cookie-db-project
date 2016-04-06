package cookie;

import javax.swing.*;
import javax.swing.event.*;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class IngredientStoragePane extends BasicPane {
	private static final long serialVersionUID = 1;
	private DefaultListModel<String> nameListModel;
	private JList<String> ingrList;
	private JPanel topField;

	public IngredientStoragePane(Database db) {
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
		ingrList = new JList<String>(nameListModel);
		ingrList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		ingrList.setPrototypeCellValue("123456789012");
		ingrList.addListSelectionListener(new CookieSelectionListener());
		JScrollPane p1 = new JScrollPane(ingrList);
		JPanel p = new JPanel();
		p.setLayout(new GridLayout(1, 1)); //1,2 från början
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
		JButton[] buttons = new JButton[0];
		return new ButtonAndMessagePanel(buttons, messageLabel,
				new ActionHandler());
	}

	/**
	 * Perform the entry actions of this pane: clear all fields, fetch the
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
		ArrayList<Ingredient> ingr = db.getIngredientList();
		for(Ingredient i : ingr){
			nameListModel.addElement(i.iName);
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
			if (ingrList.isSelectionEmpty()) {
				return;
			}
			String ingrName = ingrList.getSelectedValue();
			clearFields();
			Ingredient i = db.getIngredient(ingrName);
			topField.add(new JLabel("We have " + Integer.toString(i.iAmount) + " *units* of " + i.iName +"."));
			if(i.iAmount > 90000){
				topField.add(new JLabel("Over nine thousand!"));
			}
		}
	}

	/**
	 * A class that listens for button clicks.
	 */
	class ActionHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			//Not needed here, but still needed for button msg.
		}
	}
}
