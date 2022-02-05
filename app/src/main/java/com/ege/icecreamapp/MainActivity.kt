package com.ege.icecreamapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.ege.icecreamapp.customView.Counter.OnChangeCount
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val menu: MutableList<ModelIceCream> = StaticData.ice_creams

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        pager.setupSideItems(140, 100)
        pager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                val model = menu[position]
                title_ice_cream.text = model.title
                desc.text = model.desc
                buy.text = "${model.price} €"
                edition_ice_cream.text = "${model.edition} edition"
                counter.updateCounter(model.countBuy)
            }
        })
        pager.adapter = IceCreamAdapter(menu)

        scroll_content.viewTreeObserver.addOnScrollChangedListener {
            val scrollY: Int = scroll_content.scrollY
            gradient.alpha = 0.01f * kotlin.math.abs(scrollY) / 5
        }

        counter.onChangeCount = object: OnChangeCount{
            override fun onChange(newCount: Int) {
                menu[pager.currentItem].countBuy = newCount
            }
        }

        customize.setOnClickListener {

        }

        buy.setOnClickListener {
            val count = counter.count

        }
    }
}