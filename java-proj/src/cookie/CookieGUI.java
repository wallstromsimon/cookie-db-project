package cookie;

import javax.swing.*;
import javax.swing.event.*;

import java.awt.*;
import java.awt.event.*;

/**
 * MovieGUI is the user interface to the movie database. It sets up the main
 * window and connects to the database.
 */
public class CookieGUI {
	/**
	 * db is the database object
	 */
	private Database db;

	/**
	 * tabbedPane is the contents of the window. It consists of two panes, User
	 * login and Book tickets.
	 */
	private JTabbedPane tabbedPane;

	/**
	 * Create a GUI object and connect to the database.
	 * 
	 * @param db
	 *            The database.
	 */
	public CookieGUI(Database db) {
		this.db = db;

		JFrame frame = new JFrame("CookieBaking");
		tabbedPane = new JTabbedPane();

		IngredientStoragePane ingredientStoragePane = new IngredientStoragePane(db);
		tabbedPane.addTab("Storage", null, ingredientStoragePane,
				"store that sugar");
		
		BakingPane bakingPane = new BakingPane(db);
		tabbedPane.addTab("Bake", null, bakingPane,
				"Bake ffs");

		PalletStoragePane palletStoragePane = new PalletStoragePane(db);
		tabbedPane.addTab("Pallet storage", null, palletStoragePane, "Store a pallet");

		tabbedPane.setSelectedIndex(0);

		frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);

		tabbedPane.addChangeListener(new ChangeHandler());
		frame.addWindowListener(new WindowHandler());

		frame.setSize(500, 400);
		frame.setVisible(true);

		/* --- change code here --- */
		/* --- change xxx to your user name, yyy to your password --- */
		if (db.openConnection("db85", "bil123!")) {
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
