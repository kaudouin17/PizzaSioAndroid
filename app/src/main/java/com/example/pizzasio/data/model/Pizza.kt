package com.example.pizzasio.data.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class Pizza(
    @StringRes val namePizza: Int,
    @DrawableRes val imagePizza: Int
)
