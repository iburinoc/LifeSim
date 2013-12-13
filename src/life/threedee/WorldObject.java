package life.threedee;

import java.awt.Color;

public class WorldObject implements ThreeDeeObject{
    private ThreeDeeObject[] planes;
    private Point center;
    
    public WorldObject(ThreeDeeObject[] planes, Point center) {
        this.planes = planes;
        this.center = center;
    }

    /* String is in format of (a, b, c);((a, b, c), (a, b, c), (a, b, c));...
     * First Point is center/origin point. All subsequent point triplets are corners of triangles.*/
    public static WorldObject generateObject(String str){
        str = str.replaceAll(" ", "");
        String[] strPoints = str.split("\\);\\(");
        String[] pointStr = strPoints[0].replaceAll("\\(", "").replaceAll("\\)", "").split(",");
        double cx = Double.parseDouble(pointStr[0]); 
        double cy = Double.parseDouble(pointStr[1]); 
        double cz = Double.parseDouble(pointStr[2]);
        Point center = new Point(cx, cy, cz);
        ThreeDeeObject[] planes = new ThreeDeeObject[strPoints.length-1];
        for (int i = 1; i < strPoints.length; i++) {
            String[] subPointStr = strPoints[i].split("\\),\\(");
            Point[] points = new Point[3];
            for (int j = 0; j < subPointStr.length; j++) {
                pointStr = subPointStr[j].replaceAll("\\(", "").replaceAll("\\)", "").split(",");
                double x = Double.parseDouble(pointStr[0]); 
                double y = Double.parseDouble(pointStr[1]); 
                double z = Double.parseDouble(pointStr[2]);
                points[j] = new Point(x, y, z);
            }
            planes[i-1] = new Triangle(points[0], points[1], points[2]);
        }
        return new WorldObject(planes, center);
    }

	@Override
	public double calculateT(Vector v, Point p){
		double minT = Double.MAX_VALUE;
		for(int i = 0; i < planes.length; i++){
			double t = planes[i].calculateT(v, p);
			if(t >= 0 && t < minT && t == t){
				minT = t;
			}
		}
		return minT;
	}

	@Override
	public Point intersection(Vector vector, Point point){
		double t = calculateT(vector, point);
		return intersection(vector, point, t);
	}
	
	@Override
	public Point intersection(Vector vector, Point point, double t){
		double nx = point.x + vector.x * t;
		double ny = point.y + vector.y * t;
		double nz = point.z + vector.z * t;
		return new Point(nx, ny, nz);
	}

	@Override
	public void translate(Vector v){
		center = new Point(center.x + v.x, center.y + v.y, center.z + v.z);
		for(ThreeDeeObject o : planes){
			o.translate(v);
		}
	}
	
	@Override
	public Color c(){
		return Color.magenta;
	}
}
