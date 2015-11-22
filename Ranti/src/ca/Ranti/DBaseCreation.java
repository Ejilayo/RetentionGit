package ca.Ranti;

import java.io.File;
import java.sql.Connection;
//import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

//----------------------------------------------------------------------
//	DBASECREATION: Creates all the tables required for the database to 
//	function;
// 	might need to add a table swap function for dropping and recreating 
//  tables to alter them etc.
//----------------------------------------------------------------------
public class DBaseCreation {

	PreparedStatement pstmt;
	Connection conn;
	ResultSet rs;
	Statement stmt;
	
	//----------------------------------------------------------------------
	//	START OF SQLITE COMMANDS
	//----------------------------------------------------------------------
	//String foreignKey = "PRAGMA foreign_keys = ON;";
	
	String createSysRun = "CREATE TABLE SYSRUN("
			+ "ID INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ "RUNDATE TEXT);";
	
	String createUser = "CREATE TABLE USER ("
			+ "ID INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ "USERNAME TEXT COLLATE NOCASE UNIQUE);";
	
	String createQuestion = "CREATE TABLE QUESTION ("
			+ "ID TEXT COLLATE NOCASE PRIMARY KEY NOT NULL,"
			+ "WORDING TEXT,"
			+ "CREATORID INTEGER,"
			+ "DATECREATED TEXT,"
			+ "DATEMODIFIED TEXT,"
			+ "FOREIGN KEY (CREATORID) REFERENCES USER(ID));";
	
	String createSubject = "CREATE TABLE SUBJECT("
			+ "ID TEXT COLLATE NOCASE PRIMARY KEY,"
			+ "DESCRIPTION TEXT COLLATE NOCASE,"
			+ "CREATORID INTEGER,"
			+ "FOREIGN KEY (CREATORID) REFERENCES USER(ID) "
			+ 	"ON UPDATE CASCADE "
			+ 	"ON DELETE SET NULL);";

	String createTest = "CREATE TABLE TEST("
			+ "ID TEXT COLLATE NOCASE PRIMARY KEY,"
			+ "DATECREATED TEXT,"
			+ "SUBJECTID TEXT,"
			+ "CREATORID INTEGER,"
			+ "POINTSPERANSWER REAL,"
			+ "TIMEPERQUESTION INTEGER,"
			+ "FOREIGN KEY (SUBJECTID) REFERENCES SUBJECT(ID) "
			+ 	"ON UPDATE CASCADE "
			+ 	"ON DELETE CASCADE,"
			+ "FOREIGN KEY (CREATORID) REFERENCES USER(ID) "
			+ 	"ON UPDATE CASCADE "
			+ 	"ON DELETE SET NULL);";
	
	String createPotWord = "CREATE TABLE POTWORD("
			+ "ID INTEGER PRIMARY KEY,"
			+ "WORD TEXT COLLATE NOCASE UNIQUE);";
	
	String createUserStats = "CREATE TABLE USERSTATS("
			+ "USERID INTEGER,"
			+ "TESTID TEXT COLLATE NOCASE,"
			+ "SUBJECTID TEXT COLLATE NOCASE,"
			+ "SCORE REAL,"
			+ "TOTALQUESTIONNUM INTEGER,"
			+ "CORRECTNUM INTEGER,"
			+ "PRIMARY KEY (USERID, TESTID, SUBJECTID),"
			+ "FOREIGN KEY (USERID) REFERENCES USER(ID) "
			+ 	"ON UPDATE CASCADE "
			+ 	"ON DELETE CASCADE,"
			+ "FOREIGN KEY (TESTID) REFERENCES TEST(ID) "
			+ 	"ON UPDATE CASCADE "
			+ 	"ON DELETE CASCADE,"
			+ "FOREIGN KEY (SUBJECTID) REFERENCES SUBJECT(ID) "
			+ 	"ON UPDATE CASCADE "
			+ 	"ON DELETE CASCADE);";
	
	String createQuestionSubject = "CREATE TABLE QUESTIONSUBJECT("
			+ "SUBJECTID TEXT COLLATE NOCASE,"
			+ "QUESTIONID TEXT COLLATE NOCASE UNIQUE,"
			+ "PRIMARY KEY (SUBJECTID,QUESTIONID),"
			+ "FOREIGN KEY (SUBJECTID) REFERENCES SUBJECT(ID) "
			+ 	"ON UPDATE CASCADE "
			+ 	"ON DELETE CASCADE,"
			+ "FOREIGN KEY (QUESTIONID) REFERENCES QUESTION(ID) "
			+ 	"ON UPDATE CASCADE "
			+ 	"ON DELETE CASCADE);";
	
	String createQuestionImage = "CREATE TABLE QUESTIONIMAGE("
			+ "QUESTIONID TEXT COLLATE NOCASE,"
			+ "IMAGEPATH TEXT,"
			+ "FOREIGN KEY (QUESTIONID) REFERENCES QUESTION(ID) "
			+ 	"ON UPDATE CASCADE "
			+ 	"ON DELETE CASCADE)";
	
	String createQuestionAnswer = "CREATE TABLE QUESTIONANSWER("
			+ "QUESTIONID TEXT COLLATE NOCASE PRIMARY KEY,"
			+ "ANSWERID INTEGER,"
			+ "FOREIGN KEY (QUESTIONID) REFERENCES QUESTION(ID) "
			+ 	"ON UPDATE CASCADE "
			+ 	"ON DELETE CASCADE, "
			+ "FOREIGN KEY (ANSWERID) REFERENCES POTWORD(ID) "
			+ 	"ON UPDATE CASCADE "
			+ 	"ON DELETE RESTRICT);";
	
	String createQuestionDetails = "CREATE TABLE QUESTIONDETAILS("
			+ "QUESTIONID TEXT COLLATE NOCASE PRIMARY KEY,"
			+ "TYPE INTEGER,"
			+ "DECOYS INTEGER,"
			+ "POT INTEGER,"
			+ "IMAGES INTEGER,"
			+ "FOREIGN KEY (QUESTIONID) REFERENCES QUESTION(ID) "
			+ 	"ON UPDATE CASCADE "
			+ 	"ON DELETE CASCADE);";
	
	String createQuestionDecoys = "CREATE TABLE QUESTIONDECOYS("
			+ "QUESTIONID TEXT COLLATE NOCASE,"
			+ "DECOYID INTEGER,"
			+ "PRIMARY KEY (QUESTIONID,DECOYID),"
			+ "FOREIGN KEY (DECOYID) REFERENCES POTWORD(ID) "
			+ 	"ON UPDATE CASCADE "
			+ 	"ON DELETE CASCADE,"
			+ "FOREIGN KEY (QUESTIONID) REFERENCES QUESTION(ID) "
			+ 	"ON UPDATE CASCADE "
			+ 	"ON DELETE CASCADE);";
	
/*	String createSubjectPot = "CREATE TABLE SUBJECTPOT("
			+ "SUBJECTID TEXT COLLATE NOCASE,"
			+ "WORDID INTEGER,"
			+ "PRIMARY KEY (SUBJECTID, WORDID),"
			+ "FOREIGN KEY (SUBJECTID) REFERENCES SUBJECT(ID) "
			+ 	"ON UPDATE CASCADE "
			+ 	"ON DELETE CASCADE,"
			+ "FOREIGN KEY (WORDID) REFERENCES POTWORD(ID) "
			+ 	"ON UPDATE CASCADE "
			+ 	"ON DELETE CASCADE);";*/
	
	String createTestQuestions = "CREATE TABLE TESTQUESTIONS("
			+ "TESTID TEXT COLLATE NOCASE,"
			+ "QUESTIONID TEXT COLLATE NOCASE,"
			+ "ANSWERGIVEN TEXT COLLATE NOCASE,"
			+ "PRIMARY KEY (TESTID,QUESTIONID),"
			+ "FOREIGN KEY (TESTID) REFERENCES TEST(ID) "
			+ 	"ON UPDATE CASCADE "
			+ 	"ON DELETE CASCADE,"
			+ "FOREIGN KEY (QUESTIONID) REFERENCES QUESTION(ID) "
			+ 	"ON UPDATE CASCADE "
			+ 	"ON DELETE CASCADE);";
	
	String createTestOptionDates = "CREATE TABLE TESTOPTIONDATES("
			+ "TESTID TEXT COLLATE NOCASE PRIMARY KEY,"
			+ "BETWEENDATE TEXT,"
			+ "ANDDATE TEXT,"
			+ "FOREIGN KEY (TESTID) REFERENCES TEST(ID) "
			+ 	"ON UPDATE CASCADE "
			+ 	"ON DELETE CASCADE);";
	
	String createTestOptionAuthors = "CREATE TABLE TESTOPTIONAUTHORS("
			+ "TESTID TEXT COLLATE NOCASE PRIMARY KEY,"
			+ "AUTHORID INTEGER, "
			+ "FOREIGN KEY (TESTID) REFERENCES TEST(ID) "
			+ 	"ON UPDATE CASCADE "
			+ 	"ON DELETE CASCADE,"
			+ "FOREIGN KEY (AUTHORID) REFERENCES USER(ID) "
			+ 	"ON UPDATE CASCADE "
			+ 	"ON DELETE CASCADE);";
	
	String createTestOptionTypes = "CREATE TABLE TESTOPTIONTYPES("
			+ "TESTID TEXT COLLATE NOCASE,"
			+ "TYPE INTEGER,"
			+ "PRIMARY KEY (TESTID,TYPE),"
			+ "FOREIGN KEY (TESTID) REFERENCES TEST(ID) "
			+ 	"ON UPDATE CASCADE "
			+ 	"ON DELETE CASCADE);";
	//----------------------------------------------------------------------
	//	END OF SQL COMMANDS
	//----------------------------------------------------------------------
	
	//----------------------------------------------------------------------
	//	CONSTRUCTOR
	//----------------------------------------------------------------------
	public DBaseCreation(Connection connector, String DBPath){
		conn = connector;
	}//end of DBaseCreation

	public void createAllDBTables() throws SQLException{
		
	//	System.out.println("DBC_UPDATE: Activating Foreign Keys");
	//	Database.createOrUpdate(foreignKey);
		
		System.out.println("DBC_UPDATE: Creating SysRun Table");
		Database.createOrUpdate(createSysRun);
		
		System.out.println("DBC_UPDATE: Creating User Table");
		Database.createOrUpdate(createUser);
		
		System.out.println("DBC_UPDATE: Creating Question Table");
		Database.createOrUpdate(createQuestion);
		
		System.out.println("DBC_UPDATE: Creating Subject Table");
		Database.createOrUpdate(createSubject);
		
		System.out.println("DBC_UPDATE: Creating Test Table");
		Database.createOrUpdate(createTest);
		
		System.out.println("DBC_UPDATE: Creating PotWord Table");
		Database.createOrUpdate(createPotWord);
		
		System.out.println("DBC_UPDATE: Creating UserStats Table");
		Database.createOrUpdate(createUserStats);
		
		System.out.println("DBC_UPDATE: Creating QuestionSubject Table");
		Database.createOrUpdate(createQuestionSubject);

		System.out.println("DBC_UPDATE: Creating QuestionImage Table");
		Database.createOrUpdate(createQuestionImage);
		
		System.out.println("DBC_UPDATE: Creating QuestionAnswer Table");
		Database.createOrUpdate(createQuestionAnswer);
		
		System.out.println("DBC_UPDATE: Creating QuestionDetails Table");
		Database.createOrUpdate(createQuestionDetails);
		
		System.out.println("DBC_UPDATE: Creating QuestionDecoys Table");
		Database.createOrUpdate(createQuestionDecoys);
		
		//System.out.println("DBC_UPDATE: Creating SubjectPot Table");
		//Database.createOrUpdate(createSubjectPot);
		
		System.out.println("DBC_UPDATE: Creating TestQuestions Table");
		Database.createOrUpdate(createTestQuestions);

		System.out.println("DBC_UPDATE: Creating TestOptionDate Table");
		Database.createOrUpdate(createTestOptionDates);
		
		System.out.println("DBC_UPDATE: Creating TestOptionAuthors Table");
		Database.createOrUpdate(createTestOptionAuthors);
		
		System.out.println("DBC_UPDATE: Creating TestOptionTypes Table");
		Database.createOrUpdate(createTestOptionTypes);
		
		//Insert the first run into table
		//Note that this could be done by calling the run
		//String date = Database.sqlStringPasser(new Dated().toSqlString());
		//createOrUpdate("INSERT INTO SYSRUN VALUES(1,"+ date +")");
	}
	
	//----------------------------------------------------------------------
	//	BACKUP DATABASE TO NAMED FILE
	//----------------------------------------------------------------------
	public void backupDatabase(String path, String fileName){

		try{
			File dbFile = new File(path,fileName);
			stmt = conn.createStatement();
			System.out.println("DBC_UPDATE: Backing up data to " + dbFile.getAbsolutePath());
			stmt.executeUpdate("backup to " + dbFile.getAbsolutePath());
		}
		catch(Exception e){
			Message.informUser(null,"Database BackUp Error!", Database.errorToString(e));
			//e.printStackTrace();
		}
	}
	
	//----------------------------------------------------------------------
	//	RESTORE DATABASE FROM NAMED FILE
	//----------------------------------------------------------------------
	public void restoreDatabase(String path, String fileName){	

		try{
			//Class.forName("org.sqlite.JDBC");
			//Connection conn = DriverManager.getConnection("jdbc:sqlite:"+path+"/"+fileName);
			File dbFile = new File(path,fileName);
			stmt = conn.createStatement();
			System.out.println("DBC_UPDATE: Restoring data from " + dbFile.getAbsolutePath());
			stmt.executeUpdate("restore from " + dbFile.getAbsolutePath());
		}
		catch(Exception e){
			Message.informUser(null,"Database Restore Error!", Database.errorToString(e));;
			//e.printStackTrace();
		}
	}
	
	//swap(Currenttable name , new table specs){
	//createOrUpdate("Drop table QuestionSubject");
	
	/*String new TablecreateQuestionSubject = "CREATE TABLE QUESTIONSUBJECT("
			+ "SUBJECTID TEXT COLLATE NOCASE,"
			+ "QUESTIONID TEXT COLLATE NOCASE UNIQUE,"
			+ "PRIMARY KEY (SUBJECTID,QUESTIONID),"
			+ "FOREIGN KEY (SUBJECTID) REFERENCES SUBJECT(ID) "
			+ 	"ON UPDATE CASCADE "
			+ 	"ON DELETE CASCADE,"
			+ "FOREIGN KEY (QUESTIONID) REFERENCES QUESTION(ID) "
			+ 	"ON UPDATE CASCADE "
			+ 	"ON DELETE CASCADE);";*/
	//}
}
