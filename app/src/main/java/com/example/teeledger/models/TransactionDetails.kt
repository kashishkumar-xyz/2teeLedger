package com.example.teeledger.models

import com.google.gson.annotations.SerializedName

data class TransactionDetails(
    @SerializedName("id") val id: String,
    @SerializedName("date") val date: String,
    @SerializedName("amount") val amount: Long,
    @SerializedName("note") val note: String?
)
