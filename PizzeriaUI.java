import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class PizzeriaUI extends JFrame {

    private JComboBox<String> pizzaComboBox;
    private JComboBox<String> userComboBox;
    private JComboBox<String> sizeComboBox;
    private JTextArea orderSummaryTextArea;
    private JLabel pizzaPriceLabel;
    private JLabel userBalanceLabel;

    private float finalPrice;
    private Database database;
    private PizzeriaManagerUI commandManagerUI;
    private DeliveryUI deliveryUI;

    private MainPizzeriaUI mainPizzeriaUI;
    private JButton viewIngredientsButton;
    private JButton addBalanceButton;

    public PizzeriaUI(Database database, MainPizzeriaUI mainPizzeriaUI, PizzeriaManagerUI commandManagerUI, DeliveryUI deliveryUI) {
        this.database = database;
        this.commandManagerUI = commandManagerUI;
        this.deliveryUI = deliveryUI;
        this.mainPizzeriaUI = mainPizzeriaUI;

        // Configurer la fenêtre principale
        setTitle("Pizzeria");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Créer les composants de l'interface utilisateur
        List<Pizza> pizzaList = database.getPizzas(); // Get pizzas from database
        
        // créer un tableau de chaînes pour stocker les noms des pizzas
        String[] pizzas = new String[pizzaList.size()];
        for (int i = 0; i < pizzaList.size(); i++) {
            pizzas[i] = pizzaList.get(i).getName();
        }
        pizzaComboBox = new JComboBox<>(pizzas);

        List<Taille> tailleList = database.getTailles(); // Get sizes from database
        String[] tailles = new String[tailleList.size()];
        for (int i = 0; i < tailleList.size(); i++) {
            tailles[i] = tailleList.get(i).getNomTaille();
        }
        sizeComboBox = new JComboBox<>(tailles);

        JButton orderButton = new JButton("Passer commande");
        // Ajouter des action listeners
        orderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                passOrder(database);
            }
        });

        pizzaPriceLabel = new JLabel("Prix: ");
        userBalanceLabel = new JLabel("Solde: ");

        List<Client> clientList = database.getClients(); // Get clients from database
        String[] clients = new String[clientList.size()];
        for (int i = 0; i < clientList.size(); i++) {
            clients[i] = clientList.get(i).getNomClient() + " " + clientList.get(i).getPrenomClient();
        }
        userComboBox = new JComboBox<>(clients);

        orderSummaryTextArea = new JTextArea(10, 30);
        orderSummaryTextArea.setEditable(false);

        pizzaComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updatePizzaPrice(pizzaList, tailleList);
            }
        });

        sizeComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updatePizzaPrice(pizzaList, tailleList);
            }
        });

        userComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateUserBalance();
            }
        });

        viewIngredientsButton = new JButton("Voir Ingrédients");
        viewIngredientsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showIngredients();
            }
        });

        addBalanceButton = new JButton("Ajouter au solde");
        addBalanceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAddBalancePopup();
            }
        });

        // Disposer les composants dans un panneau
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Choisissez votre pizza:"), gbc);

        gbc.gridx = 1;
        panel.add(pizzaComboBox, gbc);

        gbc.gridx = 2; // Placer le bouton "Voir Ingrédients" à droite de la pizza sélectionnée
        panel.add(viewIngredientsButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Taille de la pizza:"), gbc);

        gbc.gridx = 1;
        panel.add(sizeComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Prix de la pizza:"), gbc);

        gbc.gridx = 1;
        panel.add(pizzaPriceLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Choisissez un utilisateur:"), gbc);

        gbc.gridx = 1;
        panel.add(userComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(new JLabel("Solde de l'utilisateur:"), gbc);

        gbc.gridx = 1;
        panel.add(userBalanceLabel, gbc);

        gbc.gridx = 2;
        panel.add(addBalanceButton, gbc); // Ajouter le bouton à côté du solde

        gbc.gridx = 1;
        gbc.gridy = 5;
        panel.add(orderButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.gridheight = 1;

        // Ajouter le panneau à la fenêtre
        add(panel);

        updatePizzaPrice(pizzaList, tailleList);
        updateUserBalance();
    }

    private void showAddBalancePopup() {
        String inputValue = JOptionPane.showInputDialog(mainPizzeriaUI, "Entrez le montant à ajouter:", "Ajouter au solde", JOptionPane.PLAIN_MESSAGE);
        if (inputValue != null) {
            try {
                float amountToAdd = Float.parseFloat(inputValue);
                if (amountToAdd < 0) {
                    JOptionPane.showMessageDialog(mainPizzeriaUI, "Le montant doit être positif.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Client client = database.getClient();
                database.refreshClientSolde(client); // Récupérer le solde actuel du client
                client.setSolde(client.getSolde() + amountToAdd);
                database.updateClientSolde(client.getSolde()); // Mettre à jour le solde dans la base de données
                userBalanceLabel.setText("Solde: " + client.getSolde() + " €");
                JOptionPane.showMessageDialog(mainPizzeriaUI, "Solde mis à jour avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
                
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(mainPizzeriaUI, "Veuillez entrer un nombre valide.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showIngredients() {
        String selectedPizzaName = (String) pizzaComboBox.getSelectedItem();
        if (selectedPizzaName == null) {
            JOptionPane.showMessageDialog(mainPizzeriaUI, "Veuillez sélectionner une pizza.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        // Rechercher l'objet Pizza correspondant au nom sélectionné
        List<Pizza> pizzaList = database.getPizzas();
        Pizza selectedPizza = null;
        for (Pizza pizza : pizzaList) {
            if (pizza.getName().equals(selectedPizzaName)) {
                selectedPizza = pizza;
                break;
            }
        }
    
        if (selectedPizza == null) {
            JOptionPane.showMessageDialog(mainPizzeriaUI, "Pizza non trouvée.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        // Obtenir les ingrédients de la pizza sélectionnée
        List<String> ingredients = database.getIngredients(selectedPizza);
    
        // Construire le message des ingrédients
        StringBuilder ingredientsMessage = new StringBuilder("Ingrédients de la pizza " + selectedPizzaName + ":\n");
        for (String ingredient : ingredients) {
            ingredientsMessage.append("- ").append(ingredient).append("\n");
        }
    
        // Afficher les ingrédients dans une popup
        JOptionPane.showMessageDialog(mainPizzeriaUI, ingredientsMessage.toString(), "Ingrédients", JOptionPane.INFORMATION_MESSAGE);
    }
    
       
    

    public void Refresh() {
        List<Pizza> pizzaList = database.getPizzas(); // Get pizzas from database
        String[] pizzas = new String[pizzaList.size()];
        for (int i = 0; i < pizzaList.size(); i++) {
            pizzas[i] = pizzaList.get(i).getName();
        }
        pizzaComboBox.removeAllItems();
        for (String pizza : pizzas) {
            pizzaComboBox.addItem(pizza);
        }

        List<Taille> tailleList = database.getTailles(); // Get sizes from database
        String[] tailles = new String[tailleList.size()];
        for (int i = 0; i < tailleList.size(); i++) {
            tailles[i] = tailleList.get(i).getNomTaille();
        }
        sizeComboBox.removeAllItems();
        for (String taille : tailles) {
            sizeComboBox.addItem(taille);
        }

        List<Client> clientList = database.getClients(); // Get clients from database
        String[] clients = new String[clientList.size()];
        for (int i = 0; i < clientList.size(); i++) {
            clients[i] = clientList.get(i).getNomClient() + " " + clientList.get(i).getPrenomClient();
        }
        userComboBox.removeAllItems();
        for (String client : clients) {
            userComboBox.addItem(client);
        }

        updatePizzaPrice(pizzaList, tailleList);
        updateUserBalance();
    }

    private void updatePizzaPrice(List<Pizza> pizzaList, List<Taille> tailleList) { 
        String selectedPizza = (String) pizzaComboBox.getSelectedItem();
        String selectedTaille = (String) sizeComboBox.getSelectedItem();

        float pizzaPrice = 0;
        for (Pizza pizza : pizzaList) {
            if (pizza.getName().equals(selectedPizza)) {
                pizzaPrice = pizza.getPrice();
                break;
            }
        }

        float tailleMultiplicatif = 1;
        for (Taille taille : tailleList) {
            if (taille.getNomTaille().equals(selectedTaille)) {
                tailleMultiplicatif = taille.getPrixMultiplicatif();
                break;
            }
        }

        finalPrice = Math.round(pizzaPrice * tailleMultiplicatif * 100) / 100.0f;
        pizzaPriceLabel.setText("Prix: " + finalPrice + " €");
    }

    public void updateUserBalance(){
        List<Client> clientList = database.getClients(); // Get clients from database
        String selectedClient = (String) userComboBox.getSelectedItem();
        for (Client client : clientList) {
            if ((client.getNomClient() + " " + client.getPrenomClient()).equals(selectedClient)) {
                database.refreshClientSolde(client);
                userBalanceLabel.setText("Solde: " + client.getSolde() + " €");
                database.setClient(client);
                break;
            }
        }
    }

    private void passOrder(Database database) {
        String selectedPizza = (String) pizzaComboBox.getSelectedItem();
        String selectedTaille = (String) sizeComboBox.getSelectedItem();

        // ajouter dans la base de donnée la commande
        String orderString = database.addOrder(selectedPizza, selectedTaille, finalPrice);

        switch (orderString) {
            case "noFunds":
                JOptionPane.showMessageDialog(mainPizzeriaUI, "Erreur avec la commande pas assez de fonds.", "Erreur", JOptionPane.ERROR_MESSAGE);
                break;
        
            case "noLivreur":
                JOptionPane.showMessageDialog(mainPizzeriaUI, "Erreur avec la commande pas de livreur disponible.", "Erreur", JOptionPane.ERROR_MESSAGE);
                break;

            case "success":
                JOptionPane.showMessageDialog(mainPizzeriaUI, "Vous avez commandé une pizza " + selectedPizza + " de taille " + selectedTaille + " au prix de " + finalPrice + " €.", "Succès", JOptionPane.INFORMATION_MESSAGE);
                break;

            case "free":
                JOptionPane.showMessageDialog(mainPizzeriaUI, "Bravo, vous avez gagné une pizza gratuite pour en avoir commandé 10 !", "Félicitation !", JOptionPane.INFORMATION_MESSAGE);
                break;
            default:
                break;
        }


        if (orderString == "success" || orderString == "free") {
            Client client = database.getClient();
            userBalanceLabel.setText("Solde: " + client.getSolde() + " €");
            commandManagerUI.refreshOrders();
            deliveryUI.refreshOrders();
        }
        //orderSummaryTextArea.setText(orderSummary.toString());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                //new PizzeriaUI().setVisible(true);
            }
        });
    }
}