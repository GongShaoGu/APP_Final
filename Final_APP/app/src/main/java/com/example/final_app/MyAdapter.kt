package com.example.final_app

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat

class MyAdapter(context: Context, resource: Int, data: List<String>) : ArrayAdapter<String>(context, resource, data) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent)
        val item = getItem(position).toString()
        val finish = item.split("\n")[3]
        // 设置背景颜色
        if(finish == "未完成") {
            view.setBackgroundColor(ContextCompat.getColor(context, R.color.purple_200))
        } else {
            view.setBackgroundColor(ContextCompat.getColor(context, R.color.tea_green))
        }


        // 设置其他视图的内容...

        return view
    }
}
