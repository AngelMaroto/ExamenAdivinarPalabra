package com.example.adivinapalabra

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.adivinapalabra.databinding.ActivityMainBinding
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    lateinit var mibinding: ActivityMainBinding
    lateinit var mireloj: CountDownTimer
    var minutos=3
    var segundos=59
    var objpalabras = Juego_De_Palabras()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mibinding= ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(mibinding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        inicializarComponentes()
        inicializarReloj()
    }

    private fun inicializarReloj() {
        //Instancio el objeto
        mireloj=object: CountDownTimer(180000,1000){
            override fun onFinish() {
                TODO("Not yet implemented")
            }

            override fun onTick(millisUntilFinished: Long) {
                segundos--
                if (segundos>0)
                    segundos=59
                    minutos--
                mibinding.tiempo.text=
            }

        }
    }

    private fun inicializarComponentes(){
        //Definimos el listener para el boton comenzar juego
        mibinding.jugar.setOnClickListener {
            //habilitamos el edittext
            mibinding.adivina.isEnabled=true
            mibinding.comprobarPalabra.isEnabled=true
            mibinding.jugar.isEnabled=false
            mibinding.imageView.visibility= View.INVISIBLE
            //mostramos la palabra transformada
            //generamos un numero aleatorio entre 0y 3 para decidir la transformacion
            var num=Random.nextInt(0,3)
            //mostramos la pista
            mibinding.pista.text=objpalabras.obtener_Pista(num)

            var palabracambaida: String=""
            when(num){
                //transformacion faltan caracteres
                0->
                    palabracambaida=objpalabras.obtener_palabra().transformar(Random.nextBoolean()){c, pos->
                        if (Random.nextBoolean())
                        {
                            '_'
                        }
                        else
                            c
                    }
                1->
                    palabracambaida=objpalabras.obtener_palabra().transformar(true){c, pos->
                        if (c == 'a')
                            'e'
                            else if (c == 'e')
                                'i'
                                else if(c == 'i')
                                    'o'
                                    else if(c == 'o')
                                        'u'
                                        else if(c == 'u')
                                            'a'
                        else
                            c
                    }
                2->
                    palabracambaida=objpalabras.obtener_palabra().transformar(false){c, pos->
                        if (pos%2==0)
                            (c.lowercaseChar().code-'a'.code+1) as Char
                        else
                            c
                    }
            }
        }
    }
}