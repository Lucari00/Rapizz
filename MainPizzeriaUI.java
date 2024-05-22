import javax.swing.*;
import java.awt.*;

public class MainPizzeriaUI extends JFrame {

    public MainPizzeriaUI(Database database) {
        // Configurer la fenêtre principale
        setTitle("Pizzeria - Interface Principale");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Utiliser un GridLayout pour disposer les interfaces dans une grille 2x2
        setLayout(new GridLayout(2, 2, 10, 10));

        // Créer les instances des interfaces
        PizzeriaUI pizzeriaUI = new PizzeriaUI(database);
        PizzeriaManagerUI commandManagerUI = new PizzeriaManagerUI();
        DeliveryUI deliveryUI = new DeliveryUI();

        // Ajouter les interfaces à la grille
        add(createPanelWithComponent(pizzeriaUI.getContentPane(), "Interface Client"));
        add(createPanelWithComponent(commandManagerUI.getContentPane(), "Interface Gérant"));
        add(createPanelWithComponent(deliveryUI.getContentPane(), "Interface Livreur"));
        add(createButtonPanel(database)); // Case vide

        // Rendre la fenêtre principale visible
        setVisible(true);
    }

    // Méthode utilitaire pour encapsuler un composant dans un JPanel avec un titre
    private JPanel createPanelWithComponent(Container component, String title) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(component, BorderLayout.CENTER);
        panel.setBorder(BorderFactory.createTitledBorder(title));
        return panel;
    }

    private JPanel createButtonPanel(Database database) {
        JPanel panel = new JPanel(new FlowLayout());
        JButton button = new JButton("Reset Database");
        button.addActionListener(e -> {
            database.resetDatabase();
        });
        panel.add(button);
        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // new MainPizzeriaUI(new Database());
            }
        });
    }
}