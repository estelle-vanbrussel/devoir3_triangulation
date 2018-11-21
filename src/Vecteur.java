public class Vecteur {
    Point point1,point2;
    int x,y;

    public Vecteur(Point point1, Point point2) {
        this.point1 = point1;
        this.point2 = point2;
        x=point2.x-point1.x;
        y=point2.y-point1.y;
    }

    public double calculCos(Vecteur v){
        double normev1 = Math.sqrt(Math.pow(this.x,2) + Math.pow(this.y,2));
        double normev2 = Math.sqrt(Math.pow(v.x,2) + Math.pow(v.y,2));
        double cos = ((this.x * v.x) + (this.y * v.y)) / (normev1 * normev2);
        return cos;
    }

    public double calculProduitVec(Vecteur v){
        return (this.x * v.y) - (this.y * v.x);
    }
}
