package ru.ivos.custom_clock_view_for_vk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatButton

class MainActivity : AppCompatActivity() {

    private val backgroundList = listOf(
        R.drawable.clock_1,
        R.drawable.clock_2,
        R.drawable.clock_3,
        R.drawable.clock_4
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnChange = findViewById<AppCompatButton>(R.id.btnChangeBack)
        val clock = findViewById<ClockViewUserBackground>(R.id.viewClock)
        var count = 0

        btnChange.setOnClickListener {
            if (count == backgroundList.size) {
                count = 0
                clock.background = null
                return@setOnClickListener
            }
            clock.background = AppCompatResources.getDrawable(this, backgroundList[count])
            count++
        }
    }
}