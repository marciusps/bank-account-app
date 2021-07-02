package com.example.bank_account_app.model

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.bank_account_app.R
import com.example.bank_account_app.activities.CreateAccActivity
import com.example.bank_account_app.model.AccountDao.readFile
import com.example.bank_account_app.model.Accounts.accountValidator
import com.example.bank_account_app.model.Accounts.readSession
import com.example.bank_account_app.model.Accounts.toSHA256
import com.example.bank_account_app.model.Accounts.updateIDCounter
import com.example.bank_account_app.model.Accounts.writeSession
import com.example.bank_account_app.model.activities.HomeActivity
import com.example.bank_account_app.utils.SharedPreferencesLogin.getLogin
import com.example.bank_account_app.utils.SharedPreferencesLogin.saveLogin
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var et_user_name: EditText
    private lateinit var et_password: EditText
    private lateinit var btn_login: View
    private lateinit var btn_create_acc: View
    private lateinit var progressBar: View

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        et_user_name = findViewById(R.id.et_user_name) as EditText
        et_password = findViewById(R.id.et_password) as EditText
        btn_login = findViewById(R.id.btn_login) as View
        btn_create_acc = findViewById(R.id.btn_create_acc) as View
        progressBar = findViewById(R.id.progressBar) as View

        readFile()
        updateIDCounter()
        readSession()
//        Accounts.accountsList.add(CurrentAccount( Accounts.idCounter++,"marcius_vianna", "123".toSHA256(), Accounts.oppeningDate(),1000))
//        Accounts.accountsList.add(CurrentAccount( Accounts.idCounter++,"danilo_conrado", "123".toSHA256(), Accounts.oppeningDate(),2000))
//        Accounts.accountsList.add(CurrentAccount( Accounts.idCounter++,"cafe_xandynho", "123".toSHA256(), Accounts.oppeningDate(),3000))
//        Accounts.accountsList.add(CurrentAccount( Accounts.idCounter++,"Oi", "Oi".toSHA256(), Accounts.oppeningDate(),999999))
//        AccountDao.writeFile()

        btn_create_acc.setOnClickListener {
            val intent = Intent(this, CreateAccActivity::class.java)
            startActivity(intent)
        }

        if (getLogin(this)[0].isNotBlank() || getLogin(this)[1].isNotBlank()) {
            val intent = Intent(this@MainActivity, HomeActivity::class.java).apply {
                Toast.makeText(
                    this@MainActivity,
                    "Welcome back!!",
                    Toast.LENGTH_SHORT
                ).show()
            }
            startActivity(intent)
            finish()
        } else {
            btn_login.setOnClickListener {
                val user_name = et_user_name.text.toString()
                val password = et_password.text.toString().toSHA256()
                if (user_name != "" || password != "") {
                    if (userPermission(user_name, password)) {
                        changeState(true)
                        delay {
                            val intent = Intent(this@MainActivity, HomeActivity::class.java).apply {
                                val userFound = accountValidator(user_name, password)
                                saveLogin(this@MainActivity, userFound)
                                //Accounts.idSesion = userFound?.accountID ?: 0
                                //writeSession()
                                Toast.makeText(
                                    this@MainActivity,
                                    "Login efetuado com sucesso!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            startActivity(intent)
                            finish()
                        }
                    } else Toast.makeText(
                        this@MainActivity,
                        "Login ou senha inválidos!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun userPermission(user: String, password: String): Boolean {
        Accounts.accountsList.forEach() {
            if (user == it.ownersName && password == it.password)
                return true
        }
        return false
    }

    fun changeState(isLoading: Boolean) {
        if (isLoading) {
            progressBar.visibility = View.VISIBLE
            btn_login.apply {
                isClickable = false
                isFocusable = false
                alpha = 0.5f
            }
        } else {
            progressBar.visibility = View.INVISIBLE
            btn_login.apply {
                isClickable = true
                isFocusable = true
                alpha = 1f
            }
        }
    }

    private fun delay(delay: Long = 1500, action: () -> Unit) {
        Handler(Looper.getMainLooper()).postDelayed(action, delay)
    }
}