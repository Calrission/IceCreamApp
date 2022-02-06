package com.ege.icecreamapp.customView.Counter

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.ege.icecreamapp.R
import com.ege.icecreamapp.vibrate
import kotlinx.android.synthetic.main.counter_layout.view.*

class Counter: LinearLayout{
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context, attributeSet: AttributeSet?, defStyle: Int) : super(context, attributeSet, defStyle)

    var onChangeCount: OnChangeCount? = null
    var count = -1

    init {
        inflate(context, R.layout.counter_layout, this)
        orientation = HORIZONTAL

        updateCounter(1)
        add_count.setOnClickListener {
            addOneCounter()
        }
        remove_count.setOnClickListener {
            removeOneCounter()
        }
        this.setOnLongClickListener {
            updateCounterAndSend(1)
            vibrate(context)
            return@setOnLongClickListener true
        }
    }

    fun updateCounter(newCounter: Int){
        count = newCounter
        setCounterToTextView()
    }

    private fun updateCounterAndSend(newCounter: Int){
        count = newCounter
        setCounterToTextView()
        onChangeCount?.onChange(count)
    }

    private fun addOneCounter(){
        if (count < 999)
            updateCounterAndSend(count + 1)
    }

    private fun removeOneCounter(){
        if (count > 1)
            updateCounterAndSend(count - 1)
    }

    private fun setCounterToTextView(){
        count_txt.text = count.toString()
    }
}