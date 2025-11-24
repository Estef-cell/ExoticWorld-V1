package com.example.exoticworld.data.repository

import com.example.exoticworld.data.model.Animalitos

class AnimalitosRepository {

    private val animalitos = listOf(
        Animalitos(1, "Tortuga", "Haz click aquí para ver articulos para Tortugas"),
        Animalitos(2, "Loro", "Haz click aquí para ver articulos para Loros"),
        Animalitos(3, "Pez", "Haz click aquí para ver articulos para Peces"),
        Animalitos(4, "Aracnidos", "Haz click aquí para ver articulos para Aracnidos"),
        Animalitos(5, "Mono", "Haz click aquí para ver articulos para Monos")
    )

    fun getAll(): List<Animalitos> = animalitos
    fun getById(id : Int): Animalitos? = animalitos.find{it.id == id}

}