/**
 * Created by Vincent on 01/12/2018.
 */
public class Arete {

    public Point point1;
    public Point point2;

    public Arete(Point point1, Point point2) {
        this.point1 = point1;
        this.point2 = point2;
    }

    public boolean existeDeja(Point p1, Point p2){
        return (p1 == point1 && p2==point2) || (p1 == point2 && p2 == point1);
    }

    public boolean contientPoint(Point point){
        return point == point1 || point == point2;
    }

    public Point autrePoint(Point point){
        if(point == point1) return point2;
        else if (point == point2) return point1;
        return null;
    }
}
