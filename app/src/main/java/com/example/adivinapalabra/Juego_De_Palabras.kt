package com.example.adivinapalabra

class Juego_De_Palabras() {

    // Lista de palabras disponibles
    private var palabras = listOf(
        "Arrancar", "Patata", "Juego", "Pizarra", "Mando",
        "Coche", "Casa", "Lapiz", "Ventana", "Libro"
    )

    // Lista de pistas (4 tipos)
    private val pistas = listOf(
        "Faltan caracteres",
        "Cambio de vocal",
        "Posición del carácter",
        "Palabra invertida"
    )

    // Puntos iniciales del jugador
    private var puntos = 2
        get() = field
        set(value) { field = value }

    // Constructor secundario (permite pasar lista propia)
    constructor(palabras: ArrayList<String>) : this() {
        this.palabras = palabras
    }

    // Devuelve palabra aleatoria
    fun obtener_palabra(): String {
        return palabras.random()
    }

    // Devuelve la pista correspondiente al tipo
    fun obtener_Pista(num: Int): String {
        return pistas[num]
    }
}
