import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class App {

    public static void main(String [] args){

        Point p1 = new Point(300,300);
        Point p2 = new Point(500,600);
        Point p3 = new Point(200,900);
        Point p4 = new Point(900,400);
        Point p5 = new Point(950,800);
        Point p6 = new Point(600,800);
        Point p7 = new Point(100,100);
        //Triangle triangle = new Triangle(p1, p2, p3);
        List<Point> points = new ArrayList<>();
        points.add(p1);
        points.add(p2);
        points.add(p3);
        points.add(p4);
        points.add(p5);
        points.add(p6);
        points.add(p7);
        EnveloppeConvexe env = new EnveloppeConvexe(points);

        /*Triangulation triangulation = new Triangulation(env);
        for (Arete arete : triangulation.aretes) {
            System.out.println(arete.point1.x);
            System.out.println(arete.point1.y);
            System.out.println(arete.point2.x);
            System.out.println(arete.point2.y);
            System.out.println('\n');
        }*/
    }

}
