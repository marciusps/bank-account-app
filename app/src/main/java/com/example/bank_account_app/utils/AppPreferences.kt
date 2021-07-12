package com.example.bank_account_app.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.bank_account_app.R
import com.example.bank_account_app.model.Account

object SharedPreferencesLogin {

    private fun getSharedLogin(context: Context): SharedPreferences? {
        return context.getSharedPreferences(context.getString(R.string.key), Context.MODE_PRIVATE)
    }

    fun saveLogin(account: Account?) {
        val sharedLogin = getSharedLogin(MainApplication.applicationContext()) ?: return
        with(sharedLogin.edit()) {
            clear()
            putString("key", account?.accountID.toString())
            putString("password", account?.password)
            apply()
        }
    }

    fun getLogin(): ArrayList<String> {
        val sharedLogin = getSharedLogin(MainApplication.applicationContext()) ?: return arrayListOf()
        return arrayListOf(
            (sharedLogin.getString("key", "")).toString(),
            (sharedLogin.getString("password", "")).toString()
        )
    }

    fun logout() {
        getSharedLogin(MainApplication.applicationContext())!!.edit().clear().apply()
    }
}