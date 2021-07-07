package com.example.bank_account_app.model

import com.example.bank_account_app.R
import com.example.bank_account_app.utils.MainApplication.Companion.applicationContext
import java.io.*

object AccountDao {

    fun readFile(): ArrayList<Account> {
        Accounts.accountsList = ArrayList()
        return Accounts.accountsList.also {
            val bufferedReader =
                BufferedReader(FileReader(applicationContext().cacheDir.absolutePath + "/accounts.csv"))
            var row: List<String>
            while (bufferedReader.ready()) {
                row = bufferedReader.readLine().split(";")
                if (row[5] == applicationContext().getString(R.string.current_acc)) {
                    //accountID, ownersName, password, oppeningDate, accountBalance
                    Accounts.accountsList.add(
                        CurrentAccount(
                            row[0].toInt(),
                            row[1],
                            row[2],
                            row[3],
                            row[4].toLong()
                        )
                    )
                } else {
                    Accounts.accountsList.add(
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

    fun writeFile() {
        File(applicationContext().cacheDir.absolutePath + "/accounts.csv").bufferedWriter()
            .use { out ->
                Accounts.accountsList.forEach {
                    if (it is CurrentAccount) {
                        out.write("${it.accountID};${it.ownersName};${it.password};${it.oppeningDate};${it.accountBalance};${applicationContext().getString(R.string.current_acc)}\n")
                    } else
                        out.write("${it.accountID};${it.ownersName};${it.password};${it.oppeningDate};${it.accountBalance};${applicationContext().getString(R.string.savings_acc)}\n")
                }
            }
    }

    fun writeUser(account: Account) {
        FileOutputStream(applicationContext().cacheDir.absolutePath + "/accounts.csv", true).bufferedWriter()
            .use { out ->
                if (account is CurrentAccount)
                    out.append("${account.accountID};${account.ownersName};${account.password};${account.oppeningDate};${account.accountBalance};${applicationContext().getString(R.string.current_acc)}\n")
                else
                    out.append("${account.accountID};${account.ownersName};${account.password};${account.oppeningDate};${account.accountBalance};${applicationContext().getString(R.string.savings_acc)}\n")
            }
    }
}