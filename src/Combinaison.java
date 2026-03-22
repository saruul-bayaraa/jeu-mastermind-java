import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import javax.naming.SizeLimitExceededException;
import java.io.Serializable;

/**
 * Cette classe représente une combinaison de pions dans le jeu du Master Mind. 
 * Elle stocke un tableau de Pions représentant la combinaison et un entier représentant le nombre de couleurs.
 * 
 * @serial Cette classe est sérialisable pour permettre la sauvegarde et la récupération des états de jeu.
 */
public class Combinaison implements Serializable {
    /**
     * serialVersionUID utilisé pour la sérialisation afin d'assurer une compatibilité de version lors de la désérialisation.
     */
    private static final long serialVersionUID = 3L;

    /**
     * Tableau de pions représentant la combinaison de pions dans le jeu.
     */
    private Pion pions[];

    /**
     * Nombre de couleurs différentes utilisées dans cette combinaison. 
     */
    private final int nbCouleur;

    /**
     * Constructeur privé pour créer une nouvelle combinaison de pions.
     * Ce constructeur est utilisé pour initialiser une combinaison avec une série spécifique de pions
     * et un nombre défini de couleurs différentes.
     *
     * @param pions Tableau de pions constituant la combinaison.
     * @param nbCouleur Nombre de couleurs différentes dans la combinaison.
     */
    private Combinaison(Pion pions[], int nbCouleur){
        this.pions = pions;
        this.nbCouleur = nbCouleur;
    }
    
    /**
     * Construit une combinaison secrète de pions.
     * Cette méthode génère aléatoirement une séquence de pions avec un nombre donné de couleurs.
     * Chaque pion dans la combinaison est choisi aléatoirement parmi le nombre de couleurs disponibles.
     *
     * @param nbPions Le nombre de pions dans la combinaison secrète.
     * @param nbCouleur Le nombre total de couleurs possibles pour chaque pion.
     * @return Une nouvelle instance de {@code Combinaison} qui représente la combinaison secrète générée.
     */
    public static Combinaison buildSecretCombinaison(int nbPions, int nbCouleur){
        // Crée un nouvel objet Random pour la génération de nombres aléatoires.
        Random random = new Random();

        // Initialise le tableau pour stocker les pions de la combinaison secrète.
        Pion[] combinaisonSecret = new Pion[nbPions];

        // Boucle pour remplir le tableau de combinaison avec des pions de couleurs aléatoires.
        for(int i=0; i<nbPions; i++){
            // Génère un indice de couleur aléatoire pour chaque pion et l'ajoute au tableau.
            combinaisonSecret[i] = new Pion(random.nextInt(nbCouleur));
        }
        
        // Retourne une nouvelle instance de Combinaison contenant les pions générés.
        return new Combinaison(combinaisonSecret, nbCouleur);

    }

    /**
     * Permet au joueur de construire une combinaison de tentative.
     * 
     * @param nbPions Le nombre de pions dans la combinaison tentative.
     * @param nbCouleur Le nombre de couleurs disponibles
     * @param indexJoueur Numéro du joueur qui fait la tentative
     * @param jeu L'instance de Jeu concernée
     * @param numPartie Le numéro de la partie
     * @param numTour Le numéro de tour (tentative)
     * @return La combinaison tentative construite par le joueur
     */
    public static Combinaison makeGuessCombinaison(int nbPions, int nbCouleur, int indexJoueur, Jeu jeu, int numPartie, int numTour) {
        // Tableau de pions représentant la combinaison tentative
        Pion[] tentative = new Pion[nbPions];

        // Affiche les couleurs disponibles
        afficherCouleursDispo(nbCouleur);  
    
        // Compteur de pions 
        int countPions = 0;

        // Boucle while qui demande à l'utilisateur les pions qu'il tente tant que tous les pions ne sont pas valides 
        while (countPions < nbPions) {
            // Chaîne de caractères qui varie en fonction du numéro du joueur
            String promptMessage = getPromptMessage(indexJoueur);
            System.out.println(promptMessage);

            // Saisie du joueur
            String input = System.console().readLine().trim();
    
            // Seul le joueur 1 peut sauvegarder, si la sauvegarde est effective: retour au menu, sinon le jeu continue
            if (saveByPlayer1(input, indexJoueur, jeu, numPartie, numTour)) continue;
    
            // Vérifie si le pion est valide
            if (estPionValide(input, nbCouleur, tentative, countPions)) {
                countPions++;
            }
        }
    
        return new Combinaison(tentative, nbCouleur);
    }
    
    /**
     * Affiche les couleurs disponible en fonction de la configuration (nbCouleur)
     * 
     * @param nbCouleur Nombre de couleurs disponibles déterminé au moment de la configuration
     */
    private static void afficherCouleursDispo(int nbCouleur) {
        System.out.println("\nListe des couleurs disponibles : ");
        for (int i = 0; i < nbCouleur; i++) {
            System.out.println(i + "- " + Pion.Couleur.getCouleur(i));
        }
    }
    
    /**
     * Choisis le prompt en fonction de l'index du joueur car seul le joueur 1 (index 0) peut sauvegarder
     * 
     * @param indexJoueur Numéro du joueur 
     * @return La chaîne de caractères pour le prompt
     */
    private static String getPromptMessage(int indexJoueur) {
        return indexJoueur == 0
            ? "\nEntrez le numéro de la couleur souhaitée ou tapez 's' pour sauvegarder et quitter: "
            : "\nEntrez le numéro de la couleur souhaitée: ";
    }
    
    /**
     * Vérifie si le joueur 1 veut sauvegarder la partie. 
     * S'il joue normalement et ne sauvegarde pas: renvoit false.
     * Sinon: Tentative de sauvegarde et renvoit true
     * 
     * @param input Saisie du joueur
     * @param indexJoueur Numéro du joueur
     * @param jeu Instance de Jeu concernée
     * @param numPartie Numéro de la partie
     * @param numTour Numéro de tour
     * @return Booléen en fonction de la saisie et du numéro de joueur.
     */
    private static boolean saveByPlayer1(String input, int indexJoueur, Jeu jeu, int numPartie, int numTour) {
        // Si c'est le joueur 1 et qu'il tape 's'
        if (input.equalsIgnoreCase("s") && indexJoueur == 0) {

            // On appelle la fonction save() pour sauvegarder le jeu
            if (jeu.save(numPartie, numTour)) {
                System.out.println("Sauvegarde réussie. Le jeu va se terminer.");
                // On retourne au menu du jeu
                Jeu.gameMenu();
            } else { // Si la sauvegarde n'est pas effective, le jeu continue
                System.out.println("Échec de la sauvegarde, le jeu continue.");
            }
            return true;
        }

        // Si ce n'est pas le joueur 1 ou que le joueur 1 ne saisit pas 's', la fonction retourne false
        return false;
    }
    
    /**
     * Vérifie si un pion est valide.
     * 
     * @param input Saisie du joueur
     * @param nbCouleur Le nombre de couleurs disponibles
     * @param tentative La combinaison tentative du joueur
     * @param countPions Le numéro de pion
     * @return Booléen indiquant si le pion est valide ou pas
     */
    private static boolean estPionValide(String input, int nbCouleur, Pion[] tentative, int countPions) {
        try {
            // Tente de convertir l'entrée utilisateur en entier pour obtenir le numéro de la couleur.
            int numCouleur = Integer.parseInt(input);
    
            // Vérifie si le numéro de couleur entré est dans la plage valide des options disponibles.
            if (numCouleur >= 0 && numCouleur < nbCouleur) {
                // Si le numéro est valide, crée un nouveau pion avec cette couleur et l'ajoute à la tentative.
                tentative[countPions] = new Pion( numCouleur );
                return true;
            } else {
                System.out.println("Erreur: Entrez un numéro entre 0 et " + (nbCouleur-1));
            }
        } catch (NumberFormatException e) {
            // Gère le cas où l'entrée ne peut pas être convertie en entier, indiquant une erreur de format.
            System.out.println("Erreur: Entrez un numéro.");
        }
        return false;
    }

    /**
     * Compare la combinaison tentée par le joueur avec la combinaison secrète du jeu.
     * Cette méthode évalue chaque pion de la tentative pour déterminer combien sont correctement positionnés
     * et combien sont corrects en couleur mais mal placés.
     *
     * @param tentativeCombi La combinaison tentée par le joueur à comparer avec cette combinaison secrète.
     * @return Un objet {@code Resultat} contenant le nombre de pions bien placés, le nombre de pions de bonne couleur mais mal placés,
     *         et une représentation sous forme de tableau de caractères indiquant la précision de chaque pion.
     * @throws SizeLimitExceededException Si les tailles des combinaisons (cette combinaison et la combinaison tentée) ne correspondent pas.
     */
    public Resultat compareCombinaison(Combinaison tentativeCombi) throws SizeLimitExceededException {
        // Vérifie si les tailles des combinaisons sont identiques.
        if (tentativeCombi.pions.length != this.pions.length) {
            throw new SizeLimitExceededException("Les tailles des combinaisons ne correspondent pas.");
        }

        // Copie des pions de cette combinaison pour manipulation sans altérer la combinaison originale.
        List<Pion> copieCombi = new ArrayList<>(Arrays.asList(this.pions));

        // Initialisation du tableau de résultats avec 'F' indiquant "faux" pour chaque pion.
        Character[] positionResult = UtilsArray.repeatCharElement('F', this.pions.length);

        // Liste pour garder une trace des indices des pions qui ne sont pas correctement positionnés.
        List<Integer> indicePasVrai = new ArrayList<>();

        // Calcul du nombre de pions bien positionnés et mise à jour de la copie et des résultats.
        int nbBonnePos = calculerBonnePos(tentativeCombi.pions, copieCombi, positionResult, indicePasVrai);

        // Calcul du nombre de pions de bonne couleur mais mal positionnés après retrait des bien placés.
        int nbBadPos = calculerBadPos(tentativeCombi.pions, copieCombi, positionResult, indicePasVrai);

        return new Resultat(nbBonnePos, nbBadPos, positionResult, tentativeCombi);
    }

    /**
     * Calcule et compte le nombre de pions qui sont exactement à la bonne position dans la tentative par rapport à la combinaison secrète.
     * Les pions correctement positionnés sont retirés de la copie de la combinaison pour ne pas être recomptés comme mal placés
     * dans les étapes ultérieures de la comparaison.
     *
     * @param tentative Tableau de pions représentant la tentative du joueur.
     * @param copieCombi Liste modifiable des pions de cette combinaison, utilisée pour retirer les pions déjà comptés.
     * @param positionResult Tableau de caractères qui est mis à jour avec 'V' pour les positions validées correctement.
     * @param indicePasVrai Liste pour stocker les indices des pions qui ne sont pas correctement positionnés.
     * @return Le nombre de pions qui sont correctement positionnés.
     */
    private int calculerBonnePos(Pion[] tentative, List<Pion> copieCombi, Character[] positionResult, List<Integer> indicePasVrai) {
        int nbBonnePos = 0;
        for (int i = 0; i < this.pions.length; i++) {
            if (this.pions[i].equals(tentative[i])) {
                nbBonnePos++;
                copieCombi.remove(this.pions[i]); // Retire le pion de la copie pour éviter de le recompter.
                positionResult[i] = 'V'; // Marque la position comme valide.
            } else {
                indicePasVrai.add(i); // Ajoute l'indice à la liste des positions incorrectes.
            }
        }
        return nbBonnePos;
    }

    /**
     * Calcule le nombre de pions qui sont de la bonne couleur mais mal positionnés dans la tentative par rapport à cette combinaison.
     * Cette méthode utilise les indices des pions qui n'ont pas été correctement positionnés pour vérifier leur correspondance
     * en couleur avec les pions restants dans la combinaison.
     *
     * @param tentative Tableau de pions représentant la tentative du joueur.
     * @param copieCombi Liste modifiable des pions de cette combinaison, utilisée pour retirer les pions déjà comptés comme mal placés.
     * @param positionResult Tableau de caractères qui est mis à jour avec 'P' pour les positions partiellement correctes (bonne couleur, mauvaise position)
     *                       et 'F' pour les positions incorrectes.
     * @param indicePasVrai Liste des indices des pions qui n'étaient pas correctement positionnés selon le calcul précédent.
     * @return Le nombre de pions qui sont de la bonne couleur mais mal positionnés.
     */
    private int calculerBadPos(Pion[] tentative, List<Pion> copieCombi, Character[] positionResult, List<Integer> indicePasVrai) {
        int nbBadPos = 0;
        for (int indice : indicePasVrai) {
            boolean trouve = false;
            for (int y = 0; y < copieCombi.size() && !trouve; y++) {
                if (tentative[indice].equals(copieCombi.get(y))) {
                    trouve = true;
                    nbBadPos++;
                    copieCombi.remove(y);
                    positionResult[indice] = 'P'; // Marque la position comme partiellement correcte.
                }
            }
            if (!trouve) {
                positionResult[indice] = 'F'; // Marque la position comme fausse.
            }
        }
        return nbBadPos;
    }

    /**
     * Getter pour récupérer le tableau de pions de la combinaison
     * 
     * @return Le tableau de pions de la combinaison
     */
    public Pion[] getPions() {
        return this.pions;
    }

    /**
     * Fournit une représentation sous forme de chaîne de caractères de la combinaison.
     * Cette représentation comprend une liste des pions contenus dans la combinaison, séparés par des espaces.
     * Chaque pion est converti en chaîne de caractères en utilisant sa méthode `toString()`.
     *
     * @return Une chaîne de caractères représentant la combinaison de pions, où chaque pion est séparé par un espace.
     */
    @Override
    public String toString() {
        String r="";
        for(int i=0; i<this.pions.length; i++){
            r+= " " + pions[i];
        }
        return r;
    }
}