/**
 * Classe représentant une commande de pizza
 */
public class Order {
    /**
     * Identifiant de la commande
     */
    private int id;
    /**
     * Prix de la commande
     */
    private float price;
    /**
     * Date de la commande
     */
    private String date;
    /**
     * Identifiant du livreur
     */
    private int delivererId;
    /**
     * Identifiant du client
     */
    private int clientId;

    /**
     * Constructeur de la classe Order
     * @param id identifiant de la commande
     * @param price prix de la commande
     * @param date date de la commande
     * @param delivererId identifiant du livreur
     * @param clientId identifiant du client
     */
    public Order(int id, float price, String date, int delivererId, int clientId) {
        this.id = id;
        this.price = price;
        this.date = date;
        this.delivererId = delivererId;
        this.clientId = clientId;
    }

    /**
     * Getter de l'identifiant de la commande
     * @return l'identifiant de la commande
     */
    public int getId() {
        return id;
    }

    /**
     * Getter du prix de la commande
     * @return le prix de la commande
     */
    public float getPrice() {
        return price;
    }

    /**
     * Getter de la date de la commande
     * @return la date de la commande
     */
    public String getDate() {
        return date;
    }

    /**
     * Getter de l'identifiant du livreur
     * @return l'identifiant du livreur
     */
    public int getDelivererId() {
        return delivererId;
    }

    /**
     * Getter de l'identifiant du client
     * @return l'identifiant du client
     */
    public int getClientId() {
        return clientId;
    }

    /**
     * Méthode toString de la classe Order
     * Redéfinition de la méthode toString de la classe Object pour afficher les informations de la commande
     * @return une chaîne de caractères représentant la commande
     */
    @Override
    public String toString() {
        return "Order{\n" +
                "   id=" + id +
                ", \n" + "  price=" + price +
                ", \n" + "  date='" + date + '\'' +
                ", \n" + "  delivererId=" + delivererId +
                ", \n" + "  clientId=" + clientId +
                ", \n" + "}";
    }
}
