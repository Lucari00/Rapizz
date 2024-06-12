import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Partie de l'interface graphique pour les informations des livreurs
 */
public class DeliveryUI extends JFrame {
    private DefaultListModel<String> orderListModel;
    private JList<String> orderList;
    private Order order;
    private JComboBox<String> deliveryComboBox;
    private List<Livreur> livreurs;

    private boolean isRefreshing = false;

    private Database database;
    private MainPizzeriaUI mainPizzeriaUI;
    private PizzeriaManagerUI commandManagerUI;
    private PizzeriaUI pizzeriaUI;

    /**
     * Constructeur de la classe DeliveryUI
     * @param database la classe de gestion de la base de données
     * @param mainPizzeriaUI la classe de l'interface principale de la pizzeria
     * @param commandManagerUI la classe de l'interface de gestion des commandes
     */
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
                    changeLivreur(livreur);
                }
            }
        });
        
        panel.add(new JScrollPane(orderList), BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Ajouter le panneau à la fenêtre
        add(panel);
    }

    /**
     * Setter de l'interface principale de la pizzeria
     * @param pizzeriaUI l'interface principale de la pizzeria
     */
    public void setPizzeriaUI(PizzeriaUI pizzeriaUI) {
        this.pizzeriaUI = pizzeriaUI;
    }

    /**
     * Procédure qui rafraîchit les commandes pour le livreur passé en paramètre
     * @param livreur le livreur sélectionné
     */
    public void changeLivreur(Livreur livreur) {
        order = database.getOrderByLivreur(livreur);
        orderListModel.clear();
        if (order != null) {
            orderListModel.addElement("Commande " + order.getId() + " - " + database.getAdressById(order.getClientId()));
        }
    }

    /**
     * Procédure qui rafraîchit les commandes pour le livreur sélectionné
     */
    public void refreshOrders() {
        if (deliveryComboBox.getSelectedIndex() == -1) {
            return;
        }
        Livreur livreur = livreurs.get(deliveryComboBox.getSelectedIndex());
        if (livreur != null) {
            changeLivreur(livreur);
        }
    }

    /**
     * Procédure qui rafraîchit la liste des livreurs
     */
    public void refreshLivreurs() {
        deliveryComboBox.removeAllItems();
        livreurs = database.getLivreurs();
        for (Livreur livreur : livreurs) {
            deliveryComboBox.addItem(livreur.getLastNameLivreur() + " " + livreur.getNameLivreur());
        }
    }

    /**
     * Procédure qui rafraîchit les données de l'interface livreur
     */
    public void refresh() {
        isRefreshing = true;
        refreshOrders();
        refreshLivreurs();
        isRefreshing = false;
    }

    /**
     * Procédure qui marque la commande comme livrée
     * Appelle la méthode markAsDelivered de la classe Database quand le bouton est cliqué
     */
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
    }
}
