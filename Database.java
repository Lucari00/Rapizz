import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
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
import java.util.Properties;

public class Database {
    private final String urlString = "jdbc:mysql://localhost:3306/Rapizz";
    private String usernameString;
    private String passwordString;
    private Connection connection;
    private Client client;

    public Database(String nomClient, String prenomClient, String adresseClient, float solde) {
        Properties props = new Properties();
        File configFile = new File("config.properties");

        if (configFile.exists()) {
            try (FileInputStream input = new FileInputStream(configFile)) {
                props.load(input);
                System.out.println("Configuration loaded from config.properties");
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        } else {
            props.setProperty("db.user", "root");
            props.setProperty("db.password", "root");
        }

        usernameString = props.getProperty("db.user");
        passwordString = props.getProperty("db.password");

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
        System.out.println("Database reset");
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

    public List<Taille> getTailles() {
        List<Taille> tailles = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Tailles");
            while (resultSet.next()) {
                Taille taille = new Taille(resultSet.getString("nomTaille"), resultSet.getFloat("prixMultiplicatif"));
                tailles.add(taille);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tailles;
    }

    private List<Livreur> getFreeLivreur() {
        // Requête SQL pour trouver les livreurs libres
        String query = "SELECT idLivreur, nomLivreur, prenomLivreur " +
                        "FROM Livreurs " +
                        "WHERE idLivreur NOT IN (SELECT idLivreur FROM Commandes WHERE dateLivree IS NULL)";

        
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

    private void updateClientSolde(float newSolde) {
        try {
            newSolde = Math.round(newSolde * 100) / 100.0f;
            String updateClientQuery = "UPDATE Clients SET solde = ? WHERE idClient = ?";
            PreparedStatement pstmt = connection.prepareStatement(updateClientQuery);
            pstmt.setFloat(1, newSolde);
            pstmt.setInt(2, client.getIdClient());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Client débité avec succès.");
                client.setSolde(newSolde);
            } else {
                System.out.println("Erreur lors du débit du client.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean addOrder(String selectedPizzaString, String selectedSizeString, float price) {
        try {
            List<Pizza> pizzaList = getPizzas();
            Pizza selectedPizza = null;
            for (Pizza pizza : pizzaList) {
                if (pizza.getName().equals(selectedPizzaString)) {
                    selectedPizza = pizza;
                    break;
                }
            }

            List<Taille> tailleList = getTailles();
            Taille selectedSize = null;
            for (Taille taille : tailleList) {
                if (taille.getNomTaille().equals(selectedSizeString)) {
                    selectedSize = taille;
                    break;
                }
            }

            List<Livreur> livreurs = getFreeLivreur();

            if (livreurs.isEmpty()) {
                System.out.println("Aucun livreur disponible.");
                return false;
            }

            if (client.getSolde() < selectedPizza.getPrice()) {
                System.out.println("Fonds insuffisants.");
                return false;
            }

            String insertOrderQuery = "INSERT INTO Commandes (prixCommande, idLivreur, idClient) VALUES (?, ?, ?)";
            PreparedStatement pstmt = connection.prepareStatement(insertOrderQuery);
            pstmt.setFloat(1, selectedPizza.getPrice());
            pstmt.setInt(2, livreurs.get(0).getIdLivreur());
            pstmt.setInt(3, client.getIdClient());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {

                // get id from last inserted order
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT MAX(idCommande) FROM Commandes");
                int idCommande = -1;
                if (resultSet.next()) {
                    idCommande = resultSet.getInt(1);
                }

                // insert into PizzaCommande
                addPizzaCommande(Integer.parseInt(selectedPizza.getId()), selectedSize.getNomTaille(), idCommande);

                updateClientSolde(client.getSolde() - selectedPizza.getPrice());

                System.out.println("Commande créée avec succès.");
                return true;
            } else {
                System.out.println("Erreur lors de la création de la commande.");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean markAsDelivered(int idCommande) {
        try {
            String updateOrderQuery = "UPDATE Commandes SET dateLivree = NOW(), tempsLivraison = TIMESTAMPDIFF(MINUTE, dateCommande, NOW()) WHERE idCommande = ?";
            PreparedStatement pstmt = connection.prepareStatement(updateOrderQuery);
            pstmt.setInt(1, idCommande);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Commande marquée comme livrée avec succès.");
                return true;
            } else {
                System.out.println("Erreur lors du marquage de la commande comme livrée.");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
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

    public void addPizzaCommande(int idPizza, String nomTaille, int idCommande) {
        try {
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO PizzaCommande (idPizza, nomTaille, idCommande) VALUES (?, ?, ?)");
            stmt.setInt(1, idPizza);
            stmt.setString(2, nomTaille);
            stmt.setInt(3, idCommande);
            int ex = stmt.executeUpdate();
            
            if (ex > 0) {
                System.out.println("Pizza ajoutée avec succès dans PizzaCommande.");
            } else {
                System.out.println("Erreur lors de l'ajout de la pizza dans PizzaCommande.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void addLivreur(String string, String string2) {
        try {
            // il faut ajouter un véhicule pour le livreur
            String sqlVehicle = "INSERT INTO Vehicules (Marque, nomVehicule, immatriculation, typeVehicule) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmtVehicle = connection.prepareStatement(sqlVehicle, Statement.RETURN_GENERATED_KEYS);
            pstmtVehicle.setString(1, "Renault");
            pstmtVehicle.setString(2, "Clio");
            pstmtVehicle.setString(3, "1234ABCD");
            pstmtVehicle.setString(4, "Voiture");

            int vehicleId = -1;
            int affectedRowsVehicle = pstmtVehicle.executeUpdate();
            if (affectedRowsVehicle > 0) {
                System.out.println("Véhicule ajouté avec succès.");
                // il faut que je récupère l'id du véhicule ajouté
                ResultSet generatedKeys = pstmtVehicle.getGeneratedKeys();
                if (generatedKeys.next()) {
                    vehicleId = generatedKeys.getInt(1);
                }
            } else {
                System.out.println("Erreur lors de l'ajout du Véhicule.");
            }

            String sql = "INSERT INTO Livreurs (nomLivreur, prenomLivreur, idVehicule) VALUES (?, ?, ?)";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, string);
            pstmt.setString(2, string2);
            pstmt.setInt(3, vehicleId);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Livreur ajouté avec succès.");
            } else {
                System.out.println("Erreur lors de l'ajout du livreur.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertData() {
        // execute the SQL script insertTables.sql
        ScriptRunner runner = new ScriptRunner(connection, true, true);
        try {
            runner.runScript(new BufferedReader(new FileReader("SQLScripts/insertTables.sql")));
            System.out.println("Data inserted successfully.");
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    public Client getClient() {
        return this.client;
    }

    public List<Order> getOrders() {
        List<Order> orders = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Commandes WHERE dateLivree IS NULL");
            while (resultSet.next()) {
                int id = resultSet.getInt("idCommande");
                float price = resultSet.getFloat("prixCommande");
                String date = resultSet.getString("dateCommande");
                boolean isFree = resultSet.getBoolean("estGratuit");
                int delivererId = resultSet.getInt("idLivreur");
                int clientId = resultSet.getInt("idClient");

                orders.add(new Order(id, price, date, isFree, delivererId, clientId));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    public String getPizzaNameFromOrder(Order order) {
        String pizzaName = "";
        try {
            PreparedStatement pstmt = connection.prepareStatement("SELECT nomPizza FROM Pizzas WHERE idPizza = (SELECT idPizza FROM PizzaCommande WHERE idCommande = ?)");
            pstmt.setInt(1, order.getId());
            ResultSet rset = pstmt.executeQuery();
            if (rset.next()) {
                pizzaName = rset.getString("nomPizza");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pizzaName;
    }

    public String getTailleFromOrder(Order order) {
        String taille = "";
        try {
            PreparedStatement pstmt = connection.prepareStatement("SELECT nomTaille FROM PizzaCommande WHERE idCommande = ?");
            pstmt.setInt(1, order.getId());
            ResultSet rset = pstmt.executeQuery();
            if (rset.next()) {
                taille = rset.getString("nomTaille");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return taille;
    }

    public List<Livreur> getLivreurs() {
        List<Livreur> livreurs = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Livreurs");
            while (resultSet.next()) {
                Livreur livreur = new Livreur(resultSet.getInt("idLivreur"), resultSet.getString("nomLivreur"), resultSet.getString("prenomLivreur"));
                livreurs.add(livreur);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return livreurs;
    }

    public Order getOrderByLivreur(Livreur Livreur) {
        Order order = null;
        try {
            PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM Commandes WHERE idLivreur = ? AND dateLivree IS NULL");
            pstmt.setInt(1, Livreur.getIdLivreur());
            ResultSet rset = pstmt.executeQuery();
            if (rset.next()) {
                order = new Order(rset.getInt("idCommande"), rset.getFloat("prixCommande"), rset.getString("dateCommande"), rset.getBoolean("estGratuit"), rset.getInt("idLivreur"), rset.getInt("idClient"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return order;
    }

    public String getAdressById(int clientId) {
        String adresse = "";
        try {
            PreparedStatement pstmt = connection.prepareStatement("SELECT adresseClient FROM Clients WHERE idClient = ?");
            pstmt.setInt(1, clientId);
            ResultSet rset = pstmt.executeQuery();
            if (rset.next()) {
                adresse = rset.getString("adresseClient");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return adresse;
    }
}
