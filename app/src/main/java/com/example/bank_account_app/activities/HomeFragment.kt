package com.example.bank_account_app.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bank_account_app.R
import com.example.bank_account_app.databinding.FragmentHomeBinding
import com.example.bank_account_app.model.AccountDao
import com.example.bank_account_app.model.Accounts.coinToMoney
import com.example.bank_account_app.model.Accounts.findUser
import com.example.bank_account_app.utils.SharedPreferencesLogin

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

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

        val user = findUser(SharedPreferencesLogin.getLogin())
        user?.accountID?.let { AccountDao.readStatements(it) }

        with(binding) {
            homeUsername.text = user?.ownersName
            homeBalance.text = user?.accountBalance?.let { coinToMoney(it) }
            AccountDao.readFile() //for non logout

            val list = ArrayList<String>()
            list.add("deposit")
            list.add("withdraw")
            list.add("transfer")
            list.add("statement")
            list.add("logout")

            val recyclerViewList: RecyclerView = binding.homeRecycler
            val homeAdapter = HomeAdapter(list) {
                when (it) {
                    "deposit" ->
                        navController.navigate(
                            HomeFragmentDirections.actionHomeFragmentToBankTransitionFragment(
                                getString(R.string.deposit)))

                    "withdraw" ->
                        navController.navigate(
                            HomeFragmentDirections.actionHomeFragmentToBankTransitionFragment(
                                getString(R.string.withdraw)))

                    "transfer" ->
                        navController.navigate(HomeFragmentDirections.actionHomeFragmentToBankTransferFragment())

                    "statement" ->
                        navController.navigate(HomeFragmentDirections.actionHomeFragmentToStatementFragment())

                    "logout" -> {
                        SharedPreferencesLogin.logout()
                        navController.popBackStack()
                    }
                }
            }

            recyclerViewList.apply {
                adapter = homeAdapter
                layoutManager = GridLayoutManager(context, 2)
            }
        }
    }
}