package com.example.exercicio_aula_04

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Build
import androidx.annotation.RequiresApi
import android.widget.EditText
import android.widget.TextView
import android.widget.Button
import android.app.DatePickerDialog
import java.util.Calendar
import java.util.*
import java.time.temporal.ChronoUnit
import java.time.LocalDate
import android.view.View


class MainActivity : AppCompatActivity() {

    val calendar: Calendar = Calendar.getInstance()
    val dia: Int = calendar.get(Calendar.DAY_OF_MONTH)
    val mes: Int = calendar.get(Calendar.MONTH)+1
    val ano: Int = calendar.get(Calendar.YEAR)

    var campoDeTexto1: EditText? = null
    var campoDeTexto3: EditText? = null
    var datepicker: DatePickerDialog? = null
    lateinit var resultado: TextView
    lateinit var calcular: Button
    var diasRestantes : Long = 0

    var frase = mutableListOf<String>()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        campoDeTexto1 = findViewById(R.id.edtNome)
        val campoDeTexto2: EditText = findViewById(R.id.edtDataNascimento)
        campoDeTexto3 = findViewById(R.id.edtPresente)
        resultado = findViewById(R.id.txtResultado)
        calcular = findViewById(R.id.btnCalcular)

        fun calcularDias(dia : Int, mes : Int): Long {
            val cldr = Calendar.getInstance() // Obtém data atual
            // Abaixo, foram isoladas em variáveis de dia, mes e ano referentes à data atual.
            val day = cldr[Calendar.DAY_OF_MONTH]
            val month = cldr[Calendar.MONTH]+1 // Aqui foi somado "+1" porque nesse caso começa a contar do zero, então janeiro é o mes 0.
            var year = cldr[Calendar.YEAR]
            var ano = year // Essa variável sera usada pra manipular a data de aniversário.
            if ((month > mes+1) || (month == mes+1 && day > dia)) {ano = year + 1} // Este comando é para comparar as datas por cada parte (dia, mês e ano). A tradução é "O mês atual é maior que o mês declarado no calendário? Ou, se o mês atual é igual ao do calendário, o dia atual é maior que o dia declarado no calendário? Se a resposta for sim para ambas as perguntas, então o ano do calendário é igual ao ano seguinte", ou seja, a data já pasou e não será calculada.

            val today = LocalDate.of(year, month, day) // Essa classe facilita o cálculo de datas.
            val birthday = LocalDate.of(ano, mes+1, dia) // Aqui foi usada a variável "ano" que está na data atual e soma-se "+1" no mês pelo mesmo motivo já descrito. Assim, é feita a conta: (dia-mes-ano de HOJE) - (dia-mes-ano do ANIVERSÁRIO).
            return ChronoUnit.DAYS.between(today, birthday)
        }

        campoDeTexto2.setOnClickListener { datepicker = DatePickerDialog(this@MainActivity,
            { view, ano, monthOfYear, dayOfMonth -> campoDeTexto2.setText(dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + ano)
                diasRestantes = calcularDias(dayOfMonth, monthOfYear) }, ano, mes, dia)
            datepicker!!.show()
        }

        calcular.setOnClickListener {

            val nomeInserido = campoDeTexto1?.text.toString()
            val dataNascimentoInserido = campoDeTexto2?.text.toString()
            val presenteInserido = campoDeTexto3?.text.toString()
            var x = ""

            fun listaStrings(X: String) {
                var exibir = ""
                frase.add(X)
                resultado.visibility = View.VISIBLE
                    for(y in frase) {
                        exibir += y
                    }
                resultado.text = exibir
            }

            when (nomeInserido) {
                null -> campoDeTexto1?.error = "Esse nome não é válido!"
                "" -> campoDeTexto1?.error = "Esse nome não é válido!"
                else -> {
                    when (diasRestantes) {
                        null -> campoDeTexto2?.error = "Essa data não é válida!"
                        else -> {
                            when (presenteInserido) {
                                null -> campoDeTexto3?.error = "Esse presente não é válido!"
                                "" -> campoDeTexto3?.error = "Esse presente não é válido!"
                                else -> {
                                  listaStrings(X = "Olá $nomeInserido, faltam $diasRestantes dias para o seu aniversário! Espero que você ganhe um $presenteInserido! \n")
                                }
                            }
                        }
                    }
                }
            }
        }

    }
}