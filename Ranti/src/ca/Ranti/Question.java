package ca.Ranti;

import java.util.ArrayList;

public class Question implements Comparable<Question>{

	private String idNum;
	private int type;
	private String wording, answer;
	private User author; 
	private Dated dateCreated,dateModified;
	private ArrayList<String> decoys,imageLinks;//anwers;
	private String subjectID;
	//private boolean torfAnswer;
	private boolean useDecoys = false,
					usePot = false,
					usesImages = false;
	private double fastestAnswerTime;
	
	private static final int DEFINITION_QUESTION = 1; //Definition question(Default)
	private static final int TRUEFALSE_QUESTION = 2; //True or False question
	private static final int FILLGAP_QUESTION =3;//Fill in the gap question

	
	
	//---------------------------------------------------------------
	// CONSTUCTOR
	//---------------------------------------------------------------
	public Question(){
		idNum = "0";
		type = 1;
		
		wording = "NULL";
		answer = "NULL";
		//author = "Default Author";
		
		dateCreated = Dated.getTodaysDate();
		dateModified = Dated.getTodaysDate();
		
		decoys = new ArrayList<String>();
		imageLinks = new ArrayList<String>();
		
		//torfAnswer = false; //(the value is not applicable for type 1 & 3)
		usePot = true;
		useDecoys = true;
		usesImages = false;
		
	}//end of Question
	
	//---------------------------------------------------------------
	// CONSTUCTOR TYPE 1,2 QUESTION
	//---------------------------------------------------------------
	public Question(String idNum, String wording, String answer){
		this.idNum = idNum;
		
		this.wording = wording;
		this.answer = answer;

		//dateCreated = Dated.getTodaysDate();
		//dateModified = Dated.getTodaysDate();
		
		decoys = new ArrayList<String>();
		imageLinks = new ArrayList<String>();		
	}//end of Question
	
	//---------------------------------------------------------------
	// CONSTUCTOR TYPE 1,2 QUESTION without answer
	//---------------------------------------------------------------
	public Question(String idNum, String wording){
		this.idNum = idNum;
		
		this.wording = wording;
		//dateCreated = Dated.getTodaysDate();
		//dateModified = Dated.getTodaysDate();
		
		decoys = new ArrayList<String>();
		imageLinks = new ArrayList<String>();
	}//end of Question
	
	//---------------------------------------------------------------
	// CONSTUCTOR TYPE 2 QUESTION
	//---------------------------------------------------------------
	/*public Question(int idNum, String wording, boolean answer){
		this.idNum = idNum;
		this.type = TYPE_TRUEFALSE;
		
		this.wording = wording;
		this.answer = "N/A";
		
		//author = "Unknown";//user should set author when needed
		
		dateCreated = Dated.getTodaysDate();
		dateModified = Dated.getTodaysDate();
		
		decoys = new ArrayList<String>();
		imageLinks = new ArrayList<String>();
		
		torfAnswer = answer;
		usePot = true;
		useDecoys = true;
		usesImages = false;
	}//end of Question
	*/
	//---------------------------------------------------------------
	// CONSTUCTOR TYPE 3 COMING SOON
	//---------------------------------------------------------------
	//public Question(int idNum, String wording)
	
/*
 * //--------------------------------------------------------------- 
 * //GETTERS AND SETTERS
 * //---------------------------------------------------------------
 */
	//---------------------------------------------------------------
	//	IDENTIFICATION - Question Identification Number
	//---------------------------------------------------------------
	public String getIdNum() {
		return idNum;
	}// end of getIdNum()

	public void setIdNum(String idNum) {
		this.idNum = idNum;
	}// end of setIdNum
	
	//---------------------------------------------------------------
	// QUESTION TYPE can be TYPE_DEF - Definition Questions, 
	// TYPE_TORF - True or False or TYPE_FILL_GAP - Fill in the Gap
	//---------------------------------------------------------------
	public int getType() {
		return type;
	}//end of getType()

	public void setType(int type) {
		this.type = type;
	}//end of setType

	//---------------------------------------------------------------
	// Wording - contains the question being asked
	//---------------------------------------------------------------
	public String getWording() {
		return wording;
	}//end of getWording()

	public void setWording(String wording) {
		this.wording = wording;
	}//end of setWording

	//---------------------------------------------------------------
	// Answers - answer to question
	//---------------------------------------------------------------
	public String getAnswer() {
		return answer;
	}//end of getAnswer()

	public void setAnswer(String answer) {
		this.answer = answer;
	}//end of setAnswer

/*	//---------------------------------------------------------------
	//	TRUE OR FALSE ANSWER 
	//---------------------------------------------------------------
	public boolean getTorfAnswer() {
		return torfAnswer;
	}//end of isTorfAnswer

	public void setTorfAnswer(boolean torfAnswer) {
		this.torfAnswer = torfAnswer;
	}//end of setTorFAnswer
*/
	//---------------------------------------------------------------
	// Authors - "I should create a user class to store preferrences" 
	//---------------------------------------------------------------
	public User getAuthor() {
		return author;
	}//end of getAuthor()

	public void setAuthor(User author) {
		this.author = author;
	}//end of setAuthor

	//---------------------------------------------------------------
	//	Dated Class only dates between 2014 and 2050
	//---------------------------------------------------------------
	public Dated getDateCreated() {
		return dateCreated;
	}//end of getDateCreated()

	public void setDateCreated(Dated dateCreated) {
		this.dateCreated = dateCreated;
	}//end of setDateCreated

	public Dated getDateModified() {
		return dateModified;
	}//end of getDateModified()

	public void setDateModified(Dated dateModified) {
		this.dateModified = dateModified;
	}//end of setDateModified

	//---------------------------------------------------------------
	// DECOYS to confuse the user
	//---------------------------------------------------------------
	public ArrayList<String> getDecoys() {
		return decoys;
	}//end of getDecoys()

	public void setDecoys(ArrayList<String> decoys) {
/*		ArrayList<String>  loweredDecoys = new ArrayList<String>();
		for(String lowDecs : decoys){
			loweredDecoys.add(lowDecs.toLowerCase());
		}
		*/
		this.decoys = decoys;
	}//end of setDecoys
	
	public void addDecoy(String decoy){
		//NB: does not check if decoy has been initialized...check in constructor
		//decoys.add(decoy.toLowerCase());
		decoys.add(decoy);
	}//end of addDecoy

	//---------------------------------------------------------------
	//	IMAGE LINKS - questions may need images to convey help
	//	NB: Think about adding a download image directly (internet)
	//---------------------------------------------------------------
	public ArrayList<String> getImageLinks() {
		return imageLinks;
	}//end of getImageLinks()
	
	public void addImageLink(String link){
		imageLinks.add(link);
	}//end of addImageLink

	public void setImageLinks(ArrayList<String> imageLinks) {
		this.imageLinks = imageLinks;
	}//end of setImageLinks

	//---------------------------------------------------------------
	//	DECOYS REPLACED FOR QUESTION - Replaced with 
	//	terms from the "pot" related to the Subject
	//	to which this question belongs //encapsulation wahala?
	//---------------------------------------------------------------
	public boolean isUsePot() {
		return usePot;
	}//end of isUsePot

	public void setUsePot(boolean usePot) {
		this.usePot = usePot;
	}//end of setUsePot()

	//---------------------------------------------------------------
	// DECOY USAGE - ARE DECOYS ALLOWED?
	//---------------------------------------------------------------
	public boolean usesDecoys() {
		return useDecoys;
	}//end of isUseDecoys

	public void setUseDecoys(boolean useDecoys) {
		this.useDecoys = useDecoys;
	}//end of setUseDecoys

	//---------------------------------------------------------------
	// IMAGE USAGE - Are they enabled; Enable if needed
	//---------------------------------------------------------------
	public boolean usesImages() {
		return usesImages;
	}//end of isUsesImages

	public void setUseImages(boolean usesImages) {
		this.usesImages = usesImages;
	}//end of setUsesImages

	//---------------------------------------------------------------
	// SC(self commenting) - "A User Class would make this sensible
	// in a case where this is used by more than one user"
	//---------------------------------------------------------------
	public double getFastestAnswerTime() {
		return fastestAnswerTime;
	}//end of getFastestAnswerTime()

	public void setFastestAnswerTime(double fastestAnswerTime) {
		this.fastestAnswerTime = fastestAnswerTime;
	}//end of setFastestAnswerTime
	
	//---------------------------------------------------------------
	// GET TYPES
	//---------------------------------------------------------------
	public boolean isTypeDefinition(){
		return (type == DEFINITION_QUESTION);
	}//end of isTypeDefinition
	
	public boolean isTypeTrueFalse(){
		return (type == TRUEFALSE_QUESTION);
	}//end of isTypeTrueFalse
	
	public boolean isTypeFillGap(){
		return (type == FILLGAP_QUESTION);
	}//end of isTypeFillGap
	
	//---------------------------------------------------------------
	//	GET AND SET SUBJECT ID
	//---------------------------------------------------------------
	public String getSubjectID(){
		return subjectID;
	}//end of getSubjectID
	
	public void setSubjectID(String subject){
		subjectID = subject;
	}//end of setSubjectID
/*
 *	//---------------------------------------------------------------
 *	//	END OF GETTERS AND SETTERS
 *	//---------------------------------------------------------------
 */	
	//---------------------------------------------------------------
	//	TO STRING: Returns Minor Details of the question
	//---------------------------------------------------------------
	public String toString(){
		String detail ="";
		
		//String id = idNum;
		//String dateOfCreation = dateCreated.toString();
		
		//Get the questions
		//detail = "["+ id +"]: " + wording + "...";
		detail = wording + "?...";	
		//Add authors
		//detail = detail + ". Created by "+ author;
		
		//Add Date Created
		//detail = detail + " on "+ dateOfCreation;

		return detail;
	}//end of toString()

	//---------------------------------------------------------------
	//	TO STRING2: Return All Details of the question
	//---------------------------------------------------------------
	public String toString2(){
		String detail ="";
		
		String id = idNum;
		String dateOfCreation = dateCreated.toString();
		int imageListSize = imageLinks.size();
		int decoysSize = decoys.size();
		
		//Get the questions
		detail = "["+ id +"]: " + wording;
		
		//Depending on the type, get the answers and or decoys associated
		if (isTypeDefinition() || isTypeTrueFalse()){
			detail = detail + "\n[Answer]: " + answer;
			
			if(usesDecoys()){
				for(int i = 0; i<decoysSize; i++){
					detail = detail + "\n[Decoy "+ (i+1)+ "]: " + decoys.get(i);
				}//for decoySize
			}//isUseDecoys
			
			if(usesImages()){
				for(int i = 0; i<imageListSize; i++){
					detail = detail + "\n[Image "+ (i+1)+ "]: " + imageLinks.get(i);
				}//for imageListSize
			}//usesImages
		}//isTypeDefinition
			
/*		else if (isTypeTrueFalse()){
			detail = detail + "\n[Answer]: " + Boolean.toString(torfAnswer);
			
			//NB: You should be doing this once(in the code...not just logically)
			if(usesImages()){
				for(int i = 0; i<imageListSize; i++){
					detail = detail + "\n[Image "+ i+1+ "]: " + imageLinks.get(i);
				}//for imageListSize
			}//usesImages	
		}//else if isTypeTrueFalse
*/		
		//Add authors
		detail = detail + "\nCreated by "+ author;
		
		//Add Date Created
		detail = detail + " on "+ dateOfCreation;

		return detail;
	}//end of toString2()
	
	//---------------------------------------------------------------------------
	//	COMPARE TO: String order
	//---------------------------------------------------------------------------
	public int compareTo(Question prob){
		String probId = prob.getIdNum();
		int value = idNum.compareTo(probId);
		return value;
	}//end of compareTo()
	
	//---------------------------------------------------------------------------
	//	IS DEFAULT returns true for questions who only use the empty constructor
	//---------------------------------------------------------------------------
	public boolean isDefaultQuestion(){
		return(idNum.equals("0"));
	}//end of isDefaultQuestion
}
