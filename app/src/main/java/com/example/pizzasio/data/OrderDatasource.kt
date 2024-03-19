package com.example.pizzasio.data

import android.content.Context
import com.example.pizzasio.data.model.Order


class OrderDatasource(private val context: Context) {

    // Méthode pour charger les pizzas à partir de données en dur
    fun loadOrder(callback: OrderCallback) {
        // Créer une liste statique de pizzas
        val allOrder = listOf(
            Order(date = "21/03/2024", total = "10.99", id=""),
            Order(date = "12/09/2023", total = "12.99", id=""),
            // Ajoutez d'autres pizzas ici si nécessaire
        )

        // Appeler le callback avec les données chargées
        callback.onDataLoaded(allOrder)
    }
}

// Interface pour gérer les résultats du chargement des commandes
interface OrderCallback {
    fun onDataLoaded(orders: List<Order>)
    fun onError(message: String)
}