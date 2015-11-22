package ca.Ranti;

import java.util.ArrayList;
import java.util.Arrays;


public class Subject {
	
	String id, desc;
	User creator;
	Dated dateCreated;
	ArrayList<String> potList;
	ArrayList<Question> questionList;
	private static int numOfQuestions;
	
/*	public static void main (String [] args){
		System.out.println(getQuestionNum("CMPT365_10"));
		System.out.println(getQuestionNum("CMPT365_200"));
		System.out.println(getQuestionNum("CMPT365_1"));
	}*/
	
	//----------------------------------------------------------------------
	//	CONSTRUCTOR:
	//----------------------------------------------------------------------
	public Subject(){
		id = "";
		desc = "";
		potList = new ArrayList<String>();
		questionList = new ArrayList<Question>();
		dateCreated = new Dated();
	}//end of Subject
	
	//----------------------------------------------------------------------
	//	CONSTRUCTOR:
	//----------------------------------------------------------------------
	public Subject(String id, String desc){
		this.id = id;
		this.desc = desc;
		potList = new ArrayList<String>();
		questionList = new ArrayList<Question>();
		dateCreated = new Dated();
	}//end of Subject(id,desc)
	
	//----------------------------------------------------------------------
	//	SET POT AND GET POT
	//----------------------------------------------------------------------
	public void setPotList(ArrayList<String> pot){
		potList =pot;
	}//end of addToPot()
	
	public ArrayList<String> getPotList(){
		return potList;
	}//end of addToPot()
	
	//----------------------------------------------------------------------
	//	ADD A TERM TO THE POT :
	//----------------------------------------------------------------------
	public void addToPotList(String term){
		potList.add(term);
	}//end of addToPot()
	
	//----------------------------------------------------------------------
	//	DELETE POT ENTRY:
	//----------------------------------------------------------------------
	public void deletePotListEntry(String term){
		
	}//end of deletePotEntry
	
	//----------------------------------------------------------------------
	//	ADD QUESTION:
	//----------------------------------------------------------------------
	public void addQuestion(Question newQuestion){
		//numOfQuestions++;
		questionList.add(newQuestion);
	}//end of addQuestion
	
	//----------------------------------------------------------------------
	//	DELETE QUESTION:
	//----------------------------------------------------------------------
	public void deleteQuestion(int questId){
		//numOfQuestions--;
	}//end of deleteQuestion
	
	//----------------------------------------------------------------------
	//	GET AND SET QUESTIONLIST
	//----------------------------------------------------------------------
	public ArrayList<Question> getQuestionList() {
		return questionList;
	}//end of getQuestionList

	public void setQuestionList(ArrayList<Question> questionList) {
		this.questionList = questionList;
	}//end of setQuestionList
	
	//----------------------------------------------------------------------
	//	SET AND GET DATE CREATED
	//----------------------------------------------------------------------
	public void setDateCreated(Dated date){
		dateCreated = date;
	}//end of setDateCreated
	

	public Dated getDateCreated(){
		return dateCreated;
	}//end of getDatedCreated
	
	//----------------------------------------------------------------------
	//	GET AND SET SUBJECT ID
	//----------------------------------------------------------------------
	public String getId() {
		return id;
	}//end of getId()

	public void setId(String id) {
		this.id = id;
	}//end of setId()

	//----------------------------------------------------------------------
	//	GET AND SET DESCRIPTION
	//----------------------------------------------------------------------
	public String getDesc() {
		return desc;
	}//end of getDesc

	public void setDesc(String desc) {
		this.desc = desc;
	}//end of setDesc
	
	//----------------------------------------------------------------------
	//	GET AND SET CREATOR
	//----------------------------------------------------------------------
	public User getCreator() {
		return creator;
	}//end of getCreator

	public void setCreator(User creator) {
		this.creator = creator;
	}//end of setCreator

	//----------------------------------------------------------------------
	//	GET NUMBER OF QUESTIONS
	//----------------------------------------------------------------------
	public int getNumOfQuestions() {
		numOfQuestions = questionList.size();
		return numOfQuestions;
	}//end of getNumOfQuestions
	
	//----------------------------------------------------------------------
	//	GENERATE NEXT QUESTION ID: uses getQuestionNum
	//----------------------------------------------------------------------
	public String generateNextQuestionID() {
		

		
		//If no questions
		if(questionList.size() == 0){
			return (id + "_1");
		}
		//else if questions exist
		String quesID = id+"_";
		int [] ques = sortQuestions();
		int size = ques.length;
		int num =0;
		int i = 1;
		
		//look for id gaps e.g
		//if size=4 but a ..._1,..._2,..._3,...5 list
		//it should return a number from 1 to 4
		while (i<=size){
			num = ques[i-1];
			if(i != num){
				quesID = quesID + Integer.toString(i);
				return quesID;
				//i = size++;
			}
			i++;
		}
		
		quesID = quesID + Integer.toString(size+1);
		
		return quesID;
	}//end of getNumOfQuestions
	
	//---------------------------------------------------------------------
	//Returns the value of the number at the end of a subjectID
	//getQuestionNum(CMPT365_1) ---> 1
	//getQuestionNum(CMPT365_6) ---> 6
	//---------------------------------------------------------------------
	private static int getQuestionNum(String id){
		int lastIndexOf = id.lastIndexOf("_");
		//String [] value = id.split("_");
		int length = id.length();
		//int lastIndex = value.length - 1;
		//String str = value[lastIndex];
		String str = id.substring(lastIndexOf+1,length);
		int result = Integer.parseInt(str);
		return result;
	}//end of getQuestionNum()
	
	//Sort the questions using quicksort
	public int[] sortQuestions(){
		int [] questionIds = new int[questionList.size()]; 
		
		int i = 0;
		for(Question eachQuest: questionList){
			questionIds[i] = getQuestionNum(eachQuest.getIdNum());
			i++;
		}
		
		Arrays.sort(questionIds);
		
		return questionIds;
	}//end of sortQuestions
	
	//----------------------------------------------------------------------
	//	TO STRING
	//----------------------------------------------------------------------
	public String toString(){
		String result = id;
		return result;
	}//end of toString()
	
	//----------------------------------------------------------------------
	//	TO STRING2
	//----------------------------------------------------------------------
	public String toString2(){
		String result = "ID: " + id + " DESCRIPTION: "+ desc 
						+ "\nCREATOR: " + creator.getUserName() + " ON: " + dateCreated;
		return result;
	}//end of toString()
	
	//-------------------------------DATABASE SECTION-----------------------------------
	
	//----------------------------------------------------------------------
	//	GET QUESTION LIST FROM DB: 
	//----------------------------------------------------------------------
	public ArrayList<Question> getQuestionListFromDB() {
		questionList = Database.getAllQuestionBasics(id);
		return questionList;
	}//end of getQuestionFromDB
	
	//----------------------------------------------------------------------
	//	GET LIST OF SUBJECT TERMS(JARGONS) FROM THE DATABASE
	//----------------------------------------------------------------------
	public ArrayList<String> getPotListFromDB(){
		potList = Database.getSubjectPotList(id);
		return potList;
	}//end of getPotListFromDB
	
/*	//----------------------------------------------------------------------
	//	GET HIGH AND LOW DATES: not so important
	// 	returns the date of the most recently and least recently created 
	//	question; 
	//----------------------------------------------------------------------
	public String[] getQuestionHighLowDateFromDB() {
		
	}//end of getQuestionFromDB
*/
}
