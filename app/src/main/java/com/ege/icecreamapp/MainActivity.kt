package com.ege.icecreamapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        pager.setupSideItems(140, 42)
        pager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                val model = StaticData.ice_creams[position]
                title_ice_cream.text = model.title
                desc.text = model.desc
                buy.text = "${model.price} €"
                edition_ice_cream.text = "${model.edition} edition"
            }
        })
        pager.adapter = IceCreamAdapter(StaticData.ice_creams)

        scroll_content.viewTreeObserver.addOnScrollChangedListener {
            val scrollY: Int = scroll_content.scrollY
            gradient.alpha = 0.01f * kotlin.math.abs(scrollY) / 5
            Log.e("alpha-gradient", gradient.alpha.toString())
        }
    }
}