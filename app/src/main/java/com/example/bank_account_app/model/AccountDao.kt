package com.example.bank_account_app.model

import android.content.Context
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
                if (row[5] == "current_ac") {
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
                        out.write("${it.accountID};${it.ownersName};${it.password};${it.oppeningDate};${it.accountBalance};current_acc\n")
                    } else
                        out.write("${it.accountID};${it.ownersName};${it.password};${it.oppeningDate};${it.accountBalance};savings_acc\n")
                }
            }
    }

    fun writeUser(account: Account) {
        FileOutputStream(applicationContext().cacheDir.absolutePath + "/accounts.csv", true).bufferedWriter()
            .use { out ->
                if (account is CurrentAccount)
                    out.append("${account.accountID};${account.ownersName};${account.password};${account.oppeningDate};${account.accountBalance};current_acc\n")
                else
                    out.append("${account.accountID};${account.ownersName};${account.password};${account.oppeningDate};${account.accountBalance};savings_acc\n")
            }
    }

    fun rewriteUser(context: Context, account: Account?) {
        val raf = RandomAccessFile(context.cacheDir.absolutePath + "/accounts.csv", "rw")
        var position = 0L
        //pular as linhas e não colunas!!!
        for (i in Accounts.accountsList.indices) {
            if (Accounts.accountsList[i].accountID == account?.accountID) break
            position++
        }
        raf.seek(position)

        if (account is CurrentAccount)
            raf.writeUTF("${account?.accountID};${account?.ownersName};${account?.password};${account?.oppeningDate};${account?.accountBalance};current_acc\n")
        else
            raf.writeUTF("${account?.accountID};${account?.ownersName};${account?.password};${account?.oppeningDate};${account?.accountBalance};savings_acc\n")
    }
}