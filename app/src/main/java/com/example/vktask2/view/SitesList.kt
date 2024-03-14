package com.example.vktask2.view

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.vktask2.R
import com.example.vktask2.model.Site
import com.example.vktask2.repository.SharedPreferencesSource
import com.example.vktask2.viewmodel.MainViewModel
import com.google.gson.Gson

@Composable
fun SitesList(context: Context, navController: NavHostController, mainViewModel: MainViewModel) {
    val sharedPreferences = SharedPreferencesSource.getInstance(context).sharedPreferences
    val sitesList = sharedPreferences.all.keys

    if (mainViewModel.isLoading.value){
        Box(modifier = Modifier
            .fillMaxSize()
        ){
            CircularProgressIndicator(
                modifier = Modifier
                    .width(60.dp)
                    .align(Alignment.Center),
                color = MaterialTheme.colors.onSecondary
            )
        }
    }else{
        Box(modifier = Modifier
            .fillMaxSize()) {
            Column(modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(16.dp)) {
                Text(
                    text = "Password Manager",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp,
                    color = colorResource(id = R.color.indigo_900)
                )
                if (sitesList.size < 2){
                    Text(
                        text = "Здесь пока ничего нет",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        textAlign = TextAlign.Center,
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.padding(vertical = 8.dp),
                        content = {
                            items(sitesList.toList()) {
                                SwipeToDeleteContainer(
                                    item = it,
                                    onDelete = {
                                        with (sharedPreferences.edit()) {
                                            remove(it)
                                            apply()
                                        }
                                    }
                                ) {
                                    if(it != "masterPassword"){
                                        val gson = Gson()
                                        val json = sharedPreferences.getString(it, "")
                                        val site = gson.fromJson(json, Site::class.java)

                                        SiteCard(
                                            context = context,
                                            navController = navController,
                                            item = site
                                        )
                                    }
                                }
                            }
                        })
                }
            }
            Button(
                onClick = { navController.navigate("AddSite") },
                modifier = Modifier
                    .padding(24.dp)
                    .size(64.dp)
                    .align(Alignment.BottomCenter),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.indigo_900), contentColor = Color.White)
            ) {
                Icon(painter = painterResource(id = R.drawable.baseline_add_24), contentDescription = null)
            }

        }
    }
}