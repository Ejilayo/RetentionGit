package ca.Ranti;

import java.util.ArrayList;

public class User {

	String userName;
	String userId;
	int numOfTakenTests;
	ArrayList<Test> testsTaken;
	//int averageScore;
	//int totalScores;
	//ArrayList<>
	
	//add methods to update this users stats
	
	//----------------------------------------------------------------------
	//	EMPTY CONSTRUCTOR
	//----------------------------------------------------------------------
	public User(){
		userId = "";
		userName = "";
	}//end User
	
	//----------------------------------------------------------------------
	//	CONSTRUCTOR
	//----------------------------------------------------------------------
	public User(String userId, String userName){
		this.userId = userId;
		this.userName = userName;
		//toString();
	}//end of User(id,name)
	
	//----------------------------------------------------------------------
	//	GETTERS AND SETTERS
	//----------------------------------------------------------------------
	//----------------------------------------------------------------------
	//	GET USERNAME
	//----------------------------------------------------------------------
	public String getUserName() {
		return userName;
	}//end of getUserName

	//----------------------------------------------------------------------
	//	SET USERNAME
	//----------------------------------------------------------------------
	public void setUserName(String userName) {
		this.userName = userName;
	}//end of setUserName

	//----------------------------------------------------------------------
	//	GET USER ID
	//----------------------------------------------------------------------
	public String getUserId() {
		return userId;
	}//end of getUserId

	//----------------------------------------------------------------------
	//	SET USER ID
	//----------------------------------------------------------------------
	public void setUserId(String userId) {
		this.userId = userId;
	}//end of setUserId
	
	//----------------------------------------------------------------------
	//	EQUALS
	//----------------------------------------------------------------------
	@Override
	public boolean equals(Object other){
		return ((((User)other).getUserId().equals(userId))&&(((User)other).getUserName().equals(userName)));
	}
	
	//----------------------------------------------------------------------
	//	TO STRING
	//----------------------------------------------------------------------
	public String toString(){
		String result = userName;
		return result;
	}
	
	//----------------------------------------------------------------------
	//	STRING
	//----------------------------------------------------------------------
	public String toString2(){
		String result = "ID: " + userId + ", NAME:" + userName;
		return result;
	}
	
}
