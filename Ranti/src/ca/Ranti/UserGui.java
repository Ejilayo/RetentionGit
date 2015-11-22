package ca.Ranti;

//import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
//import javax.swing.JPanel;

public class UserGui {

	private static JFrame frame;
	//private static UserGuiPanel users;// = new UserGuiPanel();
	
	//----------------------------------------------------------------------
	//	CONSTRUCTOR
	//----------------------------------------------------------------------
	public UserGui(){
		start();
	}//end of UserGui()
	
	//----------------------------------------------------------------------
	//	START - Temporarily creates frame and hold menu items
	//----------------------------------------------------------------------
	public void start(){

		JMenuBar menuBar = new JMenuBar();
		JMenu file = new JMenu ("Goto");
		JMenuItem userPage = new JMenuItem("Goto Users");
		file.add(userPage);
		menuBar.add(file);
		userPage.setName("userPage");
		userPage.addActionListener(new MenuListener());
				
		
		frame = new JFrame("Retention - Users");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		UserGuiPanel users = new UserGuiPanel(frame);
		frame.getContentPane().add(users.getMainPanel());
		frame.setIconImage(Misc.getIcon());
		
		//Dimension minimumSize = new Dimension(296,536);
		//frame.setMinimumSize(minimumSize);
		//frame.setMinimumSize(minimumSize);
		
		frame.setJMenuBar(menuBar);
		
		//frame.pack();
		
		//frame.setLocationRelativeTo(null);
		new TimeWasteIntro();
		frame.setVisible(true);
	}//end of start()
	
	
	//----------------------------------------------------------------------
	//	LISTENER CLASSES
	//----------------------------------------------------------------------
	private class MenuListener implements ActionListener{
		
		public void actionPerformed(ActionEvent e){
			
			//Object source = e.getSource();
			String text = ((JMenuItem)e.getSource()).getName();
			
			if (text.equals("userPage")){
				//frame.getContentPane().removeAll();
				new UserGuiPanel(frame);
			}//end if text = userPage
		}//end of actionPerformed()
	}
	
}
