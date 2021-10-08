package com.example.gambling

import android.content.Context
import android.content.Intent
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Message.obtain
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import java.util.*
import kotlin.concurrent.thread
import kotlin.math.ceil
import kotlin.math.floor

class MainActivity : AppCompatActivity() {
    companion object {
        lateinit var conte: Context
        var resu = ""
        val l: LinkedList<Int> = LinkedList()
        val rec: LinkedList<String> = LinkedList()
        var p1 = 6
        var p2 = 6
        var p3 = 6
        var p4 = 6
        var p5 = 6
        var p6 = 6
        val previous: LinkedList<Long> = LinkedList()
    }

    private val handler: Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            val th = msg.data
            randomimg(1, th.getInt("fin1"))
            randomimg(2, th.getInt("fin2"))
            randomimg(3, th.getInt("fin3"))
            randomimg(4, th.getInt("fin4"))
            randomimg(5, th.getInt("fin5"))
            randomimg(6, th.getInt("fin6"))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()
        conte = this


        AlertDialog.Builder(conte).apply {
            setTitle("玩家须知：")
            setMessage("玩家可以通过左右摇动手机，或单击“摇骰子”按钮，来摇动骰子。但是，请勿频繁点击“摇骰子”，更不要频繁摇动手机O！等停下来再继续也不迟QAQ")
            setCancelable(false)
            setPositiveButton("好哒！") { dialog, which ->

            }
            setNegativeButton("返回") { dialog,
                                      which ->
            }
            show()

        }


        var sensorHelper = SensorManagerHelper(this)
        sensorHelper.setOnShakeListener(object : SensorManagerHelper.OnShakeListener {
            override fun onShake() {
                if (previous.isEmpty()) {
                    val now = Date().time
                    onResponse()
                    Toast.makeText(this@MainActivity, "您摇动了手机，请稍后...", Toast.LENGTH_SHORT).show()
                    previous.add(now)
                } else {
                    val pr = previous[previous.size - 1]
                    val now = Date().time
                    if (now - pr < 3400) {

                    } else {
                        onResponse()
                        Toast.makeText(this@MainActivity, "您摇动了手机，请稍后...", Toast.LENGTH_SHORT)
                            .show()
                        previous.clear()
                        previous.add(now)
                    }
                }

            }
        })


        findViewById<TextView>(R.id.seer).setOnClickListener {
            var intent = Intent(this, RegulationActivity::class.java) //前往主界面
            startActivity(intent)
        }
        findViewById<Button>(R.id.jksdalk).setOnClickListener {
            onResponse()
        }


    }

    fun onResponse() {
        p1 = floor(Math.random() * 6 + 1).toInt()
        p2 = floor(Math.random() * 6 + 1).toInt()
        p3 = floor(Math.random() * 6 + 1).toInt()
        p4 = floor(Math.random() * 6 + 1).toInt()
        p5 = floor(Math.random() * 6 + 1).toInt()
        p6 = floor(Math.random() * 6 + 1).toInt()
        l.clear()
        l.add(p1)
        l.add(p2)
        l.add(p3)
        l.add(p4)
        l.add(p5)
        l.add(p6)
        thread {
            var i = 1
            while (i <= 25) {
                val a = obtain()
                val b = Bundle()
                b.putInt("fin1", 0)
                b.putInt("fin2", 0)
                b.putInt("fin3", 0)
                b.putInt("fin4", 0)
                b.putInt("fin5", 0)
                b.putInt("fin6", 0)
                a.data = b
                handler.sendMessage(a)
                if (i < 10) Thread.sleep(50)
                else if (i < 15) Thread.sleep(100)
                else if (i < 22) Thread.sleep(200)
                else Thread.sleep(300)
                i++
            }
            val a = obtain()
            val b = Bundle()
            b.putInt("fin1", p1)
            b.putInt("fin2", p2)
            b.putInt("fin3", p3)
            b.putInt("fin4", p4)
            b.putInt("fin5", p5)
            b.putInt("fin6", p6)
            a.data = b
            handler.sendMessage(a)
        }
    }

    fun randomimg(s: Int, fin: Int) { //s:第几个图片位置   fin:结束于几，0则随机
        lateinit var a: ImageView
        var f = 6
        when (s) {
            1 -> {
                a = findViewById(R.id.i1)
            }
            2 -> {
                a = findViewById(R.id.i2)
            }
            3 -> {
                a = findViewById(R.id.i3)
            }
            4 -> {
                a = findViewById(R.id.i4)
            }
            5 -> {
                a = findViewById(R.id.i5)
            }
            6 -> {
                a = findViewById(R.id.i6)
            }
        }
        when (fin) {
            0 -> {
                f = floor(Math.random() * 6 + 1).toInt()
            }
            1 -> {
                f = 1
            }
            2 -> {
                f = 2
            }
            3 -> {
                f = 3
            }
            4 -> {
                f = 4
            }
            5 -> {
                f = 5
            }
            5 -> {
                f = 6
            }
        }
        when (f) {
            1 -> {
                a.setImageResource(R.drawable.p1)
            }
            2 -> {
                a.setImageResource(R.drawable.p2)
            }
            3 -> {
                a.setImageResource(R.drawable.p3)
            }
            4 -> {
                a.setImageResource(R.drawable.p4)
            }
            5 -> {
                a.setImageResource(R.drawable.p5)
            }
            6 -> {
                a.setImageResource(R.drawable.p6)
            }
        }
        if (s == 6 && fin != 0) {
            examinResulet(l)
        }
    }


    fun examinResulet(r: LinkedList<Int>) {
        var c1 = 0
        var c2 = 0
        var c3 = 0
        var c4 = 0
        var c5 = 0
        var c6 = 0
        for (i in r) {
            when (i) {
                1 -> {
                    c1++
                }
                2 -> {
                    c2++
                }
                3 -> {
                    c3++
                }
                4 -> {
                    c4++
                }
                5 -> {
                    c5++
                }
                6 -> {
                    c6++
                }
            }
        }
        val t = Date()
        val year = t.year
        val month = t.month
        val day = t.day
        val hour = t.hours
        val minute = t.minutes
        val sec = t.seconds
//        val ti=""+year+"年"+month+"月"+day+"日 "+hour+":"+minute+":"+sec
        val ti = "" + hour + ":" + minute + ":" + sec

        resu = "    很遗憾，没中奖哦QAQ\n    摸摸头，不哭哦，根据“运气守恒定律”，你现在减少运气花费，积累到大三上期末考考试周，有望蒙的全对哦"
        if (c4 == 4 && c1 == 2) {
            resu = "恭喜您获得了：状元（插金花）！免宿舍卫生一学期"
            rec.add(ti + "  状元（插金花）")
        } else if (c4 == 6) {
            resu = "恭喜您获得了：状元（六杯红）！免宿舍卫生一学期"
            rec.add(ti + "  状元（六杯红）")
        } else if (c6 == 6) {
            resu = "恭喜您获得了：状元（插金黑）！免宿舍卫生一学期"
            rec.add(ti + "  状元（插金黑）")
        } else if (c4 == 5) {
            resu = "恭喜您获得了：五王！免宿舍卫生一学期"
            rec.add(ti + "  五王")
        } else if (c4 == 4) {
            resu = "恭喜您获得了：状元！免宿舍卫生一学期"
            rec.add(ti + "  状元")
        } else if (c1 == 1 && c2 == 1 && c3 == 1 && c4 == 1 && c5 == 1 && c6 == 1) {
            resu = "恭喜您获得了：榜眼（对堂）！获得零食礼包一份"
            rec.add(ti + "  榜眼（对堂）")
        } else if (c4 == 3) {
            resu = "恭喜您获得了：探花（三红）！奖励水杯一个"
            rec.add(ti + "  探花（三红）")
        } else if (c2 == 4) {
            resu = "恭喜您获得了：进士（四进）！奖励奶茶一杯"
            rec.add(ti + "  进士（四进）")
        } else if (c4 == 2) {
            resu = "恭喜您获得了：举人（二举）！参与奖一份"
            rec.add(ti + "  举人（二举）")

        } else {
            rec.add(ti + "   没中哦")
        }
        val t1 = findViewById<TextView>(R.id.record1)
        val t2 = findViewById<TextView>(R.id.record2)
        val t3 = findViewById<TextView>(R.id.record3)
        val t4 = findViewById<TextView>(R.id.record4)
        val t5 = findViewById<TextView>(R.id.record5)
        if (rec.size == 0) {
            AlertDialog.Builder(conte).apply {
                setTitle("结果通知：")
                setMessage("rec长度是0")
                setCancelable(false)
                setPositiveButton("再玩亿次") { dialog, which ->

                }
                setNegativeButton("返回") { dialog,
                                          which ->
                }
                show()

            }
        } else if (rec.size == 1) {
            t1.text = rec[0]
        } else if (rec.size == 2) {
            t1.text = rec[1]
            t2.text = rec[0]

        } else if (rec.size == 3) {
            t1.text = rec[2]
            t2.text = rec[1]
            t3.text = rec[0]

        } else if (rec.size == 4) {
            t1.text = rec[3]
            t2.text = rec[2]
            t3.text = rec[1]
            t4.text = rec[0]

        } else if (rec.size == 5) {
            t1.text = rec[4]
            t2.text = rec[3]
            t3.text = rec[2]
            t4.text = rec[1]
            t5.text = rec[0]

        } else {
            t1.text = rec[rec.size - 1]
            t2.text = rec[rec.size - 2]
            t3.text = rec[rec.size - 3]
            t4.text = rec[rec.size - 4]

        }

        AlertDialog.Builder(conte).apply {
            setTitle("结果通知：")
            setMessage(resu)
            setCancelable(false)
            setPositiveButton("再玩亿次") { dialog, which ->

            }
            setNegativeButton("返回") { dialog,
                                      which ->
            }
            show()

        }

    }
}