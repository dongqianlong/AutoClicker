package com.demo.autoclicker.ui

import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.demo.autoclicker.R
import com.demo.autoclicker.databinding.FloatingButtonLayoutBinding
import com.demo.autoclicker.service.AutoClickService

class FloatingButtonService : Service() {
    
    private var windowManager: WindowManager? = null
    private var floatingView: View? = null
    private var binding: FloatingButtonLayoutBinding? = null
    
    override fun onBind(intent: Intent?): IBinder? = null
    
    override fun onCreate() {
        super.onCreate()
        createFloatingButton()
    }
    
    override fun onDestroy() {
        super.onDestroy()
        removeFloatingButton()
    }
    
    private fun createFloatingButton() {
        // 获取WindowManager
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        
        // 创建悬浮窗视图
        binding = FloatingButtonLayoutBinding.inflate(LayoutInflater.from(this))
        floatingView = binding!!.root
        
        // 设置悬浮窗参数
        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            } else {
                @Suppress("DEPRECATION")
                WindowManager.LayoutParams.TYPE_PHONE
            },
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.START
            x = 100
            y = 100
        }
        
        // 添加悬浮窗到窗口管理器
        windowManager?.addView(floatingView, params)
        
        setupFloatingButtonActions(params)
    }
    
    private fun setupFloatingButtonActions(params: WindowManager.LayoutParams) {
        var initialX = 0
        var initialY = 0
        var initialTouchX = 0f
        var initialTouchY = 0f
        var isDragging = false
        
        // 设置触摸监听器（拖拽功能）
        binding?.floatingButton?.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    initialX = params.x
                    initialY = params.y
                    initialTouchX = event.rawX
                    initialTouchY = event.rawY
                    isDragging = false
                    true
                }
                MotionEvent.ACTION_MOVE -> {
                    val deltaX = event.rawX - initialTouchX
                    val deltaY = event.rawY - initialTouchY
                    
                    if (Math.abs(deltaX) > 10 || Math.abs(deltaY) > 10) {
                        isDragging = true
                        params.x = initialX + deltaX.toInt()
                        params.y = initialY + deltaY.toInt()
                        windowManager?.updateViewLayout(floatingView, params)
                    }
                    true
                }
                MotionEvent.ACTION_UP -> {
                    if (!isDragging) {
                        // 如果不是拖拽，则执行点击操作
                        toggleAutoClick()
                    }
                    true
                }
                else -> false
            }
        }
        
        // 关闭按钮
        binding?.btnClose?.setOnClickListener {
            stopSelf()
        }
        
        updateButtonState()
    }
    
    private fun toggleAutoClick() {
        if (AutoClickService.isRunning) {
            // 停止点击
            val intent = Intent(this, AutoClickService::class.java).apply {
                action = AutoClickService.ACTION_STOP_CLICKING
            }
            startService(intent)
            Toast.makeText(this, "停止自动点击", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "请在主界面设置点击参数后再使用", Toast.LENGTH_LONG).show()
        }
        
        updateButtonState()
    }
    
    private fun updateButtonState() {
        binding?.floatingButton?.setImageResource(
            if (AutoClickService.isRunning) {
                R.drawable.ic_stop
            } else {
                R.drawable.ic_play
            }
        )
    }
    
    private fun removeFloatingButton() {
        floatingView?.let { view ->
            windowManager?.removeView(view)
        }
        floatingView = null
        binding = null
    }
}
