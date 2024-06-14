/**
 * Classe des livreurs de la pizzeria
 */
class Livreur {
    /**
     * Identifiant du livreur
     */
    private int idLivreur;
    /**
     * Nom du livreur
     */
    private String lastNameLivreur;
    /**
     * Prénom du livreur
     */
    private String nameLivreur;

    /**
     * Constructeur de la classe Livreur
     * @param idLivreur l'identifiant du livreur
     * @param nomLivreur le nom du livreur
     * @param prenomLivreur le prénom du livreur
     */
    public Livreur(int idLivreur, String nomLivreur, String prenomLivreur) {
        this.idLivreur = idLivreur;
        this.lastNameLivreur = nomLivreur;
        this.nameLivreur = prenomLivreur;
    }

    /**
     * Getter de l'identifiant du livreur
     * @return l'identifiant du livreur
     */
    public int getIdLivreur() {
        return idLivreur;
    }

    /**
     * Getter du nom du livreur
     * @return le nom du livreur
     */
    public String getLastNameLivreur() {
        return lastNameLivreur;
    }

    /**
     * Getter du prénom du livreur
     * @return le prénom du livreur
     */
    public String getNameLivreur() {
        return nameLivreur;
    }

    /** Méthode toString de la classe Livreur
     * Redéfinition de la méthode toString de la classe Object pour afficher le nom et le prénom du livreur
     * @return une chaîne de caractères représentant le nom et le prénom du livreur.
     */
    @Override
    public String toString() {
        return this.lastNameLivreur + " " + this.nameLivreur;
    }
}
