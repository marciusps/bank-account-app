package com.example.bank_account_app.utils

import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import com.example.bank_account_app.R
import com.example.bank_account_app.model.Account

object SharedPreferencesLogin {

    private fun getSharedLogin(context: Context): SharedPreferences? {
        return context.getSharedPreferences(context.getString(R.string.key), Context.MODE_PRIVATE)
    }

    fun saveLogin(context: Context, account: Account?) {
        val sharedLogin = getSharedLogin(context) ?: return
        with(sharedLogin.edit()) {
            clear()
            putString("key", account?.accountID.toString())
            putString("password", account?.password)
            apply()
            Toast.makeText(context, "Saved login.", Toast.LENGTH_SHORT).show()
        }
    }

    fun getLogin(context: Context): ArrayList<String> {
        val sharedLogin = getSharedLogin(context) ?: return arrayListOf()
        return arrayListOf(
            (sharedLogin.getString("key", "")).toString(),
            (sharedLogin.getString("password", "")).toString()
        )
    }

    fun logout(context: Context) {
        getSharedLogin(context)!!.edit().clear().apply()
    }
}