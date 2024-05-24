package com.example.pizzasio.data.model

data class Commande(
    val id: String,
    val date: String,
    val total: String,
    val ligneCommande: MutableList<PizzaCommande> = mutableListOf()

)

// Modèle de données pour une ligne de commande
data class PizzaCommande(
    val id_pizza: String,
    val price_commande: String,
    val size_pizza: String,
    val pizza_name: String
)