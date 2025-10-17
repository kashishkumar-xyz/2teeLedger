package com.example.teeledger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.teeledger.models.TransactionDetails
import com.example.teeledger.viewmodel.TransactionHistoryViewModel

class TransactionHistoryActivity : AppCompatActivity() {

    private val viewModel: TransactionHistoryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction_history)

        val personName = intent.getStringExtra("person_name") ?: ""

        val recyclerView = findViewById<RecyclerView>(R.id.transactions_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        viewModel.getTransactionHistory(personName).observe(this) { transactions ->
            recyclerView.adapter = TransactionAdapter(transactions)
        }
    }
}

class TransactionAdapter(private val transactions: List<TransactionDetails>) : RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val amount: TextView = view.findViewById(android.R.id.text1)
        val note: TextView = view.findViewById(android.R.id.text2)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_2, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val transaction = transactions[position]
        holder.amount.text = transaction.amount.toString()
        holder.note.text = transaction.note
    }

    override fun getItemCount() = transactions.size
}
