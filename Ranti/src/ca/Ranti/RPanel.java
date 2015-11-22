package ca.Ranti;

import java.awt.LayoutManager;

import javax.swing.JPanel;

public class RPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public RPanel(){
		super();
		setBackground(Misc.groundColor);
	}
	
	public RPanel(LayoutManager l){
		super(l);
		setBackground(Misc.groundColor);
	}

}
