import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;

import java.io.BufferedReader;
import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) throws SQLException, FileNotFoundException, IOException {
         // Create a new database connection
         
         Database database = new Database("Palaysi", "Luca", "null", 100);

         // create main Upizzeria UI
         MainPizzeriaUI mainPizzeriaUI = new MainPizzeriaUI(database);
         
   }
}
