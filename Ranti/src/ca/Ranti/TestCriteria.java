package ca.Ranti;

import java.util.ArrayList;

public class TestCriteria {

 //Check Database UserStats to drop subjectID as a key
   
	private String subjectId,dateCreated,creatorId;
	private ArrayList<String> testDates,testAuthors;
	private int []  testOptionTypes;
	private int numOfQuestionWanted;
	boolean wantObj;
	//private ArrayList<Question> testQuestions;
	
	//----------------------------------------------------------------------
	//	CONSTRUCTOR
	//----------------------------------------------------------------------
	public TestCriteria(){
		
		testAuthors = new ArrayList<String>();
		testDates = new ArrayList<String>();
		testOptionTypes = new int[3];
		
		//testQuestions = new ArrayList<Question>();
	}//end of public TestTaking
	
	//----------------------------------------------------------------------
	//	CONSTRUCTOR
	//----------------------------------------------------------------------
	public TestCriteria(String subjectId){
		//testID = generateNextTestID();
		this.subjectId = subjectId;
		//this.creatorId = creatorId;
		dateCreated = new Dated().toSqlString();
		
		testAuthors = new ArrayList<String>();
		testDates = new ArrayList<String>();
		testOptionTypes = new int[3];
		
		wantObj = true;
		
		//testQuestions = new ArrayList<Question>();
	}//end of public TestTaking(subjectId, creatorId);

//------------------------------------STUFF-------------------------------------
	
	//----------------------------------------------------------------------
	//	ADD TESTOPTION TYPE:
	//		A visual of the testOptionTypeArray
	//			    0		  	   1		    2
	//		  |-------------|-------------|-----------|
	//	array[|Definition(1)|True False(2)|Fill Gap(3)|]
	//		  |-------------|-------------|-----------|
	//
	//	a test option is added by setting its array bloc greater than 0
	//----------------------------------------------------------------------
	public void addTestOptionType(int testType){
		boolean outOfTestTypeRange = (testType < 1 || testType > 3);
		int typeArrayLocation = testType -1;
		if(!outOfTestTypeRange){
			testOptionTypes[typeArrayLocation] = testType;
		}
	}//end of addTestOptionType
	
	//----------------------------------------------------------------------
	//	REMOVE TESTOPTION TYPE: set array block back to zero
	//----------------------------------------------------------------------
	public void removeTestOptionType(int testType){
		boolean outOfTestTypeRange = (testType < 1 || testType > 3);
		int typeArrayLocation = testType -1;
		if(!outOfTestTypeRange){
			testOptionTypes[typeArrayLocation] = 0;
		}
	}//end of removeTestOptionTypes
	
	//----------------------------------------------------------------------
	//	GET AND SET OPTION TYPES ARRAY: I strongly advice myself against 
	//	using this
	//----------------------------------------------------------------------
	public int[] getTestOptionTypes() {
		return testOptionTypes;
	}//end of getTestOptionTypes

	public void setTestOptionTypes(int[] testOptionTypes) {
		this.testOptionTypes = testOptionTypes;
	}//end of setTestOptionTypes
	
	//----------------------------------------------------------------------
	//	SET TEST POOL DATES(AFTER THIS DATE, BUT NOT BEFORE THIS DATE)
	//  SQLish TRANSLATION => get questions with Dates => firstDate & <=lastDate
	//----------------------------------------------------------------------
	public void setBetweenDates(String firstDate, String lastDate){
		testDates.add(firstDate);
		testDates.add(lastDate);
	}//end of setBetweenDates
	
	//----------------------------------------------------------------------
	//	ADD AUTHOURID
	//	(I should consider removeAuthor)
	//----------------------------------------------------------------------
	public void addAuthor(String authorId){
		boolean alreadyAdded = false;
		if (testAuthors.size() == 0){
			testAuthors.add(authorId);
		}
		else{
			for(String val: testAuthors){
				if(authorId.equals(val)){
					alreadyAdded = true;
				}
			}
			if(!alreadyAdded){
				testAuthors.add(authorId);
			}
		}
	}//end of addAuthor

//------------------GETTERS AND SETTERS(FOR ALREADY SELECTED TEST CRITERIA ATTRIBUTES)-------------
	//----------------------------------------------------------------------
	//	GET AND SET TESTDATES: i.e the between dates
	//----------------------------------------------------------------------
	public ArrayList<String> getTestDates() {
		return testDates;
	}//end of getTestDates()

	public void setTestDates(ArrayList<String> testDates) {
		this.testDates = testDates;
	}//end of setTestDates

	//----------------------------------------------------------------------
	//	GET AND SET TESTAUTHORS i.e the list of question creators
	//----------------------------------------------------------------------
	public ArrayList<String> getTestAuthors() {
		return testAuthors;
	}//end of getTestAuthors

	public void setTestAuthors(ArrayList<String> testAuthors) {
		this.testAuthors = testAuthors;
	}//end of setTestAuthors
			
//--------------------GETTERS AND SETTERS(ATTRIBUTES OF THIS OBJECT'S CREATION)-------------------	
	//----------------------------------------------------------------------
	//	GET AND SET SUBJECT ID
	//----------------------------------------------------------------------
	public String getSubjectId() {
		return subjectId;
	}//end of getSubjectId

	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}//end of setSubjectId

	//----------------------------------------------------------------------
	//	GET AND SET IF YOU WANT OBJ OR TYPE-IN QUESTIONS ASKING STYLE
	//----------------------------------------------------------------------
	public boolean isWantObj() {
		return wantObj;
	}//end of isWantEasy

	public void setWantObj(boolean wantEasy) {
		this.wantObj = wantEasy;
	}//end of setWantEasy
	
	//----------------------------------------------------------------------
	//	GET AND SET The date this criteria was listed...intend to remove
	//----------------------------------------------------------------------
	public String getDateCreated() {
		return dateCreated;
	}//end of getDateCreated

	public void setDateCreated(String dateCreated) {
		this.dateCreated = dateCreated;
	}//end of setDateCreated

	//----------------------------------------------------------------------
	//	GET AND SET CREATOR ID: Kinda useless... fulfills some righteousness
	//----------------------------------------------------------------------
	public String getCreatorId() {
		return creatorId;
	}//end of getCreatorId

	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}//end of setCreatorId

	//----------------------------------------------------------------------
	//	GET AND SET the amount of questions user is willing to take
	//----------------------------------------------------------------------
	public int getNumOfQuestionWanted() {
		return numOfQuestionWanted;
	}//end of getNumOfQuestionWanted()

	public void setNumOfQuestionWanted(int numOfQuestionWanted) {
		this.numOfQuestionWanted = numOfQuestionWanted;
	}//end of setNumOfQuestionWanted(int)
}
