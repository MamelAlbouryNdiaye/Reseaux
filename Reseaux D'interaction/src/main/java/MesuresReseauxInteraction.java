
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import org.graphstream.algorithm.Toolkit;
import org.graphstream.algorithm.generator.BarabasiAlbertGenerator;
import org.graphstream.algorithm.generator.Generator;
import org.graphstream.algorithm.generator.RandomGenerator;
import org.graphstream.graph.BreadthFirstIterator;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;


public class MesuresReseauxInteraction {
Graph graph;

    /**
     * Constructeur de classe
     * @param graph
     */
    public MesuresReseauxInteraction(Graph graph) {
      this.graph=graph;
    }

    /**
     * cette méthode calcul la distribution de degré
     */
    public void distDeg(){
        //nombre de noeud du graphe
        int nbNoeud = graph.getNodeCount();
        // stock dans un tableau d'entiers : où chaque indice de cellule représente le degré d'un noeud,
        // et la valeur de la cellule le nombre de noeuds ayant ce degré
        int [] degreProba = Toolkit.degreeDistribution(graph);
        // création d'un fichier pour le stock des résultats de la distribution des degrés
        try {
            PrintWriter fichier = new PrintWriter(new FileWriter("distribution/distributionDeg.txt"));
            for (int i = 0; i < degreProba.length; i++) {
                // degreProba[i]/nbNoeud  : represente la probabilité  qu’un noeud choisi au hasard ait degré i
                fichier.write(i + "   " + (double)degreProba[i]/nbNoeud);
                fichier.println();
            }
            fichier.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * cette méthode  renvoit la somme des distances d'un noeud s vers les autres noeuds.
     * @return double
     */
    public double sumDist(){
        double dist=0;
        //liste pour stocker les noeuds choisi avec random dans le but d'éviter les répétitions
        ArrayList list = new ArrayList();
        for (int i = 0; i < 1000; i++) {
            Node node;
            do {
                //récupérer un noeud quelconque du graphe
                node = Toolkit.randomNode(graph);
            }while (list.contains(node));
               list.add(node);
            //parcours du graphe
            BreadthFirstIterator bf = new BreadthFirstIterator(node);
            while (bf.hasNext()) {
                //somme de ses distances vers les autres noeuds
                dist += (bf.getDepthOf(bf.next()));
            }
        }
        return dist;
    }

    /**
     * cette méthode permet de calculer la distance moyenne
     * @return double
     */
    public double distanceMoyenne() {
       //échantillonnage de 1000 sommets choisis au hasard
        int sample = 1000;
        double distance = sumDist();
        // car on parcours le graphe 1000 fois
        int max = graph.getNodeCount() * sample;

        return distance/max;
    }


    /**
     * distribution des distances
     */
    public void distributionDistances() {
        double distProba[] = new double[1000];
        int nbNoeud = graph.getNodeCount() * 1000;
        for (int i = 0; i < 1000; i++) {
            //recuperer en aleatoire des noeud du graphe
            Node noeud = Toolkit.randomNode(graph);
            //parcour en largeur à partir du noeud tirer au hasard
            BreadthFirstIterator bf = new BreadthFirstIterator(noeud);
            while (bf.hasNext()) {
                distProba[bf.getDepthOf(bf.next())]+= distProba[bf.getDepthOf(bf.next())]+1;
            }
        }

        try {
            PrintWriter fichier = new PrintWriter(new FileWriter("distribution/distributionDist.txt"));
            int j = 0;
            while (distProba[j] != 0) {
                // distProba[j]/nbNoeud  : represente la probabilité  qu’un noeud choisi au hasard ait  la distance j
                fichier.write(j + " " + distProba[j]/nbNoeud);
                fichier.println();
                j++;
            }
            fichier.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * crée un graphique aléatoire de taille passer en parametre
     * @param nbNoeuds
     * @param degreeMoyen
     * @return
     */
    public Graph generateurRandom(int nbNoeuds, int degreeMoyen){
        //configurer l'apparence du graphe, j'ai choisis le rendu swing (faut la définir avant d'appeler graphe.display())
        System.setProperty("org.graphstream.ui", "swing");
        // instancie un graphe g en faisant l'implémentation de graphe SingleGraph
        Graph g = new SingleGraph("RandomGraph");
        // instancie un générateur gen qui va nous permettre de  crée des graphiques aléatoires de toute taille
        Generator gen = new RandomGenerator(degreeMoyen, false, false);
        gen.addSink(g);
        //ajoute un noeud unique dans graphe
        gen.begin();
        //ajoute un nouveau noeud
        for (int i = 0; i < nbNoeuds; i++)
            gen.nextEvents();
        gen.end();

        System.out.println("le nombre de noeud de ce graphe générer aléatoirement est :" +g.getNodeCount() );//affichage du nombre de noeud que contient le graphe
        System.out.println("le nombre de liens de ce graphe générer aléatoirement  est :" + g.getEdgeCount());//affichage du nomre d'aretes que contient le graphe
        System.out.println("le degré moyen de ce graphe générer aléatoirement  est  :" + Toolkit.averageDegree(g));// affichage du degré moyen de ce graphe
        System.out.println("le clustering coeficient de ce graphe générer aléatoirement est  :" + Toolkit.averageClusteringCoefficient(g));//calcule du coefficient de clustering

       //dessiner le graphe
       // g.display();
        return g;
    }

    /**
     * crée un graphique(en utilisant la règle d'attachement préférentielle) de taille passer en parametre
     * @return
     */
    public Graph generateurBarabasi(int nbNoeuds, int degreeMoyen) {
        //configurer l'apparence du graphe, j'ai choisis le rendu swing (faut la définir avant d'appeler graphe.display())
        System.setProperty("org.graphstream.ui", "swing");
        // instancie un graphe g en faisant l'implémentation de graphe SingleGraph
        SingleGraph g = new SingleGraph("BarabasiGen");
        // instancie un générateur gen qui va nous permettre de  crée des graphiques en utilisant
        // la règle d'attachement préférentielle de toute taille définie dans le modèle Barabási-Albert
        Generator gen = new BarabasiAlbertGenerator(degreeMoyen);
        gen.addSink(g);
        //ajoute un noeud unique dans graphe
        gen.begin();
       //Génére et ajoute un nouveau noeud
        for (int i = 0; i < nbNoeuds; i++)
            gen.nextEvents();
        gen.end();

        System.out.println("le nombre de noeud de ce graphe generer avec le generateur barbasi est :" +g.getNodeCount() );//affichage du nombre de noeud que contient le graphe
        System.out.println("le nombre de liens de ce graphe avec le generateur barbasi est :" + g.getEdgeCount());//affichage du nomre d'aretes que contient le graphe
        System.out.println("le degré moyen de ce graphe avec le generateur barbasi est  :" + Toolkit.averageDegree(g));// affichage du degré moyen de ce graphe
        System.out.println("le clustering coeficient avec le generateur barbasi est  :" + Toolkit.averageClusteringCoefficient(g));//calcule du coefficient de clustering


        //g.display();

        return g;
    }






}
