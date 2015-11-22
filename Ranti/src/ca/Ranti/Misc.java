package ca.Ranti;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
//import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;


//-----------------------------------------------------------
//	This class is to contain constants and random 
//	peculiar functions that are currently in 
//	classes they shouldn't. But have no class to call theirs
//	Misc short for miscellaneous is where they belong e.g
//	Database.toBoolean()...DataBase.toInt() etc.
//-----------------------------------------------------------
public class Misc {
	
	
	static Color groundColor = new Color(165,200,200);
	static Color darkColor = new Color(65,48,60);
	static Color lighterBlueColor = new Color(241, 255, 255);
	static Color decoyBackColor = new Color(65,48,50);
	
	static Font buttonFont =  new Font("Verdana",Font.BOLD,12);
	static Font labelFont =  new Font("Verdana",Font.PLAIN,14);
	static Font cBoxFont =  new Font("Verdana",Font.PLAIN,12);
	static Font decoyButtonFont = new Font("Verdana",Font.PLAIN,20);
	static Font sliderFont = new Font("Verdana",Font.PLAIN,10);
	static Font questionFont = new Font("Verdana",Font.BOLD,27);
	
	public static ArrayList<Image> getIcons() {
		ArrayList<Image> icons = new ArrayList<Image>();
		File image1 = new File("Data/images/UI/Retend.png");
		File image2 = new File("Data/images/UI/Retend2.png");
		File image3 = new File("Data/images/UI/Retend3.png");
		try{
			Image imagesBuff1 = ImageIO.read(image1);
			Image imagesBuff2 = ImageIO.read(image2); 
			Image imagesBuff3 = ImageIO.read(image3); 
			
			icons.add(imagesBuff1);
			icons.add(imagesBuff2);
			icons.add(imagesBuff3);
			
		}
		catch(IOException e){
			Message.informUser(null,"Misc Icon Retrieve Error!", Database.errorToString(e));
		}
		
		return icons;
	}
	
	public static Image getIcon() {
		
		File image4 = new File("Data/images/UI/Retend10.png");
		Image imagesBuff3 = null;
		try{

			imagesBuff3 = ImageIO.read(image4); 

		}
		catch(IOException e){
			Message.informUser(null,"Misc Icon Retrieve Error!", Database.errorToString(e));
		}
		
		return imagesBuff3;
	}
	
}
