package com.ege.icecreamapp

import android.content.Context
import android.content.Context.VIBRATOR_SERVICE
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.util.DisplayMetrics
import android.view.View
import android.widget.TextView
import androidx.viewpager2.widget.ViewPager2


fun convertDpToPx(dp: Int, context: Context): Float{
    return dp * (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
}

/*
https://stackoverflow.com/a/58088398
*/

fun ViewPager2.setupSideItems(nextItemVisibleDp: Int, currentItemHorizontalMarginDp: Int){
    this.apply {
        clipToPadding = false   // allow full width shown with padding
        clipChildren = false    // allow left/right item is not clipped
        offscreenPageLimit = 1
    }
    val nextItemVisiblePx = convertDpToPx(nextItemVisibleDp, context)
    val currentItemHorizontalMarginPx = convertDpToPx(currentItemHorizontalMarginDp, context)
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

fun calculatePriceIceCream(modelIceCream: ModelIceCream): Float{
    val sumSettings = modelIceCream.groupSetting.map{it.settings.map{ setting -> if (setting.isActivation) setting.cost else 0f}.sum()}.sum()
    val costSize = when(modelIceCream.size) {
        0 -> modelIceCream.price - 1f
        1 -> modelIceCream.price - 0.5f
        2 -> modelIceCream.price
        3 -> modelIceCream.price + 1f
        else -> modelIceCream.price
    }
    return (sumSettings + costSize) * modelIceCream.countBuy
}

fun TextView.setTextWithEndSim(text: CharSequence, sim: Char) {
    this.text = "$text $sim"
}

fun vibrate(context: Context){
    val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager =  context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        vibratorManager.defaultVibrator;
    } else {
        context.getSystemService(VIBRATOR_SERVICE) as Vibrator
    }
    vibrator.vibrate(VibrationEffect.createOneShot(100, 1))
}
