import java.math.BigDecimal;
import java.sql.Timestamp;

public class Order {
    private int id;
    private float price;
    private String date;
    private boolean isFree;
    private int delivererId;
    private int clientId;
    private Timestamp dateOrdered;
    private Timestamp dateDelivered;

    // public Order(int id, float price, String date, int delivererId, int clientId) {
    //     this.id = id;
    //     this.price = price;
    //     this.date = date;
    //     this.delivererId = delivererId;
    //     this.clientId = clientId;
    // }

    public Order(int idCommande, float prixCommande, Timestamp dateCommande, Timestamp dateLivree, int idLivreur, int idClient) {
        this.id = idCommande;
        this.price = prixCommande;
        this.dateOrdered = dateCommande;
        this.dateDelivered = dateLivree;
        this.delivererId = idLivreur;
        this.clientId = idClient;
    }

    public int getId() {
        return id;
    }

    public float getPrice() {
        return price;
    }

    public String getDate() {
        return date;
    }

    public int getDelivererId() {
        return delivererId;
    }

    public int getClientId() {
        return clientId;
    }

    public String toStringNotDelivered() {
        return "Order{\n" +
                "   id=" + id +
                ", \n" + "  price=" + price +
                ", \n" + "  date='" + date + '\'' +
                ", \n" + "  delivererId=" + delivererId +
                ", \n" + "  clientId=" + clientId +
                ", \n" + "}";
    }

    public String toStringDelivered() {
        return "Order{\n" +
                "   id=" + id +
                ", \n" + "  price=" + price +
                ", \n" + "  dateOrdered=" + dateOrdered.toString() +
                ", \n" + "  dateDelivered=" + dateDelivered.toString() +
                ", \n" + "  delivererId=" + delivererId +
                ", \n" + "  clientId=" + clientId +
                ", \n" + "}";
    }
}
