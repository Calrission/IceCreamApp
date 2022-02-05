package com.ege.icecreamapp.customView.Counter

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.ege.icecreamapp.R
import kotlinx.android.synthetic.main.counter_layout.view.*

class Counter: LinearLayout{
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context, attributeSet: AttributeSet?, defStyle: Int) : super(context, attributeSet, defStyle) {
        initView(context)
    }

    var onChangeCount: OnChangeCount? = null
    var count = -1

    private fun init() {
        updateCounter(1)
        add_count.setOnClickListener {
            addOneCounter()
        }
        remove_count.setOnClickListener {
            removeOneCounter()
        }
    }

    private fun initView(context: Context){
        inflate(context, R.layout.counter_layout, this)
        orientation = HORIZONTAL
        init()
    }

    fun updateCounter(newCounter: Int){
        count = newCounter
        setCounterToTextView()
    }

    private fun addOneCounter(){
        if (count < 999) {
            updateCounter(count + 1)
            onChangeCount?.onChange(count)
        }
    }

    private fun removeOneCounter(){
        if (count > 1) {
            updateCounter(count - 1)
            onChangeCount?.onChange(count)
        }
    }

    private fun setCounterToTextView(){
        count_txt.text = count.toString()
    }
}