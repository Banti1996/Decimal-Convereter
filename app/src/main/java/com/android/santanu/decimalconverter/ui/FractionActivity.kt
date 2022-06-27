package com.android.santanu.decimalconverter.ui

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.android.santanu.decimalconverter.R
import com.android.santanu.decimalconverter.databinding.ActivityFractionBinding
import com.android.santanu.decimalconverter.databinding.ActivityMainBinding
import com.android.santanu.decimalconverter.ui.base.BaseActivity

class FractionActivity : BaseActivity<ActivityFractionBinding>() {
    private val TAG: String by lazy { FractionActivity::class.java.simpleName }

    private lateinit var mLayout: ActivityFractionBinding

    override fun getLayoutId(): Int = R.layout.activity_fraction

    override fun onCreate(savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT >= 21) {
            val window = this.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = ContextCompat.getColor(
                this@FractionActivity, R.color.teal_200
            )
        }

        supportActionBar?.hide()

        super.onCreate(savedInstanceState)

        mLayout = getViewDataBinding().apply {
            this.lifecycleOwner = this@FractionActivity
        }



        mLayout.tvBack.setOnClickListener {
            Intent(this@FractionActivity, MainActivity::class.java).apply {
                startActivity(this)
            }
        }
    }
}