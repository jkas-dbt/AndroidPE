package $PACKAGE$

import android.app.Service
import android.content.Intent
import android.os.IBinder

class $CLASS_NAME$ : Service() {
    override fun onBind(intent: Intent): IBinder? {
        // Ne gère pas de communication avec des activités
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Fais ton travail de longue durée ici
        return START_NOT_STICKY
    }
}
