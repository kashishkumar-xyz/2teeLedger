package com.example.teeledger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import com.example.teeledger.viewmodel.AddTransactionViewModel

class AddTransactionActivity : AppCompatActivity() {

    private val viewModel: AddTransactionViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_transaction)

        val personNameInput = findViewById<EditText>(R.id.person_name_input)
        val amountInput = findViewById<EditText>(R.id.amount_input)
        val noteInput = findViewById<EditText>(R.id.note_input)
        val saveButton = findViewById<Button>(R.id.save_button)

        saveButton.setOnClickListener {
            val personName = personNameInput.text.toString()
            val amount = amountInput.text.toString().toLongOrNull() ?: 0
            val note = noteInput.text.toString()

            if (viewModel.addTransaction(personName, amount, note)) {
                Toast.makeText(this, "Transaction saved", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Invalid input", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
