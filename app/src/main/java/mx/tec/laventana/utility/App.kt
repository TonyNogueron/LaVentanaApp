package mx.tec.laventana.utility

import android.app.Application

import android.util.Log
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import mx.tec.laventana.model.Location
import mx.tec.laventana.model.ProyectResponse
import mx.tec.laventana.service.RequestData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class App : Application() {
    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate() {
        super.onCreate()
        GlobalScope.launch {
            val service = RequestData.service
            try {
                service.getLocations().enqueue(object : Callback<ProyectResponse> {
                    override fun onResponse(
                        call: Call<ProyectResponse>,
                        response: Response<ProyectResponse>
                    ) {
                        if (response.isSuccessful) {
                            
                            AppDatabase.clearDatabase()
                            val db = AppDatabase.getInstance(applicationContext)
                            val body = response.body()
                            val proyectos = body?.proyectos
                            if (proyectos != null) {
                                for (proyecto in proyectos) {
                                    Log.i("todos los proyectos", proyecto.toString())
                                    GlobalScope.launch {
                                        val location = Location(
                                            proyecto.id_proyecto,
                                            proyecto.nombre_proyecto,
                                            proyecto.descripcion_proyecto,
                                            proyecto.imagen_proyecto,
                                            proyecto.latitud.toDouble(),
                                            proyecto.longitud.toDouble(),
                                            proyecto.categoria_proyecto,
                                        )

                                        db.locationDao().registerLocation(location)
                                    }
                                }
                            } else {
                                Log.i("API error", "Respuesta nula")
                            }
                        }
                    }

                    override fun onFailure(call: Call<ProyectResponse>, t: Throwable) {
                        Log.e("API error", t.message!!)
                    }
                })
            } catch (e: Exception) {
                Log.i("API error", "Error en la solicitud de los proyectos", e)
            }
        }
    }
}