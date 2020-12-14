package com.melq.howmanydays

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.time.DateTimeException
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class CustomAdapter(private val dateList: ArrayList<DateData>): RecyclerView.Adapter<CustomAdapter.ViewHolder>() {
    lateinit var listener: OnItemClickListener

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val box: View = view.findViewById(R.id.layout_date_box)
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

        val count = try {
            ChronoUnit.DAYS.between(LocalDate.of(dateData.year, dateData.month, dateData.date), LocalDate.now()).toString()
        } catch (e: DateTimeException) {
            Log.e("MELQ", "got exception", e)
            "N/A"
        }
        val date = dateData.year.toString() + "-" +
                dateData.month.toString() + "-" +
                dateData.date.toString()

        holder.name.text = dateData.name
        holder.count.text = count
        holder.date.text = date

        holder.box.setOnClickListener {
            listener.onItemClickListener(it, position, dateData.id)
        }
    }

    override fun getItemCount() = dateList.size

    interface OnItemClickListener {
        fun onItemClickListener(view: View, position: Int, clickedId: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }
}