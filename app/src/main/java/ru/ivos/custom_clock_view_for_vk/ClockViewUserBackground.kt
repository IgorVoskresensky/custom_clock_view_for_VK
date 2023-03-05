package ru.ivos.custom_clock_view_for_vk

import android.content.Context
import android.content.res.Configuration
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import java.util.*
import kotlin.math.cos
import kotlin.math.sin

/**
 * In this View the user can apply their own prepared background
 * Reacts to the Dark/Light mode of the device
 */

class ClockViewUserBackground(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int,
    defStyleRes: Int,
) : View(context, attrs, defStyleAttr, defStyleRes) {

    private var padding = 0
    private var fontSize = 0
    private var numeralSpacing = 0
    private var handTruncation = 0
    private var hourHandTruncation = 0
    private var radius = 0
    private var paint = Paint()
    private var isInit = false
    private var numbers = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)
    private var rect = Rect()
    private var drawNumbers: Boolean? = false

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(
        context,
        attrs,
        R.styleable.ClockViewUserBackground_drawNumbers
    )

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : this(
        context!!,
        attrs,
        defStyleAttr,
        R.style.Theme_CustomClockViewForVK
    )

    init {

        if (attrs != null) {
            initAttributes(attrs, defStyleAttr, defStyleRes)
        }

    }

    private fun initAttributes(attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        val typedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.ClockViewUserBackground,
            defStyleAttr,
            defStyleRes
        )

        // parsing XML attributes
        drawNumbers =
            typedArray.getBoolean(R.styleable.ClockViewUserBackground_drawNumbers, true)
        typedArray.recycle()
    }

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

        /**
         * If the user wants to put a background like the one in the ClockViewDefaultBackground class,
         * he can use the custom attribute <drawNumbers> and remove drawing numbers from the View
         */
        if (drawNumbers!!) {
            drawNumeral(canvas)
        }

        drawHands(canvas)

        postInvalidateDelayed(500)
        invalidate()
    }

    private fun drawCircle(canvas: Canvas?) {
        paint.apply {
            reset()
            val config =
                resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
            color = if (config) {
                resources.getColor(android.R.color.white)
            } else {
                resources.getColor(android.R.color.black)
            }
            strokeWidth = 5f
            style = Paint.Style.STROKE
            isAntiAlias = true
        }
        canvas?.drawCircle(width / 2f, height / 2f, radius + padding - 10f, paint)
    }

    private fun drawCenter(canvas: Canvas?) {
        paint.style = Paint.Style.FILL
        canvas?.drawCircle(width / 2f, height / 2f, 12f, paint)
    }

    private fun drawNumeral(canvas: Canvas?) {
        paint.textSize = fontSize.toFloat()

        for (number in numbers) {
            val tmp = number.toString()
            paint.getTextBounds(tmp, 0, tmp.length, rect)
            val angle = Math.PI / 6 * (number - 3)
            val x = (width / 2 + cos(angle) * radius - rect.width() / 2).toFloat()
            val y = (width / 2 + sin(angle) * radius - (rect.height() / 2) + 30).toFloat()
            canvas?.drawText(tmp, x, y, paint)
        }
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