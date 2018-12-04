/**
 * Created by Vincent on 01/12/2018.
 */
public class Triangle {

    public Point p1;
    public Point p2;
    public Point p3;
    public Point centre;
    public double rayonCercleCirconscrit;

    public Triangle(Point p1, Point p2, Point point3) {
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = point3;
        this.centre = calculerCentre();
        rayonCercleCirconscrit = Math.sqrt(Math.pow(this.p1.x - this.centre.x, 2) + Math.pow(this.p1.y - this.centre.y, 2));
    }

    public boolean contientPoint(Point point) {
        return (point == p1 || point == p2 || point == p3);
    }

    public Point calculerCentre() {
        //milieu d'un premier côté du triangle
        double xMilieu1_2 = (p1.x + p2.x) / 2;
        double yMilieu1_2 = (p1.y + p2.y) / 2;

        //vecteur directeur de ce côté
        Vecteur vecteur1_2 = new Vecteur(p1, p2);

        //vecteur normal de l'autre vecteur (= médiatrice)
        Vecteur vecteurTemp = new Vecteur(new Point(xMilieu1_2, yMilieu1_2), new Point(-vecteur1_2.y + xMilieu1_2, vecteur1_2.x + yMilieu1_2));

        //equation cartesienne du vecteur normal
        double a1 = -(vecteurTemp.point2.y - vecteurTemp.point1.y);
        double b1 = vecteurTemp.point2.x - vecteurTemp.point1.x;
        double c1 = ((-a1 * vecteurTemp.point1.x) - (b1 * vecteurTemp.point1.y));
        //on fait pareil pour un autre côté
        double xMilieu2_3 = (p2.x + p3.x) / 2;
        double yMilieu2_3 = (p2.y + p3.y) / 2;

        Vecteur vecteur2_3 = new Vecteur(p2, p3);

        Vecteur vecteurTemp2 = new Vecteur(new Point(xMilieu2_3, yMilieu2_3), new Point(-vecteur2_3.y + xMilieu2_3, vecteur2_3.x + yMilieu2_3));

        double a2 = -(vecteurTemp2.point2.y - vecteurTemp2.point1.y);
        double b2 = vecteurTemp2.point2.x - vecteurTemp2.point1.x;
        double c2 = (-a2 * vecteurTemp2.point1.x) - (b2 * vecteurTemp2.point1.y);

        //resolution du système d'équations pour trouver le point d'intersection des médiatrices
        double det = a1*b2-a2*b1;
        double x = (b1*c2-b2*c1)/det;
        double y = (c1*a2-c2*a1)/det;

        return new Point(x,y);
    }

}
