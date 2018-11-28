import java.util.*;

public class EnveloppeConvexe {

    List<Point> points ;
    List<Point> enveloppeConvexe;
    private int fail =0;
    Point pointHautGauche;
    Point pointHautDroit;
    Point pointBasGauche;
    Point pointBasDroit;

    public EnveloppeConvexe(List<Point> points) {
        this.points = new ArrayList<>(points);
        this.points.sort(Point::compareTo);
        enveloppeConvexe = buildEnveloppeConvexe(0,points.size()-1);
    }

    public List<Point> buildEnveloppeConvexe(int debut, int fin) {
        int tailleEnsemble = fin - debut + 1;
        List<Point> enveloppe = new ArrayList<>();
        if (tailleEnsemble < 4) {
            //gérer les cas particuliers
            for (int i=debut; i< fin +1 ; i++) {
                enveloppe.add(points.get(i));
            }
        } else {

            //Les deux enveloppe à fusionner
            List<Point> enveloppe1 = buildEnveloppeConvexe(debut, tailleEnsemble / 2);
            List<Point> enveloppe2 = buildEnveloppeConvexe((tailleEnsemble / 2) + 1, fin);
            //Les deux points du vecteur initial
            pointHautGauche = points.get((tailleEnsemble / 2));
            pointHautDroit = points.get((tailleEnsemble / 2) + 1);
            pointBasGauche = points.get(tailleEnsemble / 2);
            pointBasDroit = points.get((tailleEnsemble / 2) + 1);
            //on arrete de monter quand il y a deux fail
            while (fail < 2) {
                //monter à droite jusqu'à fail
                monterDroit(enveloppe1, enveloppe2);
                if(fail==2) break;
                //monter à gauche jusqu'à fail
                monterGauche(enveloppe1, enveloppe2);
            }
            fail=0;
            while (fail < 2) {
                //monter à droite jusqu'à fail
                descendreDroit(enveloppe1, enveloppe2);
                if(fail==2) break;
                //monter à gauche jusqu'à fail
                descendreGauche(enveloppe1, enveloppe2);
            }
            fail=0;
            //rassembler les enveloppes
            int i = findPointInEnveloppe(enveloppe1, pointBasGauche);
            while(!(enveloppe1.get(i).equals(pointHautGauche))){
                enveloppe.add(enveloppe1.get(i));
                if(i == enveloppe1.size()-1) i = 0;
                else ++i;
            }
            enveloppe.add(pointHautGauche);
            i = findPointInEnveloppe(enveloppe2, pointHautDroit);
            while(!(enveloppe2.get(i).equals(pointBasDroit))){
                enveloppe.add(enveloppe2.get(i));
                if(i == enveloppe2.size()-1) i = 0;
                else ++i;
            }
            enveloppe.add(pointBasDroit);
        }
        return enveloppe;
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
            pointHautDroit = vecteurTest.point1;
            monterDroit(enveloppe1,enveloppe2);
        }
        else{
            int indexGauche = findPointInEnveloppe(enveloppe1, pointHautGauche);
            int indexpred;
            if (indexGauche == 0) indexpred = enveloppe1.size() -1;
            else indexpred = indexGauche - 1;
            pointHautGauche = enveloppe1.get(indexpred);
            ++fail;
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
            monterGauche(enveloppe1,enveloppe2);
        }
        else{
            int indexDroit = findPointInEnveloppe(enveloppe2, pointHautDroit);
            int indexsucc = 0;
            if (indexDroit == enveloppe2.size() -1) indexsucc = 0;
            else indexsucc = indexDroit + 1;
            pointHautDroit = enveloppe2.get(indexsucc);
            ++fail;
        }
    }

    private void descendreDroit(List<Point> enveloppe1, List<Point> enveloppe2) {
        Vecteur vecteurInitial = new Vecteur(pointBasGauche, pointBasDroit);
        int indexDroit = findPointInEnveloppe(enveloppe2, pointBasDroit);
        int indexpred;
        if(indexDroit == 0) indexpred = enveloppe2.size()-1;
        else indexpred=indexDroit-1;
        Vecteur vecteurTest = new Vecteur(pointBasGauche,enveloppe2.get(indexpred));
        double produitVec = vecteurInitial.calculProduitVec(vecteurTest);
        if(produitVec<0) {
            fail = 0;
            pointBasDroit = vecteurTest.point2;
            descendreDroit(enveloppe1,enveloppe2);
        }
        else{
            int indexGauche = findPointInEnveloppe(enveloppe1, pointBasGauche);
            int indexsucc;
            if (indexGauche == enveloppe1.size() -1) indexsucc = 0;
            else indexsucc = indexGauche + 1;
            pointBasGauche = enveloppe1.get(indexsucc);
            ++fail;
        }
    }

    private void descendreGauche(List<Point> enveloppe1, List<Point> enveloppe2) {
        Vecteur vecteurInitial = new Vecteur(pointBasDroit, pointBasGauche);
        int indexGauche = findPointInEnveloppe(enveloppe1, pointBasGauche);
        int indexsucc;
        if(indexGauche == enveloppe2.size()-1) indexsucc = 0;
        else indexsucc=indexGauche+1;
        Vecteur vecteurTest = new Vecteur(pointBasDroit,enveloppe1.get(indexsucc));
        double produitVec = vecteurInitial.calculProduitVec(vecteurTest);
        if(produitVec>0) {
            fail = 0;
            pointBasGauche = vecteurTest.point2;
            descendreGauche(enveloppe1,enveloppe2);
        }
        else{
            int indexDroit = findPointInEnveloppe(enveloppe2, pointHautDroit);
            int indexpred = 0;
            if (indexDroit == 0) indexpred = enveloppe2.size() -1;
            else indexpred = indexDroit - 1;
            pointBasDroit = enveloppe2.get(indexpred);
            ++fail;
        }
    }


    public int findPointInEnveloppe(List<Point> enveloppe, Point pointToFind) {
        for (int i = 0; i < enveloppe.size() ; ++i) {
            if (enveloppe.get(i).equals(pointToFind)) {
                return i;
            }
        }
        return -1;
    }
}
