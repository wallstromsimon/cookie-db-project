package cookie;

import javax.swing.*;
import javax.swing.border.*;

import java.awt.*;


public class BasicPane extends JPanel {
	private static final long serialVersionUID = 1;
    protected Database db;
    protected JLabel messageLabel;

    public BasicPane(Database db) {
        this.db = db;
        messageLabel = new JLabel("      ");
        setLayout(new BorderLayout());
                
        JComponent leftPanel = createLeftPanel();
		leftPanel.setPreferredSize(new Dimension(200,600));
        add(leftPanel, BorderLayout.WEST);
                
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());
                
        JComponent topPanel = createTopPanel();
        JComponent middlePanel = createMiddlePanel();
        JComponent bottomPanel = createBottomPanel();
        bottomPanel.setBorder
            (new CompoundBorder(new SoftBevelBorder(BevelBorder.RAISED),
                                bottomPanel.getBorder()));
        rightPanel.add(topPanel, BorderLayout.NORTH);
        rightPanel.add(middlePanel, BorderLayout.CENTER);
        rightPanel.add(bottomPanel, BorderLayout.SOUTH);
        add(rightPanel, BorderLayout.CENTER);
    }
        
    public JComponent createLeftPanel() { 
        return new JPanel();
    }
        
    public JComponent createTopPanel() { 
        return new JPanel();
    }
        
    public JComponent createMiddlePanel() { 
        return new JPanel();
    }
        
    public JComponent createBottomPanel() { 
        return new JPanel();
    }
        
    public void entryActions() {}
        
    public void displayMessage(String msg) {
        messageLabel.setText(msg);
        messageLabel.revalidate();
    }
        
    public void clearMessage() {
        messageLabel.setText(" ");
    }
}
