package cookie;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class OrderPane extends BasicPane {
	private static final long serialVersionUID = 1;
	private DefaultListModel<String> nameListModel;
	private JList<String> orderList;
	private JPanel topField;

	public OrderPane(Database db) {
		super(db);
	}

	public JComponent createLeftPanel() {
		nameListModel = new DefaultListModel<String>();
		orderList = new JList<String>(nameListModel);
		orderList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		orderList.setPrototypeCellValue("123456789012");
		orderList.addListSelectionListener(new CookieSelectionListener());
		JScrollPane p1 = new JScrollPane(orderList);
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
		ArrayList<Integer> ol = db.getOrderNbrs();
		for(int o : ol){
			nameListModel.addElement(Integer.toBinaryString(o));
		}
	}

	private void clearFields() {
		topField.removeAll();
		topField.revalidate();
	}

	class CookieSelectionListener implements ListSelectionListener {
		public void valueChanged(ListSelectionEvent e) {
			if (orderList.isSelectionEmpty()) {
				return;
			}
			int oID = Integer.parseInt(orderList.getSelectedValue());
			clearFields();
			Order o = db.getOrderInfo(oID); 
			topField.add(new JLabel("Order: " + Integer.toString(o.orderID)));
			topField.add(new JLabel("Delivery Date: " + o.dDate));
			topField.add(new JLabel("Deliver to: " + o.uName + ": " + o.address));	
			topField.add(new JLabel("    "));
			topField.add(new JLabel("Order includes: "));
			for(String s : o.oItems.keySet()){
				topField.add(new JLabel(o.oItems.get(s) + "st " + s));
			}
		}
	}

	class ActionHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
		}
	}
}
