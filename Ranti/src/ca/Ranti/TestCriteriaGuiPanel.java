package ca.Ranti;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class TestCriteriaGuiPanel{

	/**
	 * 
	 */
	//private static final long serialVersionUID = 1L;
	private JFrame frame;
	private RPanel mainPanel,questDifficulty,questNumberPanel,datesPanel,authorPanel,typePanel,finalPanel;
	private DefaultListModel<User> authorModel;
	private DefaultListModel<String> typeModel;
	private JScrollPane authorPane,typePane;
	private RList<User> authorList;
	private RList<String> typeList;
	private RCheckBox typeBox,authorBox,hard,easy;
	private RLabel questCountLabel, betweenLabel, andLabel, byLabel, typeLabel;
//	private JTextField questionCountField;
	private RButton startPracticingButton;
	private JSlider questionCountSlider;
	private JDateComboBox dateStartDcb,dateEndDcb;
	
	private Subject selectedSubject;
	private User mainUser;
	private TestCriteria testOps;//as in Test Options
	//private ArrayList<User> questionAuthors;
	
	int questCount;
	//private Dated startDate,endDate;
	
	//----------------------------------------------------------------------
	//	CONSTRUCTOR: must call setupFrame()
	//----------------------------------------------------------------------
	public TestCriteriaGuiPanel(JFrame caller, Subject selected){
		
	//	questionAuthors = new ArrayList<User>();
		selectedSubject = selected;
		selectedSubject.getQuestionListFromDB();
		questCount = selectedSubject.getNumOfQuestions();
		//questCount = 0;//selectedSubject.getNumOfQuestions();//using getNumOfQuestions did not 
			//consider the dates options already on the//gui//actually because it was supposed to be showing 
			//the date from the beginning of time till now...edit that first, then come back
		
		setupMainPanel();
		frame = caller;
		setupFrame();
		//System.out.println(frame.getSize().toString());
	}//end of TestCriteriaGui
	
	//----------------------------------------------------------------------
	//	FRAME: must call setupMainPanel()
	//----------------------------------------------------------------------
	private void setupFrame(){
		RPanel about2TakeTest = getMainPanel();
		frame.getContentPane().removeAll();
		frame.setTitle("Retention - Select Test Criteria");
		frame.getContentPane().add(about2TakeTest);
		frame.revalidate();
		//frame.pack();
		frame.setMinimumSize(new Dimension(296, 620));
	}//end of setupFrame()
	
	//----------------------------------------------------------------------
	//	GET MAINPANEL
	//----------------------------------------------------------------------
	public RPanel getMainPanel(){
		return mainPanel;
	}//end of getMainPanel
	
	//----------------------------------------------------------------------
	//	MAIN PANEL: must call setupQuestNumberPanel(),setupDatesPanel(),
	//	setupAuthorPanel(), setupTypePanel() before adding panels
	//----------------------------------------------------------------------
	private void setupMainPanel(){
		mainPanel = new RPanel();
		BoxLayout boxMain = new BoxLayout(mainPanel,BoxLayout.Y_AXIS);
		mainPanel.setLayout(boxMain);
		
		setupQuestNumberPanel(questCount);
		setupDatesPanel();
		setupAuthorPanel();
		setupTypePanel();
		setupQuestDifficultyPanel();
		setupFinalButtonPanel();
		
		mainPanel.add(questNumberPanel);
		mainPanel.add(datesPanel);
		mainPanel.add(authorPanel);
		mainPanel.add(typePanel);
		mainPanel.add(questDifficulty);
		mainPanel.add(finalPanel);
	}//end of setupMainPanel()
	
	//----------------------------------------------------------------------
	//	QUESTION NUMBER PANEL 
	//----------------------------------------------------------------------
	private void setupQuestNumberPanel(int questionNum){

		questNumberPanel = new RPanel();
		questNumberPanel.setLayout(new BoxLayout(questNumberPanel,BoxLayout.Y_AXIS));
	//	questionCountField = new JTextField(5);
		questCountLabel = new RLabel(questCount + " of " + questionNum + " available questions created ");
		questCountLabel.setAlignmentX(JPanel.CENTER_ALIGNMENT);
		
		questionCountSlider = new JSlider(JSlider.HORIZONTAL,0,questCount,questCount);
		
		if((questCount/8) > 4){
			questionCountSlider.setMajorTickSpacing(questCount/8);
			questionCountSlider.setMinorTickSpacing(2);
		}
		else{
			questionCountSlider.setMajorTickSpacing(2);
			questionCountSlider.setMinorTickSpacing(1);
		}
		
		questionCountSlider.setPaintTicks(true);
		questionCountSlider.setPaintLabels(true);
		//Font hel = new Font("Courier New",Font.ITALIC,12);
		//questCountLabel.setFont(hel);
		questionCountSlider.addChangeListener(new SliderListener());
		
		RPanel temp = new RPanel();
		temp.add(questionCountSlider);
		questNumberPanel.add(temp);
		questNumberPanel.add(questCountLabel);

		//questNumberPanel.add(questionCountField);
		
		questionCountSlider.setBackground(Misc.groundColor);
		questionCountSlider.setForeground(Misc.decoyBackColor);
		questionCountSlider.setFont(Misc.sliderFont);

	}//end of setupQuestNumberPanel()
	
	//----------------------------------------------------------------------
	//	DATES PANEL: Displays between start and end dates
	//----------------------------------------------------------------------
	private void setupDatesPanel(){
		datesPanel = new RPanel();
		datesPanel.setLayout(new BoxLayout(datesPanel, BoxLayout.Y_AXIS));
		betweenLabel = new RLabel("between:");
		betweenLabel.setAlignmentX(JPanel.CENTER_ALIGNMENT);
		andLabel = new RLabel("and:");
		andLabel.setAlignmentX(JPanel.CENTER_ALIGNMENT);
		
		dateStartDcb = new JDateComboBox();
		dateEndDcb = new JDateComboBox(new Dated());
		//System.out.println("Today's Date Supposedly is:" + new Dated().getTheYear());
		
		dateStartDcb.addDateChangeListener(new DCListener());
		dateEndDcb.addDateChangeListener(new DCListener());
		
		//dateStartDcb.addActionListener(new ButtonListener());
		
		RPanel temp = new RPanel();
		temp.setLayout(new BoxLayout(temp,BoxLayout.PAGE_AXIS));
		temp.add(betweenLabel);
		temp.add(dateStartDcb);
		
		//dateStartDcb.addActionListener(new ButtonListener());
		
		RPanel temp2 = new RPanel();
		temp2.setLayout(new BoxLayout(temp2,BoxLayout.PAGE_AXIS));
		temp2.add(andLabel);
		temp2.add(dateEndDcb);

		datesPanel.add(temp);
		datesPanel.add(Box.createVerticalStrut(10));
		datesPanel.add(temp2);
		datesPanel.add(Box.createVerticalStrut(10));
	}//end of setupDatesPanel()
	
	//----------------------------------------------------------------------
	//	AUTHOR PANEL: Displays list of authors
	//----------------------------------------------------------------------
	private void setupAuthorPanel(){
		authorPanel = new RPanel();
		authorPanel.setLayout(new BoxLayout(authorPanel, BoxLayout.Y_AXIS));
		byLabel = new RLabel("by:");
		authorBox = new RCheckBox("all authors",true);
		authorBox.addItemListener(new CheckListener());
		

		authorModel = new DefaultListModel<User>();
		authorList = new RList<User>(authorModel);
		authorPane = new JScrollPane(authorList);
		authorPane.setPreferredSize(new Dimension(259,131));
		
		//DATA
		boolean inThere = false;
		User author;
		
		ArrayList <Question> ques = selectedSubject.getQuestionList();
		for(Question entry: ques){
			author = entry.getAuthor();
			//inThere = ((authorModel.indexOf(author)) > -1);
			inThere = authorModel.contains(author);
			if(!inThere){
			//if author Model does not contain this^ authour,
				authorModel.addElement(author);
			}
		}
		
		RPanel temp = new RPanel();
		temp.setLayout(new BoxLayout(temp, BoxLayout.X_AXIS));
		temp.add(byLabel);
		temp.add(authorBox);
		
		authorPanel.add(temp);
		
		RPanel temp2 = new RPanel();
		temp2.add(authorPane);
		
		authorPanel.add(temp2);
		
		authorList.setSelectionInterval(0,authorModel.size()-1);
		authorList.addListSelectionListener(new ListChangeListener());
		
		//COLOURING
		authorPane.setBackground(Misc.groundColor);

		//authorList.setSelectionForeground(Misc.lighterBlueColor);
	}//end of setupAuthorPanel()
	
	//----------------------------------------------------------------------
	//	TYPE PANEL: Displays list of question types
	//----------------------------------------------------------------------
	private void setupTypePanel(){
		typePanel = new RPanel();
		typePanel.setLayout(new BoxLayout(typePanel, BoxLayout.Y_AXIS));
		typeLabel = new RLabel("of type:");
		typeBox = new RCheckBox("all types",true);
		typeBox.addItemListener(new CheckListener());
		
		typeModel = new DefaultListModel<String>();
		typeModel.addElement("Definition");
		typeModel.addElement("True/False");
		typeModel.addElement("Fill Gap");
		
		typeList = new RList<String>(typeModel);
		typePane = new JScrollPane(typeList);
		typePane.setPreferredSize(new Dimension(259,131));
		
		RPanel temp = new RPanel();
		temp.setLayout(new BoxLayout(temp, BoxLayout.X_AXIS));
		temp.add(typeLabel);
		temp.add(typeBox);
		
		typePanel.add(temp);
		
		RPanel temp2 = new RPanel();
		temp2.add(typePane);
		
		typePanel.add(temp2);
		
		typeList.setSelectionInterval(0,typeModel.size()-1);
		typeList.addListSelectionListener(new ListChangeListener());
		
		//COLOURING
		typePane.setBackground(Misc.groundColor);
		

	}// end of setupTypePanel()
	
	//----------------------------------------------------------------------
	//	Hard and Easy Questions button
	//----------------------------------------------------------------------
	private void setupQuestDifficultyPanel(){
		questDifficulty = new RPanel();
		JLabel difficulty = new JLabel("Select Difficulty:");
		difficulty.setFont(new Font("Arial",Font.ITALIC, 12));
		
		hard = new RCheckBox("Hard");
		easy = new RCheckBox("Easy",true);
		
		ButtonGroup level = new ButtonGroup();
		level.add(easy);
		level.add(hard);
		
		questDifficulty.add(difficulty);
		questDifficulty.add(easy);
		questDifficulty.add(hard);
	}//end of setupQuestStrengthPanel()
	
	//----------------------------------------------------------------------
	//	FINAL PANEL: Displays final button
	//----------------------------------------------------------------------
	private void setupFinalButtonPanel(){
		finalPanel = new RPanel();
		startPracticingButton = new RButton("START PRACTICING");
		finalPanel.add(startPracticingButton);
		
		startPracticingButton.addActionListener(new ButtonListener());
	}//end of setupFinalButtonPanel()
	
	//----------------------------------------------------------------------
	//	GET TEST CRITERIA
	//----------------------------------------------------------------------
	public TestCriteria getTestTakingCriteria(){
		testOps = new TestCriteria(selectedSubject.getId());
		int [] authors = authorList.getSelectedIndices();
		int [] type = typeList.getSelectedIndices();
		
		String authorId = "";
		
		for(int iter: authors){
			authorId = authorModel.get(iter).getUserId();
			testOps.addAuthor(authorId);
		}
		
		for(int iter: type){
			testOps.addTestOptionType(iter+1);
		}
		String afterDate = dateStartDcb.getDate().toSqlString();
		String beforeDate = dateEndDcb.getDate().toSqlString();
		testOps.setBetweenDates(afterDate,beforeDate);
		
		if(hard.isSelected()){
			testOps.setWantObj(false);
		}
		
		//it is never more than the available questions
		//(solely because of the way this GUI is setup)...change GUI change this,
		//so no need to error check when using it---6 months after::: I was wrong...there were errors
		testOps.setNumOfQuestionWanted(questionCountSlider.getValue());
		//Ironic what happens when you try to send this to an object that needs 
		
		return testOps;
	}//end of getTestTakingCritearia
	
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
	
/********************
	//----------------------------------------------------------------------
	//	PRIVATE CLASS BUTTONLISTENER
	//----------------------------------------------------------------------
********************/
	
	private class ButtonListener implements ActionListener{
		
		public void actionPerformed(ActionEvent e){
			Object source = e.getSource();
			boolean questionsAvailable = questCount >0;
			
			if (source == startPracticingButton && questionsAvailable){
				//System.out.println(dateEndDcb.getDate().toSqlString());
				getTestTakingCriteria();
				Test test = new Test(testOps,selectedSubject.getId(),mainUser);
				test.setFinalQuestionList(testOps.getNumOfQuestionWanted());
				
				TestTakingGuiPanel startTest = new TestTakingGuiPanel(frame,test);
				startTest.setMainUser(getMainUser());
				startTest.setSelectedSubject(getSelectedSubject());
			}
		}
	}
	
/********************
	//----------------------------------------------------------------------
	//	PRIVATE CLASS SLIDERLISTENER
	//----------------------------------------------------------------------
********************/
	private class SliderListener implements ChangeListener{
		
		public void stateChanged(ChangeEvent e){
			Object source = e.getSource();
			if (source == questionCountSlider){
				int value = questionCountSlider.getValue();
				questCountLabel.setText(value + " of " + questCount + " avalaible questions created");
				questCountLabel.setAlignmentX(JPanel.CENTER_ALIGNMENT);
			}
		}
	}
	
	private class CheckListener implements ItemListener{
		
		public void itemStateChanged(ItemEvent e){
			Object source = e.getSource();
			if (source == typeBox ){
				if(typeBox.isSelected()){
					typeList.setSelectionInterval(0,typeModel.getSize()-1);
				}
				
				else if(!typeBox.isSelected()){
					typeList.setSelectedIndex(typeList.getSelectedIndex());
				}
			}
			
			else if (source == authorBox ){
				if( authorBox.isSelected()){
					authorList.setSelectionInterval(0,authorModel.getSize()-1);
				}
				else if(!authorBox.isSelected()){
					authorList.setSelectedIndex(authorList.getSelectedIndex());
				}
			}
		}
	}
	
	private class DCListener implements DateChangeListener{
		
		public void dateChanged(DateChangeEvent e){
			getTestTakingCriteria();
			Test test = new Test(testOps,selectedSubject.getId(),mainUser);
			questCount = test.getNumOfQuestion();
			mainPanel.remove(questNumberPanel);
			setupQuestNumberPanel(questCount);
			mainPanel.add(questNumberPanel,0);
			//questNumberPanel.revalidate();
			mainPanel.revalidate();
			//frame.revalidate();
		}
	}
	
	private class ListChangeListener implements ListSelectionListener{
		
		public void valueChanged(ListSelectionEvent e){
			Object source = e.getSource();
			
			if(source == typeList){
				int [] selectInterval = typeList.getSelectedIndices();
				boolean allSelected = (selectInterval.length == typeModel.size());
				if(!allSelected){
					typeBox.setSelected(false);
				}
				else if(allSelected){
					typeBox.setSelected(true);
				}
			}
			
			else if(source == authorList){
				int [] selectInterval = authorList.getSelectedIndices();
				boolean allSelected = (selectInterval.length == authorModel.size());
				if(!allSelected){
					authorBox.setSelected(false);
				}
				else if(allSelected){
					authorBox.setSelected(true);
				}
			}
			
			getTestTakingCriteria();
			Test test = new Test(testOps,selectedSubject.getId(),mainUser);
			questCount = test.getNumOfQuestion();
			mainPanel.remove(questNumberPanel);
			setupQuestNumberPanel(questCount);
			mainPanel.add(questNumberPanel,0);

			mainPanel.revalidate();
			//frame.revalidate();
		}
	}
}
