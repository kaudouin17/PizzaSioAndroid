package com.example.pizzasio.ui.pizza

import com.example.pizzasio.data.PizzaCallback
import com.example.pizzasio.data.PizzaDatasource
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ShoppingBasket
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.fragment.app.Fragment
import androidx.tv.material3.Border
import androidx.tv.material3.ExperimentalTvMaterial3Api
import com.example.pizzasio.data.model.Pizza
import com.example.pizzasio.databinding.FragmentHomeBinding
import com.example.pizzasio.ui.theme.PizzaTheme
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.example.pizzasio.R

class PizzaFragment : Fragment() {

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
                        PizzaApp(context = requireContext())
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
    fun PizzaList(pizzas: List<Pizza>, showDialog: Boolean, onShowDialogChange: (Boolean) -> Unit) {
        LazyColumn {
            items(pizzas) { pizza ->
                PizzaCard(
                    pizza = pizza,
                    modifier = Modifier.padding(8.dp),
                    initialShowDialog = showDialog
                )
            }
        }
    }

    @Composable
    fun PizzaApp(context: Context) {
        val pizzaDataSource = PizzaDatasource(context)
        var pizzasState by remember(pizzaDataSource) {
            mutableStateOf<List<Pizza>>(emptyList())
        }
        var showDialog by remember { mutableStateOf(false) }

        LaunchedEffect(key1 = Unit) {
            pizzaDataSource.loadPizza(object : PizzaCallback {
                override fun onDataLoaded(pizzas: List<Pizza>) {
                    pizzasState = pizzas
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
                painter = rememberAsyncImagePainter(R.drawable.decoration_de_pizzerias_pizzart),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillBounds
            )

            // Contenu de l'application
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = Color.Black.copy(alpha = 0.5f) // Opacité réduite pour mieux voir l'image de fond
            ) {
                PizzaList(pizzasState, showDialog) { newShowDialog ->
                    showDialog = newShowDialog
                }
            }
        }
    }


    @Composable
    fun PizzaCard(pizza: Pizza, modifier: Modifier = Modifier, initialShowDialog: Boolean) {
        var showDialog by remember { mutableStateOf(initialShowDialog) }
        var showSizeModal by remember { mutableStateOf(false) }

        OutlinedCard(
            modifier = modifier.padding(8.dp),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, Color.White),
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                Image(
                    painter = rememberAsyncImagePainter(pizza.image),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clickable {
                            showDialog = true
                        },
                    contentScale = ContentScale.Crop
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = pizza.name,
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.DarkGray
                        ),
                        textAlign = TextAlign.Start,
                        modifier = Modifier.padding(start = 8.dp, top = 8.dp)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        imageVector = Icons.Default.ShoppingBasket,
                        contentDescription = "Basket Cart",
                        tint = Color.Black,
                        modifier = Modifier
                            .size(48.dp)
                            .padding(end = 16.dp, top = 16.dp)
                            .clickable {
                                showSizeModal = true
                            }
                    )
                }

                Text(
                    text = pizza.price + "€",
                    style = TextStyle(
                        fontSize = 16.sp,
                        color = Color.DarkGray
                    ),
                    textAlign = TextAlign.Start,
                    modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        if (showDialog) {
            Dialog(
                onDismissRequest = { showDialog = false },
                properties = DialogProperties(
                    dismissOnClickOutside = true
                )
            ) {
                Surface(
                    modifier = Modifier.padding(16.dp),
                    shape = RoundedCornerShape(8.dp),
                    color = Color.White
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = pizza.name,
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                fontSize = 32.sp,
                                color = colorResource(id = R.color.italianGreen)
                            ),
                            modifier = Modifier.padding(vertical = 16.dp)
                        )
                        // Titre "Pâte"
                        Text(
                            text = "Pâte",
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp,
                                color = colorResource(id = R.color.italianGreen)
                            ),
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        Text(
                            text = pizza.idPate, // Ajoutez votre texte pour la pâte ici
                            style = TextStyle(
                                fontSize = 18.sp,
                                color = Color.Black
                            ),
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        Divider(color = colorResource(id = R.color.italianGreen)) // Ligne de séparation verte
                        // Titre "Base"
                        Text(
                            text = "Base",
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp,
                                color = colorResource(id = R.color.italianGreen)
                            ),
                            modifier = Modifier.padding(vertical = 16.dp)
                        )
                        Text(
                            text = pizza.idBase, // Ajoutez votre texte pour la base ici
                            style = TextStyle(
                                fontSize = 18.sp,
                                color = Color.Black
                            ),
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        Divider(color = colorResource(id = R.color.italianGreen)) // Ligne de séparation rouge
                        // Titre "Ingrédients"
                        Text(
                            text = "Ingrédients",
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp,
                                color = colorResource(id = R.color.italianGreen)
                            ),
                            modifier = Modifier.padding(vertical = 16.dp)
                        )
                        Divider(color = colorResource(id = R.color.italianGreen)) // Ligne de séparation jaune
                    }
                }
            }
        }

        if (showSizeModal) {
            SizeModal(
                onDismissRequest = { showSizeModal = false },
                sizes = listOf("S", "M", "L", "XL"),
                pizzaPrice = pizza.price // Passer le prix de la pizza comme paramètre
            )
        }

    }
    @Composable
    fun SizeModal(
        onDismissRequest: () -> Unit,
        sizes: List<String>,
        pizzaPrice: String // Ajouter le prix de la pizza comme paramètre
    ) {
        var selectedSize by remember { mutableStateOf("M") } // Sélectionner la taille "M" par défaut
        var totalPrice by remember { mutableFloatStateOf(pizzaPrice.toFloat()) }


        val sizePrices = mapOf(
            "S" to 0.8, // Multiplicateur de prix pour la taille S
            "M" to 1.0, // Multiplicateur de prix pour la taille M
            "L" to 1.3, // Multiplicateur de prix pour la taille L
            "XL" to 1.7 // Multiplicateur de prix pour la taille XL
        )

        Dialog(
            onDismissRequest = onDismissRequest,
            properties = DialogProperties(
                dismissOnClickOutside = true,
            )
        ) {
            Surface(
                modifier = Modifier.padding(8.dp).width(300.dp), // Ajoutez une marge autour de la modale
                shape = RoundedCornerShape(8.dp),
                color = Color.White
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)// Ajoutez un rembourrage à l'intérieur de la modale
                ) {
                    Text(
                        text = "Sélectionnez une taille",
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = Color.Black
                        ),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    sizes.forEach { size ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .padding(bottom = 4.dp) // Réduisez l'espacement entre chaque élément de la liste
                                .clickable {
                                    selectedSize = size
                                }
                        ) {
                            Text(
                                text = size,
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    color = Color.Black
                                ),
                                modifier = Modifier.width(40.dp) // Ajustez la largeur du texte de la taille
                            )
                            Spacer(modifier = Modifier.width(8.dp)) // Ajoutez de l'espace entre le texte de la taille et le bouton radio
                            RadioButton(
                                selected = size == selectedSize,
                                onClick = {
                                    selectedSize = size
                                    val multiplier = sizePrices[size] ?: 1.0
                                    val totalOriginalPrice = pizzaPrice.toFloat()
                                    totalPrice = (totalOriginalPrice * multiplier).toFloat()
                                },
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = colorResource(id = R.color.italianGreen)
                                ),
                                modifier = Modifier.width(24.dp) // Ajustez la largeur du bouton radio
                            )
                            Spacer(modifier = Modifier.width(8.dp)) // Ajoutez de l'espace entre le bouton radio et la différence de prix
                            val multiplier = sizePrices[size] ?: 1.0
                            val totalOriginalPrice = pizzaPrice.toFloat()
                            val totalNewPrice = totalOriginalPrice * multiplier
                            val difference = totalNewPrice - totalOriginalPrice
                            Text(
                                text = if (size == "M") {
                                    ""
                                } else {
                                    if (size == "S") {
                                        String.format("%.2f", difference) + "€" // Afficher la différence sans le "+" pour la taille "S"
                                    } else {
                                        "+ " + String.format("%.2f", difference) + "€" // Afficher la différence avec le "+" pour les autres tailles
                                    }
                                },
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    color = Color.Black
                                ),
                                modifier = Modifier.weight(1f) // Ajustez la largeur de la différence pour remplir l'espace disponible
                            )
                        }
                    }
                    Button(
                        onClick = {
                            // Gérer la sélection de la taille ici
                            onDismissRequest.invoke()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp), // Ajoutez une marge en haut du bouton
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = colorResource(id = R.color.italianGreen)
                        )
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Valider",
                                color = Color.White
                            )
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Check Icon",
                                tint = Color.White,
                                modifier = Modifier.padding(start = 4.dp)
                            )
                            Text(
                                text = "$totalPrice€",
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}
