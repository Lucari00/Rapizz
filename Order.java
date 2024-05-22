public class Order {
    private int id;
    private float price;
    private String date;
    private boolean isFree;
    private int delivererId;
    private int clientId;

    public Order(int id, float price, String date, boolean isFree, int delivererId, int clientId) {
        this.id = id;
        this.price = price;
        this.date = date;
        this.isFree = isFree;
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

    public boolean isFree() {
        return isFree;
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
                ", \n" + "  isFree=" + isFree +
                ", \n" + "  delivererId=" + delivererId +
                ", \n" + "  clientId=" + clientId +
                ", \n" + "}";
    }
}
