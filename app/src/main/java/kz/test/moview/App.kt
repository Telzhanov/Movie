package kz.test.moview

import android.app.Application
import android.content.Context
import android.content.SharedPreferences

class App:Application(){
    companion object {
        var sharedPreference:SharedPreferences?=null
    }
    override fun onCreate() {
        super.onCreate()
        sharedPreference = getSharedPreferences("LOCAL_STORAGE", Context.MODE_PRIVATE)
    }
}