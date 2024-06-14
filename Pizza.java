/**
 * Classe représentant une pizza de la base de données.
 */
public class Pizza {
    /**
     * Identifiant de la pizza.
     */
    private int id;
    /**
     * Nom de la pizza.
     */
    private String name;
    /**
     * Prix de la pizza.
     */
    private float price;

    /**
     * Getter de l'identifiant de la pizza.
     * @return L'identifiant de la pizza.
     */
    public int getId() {
        return id;
    }

    /**
     * Setter de l'identifiant de la pizza.
     * @param id L'identifiant de la pizza.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Getter du nom de la pizza.
     * @return Le nom de la pizza.
     */
    public String getName() {
        return name;
    }

    /**
     * Setter du nom de la pizza.
     * @param name Le nom de la pizza.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter du prix de la pizza.
     * @return Le prix de la pizza.
     */
    public float getPrice() {
        return price;
    }

    /**
     * Setter du prix de la pizza.
     * @param price Le prix de la pizza.
     */
    public void setPrice(float price) {
        this.price = price;
    }

    /**
     * Constructeur de la classe Pizza.
     * @param id Identifiant de la pizza.
     * @param name Nom de la pizza.
     * @param price Prix de la pizza.
     */
    public Pizza(int id, String name, float price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    /**
     * Méthode toString de la classe Pizza
     * Redéfinition de la méthode toString de la classe Object pour afficher le nom de la pizza
     * @return une chaîne de caractères représentant le nom de la pizza.
     */
    @Override
    public String toString() {
        return this.name;
    }
}
