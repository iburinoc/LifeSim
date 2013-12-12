package life.threedee;

public class WorldObject implements ThreeDeeObject{
    private ThreeDeeObject[] planes;
    private Point3D center;
    
    /* String is in format of (a, b, c);((a, b, c), (a, b, c), (a, b, c));...
     * First Point is center/origin point. All subsequent point triplets are corners of triangles.*/
    public static WorldObject generateObject(String str){
        str = str.replaceAll(" ", "");
        String[] strPoints = str.split(");(");
        String[] pointStr = strPoints[0].replaceAll("(", "").replaceAll(")", "").split(",");
        double cx = Double.parseDouble(pointStr[0]); 
        double cy = Double.parseDouble(pointStr[1]); 
        double cz = Double.parseDouble(pointStr[2]);
        Point3D center = new Point3D(cx, cy, cz);
        Plane[] planes = new Plane[strPoints.length-1];
        for (int i = 1; i < strPoints.length; i++) {
            String[] subPointStr = strPoints[i].split("),(");
            pointStr = strPoints[0].replaceAll("(", "").replaceAll(")", "").split(",");
        }
        return null;
    }
    public static WorldObject generateObject(String str){return null;}

	@Override
	public double calculateT(Vector v, Point3D p){
		double minT = 0;
		for(int i = 0; i < planes.length; i++){
			double t = planes[i].calculateT(v, p);
			if(t >= 0 && t < minT && t == t){
				minT = t;
			}
		}
		return minT;
	}

	@Override
	public Point3D intersection(Vector vector, Point3D point3D){
		double t = calculateT(vector, point3D);
		return intersection(vector, point3D, t);
	}
	
	@Override
	public Point3D intersection(Vector vector, Point3D point3D, double t){
		double nx = point3D.x + vector.x * t;
		double ny = point3D.y + vector.y * t;
		double nz = point3D.z + vector.z * t;
		return new Point3D(nx, ny, nz);
	}

	@Override
	public void translate(Vector v){
		center = new Point3D(center.x + v.x, center.y + v.y, center.z + v.z);
		for(ThreeDeeObject o : planes){
			o.translate(v);
		}
	}
}
