package ru.ivos.custom_clock_view_for_vk

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import java.util.*
import kotlin.math.cos
import kotlin.math.sin

/**
 * In this View background is set by default
 * Doesn't react to the Dark/Light mode of the device
 */

class ClockViewDefaultBackground : View {

    private var padding = 0
    private var fontSize = 0
    private var numeralSpacing = 0
    private var handTruncation = 0
    private var hourHandTruncation = 0
    private var radius = 0
    private var paint = Paint()
    private var isInit = false

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )
    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    private fun intiClock() {
        width
        height
        padding = numeralSpacing + 50
        fontSize =
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 13f, resources.displayMetrics)
                .toInt()
        val min = height.coerceAtMost(width)
        radius = min / 2 - padding
        handTruncation = min / 20
        hourHandTruncation = min / 7
        paint
        isInit = true
    }

    override fun onDraw(canvas: Canvas?) {
        if (!isInit) {
            intiClock()
        }
        canvas?.drawColor(resources.getColor(android.R.color.transparent))
        drawCircle(canvas)
        drawCenter(canvas)
        drawHands(canvas)

        postInvalidateDelayed(500)
        invalidate()
    }

    private fun drawCircle(canvas: Canvas?) {
        paint.apply {
            reset()
            color = resources.getColor(android.R.color.black)
            strokeWidth = 5f
            style = Paint.Style.STROKE
            isAntiAlias = true
            background = resources.getDrawable(R.drawable.clock)
        }
        canvas?.drawCircle(width / 2f, height / 2f, radius + padding - 10f, paint)
    }

    private fun drawCenter(canvas: Canvas?) {
        paint.style = Paint.Style.FILL
        canvas?.drawCircle(width / 2f, height / 2f, 12f, paint)
    }


    private fun drawHand(canvas: Canvas?, loc: Double, isHour: Boolean) {
        val angle = Math.PI * loc / 30 - Math.PI / 2
        val handRadius =
            if (isHour) radius - handTruncation - hourHandTruncation else radius - handTruncation
        canvas?.drawLine(
            width / 2f,
            height / 2f,
            (width / 2f + cos(angle) * handRadius).toFloat(),
            (height / 2f + sin(angle) * handRadius).toFloat(),
            paint
        )
    }

    private fun drawHands(canvas: Canvas?) {
        val calendar = Calendar.getInstance()
        var hour = calendar.get(Calendar.HOUR_OF_DAY)
        if (hour > 12) hour -= 12
        drawHand(canvas, (hour + calendar.get(Calendar.MINUTE) / 60) * 5.toDouble(), true)
        drawHand(canvas, hour + calendar.get(Calendar.MINUTE).toDouble() - 7, false)
        drawHand(canvas, hour + calendar.get(Calendar.SECOND).toDouble(), false)
    }
}