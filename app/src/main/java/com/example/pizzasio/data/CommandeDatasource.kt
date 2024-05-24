package com.example.pizzasio.data

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.pizzasio.data.model.Commande
import com.example.pizzasio.data.model.PizzaCommande
import com.example.pizzasio.ui.Pizzasio
import org.json.JSONException


class CommandeDatasource(private val context: Context) {

    private val requestQueue: RequestQueue by lazy {
        Volley.newRequestQueue(context.applicationContext)
    }

    // Méthode pour charger les pizzas à partir de l'API
// Méthode pour charger les pizzas à partir de l'API
    fun loadCommande(callback: CommandeCallback) {
        val allCommande = mutableListOf<Commande>()
        val idUser = Pizzasio.user.idUser

        val apiUrl = "https://slam.cipecma.net/2224/kaudouin/Api/CommandeByUserId?user_id=$idUser"
        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, apiUrl, null,
            { response ->
                try {
                    for (i in 0 until response.length()) {
                        val commandeJson = response.getJSONObject(i).getJSONObject("commande")
                        val date = commandeJson.getString("date_commande")
                        val id = commandeJson.getString("id_commande")
                        val total = commandeJson.getString("total_commande")

                        // Récupération des détails de la ligne de commande
                        val ligne_commandeJson = response.getJSONObject(i).getJSONArray("ligne_commande")
                        val ligneCommandeList = mutableListOf<PizzaCommande>()
                        for (j in 0 until ligne_commandeJson.length()) {
                            val ligneCommandeJson = ligne_commandeJson.getJSONObject(j)
                            val id_pizza = ligneCommandeJson.getString("id_pizza")
                            val price_commande = ligneCommandeJson.getString("price_commande")
                            val size_pizza = ligneCommandeJson.getString("size_pizza")
                            val pizza_name = ligneCommandeJson.getString("pizza_name")
                            val ligneCommande = PizzaCommande(id_pizza = id_pizza, price_commande = price_commande, size_pizza = size_pizza, pizza_name = pizza_name)
                            ligneCommandeList.add(ligneCommande)
                        }
                        val commande = Commande(id = id, date = date, total = total, ligneCommande = ligneCommandeList)
                        allCommande.add(commande)
                    }
                    Log.i("Couc", allCommande.toString())
                    // Appeler le callback avec les données chargées
                    callback.onDataLoaded(allCommande)
                } catch (e: JSONException) {
                    // En cas d'erreur de parsing JSON, appeler le callback avec un message d'erreur
                    callback.onError("Erreur de parsing JSON")
                }
            },
            { error ->
                // En cas d'erreur réseau, appeler le callback avec un message d'erreur
                callback.onError("Erreur: ${error.message}")
            }
        )
        // Ajouter la requête à la file d'attente
        requestQueue.add(jsonArrayRequest)
    }

}

// Interface pour gérer les résultats du chargement des pizzas
interface CommandeCallback {
    fun onDataLoaded(allCommande: MutableList<Commande>)
    fun onError(message: String)
}