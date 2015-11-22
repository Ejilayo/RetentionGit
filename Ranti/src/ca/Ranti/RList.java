package ca.Ranti;

import javax.swing.JList;
import javax.swing.ListModel;

public class RList<E> extends JList<E> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RList(ListModel<E> l){
		super(l);
		
		setBackground(Misc.lighterBlueColor);
		setForeground(Misc.decoyBackColor);
		setFont(Misc.labelFont);
		setSelectionBackground(Misc.groundColor);
	}
}
