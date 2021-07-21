package com.example.bank_account_app.util

import com.example.bank_account_app.R
import com.example.bank_account_app.model.Account
import com.example.bank_account_app.model.CurrentAccount
import com.example.bank_account_app.model.SavingsAccount
import com.example.bank_account_app.util.MainApplication.Companion.applicationContext
import java.io.BufferedReader
import java.io.File
import java.io.FileOutputStream
import java.io.FileReader

const val accounts: String = "accounts"

object AccountDao {

    fun readFile(): ArrayList<Account> {

        AccountManager.accountsList = ArrayList()
        if (!File(applicationContext().cacheDir.absolutePath + "/${accounts}.csv").exists())
            File(applicationContext().cacheDir.absolutePath + "/${accounts}.csv").createNewFile()

        return AccountManager.accountsList.also {
            val bufferedReader =
                BufferedReader(FileReader(applicationContext().cacheDir.absolutePath + "/${accounts}.csv"))
            var row: List<String>
            while (bufferedReader.ready()) {
                row = bufferedReader.readLine().split(";")
                if (row[5] == applicationContext().getString(R.string.current_acc)) {
                    //accountID, ownersName, password, oppeningDate, accountBalance
                    AccountManager.accountsList.add(
                        CurrentAccount(
                            row[0].toInt(),
                            row[1],
                            row[2],
                            row[3],
                            row[4].toLong()
                        )
                    )
                } else {
                    AccountManager.accountsList.add(
                        SavingsAccount(
                            row[0].toInt(),
                            row[1],
                            row[2],
                            row[3],
                            row[4].toLong()
                        )
                    )
                }
            }
        }
    }

    fun daleContas(): String {
        for (i in 1..10000) {
            if (i % 2 == 0) {
                AccountManager.accountsList.add(
                    CurrentAccount(
                        AccountManager.updatedID(), "${AccountManager.updatedID()}", "${AccountManager.updatedID()}",
                        AccountManager.oppeningDate(), (100L..10000L).random()
                    )
                )
            } else
                AccountManager.accountsList.add(
                    SavingsAccount(
                        AccountManager.updatedID(), "${AccountManager.updatedID()}", "${AccountManager.updatedID()}",
                        AccountManager.oppeningDate(), (100L..10000L).random()
                    )
                )

        }
        writeFile()
        AccountManager.coroutine = true
        return "generated accounts"
    }

    fun writeFile() {
        File(applicationContext().cacheDir.absolutePath + "/${accounts}.csv").bufferedWriter()
            .use { out ->
                AccountManager.accountsList.forEach {
                    if (it is CurrentAccount) {
                        out.write(
                            "${it.accountID};${it.ownersName};${it.password};${it.oppeningDate};${it.accountBalance};${
                                applicationContext().getString(
                                    R.string.current_acc
                                )
                            }\n"
                        )
                    } else
                        out.write(
                            "${it.accountID};${it.ownersName};${it.password};${it.oppeningDate};${it.accountBalance};${
                                applicationContext().getString(
                                    R.string.savings_acc
                                )
                            }\n"
                        )
                }
            }
    }

    fun writeUser(account: Account) {
        FileOutputStream(
            applicationContext().cacheDir.absolutePath + "/${accounts}.csv",
            true
        ).bufferedWriter()
            .use { out ->
                if (account is CurrentAccount)
                    out.append(
                        "${account.accountID};${account.ownersName};${account.password};${account.oppeningDate};${account.accountBalance};${
                            applicationContext().getString(
                                R.string.current_acc
                            )
                        }\n"
                    )
                else
                    out.append(
                        "${account.accountID};${account.ownersName};${account.password};${account.oppeningDate};${account.accountBalance};${
                            applicationContext().getString(
                                R.string.savings_acc
                            )
                        }\n"
                    )
            }
    }

    fun readStatements(id: Int): ArrayList<String> {
        AccountManager.statementsList = ArrayList()
        return AccountManager.statementsList.also {
            if (!File(applicationContext().cacheDir.absolutePath + "/${id}.csv").exists()) {
                File(applicationContext().cacheDir.absolutePath + "/${id}.csv").createNewFile()
            }
            val bufferedReader =
                BufferedReader(FileReader(applicationContext().cacheDir.absolutePath + "/${id}.csv"))
            var row: String
            while (bufferedReader.ready()) {
                row = bufferedReader.readLine()
                AccountManager.statementsList.add(row)
            }
        }
    }

    fun writeStatement(id: Int, statement: String) {
        FileOutputStream(
            applicationContext().cacheDir.absolutePath + "/${id}.csv",
            true
        ).bufferedWriter()
            .use { out ->
                out.write("${statement}\n")
            }
    }

    fun fillMenu() {
        AccountManager.menuList = arrayListOf("deposit", "withdraw", "transfer", "statement", "+10k")
    }
}