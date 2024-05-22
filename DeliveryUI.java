import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class DeliveryUI extends JFrame {
    private DefaultListModel<String> orderListModel;
    private JList<String> orderList;
    private ArrayList<String> orders;

    public DeliveryUI() {
        // Initialiser les données des commandes
        orders = new ArrayList<>();
        orders.add("Commande 1: 123 Rue de Paris");
        orders.add("Commande 2: 456 Avenue de Lyon");
        orders.add("Commande 3: 789 Boulevard de Marseille");

        // Configurer la fenêtre principale
        setTitle("Interface Livreur de la Pizzeria");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Créer les composants de l'interface utilisateur
        orderListModel = new DefaultListModel<>();
        for (String order : orders) {
            orderListModel.addElement(order);
        }

        orderList = new JList<>(orderListModel);
        orderList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JButton deliveredButton = new JButton("Marquer comme livré");
        deliveredButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                markAsDelivered();
            }
        });

        // Disposer les composants dans un panneau
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(orderList), BorderLayout.CENTER);
        panel.add(deliveredButton, BorderLayout.SOUTH);

        // Ajouter le panneau à la fenêtre
        add(panel);
    }

    private void markAsDelivered() {
        int selectedIndex = orderList.getSelectedIndex();
        if (selectedIndex != -1) {
            String deliveredOrder = orders.remove(selectedIndex);
            orderListModel.remove(selectedIndex);
            JOptionPane.showMessageDialog(this, deliveredOrder + " a été marqué comme livré.", "Commande Livrée", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une commande à marquer comme livrée.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new DeliveryUI().setVisible(true);
            }
        });
    }
}
