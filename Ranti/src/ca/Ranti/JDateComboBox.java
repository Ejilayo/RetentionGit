package ca.Ranti;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.event.EventListenerList;

public class JDateComboBox extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Dated theDate;
	private JComboBox<String> yearBox,monthBox,dayBox;
	
	private String [] months = {"January","February","March","April","May","June",
								"July","August","September","October","November","December"};
	
	String[] choice1Full = {"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15",
							"16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31"};
	
	String[] choice2Sajn = {"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15",
							"16","17","18","19","20","21","22","23","24","25","26","27","28","29","30"};
	
	String[] choice3FebLeap = {"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15",
							   "16","17","18","19","20","21","22","23","24","25","26","27","28","29"};
	
	String[] choice4Feb = {"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15",
						   "16","17","18","19","20","21","22","23","24","25","26","27","28"};
	
	private String [] days;
	private String [] years;
	private int theYear;
	private int theMonth;
	private int theDay;

	private static int startYear = 2014;
	private static int endYear = 2050;
	private int daysCount;

	private EventListenerList listenerList = new EventListenerList();
//	TESTING	
/* 
  public static void main(String [] args){
		JFrame frame = new JFrame("Dates");
		JDateComboBox one = new JDateComboBox();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		one.JDateComboBox();
		frame.getContentPane().add(one);
		frame.pack();
		frame.setVisible(true);
	}
*/	
	
	//----------------------------------------------------------------------
	//	CONSTRUCTORS: date indicated, no date indicated
	//----------------------------------------------------------------------
	public JDateComboBox(Dated date){
		setLayout(new BoxLayout(this,BoxLayout.X_AXIS));
		
		theYear = date.getTheYear();
		theMonth = date.getTheMonth();
		theDay = date.getTheDay();
		daysCount = setupDaysCount(theMonth,theYear);
		
		if(daysCount == 28)
			days = choice4Feb;
		else if(daysCount == 29)
			days = choice3FebLeap;
		else if(daysCount == 30)
			days = choice2Sajn;
		else
			days = choice1Full;
		
		setupYear();
		setupComboBoxes();
		setupColors();
		
		dayBox.setSelectedIndex(theDay-1);
		monthBox.setSelectedIndex(theMonth-1);
		int yearChoice = theYear - startYear;
		yearBox.setSelectedIndex(yearChoice);
		
		add(yearBox);
		add(monthBox);
		add(dayBox);
	}//end of JDateComboBox()
	
	public JDateComboBox(){
		setLayout(new BoxLayout(this,BoxLayout.X_AXIS));
		
		days = choice1Full;
		theYear = startYear;
		theMonth = 1;
		daysCount = 31;
		theDay = 1;
		
		setupYear();
		setupComboBoxes();
		setupColors();
		
		add(yearBox);
		add(monthBox);
		add(dayBox);
		
	}//end of JDateComboBox()
	
	private void setupColors() {
		// TODO Auto-generated method stub
		dayBox.setBackground(Misc.lighterBlueColor);
		dayBox.setForeground(Misc.decoyBackColor);
		dayBox.setFont(Misc.cBoxFont);
		
		monthBox.setBackground(Misc.lighterBlueColor);
		monthBox.setForeground(Misc.decoyBackColor);
		monthBox.setFont(Misc.cBoxFont);
		
		yearBox.setBackground(Misc.lighterBlueColor);
		yearBox.setForeground(Misc.decoyBackColor);
		yearBox.setFont(Misc.cBoxFont);
	}

	//----------------------------------------------------------------------
	//	RETURNS THE DATE FROM THE PANEL
	//----------------------------------------------------------------------
	public Dated getDate(){
		int year = yearBox.getSelectedIndex() + startYear;
		int month = monthBox.getSelectedIndex()+ 1;
		int day = dayBox.getSelectedIndex() + 1;
		
		Dated result = new Dated(year,month,day);
		theDate =  result;
		
		return theDate;
	}//end of getDate()
	
	//----------------------------------------------------------------------
	//	SET UP ALL COMBO BOXES
	//----------------------------------------------------------------------
	private void setupComboBoxes(){
		monthBox = new JComboBox<String>(months);
		monthBox.addActionListener(new YearMonthListener());
		
		yearBox = new JComboBox<String>(years);
		yearBox.addActionListener(new YearMonthListener());
		
		dayBox = new JComboBox<String>(days);
		
		DateChangeChecker dateChangeNotify= new DateChangeChecker(); 
		
		dayBox.addItemListener(dateChangeNotify);
		monthBox.addItemListener(dateChangeNotify);
		yearBox.addItemListener(dateChangeNotify);
		
		setMaximumSize(new Dimension(201,25));

	}//end of ComboBoxes()
	
	//----------------------------------------------------------------------
	//	SETs UP THE YEAR COMBO BOX: start year = 2014; end year = 2050;
	//----------------------------------------------------------------------
	private String[] setupYear(){
		int year = startYear-1;
		int difference = (endYear - startYear) + 1;
		if(difference > 0){
			years = new String[difference];
			
			for(int i = 0; i<difference; ++i){
				year = year + 1;
				years[i] = Integer.toString(year);
			}
		}
		
		else if(difference <= 0){
			years = new String[1];
			years[0] = Integer.toString(startYear);
		}
		
		return years;
	}//end of setupYear()
	
	//----------------------------------------------------------------------
	//	SETUP DAYS based on months and year
	//----------------------------------------------------------------------
	private int setupDaysCount(int dayMonth, int dayYear){
		daysCount = 31;
		theMonth = dayMonth;
		//theYear = dayYear;//this error...whatever it is has been really falling my hand
		int february = 2, april = 4, june = 6, september= 9, november = 11;
		
		boolean leap = (dayYear%4) == 0;
		boolean month30days = (dayMonth == april)||(dayMonth == june)||
							  (dayMonth == september)||(dayMonth == november);
		boolean monthFeb = (dayMonth ==february);
		
		if (monthFeb && leap){
			daysCount = 29;
		}
		else if (monthFeb){
			daysCount = 28;
		}
		else if(month30days){
			daysCount = 30;;
		}
		return daysCount;
	}//end of setupDays()
	
	//----------------------------------------------------------------------
	//	GETTERS AND SETTERS
	//----------------------------------------------------------------------
	public static int getStartYear() {
		return startYear;
	}

	public static void setStartYear(int startYear) {
		JDateComboBox.startYear = startYear;
	}

	public static int getEndYear() {
		return endYear;
	}

	public static void setEndYear(int endYear) {
		JDateComboBox.endYear = endYear;
	}
	//----------------------------------------------------------------------
	//	END OF GETTERS AND SETTERS
	//----------------------------------------------------------------------
	
	
	//I don't know what is going on here
	public void fireDateChangeEvent(DateChangeEvent event){
		Object [] listeners = listenerList.getListenerList();
		
		for(int i = 0; i < listeners.length; i+=2){
			if(listeners[i] == DateChangeListener.class){
				((DateChangeListener)listeners[i+1]).dateChanged(event);
			}
		}
	}
	
	public void addDateChangeListener(DateChangeListener l){
		listenerList.add(DateChangeListener.class, l);
	}
	
	public void removeDateChangeListener(DateChangeListener l){
		listenerList.remove(DateChangeListener.class, l);
	}
	
	public JDateComboBox getAll(){
		return this;
	}


/*************************
	 //----------------------------------------------------------------------
	 //	PRIVATE CLASS LISTENERS
	 //----------------------------------------------------------------------
*************************/
	
	private class DateChangeChecker implements ItemListener{
		public void itemStateChanged(ItemEvent e){
			if(e.getStateChange() == ItemEvent.SELECTED && e.getID() == ItemEvent.ITEM_LAST)
				fireDateChangeEvent(new DateChangeEvent(this,getAll()));
		}
	}
	
	
	private class YearMonthListener implements ActionListener{
		
		public void actionPerformed(ActionEvent ce){
		
		//Set up the number of days in a given year/month
			int month = monthBox.getSelectedIndex() +1;
			int year = yearBox.getSelectedIndex() + startYear;
			int daysNum = setupDaysCount(month,year);
			int last = dayBox.getItemCount();
			
			int difference = daysNum - last;
			
			if (difference>0){
				addDays(difference,last);
			}
			else if (difference < 0){
				if(dayBox.getSelectedIndex() >= daysNum-1)
					dayBox.setSelectedIndex(daysNum-1);	
				//dayBox.revalidate();
				removeDays((-difference),last);
			}
			//fireDateChangeEvent(new DateChangeEvent(this,getAll()));
		}
		
		//add the number of days left in this new selected month
		private void addDays(int count,int lastDay){
			
			for (int i = 0; i<count; i++){
				dayBox.addItem(Integer.toString(lastDay+1));
				lastDay = lastDay + 1;
			}
		}
		
		//remove days for months like February, September etc
		private void removeDays(int count,int lastDay){
			
			for (int i = 0; i<count; i++ ){
				dayBox.removeItemAt(lastDay-1);
				lastDay = lastDay-1;
			}
		}
	}
}
