package com.example.bank_account_app.activities

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.bank_account_app.R
import com.example.bank_account_app.databinding.FragmentBankTransitionBinding
import com.example.bank_account_app.model.AccountDao.writeFile
import com.example.bank_account_app.model.Accounts
import com.example.bank_account_app.utils.SharedPreferencesLogin
import com.example.bank_account_app.utils.toast
import java.text.NumberFormat

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class BankTransitionFragment : Fragment(R.layout.fragment_bank_transition) {

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
            etTransitionValue.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                var current = ""
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s.toString().isNotBlank()) {
                        etTransitionValue.removeTextChangedListener(this)
                        val cleanString = s?.replace("""[$,.]""".toRegex(), "") ?: s.toString()
                        val parsed = cleanString.filter { it.isDigit() }.toDouble()
                        val formatted = NumberFormat.getCurrencyInstance().format((parsed / 100))
                        current = formatted
                        etTransitionValue.setText(formatted)
                        etTransitionValue.setSelection(formatted.length)
                        etTransitionValue.addTextChangedListener(this)
                    }
                }
                override fun afterTextChanged(s: Editable?) {}
            })

            val user = Accounts.findUser(SharedPreferencesLogin.getLogin())
            balance.text = "R$%.2f".format(user?.accountBalance?.let { Accounts.coinToMoney(it) })

            when (args.transition) {
                getString(R.string.deposit) -> {
                    transitionType.text = "DEPOSIT"
                    btnSubmitTransition.setOnClickListener {
                        if (etTransitionValue.text.toString() != "") {
                            val transition =
                                etTransitionValue.text.filter { it.isDigit() }.toString().toLong()
                            user?.deposit(transition)
                            writeFile()
                            navController.popBackStack()
                        } else {
                            toast("Must enter some value!")
                        }
                    }
                }
                getString(R.string.withdraw) -> {
                    transitionType.text = "WITHDRAW"
                    btnSubmitTransition.setOnClickListener {
                        if (etTransitionValue.text.toString() != "") {
                            val transition =
                                etTransitionValue.text.filter { it.isDigit() }.toString().toLong()
                            if (user?.accountBalance ?: 0 - transition >= 0) {
                                user?.withdraw(transition)
                                writeFile()
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