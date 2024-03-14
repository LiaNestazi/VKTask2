package com.example.vktask2.view

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.example.vktask2.ui.navigation.SetupNavGraph
import com.example.vktask2.viewmodel.MainViewModel

@Composable
fun MainPage(context: Context, mainViewModel: MainViewModel) {
    val navController = rememberNavController()
    Box {
        SetupNavGraph(context, navController, mainViewModel)
        navController.navigate("SitesList", navOptions = null)
    }
}