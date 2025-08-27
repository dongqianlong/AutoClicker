package com.demo.autoclicker.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.demo.autoclicker.R
import com.demo.autoclicker.databinding.ActivityPositionSelectorBinding

class PositionSelectorActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityPositionSelectorBinding
    private var selectedX = 0f
    private var selectedY = 0f
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPositionSelectorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupUI()
    }
    
    private fun setupUI() {
        // 设置全屏触摸监听
        binding.touchOverlay.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                selectedX = event.rawX
                selectedY = event.rawY
                
                // 更新十字线位置
                binding.crosshairVertical.x = selectedX - binding.crosshairVertical.width / 2
                binding.crosshairHorizontal.y = selectedY - binding.crosshairHorizontal.height / 2
                
                // 显示十字线
                binding.crosshairVertical.visibility = View.VISIBLE
                binding.crosshairHorizontal.visibility = View.VISIBLE
                
                // 更新坐标显示
                binding.tvCoordinates.text = "坐标: (${selectedX.toInt()}, ${selectedY.toInt()})"
                binding.tvCoordinates.visibility = View.VISIBLE
                
                // 显示确认按钮
                binding.btnConfirm.visibility = View.VISIBLE
                binding.btnCancel.visibility = View.VISIBLE
                
                true
            } else {
                false
            }
        }
        
        // 确认按钮
        binding.btnConfirm.setOnClickListener {
            val resultIntent = Intent().apply {
                putExtra("x", selectedX)
                putExtra("y", selectedY)
            }
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
        
        // 取消按钮
        binding.btnCancel.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }
    }
    
    override fun onBackPressed() {
        setResult(Activity.RESULT_CANCELED)
        super.onBackPressed()
    }
}
