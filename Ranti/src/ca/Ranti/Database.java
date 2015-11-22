package ca.Ranti;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


//To delete QuestionDecoys use both columns, also SubjectQuestions, also SubjectPot and Potwords
public class Database {
	
	static Connection connector;
	static PreparedStatement pstmt;
	static ResultSet rs;
	static String dBDirectory ="Data";//Default Database folder
	static String dBName = "RantiSqlite2.db";
	static String dBDirectoryAndName = dBDirectory +"/" + dBName;
	static String comma = ",";
	
	
/*	public static void main(String [] args){
		//firstTimePopulate();
		//getQuestions("cmpt");
		connectDB();

		TestTaking test = new TestTaking("CMPT300", "1");
		test.addTestOptionType(1);
		test.addTestOptionType(2);
		test.setBetweenDates("2014-08-01","2014-11-31");
		test.addAuthor("1");
		test.addAuthor("2");
		test.addAuthor("3");

		System.out.println(prepareTestTakingSqlCode(test));
		ArrayList <Question> qS = getQuestions(test);
		for(Question eachQ : qS){
			System.out.println(eachQ.toString2());
		}
		
		createOrUpdate("DELETE FROM QUESTION WHERE ID = 'CMPT365_4'");
		
		
		
		closeDB();
	}*/
	
	
	//----------------------------------------------------------------------
	//	CONNECT TO DATABASE
	//----------------------------------------------------------------------	
	public static void connectDB(){

		String connectionString = "jdbc:sqlite:"+dBDirectoryAndName;
		File rantiDirectory = new File(dBDirectory);
		if(!rantiDirectory.exists())
			rantiDirectory.mkdirs();
		
		try{
			Class.forName("org.sqlite.JDBC");
			connector = DriverManager.getConnection(connectionString);
			createOrUpdate("PRAGMA foreign_keys = ON;");
		}catch(Exception e){
			Message.informUser(null,"Database Open Error!",	errorToString(e));
		}
		//System.out.println("\tDataBase Opened");
	}//end connectDB

	//----------------------------------------------------------------------
	//	CLOSE CONNECTION
	//----------------------------------------------------------------------
	public static void closeDB(){
		
		try{
			closeRS();
			closePS();
			if(connector != null){
				connector.close();
			}
		}catch(Exception e){
			Message.informUser(null,"Database Close Error!",	errorToString(e));
		}
		//System.out.println("\tDataBase Closed");
	}//end closeDB

	//----------------------------------------------------------------------
	//	CLOSE RESULTSET OBJECT
	//----------------------------------------------------------------------
	public static void closeRS(){
		try{
			if(rs != null){
				rs.close();
			}
		}
		catch(Exception e){
			Message.informUser(null,"Database ResultSet Close Error!",	errorToString(e));
		}
	}//end of closeRS
	
	//----------------------------------------------------------------------
	//	CLOSE PREPARED STATEMENT OBJECT
	//----------------------------------------------------------------------
	public static void closePS(){
		try{
			if(pstmt != null){
				pstmt.close();
			}
		}catch(Exception e){
			Message.informUser(null,"Database Prepared Statement Error!", errorToString(e));
		}
	}//end of closeRS
	
	//----------------------------------------------------------------------
	//	POPULATE DATABASE ON FIRST RUN:
	//----------------------------------------------------------------------	
	public static void firstTimePopulate(){
		File rantiDirectory = new File(dBDirectoryAndName);

		if (!rantiDirectory.exists()){
			//System.out.println("DB_UPDATE:Database:"+dBName+" does NOT exist.");
			//System.out.println("DB_UPDATE:Creating Directory and Database:");
			//System.out.println("DB_UPDATE:Attempt to populate Database");
			
			connectDB();
		
			try 
			{
				new DBaseCreation(connector,dBDirectory).createAllDBTables();
			}
			catch(SQLException e)
			{
				Message.informUser(null,"Database First Run  Error!", errorToString(e));
			}
		}
		else
		{
			//database already exist message
			//System.out.println("DB_UPDATE:Database: "+dBName+" ALREADY exists.");
		}

		closeDB();
	}//end of firstTimePopulate()
	
	//----------------------------------------------------------------------
	//	RUNCOUNT: Inserts the count of times the program has been run.
	//----------------------------------------------------------------------
	public static void runCount(){
		connectDB();
		String date = Database.sSP(new Dated().toSqlString());
		createOrUpdate("INSERT INTO SYSRUN VALUES(NULL,"+ date +")");
		closeDB();
	}//end of runCount()
	
	//----------------------------------------------------------------------
	//	CREATE FOLDER TO PLACE DATABASE: Returns (string)directory of the
	//	created folder
	//----------------------------------------------------------------------	
/*	private static String createFolder(){
		 
	 	//Allowing a user to select default folder;
		
		JFileChooser chooseDBdir = new JFileChooser();
		chooseDBdir.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		int selectedDir = chooseDBdir.showDialog(null, "Select Folder");
		
		if(selectedDir == JFileChooser.APPROVE_OPTION)
			dBDirectory = chooseDBdir.getSelectedFile().toString();
		
	
		File theDirectory = new File(dBDirectory);
		theDirectory.mkdirs();

		return dBDirectory;
	}//end of createFolder
*/	
	
	//----------------------------------------------------------------------
	//	GET ALL USERS: Used by UserGuiPanel
	//----------------------------------------------------------------------
	public static ArrayList<User> getAllUsers(){
		ArrayList<User> users = new ArrayList<User>();
		String sqlGetUsers = "SELECT * FROM USER";
		String userNum = "";
		String userName = "";
		
		connectDB();
		rs = queryTable(sqlGetUsers);
		
		try{
			
			while (rs.next()){
				userNum = Integer.toString(rs.getInt(1));
				userName = rs.getString(2);
				users.add(new User(userNum, userName));
			}//end while rs.next
		}
		catch(SQLException e){
			Message.informUser(null,"Database Get Users Error!", errorToString(e));
		}
		finally{
			closeDB();
		}
		
		return users;
	}//end of getAllUsers()
	
	//----------------------------------------------------------------------
	//	ADD A USER: Used by UserGuiPanel
	//----------------------------------------------------------------------
	public static void addUser(String userName){
		String sqlAddUser = "INSERT INTO USER VALUES (NULL,"+ sSP(userName) +")";
		
		connectDB();
		createOrUpdate(sqlAddUser);
		closeDB();
	}//end of addUser
	
	//----------------------------------------------------------------------
	//	GET ALL SUBJECTS
	//----------------------------------------------------------------------
	public static ArrayList<Subject> getAllSubjectNames(){
		ArrayList<Subject> subs = new ArrayList<Subject>();
		
		String sqlGetSubjectNames = "SELECT * FROM SUBJECT";
		String id = "";
		String desc = "";
		//User author;
		connectDB();
		
		try{
			rs = queryTable(sqlGetSubjectNames);
			
			while(rs.next()){
				id = rs.getString(1);
				desc = rs.getString(2);
				subs.add(new Subject(id,desc));
				//reset values
				id = "";
				desc = "";
			}
		}
		catch(SQLException e){
			Message.informUser(null,"Database Get Subject Name Error!", errorToString(e));
		}
		finally{
			closeDB();
		}
		
		return subs;
	}//end of getAllSubjects
	
	//----------------------------------------------------------------------
	//	ADD A SUBJECT
	//----------------------------------------------------------------------
	public static void addSubject(String id, String desc, String creatorId){
		String sqlSubject = "SUBJECT";
		String sqlValues = sSP(id) +comma + sSP(desc) + comma + sSP(creatorId);
		
		connectDB();
		insertInto(sqlSubject,sqlValues);
		closeDB();
	}//end of addUser
	
	//----------------------------------------------------------------------
	//	GET SUBJECT CONTENT
	//----------------------------------------------------------------------
	public static Subject getSubjectContent(Subject subject){
		
		
		
		return subject;
	}//end of getAllSubjects 
	
	//----------------------------------------------------------------------
	//	GET SUBJECT POTLIST
	//----------------------------------------------------------------------
	public static ArrayList<String> getSubjectPotList(String subjectID){
		ArrayList<String> pot = new ArrayList<String>();
		//String sqlQuery = "SELECT P.ID,P.WORD FROM POTWORD P INNER JOIN SUBJECTPOT SP "
						//+ "ON (P.ID = SP.WORDID AND SP.SUBJECTID = "+ sSP(subjectID) +")";
		
/*		String sqlQuery1 = "SELECT DISTINCT PW.ID,PW.WORD " 
				 +"FROM QUESTIONDECOYS QD INNER JOIN QUESTIONSUBJECT QS INNER JOIN POTWORD PW ON "
				 +"(QD.QUESTIONID = QS.QUESTIONID AND QD.DECOYID = PW.ID AND QS.SUBJECTID =" + sSP(subjectID)+ ");";
		String sqlQuery2 = "SELECT P.ID,P.WORD FROM POTWORD P,QUESTIONANSWER QA,QUESTIONSUBJECT QS"
				+ " WHERE QA.QUESTIONID = QS.QUESTIONID AND QA.ANSWERID = P.ID AND QS.SUBJECTID = "+sSP(subjectID)+" AND P.ID NOT IN " 
				+ "(SELECT  PW.ID" 
				+" FROM QUESTIONDECOYS QD INNER JOIN QUESTIONSUBJECT QS INNER JOIN POTWORD PW ON "
				+" (QD.QUESTIONID = QS.QUESTIONID AND QD.DECOYID = PW.ID AND QS.SUBJECTID = "+sSP(subjectID)+"));";*/
		
		String sqlQuery1 = "SELECT P.ID,P.WORD "
				+ "FROM QUESTIONDECOYS QD INNER JOIN QUESTIONSUBJECT QS INNER JOIN POTWORD P "
				+ "ON QD.QUESTIONID = QS.QUESTIONID AND QD.DECOYID = P.ID AND QS.SUBJECTID =" +sSP(subjectID)
				+" UNION "
				+ "SELECT P.ID,P.WORD "
				+ "FROM QUESTIONANSWER QA,QUESTIONSUBJECT QS, POTWORD P "
				+ "WHERE QA.QUESTIONID = QS.QUESTIONID AND QA.ANSWERID = P.ID AND QS.SUBJECTID =" +sSP(subjectID)
				+" ORDER BY P.WORD DESC";
		
		connectDB();
		
		try {
			rs = queryTable(sqlQuery1);
			while (rs.next()){
				pot.add(rs.getString(2));
			}

		}
		catch(SQLException e){
			Message.informUser(null,"Database PotList Error!", errorToString(e));
		}
		finally{
			closeDB();
		}
		
		
		return pot;
	}//end of getSubjectPotList 
	
	//----------------------------------------------------------------------
	//	GET ALL QUESTIONS and minor details OF SPECIFIED SUBJECT;
	// 	including ID,Wordings and Creator
	//----------------------------------------------------------------------
	public static ArrayList<Question> getAllQuestionBasics(String subjectID){
		ArrayList<Question> result = new ArrayList<Question>();
		String sqlGetQuestions = "SELECT Q.ID,Q.WORDING,Q.CREATORID,USER.USERNAME,Q.DATECREATED"
				+ " FROM QUESTION Q INNER JOIN QUESTIONSUBJECT QS "
				+ "ON (Q.ID = QS.QUESTIONID AND QS.SUBJECTID = "+ sSP(subjectID)+") "
						+ "INNER JOIN USER ON Q.CREATORID = USER.ID;";
		
		String id = "";
		String words = "";
		String creatorID = "";
		String creatorName = "";
	//	Dated created;
		Question quest;
		User creator;
		
		connectDB();
		rs = queryTable(sqlGetQuestions);
		
		try{
			while(rs.next()){
				id = rs.getString(1);
				words = rs.getString(2);
				creatorID = Integer.toString(rs.getInt(3));
				creatorName = rs.getString(4);
				
				//Question 
				quest = new Question(id,words);
				quest.setSubjectID(subjectID);
				creator = new User(creatorID, creatorName);
				quest.setAuthor(creator);
				
				//Add
				result.add(quest);
				
				//Reset values
				id = "";
				words = "";
				creatorID = "";
				creatorName = "";
			}
		}
		catch(SQLException e)
		{
			Message.informUser(null,"Database Question Basics Error!", errorToString(e));
		}
		
		finally{
			closeDB();
		}
		
		return result;
	}//end of getQuestions()
	
	//----------------------------------------------------------------------
	//	PREPARE TESTAKING CRITERIA SQL STATEMENT: 
	//----------------------------------------------------------------------
	private static String prepareTestTakingSqlCode(TestCriteria criteria){
		String statement = "SELECT QS.SUBJECTID, Q.*, "
								  + "QDETS.TYPE,QDETS.DECOYS,QDETS.POT,QDETS.IMAGES,"
								  + "P.ID,P.WORD,U.* "
	    
								  + "FROM QUESTIONSUBJECT QS INNER JOIN " 
										+ "QUESTION 		 Q  INNER JOIN "
										+ "USER 			 U  INNER JOIN "
									 	+ "POTWORD		 P  INNER JOIN "
										+ "QUESTIONANSWER  QA INNER JOIN " 
										+ "QUESTIONDETAILS QDETS ON "
													+ "(QS.QUESTIONID = Q.ID AND "
													+ "Q.ID = QA.QUESTIONID AND "
													+ "QA.QUESTIONID = QDETS.QUESTIONID AND "
													+ "QA.ANSWERID = P.ID AND "
													+ "U.ID = Q.CREATORID AND "
													+ "QS.SUBJECTID = "+ sSP(criteria.getSubjectId()) +" ) ";
		
		//Prepare the dates, question creators and question types for the "where" statement
		
		//dates 
		//the check may be a waste of time,
		//but they are to prevent uninitialized TestTaking objects crashing the program
		ArrayList<String> dates = criteria.getTestDates();
		String qDate = "(strftime('%Y-%m-%d',Q.DATECREATED) >= strftime('%Y-%m-%d','1991-01-01') )";
		if(dates.size() > 0){
			qDate = "(strftime('%Y-%m-%d',Q.DATECREATED) >= " 
					+ "strftime('%Y-%m-%d'" +comma+ sSP(dates.get(0)) + ")" 
					+ " AND strftime('%Y-%m-%d',Q.DATECREATED) <= "
					+ "strftime('%Y-%m-%d'" +comma + sSP(dates.get(1))+"))";
		}

		//types
		int [] type = criteria.getTestOptionTypes();
		String qDetsDets ="(QDETS.TYPE = " + Integer.toString(type[0]) + " OR " +
						  " QDETS.TYPE = " + Integer.toString(type[1]) + " OR " +
						  " QDETS.TYPE = " + Integer.toString(type[2]) +")";
		//authors
		ArrayList<String> authors = criteria.getTestAuthors();
		String qAuthors = "(";
		int i = 0;
		int authorSize = authors.size();
		for(;i<authorSize;i++){
			if(i == authorSize-1){
				qAuthors = qAuthors + "Q.CREATORID = " + authors.get(i) + " )";
			}
			else{
				qAuthors = qAuthors + "Q.CREATORID = " + authors.get(i) + " OR ";
			}
		}
		
		//Finish sqlStatement
		if(qAuthors.equals("(")){
			statement = statement + "WHERE " + qDate + " AND " + qDetsDets;
		}
		else{
			statement = statement + "WHERE " + qDate + " AND " + qDetsDets + " AND " + qAuthors;	
		}
		
		
		return statement;
	}
	
	//----------------------------------------------------------------------
	//	GET ALL QUESTIONS major and minor details OF SPECIFIED Question;
	// 	including ID,Wordings and Creator Used by TestTaking Object
	//----------------------------------------------------------------------
	public static ArrayList<Question> getQuestions(TestCriteria criteria){
		ArrayList<Question> result = new ArrayList<Question>();
		String sqlStatement = prepareTestTakingSqlCode(criteria);
		
		Question qFromDB;
		String subId = "";
		String qId = "";
		String qWording = "";
		String qCreatorId = "";
		String qCreatorName = "";
		String qDateCreated = "";
		String qDateModified = "";
		int qType;
		boolean decoys;
		boolean pot;
		boolean images;
		String qAnswer;
		
		connectDB();
		rs = queryTable(sqlStatement);
		
		try{
			while (rs.next()){
				subId = rs.getString(1);
				qId = rs.getString(2);
				qWording = rs.getString(3);
				//qCreatorId = rs.getString(4);
				qDateCreated = rs.getString(5);
				qDateModified = rs.getString(6);
				qType = rs.getInt(7);
				decoys = toBoolean(rs.getInt(8));
				pot = toBoolean(rs.getInt(9));
				images = toBoolean(rs.getInt(10));
				qAnswer = rs.getString(12);
				qCreatorId = rs.getString(13);
				qCreatorName = rs.getString(14);
				
				qFromDB = new Question(qId,qWording,qAnswer);
				qFromDB.setType(qType);
				qFromDB.setAuthor(new User(qCreatorId,qCreatorName));
				qFromDB.setDateCreated(new Dated(qDateCreated));
				qFromDB.setDateModified(new Dated(qDateModified));
				qFromDB.setUseDecoys(decoys);
				qFromDB.setUsePot(pot);
				qFromDB.setUseImages(images);
				qFromDB.setSubjectID(subId);
				
				if (qFromDB.usesDecoys()){
					qFromDB.setDecoys(getQuestionDecoy(qFromDB.getIdNum()));
				}
				if(qFromDB.usesImages()){
					qFromDB.setImageLinks(getQuestionImages(qFromDB.getIdNum()));
				}
				
				result.add(qFromDB);
			}
		}
		catch(SQLException e){
			Message.informUser(null,"Database Get Questions Error!", errorToString(e));
		}
/*		finally{
			closeDB();
		}*/
		
		return result;
	}//end of getQuestions()
	
	//----------------------------------------------------------------------
	//	GET QUESTION DECOYS: Manages its own result set;
	//----------------------------------------------------------------------
	private static ArrayList<String> getQuestionDecoy(String questionId){
		
		ArrayList<String> decoys = new ArrayList<String>();
		String sqlString = "SELECT * FROM QUESTIONDECOYS QD INNER JOIN "
						 + " POTWORD P ON (QD.DECOYID = P.ID AND QD.QUESTIONID = "
						 + sSP(questionId)+");";
		
		String word = "";
		ResultSet internalRs = queryTable(sqlString);
		
		try{
			while(internalRs.next()){
				word = internalRs.getString(4);
				decoys.add(word);
				word = "";
			}
		}
		catch(SQLException e){
			Message.informUser(null,"Database Question Decoys Error!", errorToString(e));
		}
		finally{
			try{
				if(internalRs != null){ 
					internalRs.close();
				}
			}catch(SQLException e){
				Message.informUser(null,"Database QDecoy ResultSet Error!", errorToString(e));
			}
		}
		
		return decoys;
	}//end of getQuestionDecoy
	
	//----------------------------------------------------------------------
	//	GET QUESTION IMAGES: Manages its own resultSet object;
	//----------------------------------------------------------------------
	private static ArrayList<String> getQuestionImages(String questionId){
		ArrayList<String> images = new ArrayList<String>();
		String sqlString = " SELECT * FROM QUESTIONIMAGE QI "
						  + " WHERE QI.QUESTIONID = " +  sSP(questionId);

		String imageLink = "";
		ResultSet internalRs = queryTable(sqlString);
		
		try{
			while(internalRs.next()){
				imageLink = internalRs.getString(2);
				images.add(imageLink);
				imageLink = "";
			}
		}
		catch(SQLException e){
			Message.informUser(null,"Database QImages Error!", errorToString(e));
		}
		finally{
			try{
				if(internalRs != null){ 
					internalRs.close();
				}
			}catch(SQLException e){
				Message.informUser(null,"Database QImages ResultSet Error!", errorToString(e));
			}
		}
		
		return images;
	}//end of getQuestionImages
	
	//----------------------------------------------------------------------
	//	ADD QUESTION
	//----------------------------------------------------------------------
	public static void addQuestion(Question tempQuestion){
		//Prepare arguments
		String subjectId = sSP(tempQuestion.getSubjectID());
		String questId = sSP(tempQuestion.getIdNum());
		String wording = sSP(tempQuestion.getWording());
		String dateCrea = sSP(tempQuestion.getDateCreated().toSqlString());
		String dateMod = sSP(tempQuestion.getDateModified().toSqlString());
		
		String type = Integer.toString(tempQuestion.getType());
		String useDecoys = Integer.toString(booleanToInt(tempQuestion.usesDecoys()));
		String pot = Integer.toString(booleanToInt(tempQuestion.isUsePot()));
		String images = Integer.toString(booleanToInt(tempQuestion.usesImages()));
		
		String answer = tempQuestion.getAnswer();//didnt't use sSP(String) because 
												//insertDecoyOrAnswer() already does
		String creatorId = tempQuestion.getAuthor().getUserId();//no sSP() cuz id is int in DB
		
		ArrayList<String> decoys = tempQuestion.getDecoys();
		ArrayList<String> imageLinks = tempQuestion.getImageLinks();

		
		connectDB();
		
		insertInto("QUESTION", questId+comma+wording+comma+ creatorId +comma+dateCrea+comma+dateMod);
		insertInto("QUESTIONSUBJECT", subjectId+comma+questId);
		insertInto("QUESTIONDETAILS",questId+comma+type+comma+useDecoys+comma+pot+comma+images);
		insertDecoyOrAnswer(tempQuestion.getIdNum(),"answer",answer);
		
		if(tempQuestion.usesDecoys()){
			for(String decoy:decoys){
				insertDecoyOrAnswer(tempQuestion.getIdNum(),"decoy",decoy);//no need for sSP()
			}
		}
		
		if(tempQuestion.usesImages()){
			for(String link:imageLinks){
				insertInto("QUESTIONIMAGE", questId+ comma + sSP(link));//MUST sSP!!!!!
			}
		}
		
		closeDB();
	}//end of addQuestion
	
	//--------------------------------------------------------------------------
	//INSERT DECOY OR ANSWER(indoa): Uses insertInto(String,String) eg
	//indoa('cmpt365_1',"answer",'word')-->insertinto("QUESTIONANSWER","'word'")
	//e.g 
	//indoa('cmpt365_1',"decoy",'word')-->insertinto("QUESTIONDECOYS","'word'").
	//Before it is called, the database connection must be opened...or whatever
	//has called it must have an open DB connection;
	//--------------------------------------------------------------------------
	public static void insertDecoyOrAnswer(String questid, String decoyOrAnswer, String word){
		String decOrAnsVal = decoyOrAnswer;
		String sqlQuestId = sSP(questid);
		int pid = getPotId(word);
		if(pid < 0){
			insertInto("POTWORD","NULL" + comma +sSP(word));
			pid = getPotId(word);
		}
		
		if(decOrAnsVal.equals("decoy")){
			insertInto("QUESTIONDECOYS", sqlQuestId + comma + pid);
		}
		else if (decOrAnsVal.equals("answer")){
			insertInto("QUESTIONANSWER",sqlQuestId+comma+pid);
		}
	}//end of insertDecoyAndAnswer()
	
	//----------------------------------------------------------------------
	//	GET POT ID
	//----------------------------------------------------------------------
	private static int getPotId(String word){
		int id = -1;
		String sqlQuery = "SELECT P.ID FROM POTWORD P WHERE WORD ="+ sSP(word); 
		
		rs = queryTable(sqlQuery);
		try{
			while (rs.next()){
				id = rs.getInt(1);
			}
		}catch(SQLException e){
			Message.informUser(null,"Database Pot Word ID Error!", errorToString(e));
		}
		
		return id;
	}//end of getPotId(String)
	
	//----------------------------------------------------------------------
	//	TEST CREATE OR UPDATE TABLES
	//----------------------------------------------------------------------
	public static void createOrUpdate(String sql){//about to change to boolean.//use a messaging sys instead
		try{
			//System.out.println("SQLSTATEMENT(createOrUpdate): " + sql);
			pstmt = connector.prepareStatement(sql);
			pstmt.executeUpdate();
		}
		catch(SQLException e){
			Message.informUser(null,"Database Create or Update Error!", errorToString(e));
			//e.printStackTrace();
		}
		finally{
			closePS();
		}
	}//end of createOrUpdate
	
	//----------------------------------------------------------------------
	//	QUERY TABLE: Returns Result Set
	//----------------------------------------------------------------------
	public static ResultSet queryTable(String query){
		try{
			//System.out.println("SQLSTATEMENT(queryTable): " + query);
			pstmt = connector.prepareStatement(query);
			ResultSet tempRS =  pstmt.executeQuery();
			return tempRS;
		}
		catch(SQLException e){
			Message.informUser(null,"Database Query Error!", errorToString(e));
			return null;
		}
	}//end of queryTable
	
	//----------------------------------------------------------------------
	// SQL STRING PASSER(sSP): Converts string to 'string' for sqlite
	// e.g "Momentary" --> "'Momentary'"; "DC" ---> "'DC'"
	//----------------------------------------------------------------------
	public static String sSP(String passed){
		String [] removeApostrophes = passed.split("'");
		String result = "'";
		for(String word: removeApostrophes){
			result = result + word;
		}
		result = result + "'";
		return result;
	}//end of sqlStringPasser 
	
	
	//----------------------------------------------------------------------
	//	Print out SQL Errors
	// 	Thinking I should create a class that collects this data and prints
	//  to a file on close of the app.
	//----------------------------------------------------------------------
	protected static String errorToString(Exception e){
		String error = (e.getClass() + ": " + e.getMessage());
		return error;
		//System.err.println(e.getClass() + ": " + e.getMessage());
	}//end of errorToString
	
	//----------------------------------------------------------------------
	//	Insert Preparer: no need for brackets:->insertInto(table, values)
	//		does createOrUpdate( INSERT INTO "table" VALUES ("values"))
	//----------------------------------------------------------------------
	private static void insertInto(String table, String values){
		String sqlReady = "INSERT INTO " +table+ " VALUES(" + values +")";
		//return sqlReady;
		createOrUpdate(sqlReady);
	}
	
	//----------------------------------------------------------------------
	//	Insert Preparer: no need for brackets:->insertInto(table, values)
	//		does createOrUpdate( INSERT INTO "table" VALUES ("values"))
	//----------------------------------------------------------------------
	public static void updateTable(String table, String setValue, String wheres){
		String sqlReady = "UPDATE " +table+ " SET " + setValue + " WHERE " + wheres;
		//return sqlReady;
		createOrUpdate(sqlReady);
	}
	
	//----------------------------------------------------------------------
	//	TO BOOLEAN : 1 = true -infinity to 0 = false; 2 to infinity = false
	//----------------------------------------------------------------------
	private static boolean toBoolean(int number){
		if(number == 1){
			return true;
		}
		else{
			return false;
		}
	}
	
	//----------------------------------------------------------------------
	//	REVERSE OF TO BOOLEAN : 1 = true; 0 = false
	//----------------------------------------------------------------------
	private static int booleanToInt(boolean truth){
		if(truth){
			return 1;
		}
		else{
			return 0;
		}
	}
}