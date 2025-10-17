package com.example.teeledger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.teeledger.models.BalanceSummary
import com.example.teeledger.viewmodel.LedgerViewModel

import com.google.android.material.floatingactionbutton.FloatingActionButton

import android.widget.ProgressBar

import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private val viewModel: LedgerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.balances_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val progressBar = findViewById<ProgressBar>(R.id.progress_bar) // Get reference to ProgressBar

        viewModel.isLoading.observe(this) { isLoading ->
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.errorMessage.observe(this) { errorMessage ->
            errorMessage?.let {
                Snackbar.make(findViewById(android.R.id.content), it, Snackbar.LENGTH_LONG).show()
            }
        }

        viewModel.getBalances().observe(this) { balances ->
            recyclerView.adapter = BalanceAdapter(balances)
        }

        val fab = findViewById<FloatingActionButton>(R.id.add_transaction_fab)
        fab.setOnClickListener {
            startActivity(Intent(this, AddTransactionActivity::class.java))
        }

        (recyclerView.adapter as BalanceAdapter).onItemClick = { personName ->
            val intent = Intent(this, TransactionHistoryActivity::class.java)
            intent.putExtra("person_name", personName)
            startActivity(intent)
        }
    }

class BalanceAdapter(private val balances: List<BalanceSummary>) : RecyclerView.Adapter<BalanceAdapter.ViewHolder>() {

    var onItemClick: ((String) -> Unit)? = null

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val personName: TextView = view.findViewById(android.R.id.text1)
        val balance: TextView = view.findViewById(android.R.id.text2)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_2, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val balance = balances[position]
        holder.personName.text = balance.personName
        holder.balance.text = balance.netBalance.toString()
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(balance.personName)
        }
    }

    override fun getItemCount() = balances.size
}
