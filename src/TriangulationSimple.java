import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

public class TriangulationSimple {
    EnveloppeConvexe enveloppeConvexe;
    List<Triangle> triangles=new ArrayList<>();
    ArrayDeque<Point> pointsRestants = new ArrayDeque<>();
    //tous les points actuels de la triangulation
    List<Point> pointsUtilises = new ArrayList<>();

    public TriangulationSimple(EnveloppeConvexe enveloppeConvexe){
        this.enveloppeConvexe = enveloppeConvexe;
        buildTriangulation();
    }

    public void buildTriangulation(){
        for (Point point : enveloppeConvexe.points) {
            pointsRestants.add(point);
        }
        for (Point point: enveloppeConvexe.enveloppeConvexe) {
            pointsRestants.remove(point);
            pointsUtilises.add(point);
        }
        Point pointAAjouter = pointsRestants.poll();
        for(int i=0; i<enveloppeConvexe.enveloppeConvexe.size()-1; ++i){
            triangles.add(new Triangle(enveloppeConvexe.enveloppeConvexe.get(i),enveloppeConvexe.enveloppeConvexe.get(i+1),pointAAjouter));
        }
        triangles.add(new Triangle(enveloppeConvexe.enveloppeConvexe.get(enveloppeConvexe.enveloppeConvexe.size()-1),enveloppeConvexe.enveloppeConvexe.get(0),pointAAjouter));
        pointsUtilises.add(pointAAjouter);
        while(!pointsRestants.isEmpty()) {
            Point point = pointsRestants.poll();
            Triangle triangle = findTriangleContainingPoint(point);
            //on ajoute les 3 nouveaux triangles et on supprime l'ancien
            triangles.add(new Triangle(triangle.p1, triangle.p2, point));
            triangles.add(new Triangle(triangle.p2, triangle.p3, point));
            triangles.add(new Triangle(triangle.p3, triangle.p1, point));
            triangles.remove(triangle);

            pointsUtilises.add(point);
        }
    }

    private Triangle findTriangleContainingPoint(Point point) {

        double x = point.x;
        double y = point.y;

        for (Triangle triangle : triangles) {

            //Equation cartésienne du premier côté
            double a1 = -(triangle.p2.y - triangle.p1.y);
            double b1 = triangle.p2.x - triangle.p1.x;
            double c1 = (-a1 * triangle.p1.x) - (b1 * triangle.p1.y);

            //si le paramètre point et le troisième point du triangle ne sont pas dans le même demi-plan, on passe au triangle suivant
            if (!((a1 * x + b1 * y + c1 <= 0 && a1 * triangle.p3.x + b1 * triangle.p3.y + c1 <= 0) || (a1 * x + b1 * y + c1 >= 0 && a1 * triangle.p3.x + b1 * triangle.p3.y + c1 >= 0)))
                continue;

            //On répète pour les 2 autres côtés
            double a2 = -(triangle.p3.y - triangle.p2.y);
            double b2 = triangle.p3.x - triangle.p2.x;
            double c2 = (-a2 * triangle.p2.x) - (b2 * triangle.p2.y);

            if (!((a2 * x + b2 * y + c2 <= 0 && a2 * triangle.p1.x + b2 * triangle.p1.y + c2 <= 0) || (a2 * x + b2 * y + c2 >= 0 && a2 * triangle.p1.x + b2 * triangle.p1.y + c2 >= 0)))
                continue;

            double a3 = -(triangle.p1.y - triangle.p3.y);
            double b3 = triangle.p1.x - triangle.p3.x;
            double c3 = (-a3 * triangle.p3.x) - (b3 * triangle.p3.y);

            if (!((a3 * x + b3 * y + c3 <= 0 && a3 * triangle.p2.x + b3 * triangle.p2.y + c3 <= 0) || (a3 * x + b3 * y + c3 >= 0 && a3 * triangle.p2.x + b3 * triangle.p2.y + c3 >= 0)))
                continue;

            return triangle;
        }
        return null;
    }
}
