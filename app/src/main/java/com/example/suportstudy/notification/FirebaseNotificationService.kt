package com.example.suportstudy.notification

import android.R
import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.suportstudy.activity.chat.ChatOneActivity
import com.example.suportstudy.activity.course.CourseTypeActivity.Companion.uid
import com.example.suportstudy.until.Constrain
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlin.random.Random


class FirebaseNotificationService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        super.onMessageReceived(remoteMessage)
        if (remoteMessage!!.data.size > 0) {
            val map: Map<String, String> = remoteMessage.getData()
            val title = map["title"]
            val message = map["message"]
            val hisID = map["hisUid"]
            val hisName = map["hisName"]
            val hisImage = map["hisImage"]
            //            String chatID = map.get("chatID");
            Log.d(
                "TAG",
                "DataMap: \n title: $title" + "\nmessage: $message" + "\nhisUid: $hisID" + "\nhisName: $hisName" + "\nhisImage: $hisImage"
            )
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O)
                createOreoNotification(
                    title,
                    message,
                    hisID,
                    hisName,
                    hisImage
                ) else createNormalNotification(title, message, hisID, hisName, hisImage)
        } else Log.d("TAG", "onMessageReceived: no data ")
    }

    @SuppressLint("WrongConstant")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createOreoNotification(
        title: String?,
        message: String?,
        hisID: String?,
        hisName: String?,
        hisImage: String?
    ) {
        val channel = NotificationChannel(
            Constrain.CHANNEL_ID,
            "Message",
            NotificationManager.IMPORTANCE_HIGH
        )
        channel.setShowBadge(true)
        channel.enableLights(true)
        channel.enableVibration(true)
        channel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)

        val intent = Intent(this, ChatOneActivity::class.java)
        intent.putExtra("hisUid", hisID)
        intent.putExtra("hisImage", hisImage)
        intent.putExtra("hisName", hisName)
        intent.putExtra("hisImage", hisImage)

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)

        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        var bitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_btn_speak_now)

        var remoteViews =
            RemoteViews(packageName, com.example.suportstudy.R.layout.notification_layout)

        remoteViews.setTextViewText(com.example.suportstudy.R.id.txtName, title)
        remoteViews.setTextViewText(com.example.suportstudy.R.id.txtMessage, message)
        remoteViews.setImageViewResource(
            com.example.suportstudy.R.id.avatarIv,
            com.example.suportstudy.R.drawable.tutor
        )
        val notification: Notification = Notification.Builder(this, Constrain.CHANNEL_ID)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setSmallIcon(R.mipmap.sym_def_app_icon)
//            .setContentTitle(title)
//            .setContentText(message)
//            .setColor(ResourcesCompat.getColor(resources, R.color.holo_blue_bright, null))
//            .setSmallIcon(R.drawable.sym_def_app_icon)
//            .setLargeIcon(bitmap)
            .setCustomBigContentView(remoteViews)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
        manager.notify(Constrain.NOTIFICATION_ID, notification)
    }

    private fun createNormalNotification(
        title: String?,
        message: String?,
        hisID: String?,
        hisName: String?,
        hisImage: String?
    ) {
        val uri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val builder: NotificationCompat.Builder = NotificationCompat.Builder(
            this,
            Constrain.CHANNEL_ID
        )
//        var bitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_btn_speak_now)

        var remoteViews =RemoteViews(packageName, com.example.suportstudy.R.layout.notification_layout)
        remoteViews.setTextViewText(com.example.suportstudy.R.id.txtName, title)
        remoteViews.setTextViewText(com.example.suportstudy.R.id.txtMessage, message)
        remoteViews.setImageViewResource(
            com.example.suportstudy.R.id.avatarIv,
            com.example.suportstudy.R.drawable.tutor
        )

        builder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setSmallIcon(R.mipmap.sym_def_app_icon)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCustomBigContentView(remoteViews)
            .setSound(uri)

        val intent = Intent(this, ChatOneActivity::class.java)
        intent.putExtra("hisUid", hisID)
        intent.putExtra("hisImage", hisImage)
        intent.putExtra("hisName", hisName)
        intent.putExtra("hisImage", hisImage)

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)

        builder.setContentIntent(pendingIntent)
        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(Random.nextInt(85 - 65), builder.build())
    }

    override fun onNewToken(s: String) {
        updateToken(s)
        super.onNewToken(s)
    }

    private fun updateToken(token: String) {
        if (uid != null) {
            val databaseReference =
                Constrain.initFirebase("Tokens").child(
                    uid!!
                )
            val map: HashMap<String, Any> = HashMap()
            map.put("token", token);
            databaseReference.updateChildren(map)
        }
    }
}