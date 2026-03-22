import java.util.Arrays;

/**
 * Fournit des méthodes utilitaires pour manipuler et interagir avec des tableaux en Java.
 * Cette classe est conçue pour être utilisée comme une boîte à outils pour les opérations courantes sur les tableaux,
 * simplifiant ainsi des tâches telles que la recherche, la modification, le tri et d'autres manipulations de tableaux.
 * 
 * Toutes les méthodes de cette classe sont statiques, ce qui permet de les appeler sans créer d'instance de la classe.
 */
public class UtilsArray {

    /**
     * Crée un tableau contenant un élément répété plusieurs fois.
     * 
     * @param element l'élément à répéter dans le tableau.
     * @param count le nombre de fois que l'élément doit être répété.
     * @return un tableau contenant l'élément répété le nombre de fois spécifié.
     */
    public static Character[] repeatCharElement(Character element, int count) {

      Character[] array = new Character[count];
      
      // Remplit le tableau avec l'élément spécifié.
      Arrays.fill(array, element);
      
      // Retourne le tableau rempli.
      return array;
    }
}
