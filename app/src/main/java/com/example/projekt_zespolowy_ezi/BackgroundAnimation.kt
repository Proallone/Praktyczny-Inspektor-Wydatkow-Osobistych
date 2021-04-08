package com.example.projekt_zespolowy_ezi

import android.graphics.drawable.AnimationDrawable
import android.view.View
import android.widget.RelativeLayout

/**
 * Objekt pozwalający na wywołanie funkcji animacji tła w dowolnej innej klasie (żeby nie powielać
 * tego samego kodu w każdej klasie (activity)
 * Animację tła można wywołać w dowolnej klasie która tworzy layout (na razie obłsuga
 * tylko layoutu RelativeLayout
 */
object BackgroundAnimation {
   fun animateUI(layout: View, EnterDuration: Int = 4000, ExitDuration: Int = 4000){
        /*Funkcja odpowiadająca za animację tła*/
        val animationDrawable = layout.background as AnimationDrawable
        animationDrawable.setEnterFadeDuration(EnterDuration)
        animationDrawable.setExitFadeDuration(ExitDuration)
        animationDrawable.start()
    }
}