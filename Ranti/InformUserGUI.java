/*
 * Filename: InformUserGUI.java
 * Author: Ejilayomi Ayomipo Mimiko
 * Copyright and Licensing: 
 * 
 * Revision History:
 * 
 * Revision      | Status                      | Publication/Revision Date   | By
 * ------------------------------------------------------------------------------------------------------
 * 1.0           | Created                     | April. 5, 2014              | Ejilayomi Ayomipo Mimiko
 * ------------------------------------------------------------------------------------------------------
 */

package ca.StreamlinedGradingSystem;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class InformUserGUI 
{
	// Attributes
	static JFileChooser browseChooser = new JFileChooser();
	static JTextField textField = new JTextField(50);
	static String path = "";
	static String lastWindowPath = "";

	// Field input
	public static void fieldInput(String errorTitle,String errorMessage, JTextField field)
	{
		String value = "";
		value = JOptionPane.showInputDialog(null, errorMessage, errorTitle, JOptionPane.OK_CANCEL_OPTION);
		field.setText(value);
	}
	
	// Gets the input for the path
	public static void pathInput(String errorTitle, String errorMessage, JTextField field)
	{
		Object[] options = { "Submit Path", "Browse", "Close" };

		JPanel panel = new JPanel();
		panel.add(new JLabel("Solution File Path:"));
		
		panel.add(textField);
		boolean open = true; 
		
		
		int result;
		while (open ==true){
			textField.setText(lastWindowPath);
			result = JOptionPane.showOptionDialog(null, panel, errorTitle,JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE,null, options, null);
			
			if (result == 0)//Submit Path
			{
				field.setText(textField.getText());
			}
			else if (result == 1) //Browse
			{
				
				browseChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int browseChooserVal = browseChooser.showDialog(null, "Select");
				String path = "";
				if (browseChooserVal == JFileChooser.APPROVE_OPTION)
				{
					path = browseChooser.getSelectedFile().toString();
					lastWindowPath = path;
					textField.setText(lastWindowPath);
				}
			}
			else if(result ==2)//Close
			{
				open = false;
			}
		}
	}
	
	// Constructor
	public static void informUser(String errorTitle, String errorMessage)
	{
		
		JOptionPane.showMessageDialog(null, errorMessage, errorTitle, JOptionPane.INFORMATION_MESSAGE);
	}
}// End of class InformUserGUI
