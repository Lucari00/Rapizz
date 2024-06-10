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
    private MainPizzeriaUI mainPizzeriaUI;
    private PizzeriaManagerUI commandManagerUI;
    private PizzeriaUI pizzeriaUI;

    public DeliveryUI(Database database, MainPizzeriaUI mainPizzeriaUI, PizzeriaManagerUI commandManagerUI) {
        this.database = database;
        this.mainPizzeriaUI = mainPizzeriaUI;
        this.commandManagerUI = commandManagerUI;

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

    public void setPizzeriaUI(PizzeriaUI pizzeriaUI) {
        this.pizzeriaUI = pizzeriaUI;
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
        if (orderList.getModel().getSize() > 0) {
            orderListModel.remove(0);
            database.markAsDelivered(order.getId());
            commandManagerUI.refreshOrders();
            commandManagerUI.refreshDeliveredOrders();
            pizzeriaUI.updateUserBalance();
        } else {
            JOptionPane.showMessageDialog(mainPizzeriaUI, "Il n'y a pas de commande à marquer comme livrée.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
        // int selectedIndex = orderList.getSelectedIndex();
        // System.out.println("selected : " + selectedIndex);
        // if (selectedIndex != -1) {
        //     //String deliveredOrder = orders.remove(selectedIndex);
        //     orderListModel.remove(selectedIndex);
        //     database.markAsDelivered(order.getId());
        //     //JOptionPane.showMessageDialog(this, deliveredOrder + " a été marqué comme livré.", "Commande Livrée", JOptionPane.INFORMATION_MESSAGE);
        // } else {
        //     JOptionPane.showMessageDialog(mainPizzeriaUI, "Veuillez sélectionner une commande à marquer comme livrée.", "Erreur", JOptionPane.ERROR_MESSAGE);
        // }
    }

}
