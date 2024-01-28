package com.example.push

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.push.ui.theme.Back
import com.example.push.ui.theme.DarkAccent
import com.example.push.ui.theme.Main
import com.example.push.ui.theme.PushTheme
import com.example.push.ui.theme.Sec
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class JoinGroup : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            receive()
            Log.i("MADE-TO", "this is set content for loop for receive")
        }
    }

}

@Composable
fun receive(){
    val tContext = LocalContext.current
    Column(modifier = Modifier
        .fillMaxSize()
        .background(color = Back), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Card(modifier = Modifier.padding(8.dp), elevation = CardDefaults.cardElevation(defaultElevation = 4.dp), colors = CardDefaults.cardColors(containerColor = Sec)){
            Row(modifier = Modifier.fillMaxWidth(),verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center){
                Text(text="CHOOSE A GROUP TO JOIN", color = Back, fontSize = 22.sp, textAlign = TextAlign.Center, modifier = Modifier.padding(8.dp))
            }

            Row (modifier = Modifier.fillMaxWidth(),verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                ElevatedButton(onClick = { common.group = "env"; common.add = 0; tContext.startActivity(Intent(tContext, LoadTask::class.java))}, modifier = Modifier.padding(2.dp)) {
                    Text("Environmental", fontSize = 13.sp)
                }
                ElevatedButton(onClick = { common.group = "soc"; common.add = 0; tContext.startActivity(Intent(tContext, LoadTask::class.java)) }, modifier = Modifier.padding(2.dp)) {
                    Text("Societal",fontSize = 13.sp)
                }
                ElevatedButton(onClick = { common.group = "no"; common.add = 0; tContext.startActivity(Intent(tContext, LoadTask::class.java)) }, modifier = Modifier.padding(2.dp)) {
                    Text("None", fontSize = 12.sp)
                }
            }

        }

    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun DeffPreview(){
    PushTheme {
        receive()
    }
}