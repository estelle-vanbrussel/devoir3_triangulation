import java.util.*;

/**
 * Created by Vincent on 01/12/2018.
 */

//https://slideplayer.fr/slide/3701093/
//https://slideplayer.fr/slide/3701093/
//https://slideplayer.fr/slide/3701093/
//https://slideplayer.fr/slide/3701093/
//https://slideplayer.fr/slide/3701093/
public class TriangulationDelaunay {

    //Set<Arete> aretes = new HashSet<>();

    //tous les triangles actuels de la triangulation qui ne contiennent pas d'autres triangles
    Set<Triangle> triangles = new HashSet<>();
    //points non encore traités, càd reliés à aucun triangle
    ArrayDeque<Point> pointsRestants = new ArrayDeque<>();
    //tous les points actuels de la triangulation
    List<Point> pointsUtilises = new ArrayList<>();
    //rectangle englobant le nuage de points
    Rectangle rectangle;

    Point pointTriangleAdjacent;

    /**
     * Constructeur dans lequel on initialise les listes et le rectangle aux bonnes dimensions
     * @param enveloppeConvexe l'enveloppe convexe sur laquelle on se base
     */
    public TriangulationDelaunay(EnveloppeConvexe enveloppeConvexe) {
        //initAretes(enveloppeConvexe.enveloppeConvexe);
        for (Point point : enveloppeConvexe.points) {
            pointsRestants.add(point);
        }
        double minX = findMinX(enveloppeConvexe.enveloppeConvexe);
        double minY = findMinY(enveloppeConvexe.enveloppeConvexe);
        double maxX = findMaxX(enveloppeConvexe.enveloppeConvexe);
        double maxY = findMaxY(enveloppeConvexe.enveloppeConvexe);
        double widthRect = (maxX - minX) * 2;
        double heightRect = (maxY - minY) * 2;
        double xRect = minX - ((maxX - minX) / 2);
        double yRect = minY - ((maxY - minY) / 2);
        rectangle = new Rectangle(xRect, yRect, widthRect, heightRect);
        Point pointHautGauche = new Point(xRect, yRect);
        Point pointHautDroite = new Point(xRect + widthRect, yRect);
        Point pointBasGauche = new Point(xRect, yRect + heightRect);
        Point pointBasDroite = new Point(xRect + widthRect, yRect + heightRect);
        pointsUtilises.add(pointHautGauche);
        pointsUtilises.add(pointHautDroite);
        pointsUtilises.add(pointBasGauche);
        pointsUtilises.add(pointBasDroite);
        triangles.add(new Triangle(pointHautGauche, pointBasGauche, pointBasDroite));
        triangles.add(new Triangle(pointHautGauche, pointHautDroite, pointBasDroite));
        hermeline();
    }

    //algo pour trouver la triangulation de Delaunay
    public void hermeline() {
        int nbIterations = 0;
        while(!pointsRestants.isEmpty()) {
            System.out.println(nbIterations);
            Point point = pointsRestants.poll();
            Triangle triangle = findTriangleContainingPoint(point);
            //on ajoute les 3 nouveaux triangles et on supprime l'ancien
            triangles.add(new Triangle(triangle.p1, triangle.p2, point));
            triangles.add(new Triangle(triangle.p2, triangle.p3, point));
            triangles.add(new Triangle(triangle.p3, triangle.p1, point));
            triangles.remove(triangle);

            /*TODO(rapport) : On ajoute le point utilisé après le check car dans checkTrianglesValides, on regarde TOUS les triangles,
                et pas seulement les 3 derniers donc il y avait possibilité pour l'un des triangles qui ne fait pas partie des 3
                d'avoir le point qu'on vient de rajouter dans son cercle circonscrit et donc on allait basculer des arêtes avant d'avoir
                basculé les arêtes pour le nouveau point.
             */

            checkTrianglesValides();
            pointsUtilises.add(point);

            ++nbIterations;
        }
    }

    public void checkTrianglesValides() {
        //TODO(peut-être) : ne parcourir que les 3 derniers triangles ?
        for (Triangle triangle1 : triangles) {
            List<Point> pointsDansCercle = bouleVide(triangle1);
            if (pointsDansCercle.size() > 1) System.out.println("azer");
            //for (Point pointDansCercle : pointsDansCercle) {
                if (!pointsDansCercle.isEmpty()) {
                    basculerArete(triangle1, pointsDansCercle);
                    checkTrianglesValides();
                    return;
                }
            //}
        }
    }

    public Triangle findTriangleAdjacent(List<Point> points, Triangle triangleCourant) {

        for (Point point : points) {
            for (Triangle triangleCandidat : triangles) {
                if (triangleCandidat.contientPoint(point)) {
                    int cptPoints = 0;
                    if (triangleCandidat.contientPoint(triangleCourant.p1)) {
                        ++cptPoints;
                    }
                    if (triangleCandidat.contientPoint(triangleCourant.p2)) {
                        ++cptPoints;
                    }
                    if (triangleCandidat.contientPoint(triangleCourant.p3)) {
                        ++cptPoints;
                    }
                    if (cptPoints == 2) {
                        pointTriangleAdjacent = point;
                        return triangleCandidat;
                    }
                }
            }
        }
        return null;
    }

    public Point findPointDifferent(Triangle triangleAdj1, Triangle triangleAdj2) {
        if (!triangleAdj1.contientPoint(triangleAdj2.p1)) {
            return triangleAdj2.p1;
        }
        if (!triangleAdj1.contientPoint(triangleAdj2.p2)) {
            return triangleAdj2.p2;
        }
        if (!triangleAdj1.contientPoint(triangleAdj2.p3)) {
            return triangleAdj2.p3;
        }
        return null;
    }

    public void basculerArete(Triangle triangleCourant, List<Point> points) {
        //suppr les triangles qui contiennent l'arête à supprimer
        //créer les 2 nouveaux triangles composés de la nouvelle arête
        Triangle triangleAdjacent = findTriangleAdjacent(points, triangleCourant);
        if (triangleAdjacent == null) {
            System.out.println("Pas de triangle adjacent, pas normal");
        }
        Point pointDifferent = findPointDifferent(triangleAdjacent, triangleCourant);
        if (pointDifferent == null) {
            System.out.println("Pas de point différent, pas normal.");
        }
        if (pointDifferent == triangleCourant.p1) {
            Triangle newTriangle1 = new Triangle(pointTriangleAdjacent, pointDifferent, triangleCourant.p2);
            Triangle newTriangle2 = new Triangle(pointTriangleAdjacent, pointDifferent, triangleCourant.p3);
            triangles.add(newTriangle1);
            triangles.add(newTriangle2);
        } else if (pointDifferent == triangleCourant.p2) {
            Triangle newTriangle1 = new Triangle(pointTriangleAdjacent, pointDifferent, triangleCourant.p1);
            Triangle newTriangle2 = new Triangle(pointTriangleAdjacent, pointDifferent, triangleCourant.p3);
            triangles.add(newTriangle1);
            triangles.add(newTriangle2);
        } else if (pointDifferent == triangleCourant.p3) {
            Triangle newTriangle1 = new Triangle(pointTriangleAdjacent, pointDifferent, triangleCourant.p1);
            Triangle newTriangle2 = new Triangle(pointTriangleAdjacent, pointDifferent, triangleCourant.p2);
            triangles.add(newTriangle1);
            triangles.add(newTriangle2);
        } else {
            System.out.println("HELP");
        }
        triangles.remove(triangleAdjacent);
        triangles.remove(triangleCourant);
    }

    /**
     * Détermine si le cercle circonscrit d'un triangle contient un point utilisé
     * @param triangle le triangle pour le quel il faut chercher
     * @return le premier point trouvé à l'intérieur du cercle circonscrit du triangle
     */
    public List<Point> bouleVide(Triangle triangle) {
        List<Point> pointsDansCercle = new ArrayList<>();
        for (Point point : pointsUtilises) {
            if (triangle.contientPoint(point)) continue;
            double distPointCentreCercleCirconscrit = Math.sqrt(Math.pow(point.x - triangle.centre.x, 2) + Math.pow(point.y - triangle.centre.y, 2));
            if (distPointCentreCercleCirconscrit < triangle.rayonCercleCirconscrit)
                pointsDansCercle.add(point);
        }
        return pointsDansCercle;
    }

    /**
     * Renvoie le triangle dans lequel le point est contenu
     * @param point le point pour lequel il faut chercher
     * @return le Triangle contenant le point
     */
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

    /*public void initAretes(List<Point> pointsEnveloppe) {
        for (int i = 0 ; i < pointsEnveloppe.size() ; ++i) {
            aretes.add(new Arete(pointsEnveloppe.get(i), pointsEnveloppe.get((i+1) % pointsEnveloppe.size())));
        }
    }*/

    public double findMinX(List<Point> points) {
        Point pointMinX = points.get(0);
        for (Point point : points) {
            if (point.x < pointMinX.x) {
                pointMinX = point;
            }
        }
        return pointMinX.x;
    }

    public double findMinY(List<Point> points) {
        Point pointMinY = points.get(0);
        for (Point point : points) {
            if (point.y < pointMinY.y) {
                pointMinY = point;
            }
        }
        return pointMinY.y;
    }

    public double findMaxX(List<Point> points) {
        Point pointMaxX = points.get(0);
        for (Point point : points) {
            if (point.x > pointMaxX.x) {
                pointMaxX = point;
            }
        }
        return pointMaxX.x;
    }

    public double findMaxY(List<Point> points) {
        Point pointMaxY = points.get(0);
        for (Point point : points) {
            if (point.y > pointMaxY.y) {
                pointMaxY = point;
            }
        }
        return pointMaxY.y;
    }
}
