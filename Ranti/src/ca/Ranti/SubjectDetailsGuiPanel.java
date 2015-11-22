package ca.Ranti;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

public class SubjectDetailsGuiPanel{

	/**
	 * 
	 */
	//private static final long serialVersionUID = 1L;
	private JFrame frame;
	private RPanel nicknamePanel,descriptionPanel,listPanel,finalButtonPanel,mainPanel;
	private RLabel nickName, description, listOfTerms;
	private JTextField nickNameField, descriptionField,potTermField;
	private RButton importButton, createButton,addTermButton,removeTermButton,editTermButton;
	private RList<String> termList;
	private JScrollPane termScroll;
	private DefaultListModel<String> termListModel;
	
	private User mainUser;
	private Subject selectedSubject;
	
	Dimension finalSize = new Dimension(298,362);
	//---------------------------------------------------------------
	// CONSTRUCTOR
	//---------------------------------------------------------------
	public SubjectDetailsGuiPanel(JFrame caller){
		frame = caller;
		setupMainPanel();
		//setupFrame();
		//System.out.println(frame.getSize().toString());
		
		RPanel addedSubject = getMainPanel();
		//clear content Pane
		frame.getContentPane().removeAll();
		frame.setMinimumSize(finalSize);
		//frame.setSize(finalSize);
		//frame.setMaximumSize(finalSize);
		frame.setTitle("Retention - Subject Description");
		
		//reveal subject details pane
		frame.getContentPane().add(addedSubject);
		frame.revalidate();
		//frame.pack();
		
	}//end of SubjectDetailsGui
	
	//---------------------------------------------------------------
	// 
	//---------------------------------------------------------------
	public RPanel getMainPanel(){
		return mainPanel;
	}//end getMainFrame()
	
	//---------------------------------------------------------------
	// SETUP MAIN JFRAME COMPONENT 
	//---------------------------------------------------------------
	public void setupFrame(){
//		frame = new JFrame("Create Subject");
//		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//
//		setupMainPanel();
//		frame.getContentPane().add(mainPanel);
//		
//		frame.pack();
//		frame.setVisible(true);
	}//end of setupFrame
	
	//---------------------------------------------------------------
	// SETUP MAINPANEL LAYOUT AND GENERAL VISUAL LAYOUT
	//---------------------------------------------------------------
	public void setupMainPanel(){
		
		mainPanel = new RPanel();

		BoxLayout boxy = new BoxLayout(mainPanel,BoxLayout.Y_AXIS);
		mainPanel.setLayout(boxy);
		
		setupNicknamePanel();
		mainPanel.add(nicknamePanel);
		
		setupDescriptionPanel();
		mainPanel.add(descriptionPanel);
		
		mainPanel.add(Box.createRigidArea(new Dimension(119,15)));
		
		setupListPanel();
		mainPanel.add(listPanel);
		
		setupFinalButtonPanel();
		mainPanel.add(finalButtonPanel);
	}//end of setupMainPanel
	
	//---------------------------------------------------------------
	// PANEL1: NICKNAME PANEL
	//---------------------------------------------------------------
	private void setupNicknamePanel(){
		
		nicknamePanel = new RPanel();
		nickNameField = new JTextField();
		nickName = new RLabel("NICKNAME:");
		
		//BoxLayout nNBoxLayout = new BoxLayout(nicknamePanel, BoxLayout.Y_AXIS);
		nicknamePanel.setLayout(new BorderLayout());
		
		nicknamePanel.add(nickName,BorderLayout.WEST);
		nicknamePanel.add(nickNameField,BorderLayout.SOUTH);
		
		nicknamePanel.setMaximumSize(new Dimension(300,50));
	}//end of setupNicknamePanel
	
	//---------------------------------------------------------------
	// PANEL2: DESCRIPTION PANEL
	//---------------------------------------------------------------
	private void setupDescriptionPanel(){
		
		descriptionPanel = new RPanel();
		descriptionField = new JTextField();
		description = new RLabel("DESCRIPTION:");
		
		//BoxLayout descBoxLayout = new BoxLayout(descriptionPanel, BoxLayout.Y_AXIS);
		descriptionPanel.setLayout(new BorderLayout());
		
		descriptionPanel.add(description,BorderLayout.WEST);
		descriptionPanel.add(descriptionField,BorderLayout.SOUTH);
		descriptionPanel.setMaximumSize(new Dimension(300,50));
	}//end of setupDescriptionPanel
	
	//---------------------------------------------------------------
	// PANEL3: LIST PANEL->Contains the list of terms and buttons
	//---------------------------------------------------------------
	private void setupListPanel(){
		RPanel left = new RPanel();// for list
		RPanel right = new RPanel();// for buttons
		
		listPanel = new RPanel();
		listOfTerms = new RLabel("LIST OF TERMS:");
		termListModel = new DefaultListModel<String>();
		termList = new RList<String>(termListModel);
		termScroll = new JScrollPane(termList);
		
		left.setLayout(new BoxLayout(left,BoxLayout.Y_AXIS));
		left.add(listOfTerms);
		left.add(termScroll);
		
		BoxLayout rightBox = new BoxLayout(right,BoxLayout.Y_AXIS);
		right.setLayout(rightBox);
		importButton = new RButton("Import Terms");
		potTermField = new JTextField(12);
		potTermField.setHorizontalAlignment(JTextField.RIGHT);
		addTermButton = new RButton("Add Term");
		editTermButton = new RButton("Edit Term");
		removeTermButton = new RButton("Remove Term");

		RPanel temp = new RPanel(new BorderLayout());
		temp.add(potTermField,BorderLayout.SOUTH);
		right.add(temp);
		right.add(importButton);
		right.add(addTermButton);
		right.add(editTermButton);
		right.add(removeTermButton);
		right.add(Box.createVerticalStrut(40));

		//BoxLayout box1 = new BoxLayout(listPanel,BoxLayout.X_AXIS);
		listPanel.setLayout(new BoxLayout(listPanel,BoxLayout.X_AXIS));
		listPanel.add(left);
		listPanel.add(right);
	}//end of setupListPanel
	
	//---------------------------------------------------------------
	// SETUP FINAL PANEL
	//---------------------------------------------------------------
	private void setupFinalButtonPanel(){
		
		finalButtonPanel = new RPanel();
		createButton = new RButton("CREATE");
		
		createButton.addActionListener(new ButtonListener());
		
		finalButtonPanel.add(createButton);
		finalButtonPanel.setMaximumSize(new Dimension(200,30));
	}//end of setupFinalButtonPanel
	
	//---------------------------------------------------------------
	// SET AND GET MAIN USER
	//---------------------------------------------------------------
	public User getMainUser(){
		return mainUser;
	}//end getMainUser
	
	public void setMainUser(User person){
		mainUser = person;
	}//end setMainUser
	
	//---------------------------------------------------------------
	// 	GET AND SET SELECTED SUBJECT
	//---------------------------------------------------------------
	public Subject getSelectedSubject(){
		return selectedSubject;
	}//end of getSelectedSubject
	
	public void setSelectedSubject(Subject selected){
		selectedSubject = selected;
	}//end of setSelectedSubject()
	
	/*
	 //--------------------------------------------------------------
	  * CLASS BUTTONLISTENERS
	 //--------------------------------------------------------------
	 */

	private class ButtonListener implements ActionListener{
		
		public void actionPerformed(ActionEvent e){
			Object source = e.getSource();
			
			if(source == createButton){
				String id = nickNameField.getText();
				String desc = descriptionField.getText();
				String creator = getMainUser().getUserId();
				
				Database.addSubject(id,desc,creator);
				
				SubjectChoiceGuiPanel subjects = new SubjectChoiceGuiPanel(frame);
				subjects.setMainUser(getMainUser());
			}
		}
	}
}
