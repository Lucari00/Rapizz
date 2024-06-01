CREATE TABLE IF NOT EXISTS Pizzas(
   idPizza INT AUTO_INCREMENT,
   nomPizza VARCHAR(50) NOT NULL,
   prixPizza DECIMAL(15,2) NOT NULL CHECK (prixPizza >= 0),
   PRIMARY KEY(idPizza),
   UNIQUE(nomPizza)
);

CREATE TABLE IF NOT EXISTS Ingredients(
   idIngredient INT AUTO_INCREMENT,
   stock INT NOT NULL CHECK (stock >= 0),
   nomIngredient VARCHAR(50) NOT NULL,
   PRIMARY KEY(idIngredient)
);

CREATE TABLE IF NOT EXISTS Tailles(
   nomTaille VARCHAR(20),
   prixMultiplicatif DECIMAL(3,2) CHECK (prixMultiplicatif >= 0),
   PRIMARY KEY(nomTaille)
);

CREATE TABLE IF NOT EXISTS Clients(
   idClient INT AUTO_INCREMENT,
   nomClient VARCHAR(50) NOT NULL,
   prenomClient VARCHAR(50) NOT NULL,
   adresseClient VARCHAR(100) NOT NULL,
   solde DECIMAL(15,2) CHECK (solde >= 0),
   PRIMARY KEY(idClient)
);

CREATE TABLE IF NOT EXISTS TypeVehicules(
   type VARCHAR(50),
   distance DECIMAL(15,2) NOT NULL,
   PRIMARY KEY(type)
);

CREATE TABLE IF NOT EXISTS Vehicules(
   idVehicule INT AUTO_INCREMENT,
   Marque VARCHAR(50) NOT NULL,
   nomVehicule VARCHAR(50) NOT NULL,
   immatriculation VARCHAR(50) NOT NULL,
   typeVehicule VARCHAR(50) NOT NULL,
   PRIMARY KEY(idVehicule),
   FOREIGN KEY(typeVehicule) REFERENCES TypeVehicules(type)
);

CREATE TABLE IF NOT EXISTS Livreurs(
   idLivreur INT AUTO_INCREMENT,
   nomLivreur VARCHAR(50) NOT NULL,
   prenomLivreur VARCHAR(50) NOT NULL,
   idVehicule INT,
   PRIMARY KEY(idLivreur),
   UNIQUE(idVehicule),
   FOREIGN KEY(idVehicule) REFERENCES Vehicules(idVehicule)
);

CREATE TABLE IF NOT EXISTS Commandes(
   idCommande INT AUTO_INCREMENT,
   prixCommande DECIMAL(15,2) NOT NULL CHECK (prixCommande >= 0),
   dateCommande DATETIME NOT NULL DEFAULT current_timestamp,
   dateLivree DATETIME,
   idLivreur INT NOT NULL,
   idClient INT NOT NULL,
   PRIMARY KEY(idCommande),
   FOREIGN KEY(idLivreur) REFERENCES Livreurs(idLivreur),
   FOREIGN KEY(idClient) REFERENCES Clients(idClient)
);

CREATE TABLE IF NOT EXISTS IngredientPizza(
   idPizza INT,
   idIngredient INT,
   PRIMARY KEY(idPizza, idIngredient),
   FOREIGN KEY(idPizza) REFERENCES Pizzas(idPizza),
   FOREIGN KEY(idIngredient) REFERENCES Ingredients(idIngredient)
);

CREATE TABLE IF NOT EXISTS PizzaCommande(
   idPizza INT,
   nomTaille VARCHAR(20),
   idCommande INT,
   PRIMARY KEY(idPizza, nomTaille, idCommande),
   FOREIGN KEY(idPizza) REFERENCES Pizzas(idPizza),
   FOREIGN KEY(nomTaille) REFERENCES Tailles(nomTaille),
   FOREIGN KEY(idCommande) REFERENCES Commandes(idCommande)
);

DELIMITER $$

CREATE TRIGGER IF NOT EXISTS check_livreur_commande 
BEFORE INSERT ON Commandes
FOR EACH ROW
BEGIN
   DECLARE livreurCount INT;

   -- Vérifier si le livreur a déjà une commande en cours
   SELECT COUNT(*) INTO livreurCount 
   FROM Commandes
   WHERE idLivreur = NEW.idLivreur AND dateLivree IS NULL;
   IF livreurCount > 0 THEN
      SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Un livreur ne peut pas avoir plus d''une commande en cours';
   END IF;
END$$

DELIMITER ;

DELIMITER $$

CREATE PROCEDURE IF NOT EXISTS update_commande_livree(IN idCmde INT)
BEGIN
   DECLARE tempsLivraison INT;
   DECLARE prixCmde DECIMAL(15,2);
   DECLARE idClt INT;

   -- Mettre à jour la date de livraison
   UPDATE Commandes
   SET dateLivree = current_timestamp
   WHERE idCommande = idCmde;

   -- Calculer le temps de livraison
   SELECT TIMESTAMPDIFF(MINUTE, dateCommande, dateLivree) INTO tempsLivraison
   FROM Commandes
   WHERE idCommande = idCmde;

   -- Si le temps de livraison est supérieur à 30 minutes, rembourser le client et mettre à jour le prix de la commande
   IF tempsLivraison > 1 THEN
      SELECT prixCommande, idClient INTO prixCmde, idClt
      FROM Commandes
      WHERE idCommande = idCmde;

      UPDATE Commandes
      SET prixCommande = 0
      WHERE idCommande = idCmde;

      UPDATE Clients
      SET solde = solde + prixCmde
      WHERE idClient = idClt;
    END IF;
END$$

DELIMITER ;