public class Vecteur {

    Point point1,point2;
    double x,y, norme;

    public Vecteur(Point point1, Point point2) {
        this.point1 = point1;
        this.point2 = point2;
        x=point2.x-point1.x;
        y=point2.y-point1.y;
        norme = Math.sqrt(Math.pow(this.x,2) + Math.pow(this.y,2));
    }

    public double calculerCos(Vecteur v){
        return ((this.x * v.x) + (this.y * v.y)) / (this.norme * v.norme);
    }

    public double calculProduitVec(Vecteur v){
        return (this.x * v.y) - (this.y * v.x);
    }

    public double produitScalaire(Vecteur v) {
        return this.norme * v.norme * this.calculerCos(v);
    }
}
