package com.example.bank_account_app.activities

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
import com.example.bank_account_app.model.AccountDao
import com.example.bank_account_app.model.Accounts
import com.example.bank_account_app.model.Accounts.toSHA256
import com.example.bank_account_app.utils.SharedPreferencesLogin
import com.example.bank_account_app.utils.toast

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private var param1: String? = null
    private var param2: String? = null

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

        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        AccountDao.readFile()
        Accounts.updateIDCounter()
//        Accounts.accountsList.add(CurrentAccount( Accounts.idCounter++,"Marcius", "123".toSHA256(), Accounts.oppeningDate(),1000))
//        Accounts.accountsList.add(CurrentAccount( Accounts.idCounter++,"Danilo", "123".toSHA256(), Accounts.oppeningDate(),2000))
//        Accounts.accountsList.add(CurrentAccount( Accounts.idCounter++,"Xandynho", "123".toSHA256(), Accounts.oppeningDate(),3000))
//        Accounts.accountsList.add(CurrentAccount( Accounts.idCounter++,"Oi", "Oi".toSHA256(), Accounts.oppeningDate(),999999))
//        AccountDao.writeFile()

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
                    if (username != "" || password != "") {
                        if (userPermission(username, password)) {
                            changeState(true)
                            delay {
                                SharedPreferencesLogin.saveLogin(
                                    Accounts.accountValidator(
                                        username,
                                        password
                                    )
                                )
                                toast("Login efetuado com sucesso!")
                                val action = LoginFragmentDirections.actionLoginFragmentToHomeFragment()
                                navController.navigate(action)
                            }
                        } else
                            toast("Login ou senha inválidos!")
                    }
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