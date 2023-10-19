package mx.tec.laventana.utility

import android.app.Application


class App : Application() {
    override fun onCreate() {
        super.onCreate()
        DataFetcher(this).fetchDataFromAPI()
    }
}