import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private final String urlString = "jdbc:mysql://localhost:3306/Rapizz";
    private final String usernameString = "root";
    private final String passwordString = "root";
    private Connection connection;
    private Client client;

    public Database(String nomClient, String prenomClient, String adresseClient, float solde) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        
        connection = getConnection();
        CreateTable();
        // this.client = CreateClient(nomClient, prenomClient, adresseClient, solde);
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Connection getConnection() {
        if (connection != null) {
            return connection;
        }
        try {
            connection = DriverManager.getConnection(urlString, usernameString, passwordString);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    private void CreateTable() {
        ScriptRunner runner = new ScriptRunner(connection, true, true);
        try {
            runner.runScript(new BufferedReader(new FileReader("SQLScripts/createTables.sql")));
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void DeleteDatabseTables() {
        ScriptRunner runner = new ScriptRunner(connection, true, true);
        try {
            runner.runScript(new BufferedReader(new FileReader("SQLScripts/dropTables.sql")));
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void resetDatabase() {
        DeleteDatabseTables();
        CreateTable();
    }

    public List<Pizza> getPizzas() {
        List<Pizza> pizzaList = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet rset = statement.executeQuery("SELECT * FROM pizzas");

            // Parcourir le résultat de la requête
            while (rset.next()) {
                String id = rset.getString("idPizza");
                String name = rset.getString("nomPizza");
                float price = rset.getFloat("prixPizza");

                // Créer un objet Pizza
                Pizza pizza = new Pizza(id, name, price);

                // Ajouter la pizza à la liste
                pizzaList.add(pizza);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pizzaList;
    }

    private List<Livreur> getFreeDelivery() {
        // Requête SQL pour trouver les livreurs libres
        String query = "SELECT idLivreur, nomLivreur, prenomLivreur " +
                        "FROM Livreurs " +
                        "WHERE idLivreur NOT IN (SELECT idLivreur FROM CommandesEnCours)";

        
        PreparedStatement pstmt;
        ResultSet rset = null;
        try {
            pstmt = connection.prepareStatement(query);
            rset = pstmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        

        // Créer une liste pour stocker les livreurs libres
        List<Livreur> livreursLibres = new ArrayList<>();

        // Parcourir le résultat de la requête
        try {
            while (rset.next()) {
                int idLivreur = rset.getInt("idLivreur");
                String nomLivreur = rset.getString("nomLivreur");
                String prenomLivreur = rset.getString("prenomLivreur");

                // Créer un objet Livreur et l'ajouter à la liste
                Livreur livreur = new Livreur(idLivreur, nomLivreur, prenomLivreur);
                livreursLibres.add(livreur);
                }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return livreursLibres;
    }

    public void addOrder(String selectedPizzaString) {
        try {

            List<Pizza> pizzaList = getPizzas();
            Pizza selectedPizza = null;
            for (Pizza pizza : pizzaList) {
                if (pizza.getName().equals(selectedPizzaString)) {
                    selectedPizza = pizza;
                    break;
                }
            }

            List<Livreur> livreurs = getFreeDelivery();

            String insertOrderQuery = "INSERT INTO CommandesEnCours (prixCommande, idLivreur, idClient) VALUES (?, ?, ?)";
            PreparedStatement pstmt = connection.prepareStatement(insertOrderQuery);
            pstmt.setFloat(1, selectedPizza.getPrice());
            pstmt.setInt(2, livreurs.get(0).getIdLivreur());
            pstmt.setInt(3, client.getIdClient());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Commande créée avec succès.");
            } else {
                System.out.println("Erreur lors de la création de la commande.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Client> getClients() {
        List<Client> clientList = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet rset = statement.executeQuery("SELECT * FROM Clients");

            // Parcourir le résultat de la requête
            while (rset.next()) {
                int id = rset.getInt("idClient");
                String nom = rset.getString("nomClient");
                String prenom = rset.getString("prenomClient");
                String adresse = rset.getString("adresseClient");
                float solde = rset.getFloat("solde");

                // Créer un objet Client
                Client client = new Client(id, nom, prenom, adresse, solde);

                // Ajouter le client à la liste
                clientList.add(client);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clientList;
    }

    public Client CreateClient(String nomClient, String prenomClient, String adresseClient, float solde) {
        int idClient = -1;
        try {
            String sql = "INSERT INTO Clients (nomClient, prenomClient, adresseClient, solde, nombreCommandes) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, nomClient);
            pstmt.setString(2, prenomClient);
            pstmt.setString(3, adresseClient);
            pstmt.setFloat(4, solde);
            pstmt.setInt(5, 0);

            // Exécuter la requête
            int affectedRows = pstmt.executeUpdate();
            ResultSet generatedKeys = null;

            // Vérifier si l'insertion a réussi et récupérer l'ID généré
            if (affectedRows > 0) {
                generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    idClient = generatedKeys.getInt(1);
                }
            } else {
                throw new SQLException("Creating client failed, no rows affected.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        Client client = new Client(idClient, nomClient, prenomClient, adresseClient, solde);
        return client;
    }
}
