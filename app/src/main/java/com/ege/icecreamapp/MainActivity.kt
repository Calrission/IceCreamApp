package com.ege.icecreamapp

import android.media.Image
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.ege.icecreamapp.customView.Counter.OnChangeCount
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.bottom_sheet.*

class MainActivity : AppCompatActivity() {

    private val menu: MutableList<ModelIceCream> = StaticData.ice_creams
    lateinit var bottomSheetDialog: BottomSheetDialog

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

        bottomSheetDialog = createBottomSheet()
        customize.setOnClickListener {
            bottomSheetDialog.showWithPrepare(menu[pager.currentItem])
        }

        buy.setOnClickListener {
            val count = counter.count

        }
    }

    private fun createBottomSheet(): BottomSheetDialog{
        val bottomSheet = BottomSheetDialog(this)
        bottomSheet.setContentView(R.layout.bottom_sheet)
        return bottomSheet
    }

    private fun BottomSheetDialog.showWithPrepare(modelIceCream: ModelIceCream){
        val views_size = mapOf<ImageView, Pair<Int, Int>>(
            s_size to Pair(R.drawable.ic_s_size, R.drawable.ic_s_size_no),
            m_size to Pair(R.drawable.ic_m_size, R.drawable.ic_m_size_no),
            l_size to Pair(R.drawable.ic_l_size, R.drawable.ic_l_size_no),
            xl_size to Pair(R.drawable.ic_xl_size, R.drawable.ic_xl_size_no)
        )
        fun switchSelectedSize(nowPositionSelected: Int){
            views_size.forEach{it.key.setImageResource(it.value.second)}
            val nowImageView = views_size.keys.toList()[nowPositionSelected]
            val nowPair = views_size[nowImageView]!!
            nowImageView.setImageResource(nowPair.first)
        }
        this.title_bottom_sheet.text = modelIceCream.title
        switchSelectedSize(modelIceCream.size)
        views_size.forEach{
            it.key.setOnClickListener { imageView ->
                val position = views_size.keys.toList().indexOf(imageView as ImageView)
                switchSelectedSize(position)
                modelIceCream.size = position
            }
        }
        this.rec_settings.apply {
            adapter = AdapterGroupSettings(modelIceCream.groupSetting)
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
        }
        this.close.setOnClickListener {
            this.hide()
        }
        this.show()
    }

}