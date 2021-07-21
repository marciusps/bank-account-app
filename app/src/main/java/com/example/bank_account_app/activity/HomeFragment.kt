package com.example.bank_account_app.activity

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
import com.example.bank_account_app.adapter.HomeAdapter
import com.example.bank_account_app.databinding.FragmentHomeBinding
import com.example.bank_account_app.util.AccountDao
import com.example.bank_account_app.util.AccountManager
import com.example.bank_account_app.util.AccountManager.coinToMoney
import com.example.bank_account_app.util.AccountManager.findUser
import com.example.bank_account_app.util.SharedPreferencesLogin
import com.example.bank_account_app.util.toast
import kotlinx.coroutines.*

class HomeFragment : Fragment(), CoroutineScope {

    private lateinit var binding: FragmentHomeBinding

    //private val tag = "BankAccount"
    private val parentJob = Job()
    override val coroutineContext = parentJob + Dispatchers.Main

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

            val recyclerViewList: RecyclerView = binding.homeRecycler
            val homeAdapter = HomeAdapter(AccountManager.menuList) {
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

                    "+10k" -> {
                        launch(Dispatchers.IO) {
                            val result = async { AccountDao.daleContas() }
                            withContext(Dispatchers.Main) {
                                progressCoroutine.visibility = View.VISIBLE
                                toast(result.await())
                                progressCoroutine.visibility = View.INVISIBLE
                            }
                        }
                    }
                }
            }

            recyclerViewList.apply {
                adapter = homeAdapter
                layoutManager = GridLayoutManager(context, 2)
            }

            btnlogout.setOnClickListener{
                SharedPreferencesLogin.logout()
                navController.popBackStack()
            }
        }
    }
}