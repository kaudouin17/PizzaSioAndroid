package com.example.pizzasio.ui.commandes

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.fragment.app.Fragment
import coil.compose.rememberAsyncImagePainter
import com.example.pizzasio.R
import com.example.pizzasio.data.OrderCallback
import com.example.pizzasio.data.OrderDatasource
import com.example.pizzasio.data.model.Order
import com.example.pizzasio.data.model.Pizza
import com.example.pizzasio.databinding.FragmentHomeBinding
import com.example.pizzasio.ui.theme.PizzaTheme

class OrderFragment : Fragment() {

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
                        OrderApp(context = requireContext())
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
    fun OrderList(order: List<Order>, showDialog: Boolean, onShowDialogChange: (Boolean) -> Unit) {
        LazyColumn {
            items(order) { order ->
                OrderCard(
                    order = order,
                    modifier = Modifier.padding(8.dp),
                )
            }
        }
    }

    @Composable
    fun OrderApp(context: Context) {
        val orderDataSource = OrderDatasource(context)
        var orderState by remember(orderDataSource) {
            mutableStateOf<List<Order>>(emptyList())
        }
        var showDialog by remember { mutableStateOf(false) }

        LaunchedEffect(key1 = Unit) {
            orderDataSource.loadOrder(object : OrderCallback {
                override fun onDataLoaded(orders: List<Order>) {
                    orderState = orders
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
                OrderList(orderState.value, showDialog) { newShowDialog ->
                    showDialog = newShowDialog
                }
            }
        }
    }

    @Composable
    fun OrderCard(order: Order, modifier: Modifier = Modifier) {
        Surface(
            modifier = modifier,
            color = Color.White
        ) {
            Box(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Date: ${order.date}",
                )
                Text(
                    text = "Total: ${order.total}",
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}