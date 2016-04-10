package cookie;

import javax.swing.*;
import javax.swing.event.*;

import java.awt.*;
import java.awt.event.*;

public class CookieGUI {
	private Database db;

	private JTabbedPane tabbedPane;

	public CookieGUI(Database db) {
		this.db = db;

		JFrame frame = new JFrame("CookieBaking");
		tabbedPane = new JTabbedPane();

		IngredientStoragePane ingredientStoragePane = new IngredientStoragePane(db);
		tabbedPane.addTab("Storage", null, ingredientStoragePane, "store that sugar");

		BakingPane bakingPane = new BakingPane(db);
		tabbedPane.addTab("Bake Pallet", null, bakingPane, "Bake");

		PalletStoragePane palletStoragePane = new PalletStoragePane(db);
		tabbedPane.addTab("Pallet storage", null, palletStoragePane, "Pallet storage");
		
		OrderPane orderPane = new OrderPane(db);
		tabbedPane.addTab("Order overview", null, orderPane, "View orders");

		tabbedPane.setSelectedIndex(0);

		frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);

		tabbedPane.addChangeListener(new ChangeHandler());
		frame.addWindowListener(new WindowHandler());

		frame.setSize(600, 600);
		frame.setVisible(true);

		/* --- change code here --- */
		/* --- change xxx to your user name, yyy to your password --- */
		if (db.openConnection("db85", "bil123!")) {
			ingredientStoragePane.entryActions();
			ingredientStoragePane.displayMessage("Connected to database");
		} else {
			ingredientStoragePane.displayMessage("Could not connect to database");
		}
	}

	/**
	 * ChangeHandler is a listener class, called when the user switches panes.
	 */
	class ChangeHandler implements ChangeListener {
		/**
		 * Called when the user switches panes. The entry actions of the new
		 * pane are performed.
		 *
		 * @param e
		 *            The change event (not used).
		 */
		public void stateChanged(ChangeEvent e) {
			BasicPane selectedPane = (BasicPane) tabbedPane
					.getSelectedComponent();
			selectedPane.entryActions();
		}
	}

	/**
	 * WindowHandler is a listener class, called when the user exits the
	 * application.
	 */
	class WindowHandler extends WindowAdapter {
		/**
		 * Called when the user exits the application. Closes the connection to
		 * the database.
		 *
		 * @param e
		 *            The window event (not used).
		 */
		public void windowClosing(WindowEvent e) {
			db.closeConnection();
			System.exit(0);
		}
	}
}
