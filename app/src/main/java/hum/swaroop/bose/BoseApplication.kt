package hum.swaroop.bose

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import hum.swaroop.bose.di.BoseComponent
import hum.swaroop.bose.di.BoseModule
import hum.swaroop.bose.di.DaggerBoseComponent

class BoseApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        component = DaggerBoseComponent.builder().boseModule(BoseModule(applicationContext)).build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.app_name)
            val description = getString(R.string.app_name)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(packageName, name, importance)
            channel.description = description
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager!!.createNotificationChannel(channel)
        }
    }

    companion object {
        lateinit var component: BoseComponent
    }
}