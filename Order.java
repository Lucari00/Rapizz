public class Order {
    private int id;
    private float price;
    private String date;
    private int delivererId;
    private int clientId;

    public Order(int id, float price, String date, int delivererId, int clientId) {
        this.id = id;
        this.price = price;
        this.date = date;
        this.delivererId = delivererId;
        this.clientId = clientId;
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
