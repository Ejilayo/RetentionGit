package ca.Ranti;

//----------------------------------------------------------------------
//	LINE OF COMMUNICATION BETWEEN DATABASE AND OTHER CLASSES
//----------------------------------------------------------------------
public class Communicate {

	private static boolean addUserError;
	
	//----------------------------------------------------------------------
	//	CONSTRUCTOR
	//----------------------------------------------------------------------
	public Communicate(){
		addUserError = false;
	}//end of Communicate
	
	//----------------------------------------------------------------------
	//	SET AND GET ADDUSERERROR
	//----------------------------------------------------------------------
	public static void setAddUserError(boolean t0rF){
		addUserError = t0rF;
	}//end of setAddUserError
	
	public static boolean getAddUserError(){
		return addUserError;
	}//end of getAddUserError
}
