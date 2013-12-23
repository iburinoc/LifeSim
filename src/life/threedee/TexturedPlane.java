package life.threedee;

import static java.lang.Math.PI;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class TexturedPlane extends Plane{
	private BufferedImage texture;
	
	private Vector right, up;

	public TexturedPlane(Point p, Vector n, BufferedImage texture){
		super(p, n);
		this.texture = texture;
		
		double[] dirPolar = this.normal.polarTransform();
		
		up = Vector.fromPolarTransform(dirPolar[0], PI/2 + dirPolar[1], 1);
		right = Vector.fromPolarTransform(dirPolar[0] - PI/2, 0, 1);
	}
	
	public Color c(Point inter) {
		Vector v = new Vector(this.origin, inter);
		throw new RuntimeException();
	}
}
