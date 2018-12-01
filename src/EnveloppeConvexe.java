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
        //pour tailleEnsemble = 3 : les 3 points ne doivent pas être ajoutés dans n'importe quel ordre :
        //ils doivent suivre le sens des aiguilles d'une montre.
        if (tailleEnsemble < 3) {
            //gérer les cas particuliers
            for (int i=debut; i< fin +1 ; i++) {
                enveloppe.add(points.get(i));
            }

        } else {

            //Les deux enveloppe à fusionner
            List<Point> enveloppe1 = buildEnveloppeConvexe(debut, debut + (tailleEnsemble / 2));
            List<Point> enveloppe2 = buildEnveloppeConvexe(debut+((tailleEnsemble / 2) +1), fin);
            pointHautDroit = enveloppe2.get(0);
            pointHautGauche = enveloppe1.get(0);
            for (Point point: enveloppe1) {
                if(point.x>pointHautGauche.x){
                    pointHautGauche = point;
                }
            }
            for (Point point: enveloppe2) {
                if(point.x<pointHautDroit.x){
                    pointHautDroit = point;
                }
            }
            pointBasGauche=pointHautGauche;
            pointBasDroit=pointHautDroit;
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
        if(produitVec > 0) {
            fail = 0;
            pointHautDroit = enveloppe2.get(indexsucc);
            monterDroit(enveloppe1,enveloppe2);
        }
        else{
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
        if(produitVec < 0) {
            fail = 0;
            pointHautGauche = enveloppe1.get(indexpred);
            monterGauche(enveloppe1,enveloppe2);
        }
        else{
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
        if(produitVec < 0) {
            fail = 0;
            pointBasDroit = enveloppe2.get(indexpred);
            descendreDroit(enveloppe1,enveloppe2);
        }
        else{
            ++fail;
        }
    }

    private void descendreGauche(List<Point> enveloppe1, List<Point> enveloppe2) {
        Vecteur vecteurInitial = new Vecteur(pointBasDroit, pointBasGauche);
        int indexGauche = findPointInEnveloppe(enveloppe1, pointBasGauche);
        int indexsucc;
        if(indexGauche == enveloppe1.size()-1) indexsucc = 0;
        else indexsucc=indexGauche+1;
        Vecteur vecteurTest = new Vecteur(pointBasDroit,enveloppe1.get(indexsucc));
        double produitVec = vecteurInitial.calculProduitVec(vecteurTest);
        if(produitVec > 0) {
            fail = 0;
            pointBasGauche = enveloppe1.get(indexsucc);
            descendreGauche(enveloppe1,enveloppe2);
        }
        else{
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
