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

	public JComponent createLeftPanel() {
		nameListModel = new DefaultListModel<String>();
		ingrList = new JList<String>(nameListModel);
		ingrList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		ingrList.setPrototypeCellValue("123456789012");
		ingrList.addListSelectionListener(new CookieSelectionListener());
		JScrollPane p1 = new JScrollPane(ingrList);
		JPanel p = new JPanel();
		p.setLayout(new GridLayout(1, 1));
		p.add(p1);
		return p;
	}

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

	public JComponent createBottomPanel() {
		JButton[] buttons = new JButton[0];
		return new ButtonAndMessagePanel(buttons, messageLabel, new ActionHandler());
	}

	public void entryActions() {
		clearMessage();
		fillNameList();
		clearFields();
	}

	private void fillNameList() {
		nameListModel.removeAllElements();
		ArrayList<Ingredient> ingr = db.getIngredientList();
		for(Ingredient i : ingr){
			nameListModel.addElement(i.iName);
		}
	}

	private void clearFields() {
		topField.removeAll();
		topField.revalidate();
	}

	class CookieSelectionListener implements ListSelectionListener {
		public void valueChanged(ListSelectionEvent e) {
			if (ingrList.isSelectionEmpty()) {
				return;
			}
			String ingrName = ingrList.getSelectedValue();
			clearFields();
			Ingredient i = db.getIngredient(ingrName);
			topField.add(new JLabel("We have " + Integer.toString(i.iAmount) + " 'units' of " + i.iName +"."));
		}
	}

	class ActionHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
		}
	}
}
