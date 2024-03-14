package com.example.vktask2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import com.example.vktask2.repository.SharedPreferencesSource
import com.example.vktask2.ui.theme.VKTask2Theme
import com.example.vktask2.view.MainPage
import com.example.vktask2.view.WelcomeDialog
import com.example.vktask2.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mainViewModel by viewModels<MainViewModel>()
        val sharedPreferences = SharedPreferencesSource.getInstance(this).sharedPreferences

        val masterPassword = sharedPreferences.getString("masterPassword", "")

        setContent {
            VKTask2Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    if (masterPassword != null && masterPassword == ""){
                        WelcomeDialog(context = this, reloadFunc = {
                            finish()
                            startActivity(intent)
                        })
                    } else{
                        MainPage(this, mainViewModel)
                    }
                }
            }
        }
    }
}