package com.example.vktask2.view

import android.content.Context
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
import com.example.vktask2.R
import com.example.vktask2.crypto.CryptoManager
import com.example.vktask2.model.MasterPassword
import com.example.vktask2.repository.SharedPreferencesSource
import com.google.gson.Gson

@Composable
fun WelcomeDialog(context: Context, reloadFunc: () -> Unit){

    val sharedPreferences = SharedPreferencesSource.getInstance(context).sharedPreferences
    val password = remember {
        mutableStateOf("")
    }
    val passwordVisible = remember {
        mutableStateOf(false)
    }
    AlertDialog(
        onDismissRequest = {

        },
        title = {
            Text(
                text = "Добро пожаловать в менеджер паролей!",
                modifier = Modifier
                    .padding(top = 8.dp, start = 8.dp, end = 8.dp)
                    .fillMaxWidth(),
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                fontSize = 18.sp
            )
        },
        text = {
            Text(
                text = "Прежде, чем продолжить, необходимо установить мастер-пароль."+
                "\nОн необходим для получения доступа к Вашим паролям.",
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontSize = 14.sp,
                color = Color.Gray
            )
        },
        buttons = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    value = password.value,
                    onValueChange = { password.value = it },
                    label = { Text("Мастер-пароль") },
                    singleLine = true,
                    placeholder = { },
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
                    onClick = {
                        if (password.value == ""){
                            Toast.makeText(context, "Поле не может быть пустым", Toast.LENGTH_SHORT).show()
                        } else{
                            if (password.value.length < 4){
                                Toast.makeText(context, "Длина пароля должна быть больше 4 символов", Toast.LENGTH_SHORT).show()
                            } else {

                                val cryptoManager = CryptoManager()
                                val passwordBytes = password.value.encodeToByteArray()
                                val (encryptedPassword, iv) = cryptoManager.encrypt(passwordBytes)
                                val masterPassword = MasterPassword(encryptedPassword, iv)
                                val gson = Gson()
                                val json = gson.toJson(masterPassword)

                                with ( sharedPreferences.edit()) {
                                    putString("masterPassword", json)
                                    apply()
                                }
                                reloadFunc()
                            }
                        }
                              },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)
                ) {
                    Text(
                        "Сохранить",
                        textAlign = TextAlign.Center,
                        fontSize = 14.sp
                    )
                }
            }
        }
    )
}