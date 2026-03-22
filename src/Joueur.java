import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

/**
 * Représente un joueur participant au jeu Master Mind.
 * Cette classe stocke les informations relatives à chaque joueur, y compris son pseudo, le nombre maximum de tentatives,
 * les tentatives actuelles, les résultats de ces tentatives, sa combinaison secrète, son plateau de jeu, ses points,
 * et un indicateur de réussite.
 *
 * @serial Cette classe est sérialisable pour permettre la sauvegarde et la récupération des états de jeu des joueurs.
 */
public class Joueur implements Serializable {
    private static final long serialVersionUID = 1L;


    /**
     * Le pseudo du joueur.
     */
    private final String pseudo;

    /**
     * Le nombre maximum de tentatives que le joueur peut faire dans une partie.
     */
    private final int nbGuessMax;

    /**
     * Le nombre de tentatives que le joueur a déjà faites.
     */
    private int nbGuess;

    /**
     * La liste des résultats de chaque tentative du joueur.
     */
    private List<Resultat> listeResultats;

    /**
     * La combinaison secrète choisie par le joueur pour cette partie.
     */
    private Combinaison combiSecrete;

    /**
     * Le plateau de jeu du joueur, contenant toutes les tentatives faites jusqu'à présent.
     */
    private List<Combinaison> plateau;

    /**
     * Le nombre de points que le joueur a accumulés.
     */
    private int nbPoints;

    /**
     * Indique si le joueur a trouvé la combinaison secrète.
     */
    private boolean aTrouve;

    /**
     * Construit une nouvelle instance de Joueur avec les paramètres spécifiés.
     * Ce constructeur initialise le joueur avec un pseudo, un nombre maximal de tentatives,
     * et crée la combinaison secrète basée sur le nombre de pions et de couleurs fournies.
     * Il initialise également les structures de données pour suivre les tentatives et les résultats.
     * 
     * @param pseudo Le pseudo du joueur
     * @param nbGuessMax Le nombre de tentatives possibles
     * @param nbGuess Le nombre de tentatives effectuées
     * @param nbPions Le nombre de pions dans une combinaison
     * @param nbCouleur Le nombre de couleurs configuré
     */
    public Joueur(String pseudo, int nbGuessMax, int nbGuess, int nbPions, int nbCouleur) {
        this.pseudo = pseudo;
        this.nbGuessMax = nbGuessMax;
        this.nbGuess = nbGuess;
        this.listeResultats = new ArrayList<Resultat>();
        this.combiSecrete = Combinaison.buildSecretCombinaison(nbPions, nbCouleur);
        this.plateau = new ArrayList<Combinaison>();
        this.nbPoints = 0;
        this.aTrouve = false;
    }

    /**
     * Crée une tentative de devinette en construisant une nouvelle combinaison de pions.
     * Cette méthode délègue la création de la combinaison de pions à la méthode {@code makeGuessCombinaison}
     * de la classe {@code Combinaison}, qui gère la logique de sélection des pions en fonction des entrées du joueur.
     *
     * @param nbPions Le nombre de pions que doit contenir chaque tentative.
     * @param nbCouleurs Le nombre de couleurs différentes disponibles pour les pions.
     * @param indexJoueur L'index du joueur dans la liste des joueurs du jeu, utilisé pour identifier qui fait la tentative.
     * @param jeu L'instance actuelle du jeu, utilisée pour accéder aux méthodes et paramètres nécessaires lors de la création de la tentative.
     * @param numPartie Le numéro de la partie actuelle, utilisé pour référencer des états spécifiques du jeu si nécessaire.
     * @param numTour Le numéro du tour actuel, utilisé pour des logiques de jeu spécifiques au tour.
     * @return Une nouvelle {@code Combinaison} représentant la tentative du joueur.
     */
    private Combinaison creerTentative(int nbPions, int nbCouleurs, int indexJoueur, Jeu jeu, int numPartie, int numTour) {
        // Appelle la méthode de la classe Combinaison pour créer une nouvelle tentative basée sur les paramètres fournis.
        return Combinaison.makeGuessCombinaison(nbPions, nbCouleurs, indexJoueur, jeu, numPartie, numTour);
    }

    /**
     * Effectue une tentative pour deviner la combinaison secrète et évalue le résultat.
     * Cette méthode crée une nouvelle tentative basée sur le nombre de pions et de couleurs spécifiés,
     * compare cette tentative à la combinaison secrète du joueur, enregistre le résultat,
     * et incrémente le compteur de tentatives du joueur.
     *
     * @param nbPions Le nombre de pions que doit contenir chaque tentative.
     * @param nbCouleurs Le nombre de couleurs différentes disponibles pour les pions.
     * @param indexJoueur L'index du joueur dans la liste des joueurs du jeu, utilisé pour des interactions spécifiques.
     * @param jeu L'instance actuelle du jeu contenant des paramètres et des méthodes de contrôle du jeu.
     * @param numPartie Le numéro de la partie actuelle, utilisé pour le suivi du jeu.
     * @param numTour Le numéro du tour actuel, utilisé pour des enregistrements ou des logiques spécifiques de tour.
     * @return Un objet {@code Resultat} contenant les détails du résultat de la tentative.
     * @throws Exception Relaye les exceptions générées par la comparaison des combinaisons ou autres erreurs.
     */
    public Resultat jouer(int nbPions, int nbCouleurs, int indexJoueur, Jeu jeu, int numPartie, int numTour) throws Exception {
        // Crée une tentative en demandant au joueur de sélectionner les pions.
        Combinaison tentative = creerTentative(nbPions, nbCouleurs, indexJoueur, jeu, numPartie, numTour);
        try{
            // Tente de comparer la combinaison tentative avec la combinaison secrète
            Resultat resultat = combiSecrete.compareCombinaison(tentative);
            listeResultats.add(resultat);
            this.nbGuess++;
            return resultat;
        } catch(Exception e) {
            throw e; // En cas d'erreur lors de la comparaison, l'exception est relancée.
        }
    }

    /**
     * Afficher le plateau du joueur composé des tentatives précédentes accompagnés des indications de bases ou plus précises en fonction de la configuration de l'aide
     * @param config Configuration du jeu
     */
    public void affichePlateau(Configuration config) {
        for(int i = 0; i < this.plateau.size(); i++) {
            Combinaison tentative = this.plateau.get(i);
            // Affiche chaque pion dans la tentative.
            for(Pion pion : tentative.getPions()) {
                String symbole = "\u25CF"; // Symbole unicode pour un rond plein
                String couleur = Pion.getANSIColor(pion); // Obtient la couleur ANSI correspondante au pion.
                if( pion.getCouleur() == Pion.Couleur.VIDE ) {
                    symbole = "\u25CB"; // Symbole Unicode pour un rond non plein
                }
                System.out.print( couleur + symbole + " " + "\u001B[0m"); // Affiche le pion coloré et réinitialise la couleur.
            }
            // Affiche les résultats selon que l'aide est activée ou non.
            if(config.getAide()){
                System.out.println(this.listeResultats.get(i).getPositionResultStringFormat()); // Affiche le résultat avec des détails sur chaque pion.
            } else {
                System.out.println("V:" + this.listeResultats.get(i).getNbBonnePos() + 
                                  " P:" + this.listeResultats.get(i).getNbBadPos()); // Affiche seulement le nombre de pions bien et mal placés.
            }
        }
    }

    /**
     * Génère une nouvelle combinaison secrète pour le joueur.
     * Cette méthode crée une nouvelle combinaison secrète aléatoire en utilisant le nombre spécifié de pions et de couleurs.
     *
     * @param nbPions Le nombre de pions que la combinaison secrète doit contenir.
     * @param nbCouleur Le nombre de couleurs différentes possibles pour chaque pion.
     */
    public void newCombiSecrete(int nbPions, int nbCouleur) {
        this.combiSecrete = Combinaison.buildSecretCombinaison(nbPions, nbCouleur);
    }

    /**
     * Efface toutes les tentatives précédemment stockées sur le plateau du joueur.
     * Cette méthode est utile pour réinitialiser le plateau du joueur à l'état vide, généralement utilisée
     * entre les parties ou lorsque le jeu recommence.
     */
    public void viderPlateau() {
        this.plateau.clear();
    }

    /**
     * Efface tous les résultats des tentatives précédentes du joueur.
     * Cette méthode est utile pour réinitialiser la liste des résultats à l'état vide, 
     * utilisée généralement entre les parties ou en préparation pour une nouvelle partie.
     */
    public void viderListeResultats() {
        this.listeResultats.clear();
    }
    
    /**
     * Incrémente de 1 le nombre de tentatives effectuées par le joueur
     */
    public void incrementNbGuess() {
        this.nbGuess++;
    }

    /**
     * Getter de la combinaison secrète du joueur.
     * @return La combinaison secrète actuellement définie pour le joueur.
     */
    public Combinaison getCombiSecrete() {
        return this.combiSecrete;
    }

    /**
     * Getter de la liste des résultats de toutes les tentatives du joueur.
     * @return La liste contenant les résultats des tentatives.
     */
    public List<Resultat> getListeResultats() {
        return this.listeResultats;
    }

    /**
     * Getter du nombre maximal de tentatives que le joueur peut faire.
     * @return Le nombre maximal de tentatives.
     */
    public int getNbGuessMax() {
        return this.nbGuessMax;
    }

    /**
     * Getter du nombre de tentatives déjà effectuées par le joueur.
     * @return Le nombre actuel de tentatives.
     */
    public int getNbGuess() {
        return this.nbGuess;
    }

    /**
     * Définit le nombre de tentatives effectuées par le joueur.
     * @param nbGuess Le nouveau nombre de tentatives.
     */
    public void setNbGuess(int nbGuess) {
        this.nbGuess = nbGuess;
    }

    /**
     * Getter du pseudo du joueur.
     * @return Le pseudo du joueur.
     */
    public String getPseudo() {
        return this.pseudo;
    }

    /**
     * Getter du plateau de jeu du joueur, contenant toutes ses tentatives.
     * @return La liste des combinaisons tentées par le joueur.
     */
    public List<Combinaison> getPlateau() {
        return this.plateau;
    }

    /**
     * Getter du nombre de points accumulés par le joueur.
     * @return Le nombre total de points.
     */
    public int getNbPoints() {
        return this.nbPoints;
    }

    /**
     * Définit le nombre de points du joueur.
     * @param nbPoints Le nouveau total de points.
     */
    public void setNbPoints(int nbPoints) {
        this.nbPoints = nbPoints;
    }

    /**
     * Indique si le joueur a trouvé la combinaison secrète.
     * @return vrai si le joueur a trouvé la combinaison, faux sinon.
     */
    public boolean getATrouve() {
        return this.aTrouve;
    }

    /**
     * Définit si le joueur a trouvé la combinaison secrète.
     * @param bool vrai pour indiquer que le joueur a trouvé la combinaison, faux sinon.
     */
    public void setATrouve(boolean bool) {
        this.aTrouve = bool;
    }

    /**
     * Construit et retourne une représentation textuelle de toutes les tentatives sur le plateau du joueur.
     * Cette méthode parcourt toutes les tentatives enregistrées dans le plateau du joueur, convertissant chaque
     * combinaison en sa représentation textuelle et les concaténant pour former une seule chaîne de caractères.
     *
     * @return Une chaîne de caractères représentant toutes les tentatives du joueur sous forme de texte.
     */
    public String plateauString() {
        String plateauStringFormat = ""; // Initialisation de la chaîne de caractères qui accumulera les résultats.
        for( Combinaison tentative : this.plateau ) {
            plateauStringFormat += tentative.toString(); // Ajoute la représentation textuelle de chaque tentative à la chaîne.
        }
        return plateauStringFormat;
    }

    /**
     * Construit et retourne une représentation textuelle de la liste des résultats de toutes les tentatives du joueur.
     * Cette méthode parcourt tous les résultats stockés dans la liste des résultats du joueur, en convertissant
     * chaque résultat en sa représentation textuelle et en les concaténant pour former une seule chaîne de caractères.
     *
     * @return Une chaîne de caractères représentant tous les résultats des tentatives du joueur sous forme de texte.
     */
    public String listeResultatString() {
        String listeResStringFormat = ""; // Initialisation de la chaîne qui accumulera les descriptions des résultats.
        for (Resultat resultat : this.listeResultats ) {
            listeResStringFormat += resultat.toString(); // Ajoute la représentation textuelle de chaque résultat à la chaîne.
        }
        return listeResStringFormat; // Retourne la chaîne complète représentant les résultats.
    }

    /**
     * Fournit une représentation textuelle complète de l'état actuel du joueur.
     * Cette méthode rassemble des informations sur le pseudo, le nombre maximal de tentatives,
     * le nombre actuel de tentatives, les résultats des tentatives, la combinaison secrète,
     * le plateau de jeu, le nombre de points, et si le joueur a trouvé la combinaison secrète,
     * en utilisant des méthodes auxiliaires pour formater les listes et les combinaisons.
     *
     * @return Une chaîne de caractères détaillant tous les aspects de l'état du joueur.
     */
    @Override
    public String toString() {
        return "pseudo=" + this.pseudo +
               ", nbGuessMax=" + this.nbGuessMax +
               ", nbGuess=" + this.nbGuess +
               ", listeResultat=" + listeResultatString() + // Utilise listeResultatString pour la représentation des résultats.
               ", combiSecrete=" + this.combiSecrete.toString() +
               ", plateau=" + plateauString() + // Utilise plateauString pour la représentation du plateau.
               ", nbPoints=" + this.nbPoints +
               ", aTrouve=" + this.aTrouve;
    }
}
