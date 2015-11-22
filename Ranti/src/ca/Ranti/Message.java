package ca.Ranti;

import java.awt.Component;

import javax.swing.JOptionPane;

//To users 
public class Message {

	public static void informUser(Component parent,String errorTitle, String errorMessage)
	{
		
		JOptionPane.showMessageDialog(parent, errorMessage, errorTitle, JOptionPane.INFORMATION_MESSAGE);
	}
}
