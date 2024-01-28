package com.example.push

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Website.URL
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.push.ui.theme.Acc
import com.example.push.ui.theme.Back
import com.example.push.ui.theme.Borders
import com.example.push.ui.theme.DarkAccent
import com.example.push.ui.theme.LightAccent
import com.example.push.ui.theme.Main
import com.example.push.ui.theme.Other
import com.example.push.ui.theme.PushTheme
import com.example.push.ui.theme.Sec
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.URL

var yourpoint = mutableStateOf(common.points)
var GROUPID = ""
var GROUP = mutableListOf<got>()
var GLOBAL = mutableListOf<got>()

class MainActivity : ComponentActivity() {
    private val BASE_URL = "http://REMOVED FOR PRIVACY/"
    private val TAG: String = "CHECK_RESPONSE"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Log.i("MADE-TO", "this is set content")
            getTask()
        }
        getData()

    }

    private fun getData(){
        val lapi = Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build().create(api::class.java)

        if (common.add != 0){
            doTask()
        }


        GlobalScope.launch(Dispatchers.IO) {

            val response = lapi.getItems()
            if(response.isSuccessful){
                for (item in response.body()!!){
                    Log.i("MADE-TO", "this is 1st for loop")
                    if (item.uid == "ffffffff"){
                        common.group = item.group
                        common.group = item.group
                        common.uid = item.uid
                        common.name = item.name
                        common.points = item.points
                        common.streak = item.streak
                        common.multiplier = item.multiplier
                        Log.i(item.group, "THIS IS THE GROUP ID")
                    }
                    if (common.group == "soc"){
                        GROUPID = "SOCIETAL"
                    }
                    else if (common.group == "env"){
                        GROUPID = "ENVIRONMENTAL"
                    }
                    else {
                        GROUPID = "UNSPECIFIED"
                    }
                    Log.i(TAG, "Data: ${item.uid}")
                }
            }
            if(response.isSuccessful){
                GROUP = mutableListOf<got>()
                for (item in response.body()!!){
                    if (item.group == common.group){
                        GROUP.add(item)
                    }
                }
            }
            if(response.isSuccessful){
                GLOBAL = mutableListOf<got>()
                for (item in response.body()!!){
                    GLOBAL.add(item)
                }
            }
        }

    }
    private fun doTask(){
        val service = Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build().create(taskadd::class.java)

        GlobalScope.launch(Dispatchers.IO){
            val response = service.getconfirm(common.uid, common.points)
            Log.i("MADE-TO", response.body()!!.message)
        }
    }
}




@Composable
fun getTask(){
    val mContext = LocalContext.current
    var text by remember { mutableStateOf("0") }

    Scaffold(floatingActionButton = {
    }, topBar = {}, bottomBar = {
        //GET NEW TASK BUTTON
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement =  Arrangement.Center){
            Button(onClick = {
                if (common.group != "no"){
                    mContext.startActivity(Intent(mContext, LoadTask::class.java))
                }
                else{
                    mContext.startActivity(Intent(mContext, JoinGroup::class.java))
                }

            }, colors = ButtonDefaults.buttonColors(containerColor = Main), modifier = Modifier
                .padding(16.dp)) {
                Text("⭐", fontSize = 18.sp)
            }
            FloatingActionButton(onClick = { mContext.startActivity(Intent(mContext, MainActivity::class.java)) }, modifier = Modifier.padding(16.dp)) {
                Icon(imageVector = Icons.Default.Refresh, contentDescription = "")
            }
            Button(onClick = {
                mContext.startActivity(Intent(mContext, JoinGroup::class.java))
            }, colors = ButtonDefaults.buttonColors(containerColor = Main), modifier = Modifier
                .padding(16.dp)) {
                Text("\uD83E\uDD1D", fontSize = 18.sp)
            }
        }
    }) { contentpadding ->

        //CURRENT USER common.points
        Column (modifier = Modifier
            .background(color = Back)
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(contentpadding), horizontalAlignment = Alignment.CenterHorizontally) {
            Card (modifier = Modifier
                .fillMaxWidth()
                .padding(start = 32.dp, end = 32.dp, top = 5.dp)
                .size(width = 100.dp, height = 200.dp), colors = CardDefaults.cardColors(containerColor = Main)){

                Column(modifier = Modifier.fillMaxSize(),horizontalAlignment = Alignment.Start, verticalArrangement = Arrangement.Center) {
                    Row(){
                        Text(text = "Hello "+common.name+"!", fontSize = 44.sp, modifier = Modifier.padding(start = 16.dp), fontFamily = FontFamily.SansSerif, color = Back)
                    }
                    Row(){
                        Text(text = "⭐"+common.points.toString()+"\uD83D\uDD25"+common.streak.toString(), fontSize = 32.sp, modifier = Modifier.padding(start = 16.dp), fontFamily = FontFamily.SansSerif, color = Back)
                    }

                }

            }

            //GLOBAL LEADERBOARD
            Column (modifier = Modifier
                .fillMaxWidth()
                .size(width = 100.dp, height = 200.dp)) {
                Text("GLOBAL LEADERBOARD", modifier = Modifier
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 8.dp)
                    .align(Alignment.Start), fontFamily = FontFamily.SansSerif, fontSize = 18.sp)
                LazyColumn {
                    items(GLOBAL.size){ index ->
                        Card(elevation = CardDefaults.cardElevation(3.dp),colors = CardDefaults.cardColors(containerColor = Back), modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp, bottom = 5.dp, top = 2.dp)){
                            Column(modifier = Modifier.fillMaxSize()) {
                                Row(){
                                    //THIS WILL BE CHANGED TO LOAD MONGO DATA
                                    Text(text=GLOBAL[index].name, fontSize = 18.sp, modifier = Modifier.padding(8.dp), fontFamily = FontFamily.SansSerif)
                                    //THIS WILL BE CHANGED TO LOAD MONGO DATA
                                    Text(text=GLOBAL[index].points.toString(), fontSize = 18.sp, modifier = Modifier.padding(8.dp), fontFamily = FontFamily.SansSerif)
                                }
                            }
                        }

                    }
                }
            }

            //GROUP LEADERBOARD
            Column (modifier = Modifier
                .fillMaxWidth()
                .size(width = 100.dp, height = 200.dp)) {
                //THIS WILL NEED TO LOAD THE NAME OF A GROUP or WE JUST BE LAZY AND MAKE THIS SAY "GROUP"
                Text(text = GROUPID+" LEADERBOARD", modifier = Modifier
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 8.dp)
                    .align(Alignment.Start), fontFamily = FontFamily.SansSerif, fontSize = 18.sp)
                LazyColumn {
                    items(GROUP.size){index ->
                        Card(elevation = CardDefaults.cardElevation(3.dp), colors = CardDefaults.cardColors(containerColor = Back), modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp, bottom = 5.dp, top = 2.dp)){
                            Column(modifier = Modifier.fillMaxSize()) {
                                Row(){
                                    //THIS WILL BE CHANGED TO LOAD MONGO DATA
                                    Text(text=GROUP[index].name, fontSize = 18.sp, modifier = Modifier.padding(8.dp))
                                    //THIS WILL BE CHANGED TO LOAD MONGO DATA
                                    Text(text=GROUP[index].points.toString(), fontSize = 18.sp, modifier = Modifier.padding(8.dp))
                                }
                            }
                        }

                    }
                }
            }
        }

    }


}


//PREVIEW DONT MESS
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DefaultPreview(){
    PushTheme {
        getTask()
    }
}