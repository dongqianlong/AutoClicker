package com.demo.autoclicker.service

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.graphics.Path
import android.view.accessibility.AccessibilityEvent
import android.os.Build
import android.util.Log

class AccessibilityService : AccessibilityService() {
    
    companion object {
        private const val TAG = "AccessibilityService"
        var instance: AccessibilityService? = null
            private set
    }
    
    override fun onServiceConnected() {
        super.onServiceConnected()
        instance = this
        Log.d(TAG, "无障碍服务已连接")
    }
    
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        // 处理无障碍事件
    }
    
    override fun onInterrupt() {
        Log.d(TAG, "无障碍服务被中断")
    }
    
    override fun onDestroy() {
        super.onDestroy()
        instance = null
        Log.d(TAG, "无障碍服务已销毁")
    }
    
    /**
     * 执行点击操作
     */
    fun performClick(x: Float, y: Float): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            Log.e(TAG, "Android版本过低，不支持手势操作")
            return false
        }
        
        try {
            val path = Path().apply { 
                moveTo(x, y) 
            }
            
            val gesture = GestureDescription.Builder()
                .addStroke(GestureDescription.StrokeDescription(path, 0, 50))
                .build()
            
            return dispatchGesture(gesture, object : GestureResultCallback() {
                override fun onCompleted(gestureDescription: GestureDescription?) {
                    super.onCompleted(gestureDescription)
                    Log.d(TAG, "点击完成: ($x, $y)")
                }
                
                override fun onCancelled(gestureDescription: GestureDescription?) {
                    super.onCancelled(gestureDescription)
                    Log.w(TAG, "点击被取消: ($x, $y)")
                }
            }, null)
        } catch (e: Exception) {
            Log.e(TAG, "执行点击失败", e)
            return false
        }
    }
    
    /**
     * 检查服务是否可用
     */
    fun isServiceAvailable(): Boolean {
        return instance != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
    }
}
