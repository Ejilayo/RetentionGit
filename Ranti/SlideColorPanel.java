import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;

public class SlideColorPanel extends JPanel{
	
	private JPanel controls, colorPanel;
	private JSlider rS, gS, bS;
	private JLabel rL, gL , bL;
	
	public SlideColorPanel(){
		
		rS = new JSlider(JSlider.HORIZONTAL, 0, 255, 0);
		rS.setMajorTickSpacing(50);
		rS.setMinorTickSpacing(5);
		rS.setPaintTicks(true);
		rS.setPaintLabels(true);
		rS.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		gS = new JSlider(JSlider.HORIZONTAL, 0, 255, 0);
		gS.setMajorTickSpacing(50);
		gS.setMinorTickSpacing(5);
		gS.setPaintTicks(true);
		gS.setPaintLabels(true);
		gS.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		bS = new JSlider(JSlider.HORIZONTAL, 0, 255, 0);
		bS.setMajorTickSpacing(50);
		bS.setMinorTickSpacing(5);
		bS.setPaintTicks(true);
		bS.setPaintLabels(true);
		bS.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		rL = new JLabel();
		rL.setText("Red: 0");
		rL.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		gL = new JLabel();
		gL.setText("Green: 0");
		gL.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		bL = new JLabel();
		bL.setText("Blue: 0");
		bL.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		SlideColorListener listener = new SlideColorListener();
		rS.addChangeListener(listener);
		gS.addChangeListener(listener);
		bS.addChangeListener(listener);
		
		controls = new JPanel();
		BoxLayout layout = new BoxLayout(controls, BoxLayout.Y_AXIS);
		controls.setLayout(layout);
		controls.add(rL);
		controls.add(rS);
		controls.add(Box.createRigidArea(new Dimension(0,20)));
		controls.add(gL);
		controls.add(gS);
		controls.add(Box.createRigidArea(new Dimension(0,20)));
		controls.add(bL);
		controls.add(bS);
		
		colorPanel = new JPanel();
		colorPanel.setPreferredSize(new Dimension(100,100));
		colorPanel.setBackground(new Color(0,0,0));
		
		add(controls);
		add(colorPanel);
	}
	
	private class SlideColorListener implements ChangeListener{
		
		public void stateChanged(ChangeEvent evt){
			int red, green, blue;
			
			red = rS.getValue();
			green = gS.getValue();
			blue = bS.getValue();
			
			rL.setText("Red: "+ red);
			gL.setText("Green: " + green);
			bL.setText("Blue: " + blue);
			
			colorPanel.setBackground(new Color(red,green,blue));
		}
	}
}
