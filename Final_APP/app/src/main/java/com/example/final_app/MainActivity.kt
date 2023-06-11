package com.example.final_app

import android.annotation.SuppressLint
import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.google.android.material.bottomsheet.BottomSheetDialog

class MainActivity : AppCompatActivity() {
    private var items: ArrayList<String> = ArrayList()
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var dbrw: SQLiteDatabase

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //取得資料庫實體
        dbrw = MyDBHelper(this).writableDatabase
        //宣告 Adapter 並連結 ListView
        adapter = ArrayAdapter(this,
            android.R.layout.simple_list_item_1, items)
        findViewById<ListView>(R.id.listView).adapter = adapter

        //設定監聽器
        setListener()
    }

    fun setListener() {
        var year = 0
        var month = 0
        var day = 0
        val calendarView = findViewById<CalendarView>(R.id.calendarView)
        calendarView.setOnDateChangeListener { _, y, m, d ->
            year = y
            month = m
            day = d
            update(year, month, day)
        }

        findViewById<Button>(R.id.button).setOnClickListener {
            val dialogView = layoutInflater.inflate(R.layout.bottom_sheet, null)
            val dialog = BottomSheetDialog(this)
            dialog.setContentView(dialogView)
            dialog.show()
            dialogView.findViewById<Button>(R.id.button3).setOnClickListener {
                val title = dialogView.findViewById<EditText>(R.id.Note_title2).text
                val content = dialogView.findViewById<EditText>(R.id.Note_content2).text
                val cv = ContentValues()
                cv.put("finish", 0)
                cv.put("year", year)
                cv.put("month", month)
                cv.put("day", day)
                cv.put("title",title.toString())
                cv.put("content",content.toString())
                dbrw.insert("myTable",null,cv)
                dialog.dismiss()
                update(year, month, day)
            }
        }

        findViewById<Button>(R.id.button2).setOnClickListener {
            val dialogView = layoutInflater.inflate(R.layout.bottom_sheet2, null)
            val dialog = BottomSheetDialog(this)
            dialog.setContentView(dialogView)
            dialog.show()
            dialogView.findViewById<Button>(R.id.button4).setOnClickListener {
                val id = dialogView.findViewById<EditText>(R.id.Note_id).text
                try {
                    //從 myTable 資料表刪除相同蔬菜的紀錄
                    dbrw.execSQL("DELETE FROM myTable WHERE Note LIKE '${id}'")
                } catch (e: Exception) {
                    showToast("刪除失敗:$e")
                }
                dialog.dismiss()
                update(year, month, day)
            }

        }
    }
    //建立 showToast 方法顯示 Toast 訊息
    private fun showToast(text: String) =
        Toast.makeText(this,text, Toast.LENGTH_LONG).show()

    private fun update(year: Int, month: Int, day: Int) {
        val queryString = "SELECT * FROM myTable WHERE year = '${year}' and month = '${month}' and day = '${day}'"
        val c = dbrw.rawQuery(queryString, null)
        c.moveToFirst() //從第一筆開始輸出
        items.clear() //清空舊資料
        showToast("共有${c.count}筆資料")
        for (i in 0 until c.count) {
            //加入新資料
            items.add("ID:${c.getInt(0)}\t標題:${c.getString(5)}\n內容:${c.getString(6)}")
            c.moveToNext() //移動到下一筆
        }
        adapter.notifyDataSetChanged() //更新列表資料
        c.close()
    }
}