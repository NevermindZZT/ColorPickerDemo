package com.letter.colorpicker

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

/**
 * 颜色选择器对话框
 * @author Letter(zhangkeqiang@ut.cn)
 * @version 1.0
 */
class ColorPickerDialog(context: Context, theme: Int) : Dialog(context, theme),
        View.OnClickListener,
        ColorPickerView.OnColorClickListener {

    private var colorPickerMain: ColorPickerView                    /* 颜色选择器主视图 */
    private var colorPickerSub: ColorPickerView                     /* 颜色选择器副视图 */
    private var positiveButton: Button                              /* 确认按钮 */
    private var negativeButton: Button                              /* 取消按钮 */

    private var selectColor: Int = 0                                /* 被选中的颜色 */

    private var colors: ArrayList<Int> = arrayListOf(0, 0, 0, 0)    /* 副视图颜色 */

    private var onColorSelectListener: OnColorSelectListener? = null    /* 监听 */

    init {
        val view =  LayoutInflater.from(context).inflate(R.layout.dialog_color_picker, null)
        addContentView(view, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT))

        colorPickerMain = view.findViewById(R.id.color_picker_main)
        colorPickerSub = view.findViewById(R.id.color_picker_sub)
        positiveButton = view.findViewById(R.id.positive_button)
        negativeButton = view.findViewById(R.id.negative_button)

        positiveButton.setOnClickListener(this)
        negativeButton.setOnClickListener(this)

        colorPickerMain.setWidthWrapContent()
        colorPickerMain.onColorClickListener = this

        colorPickerSub.setWidthWrapContent()
        colorPickerSub.onColorClickListener = this
    }

    constructor(context: Context): this(context, R.style.ColorPickerDialogTheme)

    constructor(builder: Builder): this(builder.context, builder.getTheme()) {
        this.onColorSelectListener = builder.getOnColorSelectListener()
        this.selectColor = builder.getColor()
        if (builder.getColors().isNotEmpty()) {
            setColors(builder.getColors())
        }
        freshColorPickerSub(selectColor, colors.size)
        colorPickerSub.selectedItem = colors.size - 1
    }

    override fun onClick(v: View?) {
        when (v) {
            positiveButton -> {
                onColorSelectListener?.onColorSelect(selectColor)
                dismiss()
            }
            negativeButton -> {
                dismiss()
            }
        }
    }

    override fun onColorClick(view: View, color: Int) {
        when (view) {
            colorPickerMain -> {
                freshColorPickerSub(color, colors.size + 1)
                selectColor = color
            }
            colorPickerSub -> {
                colorPickerMain.cancelSelect()
                selectColor = color
            }
        }
    }

    private fun freshColorPickerSub(color: Int, level: Int) {
        Log.d("Color", "color:$color")
        val stepR = (0XFF - color.and(0x00FF0000).ushr(16)) / level
        val stepG = (0xFF - color.and(0x0000FF00).ushr(8)) / level
        val stepB = (0xFF - color.and(0x000000FF)) / level
        for (i in colors.indices) {
            colors[i] = (color + (stepR * (level - i - 1)).shl(16)
                    + (stepG * (level - i - 1)).shl(8)
                    + (stepB * (level - i - 1)))
        }
        colorPickerSub.setColors(colors)
    }

    fun setColors(colorStrings: Array<String>) {
        colorPickerMain.setColors(colorStrings)
    }

    fun setColors(colors: ArrayList<Int>) {
        colorPickerMain.setColors(colors)
    }

    class Builder(var context: Context) {
        private var theme = R.style.ColorPickerDialogTheme
        private var onColorSelectListener: OnColorSelectListener? = null
        private var color = 0
        private var colors = arrayListOf<Int>()

        fun setTheme(theme: Int): Builder {
            this.theme = theme
            return this
        }

        fun getTheme(): Int {
            return theme
        }

        fun getOnColorSelectListener(): OnColorSelectListener? {
            return onColorSelectListener
        }

        fun setOnColorSelectListener(onColorSelectListener: OnColorSelectListener): Builder {
            this.onColorSelectListener = onColorSelectListener
            return this
        }

        fun getColor(): Int {
            return color
        }

        fun setColor(color: Int): Builder {
            this.color = color
            return this
        }

        fun getColors(): ArrayList<Int> {
            return colors
        }

        fun setColors(colorStrings: Array<String>): Builder {
            for (colorString in colorStrings) {
                colors.add(Color.parseColor(colorString))
            }
            return this
        }

        fun setColors(colors: ArrayList<Int>): Builder {
            this.colors = colors
            return this
        }

        fun create(): ColorPickerDialog {
            return ColorPickerDialog(this)
        }
    }

    interface OnColorSelectListener {
        fun onColorSelect(color: Int)
    }
}