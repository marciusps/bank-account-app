package com.example.bank_account_app.model

abstract class Account (
    val accountID: Int, var ownersName: String, var password: String, var oppeningDate: String,
    var accountBalance: Long){

    abstract fun deposit(insertedMoney: Long)

    abstract fun withdraw(insertedMoney: Long)


    override fun toString(): String {
        return "Account(accountID= $accountID, ownersName= '$ownersName', password= '$password', " +
                "oppeningDate= $oppeningDate, accountBalance= R$${accountBalance / 100})"
    }
}