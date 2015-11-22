package ca.Ranti;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
//import java.net.MalformedURLException;
//import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
//import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.border.Border;

public class TestTakingGuiPanel{

	/**
	 * 
	 */
	//private static final long serialVersionUID = 1L;
	private JFrame frame;
	private JLabel question,countdown;
	private RPanel questionPanel, decoyPanel, imagesPanel,//controlButtonsPanel,
					fieldPanel,resultsPanel, timerPanel, mainPanel;
	private ArrayList<DecoyButton> decoyButtons;
	private Timer timer;
	//private JList<ImageIcon> pics;
	private int time;
	private JScrollPane imagePane;// = new JScrollPane();
	private JTextField answerField;
	private Test test;
	private User mainUser;
	private Subject selectedSubject;
	boolean reachedLast;
	boolean objectiveTypes;
	boolean answered; //prevents multiple answers reaching the answer ArrayList in the test Object, signaling
	
	Dimension emptySize = new Dimension(10,10);
	Color groundColor = new Color(165,200,200);
	Color darkColor = new Color(65,48,60);
	Color lighterBlueColor = new Color(241, 255, 255);
	Color decoyBackColor = new Color(65,48,50);
	Color correctGreenColor = new Color(160,255,160);
	Color wrongRedColor = new Color (255,130,130);
	
	Font enterAnswerFont  = new Font("Verdana",Font.ITALIC+Font.BOLD,14);
	Font answerFieldFont = new Font("Courier",Font.PLAIN,20);
	Font timeFont = new Font("Courier",Font.BOLD,18);
	Font decoyFont =  new Font("Verdana",Font.PLAIN,20);
	//TESTING
	//public static void main(String[]args){
	//	TestTakingGui gui = new TestTakingGui();
	//	gui.TestTakingGui();
	//}
	
	//----------------------------------------------------------------------
	//	CONSTRUCTOR: must call setupFrame()//Practically useless for now
	//----------------------------------------------------------------------
	public TestTakingGuiPanel(JFrame caller, TestCriteria testOps){
		//DATA
		test = new Test(testOps,selectedSubject.getId(),mainUser);
		//questions = test.getQuestions();
		//setCurrentQuestion(currentQNumber);
		answered = false;
		
		//GUI
		decoyButtons = new ArrayList<DecoyButton>();
		frame = caller;
		frame.setTitle("Retention - Test in Progress");
		setupMainPanel();
		setupFrame();
		//System.out.println(frame.getSize().toString());

	}//end of TestTakingGui
	
	//----------------------------------------------------------------------
	//	CONSTRUCTOR: must call setupFrame()
	//----------------------------------------------------------------------
	public TestTakingGuiPanel(JFrame caller, Test test){
		//DATA
		this.test = test;
		objectiveTypes = test.isAllowDecoys();
		//questions = test.getQuestions();
		//setCurrentQuestion(currentQNumber);
		answered = false;
		
		//GUI
		decoyButtons = new ArrayList<DecoyButton>();
		frame = caller;
		frame.setTitle("Retention - Test in Progress");
		setupMainPanel();
		setupFrame();
		//System.out.println(frame.getSize().toString());

	}//end of TestTakingGui

	//----------------------------------------------------------------------
	//	Get MainPanel
	//----------------------------------------------------------------------	
	public RPanel getMainPanel(){
		return mainPanel;
	}//end of getMainPanel
	
	//----------------------------------------------------------------------
	//	MAIN PANEL: must call setupMainPanel()
	//----------------------------------------------------------------------	
	private void setupFrame(){
		
		frame.getContentPane().removeAll();
		
		frame.getContentPane().add(getMainPanel());
		frame.revalidate();
		//frame.setPreferredSize(new Dimension(430,472));
		//frame.setHeight(500);
/*		if(test.getCurrentQuestion().usesImages()){
			if(frame.getWidth()< mainPanel.getWidth() || frame.getHeight()< mainPanel.getHeight())
				frame.pack();
		}*/
		frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
		
		
	//	frame.setPreferredSize(new Dimension(430,472));
		if(!objectiveTypes){
			answerField.requestFocusInWindow();
		}

	}//end of setupFrame
	
	//----------------------------------------------------------------------
	//	MAIN PANEL: must call setupQuestionPanel and setupTimerPanel()
	//----------------------------------------------------------------------
	public void setupMainPanel(){
		answered = false;
		
		mainPanel = new RPanel(new BorderLayout());
		Border rightBorder = BorderFactory.createMatteBorder(2,3,2,2,Color.DARK_GRAY);
		
		RPanel subMainPanel;// = new JPanel(new GridLayout(3,0));
		
		//IF THE Question uses images --> border layout
		if(test.getCurrentQuestion().usesImages()){
			subMainPanel = new RPanel(new BorderLayout());
			
			setupImagesPanel(test.getCurrentQuestion());
			subMainPanel.add(imagePane,BorderLayout.CENTER);
			
			imagesPanel.setBackground(groundColor);//controlButtonsPanel,
			imagePane.setBackground(groundColor);
			
			setupQuestionPanel(test.getCurrentQuestion());
			subMainPanel.add(questionPanel, BorderLayout.NORTH);
			
			if(objectiveTypes){
				setupDecoyPanel(test.getDecoyAnswer());
				subMainPanel.add(decoyPanel, BorderLayout.SOUTH);
				decoyPanel.setBackground(groundColor); 
			}
			else{
				setupFieldPanel();
				subMainPanel.add(fieldPanel, BorderLayout.SOUTH);
				fieldPanel.setBackground(groundColor);
			}
		}
		
		else {
			subMainPanel = new RPanel();
			
			setupQuestionPanel(test.getCurrentQuestion());
			if(objectiveTypes){
				subMainPanel.setLayout(new GridLayout(2,0));
				subMainPanel.add(questionPanel);
				
				setupDecoyPanel(test.getDecoyAnswer());
				subMainPanel.add(decoyPanel);
				decoyPanel.setBackground(groundColor); 
			}
			else{
				subMainPanel.setLayout(new BoxLayout(subMainPanel,BoxLayout.Y_AXIS));
				subMainPanel.add(questionPanel);
				setupFieldPanel();
				subMainPanel.add(fieldPanel);
				fieldPanel.setBackground(groundColor);
			}	
		}
		
		questionPanel.setBackground(groundColor);
		questionPanel.setBorder(rightBorder);
		
		subMainPanel.setBackground(groundColor);
		
		mainPanel.add(subMainPanel,BorderLayout.CENTER);
		
		setupTimerPanel();
		mainPanel.add(timerPanel,BorderLayout.NORTH);
		timerPanel.setBackground(groundColor);
		mainPanel.setBackground(groundColor);
	//	mainPanel.setPreferredSize(new Dimension(300,200));
	}//end of setupMainPanel
	
	//----------------------------------------------------------------------
	//	QUESTION PANEL: Display questions//uses imagesPanel
	//----------------------------------------------------------------------
	private void setupQuestionPanel(Question currentQ){
		
		//setupImagesPanel(currentQ);
		questionPanel = new RPanel(new BorderLayout());
		//questionPanel.add(imagesPanel);
		//Font wordFont = new Font("Arial",Font.PLAIN,20);
		String wording = "<html><BR>"
				+ "<p align=\"center\">"
				//+ "<font style=\"font-size: 24px; line-height: 2px; color: rgb(41, 41, 41); font-family: Verdana;\"> "
				+currentQ.getWording();
		//String wording = currentQ.getWording();
		if(currentQ.isTypeTrueFalse()){
			wording = wording + " (True/False)";
		}
		
		//haven't closed font
		wording = wording +"</p></html>";
		
		//JTextArea area = new JTextArea(wording);
		//area.append(wording);
		question = new JLabel(wording,JLabel.CENTER);
		question.setFont(Misc.questionFont);
/*		area.setBackground(questionPanel.getBackground());
		area.setFont(Misc.questionFont);
		area.setLineWrap(true);
		area.setEditable(false);
		area.setWrapStyleWord(true);*/
		
		//if (currentQ.usesImages())
			//questionPanel.add(imagePane, BorderLayout.NORTH);
		//else
		questionPanel.add(Box.createRigidArea(emptySize), BorderLayout.NORTH);
		questionPanel.add(Box.createRigidArea(emptySize), BorderLayout.SOUTH);
		questionPanel.add(Box.createRigidArea(emptySize), BorderLayout.EAST);
		questionPanel.add(Box.createRigidArea(emptySize), BorderLayout.WEST);
		
		questionPanel.add(question,BorderLayout.CENTER);
	}//end of setupQuestionPanel
	
	//----------------------------------------------------------------------
	//	IMAGES PANEL: Displays at most two images if question uses images
	//----------------------------------------------------------------------
	private void setupImagesPanel(Question currentQ){
		imagesPanel  = new RPanel();
		imagePane = new JScrollPane();
		
		boolean usesImages = currentQ.usesImages();
		
		if(usesImages){
			imagePane = new JScrollPane(imagesPanel);
			ArrayList<String> links = currentQ.getImageLinks();
			int i = 0;
			while (i<2){
				ImageIcon testImage= new ImageIcon(links.get(i));
				JLabel image = new JLabel(testImage);

				imagesPanel.add(image);
				imagePane.setPreferredSize(new Dimension (testImage.getIconWidth(),testImage.getIconHeight()));
				
				i++;
				
				//break
				if(links.size() == 1){i++;}
			}
			
		}//if useImages
	}//end of setupImagesPanel
	
	//----------------------------------------------------------------------
	//	DECOY PANEL: Displays the decoys and the answer;
	//----------------------------------------------------------------------
	private void setupDecoyPanel(ArrayList<String> testDecoys){
		decoyPanel = new RPanel();
		decoyPanel.setLayout(new GridLayout(testDecoys.size(),0,2,2));
		//decoyPanel.setLayout(new BoxLayout(decoyPanel, BoxLayout.Y_AXIS));
		for(String decAns: testDecoys){
/*			String htmlVer = "<html>" + "<p align=\"center\">"
							+ "<font style=\"font-size: 12px; line-height: 0px; color: rgb(241, 255, 255); font-family: Verdana;\"> "
							+ (decAns) + "</p></font></html>";*/
			DecoyButton answerOp = new DecoyButton(decAns);
			//answerOp.setSize(300,20);
			
			//answerOp.setBorder(null);
			answerOp.addActionListener(new DecoyListener());
			decoyButtons.add(answerOp);
			decoyPanel.add(decoyButtons.get(decoyButtons.size()-1));
			//decoyPanel.add(answerOp);
			//Dimension buttonSize = answerOp.getSize();
		}
		
	}//end of setupDecoyPanel
	
	//----------------------------------------------------------------------
	//	FIELD PANEL: Displays a text field to enter answers;
	//----------------------------------------------------------------------
	private void setupFieldPanel(){
		fieldPanel = new RPanel();
		fieldPanel.setLayout(new BoxLayout(fieldPanel, BoxLayout.Y_AXIS));
		
		JLabel instruct = new JLabel("(Hit Enter to Submit)",JLabel.RIGHT);
		instruct.setFont(new Font("Arial",Font.ITALIC,12));
		
		/*JLabel enterAnswer = new JLabel ("<html><font style =\"font-size: 12px; color: rgb (241,255,255); font-style:italic;\">"
										+ "Answer:"
										+ "</font></html>");*/
		JLabel enterAnswer = new JLabel("Answer:");
		enterAnswer.setFont(enterAnswerFont);
		enterAnswer.setForeground(lighterBlueColor);

		
		answerField = new JTextField(65);
		answerField.setFont(answerFieldFont);
		answerField.setPreferredSize(new Dimension(300,40));
		RPanel subFieldP = new RPanel();
		subFieldP.add(enterAnswer);
		subFieldP.add(answerField);
		
		subFieldP.setBackground(darkColor);
		enterAnswer.setBackground(lighterBlueColor);
		
		fieldPanel.add(instruct);
		fieldPanel.add(subFieldP);
		
		answerField.addActionListener(new DecoyListener());
	}//end of setupDecoyPanel
	
	//----------------------------------------------------------------------
	//	RESULTS PANEL: DISPLAYS THE RESULT OF CURRENT QUIZ
	//----------------------------------------------------------------------
	private void setupResultsPanel(){
		resultsPanel = new RPanel(new GridLayout(2,0));
		
		long seed = System.nanoTime();
		Random randGen = new Random(seed);
		
		String coolPath = "Data/images/TestEnd/Cool";
		String uncoolPath = "Data/images/TestEnd/UnCool";
		String impossiblePath = "Data/images/TestEnd/Impossible";
		
		//URL url = null;
		String chosenPic = "";
		String [] pics;
		File picsDir;
		int pick;
		
		boolean coolEasy = (test.getPercentage() > 85.0) &&  objectiveTypes;
		boolean coolHard = (test.getPercentage() > 70.0) && !objectiveTypes;
		boolean cool = (coolHard || coolEasy) ;
		boolean unbelievableHard = (test.getPercentage() > 90.00) && !(objectiveTypes) && (test.getNumOfQuestion() > 10);
		//try{
		if(unbelievableHard){
			picsDir = new File(impossiblePath);
			pics = picsDir.list();
			pick = randGen.nextInt(pics.length);
			chosenPic = impossiblePath+"/"+pics[pick];
			//url = new URL(impossiblePath +"/" +chosenPic);
		}
		else if(cool){
			picsDir = new File(coolPath);
			pics = picsDir.list();
			pick = randGen.nextInt(pics.length);
			chosenPic = coolPath+"/"+ pics[pick];
			//url = new URL(coolPath +"/" +chosenPic);
		}
		else{
			picsDir = new File(uncoolPath);
			pics = picsDir.list();
			pick = randGen.nextInt(pics.length);
			chosenPic = uncoolPath+"/"+  pics[pick];
			//url = new URL(uncoolPath +"/" +chosenPic);
		}
		//System.out.println(chosenPic);
		
		 
		 //BufferedImage img = ImageIO.read(url);
		resultsPanel.add(new JLabel(new ImageIcon(chosenPic)));
/*		}
		catch(MalformedURLException e){
			e.printStackTrace();
		}*/
/*		catch(IOException e){
			e.printStackTrace();
		}*/
		 //HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		
		//resultsPanel.add(new JLabel(test.toString()));

		JTextArea summary = new JTextArea(test.toString());
		JScrollPane summPane = new JScrollPane(summary);
		Font wordFont = new Font("Courier New",Font.BOLD,16);
		summary.setFont(wordFont);
		summary.setLineWrap(true);
		summary.setEditable(false);
		summary.setWrapStyleWord(true);
		
		RButton retakeButton = new RButton("REDEEM YOURSELF!!!");
		retakeButton.setName("retakeButton");
		retakeButton.addActionListener(new ButtonListener());
		
		RPanel holdPaneAndButton = new RPanel();
		holdPaneAndButton.setLayout(new BoxLayout(holdPaneAndButton, BoxLayout.Y_AXIS));
		
		holdPaneAndButton.add(summPane);
		holdPaneAndButton.add(retakeButton);
		
		resultsPanel.add(holdPaneAndButton);
		//System.out.println(test);
	}//end of setupResultsPanel()
	
	//----------------------------------------------------------------------
	//	TIMER PANEL: Display questions
	//----------------------------------------------------------------------
	private void setupTimerPanel(){
		time = test.getTime();
		TimerListener timerListen = new TimerListener();
		timer = new Timer(1000, timerListen);
		
		timerPanel = new RPanel(new FlowLayout(FlowLayout.RIGHT));
		countdown = new JLabel("TIME: "+ time);
		countdown.setFont(timeFont);
		timerPanel.add(countdown);
		
		timer.start();
	}//end of setupTimerPanel
	
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
	
/***************************************************************************	
	//----------------------------------------------------------------------
	//	PRIVATE CLASSES
	//----------------------------------------------------------------------
***************************************************************************/
	
	private class TimerListener implements ActionListener{
		
		public void actionPerformed(ActionEvent e){
			//use a global variable for ... "Retention - Test in Progress"
			boolean userOut = !(frame.getTitle().equals("Retention - Test in Progress"));
			if(!userOut){
				time = time - 1;
				countdown.setText("TIME: "+ time);
				reachedLast = test.isLast();
				
				//set time panel colors
				int red =   165 + (5* (test.getTime() - time));
					red = (red > 255)? 255: red;
				int green = 200 - (5* (test.getTime() - time));
					green = (green < 0)? 0: green;
				int blue = green;
				
				if(time<8){
					
					if(time%2 == 1){
						timerPanel.setBackground(groundColor);
						countdown.setForeground(Color.black);
					}
					else
						timerPanel.setBackground(new Color((red),green,blue));
					
					//countdown.setForeground(Color.white);
					countdown.setFont(timeFont);
				}
				
				//Program Logic
				if(time == 0 && !reachedLast){
					timer.stop();
					
					if(objectiveTypes){
						if(!answered){
							String answer = "{NONE}";
							test.addAnswer(answer);
						}
						else if(answered){
							String answer = "SuperCalifragalisticEspialidocious";
							test.calculateScore(answer,time);
						}
					}
					else if(!objectiveTypes){
						if(!answered){
							String answer ="";
							answer = answerField.getText();
							test.addAnswer(answer);
						}
						/*else if(answered){
						}*/
					}
					
					test.nextQuestion();
					setupMainPanel();
					setupFrame();
					mainPanel.revalidate();
					//frame.revalidate();
					//time = 3;
				}//if time == 0 and not ReachedLast
				else if(time == 0 && reachedLast){
					timer.stop();
					frame.getContentPane().removeAll();
					setupResultsPanel();
					frame.getContentPane().add(resultsPanel);
					resultsPanel.setBackground(groundColor); 
					frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
					frame.revalidate();
				}//else if time == 0 and reachedLast
			}// if user not  out
			else if(userOut){
				timer.stop();
			}//if user out
		}
	}
	
	private class DecoyListener implements ActionListener{
		
		public void actionPerformed(ActionEvent e){
			Object source = e.getSource();
			boolean hardCorrect = false;
			
			//QUESTIONS USE DECOY BUTTONS
			if(objectiveTypes){
				//String answer = ((JButton)source).getText().replaceAll("\\<.*?>","");
				String answer = ((JButton)source).getText();
				//System.out.println(answer);
				boolean correct = test.calculateScore(answer,time);
				answered = true;

				if(correct){
					((JButton)source).setBackground(correctGreenColor);
				}
				else if(!correct){
					((JButton)source).setBackground(wrongRedColor);
				}
				
				//disable clicking
				for(JButton dec: decoyButtons){
					dec.setEnabled(false);
				}
			}
			
			//QUESTIONS USE ONLY TEXT BOXES i.e textFieldTypes
			else if(!objectiveTypes){
				String answer = ((JTextField)source).getText();
				boolean correct = test.calculateScore(answer,time);
				hardCorrect = correct;
				
				if(correct){
					((JTextField)source).setEnabled(false);
					//((JTextField)source).setText("");
					((JTextField)source).setBackground(correctGreenColor);
					answered = true;

				}
				else if(!correct){
					((JTextField)source).setBackground(wrongRedColor);
					answered = false;
				}

			}
			
			if(objectiveTypes || (!objectiveTypes && hardCorrect)){
				time = 1;
			}
			
		}
	}
	
	private class ButtonListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			Object source = e.getSource();
			
			if(((RButton)source).getName().equals("retakeButton")){
				new TestTakingGuiPanel(frame,new Test(test));
			}
		}
	}
	
}
