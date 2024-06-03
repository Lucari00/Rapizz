-- Insertion des données dans la table Pizzas
INSERT INTO Pizzas (nomPizza, prixPizza) VALUES
('Margherita', 8.99),
('Pepperoni', 9.99),
('4 Fromages', 10.99);

-- Insertion des données dans la table Ingredients
INSERT INTO Ingredients (stock, nomIngredient) VALUES
(100, 'Tomate'),
(150, 'Mozzarella'),
(120, 'Pepperoni'),
(80, 'Champignons'),
(90, 'Oignons'),
(110, 'Olives');

-- Insertion des données dans la table Tailles
INSERT INTO Tailles (nomTaille, prixMultiplicatif) VALUES
('Petite', 0.66),
('Moyenne', 1),
('Grande', 1.33);

-- Insertion des données dans la table Clients
INSERT INTO Clients (nomClient, prenomClient, adresseClient, solde) VALUES
('Dupont', 'Jean', '1 rue de la Paix, Paris', 50.00),
('Martin', 'Sophie', '15 avenue des Lilas, Lyon', 35.00),
('Durand', 'Pierre', '8 rue de la République, Marseille', 75.00);

-- Insertion des données dans la table TypeVehicules
INSERT INTO TypeVehicules (type, distance) VALUES
('Voiture', 100),
('Vélo', 20),
('Moto', 80);

-- Insertion des données dans la table Vehicules
INSERT INTO Vehicules (Marque, nomVehicule, immatriculation, typeVehicule) VALUES
('Toyota', 'Corolla', 'AB-123-CD', 'Voiture'),
('Peugeot', 'VTT', 'EF-456-GH', 'Vélo'),
('Honda', 'CBR', 'IJ-789-KL', 'Moto');

-- Insertion des données dans la table Livreurs
INSERT INTO Livreurs (nomLivreur, prenomLivreur, idVehicule) VALUES
('Dubois', 'Philippe', 1),
('Lefebvre', 'Julie', 2),
('Moreau', 'Luc', 3);

-- Insertion des données dans la table IngredientPizza
INSERT INTO IngredientPizza (idPizza, idIngredient) VALUES
(1, 1),
(1, 2),
(1, 6),
(2, 1),
(2, 3),
(2, 6),
(3, 1),
(3, 2),
(3, 4),
(3, 5);
