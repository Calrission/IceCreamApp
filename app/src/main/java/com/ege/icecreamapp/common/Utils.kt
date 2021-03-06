package com.ege.icecreamapp

import android.content.Context
import android.content.Context.VIBRATOR_SERVICE
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.util.DisplayMetrics
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.ege.icecreamapp.common.API
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


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

fun TextView.setTextWithEndText(text: CharSequence, sim: String) {
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

fun initRetrofit(): API {
    return Retrofit.Builder()
        .baseUrl("https://api.openrouteservice.org/v2/directions/foot-walking/")
        .addConverterFactory(GsonConverterFactory.create())
        .build().create(API::class.java)
}
