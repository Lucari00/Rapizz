/**
 * Classe représentant une commande de pizza
 */
import java.sql.Timestamp;
import java.time.temporal.ChronoUnit;

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
    private Timestamp dateOrdered;
    private Timestamp dateDelivered;

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
     * Fonction pour l'affichage d'une commande en cours de livraison
     * @return l'affichage de la commande
     */
    public String toStringNotDelivered() {
        return "Order{\n" +
                "   id=" + id +
                ", \n" + "  price=" + price +
                ", \n" + "  date='" + dateOrdered.toString() + '\'' +
                ", \n" + "  delivererId=" + delivererId +
                ", \n" + "  clientId=" + clientId +
                ", \n" + "}";
    }

    /**
     * Fonction pour l'affichage d'une commande livrée
     * @return l'affichage de la commande
     */
    public String toStringDelivered() {
        long minutesBetween = ChronoUnit.MINUTES.between(dateOrdered.toLocalDateTime(), dateDelivered.toLocalDateTime());
        boolean estEnRetard = minutesBetween > 30;
        return "Order{\n" +
                "   id=" + id +
                ", \n" + "  price=" + price +
                ", \n" + "  dateOrdered=" + dateOrdered.toString() +
                ", \n" + "  dateDelivered=" + dateDelivered.toString() +
                ", \n" + "  delivererId=" + delivererId +
                ", \n" + "  clientId=" + clientId +
                ", \n" + "  estEnRetard=" + estEnRetard +
                ", \n" + "}";
    }
}
