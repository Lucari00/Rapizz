import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class PizzeriaUI extends JFrame {

    private JComboBox<String> pizzaComboBox;
    private JComboBox<String> userComboBox;
    private JTextArea orderSummaryTextArea;
    private JLabel pizzaPriceLabel;
    private JLabel userBalanceLabel;

    private Database database;

    public PizzeriaUI(Database database) {
        this.database = database;
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
                updatePizzaPrice(pizzaList);
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
        panel.add(new JLabel("Prix de la pizza:"), gbc);

        gbc.gridx = 1;
        panel.add(pizzaPriceLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Choisissez un utilisateur:"), gbc);

        gbc.gridx = 1;
        panel.add(userComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Solde de l'utilisateur:"), gbc);

        gbc.gridx = 1;
        panel.add(userBalanceLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        panel.add(orderButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 3;
        panel.add(new JScrollPane(orderSummaryTextArea), gbc);

        // Ajouter le panneau à la fenêtre
        add(panel);

        updatePizzaPrice(pizzaList);
        updateUserBalance(clientList);
    }

    private void updatePizzaPrice(List<Pizza> pizzaList) {
        String selectedPizza = (String) pizzaComboBox.getSelectedItem();
        for (Pizza pizza : pizzaList) {
            if (pizza.getName().equals(selectedPizza)) {
                pizzaPriceLabel.setText("Prix: " + pizza.getPrice() + " €");
                break;
            }
        }
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

        // ajouter dans la base de donnée la commande
        database.addOrder(selectedPizza);


        // Créer un StringBuilder pour construire le résumé de la commande
        StringBuilder orderSummary = new StringBuilder();
        orderSummary.append("Vous avez commandé une pizza ").append(selectedPizza);

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