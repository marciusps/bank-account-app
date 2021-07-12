package com.example.bank_account_app.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.bank_account_app.R
import com.example.bank_account_app.databinding.FragmentBankTransitionBinding
import com.example.bank_account_app.model.AccountDao
import com.example.bank_account_app.model.Accounts
import com.example.bank_account_app.utils.SharedPreferencesLogin
import com.example.bank_account_app.utils.onChange
import com.example.bank_account_app.utils.toast

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class BankTransitionFragment : Fragment() {

    private lateinit var binding: FragmentBankTransitionBinding
    private var param1: String? = null
    private var param2: String? = null
    private val args: BankTransitionFragmentArgs by navArgs()

    private val navController: NavController by lazy {
        findNavController()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBankTransitionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        with(binding) {
            etTransitionValue.onChange(etTransitionValue)

            val user = Accounts.findUser(SharedPreferencesLogin.getLogin())
            balance.text = user?.accountBalance?.let { Accounts.coinToMoney(it) }

            when (args.transition) {
                getString(R.string.deposit) -> {
                    transitionType.text = getString(R.string.deposit)
                    btnSubmitTransition.setOnClickListener {
                        if (etTransitionValue.text.toString() != "") {
                            val transition =
                                etTransitionValue.text.filter { it.isDigit() }.toString().toLong()
                            user?.deposit(transition)
                            val statement = "Depósito;${user?.ownersName};${Accounts.coinToMoney(transition)};${Accounts.oppeningDate()}"
                            Accounts.statementsList.add(statement)
                            user?.accountID?.let { it1 -> AccountDao.writeStatement(it1, statement) }
                            AccountDao.writeFile()
                            navController.popBackStack()
                        } else {
                            toast("Must enter some value!")
                        }
                    }
                }
                getString(R.string.withdraw) -> {
                    transitionType.text = getString(R.string.withdraw)
                    btnSubmitTransition.setOnClickListener {
                        if (etTransitionValue.text.toString() != "") {
                            val transition =
                                etTransitionValue.text.filter { it.isDigit() }.toString().toLong()
                            if (user?.accountBalance ?: 0 - transition >= 0) {
                                user?.withdraw(transition)
                                val statement = "Saque;${user?.ownersName};${Accounts.coinToMoney(transition)};${Accounts.oppeningDate()}"
                                Accounts.statementsList.add(statement)
                                user?.accountID?.let { it1 -> AccountDao.writeStatement(it1, statement) }
                                AccountDao.writeFile()
                                navController.popBackStack()
                            } else {
                                toast("Cannot withdraw this amount!")
                            }
                        } else {
                            toast("Must enter some value!")
                        }
                    }
                }
            }
        }
    }
}