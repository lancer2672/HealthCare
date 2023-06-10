package com.example.healthcareapp.ui.adapters
//
//import android.R.attr.thumb
//import android.content.Context
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.BaseAdapter
//import android.widget.SpinnerAdapter
//import android.widget.TextView
//import com.example.healthcareapp.R
//import com.example.healthcareapp.data.models.MonthYearModel
//import com.example.healthcareapp.data.models.QuestionModel
//
//
//class MonthYearAdapter(private val context: Context, private val list: List<MonthYearModel>): BaseAdapter(){
//
//    private fun formatMonthYear(monthYear: MonthYearModel?): String {
//        monthYear?.let {
//            return "Tháng ${it.month} Năm ${it.year}"
//        }
//        return ""
//    }
//    override fun getCount(): Int {
//        return list.size
//    }
//
//    override fun getItem(position: Int): Any {
//        return list[position]
//    }
//
//    override fun getItemId(position: Int): Long {
//        return position.toLong()
//    }
//
//    override fun getView(position: Int,  convertView: View?, parent: ViewGroup?): View {
//
//        val viewHolder: ViewHolder
//        var view = convertView
//
//        if (view == null) {
//            viewHolder = ViewHolder()
//            val inflater = LayoutInflater.from(context)
//            view = inflater.inflate(R.layout.simple_spinner_item, parent, false)
//            viewHolder.tv = view.findViewById(R.id.tv1)
//            view.tag = viewHolder
//        } else {
//            viewHolder = view.tag as ViewHolder
//        }
//
//        val monthYear = list[position]
//        viewHolder.tv?.text = formatMonthYear(monthYear)
//
//        return view!!
//    }
//    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
//        val view: View
//        val viewHolder: ViewHolder
//
//        if (convertView == null) {
//            viewHolder = ViewHolder()
//            val inflater = LayoutInflater.from(context)
//            view = inflater.inflate(R.layout.simple_spinner_dropdown_item, parent, false)
//            viewHolder.tv = view.findViewById(R.id.tv1)
//            view.tag = viewHolder
//        } else {
//            view = convertView
//            viewHolder = view.tag as ViewHolder
//        }
//
//        val monthYear = list[position]
//        viewHolder.tv?.text = formatMonthYear(monthYear)
//
//        return view
//    }
//    inner class ViewHolder {
//        var tv: TextView? = null
//    }
//}
