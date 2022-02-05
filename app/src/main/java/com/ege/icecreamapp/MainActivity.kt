package com.ege.icecreamapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.ege.icecreamapp.customView.Counter.OnChangeCount
import com.google.android.material.bottomsheet.BottomSheetDialog
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

        val bottomSheetDialog = prepareBottomSheet()
        customize.setOnClickListener {
            bottomSheetDialog.show()
        }

        buy.setOnClickListener {
            val count = counter.count

        }
    }

    private fun prepareBottomSheet(): BottomSheetDialog{
        val bottomSheet = BottomSheetDialog(this)
        bottomSheet.setContentView(R.layout.bottom_sheet)
        return bottomSheet
    }
}