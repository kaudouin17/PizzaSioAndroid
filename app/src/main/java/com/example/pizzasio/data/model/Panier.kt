package com.example.pizzasio.data.model


data class Panier(
    val panierItems: MutableList<PanierItem> = mutableListOf()
)
data class PanierItem(
    val id: Int,
    val name: String,
    val price: String,
    val size: String,
    val idPizza: String
)
