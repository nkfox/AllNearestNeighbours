package allnearestneighbours;

/**
 * Created by Nataliia Kozoriz on 19.03.2016.
 */
public class VoronoiPoint extends Point  {

    VoronoiEdge firstEdge;

    public VoronoiPoint(int x,int y){
        super(x,y);
    }

    public VoronoiPoint(Point point){
        super(point.x,point.y);
    }
}
