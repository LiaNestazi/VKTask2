package com.example.vktask2.repository

import android.content.Context


class SharedPreferencesSource(context: Context){

    val sharedPreferences = context.getSharedPreferences("sitesPref", Context.MODE_PRIVATE)

    companion object{
        private var instance: SharedPreferencesSource? = null

        fun getInstance(context: Context): SharedPreferencesSource {
            if (instance == null){
                instance = SharedPreferencesSource(context)
            }
            return instance as SharedPreferencesSource
        }

    }




}