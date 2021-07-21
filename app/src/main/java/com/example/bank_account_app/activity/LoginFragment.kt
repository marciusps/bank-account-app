package com.example.bank_account_app.activity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.bank_account_app.databinding.FragmentLoginBinding
import com.example.bank_account_app.util.AccountDao
import com.example.bank_account_app.util.AccountManager
import com.example.bank_account_app.util.AccountManager.loginValidation
import com.example.bank_account_app.util.AccountManager.toSHA256
import com.example.bank_account_app.util.SharedPreferencesLogin
import com.example.bank_account_app.util.toast

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding

    private val navController: NavController by lazy {
        findNavController()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        AccountDao.readFile()
        AccountManager.updateIDCounter()
        AccountDao.fillMenu()

        with(binding) {
            btnCreateAcc.setOnClickListener {
                val action = LoginFragmentDirections.actionLoginFragmentToCreateAccFragment()
                navController.navigate(action)
            }

            if (SharedPreferencesLogin.getLogin()[0].isNotBlank() || SharedPreferencesLogin.getLogin()[1].isNotBlank()) {
                val action = LoginFragmentDirections.actionLoginFragmentToHomeFragment()
                navController.navigate(action)
            } else {
                btnLogin.setOnClickListener {
                    val username = etUserName.text.toString()
                    val password = etPassword.text.toString().toSHA256()
                    if (loginValidation(username, password, radioCurrentAcc.isChecked)) {
                        changeState(true)
                        delay {
                            SharedPreferencesLogin.saveLogin(AccountManager.accountValidator(username, password))
                            toast("Login efetuado com sucesso!")
                            val action = LoginFragmentDirections.actionLoginFragmentToHomeFragment()
                            navController.navigate(action)
                        }
                    } else
                        toast("Dados inválidos!")
                }
            }
        }
    }

    private fun changeState(isLoading: Boolean) {
        with(binding) {
            if (isLoading) {
                progressBar.visibility = View.VISIBLE
                btnLogin.apply {
                    isClickable = false
                    isFocusable = false
                    alpha = 0.5f
                }
            } else {
                progressBar.visibility = View.INVISIBLE
                btnLogin.apply {
                    isClickable = true
                    isFocusable = true
                    alpha = 1f
                }
            }
        }
    }

    private fun delay(delay: Long = 2000, action: () -> Unit) {
        Handler(Looper.getMainLooper()).postDelayed(action, delay)
    }
}