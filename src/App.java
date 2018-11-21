import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class App {

    public static void main(String [] args){
        Point p1 = new Point(1,2);
        Point p2 = new Point(6,4);
        Point p3 = new Point(8,6);
        Point p4 = new Point(4,1);
        Point p5 = new Point(3,8);
        Point p6 = new Point(9,5);
        List<Point> points = new ArrayList<>();
        points.add(p1);
        points.add(p2);
        points.add(p3);
        points.add(p4);
        points.add(p5);
        points.add(p6);
        EnveloppeConvexe env = new EnveloppeConvexe(points);
        for (Point point: env.points) {
            System.out.println(point.x);
        }
    }

}
