import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.pizzasio.data.model.PanierItem
import com.example.pizzasio.data.model.Pizza
import com.example.pizzasio.ui.Pizzasio
import com.google.gson.Gson
import org.json.JSONObject

class PostCommandResponse(
    val success: Boolean
    // Ajoutez d'autres champs de réponse si nécessaire
)

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
        // Récupérer l'ID de l'utilisateur depuis les données globales
        val userId = Pizzasio.user.idUser

        // Créer un objet représentant les données à envoyer
        val commandData = JSONObject().apply {
            put("userId", userId)
            put("pizza", panier.panierItems.map { panierItem ->
                JSONObject().apply {
                    put("id", panierItem.idPizza)
                    put("size", panierItem.size)
                    put("price", panierItem.price)
                }
            })
        }

        // URL de votre endpoint API
        val apiUrl = "https://slam.cipecma.net/2224/kaudouin/Api/CommandeToAPI"

        // Création de la requête POST
        val request = JsonObjectRequest(
            Request.Method.POST,
            apiUrl,
            commandData,
            { response ->
                // Gestion de la réponse du serveur
                val postCommandResponse = Gson().fromJson(response.toString(), PostCommandResponse::class.java)
                if (postCommandResponse != null && postCommandResponse.success) {
                    // La commande a été envoyée avec succès
                    // Effectuez les actions nécessaires ici en cas de succès
                } else {
                    // La commande n'a pas été envoyée avec succès
                    // Gérez les erreurs ici
                }
            },
            { error ->
                // Gestion des échecs de la requête
                error.printStackTrace()
            })

        // Ajout de la requête à la file d'attente Volley
        Volley.newRequestQueue(context).add(request)

        Log.i("Coucou", commandData.toString())
    }

    fun removeAllPanierItems() {
        // Supprimer tous les éléments de la liste panierItems
        panier.panierItems.clear()

        // Mettre à jour l'état pour refléter les changements
        panierItemsState.value = emptyList() // Mettre à jour avec une liste vide
    }


}
