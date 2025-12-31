package com.refactoring_android.math_skill.core

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet

import android.view.View
import androidx.core.content.ContextCompat

class InputNumber @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var redis: Int = 0
    private var fontSize: Int = 20
    var number: Int? = null  // 표시할 숫자

    init{
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.InputNumber)
        redis = typedArray.getInteger(R.styleable.InputNumber_redis, 0)
        fontSize = typedArray.getInteger(R.styleable.InputNumber_fontSize, 30)
        typedArray.recycle()
    }

    fun setValue(number: Int?) {
        this.number = number
        invalidate()
    }


    private val circlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.primaryColor)
        style = Paint.Style.FILL
    }

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE // 숫자 색상
        textSize = fontSize.toFloat()     // 숫자 크기
        textAlign = Paint.Align.CENTER
    }

    private val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK // 테두리 색상
        style = Paint.Style.STROKE // 테두리 스타일
        strokeWidth = 0.5f // 테두리 두께 1px
    }

    private val whitePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE // 테두리 색상
    }




    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val radius = (width.coerceAtMost(height) / 2).toFloat() - 0.5f
        val centerX = width / 2f
        val centerY = height / 2f

        if (number == null) {
            // 내부 흰색 원 + 테두리
            canvas.drawCircle(centerX, centerY, radius - 1f, whitePaint)
            canvas.drawCircle(centerX, centerY, radius, borderPaint)
        } else {
            // 내부 색 변경 + 테두리 (흰색 원 덮기)
            canvas.drawCircle(centerX, centerY, radius, circlePaint)
        }

        val textOffsetY = (textPaint.descent() + textPaint.ascent()) / 2
        canvas.drawText(number?.toString() ?: "", centerX, centerY - textOffsetY, textPaint)
    }
}
