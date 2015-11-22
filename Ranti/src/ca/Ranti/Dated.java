package ca.Ranti;

import java.text.SimpleDateFormat;
import java.util.Date;

//----------------------------------------------------------------------
//	Date arithmetics not yet implemented, i.e adding,subtracting etc not
//	yet possible...may not be needed for this project
//----------------------------------------------------------------------	

public class Dated implements Comparable<Dated>{
	
	private int theYear;
	private int theMonth;
	private int theDay;
	
	private static int FINAL_YEAR = 2050;
	private static int INITIAL_YEAR = 2014;


	//----------------------------------------------------------------------
	//	CONSTRUCTORS:no date indicated
	//----------------------------------------------------------------------	
	public Dated(){
		theYear = 2014;
		theMonth = 6;
		theDay = 20;
		
		Date todaysDate = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = dateFormat.format(todaysDate);
		String [] dateStringArray = dateString.split("-");
		
		setTheYear(Integer.parseInt(dateStringArray[0]));
		setTheMonth(Integer.parseInt(dateStringArray[1]));
		setTheDay(Integer.parseInt(dateStringArray[2]));

	}//end of Dated()
	
	//----------------------------------------------------------------------
	//	CONSTRUCTORS: date indicated
	//----------------------------------------------------------------------
	public Dated(int year, int month, int day){
		
		theYear = correctYear(year);
		theMonth = correctMonth(month);
		theDay = correctDay(day);
	}// end of Dated
	
	//----------------------------------------------------------------------
	//	CONSTRUCTORS: String in format yyyy-mm-dd
	//----------------------------------------------------------------------
	public Dated(String date){
		String [] dateArray = date.split("-");
		
		setTheYear(Integer.parseInt(dateArray[0]));
		setTheMonth(Integer.parseInt(dateArray[1]));
		setTheDay(Integer.parseInt(dateArray[2]));
	}// end of Dated

//--------------------------------------------------------------------------
//	GETTERS AND SETTERS
//--------------------------------------------------------------------------	
	public int getTheYear() {
		return theYear;
	}

	public void setTheYear(int theYear) {
		this.theYear = correctYear(theYear);
	}

	public int getTheMonth() {
		return theMonth;
	}

	public void setTheMonth(int theMonth) {
		this.theMonth = correctMonth(theMonth);
	}

	public int getTheDay() {
		return theDay;
	}

	public void setTheDay(int theDay) {
		this.theDay = correctDay(theDay);
	}

	
	public static int getFINAL_YEAR() {
		return FINAL_YEAR;
	}

	public static void setFINAL_YEAR(int fINAL_YEAR) {
		FINAL_YEAR = fINAL_YEAR;
	}

	public static int getINITIAL_YEAR() {
		return INITIAL_YEAR;
	}

	public static void setINITIAL_YEAR(int iNITIAL_YEAR) {
		INITIAL_YEAR = iNITIAL_YEAR;
	}
//--------------------------------------------------------------------------
//	END OF GETTERS AND SETTERS
//--------------------------------------------------------------------------	
	
	//----------------------------------------------------------------------
	//	COMPARE TO
	//----------------------------------------------------------------------	
	public int compareTo(Dated day){
		int result = 0;
		int yearResult = theYear - day.getTheYear();
		int monthResult = theMonth - day.getTheMonth();
		int dayResult = theDay - day.getTheDay();
		//result = theYear;
		if (yearResult != 0){
			result = yearResult;
		}
		else if (monthResult != 0){
			result = monthResult;
		}
		else if (dayResult != 0){
			result = dayResult;
		}
		
		return result;
	}//end of CompareTo
	

	
	//----------------------------------------------------------------------
	//	TODAYS DATE
	//----------------------------------------------------------------------
	public static Dated getTodaysDate(){
		Dated today = new Dated();		
		return today;
	}//end of getTodaysDate
	
	//----------------------------------------------------------------------
	//	TO STRING
	//----------------------------------------------------------------------
	public String toString(){
		String [] allMonths = {"January","February","March","April","May","June",
				"July","August","September","October","November","December"};
		
		String year = Integer.toString(theYear);
		String month = allMonths[theMonth-1];
		//String day = Integer.toString(theDay);
		
		String result = daySuffix(theDay) +" "+ month+", " + year;
		
		return result;
	}//end of toString()
	
	//----------------------------------------------------------------------
	//	TO SQL STRING: needs more "robustness" ... to juvenile
	//----------------------------------------------------------------------
	public String toSqlString(){
		//Dated today = getTodaysDate(); -- this was foolish
		String year  = Integer.toString(theYear);
		String month = "";
		if(theMonth < 10){
			month = "0" + Integer.toString(theMonth);
		}
		else{
			month = Integer.toString(theMonth);
		}
		
		String day = "";
		
		if(theDay < 10){
			day = "0" + theDay;
		}
		else{
			day = Integer.toString(theDay);
		}
		
		String date = year+"-"+month+"-"+day;
		return date;
	}
	
	//------------------------------------PRIVATE METHODS---------------------------------------
	//----------------------------------------------------------------------
	//	CORRECT YEAR: years between 2014 and 2050 ; Error checking
	//----------------------------------------------------------------------	
	private int correctYear(int value){
		if (value>FINAL_YEAR){
			return FINAL_YEAR;
		}
		else if (value<INITIAL_YEAR){
			return INITIAL_YEAR;
		}
		else
			return value;
	}//end of correctYear
	
	//----------------------------------------------------------------------
	//	CORRECT MONTH: between January and December; Error Checking
	//----------------------------------------------------------------------	
	private int correctMonth(int value){
		int month = value;
		if(value<1 || value>12){
			month = 1;
		}
		return month;
	}//end of correctMonth
	
	//----------------------------------------------------------------------
	//	CORRECT DAY: between 1 & 28,29,30,31 depending on the month & year
	//----------------------------------------------------------------------	
	private int correctDay(int value){
		int days = 31;
		int febDays = 28;
		int february = 2, april = 4, june = 6, september= 9, november = 11;
		boolean leap = (theYear%4) == 0;
		
		if(theMonth == april ||theMonth ==  june||theMonth == september||theMonth == november){
			if (value<1||value>30){
				value = 30;
			}
		}
		else if(theMonth == february){
			if(leap){
				if(value<1||value>29){
					value = febDays+1;
				}
			}
			else if(value<1||value>28){
				value = febDays;
			}
		}
		else if (value<1||value>31){
			value = days;
		}
		return value;
	}//end of correctDay
	
	//----------------------------------------------------------------------
	//	RETURNS CORRECT SUFFIX FOR DAYS...i.e FIRST(st) SECOND(nd) THIRD(rd)
	//	FOURTH(th)
	//----------------------------------------------------------------------
	private String daySuffix(int aDay){
		String correctDay = "";
		
		String st = "st";
		String nd = "nd";
		String rd = "rd";
		String th = "th";
		
		if(aDay==11||aDay==12||aDay==13){
			correctDay = Integer.toString(aDay)+th;
		}
		else{
			int mod = aDay % 10;
			
			switch(mod){
			case 0:
				correctDay = Integer.toString(aDay)+th;
				break;
			case 1:
				correctDay = Integer.toString(aDay)+st;
				break;
			case 2:
				correctDay = Integer.toString(aDay)+nd;
				break;
				
			case 3:
				correctDay = Integer.toString(aDay)+rd;
				break;
			
			default:
				correctDay = Integer.toString(aDay)+th;
				break;
			}
		}
		
		return correctDay;
	}
}
