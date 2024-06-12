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
    private int delivererId;
    /**
     * Identifiant du client
     */
    private int clientId;
    /**
     * Date de la commande
     */
    private Timestamp dateOrdered;
    /**
     * Date de livraison de la commande
     */
    private Timestamp dateDelivered;

    /**
     * Constructeur de la classe Order
     * @param idCommande l'identifiant de la commande
     * @param prixCommande le prix de la commande
     * @param dateCommande la date de la commande
     * @param dateLivree la date de livraison de la commande
     * @param idLivreur l'identifiant du livreur
     * @param idClient l'identifiant du client
     */
    public Order(int idCommande, float prixCommande, Timestamp dateCommande, Timestamp dateLivree, int idLivreur, int idClient) {
        this.id = idCommande;
        this.price = prixCommande;
        this.dateOrdered = dateCommande;
        this.dateDelivered = dateLivree;
        this.delivererId = idLivreur;
        this.clientId = idClient;
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
     * @return l'affichage de la commande'
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
