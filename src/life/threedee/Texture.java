package life.threedee;

import java.awt.image.BufferedImage;

public class Texture{
	
	public static final int STRETCH_NORMAL = 0;
	public static final int STRETCH_MIDDLE = 1;
	
	private static final int PX = 50;
	
	private BufferedImage texture;
	private int stretchStrategy;

	public Texture(BufferedImage texture, int stretchStrategy){
		super();
		this.texture = texture;
		this.stretchStrategy = stretchStrategy;
	}
	
	public BufferedImage stretch(double width, double height, Plane p, )
}
