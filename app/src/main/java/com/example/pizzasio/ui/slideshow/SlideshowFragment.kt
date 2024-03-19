package com.example.pizzasio.ui.slideshow

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.compose.rememberAsyncImagePainter
import com.example.pizzasio.R
import com.example.pizzasio.data.PizzaCallback
import com.example.pizzasio.data.PizzaDatasource
import com.example.pizzasio.data.model.Pizza
import com.example.pizzasio.databinding.FragmentSlideshowBinding

class SlideshowFragment : Fragment() {

    private var _binding: FragmentSlideshowBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        val slideshowViewModel =
                ViewModelProvider(this).get(SlideshowViewModel::class.java)

        _binding = FragmentSlideshowBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textSlideshow
        slideshowViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @Composable
    fun OrderApp(context: Context) {
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
            }
        }
    }