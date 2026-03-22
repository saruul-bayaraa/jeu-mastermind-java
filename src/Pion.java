import java.io.Serializable;

/**
 * Cette classe représente un pion utilisé dans un jeu.
 * Elle stocke la couleur du pion.
 * 
 * @serial Cette classe est sérialisable pour permettre la sauvegarde et la récupération des états de jeu.
 */
public class Pion implements Serializable {
    private static final long serialVersionUID = 5L;
    
    /**
     * Représente la couleur d'un pion.
     */
    private Couleur couleur;
    
    /**
     * Enumération des couleurs possibles pour un pion.
     */
    public enum Couleur {
        JAUNE,   
        BLEU,    
        ROUGE,   
        VERT,    
        BLANC,   
        VIOLET,  
        ROSE,    
        ORANGE,
        VIDE;  


        /**
         * Obtient la couleur correspondant au poids donné.
         * @param poids Le poids de la couleur recherchée.
         * @return La couleur correspondante.
         */
        public static Couleur getCouleur(int poids) {
            return Couleur.values()[poids];
        }

    }

    /**
     * Retourne le code de couleur ANSI correspondant à la couleur du pion.
     * Les couleurs ANSI sont utilisées pour colorer le texte dans les terminaux qui supportent les séquences ANSI.
     *
     * @param pion Le pion dont la couleur est utilisée pour déterminer le code ANSI.
     * @return Le code ANSI sous forme de chaîne de caractères qui correspond à la couleur du pion.
     *         Si la couleur du pion ne correspond à aucune des couleurs prédéfinies, retourne le code ANSI pour la couleur par défaut (blanc).
     */
    public static String getANSIColor(Pion pion) {
        String ANSI;
        switch( pion.getCouleur() ) {
            case JAUNE:
                ANSI = "\u001B[33m";
                break;
            case BLEU:
                ANSI = "\u001B[34m";
                break;
            case ROUGE:
                ANSI = "\u001B[31m";
                break;
            case VERT:
                ANSI = "\u001B[32m";
                break;
            case BLANC:
                ANSI = "\u001B[37m";
                break;
            case VIOLET:
                ANSI = "\u001B[35m";
                break;
            case ROSE:
                ANSI = "\u001B[95m";
                break;
            case ORANGE:
                ANSI = "\u001B[38;5;208m";
                break;
            default:
                ANSI = "\u001B[0m"; // Couleur par défaut
                break;
        }
        return ANSI;
    }

    /**
     * Constructeur d'un pion avec un poids donné.
     * @param poids Le poids (numéro) du pion à créer.
     */
    public Pion(int poids) {
        this.couleur = Couleur.getCouleur(poids);
    }

    /**
     * Retourne la représentation en chaîne de caractères de cet objet Pion.
     * Cette représentation est la couleur du pion sous forme de texte, ce qui facilite l'affichage
     * et le débogage des instances de Pion.
     *
     * @return Une chaîne de caractères représentant la couleur du pion.
     */
    @Override
    public String toString() {
        return String.format("%s", this.couleur);
    }

    /**
     * Récupère la couleur actuelle de ce pion.
     * Cette méthode permet d'accéder à la propriété couleur de l'instance de Pion,
     * facilitant ainsi l'interrogation de l'état de l'objet Pion sans modifier ses attributs.
     *
     * @return La couleur de ce pion sous forme d'une énumération de type Couleur.
     */
    public Couleur getCouleur() {
        return this.couleur;
    }

    /**
     * Compare ce pion avec un autre objet pour vérifier l'égalité.
     * L'égalité est basée uniquement sur la couleur des pions. Cette méthode est utilisée pour
     * déterminer si deux objets Pion sont considérés comme équivalents, ce qui est essentiel
     * pour la gestion correcte des pions dans des collections et lors de comparaisons.
     *
     * @param o L'objet avec lequel comparer ce pion.
     * @return true si l'objet fourni est un pion ayant la même couleur que ce pion, false sinon.
     */
    @Override
    public boolean equals(Object o) {
        if (o == this) { 
            return true; 
        }
        if (o == null) { 
            return false; 
        }
        if (!(o instanceof Pion)) { 
            return false; 
        } 
        Pion object = (Pion) o;        
        return this.couleur==object.couleur;
    }

}
