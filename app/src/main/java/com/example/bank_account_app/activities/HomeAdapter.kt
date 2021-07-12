package com.example.bank_account_app.activities

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bank_account_app.R

class HomeAdapter(private val dataSet: ArrayList<String>, private val onClick: (String) -> Unit) :
    RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {

    class HomeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textViewTitle: TextView
        val btn_recycler: FrameLayout

        init {
            textViewTitle = view.findViewById(R.id.button_id)
            btn_recycler = view.findViewById(R.id.btn_recycler)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): HomeViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.buttom_row_item, viewGroup, false)

        return HomeViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.textViewTitle.text = dataSet[position]

        holder.btn_recycler.setOnClickListener{
            onClick.invoke(dataSet[position])
        }
    }

    override fun getItemCount() = dataSet.size
}