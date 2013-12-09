package life.threedee;

import java.awt.Dimension;
import java.awt.Graphics;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class POC3D extends JPanel{
	
	private JFrame j;
	
	private List<Plane> objects;
	
	public POC3D(){
		j = new JFrame("Proof of Concept");
		
		this.setPreferredSize(new Dimension(1280,720));
		
		j.setSize(new Dimension(1280,720));
		j.setVisible(true);
		
	}
	
	@Override
	public void paintComponent(Graphics g){
		
	}
	
	public static void main(String[] args){
		new POC3D();
	}
}
