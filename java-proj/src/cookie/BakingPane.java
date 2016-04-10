package cookie;

import javax.swing.*;
import javax.swing.event.*;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class BakingPane extends BasicPane {
	private static final long serialVersionUID = 1;
	private DefaultListModel<String> nameListModel;
	private JList<String> cookieList;
	private JPanel topField;

	public BakingPane(Database db) {
		super(db);
	}

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

	public JComponent createTopPanel() {
		topField = new JPanel();
		topField.setLayout(new FlowLayout(FlowLayout.LEFT));
		topField.setLayout(new GridLayout(0, 2));
		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
		p.add(topField);
		return p;
	}

	public JComponent createBottomPanel() {
		JButton[] buttons = new JButton[1];
		buttons[0] = new JButton("Bake Pallet");
		return new ButtonAndMessagePanel(buttons, messageLabel, new ActionHandler());
	}

	public void entryActions() {
		clearMessage();
		fillNameList();
		clearFields();
	}

	private void fillNameList() {
		nameListModel.removeAllElements();
		ArrayList<String> cookie = db.getCookies();
		for(String name : cookie){
			nameListModel.addElement(name);
		}
	}

	private void clearFields() {
		topField.removeAll();
		topField.revalidate();
	}

	class CookieSelectionListener implements ListSelectionListener {
		public void valueChanged(ListSelectionEvent e) {
			if (cookieList.isSelectionEmpty()) {
				return;
			}
			String cookieName = cookieList.getSelectedValue();
			clearFields();
			topField.add(new JLabel("Ingredients needed for a pallet of " + cookieName + ":"));
			topField.add(new JLabel("      ")); //padd gridlayout
			topField.add(new JLabel("      "));
			topField.add(new JLabel("      "));

			ArrayList<Ingredient> ingr = db.getIngredients(cookieName);
			for(Ingredient i : ingr){
				topField.add(new JLabel(i.iName));
				topField.add(new JLabel(Integer.toString(i.iAmount*15*10*36)));
				// 15 cookies in each bag, with 10 bags in each box, each pallet contains 36 boxes.
			}
			topField.revalidate();
		}
	}

	class ActionHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (cookieList.isSelectionEmpty()) {
				return;
			}
			if (!db.isConnected()){
				displayMessage("No database connection");
				return;
			}
			String cookieName = cookieList.getSelectedValue();
			if(db.bakePallet(cookieName)){ //  enough ingredients
				displayMessage("Pallet baked");
			}else{
				displayMessage("Baking unsuccessfull, ingredients unavailable");
			}
		}
	}
}
