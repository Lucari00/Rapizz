import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class DeliveryUI extends JFrame {
    private DefaultListModel<String> orderListModel;
    private JList<String> orderList;
    private Order order;
    private JComboBox<String> deliveryComboBox;
    List<Livreur> livreurs;

    private boolean isRefreshing = false;

    private Database database;

    public DeliveryUI(Database database) {
        this.database = database;
        

        // Configurer la fenêtre principale
        setTitle("Interface Livreur de la Pizzeria");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        

        JButton deliveredButton = new JButton("Marquer comme livré");
        deliveredButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                markAsDelivered();
            }
        });

        // Menu déroulant pour sélectionner le livreur
        deliveryComboBox = new JComboBox<>();
        livreurs = database.getLivreurs();

        for (Livreur livreur : livreurs) {
            deliveryComboBox.addItem(livreur.getLastNameLivreur() + " " + livreur.getNameLivreur());
        }

        // Disposer les composants dans un panneau
        JPanel panel = new JPanel(new BorderLayout());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(deliveredButton);
        buttonPanel.add(deliveryComboBox);

        orderListModel = new DefaultListModel<>();
        // Récupérer les commandes pour le livreur sélectionné
        if (!livreurs.isEmpty()) {
            Livreur livreur = livreurs.get(0);
            order = database.getOrderByLivreur(livreur);
            if (order != null) {
                orderListModel.addElement("Commande " + order.getId() + " - " + database.getAdressById(order.getClientId()));
            }
        }

        orderList = new JList<>(orderListModel);
        orderList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // detect changement de livreur
        deliveryComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isRefreshing) {
                    JComboBox cb = (JComboBox)e.getSource();
                    Livreur livreur = livreurs.get(cb.getSelectedIndex());
                    ChangeLivreur(livreur);
                }
            }
        });
        
        panel.add(new JScrollPane(orderList), BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Ajouter le panneau à la fenêtre
        add(panel);
    }

    public void ChangeLivreur(Livreur livreur) {
        order = database.getOrderByLivreur(livreur);
        orderListModel.clear();
        if (order != null) {
            orderListModel.addElement("Commande " + order.getId() + " - " + database.getAdressById(order.getClientId()));
        }
    }

    public void refreshOrders() {
        if (deliveryComboBox.getSelectedIndex() == -1) {
            return;
        }
        Livreur livreur = livreurs.get(deliveryComboBox.getSelectedIndex());
        if (livreur != null) {
            ChangeLivreur(livreur);
        }
    }

    public void refreshLivreurs() {
        deliveryComboBox.removeAllItems();
        livreurs = database.getLivreurs();
        for (Livreur livreur : livreurs) {
            deliveryComboBox.addItem(livreur.getLastNameLivreur() + " " + livreur.getNameLivreur());
        }
    }

    public void Refresh() {
        isRefreshing = true;
        refreshOrders();
        refreshLivreurs();
        isRefreshing = false;
    }

    private void markAsDelivered() {
        int selectedIndex = orderList.getSelectedIndex();
        if (selectedIndex != -1) {
            //String deliveredOrder = orders.remove(selectedIndex);
            orderListModel.remove(selectedIndex);
            database.markAsDelivered(order.getId());
            //JOptionPane.showMessageDialog(this, deliveredOrder + " a été marqué comme livré.", "Commande Livrée", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une commande à marquer comme livrée.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

}
