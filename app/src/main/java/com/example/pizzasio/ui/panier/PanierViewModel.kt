package com.example.pizzasio.ui.panier

import PanierDatasource
import SendPanierCallback
import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.pizzasio.data.model.PanierItem
import com.example.pizzasio.data.model.Pizza
import com.example.pizzasio.ui.Pizzasio
import com.google.gson.Gson

class PanierViewModel: ViewModel() {
    private val panier = Pizzasio.panier
    val panierItemsState = mutableStateOf(getAllCartItemsAsList())

    fun getAllCartItemsAsList(): List<PanierItem> {
        return panier.panierItems.toList()
    }

    fun addPizzaToPanier(pizza: Pizza, size: String) {
        val totalPrice = calculateTotalPrice(pizza.price, size)
        panier.panierItems.add(PanierItem(panier.panierItems.count(), pizza.name, totalPrice, size, pizza.id))
        panierItemsState.value = getAllCartItemsAsList()
    }

    private fun calculateTotalPrice(basePrice: String, size: String): String {
        val sizePrices = mapOf(
            "S" to 0.8, // Multiplicateur de prix pour la taille S
            "M" to 1.0, // Multiplicateur de prix pour la taille M
            "L" to 1.3, // Multiplicateur de prix pour la taille L
            "XL" to 1.7 // Multiplicateur de prix pour la taille XL
        )
        val multiplier = sizePrices[size] ?: 1.0
        val totalOriginalPrice = basePrice.toFloat()
        val totalPrice = totalOriginalPrice * multiplier
        return String.format("%.2f", totalPrice)
    }

    fun removePizzaFromPanier(panierItem: PanierItem) {
        panier.panierItems.removeIf { it.id == panierItem.id }
        panierItemsState.value = getAllCartItemsAsList()
        Log.i("Panier", Gson().toJson(Pizzasio.panier))
    }

    fun postCommandToAPI(context: Context) {
        val panierDatasource = PanierDatasource(context)
        val panierItems = panierItemsState.value // Obtenez les panierItems actuels
        val callback = object : SendPanierCallback {
            override fun onError(message: String) {
                // Gérer les erreurs lors de l'envoi du panier à l'API
            }

            override fun onSuccess(response: String) {
                // Gérer le succès de l'envoi du panier à l'API
            }
        }
        panierDatasource.sendPanierData(panierItems, callback)
    }
    fun removeAllPanierItems() {
        panier.panierItems.clear()
        panierItemsState.value = emptyList()
    }



}