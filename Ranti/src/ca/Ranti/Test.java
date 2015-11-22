package ca.Ranti;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

//LASTS FOR THE DURATION OF TEST, STORES ALL THE INFO ABOUT USER ETC.
//USERS SHOULD HAVE LIST OF PREVIOUS QUIZES

public class Test {

	private String quizId,subjectId;//,creatorId;
	private User testTaker;
	private TestCriteria testCriteria;
	private Dated dateCreated;
	private Question currentQuestion;
	private double percentage;
	private int numOfQuestions, numOfCorrectAnswers;
	private int currentQNum;
	private int time;//default 10
	private double averageTime;
	private double score;
	private double correctPoint = 3;
	private double incorrectPoint = 0;
	private boolean allowDecoys;
	boolean repeat;
	private ArrayList<Question> questions;
	private ArrayList<String> answers;
	//----------------------------------------------------------------------
	//	EMPTY CONSTRUCTOR
	//----------------------------------------------------------------------
	public Test(Test test){
		this.testTaker = test.getTestTaker();
		this.subjectId = test.getSubject();
		this.quizId = generateNextTestID();
		
		dateCreated = new Dated();
		
		testCriteria = test.getTestCriteria();
		questions = test.getQuestions();
		numOfQuestions = test.getNumOfQuestion();
		
		currentQNum = 0;
		allowDecoys =  testCriteria.isWantObj();
		
		if(allowDecoys){
			time = 20;
		}
		else{
			time = 30;
		}
		
		if(questions.size() != 0){
			currentQuestion = questions.get(currentQNum);
		}
		
		String [] emptyAnswers = new String [numOfQuestions];
		answers = new ArrayList<String>(Arrays.asList(emptyAnswers));
		score = 0;
		percentage = 0;
		numOfCorrectAnswers = 0;
		averageTime = 0.0;
		repeat = true;
		//int previousTestGrade
		//if repeated print answers in previous test vs answers in this test vs correct answer;
		
	}//end of Quiz
	
	//----------------------------------------------------------------------
	//	CONSTRUCTOR
	//----------------------------------------------------------------------
	public Test(TestCriteria criteria, String subjectId ,User creator){
		this.testTaker = creator;
		this.subjectId = subjectId;
		this.quizId = generateNextTestID();
		
		dateCreated = new Dated();
		
		//should create a private class to do this
		testCriteria = criteria;
		questions = Database.getQuestions(testCriteria);
		//System.out.println("In Constructor Num of Questions: " + questions.size());
		Collections.shuffle(questions);
		numOfQuestions = questions.size();
		
		currentQNum = 0;
		allowDecoys =  criteria.isWantObj();
		if(allowDecoys){
			time = 20;
		}
		else{
			time = 30;
		}
		
		
		if(questions.size() != 0){
			currentQuestion = questions.get(currentQNum);
		}
/*		else{
			currentQuestion = new Question();//causes anomalies be careful
		}*/
		
		String [] emptyAnswers = new String [numOfQuestions];
		answers = new ArrayList<String>(Arrays.asList(emptyAnswers));
		score = 0;
		percentage = 0;
		numOfCorrectAnswers = 0;
		averageTime = 0.0;
		repeat = false;
	}//end of Quiz
	
	//----------------------------------------------------------------------
	//	GENERATES NEXT TEST ID: FORMAT->Subject_dateCreated_hrMinss
	//----------------------------------------------------------------------
	public String generateNextTestID(){
		SimpleDateFormat dateFormat = new SimpleDateFormat("YYYYMMdd_HHmmSS");
		Date today = new Date();//disowning my Dated class
		String datePart = dateFormat.format(today);
		
		String result = subjectId + "_" + datePart;
		
		return result;
	}//end of generateNextTestID()

	//----------------------------------------------------------------------
	//	QUIZ-ID
	//----------------------------------------------------------------------
	public String getQuizId() {
		return quizId;
	}//end of getQuizId

	public void setQuizId(String quizId) {
		this.quizId = quizId;
	}//end of setQuizId

	//----------------------------------------------------------------------
	//	GET AND SET SUBJECT
	//----------------------------------------------------------------------
	public String getSubject() {
		return subjectId;
	}//end of getSubject()

	public void setSubject(String subject) {
		this.subjectId = subject;
	}//end of setSubject

	//----------------------------------------------------------------------
	//	GET AND SET DATE TAKEN
	//----------------------------------------------------------------------
	public Dated getDateCreated() {
		return dateCreated;
	}//end of getDateTaken

	public void setDateCreated(Dated dateTaken) {
		this.dateCreated = dateTaken;
	}//end of setDateTaken

	//----------------------------------------------------------------------
	//	GET AND SET NUMBER OF QUESTIONS
	//----------------------------------------------------------------------
	public int getNumOfQuestion() {
		return numOfQuestions;
	}//end of getNumOfQuestion

/*	public void setNumOfQuestion(int numOfQuestion) {
		this.numOfQuestions = numOfQuestion;
	}//end of setNumofQuestion
*/
	//----------------------------------------------------------------------
	//	GET, SET AND ADD QUESTIONS
	//----------------------------------------------------------------------
	public ArrayList<Question> getQuestions() {
		return questions;
	}//end of getQuestions

	public void addQuestion(Question question){
		questions.add(question);
		numOfQuestions++;
	}//end of addQuestion
	
	public void setQuestions(ArrayList<Question> questions) {
		this.questions = questions;
	}//end of setQuestions

	//----------------------------------------------------------------------
	//	GET AND SET CREATOR ID
	//----------------------------------------------------------------------
	public User getTestTaker() {
		return testTaker;
	}//end of getCreatorId

	public void setTestTaker(User testTaker) {
		this.testTaker = testTaker;
	}//end of setCreatorId
	
	//----------------------------------------------------------------------
	//	NEXT QUESTION
	//----------------------------------------------------------------------
	public Question nextQuestion(){
		currentQNum++;
		if(currentQNum<numOfQuestions){
			currentQuestion = questions.get(currentQNum);
			return currentQuestion;
		}
		else{
			//return default
			return null;//which may cause errors needs to come up with better soln
		}
	}//end of nextQuestion
	
	//----------------------------------------------------------------------
	//	NEXT QUESTION
	//----------------------------------------------------------------------
	public void setFinalQuestionList(int countWanted){
		//System.out.println("Question Count:" + questions.size());
		List<Question> hold = questions.subList(0,countWanted);
		questions = new ArrayList<Question>(hold);
		numOfQuestions = questions.size();
	}//end of finalQuestionList(int countWanted)
	
	//----------------------------------------------------------------------
	//	BOOLEAN LAST QUESTION
	//----------------------------------------------------------------------
	public boolean isLast(){
		return(currentQNum == numOfQuestions -1);
	}//end of isLast

	//----------------------------------------------------------------------
	//	CURRENT QUESTION
	//----------------------------------------------------------------------
	public Question getCurrentQuestion() {
		return currentQuestion;
	}//end of getCurrentQuestion
	
	//----------------------------------------------------------------------
	//	CALCULATE SCORES
	//	Stores the answers given by user and also calculates the score of
	//	the user so far
	//----------------------------------------------------------------------
	public boolean calculateScore(String givenAnswer, int timeLeft){
		boolean rubbish = givenAnswer.equals("SuperCalifragalisticEspialidocious");
		boolean rightAnswer = (currentQuestion.getAnswer().toLowerCase().trim().replace(" ","").equals(givenAnswer.toLowerCase().trim().replace(" ","")));
		if(rightAnswer && !rubbish){
			score = score + (timeLeft*correctPoint);
			answers.set(currentQNum,(givenAnswer + "<---CORRECT!!"));
			numOfCorrectAnswers++;
			averageTime = averageTime + ((timeLeft + 0.0)/(numOfQuestions + 0.0));
			//System.out.println(score);
		}
		else if (!rightAnswer && !rubbish){
			score = score + (timeLeft*incorrectPoint);
			answers.set(currentQNum,(givenAnswer + "-->INCORRECT!!! --->" + currentQuestion.getAnswer()));
			//System.out.println(score);
			averageTime = averageTime + ((timeLeft + 0.0)/(numOfQuestions + 0.0));
		}
		
		return rightAnswer;
		
	}//end of calculateScore
	
	//----------------------------------------------------------------------
	//	ADD ANSWER: for when a question has run out of time and we need to
	//	store the answer.
	//----------------------------------------------------------------------
	public void addAnswer(String answer) {
		answers.set(currentQNum, (answer + "->INCORRECT!!! -->" + currentQuestion.getAnswer()));
	}//end of addAnswer
	

	//----------------------------------------------------------------------
	//	GET DECOYS AND ANSWER: returns decoys shuffled so that user may not 
	// 	notice patterns in the way the decoys are arranged
	//----------------------------------------------------------------------
	public ArrayList<String> getDecoyAnswer() {
		ArrayList<String> decoyOrAnswer =  new ArrayList<String>();
		
		if (currentQuestion.usesDecoys()){
			decoyOrAnswer = currentQuestion.getDecoys();
		}
		if(!repeat){
			decoyOrAnswer.add(currentQuestion.getAnswer());
		}
		
		Collections.shuffle(decoyOrAnswer);
		
		return decoyOrAnswer;
	}//end of getDecoyAnswer
	
/*	public void setCurrentQuestion(Question currentQuestion) {
		this.currentQuestion = currentQuestion;
	}*/

	//----------------------------------------------------------------------
	//	GET AND SET SCORE://probably never use set
	//----------------------------------------------------------------------
	public double getScore() {
		return score;
	}//end of getScore

/*	public void setScore(int score) {
		this.score = score;
	}*/

	//----------------------------------------------------------------------
	//	PERCENTAGE = Denominator => Num Of Questions * 10(seconds)
	//----------------------------------------------------------------------
	public double getPercentage() {
		double denominator = getNumOfQuestion()*(correctPoint * getTime());//10 being time for each question
		percentage = (score/denominator) * 100;
		percentage = (double)((int)(percentage * 100));
		percentage = percentage/100;
		return percentage;
	}//end of getPercentage

	public void setPercentage(double percentage) {
		this.percentage = percentage;
	}//end of setPercentage

	//----------------------------------------------------------------------
	//	GET AND SET TIME FOR EACH QUESTION
	//----------------------------------------------------------------------
	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	//----------------------------------------------------------------------
	//	GET AND SET DIFFICULTY WANTED FOR THIS SESSION
	//	harder difficulty = means no decoys just textfields
	//----------------------------------------------------------------------
	public boolean isAllowDecoys() {
		return allowDecoys;
	}//end isEasyDifficulty 

	public void setAllowDecoys(boolean decoysYeaOrNae) {
		this.allowDecoys = decoysYeaOrNae;
	}//end of setEasyDifficulty()

	
	public TestCriteria getTestCriteria() {
		return testCriteria;
	}

	public void setTestCriteria(TestCriteria testCriteria) {
		this.testCriteria = testCriteria;
	}

	public double getCorrectPoint() {
		return correctPoint;
	}

	public void setCorrectPoint(double correctPoint) {
		this.correctPoint = correctPoint;
	}

	public double getIncorrectPoint() {
		return incorrectPoint;
	}

	public void setIncorrectPoint(double incorrectPoint) {
		this.incorrectPoint = incorrectPoint;
	}

	//----------------------------------------------------------------------
	//	TO STRING
	//----------------------------------------------------------------------
	public String toString(){
		DecimalFormat timeForm = new DecimalFormat("0.#");
		String result = "";
		
		result = "[QUIZID: " + quizId + "\n"
				+ "SUBJECTID: " + subjectId + "\n"
				+ "TAKEN BY: " + testTaker +"\n"
				+ "ON: "+dateCreated +"\n" 
				+ "QUESTIONS: ";
		
		for (int i =0; i<questions.size();i++){
			result = result + "\n"+ "["+ (i+1) + "]: ";
			result = result + questions.get(i) + "\n";
			result = result + "GIVEN ANSWER: " + answers.get(i);
		}
		//result = result + " QUESTIONS: " + questions.toString() + "\n";
		//result = result + " ANSWERS: " + answers.toString() + "\n";
		result = result + "\nSCORING: " + getScore() + "/"+ getNumOfQuestion()*(correctPoint * getTime()) +" i.e ("+ getPercentage() +"%) \n"
						+ "AVERAGE ANSWER TIME:" + timeForm.format(averageTime) + "s/" + getTime() + "s\n"
				 		+ " getting "+ numOfCorrectAnswers +" of "+ getNumOfQuestion() +" correct]";
				 		
		
		return result;
	}

	//-----------------------------------DATABASE------------------------------------------------
	//----------------------------------------------------------------------
	//	GET QUESTIONS BASED ON ALREADY SET CRITERIA
	//----------------------------------------------------------------------
	public void getQuestionsBasedOnCriteriaFromDB(){
		questions = Database.getQuestions(testCriteria);
		//System.out.println(testQuestions);
		//return testQuestions;
	}//end of getQuestionsBasedOnCriteriaFromDB()
}
