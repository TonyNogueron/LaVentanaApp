package mx.tec.laventana.service

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RequestData {
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://1mfu6t7gmh.execute-api.us-east-1.amazonaws.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    var service: ServiceInterface = retrofit.create(ServiceInterface::class.java)
}