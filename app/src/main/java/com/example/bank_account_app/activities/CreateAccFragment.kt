package com.example.bank_account_app.activities

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.bank_account_app.R
import com.example.bank_account_app.databinding.FragmentCreateAccBinding
import com.example.bank_account_app.model.AccountDao.writeUser
import com.example.bank_account_app.model.Accounts
import com.example.bank_account_app.model.Accounts.existingAccount
import com.example.bank_account_app.model.Accounts.toSHA256
import com.example.bank_account_app.model.Accounts.updatedID
import com.example.bank_account_app.model.CurrentAccount
import com.example.bank_account_app.model.SavingsAccount
import com.example.bank_account_app.utils.popBackStack
import com.example.bank_account_app.utils.toast
import java.text.NumberFormat

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class CreateAccFragment : Fragment(R.layout.fragment_create_acc) {

    private lateinit var binding: FragmentCreateAccBinding
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreateAccBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        with(binding) {
            etAccBalance.addTextChangedListener(object : TextWatcher {
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
                        etAccBalance.removeTextChangedListener(this)
                        val cleanString = s?.replace("""[$,.]""".toRegex(), "") ?: s.toString()
                        val parsed = cleanString.filter { it.isDigit() }.toDouble()
                        val formatted = NumberFormat.getCurrencyInstance().format((parsed / 100))
                        current = formatted
                        etAccBalance.setText(formatted)
                        etAccBalance.setSelection(formatted.length)
                        etAccBalance.addTextChangedListener(this)
                    }
                }

                override fun afterTextChanged(s: Editable?) {}
            })

            btnSubmitNewAcc.setOnClickListener {
                val user = etUserName.text.toString()
                val pass = etPassword.text.toString().toSHA256()
                val bal = etAccBalance.text.filter { it.isDigit() }.toString().toLong()

                if (!existingAccount(user, radioCurrentAcc.isChecked)) {
                    if (radioCurrentAcc.isChecked) {
                        val user =
                            CurrentAccount(updatedID(), user, pass, Accounts.oppeningDate(), bal)
                        Accounts.accountsList.add(user)
                        writeUser(user)
                    } else {
                        val user =
                            SavingsAccount(updatedID(), user, pass, Accounts.oppeningDate(), bal)
                        Accounts.accountsList.add(user)
                        writeUser(user)
                    }
                    popBackStack()
                } else {
                    toast("This user already have this account type!")
                }
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Test1Fragment.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CreateAccFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
