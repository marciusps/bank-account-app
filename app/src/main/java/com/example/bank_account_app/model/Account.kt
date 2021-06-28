package com.example.bank_account_app.model

import java.util.*

abstract class Account(
    val accountNumber: Int,
    val ownersName: String,
    val oppeningDate: Date,
    val password: Int,
    var accountBalance: Long
) {

    fun deposit(insertedMoney: Long) {
        accountBalance+= insertedMoney
    }

    fun withdraw(insertedMoney: Long) {
        if(accountBalance - insertedMoney >= 0 ){
            accountBalance-= insertedMoney
        }else{
            throw Exception("It is not possible to withdraw this amount.")
        }
    }

    fun login (user: String, password: Int): Boolean{
        Accounts.accountsList.forEach() {
            val nomeFormatado = it.ownersName.lowercase().replace(" ", "_")
            if (ownersName == nomeFormatado && (password == it.password)
            )
                return true
        }
        return false
    }
}