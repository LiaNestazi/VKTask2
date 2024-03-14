package com.example.vktask2.view

import android.content.Context
import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.vktask2.R
import com.example.vktask2.crypto.CryptoManager
import com.example.vktask2.model.Site
import com.example.vktask2.repository.SharedPreferencesSource
import com.example.vktask2.viewmodel.MainViewModel
import com.google.gson.Gson
import kotlin.concurrent.thread

@Composable
fun AddSitePage(context: Context, mainViewModel: MainViewModel, navController: NavHostController, url: String) {
    val sharedPreferences = SharedPreferencesSource.getInstance(context).sharedPreferences

    val cryptoManager = CryptoManager()
    var decryptedPassword = ""
    val gson = Gson()
    val json = sharedPreferences.getString(url, "")
    val site = gson.fromJson(json, Site::class.java)
    if (site != null){
        if (site.pass != null && site.iv != null){
            decryptedPassword = cryptoManager.decrypt(site.pass!!, site.iv!!).decodeToString()
        }
    }

    val title = remember {
        mutableStateOf("")
    }
    val siteUrl = remember {
        mutableStateOf(url)
    }
    val password = remember {
        mutableStateOf(decryptedPassword)
    }
    val passwordVisible = remember {
        mutableStateOf(false)
    }
    if (url == ""){
        title.value = "Добавление нового сайта"

    } else{
        title.value = "Редактирование пароля"
    }
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
    } else{
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_arrow_back_ios_new_24),
                        contentDescription = null,
                        tint = colorResource(id = R.color.indigo_900)
                    )
                }
                Text(
                    text = title.value,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp,
                    color = colorResource(id = R.color.indigo_900)
                )
            }
            Column(
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Text(modifier = Modifier.padding(horizontal = 16.dp),
                    text = "Сайт",
                    color = colorResource(id = R.color.indigo_900),
                    textAlign = TextAlign.Start
                )
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    value = siteUrl.value,
                    onValueChange = { siteUrl.value = it },
                    leadingIcon =
                    {
                        Text(
                            modifier = Modifier.padding(start = 16.dp),
                            text = "https://",
                            color = Color.Black
                        )
                    },
                    singleLine = true,
                    placeholder = { },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri),
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = Color.Black,
                        cursorColor = Color.Black,
                        trailingIconColor = Color.Gray,
                        focusedLabelColor = colorResource(id = R.color.indigo_900),
                        backgroundColor = Color.White,
                        focusedIndicatorColor = colorResource(id = R.color.indigo_900)
                    )
                )
                Text(modifier = Modifier.padding(start = 16.dp, top = 16.dp),
                    text = "Пароль",
                    color = colorResource(id = R.color.indigo_900),
                    textAlign = TextAlign.Start
                )
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    value = password.value,
                    onValueChange = { password.value = it },
                    singleLine = true,
                    placeholder = { Text("Введите пароль")},
                    visualTransformation = if (passwordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    trailingIcon = {
                        val image = if (passwordVisible.value)
                            R.drawable.baseline_visibility_24
                        else R.drawable.baseline_visibility_off_24

                        val description = if (passwordVisible.value) "Скрыть пароль" else "Показать пароль"

                        IconButton(onClick = {passwordVisible.value = !passwordVisible.value}){
                            Icon(painter = painterResource(id = image), contentDescription = description)
                        }
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = Color.Black,
                        cursorColor = Color.Black,
                        trailingIconColor = Color.Gray,
                        focusedLabelColor = colorResource(id = R.color.indigo_900),
                        backgroundColor = Color.White,
                        focusedIndicatorColor = colorResource(id = R.color.indigo_900)
                    )
                )

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    onClick = {
                        saveChanges(context, cryptoManager, mainViewModel, navController, url, siteUrl.value, password.value)
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.indigo_900), contentColor = Color.White)
                ) {
                    Text(
                        "Сохранить",
                        textAlign = TextAlign.Center,
                        fontSize = 16.sp
                    )
                }

            }

        }
    }

}

fun String.isValidUrl(): Boolean = Patterns.WEB_URL.matcher(this).matches()

fun saveChanges(context: Context, cryptoManager: CryptoManager, mainViewModel: MainViewModel, navController: NavHostController, url: String, newSiteUrl: String, password: String) {
    val sharedPreferences = SharedPreferencesSource.getInstance(context).sharedPreferences

    if (newSiteUrl == "" || password == ""){
        Toast.makeText(context, "Поля не могут быть пустыми", Toast.LENGTH_SHORT).show()
    } else{
        if (!"https://$newSiteUrl".isValidUrl()){
            Toast.makeText(context, "Введите верный URL", Toast.LENGTH_SHORT).show()
        } else{
            //Шифрование пароля
            val passwordBytes = password.encodeToByteArray()
            val (encryptedPassword, iv) = cryptoManager.encrypt(passwordBytes)

            val gson = Gson()
            //Если сайт уже был сохранен и сейчас редактируется
            if (url != "" || sharedPreferences.all.keys.contains(newSiteUrl)){
                var json = sharedPreferences.getString(url, "")
                if (json == ""){
                    json = sharedPreferences.getString(newSiteUrl, "")
                }
                val site = gson.fromJson(json, Site::class.java)
                var edittedSite = Site(newSiteUrl, encryptedPassword, iv, site.icon_url)
                var siteToJson = gson.toJson(edittedSite)

                //Если сайт изменился, то заново получаем иконку
                if (url != newSiteUrl){
                    lateinit var iconHref: String
                    try{
                        mainViewModel.isLoading.value = true
                        val t = thread(start = true) {
                            iconHref = mainViewModel.getSiteIcon(newSiteUrl)
                        }
                        t.join()
                    } catch (e: Exception) {
                        iconHref = ""
                    }
                    mainViewModel.isLoading.value = false
                    edittedSite = Site(edittedSite.url, edittedSite.pass, edittedSite.iv, iconHref)
                    siteToJson = gson.toJson(edittedSite)

                    with (sharedPreferences.edit()) {
                        remove(url)
                        putString(newSiteUrl, siteToJson)
                        apply()
                    }

                } else{
                    with (sharedPreferences.edit()) {
                        remove(url)
                        putString(newSiteUrl, siteToJson)
                        apply()
                    }
                }
            } else{
                //Иначе создаем новый сайт
                lateinit var iconHref: String

                try{
                    mainViewModel.isLoading.value = true
                    val t = thread(start = true) {
                        iconHref = mainViewModel.getSiteIcon(newSiteUrl)
                    }
                    t.join()
                } catch (e: Exception) {
                    iconHref = ""
                }
                mainViewModel.isLoading.value = false

                val site = Site(newSiteUrl, encryptedPassword, iv, iconHref)
                val siteToJson = gson.toJson(site)
                with (sharedPreferences.edit()) {
                    putString(newSiteUrl, siteToJson)
                    apply()
                }
            }

            Toast.makeText(context, "Сохранение прошло успешно!", Toast.LENGTH_SHORT).show()
            navController.popBackStack()
        }
    }
}
