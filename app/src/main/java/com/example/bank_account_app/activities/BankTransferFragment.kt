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
import com.example.bank_account_app.utils.*

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

            val user = Utils.findUser(SharedPreferencesLogin.getLogin())
            balance.text = user?.accountBalance?.let { Utils.coinToMoney(it) }

            btnSubmitTransfer.setOnClickListener{
                val transferAccount = Utils.accountFinder(etAccName.text.toString(), radioCurrentAcc.isChecked)
                if(transferAccount != null){
                    if (etTransitionValue.text.toString() != "") {
                        val transition =
                            etTransitionValue.text.filter { it.isDigit() }.toString().toLong()
                        if (user?.accountBalance ?: 0 - transition >= 0) {
                            user?.withdraw(transition)
                            transferAccount.deposit(transition)
                            val statement1 = "Transferência enviada;${transferAccount?.ownersName};${Utils.coinToMoney(transition)};${Utils.oppeningDate()}"
                            val statement2 = "Transferência recebida;${user?.ownersName};${Utils.coinToMoney(transition)};${Utils.oppeningDate()}"
                            Utils.statementsList.add(statement1)
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