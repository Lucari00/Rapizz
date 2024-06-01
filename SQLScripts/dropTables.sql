DROP TABLE IF EXISTS IngredientPizza, PizzaCommande, Commandes, Livreurs, Vehicules, TypeVehicules, Clients, Tailles, Ingredients, Pizzas;
DROP TRIGGER IF EXISTS check_livreur_commande;
DROP PROCEDURE IF EXISTS update_commande_livree;