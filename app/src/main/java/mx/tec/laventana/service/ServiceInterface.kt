package mx.tec.laventana.service

import mx.tec.laventana.model.ProyectResponse
import retrofit2.Call
import retrofit2.http.GET

interface ServiceInterface {
    @GET(value = "proyecto/")
    fun getLocations(): Call<ProyectResponse>
}