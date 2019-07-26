package com.letter.colorpickerdemo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.letter.colorpicker.ColorPickerDialog

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private var button: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button = findViewById(R.id.button)
        button?.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            button -> {
                val dialog = ColorPickerDialog.Builder(this)
                    .setOnColorSelectListener{}
                    .setSelectedColor(0)
                    .setColors(resources.getStringArray(R.array.color_picker_values))
                    .setColumns(5)
                    .create()
                dialog.show()
            }
        }
    }

}
