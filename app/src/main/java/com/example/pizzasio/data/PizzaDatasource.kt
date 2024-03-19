package com.example.pizzasio.data

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.pizzasio.data.model.Pizza
import org.json.JSONException

class PizzaDatasource(private val context: Context) {

    private val requestQueue: RequestQueue by lazy {
        Volley.newRequestQueue(context.applicationContext)
    }

    // Méthode pour charger les pizzas à partir de l'API
    fun loadPizza(callback: PizzaCallback) {
        val allPizza = mutableListOf<Pizza>()

        val apiUrl = "https://slam.cipecma.net/jsabbah/Api/AllPizza"
        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, apiUrl, null,
            { response ->
                try {
                    for (i in 0 until response.length()) {
                        val pizzaJson = response.getJSONObject(i)
                        val name = pizzaJson.getString("name")
                        val price = pizzaJson.getString("price")
                        val image = pizzaJson.getString("img_url")
                        val id = pizzaJson.getString("id")
                        val idPate = pizzaJson.getString("dough")
                        val idBase = pizzaJson.getString("base")
                        // Créer un objet Pizza avec les détails extraits
                        val pizza = Pizza(name = name, price = price, image = image, id = id, idPate = idPate, idBase = idBase)
                        // Ajouter l'objet Pizza à la liste
                        allPizza.add(pizza)
                    }
                    // Appeler le callback avec les données chargées
                    callback.onDataLoaded(allPizza)
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
interface PizzaCallback {
    fun onDataLoaded(pizzas: List<Pizza>)
    fun onError(message: String)
}
