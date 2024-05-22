class Livreur {
    private int idLivreur;
    private String lastNameLivreur;
    private String nameLivreur;

    public Livreur(int idLivreur, String nomLivreur, String prenomLivreur) {
        this.idLivreur = idLivreur;
        this.lastNameLivreur = nomLivreur;
        this.nameLivreur = prenomLivreur;
    }

    public int getIdLivreur() {
        return idLivreur;
    }

    public String getLastNameLivreur() {
        return lastNameLivreur;
    }

    public String getNameLivreur() {
        return nameLivreur;
    }
}
