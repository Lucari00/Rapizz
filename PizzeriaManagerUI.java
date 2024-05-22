import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class PizzeriaManagerUI extends JFrame {
    private DefaultListModel<String> orderListModel;
    private JList<String> orderList;
    private JTextArea orderDetailsTextArea;

    private List<Order> orders;

    private Database database;

    public PizzeriaManagerUI(Database database) {
        this.database = database;
        
        // Configurer la fenêtre principale
        setTitle("Gestion des commandes de la Pizzeria");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Créer les composants de l'interface utilisateur
        orderListModel = new DefaultListModel<>();
        orderList = new JList<>(orderListModel);
        orderList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        orderList.addListSelectionListener(e -> showOrderDetails());

        orderDetailsTextArea = new JTextArea(10, 30);
        orderDetailsTextArea.setEditable(false);

        JButton refreshOrdersButton = new JButton("Rafraîchir les commandes");
        refreshOrdersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshOrders();
            }
        });

        // Disposer les composants dans un panneau
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(refreshOrdersButton);
        
        panel.add(new JScrollPane(orderList), BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        panel.add(new JScrollPane(orderDetailsTextArea), BorderLayout.EAST);

        // Ajouter le panneau à la fenêtre
        add(panel);

        // Rafraîchir les commandes lors du lancement
        refreshOrders();
    }

    private void showOrderDetails() {
        int selectedIndex = orderList.getSelectedIndex();
        if (selectedIndex != -1) {
            Order selectedOrder = orders.get(selectedIndex);
            orderDetailsTextArea.setText(selectedOrder.toString());
        } else {
            orderDetailsTextArea.setText("");
        }
    }

    public void refreshOrders() {
        orders = database.getOrders();
        orderListModel.clear();
        for (Order order : orders) {
            orderListModel.addElement("Commande " + order.getId() + " - " + database.getPizzaNameFromOrder(order) + " - " + database.getTailleFromOrder(order));
        }
    }

    private void removeOrder() {
        int selectedIndex = orderList.getSelectedIndex();
        if (selectedIndex != -1) {
            // Supprimer la commande de la base de données (à implémenter dans la classe Database)
            Order orderToRemove = orders.get(selectedIndex);
            //database.removeOrder(orderToRemove.getId());

            // Rafraîchir la liste des commandes
            refreshOrders();
        } else {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une commande à supprimer.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

}
