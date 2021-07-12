package com.example.bank_account_app.model

import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

object Accounts {
    var accountsList: ArrayList<Account> = ArrayList()
    var statementsList: ArrayList<String> = ArrayList()
    var operationList: ArrayList<String> = ArrayList()
    var idCounter: Int = 1

    fun updateIDCounter() {
        idCounter = accountsList.size
    }

    fun updatedID(): Int {
        var newID: Int = -1
        accountsList.forEach {
            if (it.accountID > newID)
                newID = it.accountID
        }
        return newID + 1
    }

    fun accountValidator(name: String, password: String): Account? {
        accountsList.forEach() {
            if (name == it.ownersName && password == it.password) {
                return it
            }
        }
        return null
    }

    fun findUser(idRow: ArrayList<String>): Account? {
        accountsList.forEach {
            if (idRow[0].toInt() == it.accountID)
                return it
        }
        return null
    }

    fun oppeningDate(): String {
        val pattern = "dd MMM"
        val simpleDateFormat = SimpleDateFormat(pattern)
        return simpleDateFormat.format(Date()).uppercase()
    }

    fun String.toSHA256(): String {
        val messageDigest = MessageDigest.getInstance("SHA-256").digest(toByteArray())
        return messageDigest.fold("", { str, it -> str + "%02x".format(it) })
    }

    fun coinToMoney(coin: Long): String {
        return "R$%.2f".format(coin / 100f)
    }

    fun accountFinder(name: String, current: Boolean): Account?{
        accountsList.forEach {
            if (name == it.ownersName && name.length == it.ownersName.length && it is CurrentAccount && current == true)
                return it
            if (name == it.ownersName && name.length == it.ownersName.length && it is SavingsAccount && current == false)
                return it
        }
        return null
    }

    fun loginValidation(name: String, password: String, current: Boolean): Boolean{
        accountsList.forEach {
            if (name == it.ownersName && password == it.password && name.length == it.ownersName.length && it is CurrentAccount && current == true)
                return true
            if (name == it.ownersName && password == it.password && name.length == it.ownersName.length && it is SavingsAccount && current == false)
                return true
        }
        return false
    }
}