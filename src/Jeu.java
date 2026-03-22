import java.util.List;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

/**
 * Représente la logique centrale du jeu Master Mind, gérant la configuration du jeu,
 * la liste des joueurs, et le déroulement des parties et des tours.
 * Cette classe encapsule toutes les interactions principales du jeu et fournit
 * des méthodes pour démarrer le jeu, jouer des tours, et gérer les résultats.
 */
public class Jeu {
    /**
     * Liste des joueurs participant au jeu.
     */
    private List<Joueur> listeJoueurs;

    /**
     * Configuration du jeu, incluant des paramètres tels que le nombre de couleurs, de pions, etc.
     */
    private Configuration config;

    /**
     * Numéro de la partie courante, utilisé pour suivre le nombre de parties jouées.
     */
    private int numPartieCourante;

    /**
     * Numéro du tour courant dans la partie actuelle.
     */
    private int numTourCourant;

    /**
     * Construit une nouvelle instance de Jeu avec une configuration spécifique.
     * Initialise la liste des joueurs et configure le jeu selon les paramètres fournis.
     *
     * @param config La configuration du jeu contenant les paramètres nécessaires à l'initialisation.
     */
    public Jeu(Configuration config) {
        this.config = config;
        this.listeJoueurs = creerListeJoueurs();  // Crée et initialise la liste des joueurs basée sur la configuration.
        this.numPartieCourante = 0;  // Initialise le compteur de parties à 0.
        this.numTourCourant = 0;  // Initialise le compteur de tours à 0.
    }

    /**
     * Constructeur par défaut pour Jeu. Peut être utilisé pour initialiser un jeu sans paramètres spécifiques
     * avant de définir la configuration.
     */
    public Jeu() {}

    /**
     * Crée et initialise une liste de joueurs pour le jeu.
     * Cette méthode interroge les utilisateurs pour obtenir leur pseudo et crée un objet Joueur pour chacun,
     * configuré selon les paramètres définis dans la configuration du jeu. Elle est utilisée lors de l'initialisation
     * d'une nouvelle partie pour s'assurer que tous les joueurs sont prêts à jouer avec les paramètres appropriés.
     *
     * @return Une liste contenant les objets Joueur initialisés.
     */
    private List<Joueur> creerListeJoueurs() {
        List<Joueur> joueurs = new ArrayList<Joueur>();
        // Boucle sur le nombre de joueurs spécifié dans la configuration pour créer chaque joueur.
        for( int i = 0; i < this.config.getNbJoueurs(); i++ ) {
            System.out.println("Pseudo du joueur " + (i + 1) + ":");
            // Lit le pseudo du joueur depuis la console.
            String pseudo = System.console().readLine();

            // Crée un nouveau joueur avec le pseudo fourni et les paramètres de jeu de la configuration.
            Joueur joueur = new Joueur(pseudo, this.config.getNbGuessMax(), 0, this.config.getNbPions(), this.config.getNbCouleurs());
            
            // Ajoute le nouveau joueur à la liste des joueurs.
            joueurs.add(joueur);
        }
        return joueurs;
    }

    /**
     * Lance et exécute une partie du jeu Master Mind, en gérant les tours jusqu'à ce que la partie se termine.
     * Cette méthode contrôle le flux d'une partie, permettant à chaque joueur qui n'a pas encore trouvé la combinaison secrète
     * de faire une tentative à chaque tour. La partie continue jusqu'à ce que le nombre maximum de tentatives soit atteint
     * ou que tous les joueurs aient trouvé leur combinaison secrète.
     *
     * @param numPartie Le numéro de la partie actuelle, utilisé pour le suivi et la logistique du jeu.
     * @param numTour Le numéro du tour de départ pour cette partie; généralement 0 au début de la partie.
     */
    private void lancerPartie(int numPartie, int numTour) {
        // Continue le jeu tant que le nombre de tours n'atteint pas le maximum et qu'il reste des joueurs actifs.
        while((numTour < this.config.getNbGuessMax() && estJouable() ) ) {
            for( Joueur joueur : this.listeJoueurs ) {
                if (!joueur.getATrouve()){ // Seuls les joueurs qui n'ont pas encore trouvé jouent
                    faireTentative(joueur, numPartie, numTour);
                } 
            }
            numTour++;
            this.numTourCourant++;
        }
        afficherAllCombiSecretes();
    }

    /**
     * Permet à un joueur donné de faire une tentative pour deviner la combinaison secrète.
     * Affiche le plateau du joueur, le numéro de la tentative, puis demande au joueur de jouer.
     * Une fois que le joueur a fait sa tentative, affiche les résultats de la tentative.
     *
     * @param joueur Le joueur qui fait la tentative.
     * @param numPartie Le numéro de la partie actuelle.
     * @param numTour Le numéro du tour actuel de la partie.
     */
    private void faireTentative(Joueur joueur, int numPartie, int numTour) {
        // Affiche le message indiquant que le joueur fait une tentative.
        System.out.println("\n" + joueur.getPseudo() + " faites une tentative:");
        
        // Affiche le plateau actuel du joueur.
        joueur.affichePlateau(this.config);
        
        // Affiche le numéro de la tentative.
        System.out.println("Tentative numéro: " + (numTour + 1));
        
        try {
            // Le joueur fait sa tentative et obtient un résultat.
            Resultat resultat = joueur.jouer(this.config.getNbPions(), this.config.getNbCouleurs(), this.listeJoueurs.indexOf(joueur), this, numPartie, numTour);
            
            // Obtient la tentative actuelle du joueur à partir de ses résultats précédents.
            Combinaison tentativeActuelle = joueur.getListeResultats().get(numTour).getTentative();
            
            // Ajoute la tentative actuelle du joueur à son plateau.
            joueur.getPlateau().add(tentativeActuelle);
            
            // Affiche les résultats de la tentative.
            afficherResultat(resultat, joueur);
        } catch (Exception e) {
            // En cas d'erreur, affiche la trace de la pile.
            e.printStackTrace();
        }
    }

    /**
     * Affiche le résultat d'une tentative de jeu.
     * Cette méthode présente le résultat de la tentative d'un joueur, indiquant le nombre de pions placés correctement,
     * le nombre de pions de la bonne couleur mais mal positionnés, et si l'aide est activée, les positions précises.
     * Si le joueur a trouvé la combinaison secrète, un message de félicitations est affiché.
     *
     * @param resultat Le résultat de la tentative du joueur à afficher.
     * @param joueur Le joueur concerné par les résultats.
     */
    private void afficherResultat(Resultat resultat, Joueur joueur) {
        // Affiche le nombre de pions correctement positionnés.
        System.out.println("\nNombre de pions justes: " + resultat.getNbBonnePos());
        // Affiche le nombre de pions de la bonne couleur mais mal positionnés.
        System.out.println("Nombre de pions de la bonne couleur à la mauvaise position: " + resultat.getNbBadPos());
        System.out.println();
        
        // Si l'aide est activée, affiche des informations détaillées sur la position des pions.
        if (this.config.getAide()) {
            System.out.println(resultat.getPositionResultStringFormat());
        }
        
        // Si le joueur trouve la combinaison secrète, affiche un message de félicitations.
        if (resultat.getNbBonnePos() == this.config.getNbPions()) {
            joueur.setATrouve(true);
            System.out.println("Bravo " + joueur.getPseudo() + ", vous avez trouvé !!!");
        }
    }
    
    /**
     * Affiche les combinaisons secrètes de tous les joueurs.
     * Cette méthode parcourt tous les joueurs participant au jeu et affiche leur combinaison secrète.
     * Elle est appelée à la fin d'une partie pour révéler les combinaisons que chaque joueur
     * tentait de deviner, offrant une transparence et un débriefing après le jeu.
     */
    private void afficherAllCombiSecretes() {
        // Itération sur chaque joueur de la liste pour afficher sa combinaison secrète.
        for (Joueur joueur : this.listeJoueurs) {
            // Affiche le pseudo du joueur et sa combinaison secrète.
            System.out.println(joueur.getPseudo() + ", votre combinaison secrète était: " + joueur.getCombiSecrete().toString());
        }
    }

    
    /**
     * Réinitialise tous les joueurs pour une nouvelle partie.
     * Cette méthode parcourt tous les joueurs enregistrés dans le jeu, réinitialisant leurs statistiques,
     * vidant leur plateau et leur liste de résultats, et générant de nouvelles combinaisons secrètes
     * en fonction des paramètres actuels de la configuration du jeu.
     * Elle est utilisée à la fin d'une partie pour s'assurer que tous les joueurs repartent sur une base égale et propre.
     */
    private void resetForNewGame() {
        for (Joueur joueur : this.listeJoueurs) {
            joueur.setNbGuess(0);  // Réinitialise le nombre de tentatives à 0 pour chaque joueur.
            joueur.setATrouve(false);  // Réinitialise l'état de découverte de la combinaison à faux.
            joueur.viderListeResultats();  // Vide la liste des résultats pour effacer les tentatives précédentes.
            joueur.viderPlateau();  // Vide le plateau pour effacer toutes les tentatives antérieures.
            joueur.newCombiSecrete(this.config.getNbPions(), this.config.getNbCouleurs());  // Génère une nouvelle combinaison secrète pour le joueur.
        }
    }

    /**
     * Indique si la partie est jouable ou non selon qu'il reste des joueurs ou non qui n'ont
     * pas encore trouvé leur combinaison secrète.
     * 
     * @return Le booléen indiquant si la partie est jouable ou non.
     */
    private boolean estJouable() {
        return (!this.listeJoueurs.isEmpty());
    }

    /**
     * Sauvegarde l'état actuel du jeu, incluant la liste des joueurs, la configuration du jeu, 
     * et les numéros de la partie et du tour courants, dans un fichier spécifié par l'utilisateur.
     * 
     * Cette méthode sollicite l'utilisateur pour le chemin du fichier de sauvegarde, puis tente d'écrire
     * les données du jeu dans ce fichier. Si la sauvegarde réussit, elle retourne true, sinon false en cas d'erreur.
     *
     * @param numPartie Le numéro de la partie en cours, qui sera sauvegardé pour une reprise précise du jeu.
     * @param numTour Le numéro du tour en cours lors de la sauvegarde.
     * @return true si la sauvegarde a réussi, false en cas d'échec.
     */
    public boolean save(int numPartie, int numTour) {
        Path filePath = inputFilePath();  // Demande à l'utilisateur le chemin du fichier de sauvegarde.
        ensureDirectoryExists(filePath.getParent());  // Assure que le dossier de sauvegarde existe.

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath.toFile()))) {
            oos.writeObject(this.listeJoueurs); // Sauvegarde la liste des joueurs.
            oos.writeObject(this.config);       // Sauvegarde la configuration du jeu.
            oos.writeInt(numPartie);            // Sauvegarde le numéro de la partie.
            oos.writeInt(numTour);              // Sauvegarde le numéro du tour.
            oos.flush();
            return true;  // Retourne vrai si tout s'est bien passé.
        } catch (IOException e) {
            e.printStackTrace();  // Affiche les détails de l'erreur si quelque chose ne va pas.
            return false;  // Retourne faux si une erreur survient.
        }
    }

    /**
     * Assure que le dossier donné existe, et le crée s'il n'existe pas.
     *
     * @param directoryPath Le chemin du dossier à vérifier ou à créer.
     */
    private void ensureDirectoryExists(Path directoryPath) {
        if (!Files.exists(directoryPath)) {
            try {
                Files.createDirectories(directoryPath);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Erreur lors de la création du dossier de sauvegarde: " + directoryPath);
            }
        }
    }

    /**
     * Demande à l'utilisateur de saisir un nom de fichier pour la sauvegarde et construit le chemin du fichier correspondant.
     * Cette méthode interagit directement avec l'utilisateur pour obtenir un nom de fichier pour la sauvegarde
     * de la partie en cours. Elle préfixe le chemin avec le dossier "save/" et ajoute l'extension ".dat"
     * pour former le chemin complet du fichier de sauvegarde.
     *
     * @return Le chemin du fichier de sauvegarde sous forme d'objet {@link Path}.
     */
    private Path inputFilePath() {
        // Affiche une invite à l'utilisateur pour saisir le nom du fichier de sauvegarde.
        System.out.println("Choisissez un nom pour la partie à sauvegarder:");
        // Lit la saisie de l'utilisateur et supprime les espaces superflus.
        String filename = System.console().readLine().trim();
        // Construit le chemin du fichier en préfixant avec "save/" et en ajoutant l'extension ".dat".
        Path filePath = Paths.get("save/" + filename + ".dat");
        // Retourne le chemin du fichier construit.
        return filePath;
    }

    /**
     * Charge l'état d'un jeu depuis un fichier spécifié.
     * Cette méthode tente de lire et de désérialiser un objet {@link Jeu} à partir du fichier donné.
     * Elle lit successivement la liste des joueurs, la configuration du jeu, et les numéros de la partie
     * et du tour courants. En cas de succès, elle retourne une instance de {@link Jeu} prête à être utilisée.
     * Si une erreur survient lors de la lecture ou de la désérialisation, elle retourne {@code null}.
     *
     * @param filePath Le chemin vers le fichier contenant l'état du jeu sauvegardé.
     * @return Une instance de {@link Jeu} si le chargement réussit, sinon {@code null}.
     */
    @SuppressWarnings("unchecked")
    public static Jeu load(Path filePath) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath.toFile()))) {
            Jeu jeu = new Jeu();  // Création d'une nouvelle instance de Jeu
            jeu.listeJoueurs = (List<Joueur>) ois.readObject(); // Lire la liste des joueurs
            jeu.config = (Configuration) ois.readObject();      // Lire la configuration
            jeu.numPartieCourante = ois.readInt();                  // Lire le numéro de partie
            jeu.numTourCourant = ois.readInt();                    // Lire le numéro de tour
            return jeu;  // Retourner l'instance de Jeu chargée
        } catch (Exception e) {
            e.printStackTrace();
            return null;  // Retourner null en cas d'échec
        }
    }

    /**
     * Affiche le menu principal du jeu et permet à l'utilisateur de choisir parmi plusieurs options.
     * Ce menu offre trois choix : démarrer une nouvelle partie, continuer une partie sauvegardée, ou quitter le jeu.
     * L'utilisateur est invité à faire un choix, et le système réagit en conséquence en appelant la méthode appropriée.
     * Ce processus se répète jusqu'à ce qu'une saisie valide soit reçue.
     */
    public static void gameMenu() {
        // Affichage des options du menu principal.
        System.out.println("\n===============MENU===============");
        System.out.println("[1]Nouvelle partie");
        System.out.println("[2]Continuer une partie sauvegardée");
        System.out.println("[3]Quitter");

        // Boucle jusqu'à ce qu'une saisie valide soit faite par l'utilisateur.
        boolean saisieValide = false;
        while (!saisieValide) {
            // Appel à la méthode choisirOptionMenu pour traiter la saisie de l'utilisateur.
            saisieValide = choisirOptionMenu();
        }
    }

/**
 * Traite la saisie de l'utilisateur pour choisir une option dans le menu principal.
 * Cette méthode lit l'entrée de l'utilisateur, valide son choix, et déclenche l'action appropriée.
 * Les options incluent démarrer une nouvelle partie, continuer une partie sauvegardée, ou quitter le jeu.
 * Si le choix est valide et entraîne une action, la méthode retourne true pour indiquer que la saisie a été traitée avec succès.
 * Sinon, elle affiche un message d'erreur et retourne false pour permettre une nouvelle saisie.
 *
 * @return true si une action a été déclenchée avec succès (nouvelle partie ou reprise d'une partie), sinon false.
 */
public static boolean choisirOptionMenu() {
    boolean saisieValide = false;
    System.out.println("\nEntrez votre choix (1,2,3): ");
    String input = System.console().readLine().trim();
    try {
        int choix = Integer.parseInt(input);

        switch (choix) {
            case 1:
                startNewGame(); // Démarre une nouvelle partie.
                saisieValide = true;
                break;
            case 2:
                resumeSavedGame(); // Continue une partie précédemment sauvegardée.
                saisieValide = true;
                break;
            case 3:
                System.out.println("Bye-bye !"); // Quitte le jeu.
                System.exit(0);
                break;
            default:
                System.out.println("Choix invalide."); // Gère le cas d'un choix hors des options proposées.
                break;
        }
    } catch (NumberFormatException e) {
        System.out.println("Saisie invalide."); // Gère le cas où l'entrée n'est pas un nombre.
    } 
    return saisieValide; // Retourne l'état de la validation de la saisie.
}

    /**
     * Démarre une nouvelle partie du jeu Master Mind.
     * Cette méthode commence par afficher et configurer les paramètres du jeu en utilisant la classe {@link Configuration}.
     * Après la configuration, elle crée une nouvelle instance de {@link Jeu} et démarre le jeu.
     * 
     * Les étapes incluent la configuration des paramètres de jeu comme le nombre de pions, de couleurs, etc.,
     * et la transition vers le jeu actif où les joueurs commencent à interagir avec le jeu.
     */
    private static void startNewGame() {
        System.out.println("\nParamètres:");
        // Crée une nouvelle configuration en interagissant avec l'utilisateur pour définir les paramètres du jeu.
        Configuration config = Configuration.createConfig();
        
        System.out.println("\n\nC'est parti !");
        // Crée une nouvelle instance du jeu avec la configuration définie.
        Jeu nouveauJeu = new Jeu(config);
        
        // Démarre le jeu, permettant aux joueurs de commencer à jouer en fonction des règles établies.
        nouveauJeu.jouerJeu();
    }

    /**
     * Permet à l'utilisateur de reprendre une partie précédemment sauvegardée.
     * Cette méthode commence par lister tous les fichiers de sauvegarde disponibles. Si aucun fichier de sauvegarde n'est trouvé,
     * un message est affiché et la méthode se termine. Si des fichiers de sauvegarde sont disponibles, l'utilisateur est invité
     * à choisir un fichier parmi ceux listés. La partie correspondant au fichier choisi est ensuite chargée et lancée.
     * 
     * @see #listSavedGames() pour la méthode qui liste les jeux sauvegardés.
     * @see #choisirSauvegarde(File[]) pour la méthode qui permet à l'utilisateur de choisir parmi les sauvegardes disponibles.
     * @see #loadAndStartGame(File) pour la méthode qui charge et démarre le jeu sauvegardé.
     */
    private static void resumeSavedGame() {
        // Récupère la liste des jeux sauvegardés disponibles.
        File[] savedGames = listSavedGames();
        
        // Vérifie si des sauvegardes sont disponibles.
        if (savedGames == null || savedGames.length == 0) {
            System.out.println("Aucun jeu sauvegardé.");
            gameMenu();
        }
        
        // Permet à l'utilisateur de choisir une sauvegarde à reprendre.
        int fileIndex = choisirSauvegarde(savedGames);
        
        // Si un fichier valide est choisi, charge et démarre le jeu.
        if (fileIndex != -1) {
            loadAndStartGame(savedGames[fileIndex]);
        }
    }

    /**
     * Liste les fichiers de sauvegarde disponibles.
     * Cette méthode accède au dossier "save" et retourne tous les fichiers qui se terminent par l'extension ".dat",
     * correspondant aux jeux sauvegardés. Elle utilise un filtre pour s'assurer que seuls les fichiers correspondant
     * à des sauvegardes de jeu sont listés.
     *
     * @return Un tableau de fichiers {@link File} représentant les jeux sauvegardés, ou null si aucun fichier n'est trouvé.
     */
    private static File[] listSavedGames() {
        // Créer un objet File pour le répertoire contenant les sauvegardes.
        File dir = new File("save");
        
        // Filtrer et retourner les fichiers qui ont l'extension ".dat".
        return dir.listFiles((d, name) -> name.endsWith(".dat"));
    }


    /**
     * Permet à l'utilisateur de choisir une sauvegarde parmi les fichiers de jeu enregistrés disponibles.
     * Cette méthode affiche la liste des fichiers de sauvegarde et invite l'utilisateur à choisir une sauvegarde
     * à charger en entrant le numéro correspondant. La validation de l'entrée assure que le choix est dans la plage valide.
     * Si l'entrée est invalide, un message d'erreur est affiché et la méthode retourne -1.
     *
     * @param savedGames Un tableau de fichiers {@link File} contenant les jeux sauvegardés.
     * @return L'index du fichier sélectionné dans le tableau savedGames, ou -1 si la sélection est invalide.
     */
    private static int choisirSauvegarde(File[] savedGames) {
        System.out.println("Sauvegardes disponibles:");
        // Affiche la liste des fichiers de sauvegarde avec un index pour l'utilisateur.
        for (int i = 0; i < savedGames.length; i++) {
            System.out.println((i + 1) + ": " + savedGames[i].getName());
        }
        
        System.out.println("Choisissez la partie à charger (entrer un chiffre):");
        // Lit l'entrée utilisateur pour la sélection de la sauvegarde.
        String input = System.console().readLine().trim();
        try {
            // Convertit l'entrée en un index de tableau.
            int choix = Integer.parseInt(input) - 1;
            if (choix >= 0 && choix < savedGames.length) {
                return choix;  // Retourne l'index valide.
            } else {
                System.out.println("Sélection invalide.");  // Message pour une sélection hors des limites.
            }
        } catch (NumberFormatException e) {
            System.out.println("Saisie invalide. Entrez un chiffre.");  // Gestion d'entrée non numérique.
        }
        return -1;  // Retourne -1 si l'entrée est invalide.
    }

    /**
     * Charge un jeu sauvegardé à partir d'un fichier spécifié et lance le jeu si le chargement est réussi.
     * Cette méthode utilise {@link Jeu#load(Path)} pour tenter de charger l'état d'un jeu depuis un fichier.
     * Si le chargement est réussi, le jeu est démarré; sinon, un message d'échec est affiché.
     *
     * @param gameFile Le fichier contenant les données du jeu sauvegardé à charger.
     */
    private static void loadAndStartGame(File gameFile) {
        // Tente de charger le jeu depuis le chemin du fichier fourni.
        Jeu jeuCharge = Jeu.load(gameFile.toPath());
        
        // Vérifie si le chargement a réussi.
        if (jeuCharge != null) {
            System.out.println("Jeu chargé avec succès.");
            // Démarre le jeu chargé.
            jeuCharge.jouerJeu();
        } else {
            // Informe l'utilisateur en cas d'échec du chargement.
            System.out.println("Chargement echoué.");
        }
    }

    /**
     * Gère le déroulement complet du jeu, en jouant le nombre de parties spécifié dans la configuration.
     * Ce processus comprend l'initiation de chaque partie, l'affichage des scores après chaque partie,
     * et la réinitialisation des conditions de jeu pour une nouvelle partie jusqu'à ce que toutes les parties soient jouées.
     * Après avoir complété toutes les parties, affiche les scores totaux de tous les joueurs et retourne au menu principal du jeu.
     */
    public void jouerJeu() {
        // Boucle sur le nombre total de parties défini dans la configuration.
        while (this.numPartieCourante < this.config.getNbParties()) {
            System.out.println("\nPartie n° " + (this.numPartieCourante + 1));
            // Lance une partie.
            this.lancerPartie(this.numPartieCourante, this.numTourCourant);

            System.out.println("\nScores actuels:");
            // Calcule et affiche les scores actuels après chaque partie.
            for (Joueur joueur : this.listeJoueurs) {
                joueur.setNbPoints(joueur.getNbPoints() + (this.config.getNbGuessMax() - joueur.getNbGuess()));
                System.out.println(joueur.getPseudo() + ": " + joueur.getNbPoints());
            }
            // Préparation pour la prochaine partie.
            this.numPartieCourante++;
            this.numTourCourant = 0;
            resetForNewGame();
        }
        
        // Affiche les scores totaux après toutes les parties.
        System.out.println("\n===============Scores totaux===============\n");
        for (Joueur joueur : this.listeJoueurs) {
            System.out.println(joueur.getPseudo() + ": " + joueur.getNbPoints() + "\n");
        }

        // Retour au menu principal.
        gameMenu();
    }

    /**
     * Getter du numéro de la partie courante.
     * @return Le numéro de la partie courante du jeu.
     */
    public int getNumPartieCourante() {
        return this.numPartieCourante;
    }

    /**
     * Getter du numéro du tour courant.
     * @return Le numéro du tour courant
     */
    public int getNumTourCourant() {
        return this.numTourCourant;
    }

    /**
     * Programme principale affichant le menu du jeu du Master Mind. 
     * Le menu permet de lancer une nouvelle partie, de charger et continuer une partie sauvegarder ou de quitter le jeu.
     */
    public static void main(String[] args) {
        gameMenu();
    }

}

