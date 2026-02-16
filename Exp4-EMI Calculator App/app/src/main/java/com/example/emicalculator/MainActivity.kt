package com.example.emicalculator

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.pow

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Hide Action Bar for full screen
        supportActionBar?.hide()

        setContentView(R.layout.activity_main)

        val principalEditText = findViewById<EditText>(R.id.principalEditText)
        val rateEditText = findViewById<EditText>(R.id.rateEditText)
        val tenureEditText = findViewById<EditText>(R.id.tenureEditText)
        val calculateButton = findViewById<Button>(R.id.calculateButton)
        val resultTextView = findViewById<TextView>(R.id.resultTextView)

        calculateButton.setOnClickListener {

            val principal = principalEditText.text.toString()
            val rate = rateEditText.text.toString()
            val tenure = tenureEditText.text.toString()

            if (principal.isEmpty() || rate.isEmpty() || tenure.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            try {
                val P = principal.toDouble()
                val annualRate = rate.toDouble()
                val N = tenure.toDouble() * 12
                val R = annualRate / (12 * 100)

                val EMI = (P * R * (1 + R).pow(N)) /
                        ((1 + R).pow(N) - 1)

                resultTextView.text = "Monthly EMI: ₹ %.2f".format(EMI)

            } catch (e: Exception) {
                Toast.makeText(this, "Invalid Input", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
