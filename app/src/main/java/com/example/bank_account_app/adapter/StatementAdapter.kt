package com.example.bank_account_app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bank_account_app.R
import kotlin.collections.ArrayList

class StatementAdapter(private val dataSet: ArrayList<String>) :
    RecyclerView.Adapter<StatementAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textViewTitle: TextView
        val textViewName: TextView
        val textViewValue: TextView
        val textViewDate: TextView
        val imageView: ImageView

        init {
            textViewTitle = view.findViewById(R.id.textViewTitle)
            textViewName = view.findViewById(R.id.textViewName)
            textViewValue = view.findViewById(R.id.textViewValue)
            textViewDate = view.findViewById(R.id.textViewDate)
            imageView = view.findViewById(R.id.imageView)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.text_row_item, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        val row = dataSet[position].split(";")
        viewHolder.textViewTitle.text = row[0]
        viewHolder.textViewValue.text = row[2]
        viewHolder.textViewDate.text = row[3] //transformar formato de data

        if(row[0]=="Transferência enviada"){
            viewHolder.imageView.setImageResource(R.drawable.ic_send_money)
            viewHolder.textViewName.text = row[1]
        }

        if(row[0]=="Transferência recebida"){
            viewHolder.imageView.setImageResource(R.drawable.ic_receive_money)
            viewHolder.textViewName.text = row[1]
        }

        if(row[0]=="Depósito"){
            viewHolder.imageView.setImageResource(R.drawable.ic_deposit)
            viewHolder.textViewName.visibility = View.GONE
        }

        if(row[0]=="Saque"){
            viewHolder.imageView.setImageResource(R.drawable.ic_withdraw)
            viewHolder.textViewName.visibility = View.GONE
        }
    }

    override fun getItemCount() = dataSet.size
}