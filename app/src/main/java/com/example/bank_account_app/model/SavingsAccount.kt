package com.example.bank_account_app.model

import java.util.*

class SavingsAccount(
    accountNumber: Int, ownersName: String, oppeningDate: Date, password: Int,
    accountBalance: Long
) : Account(accountNumber, ownersName, oppeningDate, password, accountBalance) {
}