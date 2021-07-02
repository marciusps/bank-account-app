package com.example.bank_account_app.model

class CurrentAccount(accountID: Int, ownersName: String, password: String, oppeningDate: String, accountBalance: Long)
    : Account(accountID, ownersName, password, oppeningDate,accountBalance)  {


    override fun deposit(insertedMoney: Long) {
        this.accountBalance += insertedMoney
    }

    override fun withdraw(insertedMoney: Long) {
        if(accountBalance - insertedMoney < 0){
            throw Exception("Cannot withdraw this amount")
        }else{
            this.accountBalance -= insertedMoney
        }
    }
}