import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Classe de l'interface principale de la pizzeria qui contient les interfaces de gestion des commandes, des livreurs et du gérant
 */
public class MainPizzeriaUI extends JFrame {
    private PizzeriaManagerUI commandManagerUI;
    private PizzeriaUI pizzeriaUI;
    private DeliveryUI deliveryUI;

    /**
     * Constructeur de la classe MainPizzeriaUI
     * @param database la classe de gestion de la base de données
     */
    public MainPizzeriaUI(Database database) {
        // Configurer la fenêtre principale
        setTitle("Pizzeria - Interface Principale");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Utiliser un GridLayout pour disposer les interfaces dans une grille 2x2
        setLayout(new GridLayout(2, 2, 10, 10));

        // Créer les instances des interfaces
        commandManagerUI = new PizzeriaManagerUI(database);
        deliveryUI = new DeliveryUI(database, this, commandManagerUI);
        pizzeriaUI = new PizzeriaUI(database, this, commandManagerUI, deliveryUI);
        deliveryUI.setPizzeriaUI(pizzeriaUI);
        
        // Ajouter les interfaces à la grille
        add(createPanelWithComponent(pizzeriaUI.getContentPane(), "Interface Client"));
        add(createPanelWithComponent(commandManagerUI.getContentPane(), "Interface Gérant"));
        add(createPanelWithComponent(deliveryUI.getContentPane(), "Interface Livreur"));
        add(createButtonsPanel(database)); 

        // Rendre la fenêtre principale visible
        setVisible(true);
    }

    /**
     * Fonction pour créer un panneau avec un composant et un titre dans la grille
     * @param component le composant à ajouter
     * @param title le titre du panneau
     * @return le panneau avec le composant et le titre
     */
    private JPanel createPanelWithComponent(Container component, String title) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(component, BorderLayout.CENTER);
        panel.setBorder(BorderFactory.createTitledBorder(title));
        return panel;
    }

    /**
     * Fonction pour créer un panneau avec les boutons pour les actions sur la base de données
     * @param database la classe de gestion de la base de données
     * @return le panneau avec les boutons
     */
    private JPanel createButtonsPanel(Database database) {
        JPanel panel = new JPanel(new FlowLayout());

        // Bouton pour réinitialiser la base de données
        JButton button = new JButton("Reset Database");
        button.addActionListener(e -> {
            database.resetDatabase();
            pizzeriaUI.Refresh();
            commandManagerUI.refreshOrders();
            deliveryUI.refresh();
            commandManagerUI.refreshDeliveredOrders();
        });
        panel.add(button);
        
        // Bouton pour ajouter un livreur avec un nom aléatoire
        JButton buttonAddLivreur = new JButton("Add Livreur");
        buttonAddLivreur.addActionListener(e -> {
            List<String> lastNames = new ArrayList<>();
            lastNames.add("Palaysi");
            lastNames.add("Philippe");
            lastNames.add("Chraiteh");
            lastNames.add("Bouzidi");
            lastNames.add("Chirac");
            lastNames.add("Mitterand");
            lastNames.add("Dupont");
            lastNames.add("Durand");
            lastNames.add("Martin");
            lastNames.add("Bernard");

            List<String> firstNames = new ArrayList<>();
            firstNames.add("Luca");
            firstNames.add("Jean");
            firstNames.add("Pierre");
            firstNames.add("Paul");
            firstNames.add("Jacques");
            firstNames.add("Marie");
            firstNames.add("Sophie");
            firstNames.add("Lucie");
            firstNames.add("Julie");
            firstNames.add("Julien");
            firstNames.add("Thomas");
            firstNames.add("Alexandre");
            firstNames.add("Alexandra");
            firstNames.add("Alexis");

            database.addLivreur(firstNames.get((int) (Math.random() * firstNames.size())),
                    lastNames.get((int) (Math.random() * lastNames.size())));
            
            deliveryUI.refresh();
        });
        panel.add(buttonAddLivreur);

        // Bouton pour insérer les données de départ dans la base de données
        JButton buttonInsertData = new JButton("Insert Data");
        buttonInsertData.addActionListener(e -> {
            database.insertData();
            pizzeriaUI.Refresh();
            commandManagerUI.refreshOrders();
            deliveryUI.refresh();
            commandManagerUI.refreshDeliveredOrders();
        });
        panel.add(buttonInsertData);

        // Bouton pour afficher les meilleurs clients
        JButton buttonTopClients = new JButton("Meilleurs Clients");
        buttonTopClients.addActionListener(e -> {
            List<String> topClients = database.getTopClients();
            StringBuilder topClientsMessage = new StringBuilder("Les 5 meilleurs clients:\n");
            for (String client : topClients) {
                topClientsMessage.append(client).append("\n");
            }
            JOptionPane.showMessageDialog(this, topClientsMessage.toString(), "Meilleurs Clients", JOptionPane.INFORMATION_MESSAGE);
        });
        panel.add(buttonTopClients);

        // Bouton pour afficher les pizzas les plus/moins demandées
        JButton buttonPizzaDemand = new JButton("Pizza la plus/moins demandée");
        buttonPizzaDemand.addActionListener(e -> {
            List<String> pizzaDemand = database.getMostAndLeastOrderedPizzas();
            StringBuilder pizzaDemandMessage = new StringBuilder();
            for (String pizza : pizzaDemand) {
                pizzaDemandMessage.append(pizza).append("\n");
            }
            JOptionPane.showMessageDialog(this, pizzaDemandMessage.toString(), "Pizza la plus/moins demandée", JOptionPane.INFORMATION_MESSAGE);
        });
        panel.add(buttonPizzaDemand);

        // Bouton pour afficher l'ingrédient favori
        JButton buttonFavoriteIngredient = new JButton("Ingrédient Favori");
        buttonFavoriteIngredient.addActionListener(e -> {
            String favoriteIngredient = database.getFavoriteIngredient();
            String message = "L'ingrédient favori est : " + favoriteIngredient;
            JOptionPane.showMessageDialog(this, message, "Ingrédient Favori", JOptionPane.INFORMATION_MESSAGE);
        });
        panel.add(buttonFavoriteIngredient);

        // Bouton pour afficher l'évolution du chiffre d'affaires par jour
        JButton buttonRevenueEvolution = new JButton("Chiffre d'affaires");
        buttonRevenueEvolution.addActionListener(e -> {
            Map<String, Float> revenuesByDay = database.getRevenueByDay();
            
            StringBuilder message = new StringBuilder("Chiffre d'affaires par jour:\n");
            for (Map.Entry<String, Float> entry : revenuesByDay.entrySet()) {
                message.append(entry.getKey()).append(": ").append(entry.getValue()).append("€\n");
            }

            JOptionPane.showMessageDialog(this, message, "Chiffre d'affaires", JOptionPane.INFORMATION_MESSAGE);
        });
        panel.add(buttonRevenueEvolution);

        // Bouton pour afficher le livreur ayant le plus de retards
        JButton buttonWorstDelevery = new JButton("Pire Livreur");
        buttonWorstDelevery.addActionListener(e -> {
            String worstDelivery = database.getWorstDeliveryPerson();
            if (worstDelivery == "") {
                worstDelivery = "Aucun livreur n'a effectué de livraison en retard.";
            }
            JOptionPane.showMessageDialog(this, worstDelivery, "Pire Livreur", JOptionPane.INFORMATION_MESSAGE);
        });
        panel.add(buttonWorstDelevery);

        // Bouton pour afficher les véhicules non utilisés
        JButton buttonUnusedVehicles = new JButton("Véhicules non utilisés");
        buttonUnusedVehicles.addActionListener(e -> {
            List<String> unusedVehicles = database.getUnusedVehicles();
            StringBuilder message = new StringBuilder();
            if (unusedVehicles.size() != 0) {
                message.append("Les véhicules non utilisés :\n");
                for (String vehicle : unusedVehicles) {
                    message.append(" - "+vehicle+"\n");
                }
            } else {
               message.append("Tous les véhicules ont été utilisés.");
            }
            JOptionPane.showMessageDialog(this, message.toString(), "Véhicules non utilisés", JOptionPane.INFORMATION_MESSAGE);
        });
        panel.add(buttonUnusedVehicles);

        // Bouton pour afficher le nombre de commandes par client
        JButton buttonOrdersPerClient = new JButton("Nombre de commandes par client");
        buttonOrdersPerClient.addActionListener(e -> {
            Map<String, Integer> ordersPerClient = database.getNumberOfOrdersPerClient();
            StringBuilder message = new StringBuilder();
            if (!ordersPerClient.isEmpty()) {
                message.append("Nombre de commandes par client :\n");
                for (Map.Entry<String, Integer> entry : ordersPerClient.entrySet()) {
                    message.append(" - "+entry.getKey()+": "+entry.getValue()+"\n");
                }
            } else {
                message.append("Aucun client n'a passé de commande.");
            }
            JOptionPane.showMessageDialog(this, message.toString(), "Nombre de commandes par client", JOptionPane.INFORMATION_MESSAGE);
        });
        panel.add(buttonOrdersPerClient);

        // Bouton pour afficher le prix de la commande moyenne
        JButton buttonAverageCommandPrice = new JButton("Prix de la commande moyenne");
        buttonAverageCommandPrice.addActionListener(e -> {
            double averageNumberOfCommand = database.getAverageOrder();
            String message = "Le prix de la commande moyenne est de : " + averageNumberOfCommand;
            JOptionPane.showMessageDialog(this, message, "Prix de la commande moyenne", JOptionPane.INFORMATION_MESSAGE);
        });
        panel.add(buttonAverageCommandPrice);

        // Bouton pour afficher les clients qui ont commandé plus que la moyenne
        JButton buttonClientsOverAverage = new JButton("Clients au dessus de la moyenne des commandes");
        buttonClientsOverAverage.addActionListener(e -> {
            List<String> clientsAboveAverage = database.getClientsOverAverage();
            StringBuilder message = new StringBuilder();
            if (clientsAboveAverage.size() != 0) {
                message.append("Les clients dont le nombre de commandes est supérieur à la moyenne :\n");
                for (String client : clientsAboveAverage) {
                    message.append(" - "+client+"\n");
                }
            } else {
                message.append("Aucun client n'a un nombre de commande supérieur à la moyenne.");
            }
            JOptionPane.showMessageDialog(this, message.toString(), "Clients au dessus de la moyenne des commandes", JOptionPane.INFORMATION_MESSAGE);
        });
        panel.add(buttonClientsOverAverage);

        return panel;
    }
}