package com.example.vktask2.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import org.jsoup.Jsoup


class MainViewModel(application: Application): AndroidViewModel(application) {
    var isLoading = mutableStateOf(false)

    fun getSiteIcon(url: String): String{
        var properUrl = url
        if (!properUrl!!.startsWith("https://")) {
            properUrl = "https://" + properUrl
        }
        try {
            val doc = Jsoup.connect(properUrl).ignoreHttpErrors(true).get()
            val linkElements = doc.select("link")
            val iconsHrefs = mutableListOf<String>()
            for (element in linkElements) {
                val rel = element.attr("rel")
                val href = element.attr("href")
                if (rel.contains("icon")) {
                    iconsHrefs.add(href)
                }
            }
            if (iconsHrefs.size < 1){
                return ""
            } else{
                return iconsHrefs[iconsHrefs.size - 1]
            }
        } catch (e: Exception){
            e.message
        }
        return ""

    }
}