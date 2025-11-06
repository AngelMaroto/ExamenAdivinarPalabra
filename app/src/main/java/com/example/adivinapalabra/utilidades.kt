package com.example.adivinapalabra

import kotlin.random.Random
import kotlin.random.nextUInt

fun String.transformar(descolocado: Boolean, func_cambiar:(caracter:Char, a: Int)->Char): String{

    var palabramodificada =""
    var palabrafinal=""

    if(descolocado){
        //conjunto que guarda las posiciones a cambiar
        var conjunto= mutableSetOf<Int>()
        var num_aleatorio: Int

        //descolocamos la original
        while(conjunto.size<this.length){
            //genero un numero aleatorio entre 0 y la longitud de la cadena menos uno
            num_aleatorio=Random.nextInt(0,this.length-1)

            //añado el numero al conjunto
            conjunto.add(num_aleatorio)
        }
        //descolocamos la palabra
        //recorro las posiciones de la palabra
        var pos: Int
        for (pos in 0..this.length-1){
            palabramodificada=palabramodificada+this.get(conjunto.elementAt(pos)).toString()
        }
    }
    else{
        palabramodificada=this
    }
    //transformar la palabra y por cada caracter se invoca a la func_cambiar

    for (i in 0..this.length-1){
        palabrafinal=palabrafinal+func_cambiar(palabramodificada.get(i),i).toString()
    }

    return palabrafinal
}