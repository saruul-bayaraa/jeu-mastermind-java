import java.io.Serializable;

/**
 * Configuration de base pour une partie du jeu Master Mind.
 * Cette classe encapsule tous les paramètres nécessaires pour configurer une partie du jeu,
 * incluant le nombre de couleurs, le nombre maximum de tentatives, le nombre de pions par tentative,
 * le nombre de joueurs, et le nombre de parties à jouer, ainsi que si des aides sont disponibles.
 *
 * @serial Cette classe est sérialisable pour permettre la sauvegarde et le chargement de la configuration.
 */
public class Configuration implements Serializable{
    private static final long serialVersionUID = 2L;

    /**
     * Nombre de couleurs différentes disponibles pour les pions.
     */
    private int nbCouleurs;

    /**
     * Nombre maximum de tentatives autorisées par partie.
     */
    private int nbGuessMax;

    /**
     * Nombre de pions utilisés dans chaque tentative.
     */
    private int nbPions;

    /**
     * Nombre de joueurs participant au jeu.
     */
    private int nbJoueurs;

    /**
     * Nombre total de parties à jouer.
     */
    private int nbParties;

    /**
     * Indicateur permettant de savoir si l'aide est disponibles pour le joueur.
     */
    private boolean aide;

    /**
     * Construit une nouvelle configuration avec les paramètres spécifiés.
     *
     * @param nbCouleurs Nombre de couleurs disponibles pour les pions.
     * @param nbGuessMax Nombre maximal de tentatives autorisées.
     * @param nbPions Nombre de pions par tentative.
     * @param nbJoueurs Nombre de joueurs dans le jeu.
     * @param nbParties Nombre de parties à jouer.
     * @param aide Indique si l'aide est disponible pour aider les joueurs.
     */
    public Configuration(int nbCouleurs, int nbGuessMax, int nbPions, int nbJoueurs, int nbParties, boolean aide) {
        this.nbCouleurs = nbCouleurs;
        this.nbGuessMax = nbGuessMax;
        this.nbPions = nbPions;
        this.nbJoueurs = nbJoueurs;
        this.nbParties = nbParties;
        this.aide = aide;
    }

    /**
     * Crée une configuration pour une partie du jeu Master Mind en interrogeant l'utilisateur pour obtenir les paramètres nécessaires.
     * Cette méthode guide l'utilisateur à travers une série de questions pour définir les paramètres de jeu, tels que
     * le nombre de couleurs, le nombre maximal de tentatives, le nombre de pions par tentative, le nombre de joueurs,
     * le nombre de parties à jouer et l'activation de l'aide.
     *
     * @return Une instance de {@code Configuration} initialisée avec les valeurs fournies par l'utilisateur.
     */
    public static Configuration createConfig() {
        int nbCouleurs = lireEntierAvecIntervalle("Veuillez entrer le nombre de couleurs (6 à 8) :", 6, 8);
        int nbGuessMax = lireEntierAvecChoix("Veuillez entrer le nombre maximal de tentatives (10 ou 12) :", 10, 12);
        int nbPions = lireEntierAvecChoix("Veuillez entrer le nombre de pions par tentative (4 ou 5) :", 4, 5);
        int nbJoueurs = lireEntier("Veuillez entrer le nombre de joueurs :");
        int nbParties = lireEntier("Veuillez entrer le nombre de parties à jouer :");
        boolean aide = lireEntierAvecChoix("Voulez-vous activer l'aide ? (0: non, 1: oui) :",0,1) == 1;

        return new Configuration(nbCouleurs, nbGuessMax, nbPions, nbJoueurs, nbParties, aide);
    }

    /**
     * Demande à l'utilisateur de saisir un nombre entier et le retourne.
     * Cette méthode affiche un message d'invite spécifié, lit la saisie de l'utilisateur,
     * et tente de convertir cette saisie en un entier. Si la conversion échoue en raison d'une saisie non numérique,
     * un message d'erreur est affiché et l'utilisateur est invité à entrer de nouveau un nombre.
     *
     * @param prompt Le message à afficher à l'utilisateur pour l'invite.
     * @return L'entier saisi par l'utilisateur.
     * @throws NumberFormatException si la saisie ne peut pas être convertie en entier, la méthode s'appelle récursivement
     *         pour demander à nouveau une entrée.
     */
    private static int lireEntier(String prompt) {
        System.out.println(prompt);
        String saisieJoueur = System.console().readLine(); // Lit la saisie de l'utilisateur.
        try {
            // Tente de convertir la saisie en entier et la retourne si la conversion est réussie.
            return Integer.parseInt(saisieJoueur);
        } catch (NumberFormatException e) {
            // Affiche un message d'erreur si la saisie n'est pas un entier valide.
            System.out.println("Erreur: veuillez entrer un nombre entier valide.");
            // Appel récursif pour demander à nouveau une entrée en cas d'erreur.
            return lireEntier(prompt);
        }
    }

    /**
     * Demande à l'utilisateur de saisir un nombre entier dans un intervalle spécifié et le retourne.
     * Cette méthode répète la demande jusqu'à ce que l'utilisateur fournisse un entier valide qui se trouve
     * entre les valeurs minimale et maximale incluses. Si l'entrée est hors de cet intervalle,
     * un message d'erreur est affiché et une nouvelle saisie est demandée.
     *
     * @param prompt Le message à afficher à l'utilisateur pour l'invite.
     * @param min La valeur minimale acceptable pour l'entrée.
     * @param max La valeur maximale acceptable pour l'entrée.
     * @return L'entier saisi par l'utilisateur qui se trouve dans l'intervalle spécifié.
     */
    private static int lireEntierAvecIntervalle(String prompt, int min, int max) {
        int valeur;
        do {
            // Demande un entier à l'utilisateur.
            valeur = lireEntier(prompt);
            // Vérifie si la valeur est hors de l'intervalle spécifié et informe l'utilisateur le cas échéant.
            if (valeur < min || valeur > max) {
                System.out.println("Veuillez entrer un nombre entre " + min + " et " + max + ".");
            }
        } while (valeur < min || valeur > max); // Répète jusqu'à obtenir une valeur valide.
        return valeur;
    }

    /**
     * Demande à l'utilisateur de saisir un des deux choix numériques spécifiques et le retourne.
     * Cette méthode répète la demande jusqu'à ce que l'utilisateur fournisse une des deux valeurs autorisées.
     * Si l'entrée est différente de ces deux choix, un message d'erreur est affiché et une nouvelle saisie est demandée.
     *
     * @param prompt Le message à afficher à l'utilisateur pour l'invite.
     * @param choix1 Le premier choix numérique valide.
     * @param choix2 Le second choix numérique valide.
     * @return L'entier saisi par l'utilisateur qui correspond à l'un des deux choix spécifiés.
     */
    private static int lireEntierAvecChoix(String prompt, int choix1, int choix2) {
        int valeur;
        do {
            // Demande un entier à l'utilisateur.
            valeur = lireEntier(prompt);
            // Vérifie si la valeur est l'un des deux choix valides et informe l'utilisateur le cas échéant.
            if (valeur != choix1 && valeur != choix2) {
                System.out.println("Veuillez entrer " + choix1 + " ou " + choix2 + ".");
            }
        } while (valeur != choix1 && valeur != choix2);  // Répète jusqu'à obtenir une valeur valide.
        return valeur;
    }

    /**
     * Getter du nombre de couleurs disponibles pour les pions.
     * @return Le nombre de couleurs.
     */
    public int getNbCouleurs() {
        return this.nbCouleurs;
    }

    /**
     * Getter du nombre maximal de tentatives autorisées par jeu.
     * @return Le nombre maximum de tentatives.
     */
    public int getNbGuessMax() {
        return this.nbGuessMax;
    }

    /**
     * Getter du nombre de joueurs participant au jeu.
     * @return Le nombre de joueurs.
     */
    public int getNbJoueurs() {
        return this.nbJoueurs;
    }

    /**
     * Getter du nombre de pions par tentative.
     * @return Le nombre de pions.
     */
    public int getNbPions() {
        return this.nbPions;
    }

    /**
     * Getter du nombre total de parties à jouer.
     * @return Le nombre de parties.
     */
    public int getNbParties() {
        return this.nbParties;
    }

    /**
     * Getter de l'option d'aide activée ou non.
     * @return true si l'aide est activée, false sinon.
     */
    public boolean getAide() {
        return this.aide;
    }


    /**
     * Fournit une représentation textuelle des paramètres de configuration du jeu.
     * Cette méthode est utile pour l'affichage des configurations dans les logs ou interfaces utilisateurs,
     * facilitant la vérification rapide des paramètres actuels du jeu.
     *
     * @return Une chaîne de caractères décrivant tous les paramètres de configuration.
     */
    @Override
    public String toString() {
        return "nbCouleurs=" + this.nbCouleurs +
            ", nbGuessMax=" + this.nbGuessMax +
            ", nbPions=" + this.nbPions +
            ", nbJoueurs=" + this.nbJoueurs +
            ", nbParties=" + this.nbParties +
            ", aide=" + (this.aide ? "oui" : "non");
    }

}
