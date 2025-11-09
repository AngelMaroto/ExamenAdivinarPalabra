package com.example.adivinapalabra

import android.os.Bundle
import android.os.CountDownTimer
import android.text.method.KeyListener
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.adivinapalabra.databinding.ActivityMainBinding
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    lateinit var mibinding: ActivityMainBinding
    var mireloj: CountDownTimer? = null

    // Variables de estado del juego
    private var euros = 10   // 🔹 Comienza con 10 puntos (antes era 2)
    private var palabraCorrecta = ""
    private var ultimoTipoTransformacion = 0
    private val objpalabras = Juego_De_Palabras()


    // Tiempo total del juego (3 minutos)
    private val TIEMPO_TOTAL = 180000L
    private var relojActivo = false // para evitar reiniciar el reloj

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mibinding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(mibinding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        inicializarComponentes()
    }

    // -------------------------------------------------------------
    // ESTADO INICIAL DE LA APLICACIÓN
    // -------------------------------------------------------------
    private fun inicializarComponentes() {

        // Bloqueamos el EditText (como pide el examen)
        mibinding.adivina.tag = mibinding.adivina.keyListener
        mibinding.adivina.keyListener = null

        mibinding.comprobarPalabra.isEnabled = false
        mibinding.jugar.isEnabled = true
        mibinding.imageView.visibility = View.INVISIBLE
        mibinding.palabraModificada.text = ""
        mibinding.pista.text = ""
        mibinding.tiempo.text = "3:00"
        mibinding.puntos.text = euros.toString()

        // ---------------------------------------------------------
        // BOTÓN "A JUGAR!!"
        // ---------------------------------------------------------
        mibinding.jugar.setOnClickListener {

            // Si el reloj no se ha iniciado todavía, lo lanzamos una vez
            if (!relojActivo) {
                iniciarReloj()
                relojActivo = true
            }

            // Reactivamos el campo para poder escribir
            mibinding.adivina.keyListener = mibinding.adivina.tag as? KeyListener
            mibinding.adivina.setText("")

            mibinding.imageView.visibility = View.INVISIBLE
            mibinding.comprobarPalabra.isEnabled = true
            mibinding.jugar.isEnabled = false

            // Nueva palabra y pista
            val num = Random.nextInt(0, 4)
            ultimoTipoTransformacion = num
            mibinding.pista.text = objpalabras.obtener_Pista(num)

            val palabraOriginal = objpalabras.obtener_palabra()
            palabraCorrecta = palabraOriginal

            val palabraModificada = when (num) {
                0 -> palabraOriginal.transformar(false) { c, _ ->
                    if (Random.nextInt(100) < 40) '_' else c
                }
                1 -> palabraOriginal.transformar(true) { c, _ ->
                    when (c.lowercaseChar()) {
                        'a' -> 'e'
                        'e' -> 'i'
                        'i' -> 'o'
                        'o' -> 'u'
                        'u' -> 'a'
                        else -> c
                    }
                }
                2 -> palabraOriginal.transformar(false) { c, pos ->
                    if (pos % 2 == 0)
                        ('a'.code + (c.lowercaseChar().code - 'a'.code + 1)).toChar()
                    else c
                }
                else -> palabraOriginal.reversed()
            }

            mibinding.palabraModificada.text = palabraModificada
        }

        // ---------------------------------------------------------
        // BOTÓN "COMPROBAR PALABRA"
        // ---------------------------------------------------------
        mibinding.comprobarPalabra.setOnClickListener {
            val respuesta = mibinding.adivina.text.toString()

            if (respuesta.equals(palabraCorrecta, ignoreCase = true)) {
                val ganancia = when (ultimoTipoTransformacion) {
                    0 -> minOf(palabraCorrecta.length / 2, 10)
                    1 -> minOf(palabraCorrecta.length, 10)
                    2 -> minOf(palabraCorrecta.length + 2, 10)
                    else -> 5
                }
                euros += ganancia
                Toast.makeText(this, "¡Correcto! +$ganancia puntos", Toast.LENGTH_SHORT).show()
            } else {
                val perdida = 2
                euros -= perdida        // 🔹 Ahora puede bajar de 0, pero no se recorta a 0
                Toast.makeText(this, "Incorrecto, pierdes $perdida puntos", Toast.LENGTH_SHORT).show()
            }

            mibinding.puntos.text = euros.toString()

// 🔹 Condición de derrota SOLO si baja de 0 (no si llega a 0)
            if (euros < 0) {
                mostrarResultado(false, "😢 HAS PERDIDO")
            } else if (euros >= 50) {
                mostrarResultado(true, "🎉 ¡HAS GANADO!")
            } else {
                mibinding.adivina.setText("")
                mibinding.palabraModificada.text = ""
                mibinding.comprobarPalabra.isEnabled = false
                mibinding.jugar.isEnabled = true
            }

        }
    }

    // -------------------------------------------------------------
    // RELOJ GLOBAL (solo se inicia una vez por partida)
    // -------------------------------------------------------------
    private fun iniciarReloj() {
        mireloj = object : CountDownTimer(TIEMPO_TOTAL, 1000) {
            override fun onFinish() {
                mibinding.tiempo.text = "0:00"
                mostrarResultado(false, "⏰ Se acabó el tiempo")
            }

            override fun onTick(millisUntilFinished: Long) {
                val totalSeconds = (millisUntilFinished / 1000).toInt()
                val minutos = totalSeconds / 60
                val segundos = totalSeconds % 60
                val segundosStr = if (segundos < 10) "0$segundos" else "$segundos"
                mibinding.tiempo.text = "$minutos:$segundosStr"
            }
        }.start()
    }

    // -------------------------------------------------------------
    // FIN DEL JUEGO
    // -------------------------------------------------------------
    private fun mostrarResultado(ganador: Boolean, mensaje: String) {
        mireloj?.cancel()
        relojActivo = false

        mibinding.imageView.setImageResource(
            if (ganador) R.drawable.images else R.drawable.cachis
        )
        mibinding.imageView.visibility = View.VISIBLE
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()

        // Solo reactivamos el botón para volver a jugar manualmente
        mibinding.jugar.isEnabled = true
        mibinding.comprobarPalabra.isEnabled = false
        mibinding.adivina.keyListener = null
    }


    // -------------------------------------------------------------
    // REINICIO A ESTADO INICIAL
    // -------------------------------------------------------------
    private fun reiniciarJuego() {
        euros = 2
        mibinding.adivina.keyListener = null
        mibinding.adivina.setText("")
        mibinding.palabraModificada.text = ""
        mibinding.pista.text = ""
        mibinding.tiempo.text = "3:00"
        mibinding.puntos.text = euros.toString()
        mibinding.imageView.visibility = View.INVISIBLE
        mibinding.comprobarPalabra.isEnabled = false
        mibinding.jugar.isEnabled = true
    }
}
