package ca.Ranti;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

/*
 * 
 * CC
 */

public class TimeWasteIntro extends JDialog {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TimeWasteIntro(){
		
		Dimension dim = new Dimension(600,600); 
		
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

		setSize(dim);
		setLocationRelativeTo(null);
		// Set undecorated
		setUndecorated(true);
		setBackground(new Color(0, 255, 0, 0));

		setContentPane(new ContentPane());
		getContentPane().setBackground(Color.BLACK);
		setLayout(new BorderLayout());

		JPanel logo = new JPanel();
		logo.add(new JLabel(new ImageIcon("Data/images/UI/Retend7.png")));
		logo.setOpaque(false);
		
		add(logo);
		setVisible(true);
		
		
		try{
			Thread.sleep(500);
		}
		catch(InterruptedException e){
			e.printStackTrace();
		}
		
		dispose();
	}
	
	private class ContentPane extends JPanel {

	    /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public ContentPane() {

	        setOpaque(false);

	    }

	    @Override
	    protected void paintComponent(Graphics g) {

	        super.paintComponent(g);


	        Graphics2D g2d = (Graphics2D) g.create();

	        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.0f));

	        g2d.setColor(getBackground());
	        g2d.fill(getBounds());

	        g2d.dispose();

	    }

	}

}
