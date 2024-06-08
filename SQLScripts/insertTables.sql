-- Insertion des données dans la table Pizzas
INSERT INTO Pizzas (nomPizza, prixPizza) VALUES
('Savoyarde', 12.00),
('Nicoise', 11.50),
('Provencale', 10.00),
('Basquaise', 13.00),
('Alsacienne', 14.00);

-- Insertion des données dans la table Ingredients
INSERT INTO Ingredients (stock, nomIngredient) VALUES
(100, 'Creme Fraiche'),
(100, 'Mozzarella'),
(100, 'Reblochon'),
(100, 'Pomme de Terre'),
(100, 'Tomate'),
(100, 'Olive Noire'),
(100, 'Anchois'),
(100, 'Poivron'),
(100, 'Jambon de Bayonne'),
(100, 'Piment d'' Espelette'),
(100, 'Lardon'),
(100, 'Oignon');

-- Insertion des données dans la table IngredientPizza
-- Savoyarde
INSERT INTO IngredientPizza (idPizza, idIngredient) VALUES
((SELECT idPizza FROM Pizzas WHERE nomPizza = 'Savoyarde'), (SELECT idIngredient FROM Ingredients WHERE nomIngredient = 'Creme Fraiche')),
((SELECT idPizza FROM Pizzas WHERE nomPizza = 'Savoyarde'), (SELECT idIngredient FROM Ingredients WHERE nomIngredient = 'Lardon')),
((SELECT idPizza FROM Pizzas WHERE nomPizza = 'Savoyarde'), (SELECT idIngredient FROM Ingredients WHERE nomIngredient = 'Reblochon')),
((SELECT idPizza FROM Pizzas WHERE nomPizza = 'Savoyarde'), (SELECT idIngredient FROM Ingredients WHERE nomIngredient = 'Pomme de Terre'));

-- Nicoise
INSERT INTO IngredientPizza (idPizza, idIngredient) VALUES
((SELECT idPizza FROM Pizzas WHERE nomPizza = 'Nicoise'), (SELECT idIngredient FROM Ingredients WHERE nomIngredient = 'Tomate')),
((SELECT idPizza FROM Pizzas WHERE nomPizza = 'Nicoise'), (SELECT idIngredient FROM Ingredients WHERE nomIngredient = 'Olive Noire')),
((SELECT idPizza FROM Pizzas WHERE nomPizza = 'Nicoise'), (SELECT idIngredient FROM Ingredients WHERE nomIngredient = 'Anchois')),
((SELECT idPizza FROM Pizzas WHERE nomPizza = 'Nicoise'), (SELECT idIngredient FROM Ingredients WHERE nomIngredient = 'Poivron'));

-- Provencale
INSERT INTO IngredientPizza (idPizza, idIngredient) VALUES
((SELECT idPizza FROM Pizzas WHERE nomPizza = 'Provencale'), (SELECT idIngredient FROM Ingredients WHERE nomIngredient = 'Tomate')),
((SELECT idPizza FROM Pizzas WHERE nomPizza = 'Provencale'), (SELECT idIngredient FROM Ingredients WHERE nomIngredient = 'Mozzarella')),
((SELECT idPizza FROM Pizzas WHERE nomPizza = 'Provencale'), (SELECT idIngredient FROM Ingredients WHERE nomIngredient = 'Olive Noire')),
((SELECT idPizza FROM Pizzas WHERE nomPizza = 'Provencale'), (SELECT idIngredient FROM Ingredients WHERE nomIngredient = 'Poivron'));

-- Basquaise
INSERT INTO IngredientPizza (idPizza, idIngredient) VALUES
((SELECT idPizza FROM Pizzas WHERE nomPizza = 'Basquaise'), (SELECT idIngredient FROM Ingredients WHERE nomIngredient = 'Tomate')),
((SELECT idPizza FROM Pizzas WHERE nomPizza = 'Basquaise'), (SELECT idIngredient FROM Ingredients WHERE nomIngredient = 'Jambon de Bayonne')),
((SELECT idPizza FROM Pizzas WHERE nomPizza = 'Basquaise'), (SELECT idIngredient FROM Ingredients WHERE nomIngredient = 'Piment d'' Espelette')),
((SELECT idPizza FROM Pizzas WHERE nomPizza = 'Basquaise'), (SELECT idIngredient FROM Ingredients WHERE nomIngredient = 'Olive Noire'));

-- Alsacienne
INSERT INTO IngredientPizza (idPizza, idIngredient) VALUES
((SELECT idPizza FROM Pizzas WHERE nomPizza = 'Alsacienne'), (SELECT idIngredient FROM Ingredients WHERE nomIngredient = 'Creme Fraiche')),
((SELECT idPizza FROM Pizzas WHERE nomPizza = 'Alsacienne'), (SELECT idIngredient FROM Ingredients WHERE nomIngredient = 'Mozzarella')),
((SELECT idPizza FROM Pizzas WHERE nomPizza = 'Alsacienne'), (SELECT idIngredient FROM Ingredients WHERE nomIngredient = 'Lardon')),
((SELECT idPizza FROM Pizzas WHERE nomPizza = 'Alsacienne'), (SELECT idIngredient FROM Ingredients WHERE nomIngredient = 'Oignon'));


-- Insertion des données dans la table Tailles
INSERT INTO Tailles (nomTaille, prixMultiplicatif) VALUES
('Petite', 0.66),
('Moyenne', 1),
('Grande', 1.33);

-- Insertion des données dans la table Clients
INSERT INTO Clients (nomClient, prenomClient, adresseClient, solde) VALUES
('Dupont', 'Jean', '1 rue de la Paix, Paris', 50.00),
('Martin', 'Sophie', '15 avenue des Lilas, Lyon', 35.00),
('Durand', 'Pierre', '8 rue de la Republique, Marseille', 75.00);

-- Insertion des données dans la table TypeVehicules
INSERT INTO TypeVehicules (type, distance) VALUES
('Voiture', 100),
('Vélo', 20),
('Moto', 80);

-- Insertion des données dans la table Vehicules
INSERT INTO Vehicules (Marque, nomVehicule, immatriculation, typeVehicule) VALUES
('Toyota', 'Corolla', 'AB-123-CD', 'Voiture'),
('Peugeot', 'VTT', 'EF-456-GH', 'Velo'),
('Honda', 'CBR', 'IJ-789-KL', 'Moto');

-- Insertion des données dans la table Livreurs
INSERT INTO Livreurs (nomLivreur, prenomLivreur, idVehicule) VALUES
('Dubois', 'Philippe', 1),
('Villani', 'Julie', 2),
('Moreau', 'Luc', 3);