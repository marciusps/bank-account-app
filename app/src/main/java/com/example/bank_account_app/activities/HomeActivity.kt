package com.example.bank_account_app.model.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bank_account_app.R
import com.example.bank_account_app.activities.BankTransitionActivity
import com.example.bank_account_app.model.AccountDao.readFile
import com.example.bank_account_app.model.Accounts.coinToMoney
import com.example.bank_account_app.model.Accounts.findUser
import com.example.bank_account_app.model.MainActivity
import com.example.bank_account_app.utils.SharedPreferencesLogin.getLogin
import com.example.bank_account_app.utils.SharedPreferencesLogin.logout
import java.util.*

class HomeActivity : AppCompatActivity() {

    private lateinit var username: TextView
    private lateinit var home_balance: TextView
    private lateinit var btn_logout: View
    private lateinit var btn_deposit: View
    private lateinit var btn_withdraw: View
    private var pressedTime: Long = 0

    override fun onBackPressed() {
        if (pressedTime + 3000 > System.currentTimeMillis()) {
            super.onBackPressed()
            finish()
        } else
            Toast.makeText(
                baseContext,
                "Pressione 'Voltar' novamente para sair.",
                Toast.LENGTH_SHORT
            ).show()
        pressedTime = System.currentTimeMillis()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        btn_logout = findViewById(R.id.btn_logout) as View
        btn_deposit = findViewById(R.id.btn_deposit) as View
        btn_withdraw = findViewById(R.id.btn_withdraw) as View
        username = findViewById(R.id.nome)
        home_balance = findViewById(R.id.home_balance)

        val user = findUser(getLogin(this@HomeActivity))
        username.text = user?.ownersName
        home_balance.text = "R$%.2f".format(user?.accountBalance?.let { coinToMoney(it)})
        readFile()

        btn_deposit.setOnClickListener {
            val intent = Intent(this@HomeActivity, BankTransitionActivity::class.java).apply {
                action = "deposit"
            }
            startActivity(intent)
            finish()
        }

        btn_withdraw.setOnClickListener {
            val intent = Intent(this@HomeActivity, BankTransitionActivity::class.java).apply {
                action = "withdraw"
            }
            startActivity(intent)
            finish()
        }

        btn_logout.setOnClickListener {
            val intent = Intent(this@HomeActivity, MainActivity::class.java).apply {
                Toast.makeText(this@HomeActivity, "Logout efetuado!", Toast.LENGTH_SHORT).show()
            }
            logout(this@HomeActivity)
            startActivity(intent)
            finish()
        }
    }
}