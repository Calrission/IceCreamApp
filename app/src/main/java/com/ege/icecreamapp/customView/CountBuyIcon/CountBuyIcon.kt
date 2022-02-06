package com.ege.icecreamapp.customView.CountBuyIcon

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.ege.icecreamapp.MainScreen.IceCreamAdapter
import com.ege.icecreamapp.ModelIceCream
import com.ege.icecreamapp.R
import com.ege.icecreamapp.customView.Counter.OnChangeCount
import kotlinx.android.synthetic.main.layout_caount_buy_icon.view.*

class CountBuyIcon: LinearLayout {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context, attributeSet: AttributeSet?, defStyle: Int) : super(context, attributeSet, defStyle)

    private val buyIceCreams: MutableList<ModelIceCream> = mutableListOf()
    var onChangeCount: OnChangeCount? = null

    init {
        inflate(context, R.layout.layout_caount_buy_icon, this)
        updateCountView()
    }

    fun addBuyModelIceCream(modelIceCream: ModelIceCream){
        buyIceCreams.add(modelIceCream)
        updateCountViewAndSend()
    }

    fun removeModelIceCreamIndex(index: Int){
        buyIceCreams.removeAt(index)
        updateCountViewAndSend()
    }

    private fun updateCountViewAndSend(){
        onChangeCount?.onChange(buyIceCreams.size)
        updateCountView()
    }

    private fun updateCountView(){
        val size = buyIceCreams.size
        if (size != 0){
            counter_buy.visibility = View.VISIBLE
            counter_buy.text = if (size <= 99) size.toString() else "99"
        }else{
            counter_buy.text = "0"
            counter_buy.visibility = View.INVISIBLE
        }
    }
}