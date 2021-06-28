package com.example.bank_account_app.model

import java.util.*

abstract class Account(
    val accountNumber: Int,
    val ownersName: String,
    val oppeningDate: Date,
    val password: Int,
    val accountBalance: Float
) {

    fun deposit(insertedMoney: Float) {
        accountBalance+= insertedMoney
    }

    fun withdraw(insertedMoney: Float) {

    }
}