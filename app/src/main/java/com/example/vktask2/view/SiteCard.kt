package com.example.vktask2.view

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.example.vktask2.model.Site
import com.example.vktask2.R
import com.example.vktask2.repository.SharedPreferencesSource
import com.example.vktask2.viewmodel.MainViewModel

@Composable
fun SiteCard(context: Context, navController: NavHostController, item: Site) {
    val sharedPreferences = SharedPreferencesSource.getInstance(context).sharedPreferences
    val showDialog = remember {
        mutableStateOf(false)
    }
    if (showDialog.value) {
        CheckMasterPassDialog(context, sharedPreferences, showDialog, navController, item.url!!)
    }

    Column(modifier = Modifier
        .fillMaxWidth()
        .background(Color.White)
        .clickable {
            showDialog.value = true
        }){
        Row(modifier = Modifier.padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (item.icon_url == ""){
                Image(
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(64.dp),
                    painter = painterResource(id = R.drawable.baseline_public_24),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
            } else{
                var icon = item.icon_url!!
                if (icon.startsWith("/")){
                    icon = item.url+icon
                }
                if (!icon.startsWith("https://")){
                    icon = "https://"+icon
                }

                if (icon.endsWith(".svg")){
                    AsyncImage(
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(64.dp),
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(icon)
                            .decoderFactory(SvgDecoder.Factory())
                            .build(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                    )
                } else{
                    AsyncImage(
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(64.dp),
                        model = icon,
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                    )
                }
            }
            Column() {
                Text(
                    text = "https://${item.url}",
                    modifier = Modifier.padding(8.dp),
                    textAlign = TextAlign.Start,
                    fontSize = 20.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        Divider(modifier = Modifier
            .fillMaxWidth()
            .size(1.dp)
            .padding(horizontal = 8.dp),
            color = colorResource(id = R.color.gray_400)
        )
    }

}