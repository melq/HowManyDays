package com.melq.howmanydays

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class CustomAdapter(private val dateList: ArrayList<DateData>): RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.date_name)
        val count: TextView = view.findViewById(R.id.date_count)
        val date: TextView = view.findViewById(R.id.date_date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dateData = dateList[position]

        val count = ChronoUnit.DAYS.between(dateData.date, LocalDate.now())

        holder.name.text = dateData.name
        holder.count.text = count.toString()
        holder.date.text = dateData.date.toString()
    }

    override fun getItemCount() = dateList.size
}