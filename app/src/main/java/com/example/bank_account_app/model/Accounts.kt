package com.example.bank_account_app.model

import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

object Accounts {
    //var idSesion: Int = 0
    var accountsList: ArrayList<Account> = ArrayList()
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

//    fun accountLogout() {
//        idSesion = 0
//        writeSession()
//    }

//    fun readSession(): Int {
//        val bufferedReader =
//            BufferedReader(FileReader(MainApplication.applicationContext().cacheDir.absolutePath + "/session.csv"))
//        val row = bufferedReader.readLine()
//        if (row!=null){
//            return row.toInt()
//        } else
//            writeSession()
//            return 0
//    }

//    fun writeSession() {
//        File(MainApplication.applicationContext().cacheDir.absolutePath + "/session.csv").bufferedWriter()
//            .use { out ->
//                out.write(Accounts.idSesion.toString())
//            }
//    }

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
        val pattern = "dd-MM-yyyy"
        val simpleDateFormat = SimpleDateFormat(pattern)
        return simpleDateFormat.format(Date())
    }

    fun String.toSHA256(): String {
        val messageDigest = MessageDigest.getInstance("SHA-256").digest(toByteArray())
        return messageDigest.fold("", { str, it -> str + "%02x".format(it) })
    }

    fun coinToMoney(coin: Long): Float {
        return coin / 100f
    }

    fun existingAccount(name: String, current: Boolean): Boolean {
        accountsList.forEach {
            if (name == it.ownersName && name.length == it.ownersName.length && it is CurrentAccount && current == true)
                return true
            if (name == it.ownersName && name.length == it.ownersName.length && it is SavingsAccount && current == false)
                return true
        }
        return false
    }
}