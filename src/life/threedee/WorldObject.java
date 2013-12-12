package life.threedee;

public class WorldObject {
    private Plane[] planes;
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
}
