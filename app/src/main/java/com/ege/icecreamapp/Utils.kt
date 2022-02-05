package com.ege.icecreamapp

import android.view.View
import androidx.viewpager2.widget.ViewPager2

/*
https://stackoverflow.com/a/58088398
*/

fun ViewPager2.setupSideItems(nextItemVisibleDp: Int, currentItemHorizontalMarginDp: Int){
    this.apply {
        clipToPadding = false   // allow full width shown with padding
        clipChildren = false    // allow left/right item is not clipped
        offscreenPageLimit = 1
    }
    val nextItemVisiblePx = nextItemVisibleDp
    val currentItemHorizontalMarginPx = currentItemHorizontalMarginDp
    val pageTranslationX = nextItemVisiblePx + currentItemHorizontalMarginPx
    val pageTransformer = ViewPager2.PageTransformer { page: View, position: Float ->
        page.translationX = -pageTranslationX * position
        //Изменение высоты и ширины крайних элементов
        page.scaleY = 1 - (0.25f * kotlin.math.abs(position))
        page.scaleX = 1 - (0.25f * kotlin.math.abs(position))
        //Изменение прозрачности крайних элементов
        page.alpha = 0.30f + (1 - kotlin.math.abs(position))
    }
    this.setPageTransformer(pageTransformer)
}
