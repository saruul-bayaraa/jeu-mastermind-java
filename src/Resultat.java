import java.io.Serializable;


/**
 * Représente le résultat d'une tentative dans le jeu du Master Mind.
 * Cette classe stocke le nombre de pions correctement positionnés, le nombre de pions de la bonne couleur mais mal positionnés,
 * et un tableau des résultats pour chaque pion dans la tentative indiquant s'ils sont bien placés ('V'), 
 * mal placés ('P'), ou complètement faux ('F').
 *
 * @serial Cette classe est sérialisable pour permettre la sauvegarde et la récupération des états de jeu.
 */
public class Resultat implements Serializable{
    private static final long serialVersionUID = 4L;

    /**
     * Nombre de pions qui sont correctement positionnés dans la tentative.
     */
    private int nbBonnePos;

    /**
     * Nombre de pions qui sont de la bonne couleur mais mal positionnés dans la tentative.
     */
    private int nbBadPos;

    /**
     * Tableau de caractères indiquant le résultat pour chaque pion dans la tentative.
     * 'V' pour un pion correctement placé, 'P' pour un pion de la bonne couleur mais mal placé, 
     * et 'F' pour un pion incorrect.
     */
    private Character positionResult[];

    /**
     * La tentative de combinaison évaluée qui a mené à ce résultat.
     */
    private Combinaison tentative;


    /**
     * Construit un nouvel objet Resultat avec les détails spécifiés de la tentative et ses évaluations.
     *
     * @param nbBonnePos Le nombre de pions correctement positionnés.
     * @param nbBadPos Le nombre de pions de la bonne couleur mais mal positionnés.
     * @param positionResult Un tableau de caractères représentant les résultats de chaque pion de la tentative.
     * @param tentative La combinaison tentée qui a été évaluée.
     */
    public Resultat(int nbBonnePos, int nbBadPos, Character positionResult[], Combinaison tentative){
        this.nbBonnePos = nbBonnePos;
        this.nbBadPos = nbBadPos;
        this.positionResult = positionResult;
        this.tentative = tentative;
    }

    /**
     * Getter du nombre de pions correctement positionnés dans la tentative.
     * Ce nombre représente le nombre de pions qui sont exactement à la place correcte.
     *
     * @return Le nombre de pions correctement positionnés.
     */
    public int getNbBonnePos() {
        return this.nbBonnePos;
    }

    /**
     * Getter du nombre de pions qui sont de la bonne couleur mais mal positionnés dans la tentative.
     * Ces pions sont corrects en terme de couleur mais se trouvent à la mauvaise position.
     *
     * @return Le nombre de pions de la bonne couleur mais mal positionnés.
     */
    public int getNbBadPos() {
        return this.nbBadPos;
    }

    /**
     * Getter: tableau de caractères indiquant le résultat pour chaque pion de la tentative.
     * Chaque caractère dans le tableau peut être 'V' (correct et bien placé), 'P' (correct en couleur mais mal placé),
     * ou 'F' (faux, ni correctement placé ni de la bonne couleur).
     *
     * @return Le tableau de caractères représentant les résultats de chaque pion.
     */
    public Character[] getPositionResult() {
        return this.positionResult;
    }

    /**
     * Getter de la combinaison de pions tentée qui a été évaluée pour produire ce résultat.
     * Cette combinaison représente l'ensemble des pions choisis par le joueur lors de la tentative.
     *
     * @return La combinaison tentée.
     */
    public Combinaison getTentative() {
        return this.tentative;
    }

    /**
     * Convertit le tableau de résultats de position en une chaîne de caractères continue.
     * Cette méthode concatène tous les caractères du tableau 'positionResult' dans une seule chaîne,
     * facilitant ainsi l'affichage ou le traitement ultérieur des résultats sous forme de texte.
     *
     * @return Une chaîne de caractères représentant les résultats de chaque pion de la tentative,
     * où 'V' signifie un pion correctement placé, 'P' signifie un pion de bonne couleur mais mal placé,
     * et 'F' indique un pion incorrect.
     */
    public String getPositionResultStringFormat(){
        String positionResultFormatString = "";
        for(int i=0; i<this.positionResult.length; i++){
            // Ajoute chaque caractère du tableau 'positionResult' à la chaîne de résultat.
            positionResultFormatString += positionResult[i];
        }

        return positionResultFormatString;
    }

    /**
     * Fournit une représentation textuelle du résultat de la tentative, incluant le nombre de pions bien placés,
     * le nombre de pions mal placés et une chaîne représentant le résultat de chaque pion.
     * Cette méthode est utile pour afficher rapidement le résultat d'une tentative ou pour l'enregistrer dans des logs.
     *
     * @return Une chaîne de caractères formatée qui contient le nombre de pions bien positionnés,
     * le nombre de pions de la bonne couleur mais mal positionnés, et la représentation textuelle
     * du résultat de chaque pion dans la tentative.
     */
    @Override
    public String toString() {

        return String.format("nbBonnePos : %d\nnbBadPos : %d\npositionResult : %s", this.nbBonnePos, this.nbBadPos, getPositionResultStringFormat());
    }

}
