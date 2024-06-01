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

    public PizzeriaUI(Database database, PizzeriaManagerUI commandManagerUI, DeliveryUI deliveryUI) {
        this.database = database;
        this.commandManagerUI = commandManagerUI;
        this.deliveryUI = deliveryUI;
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
                updateUserBalance(clientList);
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

        gbc.gridx = 1;
        gbc.gridy = 5;
        panel.add(orderButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.gridheight = 1;
        JScrollPane scrollPane = new JScrollPane(orderSummaryTextArea);
        //scrollPane.setVerticalScrollBarPolicy(JScrollPane); // Définir la politique d'ajustement
        panel.add(scrollPane, gbc);

        // Ajouter le panneau à la fenêtre
        add(panel);

        updatePizzaPrice(pizzaList, tailleList);
        updateUserBalance(clientList);
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
        updateUserBalance(clientList);
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

    private void updateUserBalance(List<Client> clientList) {
        String selectedClient = (String) userComboBox.getSelectedItem();
        for (Client client : clientList) {
            if ((client.getNomClient() + " " + client.getPrenomClient()).equals(selectedClient)) {
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
        boolean hasPassed = database.addOrder(selectedPizza, selectedTaille, finalPrice);

        // Créer un StringBuilder pour construire le résumé de la commande
        StringBuilder orderSummary = new StringBuilder();
        if (hasPassed) {
            orderSummary.append("Vous avez commandé une pizza ").append(selectedPizza).append(" de taille ").append(selectedTaille).append(" au prix de ").append(finalPrice).append(" €.");

            // update user balance
            Client client = database.getClient();
            userBalanceLabel.setText("Solde: " + client.getSolde() + " €");
            commandManagerUI.refreshOrders();
            deliveryUI.refreshOrders();
        } else {
            orderSummary.append("Erreur avec la commande ou fonds insuffisants ou plus de livreurs disponibles.\n");
        }
        

        orderSummaryTextArea.setText(orderSummary.toString());
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