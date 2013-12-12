package life.threedee;

public class WorldObject {
    private Plane[] planes;
    private Point3D center;
    
    /* String is in format of (a, b, c),((a, b, c), (a, b, c), (a, b, c)),...
     * First Point is center/origin point. All subsequent point triplets are corners of triangles.*/
    public static WorldObject generateObject(String str){return null;}
}
