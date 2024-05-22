import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class PizzeriaManagerUI extends JFrame {
    private DefaultListModel<String> orderListModel;
    private JList<String> orderList;
    private JTextArea orderDetailsTextArea;

    private ArrayList<String> orders;

    public PizzeriaManagerUI() {
        // Initialiser les données des commandes
        orders = new ArrayList<>();
        
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

        JButton addOrderButton = new JButton("Ajouter une commande");
        addOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addOrder();
            }
        });

        JButton removeOrderButton = new JButton("Supprimer la commande");
        removeOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeOrder();
            }
        });

        // Disposer les composants dans un panneau
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addOrderButton);
        buttonPanel.add(removeOrderButton);
        
        panel.add(new JScrollPane(orderList), BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        panel.add(new JScrollPane(orderDetailsTextArea), BorderLayout.EAST);

        // Ajouter le panneau à la fenêtre
        add(panel);
    }

    private void showOrderDetails() {
        int selectedIndex = orderList.getSelectedIndex();
        if (selectedIndex != -1) {
            String orderDetails = orders.get(selectedIndex);
            orderDetailsTextArea.setText(orderDetails);
        } else {
            orderDetailsTextArea.setText("");
        }
    }

    private void addOrder() {
        String newOrder = JOptionPane.showInputDialog(this, "Entrez les détails de la commande:");
        if (newOrder != null && !newOrder.trim().isEmpty()) {
            orders.add(newOrder);
            orderListModel.addElement("Commande " + (orders.size()));
        }
    }

    private void removeOrder() {
        int selectedIndex = orderList.getSelectedIndex();
        if (selectedIndex != -1) {
            orders.remove(selectedIndex);
            orderListModel.remove(selectedIndex);
            orderDetailsTextArea.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une commande à supprimer.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new PizzeriaManagerUI().setVisible(true);
            }
        });
    }
}
