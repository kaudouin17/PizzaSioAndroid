package com.example.pizzasio.ui.panier

import PanierDatasource
import SendPanierCallback
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import com.example.pizzasio.R
import com.example.pizzasio.ui.Pizzasio
import com.example.pizzasio.ui.theme.PizzaTheme
import com.google.gson.Gson


val panierViewModel = PanierViewModel()

class PanierFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                PizzaTheme {
                    Surface(modifier = Modifier.fillMaxSize()) {
                        PanierContent()

                    }
                }
            }
        }
    }
}

@Composable
fun PanierContent() {
    val panierValue = panierViewModel.panierItemsState.value
    val isPanierEmpty = panierValue.isEmpty()
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item {
            panierValue.forEach { panierItem ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp), // Ajout du padding horizontal
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val name = panierItem.name
                    val size = panierItem.size
                    val price = panierItem.price

                    Text(
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append(name)
                            }
                            append(" - Taille: $size - $price€")
                        },
                        lineHeight = 15.sp
                    )

                    IconButton(
                        onClick = {
                            panierViewModel.removePizzaFromPanier(panierItem)
                        },
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxHeight()
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_cancel_24),
                            contentDescription = "Supprimer la pizza du panier",
                        )
                    }
                }
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(
                onClick = { panierViewModel.removeAllPanierItems() },
                modifier = Modifier.padding(end = 16.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.baseline_delete_24),
                    contentDescription = "Effacer le panier"
                )
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Button(
            onClick = {
                if (!isPanierEmpty) {

                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black
            ),
            enabled = !isPanierEmpty,
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Text("Commander")
        }
    }
}
