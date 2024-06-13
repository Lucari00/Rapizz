/**
 * Classe représentant un client de la pizzeria
 */
public class Client {
    /**
     * Identifiant du client
     */
    private int idClient;
    /**
     * Nom du client
     */
    private String nomClient;
    /**
     * Prénom du client
     */
    private String prenomClient;
    /**
     * Adresse du client
     */
    private String adresseClient;
    /**
     * Solde du client
     */
    private float solde;

    /**
     * Constructeur de la classe Client
     * @param idClient : identifiant du client
     * @param nomClient
     * @param prenomClient
     * @param adresseClient
     * @param solde
     */
    public Client(int idClient, String nomClient, String prenomClient, String adresseClient, float solde) {
        this.idClient = idClient;
        this.nomClient = nomClient;
        this.prenomClient = prenomClient;
        this.adresseClient = adresseClient;
        this.solde = solde;
    }

    /**
     * Getter de l'identifiant du client
     * @return l'identifiant du client
     */
    public int getIdClient() {
        return idClient;
    }

    /**
     * Setter de l'identifiant du client
     * @param idClient le nouvel identifiant du client
     */
    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }

    /**
     * Getter du nom du client
     * @return le nom du client
     */
    public String getNomClient() {
        return nomClient;
    }

    /**
     * Setter du nom du client
     * @param nomClient le nouveau nom du client
     */
    public void setNomClient(String nomClient) {
        this.nomClient = nomClient;
    }

    /**
     * Getter du prénom du client
     * @return le prénom du client
     */
    public String getPrenomClient() {
        return prenomClient;
    }

    /**
     * Setter du prénom du client
     * @param prenomClient le nouveau prénom du client
     */
    public void setPrenomClient(String prenomClient) {
        this.prenomClient = prenomClient;
    }

    /**
     * Getter de l'adresse du client
     * @return l'adresse du client
     */
    public String getAdresseClient() {
        return adresseClient;
    }

    /**
     * Setter de l'adresse du client
     * @param adresseClient la nouvelle adresse du client
     */
    public void setAdresseClient(String adresseClient) {
        this.adresseClient = adresseClient;
    }

    /**
     * Getter du solde du client
     * @return le solde du client
     */
    public float getSolde() {
        return solde;
    }

    /**
     * Setter du solde du client
     * @param solde le nouveau solde du client
     */
    public void setSolde(float solde) {
        this.solde = solde;
    }

    /**
     * Méthode toString de la classe Client
     * Redéfinition de la méthode toString de la classe Object pour afficher les informations du client
     * @return une chaîne de caractères représentant le client
     */
    @Override
    public String toString() {
        return this.nomClient + " " + this.prenomClient;
    }
}
