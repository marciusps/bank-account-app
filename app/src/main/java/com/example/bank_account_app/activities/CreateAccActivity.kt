package com.example.bank_account_app.activities

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.isDigitsOnly
import com.example.bank_account_app.R
import com.example.bank_account_app.model.*
import com.example.bank_account_app.model.AccountDao.writeUser
import com.example.bank_account_app.model.Accounts.existingAccount
import com.example.bank_account_app.model.Accounts.toSHA256
import com.example.bank_account_app.model.Accounts.updatedID
import java.text.NumberFormat

class CreateAccActivity : AppCompatActivity() {
    private lateinit var et_user_name: EditText
    private lateinit var et_password: EditText
    private lateinit var et_acc_balance: EditText
    private lateinit var btn_submit_new_acc: View
    private lateinit var radio_group: RadioGroup
    private lateinit var radioText: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_acc)
        et_user_name = findViewById(R.id.et_user_name) as EditText
        et_password = findViewById(R.id.et_password) as EditText
        et_acc_balance = findViewById(R.id.et_acc_balance) as EditText

        et_acc_balance.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            var current = ""
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(s.toString().isNotBlank()){
                    et_acc_balance.removeTextChangedListener(this)
                    val cleanString = s?.replace("""[$,.]""".toRegex(), "") ?: s.toString()
                    val parsed = cleanString.filter { it.isDigit() }.toDouble()
                    val formatted = NumberFormat.getCurrencyInstance().format((parsed / 100))
                    current = formatted
                    et_acc_balance.setText(formatted)
                    et_acc_balance.setSelection(formatted.length)
                    et_acc_balance.addTextChangedListener(this)
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        radio_group = findViewById(R.id.radio_group) as RadioGroup
        btn_submit_new_acc = findViewById(R.id.btn_submit_new_acc) as View

        radio_group.setOnCheckedChangeListener(
            RadioGroup.OnCheckedChangeListener { group, checkedId ->
                val radio: RadioButton = findViewById(checkedId)
                radioText = radio.text.toString()
                Toast.makeText(
                    applicationContext,
                    " On checked change : ${radio.text} -> $radioText",
                    Toast.LENGTH_SHORT
                ).show()
            }
        )

        btn_submit_new_acc.setOnClickListener {
            val user = et_user_name.text.toString()
            val pass = et_password.text.toString().toSHA256()
            val bal = et_acc_balance.text.filter { it.isDigit()}.toString().toLong()
//            if(existingAccount(user, radioText)){
                if (radioText == "Current Account") {
                    val user =
                        CurrentAccount(updatedID(), user, pass, Accounts.oppeningDate(), bal)
                    Accounts.accountsList.add(user)
                    writeUser(user)
                } else {
                    val user =
                        SavingsAccount(updatedID(), user, pass, Accounts.oppeningDate(), bal)
                    Accounts.accountsList.add(user)
                    writeUser(user)
                }
                finish()
//            }else{
//                Toast.makeText(
//                    this@CreateAccActivity,
//                    "This user already have this account type!",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
        }
    }

    fun onRadioButtonClicked(view: View): String {
        if (view is RadioButton) {
            val checked = view.isChecked
            when (view.id) {
                R.id.radio_current_acc ->
                    if (checked) {
                        return "current_acc"
                    }
                R.id.radio_savings_acc ->
                    if (checked) {
                        return "savings_acc"
                    }
            }
        }
        throw Exception("deu ruim dai")
    }
}
