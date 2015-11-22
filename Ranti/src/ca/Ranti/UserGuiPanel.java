package ca.Ranti;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class UserGuiPanel  {

	private RPanel mainOne;
	private RPanel rightButtonsPanel, leftListPanel;
	private RButton chooseUser, addUser, settings;
	private RList <User> userList;
	private DefaultListModel <User> userListModel;
	private JScrollPane userListScrollPane;
	private ArrayList<User> users;
	private JFrame frame;
	
	private User selectedUser;
	
	Dimension minimumSize = new Dimension(350,536); 
	Dimension emptySize = new Dimension(10,10);  //Componentless area
	
	//private boolean addUserError = false;
	//---------------------------------------------------------------
	// CONSTRUCTOR
	//---------------------------------------------------------------
	public UserGuiPanel(JFrame caller){
		frame = caller;

		//GUI + DATA
		setupUserGuiPanel();
		
		rightButtonsPanel.setOpaque(false);
		leftListPanel.setOpaque(false);
		
		//create a setupFrameMethod
		//frame.getContentPane().setEnabled(false);
		//frame.getContentPane().disableEvent(AWTEvent);
		frame.getContentPane().removeAll();
		frame.setMinimumSize(minimumSize);
		//frame.setSize(minimumSize);
		frame.setTitle("Retention - Users");
		frame.getContentPane().add(getMainPanel());
		frame.setLocationRelativeTo(null);
		frame.revalidate();
	}
	
	public void setupUserGuiPanel(){
		mainOne = new RPanel();
		mainOne.setLayout(new GridLayout(0,2));
		
		setupUserList();
		setupButtons();
		//chooseUser.setPreferredSize(addUser.getMinimumSize());
		setupLeftPanel();
		setupRightPanel();

	}
	
	//---------------------------------------------------------------
	// GET MAIN PANEL
	//---------------------------------------------------------------
	public RPanel getMainPanel(){
		return mainOne;
	}
	
	 //---------------------------------------------------------------
	 // Setup buttons including image, icon etc
	 //---------------------------------------------------------------
	public void setupButtons(){
		chooseUser = new RButton("Select User");
		addUser = new RButton("Add New User");
		settings = new RButton("Folder Settings");
		settings.setEnabled(false);
				
		ClickListener listener = new ClickListener();
		chooseUser.addActionListener(listener);
		addUser.addActionListener(listener);
		settings.addActionListener(listener);		
	}
	
	//---------------------------------------------------------------
	// Setup Buttons, visually on the right Panel
	//---------------------------------------------------------------
	public void setupRightPanel(){

		rightButtonsPanel = new RPanel();
		
		RPanel borderCenter = new RPanel();
		borderCenter.setOpaque(false);
		BoxLayout box = new BoxLayout(borderCenter,BoxLayout.Y_AXIS);
		borderCenter.setLayout(box);
		borderCenter.add(Box.createRigidArea(emptySize));
		borderCenter.add(chooseUser);
		borderCenter.add(Box.createRigidArea(emptySize));
		borderCenter.add(addUser);
		borderCenter.add(Box.createVerticalStrut(30));
		borderCenter.add(Box.createVerticalGlue());
		borderCenter.add(settings);
		
		BorderLayout borderRight = new BorderLayout();
		rightButtonsPanel.setLayout(borderRight);
		
		rightButtonsPanel.add(Box.createRigidArea(emptySize), BorderLayout.NORTH);
		rightButtonsPanel.add(Box.createRigidArea(emptySize), BorderLayout.SOUTH);
		rightButtonsPanel.add(Box.createRigidArea(emptySize), BorderLayout.EAST);
		rightButtonsPanel.add(Box.createRigidArea(emptySize), BorderLayout.WEST);

		rightButtonsPanel.add(borderCenter, BorderLayout.CENTER);	

		mainOne.add(rightButtonsPanel);	
	}
	
	//---------------------------------------------------------------
	// Setup List (visual) Panel
	//---------------------------------------------------------------
	public void setupLeftPanel(){

		leftListPanel = new RPanel();
		BorderLayout borderLeft = new BorderLayout(5,5);
		leftListPanel.setLayout(borderLeft);
		
		leftListPanel.add(Box.createRigidArea(emptySize), BorderLayout.NORTH);
		leftListPanel.add(Box.createRigidArea(emptySize), BorderLayout.SOUTH);
		leftListPanel.add(Box.createRigidArea(emptySize), BorderLayout.EAST);
		leftListPanel.add(Box.createRigidArea(emptySize), BorderLayout.WEST);
		leftListPanel.add(userListScrollPane,BorderLayout.CENTER);
		
		mainOne.add(leftListPanel);
	}
	
	//---------------------------------------------------------------
	// Gets the list of Users needed to be displayed
	//---------------------------------------------------------------
	private boolean setupUserList(){
		//GUI
		userListModel = new DefaultListModel<User>();
		userList = new RList<User>(userListModel);
		userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		userListScrollPane = new JScrollPane(userList);
		
		//DATA
		users = Database.getAllUsers();
		boolean userSetup = false;
		
		for (User usr : users){
			userListModel.addElement(usr);
		}
		
		userList.setSelectedIndex(0);
		userSetup = true;
		return userSetup;
		
	}
	
	//------------------------------------------------------------------
	//	
	//------------------------------------------------------------------
/*
	//------------------------------------------------------------------
	//		LISTENER CLASS
	//------------------------------------------------------------------
*/
	private class ClickListener implements ActionListener{
		
		public void actionPerformed(ActionEvent event){
			Object source = event.getSource();
//			Dimension minimumSize = new Dimension(324,204); 
			
			if(source == chooseUser){
				selectedUser = userList.getSelectedValue();
				SubjectChoiceGuiPanel subjects = new SubjectChoiceGuiPanel(frame);
				subjects.setMainUser(selectedUser);
			}
			
			else if(source == addUser){
				String name = JOptionPane.showInputDialog(frame, "Enter Name:");
				//
				if((name != null)){ 
					if(!(name.trim().equals("")) ){
						Database.addUser(name);
						//userListModel.addElement(name);
						
						leftListPanel.remove(userList);
						leftListPanel.remove(userListScrollPane);
						setupUserList();
						leftListPanel.add(userListScrollPane, BorderLayout.CENTER);
						mainOne.revalidate();
						userList.setSelectedIndex(userListModel.size() -1);
					}
				}
			}
			else if (source == settings){
				
			}

		}
	}
}
