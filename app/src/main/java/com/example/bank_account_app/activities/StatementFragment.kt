package com.example.bank_account_app.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bank_account_app.adapters.StatementAdapter
import com.example.bank_account_app.databinding.FragmentStatementBinding
import com.example.bank_account_app.utils.Utils

class StatementFragment : Fragment() {

    private lateinit var list: ArrayList<String>

    private lateinit var binding: FragmentStatementBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStatementBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        list = Utils.statementsList

        val recyclerViewList: RecyclerView = binding.statementFragment
        val customAdapter = StatementAdapter(list)

        recyclerViewList.apply {
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            adapter = customAdapter
            layoutManager = LinearLayoutManager(context)
        }

        if(list.size==0){
            binding.transactionHistoric.visibility = View.VISIBLE
        }
    }
}