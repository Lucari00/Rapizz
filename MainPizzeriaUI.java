import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MainPizzeriaUI extends JFrame {
    private PizzeriaManagerUI commandManagerUI;
    private PizzeriaUI pizzeriaUI;
    private DeliveryUI deliveryUI;

    public MainPizzeriaUI(Database database) {
        // Configurer la fenêtre principale
        setTitle("Pizzeria - Interface Principale");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Utiliser un GridLayout pour disposer les interfaces dans une grille 2x2
        setLayout(new GridLayout(2, 2, 10, 10));

        // Créer les instances des interfaces
        commandManagerUI = new PizzeriaManagerUI(database);
        deliveryUI = new DeliveryUI(database);
        pizzeriaUI = new PizzeriaUI(database, commandManagerUI, deliveryUI);
        
        // Ajouter les interfaces à la grille
        add(createPanelWithComponent(pizzeriaUI.getContentPane(), "Interface Client"));
        add(createPanelWithComponent(commandManagerUI.getContentPane(), "Interface Gérant"));
        add(createPanelWithComponent(deliveryUI.getContentPane(), "Interface Livreur"));
        add(createButtonPanel(database)); 

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
            pizzeriaUI.Refresh();
            commandManagerUI.refreshOrders();
            deliveryUI.Refresh();
        });
        panel.add(button);

        JButton buttonAddLivreur = new JButton("Add Livreur");
        buttonAddLivreur.addActionListener(e -> {
            // liste de nom de famille aléatoire
            List<String> lastNames = new ArrayList<>();
            lastNames.add("Palaysi");
            lastNames.add("Philippe");
            lastNames.add("Test");
            lastNames.add("famille");
            lastNames.add("nice");
            lastNames.add("Luca");
            lastNames.add("Jean");
            lastNames.add("damien");
            lastNames.add("prenom");
            lastNames.add("lol");


            // liste de prénom aléatoire
            List<String> firstNames = new ArrayList<>();
            firstNames.add("Luca");
            firstNames.add("Jean");
            firstNames.add("damien");
            firstNames.add("prenom");
            firstNames.add("lol");
            firstNames.add("Palaysi");
            firstNames.add("Philippe");
            firstNames.add("Test");
            firstNames.add("famille");
            firstNames.add("nice");

            // ajouter un livreur avec un nom et un prénom aléatoire
            database.addLivreur(firstNames.get((int) (Math.random() * firstNames.size())),
                    lastNames.get((int) (Math.random() * lastNames.size())));
            
            //database.addLivreur("Luca", "Palaysi");
            deliveryUI.Refresh();
        });
        panel.add(buttonAddLivreur);

        JButton buttonInsertData = new JButton("Insert Data");
        buttonInsertData.addActionListener(e -> {
            database.insertData();
            pizzeriaUI.Refresh();
            commandManagerUI.refreshOrders();
            deliveryUI.Refresh();
        });
        panel.add(buttonInsertData);
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