import org.graphstream.algorithm.Toolkit;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.DefaultGraph;
import org.graphstream.stream.file.FileSourceEdge;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        String filePath ="dblp/com-dblp.ungraph.txt";
        Graph graph = new DefaultGraph("graph");
        MesuresReseauxInteraction mesures=  new MesuresReseauxInteraction(graph);
        FileSourceEdge fs = new FileSourceEdge();
        fs.addSink(graph);

        try {
            fs.readAll(filePath);

        } catch (IOException ioException) {
            ioException.printStackTrace();
        } finally {
            fs.removeSink(graph);
        }


        System.out.println("le nombre de noeud de ce graphe est :" +graph.getNodeCount() );//affichage du nombre de noeud que contient le graphe
        System.out.println("le nombre de liens de ce graphe est :" + graph.getEdgeCount());//affichage du nomre d'aretes que contient le graphe
        System.out.println("le degré moyen de ce graphe est  :" + Toolkit.averageDegree(graph));// affichage du degré moyen de ce graphe
        System.out.println("le clustering coeficient est  :" + Toolkit.averageClusteringCoefficient(graph));//calcule du coefficient de clustering


        double nb=0;
        double[] t = Toolkit.clusteringCoefficients(graph);
        for (int i=0; i<t.length;i++){
            nb += t[i];
        }

        System.out.println("vérification du coefficient de clustreing" + nb/graph.getNodeCount());
        System.out.println("Est il connexe ?? " +Toolkit.isConnected(graph));



         mesures.distDeg();

        System.out.println("la distance  : " + mesures.distanceMoyenne());
        mesures.distanceMoyenne();
         mesures.distributionDistances();

        mesures.generateurRandom(317080,7);
        mesures.generateurBarabasi(317080,7);

/*
        System.out.println("-----------------------------------Propagation----------------------------------------------");


        PropagationReseaux pr = new PropagationReseaux(graph);
        double k = Toolkit.averageDegree(graph);
        double seuilDBLP = k / pr.dispersionDegre();
        double seuilReseauRandom = 1 / (k+1);
        System.out.println("Le seuil épidémique du réseau (DBLP) λc = " + k +" / "+ pr.dispersionDegre() + " = " +  seuilDBLP);
        System.out.println("Le seuil épidémique du réseau Aléatoire λc = " + 1 +" / "+  (k+1) + " = " +  seuilReseauRandom);

        //pr.propagationSc1();
       //pr.propagationSc2();
      // pr.propagationSc3();

      System.out.println( "---------------------Simulation avec un Réseau aléatoire--------------------");

       Graph f =  mesures.generateurRandom(317080,(int)k);

        PropagationReseaux prAleatoire = new PropagationReseaux(f);
        double kAleatoire = Toolkit.averageDegree(graph);
        double seuilAleatoire = kAleatoire / prAleatoire.dispersionDegre();
        double seuilReseauAleatoire = 1 / (kAleatoire+1);
        System.out.println("Le seuil épidémique du réseau Aléatoire λc = " + kAleatoire +" / "+ prAleatoire.dispersionDegre() + " = " +  seuilAleatoire);
        System.out.println("Le seuil épidémique du réseau Aléatoire λc = " + 1 +" / "+  (kAleatoire+1) + " = " +  seuilReseauAleatoire);

        //prAleatoire.propagationSc1();
        // prAleatoire.propagationSc2();
      // prAleatoire.propagationSc3();





        System.out.println( "---------------------Simulation avec un Réseau avec la méthode d'attachement préférentiel--------------------");


        Graph barb =  mesures.generateurBarabasi(317080,(int)k);

        PropagationReseaux prBarbasi = new PropagationReseaux(barb);
        double kBarbasi = Toolkit.averageDegree(graph);
        double seuilBarbasi = kBarbasi / prBarbasi.dispersionDegre();
        double seuilReseauBarbasi = 1 / (kBarbasi+1);
        System.out.println("Le seuil épidémique du réseau Barbasi λc = " + kBarbasi +" / "+ prBarbasi.dispersionDegre() + " = " +  seuilBarbasi);
        System.out.println("Le seuil épidémique du réseau Aléatoire  avec la méthode d'attachement préférentiel λc = " + 1 +" / "+  (kBarbasi+1) + " = " +  seuilReseauBarbasi);

        prBarbasi.propagationSc1();
        //prBarbasi.propagationSc2();
        //prBarbasi.propagationSc3();
 */


    }

}
