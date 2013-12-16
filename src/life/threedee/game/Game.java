package life.threedee.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JFrame;

import life.threedee.ThreeDeeObject;

public class Game implements Runnable{
	
	public static final int TICK_RATE = 10;
	public static final int FRAME_RATE = 30;
	
	public static void main(String[] args){
		new Thread(new Game()).start();
	}
	
	private List<ThreeDeeObject> objects;
	private List<Tickable> tickables;
	
	private Player p;
	
	private JFrame j;
	
	private Input i;
	
	private boolean running;
	
	public Game() {
		j = new JFrame("Game");
		p = new Player(this);
		setObjects(new ArrayList<ThreeDeeObject>());
		setTickables(new ArrayList<Tickable>());
		
		i = new Input(p, this, j);
		
		running = true;
		
		j.addMouseListener(i);
		j.addMouseMotionListener(i);
		j.addMouseWheelListener(i);
		j.addKeyListener(i);
		
		j.add(p);
		j.pack();
		j.setVisible(true);
		j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	@Override
	public void run() {
		long tick_time = System.currentTimeMillis();
		int tick_delta = 0;
		
		long frame_time = System.currentTimeMillis();
		int frame_delta = 0;
		
		int tickRateMillis = 1000/TICK_RATE;
		int frameRateMillis = 1000/FRAME_RATE;
		while(running) {
			long frameT = System.currentTimeMillis();
			frame_delta = (int) (frameT - frame_time);
			if(frame_delta >= frameRateMillis) {
				drawFrame();
				frame_time = frameT;
			}
			
			long tickT = System.currentTimeMillis();
			tick_delta = (int) (tickT - tick_time);
			if(tick_delta >= tickRateMillis) {
				tickTickables(tick_delta);
				tick_time = tickT;
			}
		}
	}
	
	private void drawFrame() {
		p.calcBuffer();
		p.repaint();
	}
	
	private void tickTickables(int delta) {
		for(int i = 0; i < tickables.size(); i++){
			tickables.get(i).tick(delta);
		}
	}
	
	public void addTickable(Tickable t) {
		tickables.add(t);
	}
	
	public void setTickables(List<Tickable> l) {
		tickables = Collections.synchronizedList(l);
	}
	
	public List<Tickable> tickables() {
		return tickables;
	}
	
	public void addObject(ThreeDeeObject o) {
		objects.add(o);
	}
	
	public void setObjects(List<ThreeDeeObject> o){
		objects = Collections.synchronizedList(o);
	}
	
	public List<ThreeDeeObject> objects() {
		return objects;
	}
}
