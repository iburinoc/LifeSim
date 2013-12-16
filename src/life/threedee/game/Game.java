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
	
	private boolean running;
	
	public Game() {
		j = new JFrame("Game");
		p = new Player(this);
		setObjects(new ArrayList<ThreeDeeObject>());
		setTickables(new ArrayList<Tickable>());
		
		running = true;
		
		j.add(p);
		j.setVisible(true);
		j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	@Override
	public void run() {
		long time = System.currentTimeMillis();
		int tick_delta = 0;
		int frame_delta = 0;
		
		int tickRateMillis = 1000/TICK_RATE;
		int frameRateMillis = 1000/FRAME_RATE;
		while(running) {
			long t = System.currentTimeMillis();
			delta = 
		}
	}
	
	private void tickTickables() {
		
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
