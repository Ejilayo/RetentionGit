/*
 * Filename: Database.java
 * Author: Ye Ming Liang, Jinhwan Kim, Ejilayomi Ayomipo Mimiko
 * Copyright and Licensing: 
 * 
 * Revision History:
 * 
 * Revision      | Status                      | Publication/Revision Date   | By
 * ------------------------------------------------------------------------------------------------------
 * 1.0           | Created                     | Mar. 17, 2014               | Ye Ming Liang, Jinhwan Kim, Ejilayomi Ayomipo Mimiko
 * ------------------------------------------------------------------------------------------------------
 * 
 * You should run this program on a CSIL Windows system
 *
 * Please create a user DSN: myDSN prior to run this program
 * 
 */


package ca.StreamlinedGradingSystem;

import java.sql.*;
import java.util.*;
import java.io.IOException;
import java.io.PrintWriter;

public class Database {

	// Class Description
	//		- Database class is responsible for all queries and updates of the database
	//		- all methods are static in this class
	//
	// Class Attributes
	private static Connection conn;						//database connection
	private static PreparedStatement pstmt = null;		//prepared SQL statement
	

	
//------------------------------------------------------------------------------------------//
//----------------------------------- Public Methods ---------------------------------------//
//------------------------------------------------------------------------------------------//

	// Take username and password, then return a User object contains all courses,
	//  activities, and other related materials depending on the account type
	public static User login(String username, String password)
	{
		//The account that will be returned, containing an user object
		User loginUser = null; 
		
		ResultSet rs = queryTable("SELECT * FROM Account INNER JOIN [User] ON Account.ID=[User].ID "
									+ "WHERE Username='" + username + "' AND Password='" + password + "' AND Locked='No'");
		try
		{
			if (rs.next()) //username found AND password correct
			{
					// Account and User attributes
					String accountType = rs.getString("AccountType");
					String id = rs.getString("ID");
					String firstName = rs.getString("FirstName");
					String lastName = rs.getString("LastName");
					
					// System Administrator
					if (accountType.equals("System Administrator"))
					{
						ArrayList<Account> accountList = getAllAccounts();
						loginUser = new SystemAdministrator(firstName,lastName,id,accountList);
					}
					//Administrative Assistant
					else if (accountType.equals("Administrative Assistant"))
					{
						ArrayList<Course> courseList = getAllCourses();
						ArrayList<User> taList = getAllTAsOrInstructors("TA");
						ArrayList<User> instructorList = getAllTAsOrInstructors("Instructor");
						loginUser = new AdminAssistant(firstName,lastName,id,courseList,taList,instructorList);
					}
					//Instructor
					else if (accountType.equals("Instructor"))
					{
						ArrayList<Course> instructorCourses = getInstructorCourses(id);
						loginUser = new Instructor(firstName,lastName,id,instructorCourses);
					}
					//TA
					else if (accountType.equals("TA"))
					{
						ArrayList<Course> taCourses = getTACourses(id);
						loginUser = new MarkerAndTA(firstName,lastName,id,accountType,taCourses);
					}
					//Academic administrator (NOT DONE)
					else if (accountType.equals("Academic Administrator"))
					{
						ArrayList<Course> courseList = getAllCourses();
						ArrayList<User> taList = getAllTAsOrInstructors("TA");
						ArrayList<User> instructorList = getAllTAsOrInstructors("Instructor");
						loginUser = new AdminAssistant(firstName,lastName,id,courseList,taList,instructorList);
					}
					
					//Close connection and return account
					rs.close();
					closeConnection();
					return loginUser;

			}
			else		  //username NOT found OR password incorrect, just return null
			{
				rs.close();
				closeConnection();
				return loginUser;
			}
			
		}catch (SQLException se)
			{
				System.out.println("\nSQL Exception occurred, the state : "+
								se.getSQLState()+"\nMessage:\n"+se.getMessage()+"\n");
				return null;
			}
	}//end of login()
	
//------------------------------------------------------------------------------------------//
	
	public static void createAccount(String username, String password, String accountType,
									 String locked, String id, String firstName, String lastName)
	{
		try
		{
			//First, add user in User table if not already exists
			insertNewUserToTable(id,firstName,lastName);

			//Then, check if account already exists in the Account Table
			ResultSet rs = queryTable("SELECT COUNT(*) FROM Account WHERE Username='" + username + "'");
			rs.next();
			if (rs.getInt(1) == 0)//NOT exist, add new account
			{
				rs.close();
				closeConnection();
				updateTable("INSERT INTO Account(Username,Password,AccountType,Locked,ID) " +
							"VALUES ('" + username + "','" + password + "','" + accountType +
							"','" + locked + "','" + id + "')");
			}

		}catch (SQLException se)
			{
				System.out.println("\ncreateAccount() - SQL Exception occurred, the state : "+
								se.getSQLState()+"\nMessage:\n"+se.getMessage()+"\n");
				return;
			}

	}//end of createAccount()

//------------------------------------------------------------------------------------------//

	public static void deleteAccount(String username, String id, String firstName, String lastName)
	{
		modifyUser(id, "FirstName",firstName);
		modifyUser(id, "LastName",lastName);
		updateTable("DELETE FROM Account WHERE Username='" + username + "'");
		cleanUserCourseTables();
	}//end of deleteAccount()

//------------------------------------------------------------------------------------------//

	public static void createCourse(String courseNo,
									String courseTitle,
								 	String semester,
									String startDate,
									String endDate,
									String instructorID,
									String firstName, //instructor's first & last name
									String lastName,
									ArrayList<MarkerAndTA> taList)
	{
		try
		{
			//First, add instructor in User table if not already exists
			//insertNewUserToTable(instructorID,firstName,lastName);

			//Then, check if course already exists in the Course Table
			ResultSet rs = queryTable("SELECT COUNT(*) FROM Course WHERE Course#='" + courseNo + "'");
			rs.next();
			if (rs.getInt(1) == 0)//NOT exist, add new course
			{
				updateTable("INSERT INTO Course(Course#,CourseTitle) " +
							"VALUES ('" + courseNo + "','" + courseTitle + "')");
			}
			rs.close();
			closeConnection();
			
			//Then, check if course offering already exists in the Course Offering Table
			rs = queryTable("SELECT COUNT(*) FROM CourseOffering WHERE Semester='" + semester 
					   + "' AND Course#='" + courseNo + "'");
			rs.next();
			if (rs.getInt(1) == 0)//NOT exist, add new course offering and TAs
			{
				//new course offering
				updateTable("INSERT INTO CourseOffering " + "VALUES ('" 
							+ semester + "','" + courseNo
							+ "','" + startDate + "','" + endDate + "','" 
							+ instructorID + "')");
				
				
				//TAs
				for (int i=0; i<taList.size(); i++)
				{
					//insertNewUserToTable(taList.get(i).getEmployeeNumber(),taList.get(i).getFirstName(),
					//					 taList.get(i).getLastName());
					updateTable("INSERT INTO TAOfCourseOffering " + "VALUES ('" 
								+ semester + "','" + courseNo
								+ "','" + taList.get(i).getEmployeeNumber() + "')");
				}
			}
			rs.close();
			closeConnection();

		}catch (SQLException se)
			{
				System.out.println("\ncreateCourse() - SQL Exception occurred, the state : "+
								se.getSQLState()+"\nMessage:\n"+se.getMessage()+"\n");
				return;
			}

	}//end of createCourse()
	
//------------------------------------------------------------------------------------------//

	//Delete Course
	public static void deleteCourse(String semester, String courseNo, boolean modify)
	{
		try
		{
			//Check if the course has any activity
			ResultSet rs = queryTable("SELECT COUNT(*) FROM Activity WHERE Semester='" + semester 
									  + "' AND Course#='" + courseNo + "'");
			rs.next();
			if (rs.getInt(1) == 0 || modify == true)//NOT exist OR modifying (delete & re-create)
			{
				updateTable("DELETE FROM CourseOffering WHERE Semester='" + semester 
						+ "' AND Course#='" + courseNo + "'");

				updateTable("DELETE FROM TAOfCourseOffering WHERE Semester='" + semester 
						+ "' AND Course#='" + courseNo + "'");

				if (modify != true){ // DON'T remove the list of students if modifying
					updateTable("DELETE FROM StudentOfCourseOffering WHERE Semester='" + semester 
							+ "' AND Course#='" + courseNo + "'");
				}

				cleanUserCourseTables();
			}
			rs.close();
			closeConnection();

		}catch (SQLException se)
			{
				System.out.println("\ndeleteCourse() - SQL Exception occurred, the state : "+
								se.getSQLState()+"\nMessage:\n"+se.getMessage()+"\n");
				return;
			}
		
	}//end of deleteCourse()

//------------------------------------------------------------------------------------------//

	public static void createActivity(String semester,
									  String courseNo,
									  String activityNo,
									  String activityName,
									  String activityType,
									  String language,
									  String dueDate,
									  String groupIndividual,
									  String solutionPath,
									  String solutionFormat,
									  String testFilePath,
									  String testFileFormat,
									  String submissionPath,
									  String submissionFormat,
									  String penaltyBonusID,
									  int maxGrade,
									  Rubric rubric)
	{
		try
		{
			//Check if activity already exists in the Course Table
			ResultSet rs = queryTable("SELECT COUNT(*) FROM Activity WHERE Semester='" + semester 
									  + "' AND Course#='" + courseNo +"' AND Activity#='" + activityNo + "'");
			rs.next();
			if (rs.getInt(1) == 0)//NOT exist, add new course
			{
				updateTable("INSERT INTO Activity " +
							"VALUES ('" + semester + "','" + courseNo + "','" + activityNo + "','" + activityName
							 + "','" + activityType + "','" + language + "','" + dueDate + "','" + groupIndividual
							 + "','" + solutionPath + "','" + solutionFormat + "','" + testFilePath + "','" + testFileFormat + "','" + 
							 submissionPath + "','" + submissionFormat + "'," + penaltyBonusID + "," + maxGrade + ")");
			}
			rs.close();
			closeConnection();

		}catch (SQLException se)
			{
				System.out.println("\ncreateActivity() - SQL Exception occurred, the state : "+
								se.getSQLState()+"\nMessage:\n"+se.getMessage()+"\n");
				return;
			}
		if (rubric != null) {
			createRubric(semester,courseNo,activityNo,rubric);
		}
		
	}//end of createActivity()

//------------------------------------------------------------------------------------------//

	//Delete Activity
	public static void deleteActivity(String semester, String courseNo, String activityNo)
	{

		updateTable("DELETE FROM Activity WHERE Semester='" + semester 
					+ "' AND Course#='" + courseNo +"' AND Activity#='" + activityNo + "'");
		
		deleteRubric(semester, courseNo, activityNo);
	}//end of deleteActivity()

//------------------------------------------------------------------------------------------//

	//Create a Rubric
	public static void createRubric(String semester, String courseNo, String activityNo, Rubric newRubric)
	{
		try
		{
			//Check if rubric already exists for that activity.
			ResultSet rs = queryTable("SELECT COUNT(*) FROM Rubric WHERE Semester='" + semester 
									  + "' AND Course#='" + courseNo +"' AND Activity#='" + activityNo + "'");
			rs.next();
			if (rs.getInt(1) == 0)//NOT exist, add new rubric
			{
				
				for (int i=0; i < newRubric.getNumberOfRubricItems(); i++)
				{
					updateTable("INSERT INTO Rubric " +
							"VALUES ('" + semester + "','" + courseNo + "','" + activityNo + "','" + Integer.toString(i+1) + "','" 
							+ newRubric.getRubricItems().get(i).getRubricDescription() + "','" + newRubric.getRubricItems().get(i).getMaxPoints() + "')");
				}
				
			}
			rs.close();
			closeConnection();

		}catch (SQLException se)
			{
				System.out.println("\ncreateRubric() - SQL Exception occurred, the state : "+
								se.getSQLState()+"\nMessage:\n"+se.getMessage()+"\n");
				return;
			}

	}//end of createRubric()
	
//------------------------------------------------------------------------------------------//

	//Delete Rubric
	public static void deleteRubric(String semester, String courseNo, String activityNo)
	{
		updateTable("DELETE FROM Rubric WHERE Semester='" + semester 
				+ "' AND Course#='" + courseNo +"' AND Activity#='" + activityNo + "'");
	
	}//end of deleteRubric()

//------------------------------------------------------------------------------------------//
	
	public static ArrayList<Course> getAllCourses()
	{
		//Connect
		connectToDatabase();

		//SQL statement
		String sSQL= "SELECT * FROM Course INNER JOIN CourseOffering " +
				 		"ON Course.Course#=CourseOffering.Course#";

		//Attributes
		String semester = "";
		String courseNo = "";
		String courseTitle = "";
		String startDate = "";
		String endDate = "";
		String instructorID = "";
		
		//ArrayList of accounts, activities
		ArrayList<Course> courses = new ArrayList<Course>();
		ArrayList<MarkerAndTA> tas = new ArrayList<MarkerAndTA>();

		//Prepare and execute SQL statement
		try
		{
			pstmt = conn.prepareStatement(sSQL);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				courseNo = rs.getString(1);
				courseTitle = rs.getString(2);
				semester = rs.getString(3);
				startDate = rs.getString(5);
				endDate = rs.getString(6);
				instructorID  = rs.getString(7);
				
				tas = getTAs(courseNo, semester);
				Course tempCourse = new Course(semester, courseTitle, courseNo, startDate, endDate, instructorID, tas, null, null);
				courses.add(tempCourse);
			}
			rs.close();
		}catch (SQLException se)
			{
				System.out.println("\ngetAllCourses() - SQL Exception occurred, the state : "+
								se.getSQLState()+"\nMessage:\n"+se.getMessage()+"\n");
				return courses;
			}

		//Close connection
		closeConnection();
		
		return courses;

	}//end of getAllCourses()	
	
//------------------------------------------------------------------------------------------//

	// Import a list of students to the database for a course
	// - if a new list is imported, existing students stay there and new students would be
	//   added to the course too
	public static void importStudents(String semester, String courseNo, ArrayList<Student> studentList)
	{
		for (int i=0; i<studentList.size(); i++)
		{
			String firstName = studentList.get(i).getStudentFirstName();
			String lastName = studentList.get(i).getStudentLastName();
			String stdID = studentList.get(i).getStudentID();
			try
			{
				//First, add student in Student table if not already exists
				insertNewStudentToTable(stdID,firstName,lastName);
	
				//Then, check if student already exists in that course offering
				ResultSet rs = queryTable("SELECT COUNT(*) FROM StudentOfCourseOffering WHERE Semester='"
										  + semester + "' AND Course#='" + courseNo + "' AND StudentID='" 
										  + stdID + "'");
				rs.next();
				if (rs.getInt(1) == 0)//NOT exist, add new student to that course offering
				{
					rs.close();
					closeConnection();
					updateTable("INSERT INTO StudentOfCourseOffering(Semester, Course#, StudentID) " +
								"VALUES ('" + semester + "','" + courseNo + "','" + stdID + "')");
				}
	
			}catch (SQLException se)
				{
					System.out.println("\nimportStudents() - SQL Exception occurred, the state : "+
									se.getSQLState()+"\nMessage:\n"+se.getMessage()+"\n");
					return;
				}
		}

	}//end of importStudents()
	
//------------------------------------------------------------------------------------------//

	// After grading, this function is called to save the grades into the database
	public static void saveGrades(String semester, String courseNo, String activityNo, GradingRubric gradingRubric)
	{
		String stdID = gradingRubric.getStudent().getStudentID();
		Rubric rubric = gradingRubric.getRubric();
		ArrayList<RubricItem> rubricItemList = rubric.getRubricItems();
		
		for (int i=0; i<rubricItemList.size(); i++)
		{

			//First, remove the old grades
			updateTable("DELETE FROM StudentGrade WHERE StudentID='" + stdID + "' AND Semester='"
						+ semester + "' AND Course#='" + courseNo + "' AND Activity#='" + activityNo
						+ "' AND SubSection#='" + Integer.toString(i+1) + "'");

			//Then, insert the new grades
			updateTable("INSERT INTO StudentGrade(StudentID,Semester,Course#,Activity#,SubSection#,ReceivedGrade)"
						+ " VALUES ('" + stdID + "','" + semester + "','" + courseNo + "','" + activityNo + "','"
						+ Integer.toString(i+1) + "'," + rubricItemList.get(i).getGrade() + ")"); 
		}


	}//end of saveGrades()
	
//------------------------------------------------------------------------------------------//	

	// For modifying the username, password, lock, etc. 
	public static void modifyAccount(String username,
									 String column,   //the table column that you want to modify
									 String newValue)
		{
		updateTable("UPDATE [Account] SET " + column + "='" + newValue +
				"' WHERE Username='" + username + "'");
		}//end of modifyUser()
	
//------------------------------------------------------------------------------------------//

	public static String getCourseName(String courseNo)
	{
		//ArrayList of TAs
		String sSQL = "SELECT CourseTitle " +
						"FROM Course " +
						"WHERE Course#='" + courseNo + "'";
		
		//TA attributes
		String courseName = "";
		
		//Connect
		connectToDatabase();
		
		try
		{
			pstmt = conn.prepareStatement(sSQL);
			ResultSet rs = pstmt.executeQuery();
			
			rs.next();
			courseName = rs.getString("CourseTitle");
			rs.close();
			
		}catch (SQLException se)
			{
				System.out.println("\ngetCourseName() - SQL Exception occurred, the state : "+
								se.getSQLState()+"\nMessage:\n"+se.getMessage()+"\n");
				return courseName;
			}

		//Close connection
		closeConnection();
		
		return courseName;
	}//end of getCourseName
	
//------------------------------------------------------------------------------------------//

	public static String[] getInstructorNames(String id)
	{
		//ArrayList of TAs
		String sSQL = "SELECT FirstName, LastName " +
						"FROM [User] " +
						"WHERE ID='" + id + "'";
		
		//TA attributes
		String[] names = new String[2];
		
		//Connect
		connectToDatabase();
		
		try
		{
			pstmt = conn.prepareStatement(sSQL);
			ResultSet rs = pstmt.executeQuery();
			
			rs.next();
			names[0] = rs.getString("FirstName");
			names[1] = rs.getString("LastName");
			rs.close();
			
		}catch (SQLException se)
			{
				System.out.println("\ngetInstructorNames() - SQL Exception occurred, the state : "+
								se.getSQLState()+"\nMessage:\n"+se.getMessage()+"\n");
				return names;
			}

		//Close connection
		closeConnection();
		
		return names;
	}//end of getInstructorNames
	
//------------------------------------------------------------------------------------------//

	public static String getLocked(String username)		 
		{
		
		String sSQL = "SELECT Locked " +
				"FROM Account " +
				"WHERE Username='" + username + "'";
		
		String locked = "";
		
		connectToDatabase();
		
		try
		{
			pstmt = conn.prepareStatement(sSQL);
			ResultSet rs = pstmt.executeQuery();
			
			if (rs.next())
			{
				locked = rs.getString("Locked");
			}
			rs.close();
			
		}catch (SQLException se)
			{
				System.out.println("\ngetLocked() - SQL Exception occurred, the state : "+
								se.getSQLState()+"\nMessage:\n"+se.getMessage()+"\n");
				return locked;
			}
		
		closeConnection();
		
		return locked;
		
	}//end of getLocked()
	
//------------------------------------------------------------------------------------------//
	
	public static String getUsername(String firstName, String lastName, String userType)
	{

	String username = null; 
	ResultSet rs = queryTable("SELECT * FROM Account INNER JOIN [User] ON Account.ID=[User].ID "
		+ "WHERE FirstName='" + firstName + "' AND LastName='" + lastName + "' AND AccountType='" + userType + "'");
	try
	{
		if (rs.next())
		{
			username = rs.getString("Username");

			rs.close();
			closeConnection();
			return username;
		}
		else
		{
			rs.close();
			closeConnection();
			return username;
		}
		
	}catch (SQLException se)
	{
		System.out.println("\ngetUsername() - SQL Exception occurred, the state : "+
				se.getSQLState()+"\nMessage:\n"+se.getMessage()+"\n");
		return null;
	}

}//end of getUsername()
	
//------------------------------------------------------------------------------------------//

	public static String getStudentName(String studentID)
	{

	String fullName = null; 
	ResultSet rs = queryTable("SELECT * FROM [Student]"
		+ "WHERE StudentID='" + studentID + "'");
	try
	{
		if (rs.next())
		{
			fullName = rs.getString(2) +" "+rs.getString(3);//First Name, Last Name

			rs.close();
			closeConnection();
			return fullName;
		}
		else
		{
			rs.close();
			closeConnection();
			return fullName;
		}
		
	}catch (SQLException se)
	{
		System.out.println("\ngetStudentName() - SQL Exception occurred, the state : "+
				se.getSQLState()+"\nMessage:\n"+se.getMessage()+"\n");
		return null;
	}

}//end of getStudentName()

//------------------------------------------------------------------------------------------//

	public static String getPassword(String username)		 
	{
	
	String sSQL = "SELECT Password " +
			"FROM Account " +
			"WHERE Username='" + username + "'";
	
	String pass = "";
	
	connectToDatabase();
	
	try
	{
		pstmt = conn.prepareStatement(sSQL);
		ResultSet rs = pstmt.executeQuery();
		
		if (rs.next())
		{
			pass = rs.getString("Password");
		}
		rs.close();
		
	}catch (SQLException se)
		{
			System.out.println("\ngetPassword() - SQL Exception occurred, the state : "+
							se.getSQLState()+"\nMessage:\n"+se.getMessage()+"\n");
			return pass;
		}
	
	closeConnection();
	
	return pass;
	
	}//end of getPassword()
	
//------------------------------------------------------------------------------------------//

	// Get a list of grading rubrics for all students for an activity of an course
	// - a grading rubric is the same as a blank rubric, but with the student's received grades in it
	public static ArrayList<GradingRubric> getGradingRubrics(String semester, String courseNo, String activityNo)
	{
		ArrayList<GradingRubric> gradingRubrics = new ArrayList<GradingRubric>();
		ArrayList<Student> students = new ArrayList<Student>();
		students = getAllStudentsInCourse(semester, courseNo);
				
		String subsection = "";
		float receivedGrade = -1;
		int maxGrade= -1;
		
		//Connect
		connectToDatabase();
				
		//Prepare and execute SQL statement
		for (int i=0; i<students.size(); i++)
		{	
			String sSQL="SELECT sub.StudentID,sub.Semester,sub.Course#,sub.Activity#,sub.SubSection#, "+
						"sub.SubSection,sub.SubSectionMaxGrade,StudentGrade.ReceivedGrade "+
						"FROM StudentGrade RIGHT JOIN "+
							"(SELECT StudentOfCourseOffering.StudentID,Rubric.Semester,Rubric.Course#, "+
							"Rubric.Activity#,Rubric.SubSection#,Rubric.SubSection,Rubric.SubSectionMaxGrade "+
							"FROM StudentOfCourseOffering INNER JOIN Rubric "+
							"ON StudentOfCourseOffering.Course#=Rubric.Course# "+
							"AND StudentOfCourseOffering.Semester=Rubric.Semester "+
							"WHERE StudentOfCourseOffering.StudentID='"+students.get(i).getStudentID()+"' "+
							"AND StudentOfCourseOffering.Semester='"+semester+"' "+
							"AND StudentOfCourseOffering.Course#='"+courseNo+"' "+
							"AND Rubric.Activity#='"+activityNo+"') sub "+
						"ON StudentGrade.StudentID=sub.StudentID "+
						"AND StudentGrade.Semester=sub.Semester "+
						"AND StudentGrade.Course#=sub.Course# "+
						"AND StudentGrade.Activity#=sub.Activity# "+
						"AND StudentGrade.SubSection#=sub.SubSection#";
			Rubric tempRubric = new Rubric();
			
			try
			{
				pstmt = conn.prepareStatement(sSQL);
				ResultSet rs = pstmt.executeQuery();

				while (rs.next()) {
					subsection = rs.getString("SubSection");
					maxGrade = rs.getInt("SubSectionMaxGrade");
					receivedGrade = rs.getFloat("ReceivedGrade");
					
					tempRubric.addRubricItem(subsection, maxGrade);
					tempRubric.getRubricItems().get(tempRubric.getNumberOfRubricItems()-1).setGrade(receivedGrade);
				}
				GradingRubric oneStudent = new GradingRubric();
				oneStudent.setStudent(students.get(i));
				oneStudent.setRubric(tempRubric);
				gradingRubrics.add(oneStudent);
				rs.close();
			}catch (SQLException se)
				{
					System.out.println("\ngetGradingRubrics() - SQL Exception occured, the state : "+
									se.getSQLState()+"\nMessage:\n"+se.getMessage()+"\n");
					return gradingRubrics;
				}
		}
	
		//Close connection
		closeConnection();
		return gradingRubrics;
				
	}//end of getGradingRubrics();
		
//------------------------------------------------------------------------------------------//

	// Backup database to a .bak file
	// - Because the SQL server can only access the \\cypress directory, we can't let the user
	//   to choose their own directory, but they can specify a file name  for the backup file
	public static void backupDatabase(String filename)
	{
		updateTable("BACKUP DATABASE c275g04A TO DISK = '\\\\cypress\\userdata\\c275g4\\backup\\"+filename+".bak'");
		
	}//end of backupDatabase()
	
//------------------------------------------------------------------------------------------//

	// Restore database from a .bak file
	// - we don't have the permission to restore a database, but if we do it should work
	public static void restoreDatabase(String filename)
	{
		updateTable("RESTORE DATABASE c275g04A FROM DISK = '\\\\cypress\\userdata\\c275g4\\backup\\"+filename+".bak'");
		
	}//end of backupDatabase()
	
//------------------------------------------------------------------------------------------//

	// Output the grades of students of an activity to a file
	//  - format: std_id, first_name, last_name, <grades for each sub-section of the rubric>
	public static void generateCSV(String path, String semester, String courseNumber, String activityNumber )
	{
		String result = "";
	
		String studentID = "";
		String firstName = "";
		String lastName = "";
		int rubric1Column = 7;
		
		//Prepare and execute SQL statement
		
		String sSQL = "Declare @rubric nvarchar(max),@query  nvarchar(max) "+
				"select @rubric = STUFF((Select Distinct ',' + QUOTENAME(sg1.SubSection#) " +
				"from ("+
						"select std.FirstName,std.LastName, sg.* "+
						"from StudentGrade sg inner join Student std on sg.StudentID = std.StudentID " +
						"where sg.Course# = '"+courseNumber+"' and sg.Semester = '"+semester+"' and sg.Activity# = '"+activityNumber+"' "+
				") as sg1 FOR XML PATH(''), TYPE).value('.','nvarchar(max)'),1,1,'') "	+
				"set @query = N'Select * from (select Student.FirstName,Student.LastName, StudentGrade.*from StudentGrade  inner join Student on "+
				"StudentGrade.StudentID = Student.StudentID where StudentGrade.Course# = ''"+courseNumber+"'' and StudentGrade.Semester = ''"
				+semester+"'' and StudentGrade.Activity# = ''"+activityNumber+"''"+
				") rTab pivot (sum(rTab.ReceivedGrade) for rTab.SubSection# in ('+ @rubric +')) as RubricTable' "+
				"execute (@query)";
		
		connectToDatabase();
			try
			{
				PrintWriter printToFile = new PrintWriter(path);				
				pstmt = conn.prepareStatement(sSQL);
				
				ResultSet rs = pstmt.executeQuery();
				ResultSetMetaData rsmd = rs.getMetaData();
				int columnCount = rsmd.getColumnCount();
				
				while (rs.next()) {
					
					firstName = rs.getString(1);
					lastName = rs.getString(2);
					studentID = rs.getString(3);
					
					result = studentID + "," + firstName +","+ lastName;
					for(int i = rubric1Column; i<=columnCount; i++)
					{
						int grade = rs.getInt(i);
						String gradeString = Integer.toString(grade);
						result = result +"," +gradeString;// +",";
					}
					printToFile.println(result);
				}
				printToFile.close();
				rs.close();
			}catch (SQLException se)
				{
					System.out.println("\nSQL Exception occured, the state : "+
									se.getSQLState()+"\nMessage:\n"+se.getMessage()+"\n");
				}
			catch (IOException e) {
		    	e.printStackTrace();
		    }
	
		//Close connection
		closeConnection();
	}//end of Generate CSV

//------------------------------------------------------------------------------------------//

	// Return a list of students that are assigned to a particular TA for an activity of a course
	public static ArrayList<Student> getStudentsInCourseForTA(String semester, String courseNo, String activityNo, String taID)
	{
		ResultSet rs;
		
		// First, check if the StudentAssignedToTa table contains records for the given TA
		try
		{
			rs = queryTable("SELECT COUNT(*) FROM StudentAssignedToTA WHERE Semester='" + semester 
					  				  + "' AND Course#='" + courseNo + "' AND Activity#='" + activityNo + "' AND TA_ID='"
					  				  + taID + "'");
			rs.next();
			if (rs.getInt(1) == 0)//NO assignments of students found, just return all students
			{
				rs.close();
				return getAllStudentsInCourse(semester, courseNo);
			}
			rs.close();
		}catch (SQLException se)
		{
			System.out.println("\ngetStudentsInCourseForTA() - SQL Exception occured, the state : "+
							se.getSQLState()+"\nMessage:\n"+se.getMessage()+"\n");
			return null;
		}
		
		// Then, if there are assignments of students in the table, only return a list of students
		//  that are assigned to the given TA
		//Connect
		connectToDatabase();
		
		//SQL statement		
		String sSQL = "SELECT * FROM " + 					 
					  "(SELECT StudentAssignedToTA.Semester,StudentAssignedToTA.Course#, "+
					  		   "StudentAssignedToTA.StudentID " +
					  "FROM StudentOfCourseOffering INNER JOIN StudentAssignedToTA " +
							 "ON StudentOfCourseOffering.StudentID=StudentAssignedToTA.StudentID "+
							 "AND StudentOfCourseOffering.Semester=StudentAssignedToTA.Semester "+
							 "AND StudentOfCourseOffering.Course#=StudentAssignedToTA.Course# "+
							 "WHERE StudentAssignedToTA.Semester='" + semester + "' "+
							 "AND StudentAssignedToTA.Course#='" + courseNo + "' "+
							 "AND Activity#='" + activityNo + "' AND TA_ID='" + taID + "') sub "+
					  "INNER JOIN Student "+
					  "ON sub.StudentID = Student.StudentID";

		//Attributes
		String studentID = "";
		String firstName = "";
		String lastName = "";
		
		//ArrayList of students
		ArrayList<Student> student = new ArrayList<Student>();
		

		//Prepare and execute SQL statement
		try
		{
			pstmt = conn.prepareStatement(sSQL);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				Student std = new Student("", "", "");
				studentID = rs.getString("StudentID");
				firstName = rs.getString("FirstName");
				lastName = rs.getString("LastName");
				
				std.setStudentID(studentID);
				std.setStudentFirstName(firstName);
				std.setStudentLastName(lastName);
				student.add(std);
			}
			rs.close();
		}catch (SQLException se)
			{
				System.out.println("\ngetStudentsInCourseForTA() - SQL Exception occured, the state : "+
								se.getSQLState()+"\nMessage:\n"+se.getMessage()+"\n");
				return student;
			}

		//Close connection
		closeConnection();
		return student;
		
	}//end of getStudentsInCourseForTA()
	
//------------------------------------------------------------------------------------------//

	// Assign a set of students to a particular TA
	public static void assignStudentsToTA(String[] studentIDs, String semester, String courseNo,
										  String activityNo, String taID)
	{
		// remove the existing assignments, if any for that TA
		updateTable("DELETE FROM StudentAssignedToTA WHERE Semester='" + semester 
				+ "' AND Course#='" + courseNo + "' AND Activity#='" + activityNo
				+ "' AND TA_ID='" + taID + "'");
		
		// New assignments of students to the TA
		for (int i=0; i<studentIDs.length; i++)
		{	
			updateTable("INSERT INTO StudentAssignedToTA(StudentID,Semester,Course#,Activity#,TA_ID) " +
						"VALUES ('" + studentIDs[i] + "','" + semester + "','" + courseNo + "','"
						+ activityNo + "','" + taID + "')");
		}

	}//end of assignStudentsToTA()
	
//------------------------------------------------------------------------------------------//

	// Return the assignments of students to TA for displaying
	public static ArrayList<String> getStudentsAssignedToTA(String semester, String courseNo,
										  		   			String activityNo, String taID)
	{
		ArrayList<String> studentIDs = new ArrayList<String>();
		ResultSet rs = queryTable("SELECT StudentID FROM StudentAssignedToTA WHERE Semester='" + semester 
								  + "' AND Course#='" + courseNo + "' AND Activity#='" + activityNo
								  + "' AND TA_ID='" + taID + "'");
		try
		{
			while (rs.next()) {
				studentIDs.add(rs.getString(1));
			}
			rs.close();
		}catch (SQLException se)
			{
				System.out.println("\ndeleteCourse() - SQL Exception occurred, the state : "+
								se.getSQLState()+"\nMessage:\n"+se.getMessage()+"\n");
				return studentIDs;
			}
		closeConnection();
		return studentIDs;
	}//end of getStudentsAssignedToTA()
		
	
	
//------------------------------------------------------------------------------------------//
//---------------------------------- Private Methods ---------------------------------------//
//------------------------------------------------------------------------------------------//

	//Method for connecting to the database
	private static void connectToDatabase()
	{
		//Make sure JDBC-ODBC bridge exists
		try
		{
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
		}catch(ClassNotFoundException ce)
			{
				System.out.println("\n\nNo JDBC-ODBC bridge; exit now.\n\n");
				return;
			}

		//Make connection to the database
		try
		{
			conn = DriverManager.getConnection("jdbc:odbc:myDSN","c275g04","NHeLY6hLhE3eeJQH");
		}catch (SQLException se)
			{
				System.out.println("\n\nNo proper DSN; exit now.\n\n");
				return;
			}
	}//end of connectToDatabase()

//------------------------------------------------------------------------------------------//

	//Method for closing the database connection
	private static void closeConnection()
	{
		//Make connection to the database
		try
		{
			conn.close();
		}catch (SQLException se)
			{
				System.out.println("\ncloseConnection() - SQL Exception occurred, the state : "+
								se.getSQLState()+"\nMessage:\n"+se.getMessage()+"\n");
				return;
			}
	}//end of closeConnection()

//------------------------------------------------------------------------------------------//

	//A generic method for updating all tables, accept a SQL statement as parameter
	//		- can be used for INSERT, UPDATE, DELETE
	private static void updateTable(String sSQL)
	{
		//Connect
		connectToDatabase();

		//Prepare and execute SQL statement
		try
		{
			pstmt = conn.prepareStatement(sSQL);
			pstmt.executeUpdate();

		}catch (SQLException se)
			{
				//System.out.println("\nupdateTable() - SQL Exception occurred, the state : "+
				//				se.getSQLState()+"\nMessage:\n"+se.getMessage()+"\n");
				//return;
			}

		//Close connection
		closeConnection();

	}//end of updateTable()

//------------------------------------------------------------------------------------------//

	//A generic method for querying all tables, accept a SQL statement as parameter
	//		- can be used for SELECT only
	//		- need to close database connection outside this method after finish
	//		  using the result set
	private static ResultSet queryTable(String sSQL)
	{
		//Connect
		connectToDatabase();

		//Prepare and execute SQL statement
		try
		{
			pstmt = conn.prepareStatement(sSQL);
			return pstmt.executeQuery();

		}catch (SQLException se)
			{
				System.out.println("\nqueryTable() - SQL Exception occurred, the state : "+
								se.getSQLState()+"\nMessage:\n"+se.getMessage()+"\n");
				return null;
			}

	}//end of queryTable()

//------------------------------------------------------------------------------------------//

	//A private method for inserting new users into the User table when creating accounts
	//  and courses, if that user does not exist already
	private static void insertNewUserToTable(String id,
									  		 String firstName,
									  		 String lastName)
	{
		try
		{
			//First, need to check if user already exists in the User table
			ResultSet rs = queryTable("SELECT COUNT(*) FROM [User] WHERE ID='" + id + "'");
			rs.next();
			if (rs.getInt(1) == 0)//NOT exist, add new user into the table
			{
				updateTable("INSERT INTO [User](ID,FirstName,LastName) " +
							"VALUES ('" + id + "','" + firstName + "','" + lastName + "')");
			}
			rs.close();
			closeConnection();

		}catch (SQLException se)
		{
			System.out.println("\ninsertNewUserToTable() - SQL Exception occurred, the state : "+
					se.getSQLState()+"\nMessage:\n"+se.getMessage()+"\n");
			return;
		}

	}//end of insertNewUserToTable()
	
//------------------------------------------------------------------------------------------//

	//A private method for inserting new students into the Student table when importing students,
	//  if that student does not exist already
	private static void insertNewStudentToTable(String stdID,
									  		 	String firstName,
									  		 	String lastName)
	{
		try
		{
			//First, need to check if user already exists in the User table
			ResultSet rs = queryTable("SELECT COUNT(*) FROM Student WHERE StudentID='" + stdID + "'");
			rs.next();
			if (rs.getInt(1) == 0)//NOT exist, add new user into the table
			{
				updateTable("INSERT INTO Student(StudentID,FirstName,LastName) " +
							"VALUES ('" + stdID + "','" + firstName + "','" + lastName + "')");
			}
			rs.close();
			closeConnection();

		}catch (SQLException se)
		{
			System.out.println("\ninsertNewStudentToTable() - SQL Exception occurred, the state : "+
					se.getSQLState()+"\nMessage:\n"+se.getMessage()+"\n");
			return;
		}

	}//end of insertNewStudentToTable()
	
//------------------------------------------------------------------------------------------//

	private static void modifyUser(String id,
								   String column,   //the table column that you want to modify
								   String newValue)
	{
		updateTable("UPDATE [User] SET " + column + "='" + newValue +
					"' WHERE ID='" + id + "'");

	}//end of modifyUser()
	
//------------------------------------------------------------------------------------------//

	// Return an ArrayList of account objects for system admin (all accounts in database)
	private static ArrayList<Account> getAllAccounts()
	{
		//Connect
		connectToDatabase();

		//SQL statement
		String sSQL= "SELECT * FROM Account INNER JOIN [User] " +
					 "ON Account.ID=[User].ID";

		//Attributes
		String username = "";
		String password = "";
		String accountType = "";
		String locked = "";
		String id = "";
		String firstName = "";
		String lastName = "";
		
		//ArrayList of accounts
		ArrayList<Account> accounts = new ArrayList<Account>();

		//Prepare and execute SQL statement
		try
		{
			pstmt = conn.prepareStatement(sSQL);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				username = rs.getString("Username");
				password = rs.getString("Password");
				accountType = rs.getString("AccountType");
				locked = rs.getString("Locked");
				id = rs.getString("ID");
				firstName = rs.getString("FirstName");
				lastName = rs.getString("LastName");

				User tempUser = new User(firstName,lastName,id,accountType);
				Account tempAccount = new Account(username, password, locked, tempUser);
				accounts.add(tempAccount);
			}
			rs.close();
		}catch (SQLException se)
			{
				System.out.println("\nmodifyUser() - SQL Exception occurred, the state : "+
								se.getSQLState()+"\nMessage:\n"+se.getMessage()+"\n");
				return accounts;
			}

		//Close connection
		closeConnection();
		
		return accounts;

	}//end of getAllAccounts()

//------------------------------------------------------------------------------------------//

	//Returns a list of all students in that course.	
	private static ArrayList<Student> getAllStudentsInCourse(String semester, String courseNo)
	{
		//Connect
		connectToDatabase();

		//SQL statement
		String sSQL= "SELECT * FROM StudentOfCourseOffering INNER JOIN [Student] " +
					 "ON StudentOfCourseOffering.StudentID = [Student].StudentID " +
					 "Where Semester ='" + semester + "' and course# = '" + courseNo + "'"; 

		//Attributes
		String studentID = "";
		String firstName = "";
		String lastName = "";
		
		//ArrayList of students
		ArrayList<Student> student = new ArrayList<Student>();
		

		//Prepare and execute SQL statement
		try
		{
			pstmt = conn.prepareStatement(sSQL);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				Student std = new Student("", "", "");
				studentID = rs.getString("StudentID");
				firstName = rs.getString("FirstName");
				lastName = rs.getString("LastName");
				
				std.setStudentID(studentID);
				std.setStudentFirstName(firstName);
				std.setStudentLastName(lastName);
				student.add(std);
				
			}
			rs.close();
		}catch (SQLException se)
			{
				System.out.println("\ngetAllStudentsInCourse() - SQL Exception occured, the state : "+
								se.getSQLState()+"\nMessage:\n"+se.getMessage()+"\n");
				return student;
			}

		//Close connection
		closeConnection();
		
		return student;

	}//end of getAllStudentsInCourse()		

//------------------------------------------------------------------------------------------//

	// Return an ArrayList of course objects when given an ID and accountType
	private static ArrayList<Course> getInstructorCourses(String id)
	{
		//Connect
		connectToDatabase();

		//SQL statement
		String sSQL= "SELECT * FROM Course INNER JOIN CourseOffering " +
				 "ON Course.Course#=CourseOffering.Course# WHERE InstructorID='"
				 + id + "'";

		//Attributes
		String semester = "";
		String courseNo = "";
		String courseTitle = "";
		String startDate = "";
		String endDate = "";
		
		//ArrayList of courses, TAs, students, activities
		ArrayList<Course> courses = new ArrayList<Course>();
		ArrayList<MarkerAndTA> tas = new ArrayList<MarkerAndTA>();
		ArrayList<Activity> activities = new ArrayList<Activity>();
		ArrayList<Student> students = new ArrayList<Student>();
		
		//Prepare and execute SQL statement
		try
		{
			pstmt = conn.prepareStatement(sSQL);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				courseNo = rs.getString(1);
				courseTitle = rs.getString(2);
				semester = rs.getString(3);
				startDate = rs.getString(5);
				endDate = rs.getString(6);
				
				activities = getActivities(semester, courseNo);
				tas = getTAs(courseNo, semester);
				students = getAllStudentsInCourse(semester, courseNo);
				Course tempCourse = new Course(semester, courseTitle, courseNo, startDate, endDate, id, tas, students, activities);
				courses.add(tempCourse);
			}
			rs.close();
		}catch (SQLException se)
			{
				System.out.println("\ngetInstructorCourses() - SQL Exception occurred, the state : "+
								se.getSQLState()+"\nMessage:\n"+se.getMessage()+"\n");
				return courses;
			}

		//Close connection
		closeConnection();
		
		return courses;

	}//end of getInstructorCourses()
	
//------------------------------------------------------------------------------------------//
	
	// Get and return a list of courses that the TA involves with
	private static ArrayList<Course> getTACourses(String id)
	{
		//Connect
		connectToDatabase();

		//SQL statement
		String sSQL= "SELECT * from Course inner join (Select CourseOffering.Semester,CourseOffering.Course#,StartDate,EndDate,InstructorID,TA_ID"
				      + " from CourseOffering inner join TAOfCourseOffering on CourseOffering.Semester=TAOfCourseOffering.Semester and CourseOffering.Course#=TAOfCourseOffering.Course#"
					  + " where TA_ID = '" + id + "') sub on Course.Course#=sub.Course#";
		
		//Attributes
		String semester = "";
		String courseNo = "";
		String courseTitle = "";
		String startDate = "";
		String endDate = "";
		String instructorID = "";
		
		//ArrayList of courses, TAs, students, activities
		ArrayList<Course> courses = new ArrayList<Course>();
		ArrayList<MarkerAndTA> tas = new ArrayList<MarkerAndTA>();
		ArrayList<Activity> activities = new ArrayList<Activity>();
		//ArrayList<Student> students = new ArrayList<Student>();
		
		//Prepare and execute SQL statement
		try
		{
			pstmt = conn.prepareStatement(sSQL);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				courseNo = rs.getString(1);//Course#
				courseTitle = rs.getString(2);//CourseTitle
				semester = rs.getString(3);//Semester
				startDate = rs.getString(5);//StartDate
				endDate = rs.getString(6);//EndDate
				instructorID = rs.getString(7);//InstructorID

				activities = getActivities(semester, courseNo);
				tas = getTAs(courseNo, semester);
				//students = getAllStudentsInCourse(semester, courseNo);
				Course tempCourse = new Course(semester, courseTitle, courseNo, startDate, endDate, instructorID, tas, null,activities);
				courses.add(tempCourse);
			}
			rs.close();
		}catch (SQLException se)
			{
				System.out.println("\ngetTACourses() - SQL Exception occurred, the state : "+
								se.getSQLState()+"\nMessage:\n"+se.getMessage()+"\n");
				return courses;
			}

		//Close connection
		closeConnection();
		
		return courses;

	}//get TA Courses
		
//------------------------------------------------------------------------------------------//

	// Return an ArrayList of activity objects when given an semester, course#
	private static ArrayList<Activity> getActivities(String semester, String courseNo)
	{
		//Connect
		connectToDatabase();

		//SQL statement
		String sSQL= "SELECT * FROM Activity WHERE Semester='" + semester + "' AND Course#='" + courseNo + "'";

		//Attributes
		String activityNo = "";
		String activityName = "";
		String activityType = "";
		String language = "";
		String dueDate = "";
		String groupIndividual = "";
		String solutionPath ="";
		String solutionFormat ="";
		String testFilePath ="";
		String testFileFormat ="";
		String submissionPath ="";
		String submissionFormat ="";
		//String penaltyBonusID = "";
		int maxGrade = -1;
		Rubric rubric = null;

		
		//ArrayList of activities
		ArrayList<Activity> activities = new ArrayList<Activity>();

		//Prepare and execute SQL statement
		try
		{
			pstmt = conn.prepareStatement(sSQL);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				activityNo = rs.getString("Activity#");
				activityName = rs.getString("ActivityName");
				activityType = rs.getString("ActivityType");
				language = rs.getString("Language");
				dueDate = rs.getString("DueDate");
				groupIndividual = rs.getString("GroupOrIndividual");
				solutionPath = rs.getString("SolutionPath");
				solutionFormat = rs.getString("SolutionFomat");
				testFilePath = rs.getString("TestFilePath");
				testFileFormat = rs.getString("TestFileFormat");
				submissionPath = rs.getString("SubmissionPath");
				submissionFormat = rs.getString("SubmissionFileFormat");
				maxGrade = rs.getInt("MaxGrade");
				rubric = getRubric(semester, courseNo, activityNo);
				
				Activity tempActivity = new Activity( activityName, activityNo, activityType, language, groupIndividual, rubric, solutionPath, 
						solutionFormat, testFilePath,  testFileFormat, submissionPath, submissionFormat, dueDate, maxGrade);
				activities.add(tempActivity);
			}
			rs.close();
		}catch (SQLException se)
			{
				System.out.println("\ngetActivities() - SQL Exception occurred, the state : "+
								se.getSQLState()+"\nMessage:\n"+se.getMessage()+"\n");
				return activities;
			}

		//Close connection
		closeConnection();
		
		return activities;

	}//end of getActivities()
	
//------------------------------------------------------------------------------------------//
	
	// Get a list of TA of a course from the database
	private static ArrayList<MarkerAndTA> getTAs(String courseNumber, String semester )
	{
		//ArrayList of TAs
		ArrayList<MarkerAndTA> taOfCourse = new ArrayList<MarkerAndTA>();
		String sSQL = "exec spViewListOfTAs ?,?";
		
		//TA attributes
		String firstName = ""; 
		String lastName = "";
		String employeeNumber = "";
		String accountType = "TA";
		
		//Connect
		connectToDatabase();
		try
		{
			pstmt = conn.prepareStatement(sSQL);
			
			//using StoredProcedures
			pstmt.setEscapeProcessing(true);
			pstmt.setString(1, courseNumber);
			pstmt.setString(2, semester);
			
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				employeeNumber = rs.getString("TA_ID");
				firstName = rs.getString("FirstName");
				lastName = rs.getString("LastName");
				
				MarkerAndTA tempMarkerAndTA = new MarkerAndTA(firstName, lastName, employeeNumber, accountType, null);
				taOfCourse.add(tempMarkerAndTA);
			}
			rs.close();
		}catch (SQLException se)
			{
				System.out.println("\ngetTAs() - SQL Exception occurred, the state : "+
								se.getSQLState()+"\nMessage:\n"+se.getMessage()+"\n");
				return taOfCourse;
			}

		//Close connection
		closeConnection();
		
		
		return taOfCourse;
	}//end of getTAs

//------------------------------------------------------------------------------------------//
	
	// Retrieve the rubric of an activity from the database
	private static Rubric getRubric(String semester, String courseNumber, String activityNumber)
	{
		Rubric tempRubric = new Rubric();
		
		//using StoredProcedures
		String sSQL = "exec spGetRubric ?,?,?";
		
		//Rubric attributes
		int maxPoints;
		String description = "";
		
		//Connect
		connectToDatabase();
		try
		{
			pstmt = conn.prepareStatement(sSQL);
			
			//using StoredProcedures
			pstmt.setEscapeProcessing(true);
			pstmt.setString(1, semester);
			pstmt.setString(2, courseNumber);
			pstmt.setString(3, activityNumber);
			
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				description = rs.getString(2);//Subsection
				maxPoints = rs.getInt(3);//SubSectionMaxGrade

				tempRubric.addRubricItem(description, maxPoints);
			}
			
			rs.close();
		}catch (SQLException se)
			{
				System.out.println("\ngetRubric() - SQL Exception occurred, the state : "+
								se.getSQLState()+"\nMessage:\n"+se.getMessage()+"\n");
				return tempRubric;
			}

		//Close connection
		closeConnection();
		
		return tempRubric;
	}//end of Get Rubric

//------------------------------------------------------------------------------------------//
	
	// For the admin assistant to get a list of instructors and TAs
	private static ArrayList<User> getAllTAsOrInstructors(String accountType)
	{
		//ArrayList of TAs
		ArrayList<User> allTAsOrInstructor = new ArrayList<User>();
		String sSQL = "SELECT FirstName, LastName, Account.ID " +
						"FROM Account INNER JOIN [User] ON Account.ID=[User].ID " +
						"WHERE Account.AccountType='" + accountType + "'";
		
		//TA attributes
		String firstName = ""; 
		String lastName = "";
		String id = "";
		
		//Connect
		connectToDatabase();
		
		try
		{
			pstmt = conn.prepareStatement(sSQL);
			ResultSet rs = pstmt.executeQuery();
	
			while (rs.next()) {
				firstName = rs.getString("FirstName");
				lastName = rs.getString("LastName");
				id = rs.getString("ID");
				
				User tempUser = new User(firstName, lastName, id, accountType);
				allTAsOrInstructor.add(tempUser);
			}
			rs.close();
		}catch (SQLException se)
			{
				System.out.println("\ngetAllTAsOrInstructors() - SQL Exception occurred, the state : "+
								se.getSQLState()+"\nMessage:\n"+se.getMessage()+"\n");
				return allTAsOrInstructor;
			}
	
		//Close connection
		closeConnection();
		
		return allTAsOrInstructor;
	}//end of getAllTAsOrInstructors
	
//------------------------------------------------------------------------------------------//

	// Clean up User and Course tables
	//  - if no account exists for a user, remove that user
	//  - if no course offering exists for a course, remove that course
	private static void cleanUserCourseTables()
	{
		updateTable("DELETE FROM [User] WHERE ID NOT IN (SELECT ID FROM Account)");
		updateTable("DELETE FROM Course WHERE Course# NOT IN (SELECT Course# FROM CourseOffering)");
		
	}//end of cleanUserCourseTables
	

}//end of Database