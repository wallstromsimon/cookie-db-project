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
	private DefaultListModel<String> nameListModel;
	private JList<String> palletList;
	private JPanel topField;

	public PalletStoragePane(Database db) {
		super(db);
	}

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

	public JComponent createBottomPanel() {
		JButton[] buttons = new JButton[1];
		buttons[0] = new JButton("BLOCK??");
		return new ButtonAndMessagePanel(buttons, messageLabel,
				new ActionHandler());
	}

	public void entryActions() {
		clearMessage();
		fillNameList();
		clearFields();
	}

	private void fillNameList() {
		nameListModel.removeAllElements();
		ArrayList<Pallet> pa = db.getPalletList();
		for(Pallet p : pa){
			nameListModel.addElement(p.palletID + " : " + p.cName);
		}
	}

	private void clearFields() {
		topField.removeAll();
		topField.revalidate();
	}

	class CookieSelectionListener implements ListSelectionListener {
		public void valueChanged(ListSelectionEvent e) {
			if (palletList.isSelectionEmpty()) {
				return;
			}
			String pIDcName = palletList.getSelectedValue();
			clearFields();
			ArrayList<Pallet> pa = db.getPalletList(); //Change this to get amount for one ingr?
			for(Pallet p : pa){
				if(pIDcName.equals(p.palletID + " : " + p.cName)){
					topField.add(new JLabel("Pallet " + Integer.toString(p.palletID)));
					topField.add(new JLabel("Made: " + p.made.toString()));
					topField.add(new JLabel("Contains: " + p.cName));
					topField.add(new JLabel("Blocked: " ));
				}
			}
		}
	}

	class ActionHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (palletList.isSelectionEmpty()) {
				return;
			}
			if (!db.isConnected()){
				displayMessage("No database connection");
				return;
			}

			String pallet = palletList.getSelectedValue(); //Needed for db

			if(db.block(pallet)){ 
				displayMessage("Blocked?");
			}else{
				displayMessage("Unblocked?");
			}
		}
	}
}
