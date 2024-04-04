import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.pizzasio.data.model.Panier
import com.example.pizzasio.data.model.PanierItem
import com.example.pizzasio.data.model.Pizza
import org.json.JSONArray
import org.json.JSONObject



class PanierDatasource(private val context: Context) {

    private val requestQueue: RequestQueue by lazy {
        Volley.newRequestQueue(context.applicationContext)
    }

    // Méthode pour envoyer les données du panier à l'API
    fun sendPanierData(panierItems: List<PanierItem>, callback: SendPanierCallback) {
        val apiUrl = "https://votre-api.com/endpoint"

        // Créer un JSONArray contenant les données du panier
        val jsonArray = JSONArray()
        for (panierItem in panierItems) {
            val panierItemJson = JSONObject().apply {
                put("idPizza", panierItem.idPizza)
                put("size", panierItem.size)
                put("price", panierItem.price)
                // Ajouter d'autres champs si nécessaire
            }
            jsonArray.put(panierItemJson)
        }

        // Créer un objet JSON contenant le tableau de panier
        val jsonBody = JSONObject().apply {
            put("panierItems", jsonArray)
        }

        // Créer la requête JSON avec la méthode POST
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, apiUrl, jsonBody,
            { response ->
                // Gérer la réponse de l'API en cas de succès
                callback.onSuccess(response.toString())
            },
            { error ->
                // Gérer les erreurs de l'API
                callback.onError(error.message ?: "Erreur inconnue")
            }
        )

        // Ajouter la requête à la file d'attente
        requestQueue.add(jsonObjectRequest)
    }

}

interface SendPanierCallback {
    //fun onDataLoaded(panier: List<PanierItem>)
    fun onError(message: String)
    fun onSuccess(response: String)
}