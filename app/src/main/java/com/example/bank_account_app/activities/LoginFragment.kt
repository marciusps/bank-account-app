package com.example.bank_account_app.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.bank_account_app.R
import com.example.bank_account_app.databinding.FragmentLoginBinding
import com.example.bank_account_app.utils.AccountDao
import com.example.bank_account_app.utils.SharedPreferencesLogin
import com.example.bank_account_app.utils.Utils
import com.example.bank_account_app.utils.Utils.loginValidation
import com.example.bank_account_app.utils.Utils.toSHA256
import com.example.bank_account_app.utils.toast

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
        Utils.updateIDCounter()
        AccountDao.readMenu()

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
                            SharedPreferencesLogin.saveLogin(Utils.accountValidator(username, password))
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

    private fun delay(delay: Long = 1500, action: () -> Unit) {
        Handler(Looper.getMainLooper()).postDelayed(action, delay)
    }
}