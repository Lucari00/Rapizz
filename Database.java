import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Classe qui gère l'accès à la base de données de l'application Rapizz.
 */
public class Database {
    /**
     * URL de connexion à la base de données.
     */
    private final String urlString = "jdbc:mysql://localhost:3306/Rapizz";
    /**
     * Nom d'utilisateur pour la connexion à la base de données.
     */
    private String usernameString;
    /**
     * Mot de passe pour la connexion à la base de données.
     */
    private String passwordString;
    /**
     * Objet Connection pour la connexion à la base de données.
     */
    private Connection connection;
    /**
     * Client actuellement connecté à l'application.
     */
    private Client client;

    /**
     * Constructeur par défaut de la classe Database.
     * Charge les informations de connexion à la base de données depuis un fichier de configuration et initialise la connexion.
     */
    public Database() {
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
        createTables();
    }

    /**
     * Setter du client actuellement connecté à l'application.
     * @param client un objet Client représentant le client actuellement connecté
     */
    public void setClient(Client client) {
        this.client = client;
    }

    /**
     * Getter de la connexion à la base de données.
     * @return l'ojet Connection représentant la connexion à la base de données
     */
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

    /**
     * Procédure qui crée les tables de la base de données en exécutant le script createTables.sql.
     * Utilise la classe ScriptRunner pour exécuter le script.
     */
    private void createTables() {
        ScriptRunner runner = new ScriptRunner(connection, true, true);
        try {
            runner.runScript(new BufferedReader(new FileReader("SQLScripts/createTables.sql")));
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Procédure qui supprime les tables de la base de données en exécutant le script dropTables.sql.
     * Utilise la classe ScriptRunner pour exécuter le script.
     */
    private void deleteTables() {
        ScriptRunner runner = new ScriptRunner(connection, true, true);
        try {
            runner.runScript(new BufferedReader(new FileReader("SQLScripts/dropTables.sql")));
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Procédure qui réinitialise la base de données en supprimant les tables et en les recréant.
     */
    public void resetDatabase() {
        deleteTables();
        createTables();
        System.out.println("Database reset");
    }

    /**
     * Fonction qui récupère la liste des pizzas disponibles dans la base de données.
     * @return une liste d'objets Pizza représentant les pizzas disponibles
     */
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

    /**
     * Fonction qui récupère la liste des tailles de pizza disponibles dans la base de données.
     * @return une liste d'objets Taille représentant les tailles de pizza disponibles
     */
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

    /**
     * Fonction qui récupère la liste des livreurs affectés à aucune commande dans la base de données.
     * @return une liste d'objets Livreur représentant les livreurs libres
     */
    private List<Livreur> getFreeLivreur() {
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

    /**
     * Procédure qui rafraîchit le solde du client passé en paramètre par une requête à la base de données.
     * @param client un objet Client dont le solde doit être rafraîchi
     */
    public void refreshClientSolde(Client client) {
        try {
            String query = "SELECT solde FROM Clients WHERE idClient = ?";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, client.getIdClient());
            ResultSet rset = pstmt.executeQuery();
            if (rset.next()) {
                client.setSolde(rset.getFloat("solde"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Procédure qui met à jour le solde du client actuellement connecté dans la base de données.
     * @param newSolde le réel du nouveau solde du client
     */
    public void updateClientSolde(float newSolde) {
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

    /**
     * Fonction qui récupère le nombre de commandes passées par un client donné.
     * @param client un objet Client dont on veut connaître le nombre de commandes
     * @return le nombre de commandes passées par le client
     */
    public int getNombreCommandesByClient(Client client) {
        int nombreCommandes = 0;
        try {
            // compte le nombre de commande du client dans la table commandes
            String query = "SELECT COUNT(*) FROM Commandes WHERE idClient = ?";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, client.getIdClient());
            ResultSet rset = pstmt.executeQuery();
            if (rset.next()) {
                nombreCommandes = rset.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nombreCommandes;
    }

    /**
     * Fonction qui ajoute une commande à la base de données.
     * Gère si un livreur est disponible, si le client a les fonds nécessaires et si le client a droit à une pizza gratuite.
     * Appelle la modification du solde du client si la commande est ajoutée avec succès.
     * @param selectedPizzaString le nom de la pizza sélectionnée
     * @param selectedSizeString le nom de la taille sélectionnée
     * @param price le prix de la commande
     * @return un message indiquant le succès ou l'échec de l'ajout de la commande
     */
    public String addOrder(String selectedPizzaString, String selectedSizeString, float price) {
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
                return "noLivreur";
            }

            System.out.println(client.getSolde());
            if (client.getSolde() < selectedPizza.getPrice()) {
                System.out.println("Fonds insuffisants.");
                return "noFunds";
            }

            // Vérifier si le client a droit à une pizza gratuite tous les 10 commandes
            int nombreCommandes = getNombreCommandesByClient(client);
            if (nombreCommandes % 10 == 0 && nombreCommandes != 0) {
                price = 0;
            }

            String insertOrderQuery = "INSERT INTO Commandes (prixCommande, idLivreur, idClient) VALUES (?, ?, ?)";
            PreparedStatement pstmt = connection.prepareStatement(insertOrderQuery);
            pstmt.setFloat(1, price);
            pstmt.setInt(2, livreurs.get(0).getIdLivreur());
            pstmt.setInt(3, client.getIdClient());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {

                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT MAX(idCommande) FROM Commandes");
                int idCommande = -1;
                if (resultSet.next()) {
                    idCommande = resultSet.getInt(1);
                }

                addPizzaCommande(Integer.parseInt(selectedPizza.getId()), selectedSize.getNomTaille(), idCommande);

                updateClientSolde(client.getSolde() - price);

                System.out.println("Commande créée avec succès.");
                if (price == 0) {
                    return "free";
                } else {
                    return "success";
                }
            } else {
                System.out.println("Erreur lors de la création de la commande.");
                return "error";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "error";
        }
    }

    /**
     * Fonction qui récupère la liste des pizzas les plus et les moins commandées dans la base de données.
     * @return le texte à afficher pour les pizzas les plus et les moins commandées
     */
    public List<String> getMostAndLeastOrderedPizzas() {
        List<String> result = new ArrayList<>();
        String mostOrderedQuery = "SELECT p.nomPizza, COUNT(pc.idPizza) AS order_count " +
                          "FROM Pizzas p " +
                          "LEFT JOIN PizzaCommande pc ON p.idPizza = pc.idPizza " +
                          "GROUP BY p.nomPizza " +
                          "ORDER BY order_count DESC " +
                          "LIMIT 1";

        String leastOrderedQuery = "SELECT p.nomPizza, COUNT(pc.idPizza) AS order_count " +
                          "FROM Pizzas p " +
                          "LEFT JOIN PizzaCommande pc ON p.idPizza = pc.idPizza " +
                          "GROUP BY p.nomPizza " +
                          "ORDER BY order_count ASC " +
                          "LIMIT 1";

        try (PreparedStatement mostStmt = connection.prepareStatement(mostOrderedQuery);
             PreparedStatement leastStmt = connection.prepareStatement(leastOrderedQuery)) {

            ResultSet mostResultSet = mostStmt.executeQuery();
            if (mostResultSet.next()) {
                result.add("La pizza la plus demandée : " + mostResultSet.getString("nomPizza"));
            }

            ResultSet leastResultSet = leastStmt.executeQuery();
            if (leastResultSet.next()) {
                result.add("La pizza la moins demandée : " + leastResultSet.getString("nomPizza"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * Fonction qui récupère la liste des ingrédients de la pizza passée en paramètre.
     * @param pizza un objet Pizza dont on veut connaître les ingrédients
     * @return la liste des ingrédients de la pizza
     */
    public List<String> getIngredients(Pizza pizza) {
        List<String> ingredients = new ArrayList<>();
        String query = "SELECT i.nomIngredient " +
                       "FROM Ingredients i " +
                       "JOIN IngredientPizza ip ON i.idIngredient = ip.idIngredient " +
                       "WHERE ip.idPizza = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, pizza.getId());
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                ingredients.add(resultSet.getString("nomIngredient"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ingredients;
    }

    /**
     * Fonction qui marque une commande comme livrée dans la base de données.
     * Utilise la procédure stockée update_commande_livree.
     * @param idCommande l'identifiant de la commande à marquer comme livrée
     * @return true si la commande a été marquée comme livrée avec succès, false sinon
     */
    public boolean markAsDelivered(int idCommande) {
        try {
            String updateCommandeLivree = "CALL update_commande_livree(?)";
            PreparedStatement pstmt = connection.prepareStatement(updateCommandeLivree);
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

    /**
     * Fonction qui récupère la liste des clients de la base de données.
     * @return une liste d'objets Client représentant les clients de la base de données
     */
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

    /**
     * Fonction qui crée un client dans la base de données.
     * @param nomClient le nom du client
     * @param prenomClient le prénom du client
     * @param adresseClient l'adresse du client
     * @param solde le solde du client
     * @return un objet Client représentant le client créé
     */
    public Client createClient(String nomClient, String prenomClient, String adresseClient, float solde) {
        int idClient = -1;
        try {
            String sql = "INSERT INTO Clients (nomClient, prenomClient, adresseClient, solde) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, nomClient);
            pstmt.setString(2, prenomClient);
            pstmt.setString(3, adresseClient);
            pstmt.setFloat(4, solde);

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

    /**
     * Procédure qui lie une pizza à une commande et à une taille dans la table PizzaCommande.
     * @param idPizza l'identifiant de la pizza
     * @param nomTaille le nom de la taille de la pizza
     * @param idCommande l'identifiant de la commande
     */
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

    /**
     * Procédure qui ajoute un livreur à la base de données.
     * Ajoute un véhicule pour le livreur.
     * @param nomLivreur le nom du livreur
     * @param prenomLivreur le prénom du livreur
     */
    public void addLivreur(String nomLivreur, String prenomLivreur) {
        try {
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
                ResultSet generatedKeys = pstmtVehicle.getGeneratedKeys();
                if (generatedKeys.next()) {
                    vehicleId = generatedKeys.getInt(1);
                }
            } else {
                System.out.println("Erreur lors de l'ajout du Véhicule.");
            }

            String sql = "INSERT INTO Livreurs (nomLivreur, prenomLivreur, idVehicule) VALUES (?, ?, ?)";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, nomLivreur);
            pstmt.setString(2, prenomLivreur);
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

    /**
     * Procédure qui insère les données dans la base de données en exécutant le script insertTables.sql.
     * Utilise la classe ScriptRunner pour exécuter le script.
     */
    public void insertData() {
        ScriptRunner runner = new ScriptRunner(connection, true, true);
        try {
            runner.runScript(new BufferedReader(new FileReader("SQLScripts/insertTables.sql")));
            System.out.println("Data inserted successfully.");
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Getter du client actuellement connecté à l'application.
     * @return un objet Client représentant le client actuellement connecté
     */
    public Client getClient() {
        return this.client;
    }

    /**
     * Fonction qui récupère la liste des commandes non livrées dans la base de données.
     * @return une liste d'objets Order représentant les commandes non livrées
     */
    public List<Order> getOrders() {
        List<Order> orders = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Commandes WHERE dateLivree IS NULL");
            while (resultSet.next()) {
                int id = resultSet.getInt("idCommande");
                float price = resultSet.getFloat("prixCommande");
                Timestamp date = resultSet.getTimestamp("dateCommande");
                int delivererId = resultSet.getInt("idLivreur");
                int clientId = resultSet.getInt("idClient");

                orders.add(new Order(id, price, date, null, delivererId, clientId));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    /**
     * Fonction qui récupère le nom de la pizza d'une commande passée en paramètre.
     * @param order un objet Order dont on veut connaître le nom de la pizza
     * @return le nom de la pizza de la commande
     */
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

    /**
     * Fonction qui récupère la taille de la pizza d'une commande passée en paramètre.
     * @param order un objet Order dont on veut connaître la taille de la pizza
     * @return le nom de la taille de la pizza de la commande
     */
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

    /**
     * Fonction qui récupère la liste des livreurs de la base de données.
     * @return une liste d'objets Livreur représentant les livreurs de la pizzeria
     */
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

    /**
     * Fonction qui récupère la commande associée à un livreur passé en paramètre.
     * @param Livreur un objet Livreur dont on veut connaître la commande associée
     * @return un objet Order représentant la commande associée au livreur, null si le livreur n'a pas de commande
     */
    public Order getOrderByLivreur(Livreur Livreur) {
        Order order = null;
        try {
            PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM Commandes WHERE idLivreur = ? AND dateLivree IS NULL");
            pstmt.setInt(1, Livreur.getIdLivreur());
            ResultSet rset = pstmt.executeQuery();
            if (rset.next()) {
                order = new Order(rset.getInt("idCommande"), rset.getFloat("prixCommande"), rset.getTimestamp("dateCommande"), null, rset.getInt("idLivreur"), rset.getInt("idClient"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return order;
    }

    /**
     * Fonction qui récupère l'adresse d'un client passé en paramètre.
     * @param clientId l'identifiant du client dont on veut connaître l'adresse
     * @return une chaîne de caractères représentant l'adresse du client
     */
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

    /**
     * Fonction qui récupère la liste des 5 clients ayant dépensé le plus dans la pizzeria.
     * @return la liste des affichages des 5 clients ayant dépensé le plus
     */
    public List<String> getTopClients() {
        List<String> topClients = new ArrayList<>();
        String query = "SELECT c.nomClient, c.prenomClient, SUM(co.prixCommande) AS montantTotal " +
                       "FROM Clients c " +
                       "JOIN Commandes co ON c.idClient = co.idClient " +
                       "GROUP BY c.idClient, c.nomClient, c.prenomClient " +
                       "ORDER BY montantTotal DESC " +
                       "LIMIT 5";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                String nom = rs.getString("nomClient");
                String prenom = rs.getString("prenomClient");
                float montantTotal = rs.getFloat("montantTotal");
                topClients.add(nom + " " + prenom + ": " + montantTotal + " €");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return topClients;
    }

    /**
     * Fonction qui récupère l'ingrédient le plus populaire parmi les clients de la pizzeria.
     * @return le nom de l'ingrédient le plus commandé
     */
    public String getFavoriteIngredient() {
        String favoriteIngredient = "";
        String query = "SELECT i.nomIngredient, COUNT(ip.idIngredient) AS ingredient_count " +
                       "FROM Ingredients i " +
                       "JOIN IngredientPizza ip ON i.idIngredient = ip.idIngredient " +
                       "GROUP BY i.idIngredient " +
                       "ORDER BY ingredient_count DESC " +
                       "LIMIT 1";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                favoriteIngredient = rs.getString("nomIngredient");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return favoriteIngredient;
    }

    /**
     * Fonction qui récupère le chiffre d'affaires par jour de la pizzeria.
     * @return une map associant les dates aux chiffres d'affaires correspondants
     */
    public Map<String, Float> getRevenueByDay() {
        String query = "SELECT DATE(dateCommande) AS date, SUM(prixCommande) AS revenue FROM Commandes GROUP BY DATE(dateCommande)";
        Map<String, Float> dailyRevenues = new HashMap<>();

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String date = rs.getString("date");
                float revenue = rs.getFloat("revenue");
                dailyRevenues.put(date, revenue);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return dailyRevenues;
    }

    /**
     * Fonction qui récupère le livreur ayant le plus de retards dans la livraison des commandes.
     * @return le message à afficher pour le livreur ayant le plus de retards avec son véhicule et le nombre de retards
     */
    public String getWorstDeliveryPerson() {
        String query = 
            "SELECT L.nomLivreur, L.prenomLivreur, V.Marque, V.nomVehicule, COUNT(*) AS retardCount " +
            "FROM Commandes C " +
            "JOIN Livreurs L ON C.idLivreur = L.idLivreur " +
            "JOIN Vehicules V ON L.idVehicule = V.idVehicule " +
            "WHERE TIMESTAMPDIFF(MINUTE, c.dateCommande, c.dateLivree) > 30 " + // retard
            "GROUP BY L.nomLivreur, L.prenomLivreur, V.Marque, V.nomVehicule " +
            "ORDER BY retardCount DESC " +
            "LIMIT 1";

        StringBuilder message = new StringBuilder();

        try (Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query)) {

            if (rs.next()) {
                message.append("Le pire livreur est : ").append(rs.getString("nomLivreur")).append(" ").append(rs.getString("prenomLivreur")).append("\n");
                message.append("Véhicule : ").append(rs.getString("Marque")).append(" ").append(rs.getString("nomVehicule")).append("\n");
                message.append("Nombre de retards : ").append(rs.getString("retardCount"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return message.toString();
    }

    /**
     * Fonction qui récupère la liste des véhicules non utilisés par les livreurs.
     * @return l'affichage des véhicules non utilisés avec leur marque et leur nom
     */
    public List<String> getUnusedVehicles() {
        String query = 
            "SELECT V.Marque, V.nomVehicule " +
            "FROM Vehicules V " +
            "LEFT JOIN Livreurs L ON V.idVehicule = L.idVehicule " +
            "LEFT JOIN Commandes C ON L.idLivreur = C.idLivreur " +
            "WHERE C.idCommande IS NULL";

        List<String> unusedVehicles = new ArrayList<>();

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String vehicleInfo = rs.getString("Marque") + " " + rs.getString("nomVehicule");
                unusedVehicles.add(vehicleInfo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return unusedVehicles;
    }

    /**
     * Fonction qui récupère le nombre de commandes passées par chaque client.
     * @return une map associant les noms + prénoms des clients au nombre de commandes passées
     */
    public Map<String, Integer> getNumberOfOrdersPerClient() {
        String query = 
            "SELECT C.nomClient, C.prenomClient, COUNT(*) AS orderCount " +
            "FROM Commandes Co " +
            "JOIN Clients C ON Co.idClient = C.idClient " +
            "GROUP BY C.nomClient, C.prenomClient";
    
        Map<String, Integer> ordersPerClient = new HashMap<>();
    
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
    
            while (rs.next()) {
                String clientName = rs.getString("nomClient") + " " + rs.getString("prenomClient");
                int orderCount = rs.getInt("orderCount");
                ordersPerClient.put(clientName, orderCount);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return ordersPerClient;
    }

    /**
     * Fonction qui récupère le prix de la commande moyenne.
     * @return le réel du prix de la commande moyenne
     */
    public double getAverageOrder() {
        String query = 
            "SELECT AVG(prixCommande) AS averageOrder " +
            "FROM Commandes";
    
        double averageOrders = 0;
    
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
    
            if (rs.next()) {
                averageOrders = rs.getDouble("averageOrder");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return averageOrders;
    }

    public List<Order> getDeliveredOrders() {
        List<Order> deliveredOrders = new ArrayList<>();
        String query = "SELECT * FROM Commandes WHERE dateLivree IS NOT NULL";

        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                int idCommande = rs.getInt("idCommande");
                BigDecimal prixCommande = rs.getBigDecimal("prixCommande");
                Timestamp dateCommande = rs.getTimestamp("dateCommande");
                Timestamp dateLivree = rs.getTimestamp("dateLivree");
                int idLivreur = rs.getInt("idLivreur");
                int idClient = rs.getInt("idClient");

                Order order = new Order(idCommande, prixCommande.floatValue(), dateCommande, dateLivree, idLivreur, idClient);
                deliveredOrders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return deliveredOrders;
    }
    
    /**
     * Fonction qui récupère la liste des noms des clients ayant passé plus de commandes que la moyenne.
     * @return la liste des noms + prénoms des clients ayant passé plus de commandes que la moyenne
     */
    public List<String> getClientsOverAverage() {
        String query = 
            "SELECT C.nomClient, C.prenomClient " +
            "FROM Clients C " +
            "LEFT JOIN Commandes Co ON C.idClient = Co.idClient " +
            "GROUP BY C.nomClient, C.prenomClient " +
            "HAVING COUNT(Co.idCommande) > (SELECT AVG(commandCount) FROM " +
            "(SELECT COUNT(idCommande) AS commandCount FROM Clients C" +
            "LEFT JOIN Commandes Co ON C.idClient = Co.idClient GROUP BY C.idClient) AS T)";
        
            List<String> clientsOverAverage = new ArrayList<>();
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
        
            while (rs.next()) {
                String clientName = rs.getString("nomClient") + " " + rs.getString("prenomClient");
                clientsOverAverage.add(clientName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return clientsOverAverage;
    }
}