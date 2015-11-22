package ca.Ranti;

import javax.swing.JCheckBox;

public class RCheckBox extends JCheckBox {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RCheckBox(String name){
		super(name);
		
		setBackground(Misc.groundColor);
		setForeground(Misc.decoyBackColor);
		setFont(Misc.labelFont);
	}
	
	public RCheckBox(String name, boolean checked){
		super(name,checked);
		
		setBackground(Misc.groundColor);
		setForeground(Misc.decoyBackColor);
		setFont(Misc.labelFont);
	}
}
