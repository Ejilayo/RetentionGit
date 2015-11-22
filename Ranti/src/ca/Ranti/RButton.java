package ca.Ranti;

import javax.swing.JButton;

public class RButton extends JButton {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RButton(String text){
		super(text);
		
		setFocusPainted(false);
		setBackground(Misc.decoyBackColor);
		setForeground(Misc.lighterBlueColor);
		setFont(Misc.buttonFont);
	}
}
