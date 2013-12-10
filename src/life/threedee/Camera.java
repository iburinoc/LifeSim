package life.threedee;

import java.util.List;

public class Camera{

	private Point loc;
	private Vector dir;
	
	private double 
			width  = 1.53465397596, 
			height = 1.15099048197;
	private int
			screenWidth  = 480,
			screenHeight = 360;
	
	public Camera(double x,double y,double z){
		
	}
	
	private Vector getVectorForPixel(int x,int y){
		int rx = screenWidth / 2 - x;
		int ry = screenHeight / 2 - y;
	}

	private Plane closestInFront(){

	}
}
