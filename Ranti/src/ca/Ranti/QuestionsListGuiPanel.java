package ca.Ranti;

import java.awt.BorderLayout;
//import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
//import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class QuestionsListGuiPanel {

	/**
	 * 
	 */
	//private static final long serialVersionUID = 1L;
	private JFrame frame;
	private RLabel questionCount;
	private RPanel topLabelPanel, listPanel, buttonsPanel, lowerPanel, mainPanel;
	private RButton addButton, removeButton, editButton;
	private RList<Question> questionsList;
	private DefaultListModel<Question> questionsModel;
	private JScrollPane questionsScroll;
	
	ArrayList<Question> questions;
	private Subject selectedSubject; //must be set
	private User mainUser;
	
	//---------------------------------------------------------------
	// CONSTRUCTOR
	//---------------------------------------------------------------
	public QuestionsListGuiPanel(JFrame caller, Subject selectedSubject){
		
		this.selectedSubject = selectedSubject;
		selectedSubject.getQuestionListFromDB();
		
		questions = new ArrayList<Question>();
		
		setupMainPanel();
		setuptopLabelPanel();
		setupLowerPanel();
		
		frame = caller;
		
		setupFrame();
		//System.out.println(frame.getSize().toString());

	}//end QuestionsGui
	
	//---------------------------------------------------------------
	// SETS UP MAIN JFRAME
	//---------------------------------------------------------------
	public void setupFrame(){
		
		RPanel cr8QuestOverview = getMainPanel();
		frame.getContentPane().removeAll();
		
		frame.setTitle("Retention - List of Questions");
		//System.out.println(frame.getSize().toString());
		frame.getContentPane().add(cr8QuestOverview);
		frame.revalidate();
		//frame.pack();
//		frame.setMinimumSize(new Dimension(347,184));
	}//end of setupFrame
	
	//---------------------------------------------------------------
	// GET MAIN PANEL
	//---------------------------------------------------------------
	public RPanel getMainPanel(){
		return mainPanel;
	}
	//---------------------------------------------------------------
	// SETS UP MAIN PANEL
	//---------------------------------------------------------------
	public void setupMainPanel(){
		mainPanel = new RPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
	}//end of setupMainPanel
	
	//---------------------------------------------------------------
	// SETS UP PANEL TO DISPLAY QUESTION COUNT //HigherPanel
	//---------------------------------------------------------------
	private void setuptopLabelPanel(){
		
		topLabelPanel = new RPanel(new FlowLayout(FlowLayout.LEFT));
		questionCount = new RLabel("QUESTION COUNT: "+ selectedSubject.getNumOfQuestions());
		topLabelPanel.add(questionCount);
		
		mainPanel.add(topLabelPanel);
	}//end setuptopLabelPanel
	
	//---------------------------------------------------------------
	// SETS UP LOWER PANEL
	//---------------------------------------------------------------
	private void setupLowerPanel(){
		
		lowerPanel = new RPanel();
		lowerPanel.setLayout(new BoxLayout(lowerPanel,BoxLayout.X_AXIS));
		
		setupListPanel();
		setupButtonsPanel();
		lowerPanel.add(listPanel);
		lowerPanel.add(buttonsPanel);
		
		mainPanel.add(lowerPanel);
	}//end of setupLowerPanel
	
	//---------------------------------------------------------------
	// SETS UP PANEL TO DISPLAY QUESTIONS LIST
	//---------------------------------------------------------------
	private void setupListPanel(){

		questionsModel = new DefaultListModel<Question>();
		questionsList = new RList<Question>(questionsModel);
		questionsScroll = new JScrollPane(questionsList);
		
		listPanel = new RPanel();
		listPanel.setLayout(new BorderLayout());
		listPanel.add(questionsScroll,BorderLayout.CENTER);
		
		questions = Database.getAllQuestionBasics(selectedSubject.getId());
		
		for(Question prob: questions){
			questionsModel.addElement(prob);
		}
	}//end of setupListPanel
	
	//---------------------------------------------------------------
	// SETS UP BUTTON DISPLAY PANEL 
	//---------------------------------------------------------------
	private void setupButtonsPanel(){
		
		addButton = new RButton("Add");
		editButton = new RButton("Edit");
		removeButton = new RButton("Remove");
		
		buttonsPanel = new RPanel();
		buttonsPanel.setLayout(new BoxLayout(buttonsPanel,BoxLayout.Y_AXIS));
		
		buttonsPanel.add(addButton);
		buttonsPanel.add(editButton);
		buttonsPanel.add(removeButton);
		
		//-------LISTENERS------------
		addButton.addActionListener(new ClickListener());
		
	}//end of setupButtonsPanel
	
	//---------------------------------------------------------------
	// GET AND SET SELECTED SUBJECT
	//---------------------------------------------------------------
	public Subject getSelectedSubject() {
		return selectedSubject;
	}//end of getSselectedSubject()

	public void setSelectedSubject(Subject selectedSubject) {
		this.selectedSubject = selectedSubject;
	}//end of setSelectedSubject(Subject)
	
	//---------------------------------------------------------------
	// 	GET AND SET MAINUSER
	//---------------------------------------------------------------
	public User getMainUser(){
		return mainUser;
	}//end of getMainUser
	
	public void setMainUser(User person){
		mainUser = person;
	}//end of setMainUser()
	
/*///////////////////////////////////////////////////////////////////
	//---------------------------------------------------------------
	// LISTENER CLASS
	//---------------------------------------------------------------
*/

	private class ClickListener implements ActionListener{
		
		public void actionPerformed(ActionEvent e){
			Object source = e.getSource();
			
			if (source == addButton){
				CreateQuestionGuiPanel addingQuestion = new CreateQuestionGuiPanel(frame,selectedSubject);
				addingQuestion.setMainUser(mainUser);
			}
			
		}
	}
}
