package ca.Ranti;

import java.util.EventObject;

public class DateChangeEvent extends EventObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Dated boxDate;
	
	public DateChangeEvent(Object source, JDateComboBox dateBox){
		super(source);
		
		this.boxDate = dateBox.getDate();
	}
	
	public Dated getBoxDate(){
		return boxDate;
	}
}
