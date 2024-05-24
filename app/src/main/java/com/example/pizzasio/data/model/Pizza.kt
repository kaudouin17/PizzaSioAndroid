package com.example.pizzasio.data.model

data class Pizza(
    val name: String,
    val price: String,
    val image: String,
    val id: String,
    val pate: String,
    val base: String,
    val ingredient: MutableList<Ingredient> = mutableListOf()
)

data class Ingredient(
    val name_ing: String
)