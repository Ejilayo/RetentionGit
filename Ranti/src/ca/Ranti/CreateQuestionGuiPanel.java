package ca.Ranti;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

public class CreateQuestionGuiPanel {
	
	//----------------------------------------------------------------------
	//	CREATEQUESTIONGUI: User uses this panel to create and edit questions
	//----------------------------------------------------------------------
	/**
	 * 
	 */
	//private static final long serialVersionUID = 1L;
	private JFrame frame;
	private RPanel mainPanel, leftPanel, rightPanel, questionPanel, finalButtonsPanel,
						answersPanel, potPanel, optionsPanel, typePanel,imagePanel,imageLabelPanel, 
						normAnswersPanel, tOrFAnswersPanel, decoysButtonsPanel,answersHoldPanel;
	
	private RButton saveButton, addButton, removeButton, editButton,addImageButton,
					addDecoyButton,removeDecoyButton,removeImageButton,addWordToDecoys;
					
	private JTextArea questionArea;
	private RLabel questionLabel, correctLabel, decoyLabel, typeLabel;
	private RCheckBox checkOpDecoyOnly, checkOpAllowPot, checkDefine,checkTorF, checkFillGap;
	private RCheckBox trueAnswer,falseAnswer;
	
	private JTextField addField,correctField,decoyField1;
	private ArrayList<JTextField> decoyFields;
	private ArrayList<RLabel> imageLabels;
	
	private RList<String> potListBox;
	private DefaultListModel<String> potListModel;
	private JScrollPane questionAreaPane,potListPane;
	
	private User mainUser;
	private Subject selectedSubject;
	private String imagesFolder;
	
	JFileChooser imageChooser = new JFileChooser();
	FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files","bmp","gif","jpeg","jpg","png");

	//TESTING
//	public static void main(String[]args){
//		CreateQuestionGuiPanel test = new CreateQuestionGuiPanel();
//		test.setupFrame();
//	}
//	
	//----------------------------------------------------------------------
	//	CONSTRUCTOR
	//----------------------------------------------------------------------
	public CreateQuestionGuiPanel(JFrame caller,Subject selected){
		//REMEMBER: DATA FIRST!!! BEFORE GUI
		selectedSubject = selected;
		selectedSubject.getPotListFromDB();
		//store images in subjectfolder
		imagesFolder = "Data/images/"+(selected.getId().toUpperCase());
		
		imageChooser.setFileFilter(filter);
		setupMainPanel();
		frame = caller;
		
		setupFrame();
		//System.out.println(frame.getSize().toString());
	}//end of CreateQuestionGui

	//----------------------------------------------------------------------
	//	CONSTRUCTOR: when user wants to edit a question
	//----------------------------------------------------------------------
	public CreateQuestionGuiPanel(JFrame caller,Subject selected,Question question){
		
	}//end of CreateQuestionGui
	
	//----------------------------------------------------------------------
	//	GET MAIN PANEL 
	//----------------------------------------------------------------------
	public RPanel getMainPanel(){
		return mainPanel;
	}//end getMainPanel()
	
	//----------------------------------------------------------------------
	//	MAIN FRAME: Calls setupMainPanel() 
	//----------------------------------------------------------------------
	public void setupFrame(){
		frame.getContentPane().removeAll();
	
		frame.getContentPane().add(getMainPanel());
		frame.setTitle("Retention - Create New Question");
		
		frame.revalidate();
		//frame.pack();
		frame.setMinimumSize(new Dimension(750,536));
	}//end of setupFrame
	
	//----------------------------------------------------------------------
	//	MAIN PANEL: calls setupRightPanel(), setupLefPanel() before adding 
	//	components(JPanel) leftPanel and rightPanel to mainPanel; see NB
	//----------------------------------------------------------------------
	public void setupMainPanel(){
		BorderLayout border = new BorderLayout();
		mainPanel = new RPanel(border);
		
		RPanel centerMain = new RPanel();
		centerMain.setLayout(new BoxLayout(centerMain, BoxLayout.X_AXIS));
		
		setupTypePanel();
		mainPanel.add(typePanel,BorderLayout.NORTH);
		
		setupLeftPanel();
		setupRightPanel();
		centerMain.add(leftPanel);
		centerMain.add(Box.createHorizontalStrut(10));
		centerMain.add(rightPanel);
		mainPanel.add(centerMain, BorderLayout.CENTER);
		
		setupFinalButtonsPanel();
		mainPanel.add(finalButtonsPanel, BorderLayout.SOUTH);
	}//end setupMainPanel()
	
	//----------------------------------------------------------------------
	//	RIGHT PANEL:  calls setupPotPanel(),setupOptionsPanel() before 
	//	adding both components to leftPanel
	//----------------------------------------------------------------------
	private void setupRightPanel(){
		rightPanel = new RPanel();//new GridLayout(3,0));
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
		
		setupPotPanel();
		rightPanel.add(potPanel);
		
		rightPanel.add(Box.createVerticalStrut(10));
		
		setupOptionsPanel();
		rightPanel.add(optionsPanel);
		
		optionsPanel.setAlignmentX(JPanel.CENTER_ALIGNMENT);
		
		Border rightBorder = BorderFactory.createMatteBorder(0,3,0,0,Color.DARK_GRAY);
	
		rightPanel.setBorder(rightBorder);
		//rightPanel.setPreferredSize(new Dimension(50,50));
	}//end setupRightPanel()
	
	//----------------------------------------------------------------------
	//	LEFT PANEL: calls setupQuestionPanel(),setupAnswersPanel() and 
	//	setupImagesPanel() before adding both components to rightPanel
	//----------------------------------------------------------------------
	private void setupLeftPanel(){
		leftPanel = new RPanel();
		leftPanel.setLayout(new BoxLayout(leftPanel,BoxLayout.Y_AXIS));
		
		setupQuestionPanel();
		leftPanel.add(questionPanel);
		
		setupImagesPanel();
		leftPanel.add(imagePanel);
		//questionPanel.add(imagePanel,BorderLayout.SOUTH);
		
		setupAnswersPanel();
		RPanel holder = new RPanel();
		holder.add(answersPanel);
		leftPanel.add(holder);
	}//end of setupLeftPanel()
	
	//----------------------------------------------------------------------
	//	TYPE PANEL: panel for check-boxes indicating the question format
	//----------------------------------------------------------------------
	private void setupTypePanel(){
		FlowLayout flow = new FlowLayout(FlowLayout.LEFT);
		typePanel = new RPanel(flow);
			
		typeLabel = new RLabel("SELECT QUESTION TYPE:");
			
		checkDefine = new RCheckBox("Defination",true);
		checkTorF = new RCheckBox("T/F");
		checkFillGap = new RCheckBox("Fill in the Gap");
			
		ButtonGroup questionTypeGroup = new ButtonGroup();
		questionTypeGroup.add(checkDefine);
		questionTypeGroup.add(checkTorF);
		questionTypeGroup.add(checkFillGap);
			
		typePanel.add(typeLabel);
		typePanel.add(checkDefine);
		typePanel.add(checkTorF);
		typePanel.add(checkFillGap);
		//CheckStateListener updates the Gui to display options related to selected checkbox
		checkTorF.addChangeListener(new CheckStateListener());
		checkFillGap.addChangeListener(new CheckStateListener());
		checkDefine.addChangeListener(new CheckStateListener());
		//checkFillGap.addActionListener(new CheckButtonListener());
	}//end of SetupTypePanel
				
	//----------------------------------------------------------------------
	//	QUESTION PANEL: panel for text area where questions can be typed
	//	//i may add an imagePanel to the southBorder 
	//----------------------------------------------------------------------
	private void setupQuestionPanel(){
		questionPanel = new RPanel(new BorderLayout());
		
		questionLabel = new RLabel("ENTER QUESTION:");
		questionArea = new JTextArea();
		questionArea.setColumns(15);
		questionArea.setLineWrap(true);
		questionArea.setWrapStyleWord(true);
		questionAreaPane = new JScrollPane(questionArea);
		
		questionPanel.add(questionLabel,BorderLayout.NORTH);
		questionPanel.add(questionAreaPane,BorderLayout.CENTER);
	}//end of setupQuestionPanel
	
	//----------------------------------------------------------------------
	//	IMAGES PANEL: sets up panel where image links can be added and 
	//	removed; could be added to South Border QuestionPanel
	//----------------------------------------------------------------------
	private void setupImagesPanel(){
		imageLabels = new ArrayList<RLabel>();
		
		imageLabelPanel = new RPanel();
		imageLabelPanel.setLayout(new BoxLayout(imageLabelPanel,BoxLayout.Y_AXIS));
		imagePanel = new RPanel();
		imagePanel.setLayout(new BoxLayout(imagePanel,BoxLayout.Y_AXIS));
		
		RPanel holdImageButton = new RPanel();
		addImageButton = new RButton("ADD NEW IMAGE");
		removeImageButton = new RButton("REMOVE LAST IMAGE");
		holdImageButton.add(addImageButton);
		holdImageButton.add(removeImageButton);
		
		addImageButton.addActionListener(new ButtonActionListener());
		removeImageButton.addActionListener(new ButtonActionListener());
		
		imagePanel.add(imageLabelPanel);
		imagePanel.add(holdImageButton);

		
	}//end of setupImagesPanel	
	
	//----------------------------------------------------------------------
	// ANSWERS PANEL: panel for text fields where the correct answer and 
	// decoys are typed
	//----------------------------------------------------------------------
	private void setupNormAnswersPanel(){
		answersHoldPanel = new RPanel();
		answersHoldPanel.setLayout(new BoxLayout(answersHoldPanel,BoxLayout.Y_AXIS));
		normAnswersPanel = new RPanel();
		normAnswersPanel.setLayout(new BoxLayout(normAnswersPanel,BoxLayout.Y_AXIS));
		
		correctLabel = new RLabel("ENTER CORRECT ANSWER:");//,JLabel.WEST);
		correctLabel.setAlignmentX(JLabel.TRAILING);
		decoyLabel = new RLabel("ENTER DECOYS:");//,JLabel.WEST);
		decoyLabel.setAlignmentX(JLabel.TRAILING);
		
		correctField = new JTextField(10);
		decoyField1 = new JTextField(10);
		
		decoyFields = new ArrayList<JTextField>();
		decoyFields.add(decoyField1);
		
		normAnswersPanel.add(correctLabel);
		normAnswersPanel.add(correctField);
		normAnswersPanel.add(decoyLabel);
		normAnswersPanel.add(decoyField1);
		
		//setupDecoys()?
		decoysButtonsPanel = new RPanel();
		addDecoyButton = new RButton("ADD DECOY");
		removeDecoyButton = new RButton("REMOVE LAST DECOY");
		decoysButtonsPanel.add(addDecoyButton);
		decoysButtonsPanel.add(removeDecoyButton);
		
		addDecoyButton.addActionListener(new ButtonActionListener());
		removeDecoyButton.addActionListener(new ButtonActionListener());
		
		answersHoldPanel.add(normAnswersPanel);
		answersHoldPanel.add(decoysButtonsPanel);
	}//end of setupAnswersPanel()
	
	//----------------------------------------------------------------------
	// TRUE/FALSE ANSWER PANEL: panel for selecting the correct answer in a
	//	true or false type question
	//----------------------------------------------------------------------
	private void setupTorFAnswersPanel(){
		tOrFAnswersPanel = new RPanel();
		
		trueAnswer = new RCheckBox("TRUE",true);
		falseAnswer = new RCheckBox("FALSE");
		
		ButtonGroup tOrF = new ButtonGroup();
		tOrF.add(trueAnswer);
		tOrF.add(falseAnswer);
		
		tOrFAnswersPanel.add(trueAnswer);
		tOrFAnswersPanel.add(falseAnswer);
	}//end of setupTorFAnswerPanel()
	
	//----------------------------------------------------------------------
	// SETUP ANSWERS PANEL
	//----------------------------------------------------------------------
	private void setupAnswersPanel(){
		answersPanel = new RPanel();
		setupNormAnswersPanel();
		setupTorFAnswersPanel();
		answersPanel.add(answersHoldPanel);
		answersPanel.add(tOrFAnswersPanel);
		tOrFAnswersPanel.setVisible(false);
	}//end of setupAnswersPanel()
	
	//----------------------------------------------------------------------
	//	TAGS PANEL: panel for display of possible tags to describe question;
	//	NB: called in setupLeftPanel() for design purposes(as opposed to 
	//	adding to the leftPanel in this method, making it tougher to achieve
	//  the desired visual design)
	//----------------------------------------------------------------------
	private void setupOptionsPanel(){
		optionsPanel = new RPanel();
		optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));
		
		RLabel tags = new RLabel("OPTIONS:");
		
		checkOpDecoyOnly = new RCheckBox("Use Decoys",true);
		checkOpAllowPot = new RCheckBox("Allow Pot Terms");
		
/*		ButtonGroup optionsGroup = new ButtonGroup();
		optionsGroup.add(checkOpDecoyOnly);
		optionsGroup.add(checkOpAllowPot);
*/		
		//tagTorF = new JCheckBox("True/False");
		
		checkOpDecoyOnly.addItemListener(new CheckOpListener());
		
		optionsPanel.add(tags);
		optionsPanel.add(checkOpDecoyOnly);
		optionsPanel.add(checkOpAllowPot);
		//tagsPanel.add(tagTorF);
	}
	
	//----------------------------------------------------------------------
	//	POT PANEL: sets up the display list of terms 
	//----------------------------------------------------------------------
	private void setupPotPanel(){
		potPanel = new RPanel();
		//potPanel.setLayout(new BoxLayout(potPanel,BoxLayout.X_AXIS));
		//potPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		
		//JPanel left = new JPanel(new BorderLayout());//List
		RPanel right = new RPanel();//Buttons
		//left.setLayout(new BoxLayout(left,BoxLayout.PAGE_AXIS));
		BoxLayout rightBox = new BoxLayout(right,BoxLayout.Y_AXIS);
		right.setLayout(rightBox);
		
		RLabel potLabel = new RLabel("TERMS:");

		potListModel = new DefaultListModel<String>();
		
		ArrayList<String> pot = selectedSubject.getPotList();
		for(String word: pot){
			potListModel.addElement(word);
		}
		
		potListBox = new RList<String>(potListModel);
		potListPane = new JScrollPane(potListBox);
		//potListPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

//		potListPane.setMinimumSize(new Dimension(50,50));
		potListPane.setPreferredSize(new Dimension(200,150));
//		potListPane.setMaximumSize(new Dimension(200,60));
		
		addField = new JTextField(5);
		addButton = new RButton("Add");
		editButton = new RButton("Edit");
		removeButton = new RButton("Remove");
		addWordToDecoys = new RButton("Use as Decoy");
		
		addButton.addActionListener(new ButtonActionListener());
		addWordToDecoys.addActionListener(new ButtonActionListener());
		right.add(potLabel);//,BorderLayout.NORTH);
		right.add(potListPane);//,BorderLayout.CENTER);
		
		right.add(new RLabel("ADD POT TERMS:"));
		right.add(addField);
		right.add(addButton);
		right.add(editButton);
		right.add(removeButton);
		right.add(addWordToDecoys);
		

		//potPanel.setBorder(BorderFactory.createMatteBorder(0,5,0,0,Color.DARK_GRAY));
		
		//JPanel leftFlow = new JPanel();
		//JPanel rightFlow = new JPanel();
		//leftFlow.add(left);
		//rightFlow.add(right);
		
		//potPanel.add(left);
		potPanel.add(Box.createHorizontalStrut(3));
		potPanel.add(right);//Flow);
		
		//addButton.setPreferredSize(addWordToDecoys.getSize());
		//editButton.setPreferredSize(addWordToDecoys.getSize());
		//removeButton.setPreferredSize(addWordToDecoys.getSize());
		
		//potPanel.setMaximumSize(new Dimension(200,200));

	}//end of setupPotPanel
	
	//----------------------------------------------------------------------
	//	FINAL BUTTONS PANEL: Save button
	//----------------------------------------------------------------------
	private void setupFinalButtonsPanel(){
		finalButtonsPanel = new RPanel();
		
		saveButton = new RButton("SAVE");
		
		saveButton.addActionListener(new ButtonActionListener());
		
		finalButtonsPanel.add(saveButton);
	}//end of setupFinalButtonsPanel
	
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
	// 	GET AND SET IMAGES FOLDER
	//---------------------------------------------------------------
	public String getImagesFolder() {
		return imagesFolder;
	}//end of getImagesFolder

	public void setImagesFolder(String imagesFolder) {
		this.imagesFolder = imagesFolder;
	}//end of setImagesFolder
	
	//---------------------------------------------------------------
	//	COPY IMAGE TO RETENTION DATA FOLDER
	//---------------------------------------------------------------
	public String copyImage(String link){

		File imagePath = new File(link);
		
		String newLink = imagesFolder + "/" + imagePath.getName();
		File newLinkFile = new File(imagesFolder);
		
		newLinkFile.mkdirs();
		
		FileChannel in = null;
		FileChannel out = null;
		try{
		in = new FileInputStream(imagePath.getAbsolutePath()).getChannel();
		out = new FileOutputStream(newLink).getChannel();
		
		out.transferFrom(in, 0, in.size());		
		}
		catch(IOException e){
			//e.printStackTrace();
			//Database.errorToString(e);
			Message.informUser(mainPanel,"Image Transfer Error",	Database.errorToString(e));
		}
		finally{
			try{
				if(in != null){
					in.close();
				}
				if(out != null){
					out.close();
				}
			}
			catch(IOException e){
				//e.printStackTrace();
				//Database.errorToString(e);
				Message.informUser(mainPanel,"Image Transfer Error",	Database.errorToString(e));
			}
		}
		
		return newLink;
	}//end copyImage
	
	//----------------------------------------------------------------------
	//	GET CREATED QUESTION:
	//----------------------------------------------------------------------
	public Question getCreatedQuestion(){
		int type = 0;
		String id = selectedSubject.generateNextQuestionID();
		String wording = questionArea.getText();
		String answer = "";
		String subjectID = selectedSubject.getId();
		ArrayList<String> decoys = new ArrayList<String>();
		ArrayList<String> images = new ArrayList<String>();
		User authour = mainUser;
		Dated createdNModified = new Dated();
		boolean allowPot,allowDecoy,useImage;
		allowPot = allowDecoy = useImage = false;
		
		//Get decoys()
		String decFieldString = "";
		boolean emptySpace = false;

		//Philosophical confusion on where to turn decoys to lower case Strings...question.java?
		for(JTextField decField : decoyFields){
			decFieldString = decField.getText();
			emptySpace = ((decFieldString.trim()).length() <= 0);
			if(!emptySpace){
				decoys.add(decFieldString);
			}
		}
		
		//Get imageLinks
		String imageLink =  "";
		boolean imageReoccur = false;
		int reccurImageIndex = -1;//if an image's link reoccurs it is set to greater>1
		
		for(RLabel imgLbl : imageLabels){
			imageLink = imgLbl.getText();
			reccurImageIndex = images.indexOf(imageLink);
			imageReoccur = (reccurImageIndex > -1);
			
			if(!imageReoccur){
				images.add(copyImage(imageLink));//images are Copied to Retention System Folder
				reccurImageIndex = -1;//unwarranted reset
			}
		}
		
		//get Question type
		if (checkDefine.isSelected()){
			type = 1;
		}
		else if(checkTorF.isSelected()){
			type = 2;
		}
		else{
			type =3;
		}
		
		//get Tags and Descriptors
		if(checkOpDecoyOnly.isSelected()){
			allowDecoy = true;
		}
		if(checkOpAllowPot.isSelected()){
			allowPot = true;
		}
		if(images.size()>0){
			useImage = true;
		}
		
		//get Answer
		if(type == 1){
			answer = correctField.getText();
		}
		else if(type == 2){
			allowDecoy = true;
			if(trueAnswer.isSelected()){
				answer = "True";
				decoys.add("False");
			}
			else{
				answer = "False";
				decoys.add("True");
			}
		}
		
		Question result = new Question(id,wording);
		result.setType(type);
		result.setAuthor(authour);
		result.setAnswer(answer);
		result.setDateCreated(createdNModified);
		result.setDateModified(createdNModified);
		result.setSubjectID(subjectID);
		result.setUseDecoys(allowDecoy);
		result.setUsePot(allowPot);
		result.setUseImages(useImage);
		result.setDecoys(decoys);
		result.setImageLinks(images);
		//System.out.println(selectedSubject.generateNextQuestionID());
		
		return result;

	}//end of getCreatedQuestion
	
	//----------------------------------------------------------------------
	//	
	//----------------------------------------------------------------------
	
/********************	
	//----------------------------------------------------------------------
	//	PRIVATE LISTENER CLASSES
	//----------------------------------------------------------------------
*********************/
	//----------------------------------------------------------------------
	//	LISTENS TO ADD DECOY, REMOVE LAST DECOY
	//----------------------------------------------------------------------
	private class ButtonActionListener implements ActionListener{
		
		public void addDecoy(){
			JTextField newDecoy = new JTextField(10);
			decoyFields.add(newDecoy);
			normAnswersPanel.add(decoyFields.get(decoyFields.size() - 1));
			normAnswersPanel.revalidate();
			newDecoy.requestFocusInWindow();
		}//end of addDecoy
		
		public void removeDecoy(){
			if(decoyFields.size() != 0){
				decoyFields.remove(decoyFields.size() - 1);
				normAnswersPanel.remove(normAnswersPanel.getComponentCount()-1);
				normAnswersPanel.revalidate();
			}
		}//end of removeDecoy
		
		public void addImage(){
			String path = "";
			int returnVal = imageChooser.showOpenDialog(frame);
			if (returnVal == JFileChooser.APPROVE_OPTION){
				path = imageChooser.getSelectedFile().getAbsolutePath();
				//System.out.println(path);
				RLabel imageLabel = new RLabel(path);
				imageLabel.setAlignmentX(JLabel.LEFT);
				imageLabels.add(imageLabel);
				imageLabelPanel.add(imageLabels.get(imageLabels.size() -  1));
				imageLabelPanel.revalidate();
			}

		}//end of addImage()
		
		public void removeImage(){
			if(imageLabels.size() != 0){
				imageLabels.remove(imageLabels.size() - 1);
				imageLabelPanel.remove(imageLabelPanel.getComponentCount()-1);
				imageLabelPanel.revalidate();
			}
		}//end of removeImage
		
		public void actionPerformed(ActionEvent e){

			Object source = e.getSource();
			
			if(source == addDecoyButton){
				addDecoy();
			}//addDecoyButton
			
			if(source == removeDecoyButton){//upgrade to selectable deletes
				removeDecoy();
			}//removeDecoyButton
			
			if(source == addImageButton){
				addImage();
			}//addImageButton
			
			if(source == removeImageButton){
				removeImage();
			}//removeImageButton
			
			if(source == saveButton){
				Question newQuestion = getCreatedQuestion();
				//if there is nothing written in the Question text area
				boolean noQWording = (newQuestion.getWording() == null) || (newQuestion.getWording().trim().equals(""));
				boolean noQImages = !newQuestion.usesImages();
				boolean noQAnswer = (newQuestion.getAnswer() == null) || (newQuestion.getAnswer().trim().equals(""));
				boolean noQuestion = (noQWording && noQImages) || (noQAnswer);
				
				if(!noQuestion){
					Database.addQuestion(newQuestion);
					QuestionsListGuiPanel continueSub = new QuestionsListGuiPanel(frame,selectedSubject);
					//continueSub.setSelectedSubject(selectedSubject);
					continueSub.setMainUser(mainUser);
					//System.out.println(newQuestion.toString2());
				}
				else{
					Message.informUser(mainPanel, "Question Create Error!","A Question with no words maybe asking too little or too much,\n and that is the problem");
					if(noQAnswer){
						Message.informUser(mainPanel, "Question Create Error!","I Forget, What is a rhetorical question?");
					}
				}
			}//saveButton
			
			if(source == addButton){
				//System.out.println(potListPane.getSize());
				//System.out.println(frame.getSize());
				System.out.println(Arrays.toString(selectedSubject.sortQuestions()));
			}
			
			if(source == addWordToDecoys){
				String word = potListBox.getSelectedValue();
				
				boolean noWord = (word == null) || word.trim().equals("");
				boolean wordExists = !noWord;
				//instead of this why not disable the button whenever nothing is selected on the list;
				if (wordExists){
					decoyFields.get(decoyFields.size() - 1).setText(word);
					normAnswersPanel.revalidate();
				}
				else{
					Message.informUser(mainPanel, "Pot Word Selection Problem","Pick a word, any word from the pot will do");
				}
				
			}
		}
	}
	
	//----------------------------------------------------------------------
	//	LISTENS TO CHECKTORF CHECKDEF CHECKFILLGAP
	//----------------------------------------------------------------------
	private class CheckStateListener implements ChangeListener{
		
		public void stateChanged(ChangeEvent e){
			Object source = e.getSource();
			
			if(source == checkTorF){
				if(checkTorF.isSelected()){
					normAnswersPanel.setVisible(false);
					tOrFAnswersPanel.setVisible(true);
					decoysButtonsPanel.setVisible(false);
					optionsPanel.setVisible(false);
				}
			}
			else if(source == checkFillGap||source == checkDefine){
				if(checkFillGap.isSelected()||checkDefine.isSelected()){
					tOrFAnswersPanel.setVisible(false);
					normAnswersPanel.setVisible(true);
					decoysButtonsPanel.setVisible(true);
					optionsPanel.setVisible(true);
				}
			}

		}
	}
	
	
	private class CheckOpListener implements ItemListener{
		
		public void itemStateChanged(ItemEvent e ){
			Object source = e.getSource();
			
			if (source == checkOpDecoyOnly){
				if(checkOpDecoyOnly.isSelected()){
					decoysButtonsPanel.setVisible(true);
					decoyLabel.setVisible(true);
					for(JTextField field: decoyFields){
						field.setVisible(true);
					}
				}
				else if(!(checkOpDecoyOnly.isSelected())){
					decoysButtonsPanel.setVisible(false);
					decoyLabel.setVisible(false);
					for(JTextField field: decoyFields){
						field.setVisible(false);
					}
				}
			}
		}
	}
}
