package ca.Ranti;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;

//---------------------------------------------------------------
// USER IS PRESENTED A LIST OF SUBJECTS TO CHOOSE FROM
// *This class does not necessarily use the same skeleton as 
// other gui classes.
// Objects are created and immediately added to the mainPanel
// so MainPanel is initialized and then calls needed contents
//---------------------------------------------------------------

public class SubjectChoiceGuiPanel{
	
	/**
	 * 
	 */
	//private static final long serialVersionUID = 1L;
	JFrame frame;
	private RPanel mainOne;
	private RPanel rightButtonsPanel, leftListPanel;
	private RButton cr8questions, takeTest, addSubject;
	private RList <Subject> subjectList;
	private DefaultListModel <Subject> subjectListModel;
	private JScrollPane subjectListScrollPane;
	private ArrayList<Subject> subjects;
	
	private User mainUser;
	private Subject selectedSubject;
	
	Dimension minimumSize = new Dimension(324,204);
	Dimension emptySpaceSize = new Dimension(10,10);  //Componentless area
	
	//---------------------------------------------------------------
	// EMPTY CONSTRUCTOR
	//---------------------------------------------------------------
	public SubjectChoiceGuiPanel(){
		subjects = new ArrayList<Subject>();
		setupMainPanel();
	}//end of SubjectchoiceGuiPanel
	
	//---------------------------------------------------------------
	// CONSTRUCTOR
	//---------------------------------------------------------------
	public SubjectChoiceGuiPanel(JFrame caller){
		subjects = new ArrayList<Subject>();
		frame = caller;
		setupMainPanel();
		
		RPanel chosenUser = getMainPanel();
		frame.getContentPane().removeAll();
		frame.setMinimumSize(minimumSize);
		frame.setTitle("Retention - Choose Subject");
		frame.getContentPane().add(chosenUser);
		frame.revalidate();
		
	}//end of SubjectChoiceGuiPanel(JFrame)
	
	//---------------------------------------------------------------
	// GET MAIN PANEL
	//---------------------------------------------------------------
	public RPanel getMainPanel(){
		return mainOne;
	}//end of getMainPanel
	
	//---------------------------------------------------------------
	// SETUP MAIN PANEL
	//---------------------------------------------------------------
	public void setupMainPanel()
	{	
		mainOne = new RPanel();
		mainOne.setLayout(new GridLayout(0,2));
		setupSubjectList();
		setupButtons();
		setupLeftPanel();
		setupRightPanel();
		
		//return mainOne;
	}//end of setupMainPanel()
	
	//---------------------------------------------------------------
	// SETUP BUTTONS
	//---------------------------------------------------------------
	private void setupButtons(){
		cr8questions = new RButton("Questions");
		takeTest = new RButton("Take Test");
		
		//ADD A BUTTON TO TAKE A PREVIOUS TEST
		
		addSubject = new RButton("Add Subject");

		//---------------------LISTENERS------------------------
				
		ClickListener listener = new ClickListener();
		cr8questions.addActionListener(listener);
		takeTest.addActionListener(listener);
		addSubject.addActionListener(listener);		
	}//end of setupButtons
	
	//---------------------------------------------------------------
	// SETUP RIGHT PANEL CONTAINING BUTTONS
	// Must first call setupButtons()
	//---------------------------------------------------------------
	private void setupRightPanel(){

		rightButtonsPanel = new RPanel();
		
		BorderLayout borderRight = new BorderLayout();
		rightButtonsPanel.setLayout(borderRight);
		
		rightButtonsPanel.add(Box.createRigidArea(emptySpaceSize), BorderLayout.NORTH);
		rightButtonsPanel.add(Box.createRigidArea(emptySpaceSize), BorderLayout.SOUTH);
		rightButtonsPanel.add(Box.createRigidArea(emptySpaceSize), BorderLayout.EAST);
		rightButtonsPanel.add(Box.createRigidArea(emptySpaceSize), BorderLayout.WEST);

		RPanel panelCenter = new RPanel();
		BoxLayout box = new BoxLayout(panelCenter,BoxLayout.Y_AXIS);
		panelCenter.setLayout(box);
		panelCenter.add(Box.createRigidArea(emptySpaceSize));
		panelCenter.add(cr8questions);
		panelCenter.add(Box.createRigidArea(emptySpaceSize));
		panelCenter.add(takeTest);
		panelCenter.add(Box.createVerticalStrut(30));
		panelCenter.add(Box.createVerticalGlue());
		panelCenter.add(addSubject);
		rightButtonsPanel.add(panelCenter, BorderLayout.CENTER);	
		
		mainOne.add(rightButtonsPanel);	
	}//end of setupRightPanel
	
	//---------------------------------------------------------------
	// SETUP LEFT PANEL CONTAINING BUTTONS
	// Must first call setupSubjectList()
	//---------------------------------------------------------------
	private void setupLeftPanel(){

		leftListPanel = new RPanel();
		BorderLayout borderLeft = new BorderLayout(5,5);
		leftListPanel.setLayout(borderLeft);
		
		leftListPanel.add(Box.createRigidArea(emptySpaceSize), BorderLayout.NORTH);
		leftListPanel.add(Box.createRigidArea(emptySpaceSize), BorderLayout.SOUTH);
		leftListPanel.add(Box.createRigidArea(emptySpaceSize), BorderLayout.EAST);
		leftListPanel.add(Box.createRigidArea(emptySpaceSize), BorderLayout.WEST);
		leftListPanel.add(subjectListScrollPane,BorderLayout.CENTER);
		
		mainOne.add(leftListPanel);
	}//end of setupLeftPanel
	
	//---------------------------------------------------------------
	// SETUP LIST OF SUBJECTS 
	//---------------------------------------------------------------
	private boolean setupSubjectList(){
		
		subjectListModel = new DefaultListModel<Subject>();
		subjectList = new RList<Subject>(subjectListModel); 
		/*{
		      protected void processMouseEvent(MouseEvent e)
		      {
		         int modifiers = e.getModifiersEx();

		         // filter out control click that unselects
		         if ((modifiers & e.CTRL_DOWN_MASK) == 0)
		         {
		            // go ahead and do normal processing on event
		            super.processMouseEvent(e);
		         }
		      }  // processMouseEvent()
		   };*/
		subjectListScrollPane = new JScrollPane(subjectList);
		
		boolean userSetup = false;
		//Database.getAllSubjects()
		subjects = Database.getAllSubjectNames();
		
		for (Subject sub : subjects){
			subjectListModel.addElement(sub);
			//subjects.add(sub);
		}
		
		subjectList.setSelectedIndex(0);
		
		userSetup = true;
		return userSetup;
		
	}//end of setupSubjectList
	
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
	
	
	//---------------------------------------------------------------
	// PRIVATE LISTENER CLASS
	//---------------------------------------------------------------
	private class ClickListener implements ActionListener{
		
		public void actionPerformed(ActionEvent event){
			Object source = event.getSource();
			selectedSubject = subjectList.getSelectedValue();
			if(source == cr8questions){

				QuestionsListGuiPanel subjectQuestions = new QuestionsListGuiPanel(frame,selectedSubject);
				subjectQuestions.setMainUser(getMainUser());
			}
			
			else if(source == addSubject){				
				SubjectDetailsGuiPanel subjectDetails = new SubjectDetailsGuiPanel(frame);
				subjectDetails.setMainUser(getMainUser());
			}
			
			else if (source == takeTest){
				TestCriteriaGuiPanel testCrit = new TestCriteriaGuiPanel(frame,selectedSubject);
				testCrit.setMainUser(getMainUser());
			}
		}
	}
}
