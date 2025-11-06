package com.example.adivinapalabra

class Juego_De_Palabras() {

    private var palabras =listOf<String>("Arrancar", "Patata", "Juego","Pizarra")
    private val pistas = listOf<String>("Faltan caracteres", "Cambio de vocal", "Posicion del caracter","Cambio consonante")
    private var puntos = 2

        get() = field
        set(value) {
            field=value
        }

    constructor(palabras: ArrayList<String>):this(){
        this.palabras=palabras
    }

    fun obtener_palabra():String{
        return palabras.random()
    }

    fun obtener_Pista(num:Int): String{
        return pistas.get(num)
    }


}