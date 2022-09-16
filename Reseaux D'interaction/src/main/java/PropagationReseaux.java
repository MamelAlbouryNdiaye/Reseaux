import org.graphstream.algorithm.Toolkit;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;


public class PropagationReseaux {


    Graph graph;

    /**
     * Constructeur de classe
     * @param graph
     */
    public PropagationReseaux(Graph graph) {
        this.graph=graph;
    }

    /**
     * calcul la dispersion des degrés <k²>
     * @return double
     */
    public  double  dispersionDegre(){
        //valeur de la dispersion des degrés
        double k2 = 0.0;
        // stock dans un tableau d'entiers : où chaque indice de cellule représente le degré d'un noeud,
        // et la valeur de la cellule le nombre de noeuds ayant ce degré
        int[] distribution = Toolkit.degreeDistribution(graph);
        //fait le calcul de <k²> ( multiplie le carré de chaque degré des noeuds fois le nombre de noeuds ayant ce degré sur le nombre de noeuds du graphe)
        for (int i = 0; i < distribution.length; i++) {
            if (distribution[i] > 0) {
                k2 += Math.pow(i, 2) * ((double) distribution[i] / graph.getNodeCount());
            }
        }
        return k2;
    }



        public void propagationSc1() {
        Random random = new Random();
        int jours = 90;
        long[] infections = new long[jours+1];
        ArrayList<Node> malades = new ArrayList<Node>();
        ArrayList<Node> copieMalades = new ArrayList<Node>();


        // patient0 est le premier patient infecté (malade)
        Node  patient0 = Toolkit.randomNode(graph);
        patient0.setAttribute("health", "infected");
        malades.add(patient0);

        //calcule le nombre de malades pendant 90 jours (une sorte de simulation)
        for (int jour = 1; jour <= jours; jour++) {
            for (Node n : malades) {
                //comme un individu envoi 1 mail par semaine a ses collaborateur alors la probabilité
                // est de 1/7 de les infecté (voisins de cet individu(noeud))
                if (random.nextInt(7) + 1 == 1) {
                    n.neighborNodes().forEach(v->{if (!copieMalades.contains(v)) {
                        //le voisin devient infecté
                        v.setAttribute("health", "infected");
                        // on ajoute ce voisin à la liste
                        copieMalades.add(v);
                    }});
                }
                //On ajoute le noeud infecté n à une liste  s'il n'y est pas déjà
                if (!copieMalades.contains(n))
                    copieMalades.add(n);
            }
            //On vide la liste des infectés
            malades.clear();
            //Chaque nœud infecté(stocké dans la liste temporaire) peut soit guérir (avec une probabilité de 1/14),
            //soit rester infecté (on l'ajoute à la liste des infectés)
            for (Node n : copieMalades) {
                if (random.nextInt(14) + 1 == 1) {
                    n.setAttribute("health", "healthy");
                } else {
                    malades.add(n);
                }
            }
            //On stock le nombre d'individus infectés ce jour là commme valeur de la case "jour" du tableau infections
            for (Node n : graph) {
                if (n.getAttribute("health") == "infected") {
                    infections[jour] = infections[jour]+1;
                }
            }
            copieMalades.clear();
        }


        try {
            System.out.println("..");
            PrintWriter fichier = new PrintWriter(new FileWriter("scenarios/sc1Barbasi.txt"));
            for (int i = 0; i < jours; i++) {
                // infections[i]  : represente le nombre de malades en fonction du jour i
                fichier.write(i + "   " + infections[i]++);
                fichier.println();
            }
            fichier.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



        /**
         * Simulation du deuxième scénario : On réussit à convaincre 50 % des individus de mettre à jour en permanence leur anti-virus (immunisation aléatoire)
         */
        public void propagationSc2(){
            int jours = 90;
            long[] infections = new long[jours+1];
            Random random = new Random();
            ArrayList<Node> malades = new ArrayList<Node>();
            ArrayList<Node> copieMalades = new ArrayList<Node>();
            Node patient0;

            double degMoyGrp0=0.0;
            double degMoyGrp1=0.0;
            int nb=0;
            int nbreMoitierGraphe = (int) graph.getNodeCount()/2;
            Graph scenario02 = graph;

            //Récupérer une moitié des noeuds du graphe aléatoirement
            List <Node> immunisation = Toolkit.randomNodeSet(graph, graph.getNodeCount()/2);

            //Immunisation des 50% d'individus pris aléatoirement
            for(Node noeud : immunisation) {
                degMoyGrp0+= noeud.getDegree();
                noeud.setAttribute("health", "immunise");
                    scenario02.removeNode(noeud);
            }
            degMoyGrp0 = degMoyGrp0 / nbreMoitierGraphe;
            System.out.println("le seuil épidémique du réseau modifié pour la stratégies d'immunisation du scenario 02 : "
                    +Toolkit.averageDegree(scenario02)/dispersionDegre());


            //L'épidémie commence avec un individu infecté (patient zéro non immunisé)
            do{
                System.out.println("ih yedad ");
                patient0 = Toolkit.randomNode(graph);
            }while((patient0.getAttribute("health") == "immunise"));
            System.out.println("adagh ukessar ");

            patient0.setAttribute("health", "infected");
            malades.add(patient0);
            //calcule le nombre de malades pendant 90 jours (une sorte de simulation)
            for(int jour=1;jour<=jours;jour++){
                //comme un individu envoi 1 mail par semaine a ses collaborateur alors la probabilité
                // est de 1/7 de les infecté (voisins de cet individu(noeud))
                for(Node n:malades){
                    if(random.nextInt(7)+1 == 1){
                        n.neighborNodes().forEach(v->{if (!copieMalades.contains(v) && !immunisation.contains(v)) {
                            //le voisin devient infecté
                            v.setAttribute("health", "infected");
                            // on ajoute ce voisin à la liste
                            copieMalades.add(v);

                        }});
                    }
                    //On ajoute le noeud infecté n à une liste s'il n'y est pas déjà
                    if(!copieMalades.contains(n))
                        copieMalades.add(n);
                }
                //On vide la liste des malades
                malades.clear();

                //Chaque noeud infecté  peut  guérir avec une probabilité de 1/14,
                //soit rester infecté alors dans ce cas on l'ajoute à la liste des malades
                for(Node noeud:copieMalades){
                    if(random.nextInt(14)+1 == 1){
                        noeud.setAttribute("health", "healthy");
                    }
                    else{
                        malades.add(noeud);
                        noeud.setAttribute("health","infected");
                    }
                }
                //On stock le nombre de noeuds infectés ce jour là commme valeur de la case "jour" du tableau nbInfections
                for (Node n:graph){
                    if(n.getAttribute("health")=="infected"){
                        infections[jour] = infections[jour] + 1;
                    }
                }
                copieMalades.clear();
            }
            for(Node n: graph){
                if(!(immunisation.contains(n))){
                    degMoyGrp1 += n.getDegree();
                    nb++;
                }
            }
            degMoyGrp1 = degMoyGrp1/ nb;
            System.out.println("\ndegre moyen dans le scenario 2  : groupe 0 = " + degMoyGrp0 + " , groupe 1 = " + degMoyGrp1);
            try {
                PrintWriter fichier = new PrintWriter(new FileWriter("scenarios/sc2Barbasi.txt"));
                for (int i = 0; i < jours; i++) {
                    // infections[i]  : represente le nombre de malades en fonction du jour i
                    fichier.write(i + "   " + infections[i]++);
                    fichier.println();
                }
                fichier.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }


    /**
     * Simulation du 3eme scénario : On réussit à convaincre 50 % des individus de convaincre
     * un de leurs contacts de mettre à jour en permanence son anti-virus (immunisation sélective).
     */
        public void propagationSc3(){
            Random random = new Random();
            List<Node> malades = new CopyOnWriteArrayList<>();
            long[] infections= new long[91] ;
            int nb=0;
                for(Node noeud:graph)
                    noeud.setAttribute("health", "healthy");
            // liste pour garder l'ensemble des individu deja immunisé
                List<Node> immunisation = new CopyOnWriteArrayList<Node>();
                List<Node> l =  Toolkit.randomNodeSet(graph, graph.getNodeCount()/2);
            double degMoyGrp0=0.0;
            double degMoyGrp1=0.0;
            int nbreMoitierGraphe = (int) graph.getNodeCount()/2;
            Graph scenario3 = graph;



            for (Node noeud: l) {
            //récupére les individus convaicu par l'un de leurs collaborateur (un voisin au hasard de chaque noeud)
                        List<Node> list = noeud.neighborNodes().collect(Collectors.toList());
                degMoyGrp0+= noeud.getDegree();


                for(Node no: list){
                  if(no.getAttribute("health") != "immunazed"){

                     int randomIndex;
                     //on prend un voisin avec random de chaque individu
                        Node randomNoeud;
                        do {
                            randomIndex = random.nextInt(list.size());
                            randomNoeud = list.get(randomIndex);
                        } while (randomNoeud.getAttribute("health") == "immunazed");
                        //son voisin convaincu de maitre à jour en permanance son antinvirus devient immunisé
                        randomNoeud.setAttribute("health", "immunazed");
                        // on l'ajoute dans la liste des immunisés
                        immunisation.add(randomNoeud);
                      degMoyGrp1 += randomNoeud.getDegree();
                  }
                }
            }

            degMoyGrp0 = degMoyGrp0 / nbreMoitierGraphe;
            degMoyGrp1 = degMoyGrp1 / immunisation.stream().count();
            System.out.println("\ndegre moyen dans le scenario 3  : groupe 0 = " + degMoyGrp0 + " , groupe 1 = " + degMoyGrp1);

            for(Node noeud : immunisation) {
                scenario3.removeNode(noeud);
            }
            System.out.println("le seuil épidémique du réseau modifié pour la stratégies d'immunisation du scenario 03 : "
                    +Toolkit.averageDegree(scenario3)/dispersionDegre());

            Node patient0;
                do {
                    //on prend un noeud au hasard du graphe qui n'est pas immunisés qui sera le petient zero infecté
                    patient0 = Toolkit.randomNode(graph);
                }while ((patient0.getAttribute("health") == "immunazed"));

                patient0.setAttribute("health", "infected");
                malades.add(patient0);

                for (int i=1; i<=90; i++){
                    for(Node n: malades) {
                        if (random.nextInt(7) + 1 == 1) {
                            n.neighborNodes().filter(v -> !malades.contains(v) && !immunisation.contains(v)).forEach(v -> {
                                //le voisin devient infecté
                                v.setAttribute("health", "infected");
                                // on ajoute ce voisin à la liste
                                malades.add(v);
                            });
                        }

                    }
                    //Chaque noeud infecté  peut  guérir avec une probabilité de 1/14,
                    //soit rester infecté alors dans ce cas on l'ajoute à la liste des malades
                    for (Node nou: malades){
                            if (random.nextInt(14) + 1 == 1) {
                                nou.setAttribute("health", "healthy");
                                malades.remove(nou);
                            }
                    }
                    for (Node node : graph) {
                        if (node.getAttribute("health") == "infected") {
                            infections[i] ++;
                        }
                    }

                }




               try {
                    PrintWriter fichier = new PrintWriter(new FileWriter("scenarios/sc3Barbasi.txt"));
                    for (int i = 0; i < 90; i++) {
                        // nbInfectes[i]  : represente le nombre de malades en fonction du jour i
                        fichier.write(i + " " + infections[i]);
                        fichier.println();
                    }
                    fichier.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }


}







}
