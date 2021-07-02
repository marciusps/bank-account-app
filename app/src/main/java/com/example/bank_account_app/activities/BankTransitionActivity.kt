package com.example.bank_account_app.activities

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bank_account_app.R
import com.example.bank_account_app.model.AccountDao.writeFile
import com.example.bank_account_app.model.Accounts
import com.example.bank_account_app.model.activities.HomeActivity
import com.example.bank_account_app.utils.SharedPreferencesLogin
import java.text.NumberFormat

class BankTransitionActivity : AppCompatActivity() {
    private lateinit var et_transition_value: EditText
    private lateinit var btn_submit_transition: View
    private lateinit var balance: TextView
    private lateinit var transition_type: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bank_transition)

        et_transition_value = findViewById(R.id.et_transition_value) as EditText
        btn_submit_transition = findViewById(R.id.btn_submit_transition) as View
        balance = findViewById(R.id.balance)
        transition_type = findViewById(R.id.transition_type)

        et_transition_value.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            var current = ""
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(s.toString().isNotBlank()){
                    et_transition_value.removeTextChangedListener(this)
                    val cleanString = s?.replace("""[$,.]""".toRegex(), "") ?: s.toString()
                    val parsed = cleanString.filter { it.isDigit() }.toDouble()
                    val formatted = NumberFormat.getCurrencyInstance().format((parsed / 100))
                    current = formatted
                    et_transition_value.setText(formatted)
                    et_transition_value.setSelection(formatted.length)
                    et_transition_value.addTextChangedListener(this)
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        val user = Accounts.findUser(SharedPreferencesLogin.getLogin(this@BankTransitionActivity))
        balance.text = "R$%.2f".format(user?.accountBalance?.let { Accounts.coinToMoney(it) })


        when (intent?.action) {
            "deposit" -> {
                transition_type.text = "DEPOSIT"
                    btn_submit_transition.setOnClickListener {
                    if (et_transition_value.text.toString() != "") {
                        val intent = Intent(this@BankTransitionActivity, HomeActivity::class.java).apply {
                            val transition = et_transition_value.text.filter { it.isDigit()}.toString().toLong()
                            user?.deposit(transition)
                            writeFile()
                        }
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(
                            this@BankTransitionActivity,
                            "Must enter some value!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
            "withdraw" -> {
                transition_type.text = "WITHDRAW"
                    btn_submit_transition.setOnClickListener {
                    if (et_transition_value.text.toString() != "") {
                        val transition = et_transition_value.text.filter { it.isDigit()}.toString().toLong()
                        if (user?.accountBalance?: 0 - transition >= 0) {
                            val intent = Intent(this@BankTransitionActivity, HomeActivity::class.java).apply {
                                user?.withdraw(transition)
                                writeFile()
                            }
                            startActivity(intent)
                            finish()
                        }else{
                            Toast.makeText(
                                this@BankTransitionActivity,
                                "Cannot withdraw this amount!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            this@BankTransitionActivity,
                            "Must enter some value!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }
}