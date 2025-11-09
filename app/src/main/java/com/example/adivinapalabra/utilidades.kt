package com.example.adivinapalabra

import kotlin.random.Random

// Función de extensión sobre String
fun String.transformar(
    descolocada: Boolean,
    func_cambiar: (caracter: Char, posicion: Int) -> Char
): String {

    // Lista mutable donde construiremos la palabra resultante
    val mutable = mutableListOf<Char>()

    // Si hay que descolocar, generamos un conjunto de índices aleatorios
    val indices = if (descolocada) {
        mutableSetOf<Int>().apply {
            while (this.size < this@transformar.length) {
                add(Random.nextInt(0, this@transformar.length)) // ✅ incluye último índice
            }
        }.toList()
    } else {
        (0 until this.length).toList()
    }

    // Recorremos la palabra aplicando la función lambda a cada carácter
    for (i in indices.indices) {
        val cOriginal = this[indices[i]]
        val cNuevo = func_cambiar(cOriginal, i)
        mutable.add(cNuevo)
    }

    // Convertimos la lista en String final
    return mutable.joinToString("")
}
