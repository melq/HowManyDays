package com.melq.howmanydays

import android.content.Context
import android.content.res.Resources
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
        val countUnit: TextView = view.findViewById(R.id.unit_count)
        val date: TextView = view.findViewById(R.id.date_date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dateData = dateList[position]

        val count = try {
            when (dateData.displayMode) {
                0 -> // 日数表示
                    ChronoUnit.DAYS.between(LocalDate.of(dateData.year, dateData.month, dateData.date), LocalDate.now()).toString()
                1 -> // 月数
                    ChronoUnit.MONTHS.between(LocalDate.of(dateData.year, dateData.month, dateData.date), LocalDate.now()).toString()
                else -> // 年数
                    ChronoUnit.YEARS.between(LocalDate.of(dateData.year, dateData.month, dateData.date), LocalDate.now()).toString()
            }
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
        holder.countUnit.text = when (dateData.displayMode) {
            0 -> "日"
            1 -> "ヶ月"
            else -> "年"
        }

        holder.box.setOnClickListener {
            listener.onItemClick(it, position, dateData.id)
        }
    }

    override fun getItemCount() = dateList.size

    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int, clickedId: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }
}