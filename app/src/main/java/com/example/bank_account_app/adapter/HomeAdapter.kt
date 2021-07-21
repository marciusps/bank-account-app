package com.example.bank_account_app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bank_account_app.R

class HomeAdapter(private val dataSet: ArrayList<String>, private val onClick: (String) -> Unit) :
    RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {

    class HomeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textViewTitle: TextView
        val btn_recycler: FrameLayout
        val btn_icon: ImageView

        init {
            textViewTitle = view.findViewById(R.id.btn_id)
            btn_recycler = view.findViewById(R.id.btn_recycler)
            btn_icon = view.findViewById(R.id.btn_icon)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): HomeViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.buttom_row_item, viewGroup, false)

        return HomeViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.textViewTitle.text = dataSet[position]

        if(dataSet[position]=="deposit")
            holder.btn_icon.setImageResource(R.drawable.ic_deposit)

        if(dataSet[position]=="withdraw")
            holder.btn_icon.setImageResource(R.drawable.ic_withdraw)

        if(dataSet[position]=="transfer")
            holder.btn_icon.setImageResource(R.drawable.ic_transfer)

        if(dataSet[position]=="statement")
            holder.btn_icon.setImageResource(R.drawable.ic_statement)

        if(dataSet[position]=="+10k")
            holder.btn_icon.setImageResource(R.drawable.ic_coroutines)

        holder.btn_recycler.setOnClickListener{
            onClick.invoke(dataSet[position])
        }
    }

    override fun getItemCount() = dataSet.size
}