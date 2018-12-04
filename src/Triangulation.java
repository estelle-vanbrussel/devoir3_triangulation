import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Vincent on 01/12/2018.
 */
public class Triangulation {

    //tous les points
    List<Point> points;
    //points appartenant à l'enveloppe (constante)
    List<Point> pointsEnveloppe;
    //points appartenant au polygone actuel
    List<Point> pointsPolygone;
    //points reliés à aucune arête
    List<Point> pointsLibres;
    // les arêtes formant le polygone dans lequel on fait la triangulation
    Set<Arete> aretes;
    //aretes correspondant à la triangulation
    Set<Arete> aretesFinales;

    public Triangulation(EnveloppeConvexe enveloppeConvexe) {
        points = enveloppeConvexe.points;
        pointsEnveloppe = enveloppeConvexe.enveloppeConvexe;
        pointsPolygone = pointsEnveloppe;
        pointsLibres = new ArrayList<>();
        aretes = new HashSet<>();
        aretesFinales = new HashSet<>();
        initPointsLibres();
        initAretes();
        int i = 0;
        while(i++ < 3 && trianguler());
    }

    /**
     * On soustrait points à pointsEnveloppe
     */
    public void initPointsLibres() {
        for (Point point : points) {
            if (!pointsEnveloppe.contains(point)) {
                pointsLibres.add(point);
            }
        }
    }

    /**
     * this.aretes = arêtes de l'enveloppe convexe
     */
    public void initAretes() {
        for (int i = 0 ; i < pointsEnveloppe.size() ; ++i) {
            aretes.add(new Arete(pointsEnveloppe.get(i), pointsEnveloppe.get((i+1) % pointsEnveloppe.size())));
        }
    }

    private boolean trianguler() {

        boolean hasChanged = false;
        for (Arete arete : aretes) {
            for (Point point : pointsLibres) {
                hasChanged = false;
                if ((point.x == arete.point1.x && point.y == arete.point1.y) || (point.x == arete.point2.x && point.y == arete.point2.y)) {
                    continue;
                }
                if (!pointDansTriangle(arete.point1, arete.point2, point)) {
                    System.out.println(pointsLibres.size());
                    Point pointASuppr = pointLibreASuppr();
                    if (pointASuppr != null)  {
                        pointsLibres.remove(point);
                    }
                    Arete nouvArete1 = new Arete(arete.point1, point);
                    Arete nouvArete2 = new Arete(arete.point2, point);
                    hasChanged = true;
                    aretes.remove(arete);
                    aretes.add(nouvArete1);
                    aretes.add(nouvArete2);
                    aretesFinales.add(nouvArete1);
                    aretesFinales.add(nouvArete2);
                    if (pointsPolygone.contains(point)) {
                        aretes.remove(findArete(arete, point));
                    }
                    break;
                }
            }
            if (hasChanged) {
                break;
            }
        }
        if (hasChanged) {
            //trianguler(aretes, points);
            return true;
        }
        return false;
    }

    public Point pointLibreASuppr() {
        for (Point point : pointsLibres) {
            for (Arete arete : aretesFinales) {
                if ((point.x == arete.point1.x && point.y == arete.point1.y) || (point.x == arete.point2.x && point.y == arete.point2.y)) {
                    return point;
                }
            }
        }
        return null;
    }

    private Arete findArete(Arete arete, Point point) {
        for (Arete areteToFind : aretes) {
            //si le premier point de l'arête en cours correspond au point en paramètre
            if (areteToFind.point1.x == point.x && areteToFind.point1.y == point.y) {
                //si l'un des 2 points de l'arête en paramètre vaut le 2e point de l'arête en cours
                if ((areteToFind.point2.x == arete.point1.x && areteToFind.point2.y == point.y) || (areteToFind.point2.x == arete.point2.x && areteToFind.point2.y == arete.point2.y)) {
                    pointsPolygone.remove(areteToFind.point2);
                    return areteToFind;
                }
            } else if (areteToFind.point2.x == point.x && areteToFind.point2.y == point.y) {
                if ((areteToFind.point1.x == arete.point1.x && areteToFind.point1.y == point.y) || (areteToFind.point1.x == arete.point2.x && areteToFind.point1.y == arete.point2.y)) {
                    pointsPolygone.remove(areteToFind.point1);
                    return areteToFind;
                }
            }
        }
        return null;
    }

    private boolean pointDansTriangle(Point point1, Point point2, Point point3) {
        double a1 = -(point2.y - point1.y);
        double b1 = point2.x - point1.x;
        double c1 = (-a1 * point1.x) - (b1 * point1.y);

        double a2 = -(point3.y - point2.y);
        double b2 = point3.x - point2.x;
        double c2 = (-a2 * point2.x) - (b2 * point2.y);

        double a3 = -(point1.y - point3.y);
        double b3 = point1.x - point3.x;
        double c3 = (-a3 * point3.x) - (b3 * point3.y);

        for (Point point : pointsLibres) {
            double x = point.x;
            double y = point.y;
            boolean verif1 = ((a1 * x + b1 * y + c1 < 0 && a1 * point3.x + b1 * point3.y + c1 < 0) || (a1 * x + b1 * y + c1 > 0 && a1 * point3.x + b1 * point3.y + c1 > 0));
            if (!verif1) continue;
            boolean verif2 = ((a2 * x + b2 * y + c2 < 0 && a2 * point1.x + b2 * point1.y + c2 < 0) || (a2 * x + b2 * y + c2 > 0 && a2 * point1.x + b2 * point1.y + c2 > 0));
            if (!verif2) continue;
            boolean verif3 = ((a3 * x + b3 * y + c3 < 0 && a3 * point2.x + b3 * point2.y + c3 < 0) || (a3 * x + b3 * y + c3 > 0 && a3 * point2.x + b3 * point2.y + c3 > 0));
            if (!verif3) continue;
            return true;
        }
        return false;
    }
}
