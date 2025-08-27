package com.demo.autoclicker.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import com.demo.autoclicker.MainActivity
import com.demo.autoclicker.R

class AutoClickService : Service() {
    
    companion object {
        private const val TAG = "AutoClickService"
        private const val NOTIFICATION_ID = 1001
        private const val CHANNEL_ID = "auto_click_channel"
        
        const val ACTION_START_CLICKING = "action_start_clicking"
        const val ACTION_STOP_CLICKING = "action_stop_clicking"
        const val EXTRA_X = "extra_x"
        const val EXTRA_Y = "extra_y"
        const val EXTRA_INTERVAL = "extra_interval"
        const val EXTRA_COUNT = "extra_count"
        
        var isRunning = false
            private set
    }
    
    private var handler: Handler? = null
    private var clickRunnable: Runnable? = null
    private var currentClickCount = 0
    private var targetClickCount = -1 // -1 表示无限点击
    private var clickInterval = 1000L
    private var clickX = 0f
    private var clickY = 0f
    
    override fun onCreate() {
        super.onCreate()
        handler = Handler(Looper.getMainLooper())
        createNotificationChannel()
        Log.d(TAG, "自动点击服务已创建")
    }
    
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START_CLICKING -> {
                clickX = intent.getFloatExtra(EXTRA_X, 0f)
                clickY = intent.getFloatExtra(EXTRA_Y, 0f)
                clickInterval = intent.getLongExtra(EXTRA_INTERVAL, 1000L)
                targetClickCount = intent.getIntExtra(EXTRA_COUNT, -1)
                startClicking()
            }
            ACTION_STOP_CLICKING -> {
                stopClicking()
            }
        }
        return START_STICKY
    }
    
    override fun onBind(intent: Intent?): IBinder? = null
    
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "自动点击服务",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "自动点击器正在运行"
                setShowBadge(false)
            }
            
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
    
    private fun createNotification(): Notification {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        val stopIntent = Intent(this, AutoClickService::class.java).apply {
            action = ACTION_STOP_CLICKING
        }
        val stopPendingIntent = PendingIntent.getService(
            this, 0, stopIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("自动连点器正在运行")
            .setContentText("位置: (${clickX.toInt()}, ${clickY.toInt()}) 间隔: ${clickInterval}ms")
            .setSmallIcon(R.drawable.ic_notification)
            .setContentIntent(pendingIntent)
            .addAction(R.drawable.ic_stop, "停止", stopPendingIntent)
            .setOngoing(true)
            .build()
    }
    
    private fun startClicking() {
        if (isRunning) return
        
        val accessibilityService = AccessibilityService.instance
        if (accessibilityService == null || !accessibilityService.isServiceAvailable()) {
            Log.e(TAG, "无障碍服务不可用")
            stopSelf()
            return
        }
        
        isRunning = true
        currentClickCount = 0
        
        startForeground(NOTIFICATION_ID, createNotification())
        
        clickRunnable = object : Runnable {
            override fun run() {
                if (!isRunning) return
                
                // 执行点击
                accessibilityService.performClick(clickX, clickY)
                currentClickCount++
                
                Log.d(TAG, "执行点击 #$currentClickCount at ($clickX, $clickY)")
                
                // 检查是否达到目标点击次数
                if (targetClickCount > 0 && currentClickCount >= targetClickCount) {
                    Log.d(TAG, "达到目标点击次数: $targetClickCount")
                    stopClicking()
                    return
                }
                
                // 安排下次点击
                handler?.postDelayed(this, clickInterval)
            }
        }
        
        handler?.post(clickRunnable!!)
        Log.d(TAG, "开始自动点击")
    }
    
    private fun stopClicking() {
        if (!isRunning) return
        
        isRunning = false
        clickRunnable?.let { handler?.removeCallbacks(it) }
        clickRunnable = null
        
        stopForeground(true)
        stopSelf()
        
        Log.d(TAG, "停止自动点击，总共点击了 $currentClickCount 次")
    }
    
    override fun onDestroy() {
        super.onDestroy()
        stopClicking()
        Log.d(TAG, "自动点击服务已销毁")
    }
}
