package com.example.bank_account_app.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.bank_account_app.R
import com.example.bank_account_app.databinding.FragmentCreateAccBinding
import com.example.bank_account_app.model.CurrentAccount
import com.example.bank_account_app.model.SavingsAccount
import com.example.bank_account_app.util.AccountDao
import com.example.bank_account_app.util.AccountManager
import com.example.bank_account_app.util.AccountManager.accountFinder
import com.example.bank_account_app.util.AccountManager.toSHA256
import com.example.bank_account_app.util.AccountManager.updatedID
import com.example.bank_account_app.util.onChange
import com.example.bank_account_app.util.toast

class CreateAccFragment : Fragment(R.layout.fragment_create_acc) {

    private lateinit var binding: FragmentCreateAccBinding

    private val navController: NavController by lazy {
        findNavController()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateAccBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            etAccBalance.onChange(etAccBalance)

            btnSubmitNewAcc.setOnClickListener {
                val user = etUserName.text.toString()
                val pass = etPassword.text.toString().toSHA256()
                val bal = etAccBalance.text.filter { it.isDigit() }.toString().toLong()

                if (accountFinder(user, radioCurrentAcc.isChecked)==null) {
                    if (radioCurrentAcc.isChecked) {
                        val user =
                            CurrentAccount(updatedID(), user, pass, AccountManager.oppeningDate(), bal)
                        AccountManager.accountsList.add(user)
                        AccountDao.writeUser(user)
                    } else {
                        val user =
                            SavingsAccount(updatedID(), user, pass, AccountManager.oppeningDate(), bal)
                        AccountManager.accountsList.add(user)
                        AccountDao.writeUser(user)
                    }
                    navController.popBackStack()
                } else {
                    toast("This user already have this account type!")
                }
            }
        }
    }
}
