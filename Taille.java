public class Taille {
    private String nomTaille;
    private float prixMultiplicatif;

    public Taille(String nomTaille, float prixMultiplicatif) {
        this.nomTaille = nomTaille;
        this.prixMultiplicatif = prixMultiplicatif;
    }

    public String getNomTaille() {
        return nomTaille;
    }

    public void setNomTaille(String nomTaille) {
        this.nomTaille = nomTaille;
    }

    public float getPrixMultiplicatif() {
        return prixMultiplicatif;
    }

    public void setPrixMultiplicatif(float prixMultiplicatif) {
        this.prixMultiplicatif = prixMultiplicatif;
    }

    @Override
    public String toString() {
        return this.nomTaille;
    }
}
