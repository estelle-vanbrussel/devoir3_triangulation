import java.util.*;

public class EnveloppeConvexe {

    List<Point> points ;
    List<Point> enveloppeConvexe;
    private int fail =0;
    Point pointHautGauche;
    Point pointHautDroit;

    public EnveloppeConvexe(List<Point> points) {
        this.points = new ArrayList<>(points);
        this.points.sort(Point::compareTo);
    }

    public List<Point> buildEnveloppeConvexe(int debut, int fin) {
        int tailleEnsemble = fin - debut + 1;

        if (tailleEnsemble < 3) {
            //gérer les cas particuliers
            for (Point point : points) {
                enveloppeConvexe.add(point);
            }
        } else {

            //Les deux enveloppe à fusionner
            List<Point> enveloppe1 = buildEnveloppeConvexe(debut, tailleEnsemble / 2);
            List<Point> enveloppe2 = buildEnveloppeConvexe((tailleEnsemble / 2) + 1, fin);
            //Les deux points du vecteur initial
            pointHautGauche = points.get(tailleEnsemble / 2);
            pointHautDroit = points.get((tailleEnsemble / 2) + 1);
            //on arrete de monter quand il y a deux fail
            while (fail < 2) {
                //monter à droite jusqu'à fail
                monterDroit(enveloppe1, enveloppe2);
                //monter à gauche jusqu'à fail
                monterGauche(enveloppe1, enveloppe2);
            }
            //TODO : boucle jusqu'à fail deux fois, la même chose pour l'arête du bas, la fusion des enveloppes
        }
        return null;
    }

    private void monterDroit(List<Point> enveloppe1, List<Point> enveloppe2) {
        Vecteur vecteurInitial = new Vecteur(pointHautGauche, pointHautDroit);
        int indexDroit = findPointInEnveloppe(enveloppe2, pointHautDroit);
        int indexsucc;
        if(indexDroit == enveloppe2.size()-1) indexsucc = 0;
        else indexsucc=indexDroit+1;
        Vecteur vecteurTest = new Vecteur(pointHautGauche,enveloppe2.get(indexsucc));
        double produitVec = vecteurInitial.calculProduitVec(vecteurTest);
        if(produitVec>0) {
            fail = 0;
            pointHautDroit = vecteurTest.point2;
            monterDroit(enveloppe1,enveloppe2);
        }
        else{
            int indexGauche = findPointInEnveloppe(enveloppe1, pointHautGauche);
            int indexpred = 0;
            if (indexGauche == 0) indexpred = enveloppe2.size() -1;
            else indexpred = indexGauche - 1;
            pointHautGauche = enveloppe2.get(indexpred);
            fail = 1;
            return;
        }
    }

    private void monterGauche(List<Point> enveloppe1, List<Point> enveloppe2) {
        Vecteur vecteurInitial = new Vecteur(pointHautDroit, pointHautGauche);
        int indexGauche = findPointInEnveloppe(enveloppe1, pointHautGauche);
        int indexpred = 0;
        if(indexGauche ==0) indexpred = enveloppe1.size()-1;
        else indexpred=indexGauche-1;
        Vecteur vecteurTest = new Vecteur(pointHautDroit,enveloppe1.get(indexpred));
        double produitVec = vecteurInitial.calculProduitVec(vecteurTest);
        if(produitVec<0) {
            fail = 0;
            pointHautGauche = vecteurTest.point2;
            monterDroit(enveloppe1,enveloppe2);
        }
        else{
            int indexDroit = findPointInEnveloppe(enveloppe2, pointHautDroit);
            int indexsucc = 0;
            if (indexDroit == enveloppe1.size()) indexsucc = 0;
            else indexsucc = indexGauche + 1;
            pointHautGauche = enveloppe2.get(indexsucc);
            fail = 1;
            return;
        }
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
