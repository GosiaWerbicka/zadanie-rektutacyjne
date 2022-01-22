package com.example.zadanie

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class MainActivity : AppCompatActivity() {


    private lateinit var countryEditText: EditText
    private lateinit var countryReportButton: Button
    private lateinit var countryReportView: TextView
    lateinit var notificationManager: NotificationManager
    lateinit var notificationChannel: NotificationChannel
    lateinit var builder: Notification.Builder
    private val channelId = "com.example.myapplication"
    private val description = "Notification"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViews()
        setListeners()

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    }

    private fun findViews() {
        countryEditText = findViewById(R.id.country_edit_text)
        countryReportButton = findViewById(R.id.country_report_button)
        countryReportView = findViewById(R.id.country_report_view)
    }

    private fun setListeners() {
        countryReportButton.setOnClickListener {
            val text = countryEditText.text.toString()
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://covid-19-data.p.rapidapi.com/")
                .build()
            val jsonapi = retrofit.create(Jsonapi::class.java)
            val mcall: Call<List<Models>> = jsonapi.getInfo(text)
            mcall.enqueue(object : Callback<List<Models>> {
                override fun onResponse(
                    call: Call<List<Models>>,
                    response: Response<List<Models>>
                ) {
                    Log.d("Response", "onResponse: ${response.body()}")
                    if (response.isSuccessful) {
                        val countryList = response.body()!!
                        if (countryList.size == 0) {
                            Toast.makeText(
                                this@MainActivity,
                                "No results found",
                                Toast.LENGTH_SHORT
                            ).show()
                            return
                        }
                        val item = countryList[0]
                        val info = "Country: " + item.country + "\n" +
                                "Code: " + item.code + "\n" +
                                "Confirmed: " + item.confirmed + "\n" +
                                "Recovered: " + item.recovered + "\n" +
                                "Critical: " + item.critical + "\n" +
                                "Deaths: " + item.deaths + "\n" +
                                "LastUpdate: " + item.lastUpdate
                        countryReportView.text = info
                        Log.d("Response", "countrylist size : ${countryList.size}")
                        val intent = Intent(this@MainActivity, LauncherActivity::class.java)
                        val pendingIntent = PendingIntent.getActivity(
                            this@MainActivity,
                            0, intent,
                            PendingIntent.FLAG_UPDATE_CURRENT
                        )
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            notificationChannel = NotificationChannel(
                                channelId,
                                description,
                                NotificationManager.IMPORTANCE_HIGH
                            )
                            notificationChannel.enableLights(true)
                            notificationChannel.lightColor = Color.BLUE
                            notificationChannel.enableVibration(true)
                            notificationManager.createNotificationChannel(notificationChannel)

                            builder = Notification.Builder(this@MainActivity, channelId)
                                .setContentTitle("Report")
                                .setContentText("Report is ready")
                                .setSmallIcon(R.drawable.ic_launcher_round)
                                .setContentIntent(pendingIntent)
                            //val handler= Handler(Looper.getMainLooper()).postDelayed({
                            //},30000)
                        } else {

                            builder = Notification.Builder(this@MainActivity)
                                .setContentTitle("Report")
                                .setContentText("Report is ready")
                                .setSmallIcon(R.drawable.ic_launcher_round)
                                .setContentIntent(pendingIntent)
                           //val handler= Handler(Looper.getMainLooper()).postDelayed({
                            //},30000)

                        }
                        notificationManager.notify(1234, builder.build())

                    } else {
                        Toast.makeText(
                            this@MainActivity,
                            "Something went wrong ${response.message()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<List<Models>>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "Something went wrong $t", Toast.LENGTH_SHORT)
                        .show()
                }
            })


        }
    }

}



















