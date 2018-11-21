import java.util.*;

public class EnveloppeConvexe {

    List<Point> points ;
    List<Point> enveloppeConvexe;

    public EnveloppeConvexe(List<Point> points) {
        this.points = new ArrayList<>(points);
        this.points.sort(Point::compareTo);
    }

    public List<Point> buildEnveloppeConvexe(int debut, int fin) {
        int tailleEnsemble = fin - debut + 1;

        if (tailleEnsemble < 3) {
            //gérer les cas particuliers
        }
        else {

            // vers le haut 1 fois
            List<Point> enveloppe1 = buildEnveloppeConvexe(debut, tailleEnsemble / 2);
            List<Point> enveloppe2 = buildEnveloppeConvexe((tailleEnsemble / 2) + 1, fin);
            Point pointGauche = points.get(tailleEnsemble/2);
            Point pointDroit = points.get((tailleEnsemble/2) +1);
            Vecteur vecteurInitial = new Vecteur(pointDroit,pointGauche);
            int indexGauche = findPointInEnveloppe(enveloppe1, pointGauche);
            int indexpred = 0;
            if(indexGauche ==0) indexpred = enveloppe1.size()-1;
            else indexpred=indexGauche-1;
            Vecteur vecteurTest = new Vecteur(pointDroit,enveloppe1.get(indexpred));
            double produitVec = vecteurInitial.calculProduitVec(vecteurTest);
            if(produitVec<0) pointGauche = vecteurTest.point2;
            else {
                int indexDroit = findPointInEnveloppe(enveloppe2, pointDroit);
                int indexsucc = 0;
                if(indexDroit == enveloppe1.size()) indexsucc = enveloppe1.size()+1;
                else indexsucc=indexGauche+1;
                pointGauche = enveloppe2.get(indexsucc);
            }

            //To-do : boucle jusqu'à fail deux fois, la même chose pour l'arête du bas, la fusion des enveloppes
        }
        return points;
    }

    public int findPointInEnveloppe(List<Point> enveloppe, Point pointToFind) {
        for (int i = 0; i < enveloppe.size() ; ++i) {
            if (enveloppe.get(i) == pointToFind) {
                return i;
            }
        }
        return -1;
    }
}
