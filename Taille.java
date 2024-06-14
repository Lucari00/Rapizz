/**
 * Classe représentant une taille de pizza.
 */
public class Taille {
    /**
     * Nom de la taille (petite, moyenne, grande)
     */
    private String nomTaille;
    /**
     * Facteur à multiplier au prix de base de la pizza
     */
    private float prixMultiplicatif;

    /**
     * Constructeur de la classe Taille.
     * @param nomTaille le nom de la taille
     * @param prixMultiplicatif le prix multiplicatif de la taille
     */
    public Taille(String nomTaille, float prixMultiplicatif) {
        this.nomTaille = nomTaille;
        this.prixMultiplicatif = prixMultiplicatif;
    }

    /**
     * Getter du nom de la taille.
     * @return le nom de la taille
     */
    public String getNomTaille() {
        return nomTaille;
    }

    /**
     * Setter du nom de la taille.
     * @param nomTaille le nom de la taille
     */
    public void setNomTaille(String nomTaille) {
        this.nomTaille = nomTaille;
    }

    /**
     * Getter du prix multiplicatif.
     * @return le prix multiplicatif
     */
    public float getPrixMultiplicatif() {
        return prixMultiplicatif;
    }

    /**
     * Setter du prix multiplicatif.
     * @param prixMultiplicatif le prix multiplicatif
     */
    public void setPrixMultiplicatif(float prixMultiplicatif) {
        this.prixMultiplicatif = prixMultiplicatif;
    }

    /**
     * Méthode toString de la classe Taille
     * Redéfinition de la méthode toString de la classe Object pour afficher le nom de la taille de pizza.
     * @return une chaîne de caractères représentant le nom de la taille de pizza
     */
    @Override
    public String toString() {
        return this.nomTaille;
    }
}
