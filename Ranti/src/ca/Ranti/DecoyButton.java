package ca.Ranti;

import javax.swing.JButton;

public class DecoyButton extends JButton {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public DecoyButton(String text){
		super(text);
		
		setFocusPainted(false);
		setBackground(Misc.decoyBackColor);
		setFont(Misc.decoyButtonFont);
		setForeground(Misc.lighterBlueColor);
	}
}
