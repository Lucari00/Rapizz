SELECT c.idClient, c.nomClient, c.prenomClient, SUM(prixCommande) AS totalDepense
FROM Commandes l
JOIN Clients c ON l.idClient = c.idClient
GROUP BY c.idClient, c.nomClient, c.prenomClient
ORDER BY totalDepense DESC
