SELECT c.idClient, c.nomClient, c.prenomClient, SUM(co.prixCommande) AS montantTotal
FROM Clients c
JOIN Commandes co ON c.idClient = co.idClient
GROUP BY c.idClient, c.nomClient, c.prenomClient
ORDER BY montantTotal DESC
LIMIT 5;
