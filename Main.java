import java.io.IOException;
import java.sql.SQLException;
import java.io.FileNotFoundException;

/**
 * Classe principale pour l'application Rapizz.
 */
public class Main {
    /**
     * Méthode principale de l'application Rapizz.
     * Elle initialise la base de données et lance l'interface utilisateur principale de la pizzeria.
     * 
     * @param args les arguments de la ligne de commande
     * @throws SQLException si une erreur de base de données survient
     * @throws FileNotFoundException si un fichier nécessaire n'est pas trouvé
     * @throws IOException si une erreur d'entrée/sortie survient
     */
    public static void main(String[] args) throws SQLException, FileNotFoundException, IOException {
        
        Database database = new Database();

        new MainPizzeriaUI(database);
         
   }
}