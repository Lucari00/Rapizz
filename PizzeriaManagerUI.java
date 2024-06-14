import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Classe de l'interface graphique du gérant de la pizzeria.
 */
public class PizzeriaManagerUI extends JFrame {
    private DefaultListModel<String> orderListModel;
    private JList<String> orderList;
    private JTextArea orderDetailsTextArea;

    private DefaultListModel<String> secondListModel;
    private JList<String> secondList;

    private List<Order> ordersNotDelivered;
    private List<Order> ordersDelivered;

    private Database database;

    /**
     * Constructeur de l'interface graphique du gérant de la pizzeria.
     * @param database l'objet qui accède à la base de données de la pizzeria
     */
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
        orderList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                showOrderDetails();
            }
        });

        secondListModel = new DefaultListModel<>();
        secondList = new JList<>(secondListModel);
        secondList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        secondList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                showOrderDelivered();
            }
        });

        orderDetailsTextArea = new JTextArea(10, 30);
        orderDetailsTextArea.setEditable(false);

        // Bouton pour rafraîchir les commandes
        JButton refreshOrdersButton = new JButton("Rafraîchir les commandes");
        refreshOrdersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshOrders();
                refreshDeliveredOrders();
            }
        });

        // Disposer les composants dans un panneau
        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel listsPanel = new JPanel(new GridLayout(2, 1));
        listsPanel.add(new JScrollPane(orderList));
        listsPanel.add(new JScrollPane(secondList));

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(refreshOrdersButton);
        
        mainPanel.add(listsPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        mainPanel.add(new JScrollPane(orderDetailsTextArea), BorderLayout.EAST);

        // Ajouter le panneau à la fenêtre
        add(mainPanel);

        // Rafraîchir les commandes lors du lancement
        refreshOrders();
        refreshDeliveredOrders();
    }

    /**
     * Procédure pour afficher les détails d'une commande livrée.
     */
    private void showOrderDelivered() {
        int selectedIndex = secondList.getSelectedIndex();
        if (selectedIndex != -1) {
            Order selectedOrder = ordersDelivered.get(selectedIndex);
            orderDetailsTextArea.setText(selectedOrder.toStringDelivered());
        } else {
            orderDetailsTextArea.setText("");
        }
    }

    /**
     * Procédure pour rafraîchir les commandes livrées.
     */
    public void refreshDeliveredOrders() {
        ordersDelivered = database.getDeliveredOrders();
        secondListModel.clear();
        for (Order order : ordersDelivered) {
            secondListModel.addElement("Commande " + order.getId() + " - " + database.getPizzaNameFromOrder(order) + " - " + database.getTailleFromOrder(order));
        }
    }

    /**
     * Procédure pour afficher les détails d'une commande non livrée.
     */
    private void showOrderDetails() {
        int selectedIndex = orderList.getSelectedIndex();
        if (selectedIndex != -1) {
            Order selectedOrder = ordersNotDelivered.get(selectedIndex);
            orderDetailsTextArea.setText(selectedOrder.toStringNotDelivered());
        } else {
            orderDetailsTextArea.setText("");
        }
    }

    /**
     * Procédure pour rafraîchir les commandes non livrées.
     */
    public void refreshOrders() {
        ordersNotDelivered = database.getOrders();
        orderListModel.clear();
        for (Order order : ordersNotDelivered) {
            orderListModel.addElement("Commande " + order.getId() + " - " + database.getPizzaNameFromOrder(order) + " - " + database.getTailleFromOrder(order));
        }
    }
}
