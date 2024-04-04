package com.example.pizzasio.ui

import android.app.Application
import com.example.pizzasio.data.model.Panier
import com.example.pizzasio.data.model.User

class Pizzasio() : Application() {
    companion object {
        var test: String = "default"

        //var user : User = User()
        var panier: Panier = Panier()
    }
}