package com.example.teeledger.models

import com.google.gson.annotations.SerializedName

data class BalanceSummary(
    @SerializedName("person") val personName: String,
    @SerializedName("balance") val netBalance: Long
)
