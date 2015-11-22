package ca.Ranti;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class RLabel extends JLabel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public RLabel(String text){
		super(text);
		
		setBackground(Misc.groundColor);
		setForeground(Misc.decoyBackColor);
		setFont(Misc.labelFont);
	}
	
	public RLabel(String text,ImageIcon image, int align){
		super(text,image,align);
		
		setBackground(Misc.groundColor);
		setForeground(Misc.decoyBackColor);
		setFont(Misc.labelFont);
	}
	
}
