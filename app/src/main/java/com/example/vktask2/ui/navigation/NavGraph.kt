package com.example.vktask2.ui.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.vktask2.view.AddSitePage
import com.example.vktask2.view.SitesList
import com.example.vktask2.viewmodel.MainViewModel

@Composable
fun SetupNavGraph(
    context: Context,
    navController: NavHostController,
    mainViewModel: MainViewModel
) {

    NavHost(navController = navController, startDestination = "SitesList") {
        composable("SitesList") {
            SitesList(context, navController, mainViewModel)
        }
        composable("AddSite") {
            AddSitePage(context, mainViewModel, navController, "")
        }
        composable(
            route = "AddSite/{url}",
            arguments = listOf(
                navArgument("url"){
                    type = NavType.StringType
                },
            )
        ){
            AddSitePage(
                context = context,
                mainViewModel = mainViewModel,
                navController = navController,
                url = it.arguments?.getString("url").toString()
            )
        }
    }
}