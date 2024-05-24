package com.example.pizzasio.ui.commande

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.fragment.app.Fragment
import coil.compose.rememberAsyncImagePainter
import com.example.pizzasio.R
import com.example.pizzasio.data.CommandeCallback
import com.example.pizzasio.data.CommandeDatasource
import com.example.pizzasio.data.model.Commande
import com.example.pizzasio.databinding.FragmentHomeBinding
import com.example.pizzasio.ui.theme.PizzaTheme
import androidx.compose.material3.Typography

class CommandeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        return ComposeView(requireContext()).apply {
            setContent {
                PizzaTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        CommandeApp(context = requireContext())
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @Composable
    fun CommandeList(commande: List<Commande>, showDialog: Boolean, onShowDialogChange: (Boolean) -> Unit) {
        // Tri des commandes par la plus récente en haut
        val commandesTriees = commande.sortedByDescending { it.date }

        LazyColumn {
            items(commandesTriees) { commande ->
                CommandeCard(
                    commande = commande,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }


    @Composable
    fun CommandeApp(context: Context) {
        val commandeDataSource = CommandeDatasource(context)
        var commandeState by remember(commandeDataSource) {
            mutableStateOf<List<Commande>>(emptyList())
        }
        var showDialog by remember { mutableStateOf(false) }

        LaunchedEffect(key1 = Unit) {
            commandeDataSource.loadCommande(object : CommandeCallback {
                override fun onDataLoaded(allCommande: MutableList<Commande>) {
                    commandeState = allCommande
                }

                override fun onError(message: String) {
                    // Gérer les erreurs de chargement
                }
            })
        }

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // Image de fond
            Image(
                painter = rememberAsyncImagePainter(R.drawable.white_paper_texture_background),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillBounds
            )

            // Contenu de l'application
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = Color.Black.copy(alpha = 0.5f) // Opacité réduite pour mieux voir l'image de fond
            ) {
                CommandeList(commandeState, showDialog) { newShowDialog ->
                    showDialog = newShowDialog
                }
            }
        }
    }

    @Composable
    fun CommandeCard(commande: Commande, modifier: Modifier = Modifier) {
        val allCommande =
        Surface(
            modifier = modifier.fillMaxWidth(), // La carte prend toute la largeur de l'écran
            color = Color.White,
            shape = MaterialTheme.shapes.medium, // Forme arrondie pour la carte
        ) {
            Column(
                modifier = Modifier.padding(16.dp) // Espacement intérieur de la carte
            ) {
                // En-tête de la commande
                Text(
                    text = "Commande ${commande.id}",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                // Date de la commande
                Text(
                    text = "Date: ${commande.date}",
                    style = MaterialTheme.typography.titleLarge, // Style de texte de la date
                    modifier = Modifier.padding(bottom = 4.dp) // Espacement après la date
                )
                // Liste des pizzas commandées
                commande.ligneCommande.forEach { ligneCommande ->
                    Text(
                        text = "${ligneCommande.pizza_name} ${ligneCommande.size_pizza}: ${ligneCommande.price_commande}€",
                        style = MaterialTheme.typography.bodyLarge, // Style de texte pour les détails de la commande
                        modifier = Modifier.padding(bottom = 4.dp) // Espacement entre les lignes de pizza
                    )
                }
                // Total de la commande
                Text(
                    text = "Total: ${commande.total}€",
                    style = MaterialTheme.typography.titleMedium, // Style de texte pour le total
                    modifier = Modifier.padding(top = 8.dp) // Espacement avant le total
                )
            }
        }
    }
}