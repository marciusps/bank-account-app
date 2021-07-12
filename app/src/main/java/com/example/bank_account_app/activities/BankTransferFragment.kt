package com.example.bank_account_app.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.bank_account_app.databinding.FragmentBankTransferBinding
import com.example.bank_account_app.model.AccountDao
import com.example.bank_account_app.model.Accounts
import com.example.bank_account_app.utils.SharedPreferencesLogin
import com.example.bank_account_app.utils.onChange
import com.example.bank_account_app.utils.toast

class BankTransferFragment : Fragment() {

    private lateinit var binding: FragmentBankTransferBinding
    private val args: BankTransitionFragmentArgs by navArgs()

    private val navController: NavController by lazy {
        findNavController()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBankTransferBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            etTransitionValue.onChange(etTransitionValue)

            val user = Accounts.findUser(SharedPreferencesLogin.getLogin())
            balance.text = user?.accountBalance?.let { Accounts.coinToMoney(it) }

            btnSubmitTransfer.setOnClickListener{
                val transferAccount = Accounts.accountFinder(etAccName.text.toString(), radioCurrentAcc.isChecked)
                if(transferAccount != null){
                    if (etTransitionValue.text.toString() != "") {
                        val transition =
                            etTransitionValue.text.filter { it.isDigit() }.toString().toLong()
                        if (user?.accountBalance ?: 0 - transition >= 0) {
                            user?.withdraw(transition)
                            transferAccount.deposit(transition)
                            val statement1 = "Transferência enviada;${transferAccount?.ownersName};${Accounts.coinToMoney(transition)};${Accounts.oppeningDate()}"
                            val statement2 = "Transferência recebida;${user?.ownersName};${Accounts.coinToMoney(transition)};${Accounts.oppeningDate()}"
                            Accounts.statementsList.add(statement1)
                            user?.accountID?.let { it1 -> AccountDao.writeStatement(it1, statement1) }
                            AccountDao.writeStatement(transferAccount.accountID, statement2)
                            AccountDao.writeFile()
                            navController.popBackStack()
                        } else {
                            toast("Cannot withdraw this amount!")
                        }
                    } else {
                        toast("Must enter some value!")
                    }
                }else{
                    toast("This account does not exist!")
                }
            }
        }
    }
}