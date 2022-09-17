package com.example.jetweatherforcast.screens.main

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.jetweatherforcast.R
import com.example.jetweatherforcast.data.DataOrException
import com.example.jetweatherforcast.model.Weather
import com.example.jetweatherforcast.model.WeatherItem
import com.example.jetweatherforcast.utils.formatDate
import com.example.jetweatherforcast.utils.formatDateTime
import com.example.jetweatherforcast.utils.formatDayName
import com.example.jetweatherforcast.utils.formatDecimal
import com.example.jetweatherforcast.widget.*

@Composable
fun MainScreen(navController: NavController, mainViewModel: MainViewModel){


    val weatherData = produceState<DataOrException<Weather, Boolean, Exception>>(
        initialValue = DataOrException(loading = true)){
        value = mainViewModel.getWeather(city = "Moscow")
    }.value

    if (weatherData.loading == true){
        CircularProgressIndicator()
    }else if (weatherData.data != null){
//        Text(text = "MainScreen ${weatherData.data!!.toString()}")
//        Log.d("MainScreen", "ShowData: data fetched!")
        MainScaffold(weatherData.data!!, navController)
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainScaffold(weather: Weather, navController: NavController) {

    Scaffold(topBar = {
        WeatherAppBar(title = weather.city.name + ", ${weather.city.country}",
            //icon = Icons.Default.ArrowBack,
            navController = navController, elevation = 5.dp){
            Log.d("Back Button", "MainScaffold: Button Clicked")
        }
    }) {
        MainContent(data = weather)
    }

}

@Composable
fun MainContent(data: Weather) {
    val weatherItem = data.list[0]
    val imageUrl = "https://openweathermap.org/img/wn/${weatherItem.weather[0].icon}.png"
    Column(modifier = Modifier
        .padding(4.dp)
        .fillMaxWidth(),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally) {

        Text(text = formatDate(weatherItem.dt) ,
        style = MaterialTheme.typography.caption,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.padding(6.dp))

        Surface(shape = CircleShape,
        modifier = Modifier
            .size(200.dp)
            .padding(4.dp),
        color = Color(0xffffc400)
        ) {
            Column(verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {

                //Image
                WeatherStateImage(imageUrl)
                Text(text = formatDecimal(weatherItem.temp.day) + "°", style = MaterialTheme.typography.h4,
                    fontWeight = FontWeight.ExtraBold)

                Text(text = weatherItem.weather[0].main,
                    fontStyle = FontStyle.Italic)
            }


        }

        HumidityWindPressureRow(weatherItem)

        Divider()

        SunriseAndSunSet(weatherItem)

        Text(text = "This Week",
        style = MaterialTheme.typography.subtitle1,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(4.dp))

        Surface(modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()
            .fillMaxHeight(),
            shape = RoundedCornerShape(12.dp),
            color = Color(0xFFD4D7E9)) {

            LazyColumn{items(data.list){weatherItem->

                SevenDaysForecastItem(weatherItem)
            }

            }
        }


    }




    //Text(text = data.city.name)
}

