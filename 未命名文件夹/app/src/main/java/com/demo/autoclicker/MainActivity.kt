package com.demo.autoclicker

import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.accessibility.AccessibilityManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.demo.autoclicker.databinding.ActivityMainBinding
import com.demo.autoclicker.service.AutoClickService
import com.demo.autoclicker.ui.FloatingButtonService
import com.demo.autoclicker.ui.PositionSelectorActivity

class MainActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityMainBinding
    private var clickX = 0f
    private var clickY = 0f
    
    companion object {
        private const val REQUEST_OVERLAY_PERMISSION = 1001
        private const val REQUEST_ACCESSIBILITY_SETTINGS = 1002
        private const val REQUEST_POSITION_SELECTOR = 1003
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupUI()
        updateServiceStatus()
    }
    
    override fun onResume() {
        super.onResume()
        updateServiceStatus()
        updateClickingButtons()
    }
    
    private fun setupUI() {
        // 启用无障碍服务
        binding.btnEnableAccessibility.setOnClickListener {
            openAccessibilitySettings()
        }
        
        // 手动设置位置
        binding.btnSetPosition.setOnClickListener {
            setPositionManually()
        }
        
        // 触摸选择位置
        binding.btnSelectPosition.setOnClickListener {
            openPositionSelector()
        }
        
        // 开始点击
        binding.btnStartClicking.setOnClickListener {
            startClicking()
        }
        
        // 停止点击
        binding.btnStopClicking.setOnClickListener {
            stopClicking()
        }
        
        // 悬浮窗按钮
        binding.btnFloatingButton.setOnClickListener {
            showFloatingButton()
        }
        
        // 无限点击复选框
        binding.cbInfiniteClicks.setOnCheckedChangeListener { _, isChecked ->
            binding.etClickCount.isEnabled = !isChecked
        }
    }
    
    private fun updateServiceStatus() {
        // 检查无障碍服务状态
        val accessibilityEnabled = isAccessibilityServiceEnabled()
        binding.tvAccessibilityStatus.text = if (accessibilityEnabled) {
            "无障碍服务: 已启用"
        } else {
            "无障碍服务: 未启用"
        }
        binding.tvAccessibilityStatus.setTextColor(
            getColor(if (accessibilityEnabled) R.color.primary_color else R.color.accent_color)
        )
        
        // 检查悬浮窗权限
        val overlayEnabled = canDrawOverlays()
        binding.tvOverlayStatus.text = if (overlayEnabled) {
            "悬浮窗权限: 已启用"
        } else {
            "悬浮窗权限: 未启用"
        }
        binding.tvOverlayStatus.setTextColor(
            getColor(if (overlayEnabled) R.color.primary_color else R.color.accent_color)
        )
    }
    
    private fun updateClickingButtons() {
        val isRunning = AutoClickService.isRunning
        binding.btnStartClicking.isEnabled = !isRunning
        binding.btnStopClicking.isEnabled = isRunning
    }
    
    private fun isAccessibilityServiceEnabled(): Boolean {
        val accessibilityManager = getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
        val enabledServices = accessibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK)
        
        return enabledServices.any { serviceInfo ->
            serviceInfo.resolveInfo.serviceInfo.packageName == packageName
        }
    }
    
    private fun canDrawOverlays(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Settings.canDrawOverlays(this)
        } else {
            true
        }
    }
    
    private fun openAccessibilitySettings() {
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        startActivityForResult(intent, REQUEST_ACCESSIBILITY_SETTINGS)
        Toast.makeText(this, "请找到并启用 '自动连点器' 服务", Toast.LENGTH_LONG).show()
    }
    
    private fun setPositionManually() {
        val xText = binding.etXCoordinate.text.toString()
        val yText = binding.etYCoordinate.text.toString()
        
        if (xText.isBlank() || yText.isBlank()) {
            Toast.makeText(this, "请输入有效的坐标", Toast.LENGTH_SHORT).show()
            return
        }
        
        try {
            clickX = xText.toFloat()
            clickY = yText.toFloat()
            updatePositionDisplay()
            Toast.makeText(this, "位置设置成功", Toast.LENGTH_SHORT).show()
        } catch (e: NumberFormatException) {
            Toast.makeText(this, "请输入有效的数字", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun openPositionSelector() {
        val intent = Intent(this, PositionSelectorActivity::class.java)
        startActivityForResult(intent, REQUEST_POSITION_SELECTOR)
    }
    
    private fun updatePositionDisplay() {
        binding.tvCurrentPosition.text = getString(R.string.position_set, clickX.toInt(), clickY.toInt())
        binding.etXCoordinate.setText(clickX.toInt().toString())
        binding.etYCoordinate.setText(clickY.toInt().toString())
    }
    
    private fun startClicking() {
        if (!isAccessibilityServiceEnabled()) {
            Toast.makeText(this, "请先启用无障碍服务", Toast.LENGTH_LONG).show()
            return
        }
        
        if (clickX == 0f && clickY == 0f) {
            Toast.makeText(this, getString(R.string.no_position_set), Toast.LENGTH_SHORT).show()
            return
        }
        
        val intervalText = binding.etClickInterval.text.toString()
        val countText = binding.etClickCount.text.toString()
        
        if (intervalText.isBlank()) {
            Toast.makeText(this, "请输入点击间隔", Toast.LENGTH_SHORT).show()
            return
        }
        
        try {
            val interval = intervalText.toLong()
            val count = if (binding.cbInfiniteClicks.isChecked) {
                -1
            } else {
                if (countText.isBlank()) 1 else countText.toInt()
            }
            
            if (interval < 100) {
                Toast.makeText(this, "点击间隔不能小于100毫秒", Toast.LENGTH_SHORT).show()
                return
            }
            
            val intent = Intent(this, AutoClickService::class.java).apply {
                action = AutoClickService.ACTION_START_CLICKING
                putExtra(AutoClickService.EXTRA_X, clickX)
                putExtra(AutoClickService.EXTRA_Y, clickY)
                putExtra(AutoClickService.EXTRA_INTERVAL, interval)
                putExtra(AutoClickService.EXTRA_COUNT, count)
            }
            
            startService(intent)
            updateClickingButtons()
            Toast.makeText(this, getString(R.string.service_running), Toast.LENGTH_SHORT).show()
            
        } catch (e: NumberFormatException) {
            Toast.makeText(this, "请输入有效的数字", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun stopClicking() {
        val intent = Intent(this, AutoClickService::class.java).apply {
            action = AutoClickService.ACTION_STOP_CLICKING
        }
        startService(intent)
        updateClickingButtons()
        Toast.makeText(this, getString(R.string.service_stopped), Toast.LENGTH_SHORT).show()
    }
    
    private fun showFloatingButton() {
        if (!canDrawOverlays()) {
            showOverlayPermissionDialog()
            return
        }
        
        val intent = Intent(this, FloatingButtonService::class.java)
        startService(intent)
        Toast.makeText(this, "悬浮按钮已显示", Toast.LENGTH_SHORT).show()
    }
    
    private fun showOverlayPermissionDialog() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.overlay_permission_required))
            .setMessage("需要悬浮窗权限来显示悬浮按钮")
            .setPositiveButton(getString(R.string.go_to_settings)) { _, _ ->
                requestOverlayPermission()
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }
    
    private fun requestOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:$packageName")
            )
            startActivityForResult(intent, REQUEST_OVERLAY_PERMISSION)
        }
    }
    
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        
        when (requestCode) {
            REQUEST_OVERLAY_PERMISSION -> {
                updateServiceStatus()
            }
            REQUEST_ACCESSIBILITY_SETTINGS -> {
                updateServiceStatus()
            }
            REQUEST_POSITION_SELECTOR -> {
                if (resultCode == RESULT_OK && data != null) {
                    clickX = data.getFloatExtra("x", 0f)
                    clickY = data.getFloatExtra("y", 0f)
                    updatePositionDisplay()
                    Toast.makeText(this, "位置选择成功", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
