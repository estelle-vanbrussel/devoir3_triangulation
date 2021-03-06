import java.util.*;

/**
 * Created by Vincent on 01/12/2018.
 */

public class TriangulationDelaunay {

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
        if (enveloppeConvexe.points.size() == 3) {
            triangles.add(new Triangle(
                    new Point(enveloppeConvexe.points.get(0).x, enveloppeConvexe.points.get(0).y),
                    new Point(enveloppeConvexe.points.get(1).x, enveloppeConvexe.points.get(1).y),
                    new Point(enveloppeConvexe.points.get(2).x, enveloppeConvexe.points.get(2).y)));
            return;
        }

        for (Point point : enveloppeConvexe.points) {
            pointsRestants.add(point);
        }
        double minX = findMinX(enveloppeConvexe.enveloppeConvexe);
        double minY = findMinY(enveloppeConvexe.enveloppeConvexe);
        double maxX = findMaxX(enveloppeConvexe.enveloppeConvexe);
        double maxY = findMaxY(enveloppeConvexe.enveloppeConvexe);
        double widthRect = (maxX - minX) * 100;
        double heightRect = (maxY - minY) * 100;
        //double xRect = (minX - ((maxX - minX) / 2));
        //double yRect = (minY - ((maxY - minY) / 2));
        double xRect = minX - (widthRect / 2);
        double yRect = minY - (heightRect / 2);
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

        List<Point> pointsRect = new ArrayList<>();
        pointsRect.add(pointHautGauche);
        pointsRect.add(pointBasGauche);
        pointsRect.add(pointHautDroite);
        pointsRect.add(pointBasDroite);
        removeRectangle(pointsRect);

        List<Arete> aretes = chercherAretesContour();
        if (aretes.size() > 0) {
            corrigerEnveloppeConvexe(aretes);
        }
    }

    public void corrigerEnveloppeConvexe(List<Arete> aretes) {

        int cptAretes = 0;
        Arete vieilleArete = aretes.get(0);
        Arete nouvelleArete = null;
        Point pointCommun = vieilleArete.point2;
        boolean signe;
        boolean oldSigne = true;
        boolean pasconvexe = false;
        List<Point> pointsPasTriangules = new ArrayList<>();
        while(cptAretes <= aretes.size()) {
            for (Arete arete: aretes) {
                if(arete.contientPoint(pointCommun) && arete != vieilleArete){
                    nouvelleArete = arete;
                    break;
                }
            }
            Vecteur vecteur1 = new Vecteur(pointCommun, vieilleArete.autrePoint(pointCommun));
            Vecteur vecteur2 = new Vecteur(pointCommun, nouvelleArete.autrePoint(pointCommun));
            if(vecteur1.calculProduitVec(vecteur2) > 0) {
                signe = true;
            } else {
                signe = false;
            }

            if (cptAretes == 0)
                oldSigne = signe;

            if(oldSigne != signe){
                if(!pasconvexe) {
                    pasconvexe = true;
                    pointsPasTriangules.add(pointCommun);
                    pointsPasTriangules.add(vieilleArete.autrePoint(pointCommun));
                    pointsPasTriangules.add(nouvelleArete.autrePoint(pointCommun));
                }
                else{
                    EnveloppeConvexe enveloppeConvexe = new EnveloppeConvexe(pointsPasTriangules);
                    pointsPasTriangules.removeAll(pointsPasTriangules);
                    TriangulationDelaunay triangulationDelaunay = new TriangulationDelaunay(enveloppeConvexe);
                    for (Triangle triangle: triangulationDelaunay.triangles) {
                        triangles.add(triangle);
                    }
                    pasconvexe = false;
                }
            } else {

                if(pasconvexe){
                    pointsPasTriangules.add(nouvelleArete.autrePoint(pointCommun));
                }
            }
            oldSigne = signe;
            ++cptAretes;
            vieilleArete = nouvelleArete;
            pointCommun = vieilleArete.autrePoint(pointCommun);
        }
    }

    public void updateAreteContour(List<Arete> aretesContour, Point p1, Point p2){
        boolean remove = false;
        Arete areteToRemove = null;
        for (Arete arete: aretesContour) {
            if (arete.existeDeja(p1, p2)) {
                areteToRemove = arete;
                remove = true;
            }
        }
        if(remove && areteToRemove!=null) {
            aretesContour.remove(areteToRemove);
        }
        else {
            aretesContour.add(new Arete(p1,p2));
        }
    }

    public List<Arete> chercherAretesContour(){
        List<Arete> aretesContour = new ArrayList<>();
        for (Triangle triangle: triangles) {
            updateAreteContour(aretesContour,triangle.p1,triangle.p2);
            updateAreteContour(aretesContour,triangle.p2,triangle.p3);
            updateAreteContour(aretesContour,triangle.p3,triangle.p1);
        }
        return aretesContour;
    }

    //algo pour trouver la triangulation de Delaunay
    public void hermeline() {
        int nbIterations = 0;
        while(!pointsRestants.isEmpty()) {
            Point point = pointsRestants.poll();
            Triangle triangle = findTriangleContainingPoint(point);
            //on ajoute les 3 nouveaux triangles et on supprime l'ancien
            triangles.add(new Triangle(triangle.p1, triangle.p2, point));
            triangles.add(new Triangle(triangle.p2, triangle.p3, point));
            triangles.add(new Triangle(triangle.p3, triangle.p1, point));
            triangles.remove(triangle);

            checkTrianglesValides();
            pointsUtilises.add(point);

            ++nbIterations;
        }
    }

    public void checkTrianglesValides() {
        for (Triangle triangle1 : triangles) {
            List<Point> pointsDansCercle = bouleVide(triangle1);
            if (!pointsDansCercle.isEmpty()) {
                basculerArete(triangle1, pointsDansCercle);
                checkTrianglesValides();
                return;
            }
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

    private void removeRectangle(List<Point> pointsRectangle){
        List<Triangle> trianglesASuppr = new ArrayList<>();
        for (Triangle triangle: triangles) {
            for (Point pointRectangle: pointsRectangle) {
                if(triangle.contientPoint(pointRectangle)) {
                    trianglesASuppr.add(triangle);
                    break;
                }
            }
        }
        triangles.removeAll(trianglesASuppr);
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
