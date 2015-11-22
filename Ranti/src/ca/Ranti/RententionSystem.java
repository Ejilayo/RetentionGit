package ca.Ranti;

//import java.io.*;

public class RententionSystem {
	
	//----------------------------------------------------------------------
	//	MAIN
	//----------------------------------------------------------------------
	public static void main(String [] args){
		
		//create databases on first run
		//System.out.println("Begin!");
		
		Database.firstTimePopulate();
		Database.runCount();
		//new TimeWasteIntro();
		new UserGui();

		//System.out.println(new Dated().toSqlString());
		//System.out.println("End!");
	}
}
