package com.example.bank_account_app.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.bank_account_app.R
import com.example.bank_account_app.databinding.FragmentHomeBinding
import com.example.bank_account_app.model.AccountDao
import com.example.bank_account_app.model.Accounts.coinToMoney
import com.example.bank_account_app.model.Accounts.findUser
import com.example.bank_account_app.utils.SharedPreferencesLogin

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class HomeFragment : Fragment(R.layout.fragment_home) {

    private var pressedTime: Long = 0

    private lateinit var binding: FragmentHomeBinding
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
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        val user = findUser(SharedPreferencesLogin.getLogin())
        with(binding) {
            homeUsername.text = user?.ownersName
            homeBalance.text = "R$%.2f".format(user?.accountBalance?.let { coinToMoney(it) })
            AccountDao.readFile() //for non logout

            btnDeposit.setOnClickListener {
                val action = HomeFragmentDirections.actionHomeFragmentToBankTransitionFragment(getString(R.string.deposit))
                navController.navigate(action)
            }

            btnWithdraw.setOnClickListener {
                val action = HomeFragmentDirections.actionHomeFragmentToBankTransitionFragment(getString(R.string.withdraw))
                navController.navigate(action)
            }

            btnLogout.setOnClickListener {
                SharedPreferencesLogin.logout()
                navController.popBackStack()
            }
        }
    }
}