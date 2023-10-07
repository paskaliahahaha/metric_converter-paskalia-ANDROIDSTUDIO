package com.example.metricconverter

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var metricSpinner: Spinner
    private lateinit var fromUnitSpinner: Spinner
    private lateinit var toUnitSpinner: Spinner
    private lateinit var valueEditText: EditText
    private lateinit var resultTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        metricSpinner = findViewById(R.id.metricSpinner)
        fromUnitSpinner = findViewById(R.id.fromUnitSpinner)
        toUnitSpinner = findViewById(R.id.toUnitSpinner)
        valueEditText = findViewById(R.id.valueEditText)
        resultTextView = findViewById(R.id.resultTextView)

        val metrics = arrayOf("Panjang", "Massa", "Waktu", "Arus Listrik", "Suhu")
        val metricAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, metrics)
        metricAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        metricSpinner.adapter = metricAdapter

        val emptyAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, emptyArray())
        emptyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        fromUnitSpinner.adapter = emptyAdapter
        toUnitSpinner.adapter = emptyAdapter

        metricSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedMetric = metrics[position]
                val units = getUnitsForMetric(selectedMetric)

                val unitAdapter = ArrayAdapter<String>(this@MainActivity, android.R.layout.simple_spinner_item, units)
                unitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                fromUnitSpinner.adapter = unitAdapter
                toUnitSpinner.adapter = unitAdapter
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        findViewById<View>(R.id.convertButton).setOnClickListener {
            convert()
        }
    }

    private fun getUnitsForMetric(metric: String): Array<String> {
        return when (metric) {
            "Panjang" -> arrayOf("Meter", "Centimeter", "Kilometer")
            "Massa" -> arrayOf("Kilogram", "Gram", "Miligram")
            "Waktu" -> arrayOf("Detik", "Menit", "Jam")
            "Arus Listrik" -> arrayOf("Ampere", "Miliampere")
            "Suhu" -> arrayOf("Celsius", "Fahrenheit", "Kelvin")
            else -> emptyArray()
        }
    }

    private fun convert() {
        val fromUnit = fromUnitSpinner.selectedItem.toString()
        val toUnit = toUnitSpinner.selectedItem.toString()
        val value = valueEditText.text.toString().toDoubleOrNull()

        if (value != null) {
            val result = performConversion(fromUnit, toUnit, value)
            resultTextView.text = result.toString()
        } else {
            resultTextView.text = "Masukkan nilai yang valid"
        }
    }

    private fun performConversion(fromMetric: String, toMetric: String, value: Double): Double {
        return when {

            fromMetric == "Meter" && toMetric == "Centimeter" -> value * 100.0
            fromMetric == "Centimeter" && toMetric == "Meter" -> value / 100.0
            fromMetric == "Meter" && toMetric == "Kilometer" -> value / 1000.0
            fromMetric == "Kilometer" && toMetric == "Meter" -> value * 1000.0


            fromMetric == "Kilogram" && toMetric == "Gram" -> value * 1000.0
            fromMetric == "Gram" && toMetric == "Kilogram" -> value / 1000.0


            fromMetric == "Detik" && toMetric == "Menit" -> value / 60.0
            fromMetric == "Menit" && toMetric == "Detik" -> value * 60.0


            fromMetric == "Ampere" && toMetric == "Miliampere" -> value * 1000.0
            fromMetric == "Miliampere" && toMetric == "Ampere" -> value / 1000.0


            fromMetric == "Kelvin" && toMetric == "Celsius" -> value - 273.15
            fromMetric == "Celsius" && toMetric == "Kelvin" -> value + 273.15
            fromMetric == "Celsius" && toMetric == "Fahrenheit" -> (value * 9 / 5) + 32
            fromMetric == "Fahrenheit" && toMetric == "Celsius" -> (value - 32) * 5 / 9

            else -> value
        }
    }
}
