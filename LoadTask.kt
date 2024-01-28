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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoadTask : ComponentActivity() {
    private val BASE_URL = "http://REMOVED FOR PRIVACY/"

    override fun onCreate(savedInstanceState: Bundle?) {
        getResponse()
        doGroup()
        super.onCreate(savedInstanceState)
        setContent {
            loadTask()
            Log.i("MADE-TO", "this is set content for loop for loadTask")
        }



    }
    private fun getResponse(){

        val chatapi = Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build().create(callChat::class.java)

        GlobalScope.launch(Dispatchers.IO) {
            Log.i("MADE-TO", "this is start of getResponse for loop for loadTask")
            val response = chatapi.getResponse(common.group)
            if(response.isSuccessful){
                Log.i("MADE-TO-Load", response.body()!!.message)
                common.task = response.body()!!.message
                Log.i("MADE-TO-Load-common", common.task)
            }
        }
    }
    private fun doGroup(){
        val service = Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build().create(confirmation::class.java)

        GlobalScope.launch(Dispatchers.IO){
            val response = service.getconfirm(common.uid, common.group)
            Log.i("MADE-TO", response.body()!!.message)
        }
    }

}

@Composable
fun loadTask(){
    val tContext = LocalContext.current
    Column(modifier = Modifier
        .fillMaxSize()
        .background(color = Back), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Card(modifier = Modifier.padding(8.dp), elevation = CardDefaults.cardElevation(defaultElevation = 4.dp), colors = CardDefaults.cardColors(containerColor = Sec)){
            Row(modifier = Modifier.fillMaxWidth(),verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center){
                Text(text=common.task, color = Back, fontSize = 32.sp, textAlign = TextAlign.Center, modifier = Modifier.padding(8.dp))
                Log.i("MADE-TO-load-common", common.task)
            }

            Row (modifier = Modifier.fillMaxWidth(),verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                ElevatedButton(onClick = { common.add = 1; tContext.startActivity(Intent(tContext, MainActivity::class.java))  }, modifier = Modifier.padding(8.dp)) {
                    Text("Completed")
                }
                ElevatedButton(onClick = { if(common.add != 0){common.add = 0}else{common.add=0}; tContext.startActivity(Intent(tContext, MainActivity::class.java)) }, modifier = Modifier.padding(8.dp)) {
                    Text("Cancel")
                }
            }

        }

    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun DefPreview(){
    PushTheme {
        loadTask()
    }
}