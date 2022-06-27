package com.android.santanu.decimalconverter.ui

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.compose.ui.text.toLowerCase
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import com.android.santanu.decimalconverter.R
import com.android.santanu.decimalconverter.data.FractionNumberConverter
import com.android.santanu.decimalconverter.data.GeneralNumberConverter
import com.android.santanu.decimalconverter.data.NumberConverter
import com.android.santanu.decimalconverter.data.NumberFormatUtils
import com.android.santanu.decimalconverter.databinding.ActivityMainBinding
import com.android.santanu.decimalconverter.ui.base.BaseActivity
import com.android.santanu.decimalconverter.vm.MainViewModel
import com.google.android.material.snackbar.Snackbar
import java.util.*
import kotlin.collections.ArrayList
import kotlin.system.exitProcess


class MainActivity : BaseActivity<ActivityMainBinding>(),
    PopupDialogFragment.PopupDialogFragmentListener {
    private val TAG: String = MainActivity::class.java.simpleName


    private lateinit var mLayout: ActivityMainBinding
    private val mMainViewModel: MainViewModel by viewModels<MainViewModel>()

    private var mPopupDialogFragment: PopupDialogFragment? = null

    private val formateList: ArrayList<String> = ArrayList<String>().apply {
        this.add("Decimal")
        this.add("Binary")
        this.add("Octal")
        this.add("Hexadecimal")
    }
    var spinnerAdapter: ArrayAdapter<String>? = null


    private var upperFormatData : String = ""

    private var upperSegmentDecimalData : String = ""
    private var upperSegmentBinaryData : String = ""
    private var upperSegmentOctalData : String = ""
    private var upperSegmentHexadecimalData : String = ""

    private var lowerSegmentInputData : String = ""
    private var lowerToFormatData : String = ""
    private var lowerFormFormatData : String = ""

    private var isActive: Boolean = false
    private var isUpperFractionActive: Boolean = false
    private var isLowerFractionActive: Boolean = false

    private var mNumberConverter: NumberConverter? = null

    private lateinit var clipboard : ClipboardManager

    override fun getLayoutId(): Int = R.layout.activity_main


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT >= 21) {
            val window = this.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = ContextCompat.getColor(
                this@MainActivity, R.color.teal_200
            )
        }

        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        mLayout = getViewDataBinding().apply {
            this.lifecycleOwner = this@MainActivity
        }

        /*
        mMainViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(
            application
        ).create(MainViewModel::class.java)
        */

        this.initializeSpinner()
        this.initializeFirstTime()

        if(mNumberConverter == null) {
            mNumberConverter = NumberConverter()
        }

        clipboard  = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

        mLayout.upperSpinner.showSoftInputOnFocus = false
        mLayout.lowerToSpinner.showSoftInputOnFocus = false
        mLayout.lowerFormSpinner.showSoftInputOnFocus = false


        mLayout.upperSpinner.doOnTextChanged { text, _, _, _ ->
            upperFormatData = text.toString()
            mLayout.upperSegmentInputData.setText("")
            this.onUpperSegmentRefresh()
        }

        mLayout.lowerToSpinner.doOnTextChanged { text, _, _, _ ->
            lowerToFormatData = text.toString()
            mLayout.lowerSegmentInputData.setText("")
            this.onLowerSegmentRefresh()
        }

        mLayout.lowerFormSpinner.doOnTextChanged { text, _, _, _ ->
            lowerFormFormatData = text.toString()
            // this.onLowerSegmentRefresh()
        }

        mLayout.upperSegmentInputData.doOnTextChanged { text, _, _, _ ->
            if (!isActive) {
                if (upperFormatData.isNotEmpty()) {
                    if(text.toString().isEmpty()) {
                        mLayout.upperSegmentInputLayout.error = null
                        mLayout.upperSegmentInputLayout.isEndIconVisible = false
                    } else {
                        when (upperFormatData) {
                            "Decimal" -> {
                                if(mNumberConverter!!.isDecimalNumber(text.toString(), isUpperFractionActive)) {
                                    // mLayout.upperSegmentInputLayout.helperText = null
                                    mLayout.upperSegmentInputLayout.error = null
                                    mLayout.upperSegmentInputLayout.isEndIconVisible = true
                                } else {
                                    // mLayout.upperSegmentInputLayout.helperText = "incorrect ${upperFormatData.lowercase(Locale.getDefault())} number format"
                                    mLayout.upperSegmentInputLayout.error = "incorrect ${upperFormatData.lowercase(Locale.getDefault())} number format"
                                    mLayout.upperSegmentInputLayout.isEndIconVisible = false

                                }
                            }
                            "Binary" -> {
                                if(mNumberConverter!!.isBinaryNumber(text.toString(), isUpperFractionActive)) {
                                    // mLayout.upperSegmentInputLayout.helperText = null
                                    mLayout.upperSegmentInputLayout.error = null
                                    mLayout.upperSegmentInputLayout.isEndIconVisible = true
                                } else {
                                    // mLayout.upperSegmentInputLayout.helperText = "incorrect ${upperFormatData.lowercase(Locale.getDefault())} number format"
                                    mLayout.upperSegmentInputLayout.error = "incorrect ${upperFormatData.lowercase(Locale.getDefault())} number format"
                                    mLayout.upperSegmentInputLayout.isEndIconVisible = false

                                }
                            }
                            "Octal" -> {
                                if(mNumberConverter!!.isOctalNumber(text.toString(), isUpperFractionActive)) {
                                    // mLayout.upperSegmentInputLayout.helperText = null
                                    mLayout.upperSegmentInputLayout.error = null
                                    mLayout.upperSegmentInputLayout.isEndIconVisible = true
                                } else {
                                    // mLayout.upperSegmentInputLayout.helperText = "incorrect ${upperFormatData.lowercase(Locale.getDefault())} number format"
                                    mLayout.upperSegmentInputLayout.error = "incorrect ${upperFormatData.lowercase(Locale.getDefault())} number format"
                                    mLayout.upperSegmentInputLayout.isEndIconVisible = false

                                }
                            }
                            "Hexadecimal" -> {
                                if(mNumberConverter!!.isHexadecimalNumber(text.toString(), isUpperFractionActive)) {
                                    // mLayout.upperSegmentInputLayout.helperText = null
                                    mLayout.upperSegmentInputLayout.error = null
                                    mLayout.upperSegmentInputLayout.isEndIconVisible = true
                                } else {
                                    // mLayout.upperSegmentInputLayout.helperText = "incorrect ${upperFormatData.lowercase(Locale.getDefault())} number format"
                                    mLayout.upperSegmentInputLayout.error = "incorrect ${upperFormatData.lowercase(Locale.getDefault())} number format"
                                    mLayout.upperSegmentInputLayout.isEndIconVisible = false

                                }
                            }
                            else -> {
                                Toast.makeText(this@MainActivity, "something went wrong", Toast.LENGTH_SHORT).also {
                                    it.show()
                                }
                            }
                        }
                    }
                } else {
                    Toast.makeText(this@MainActivity, "select number format first", Toast.LENGTH_SHORT).also {
                        it.show()
                    }
                }
            }
        }

        mLayout.lowerSegmentInputData.doOnTextChanged { text, _, _, _ ->
            if (isActive) {
                if (lowerToFormatData.isNotEmpty()) {
                    if(text.toString().isEmpty()) {
                        mLayout.lowerSegmentInputLayout.error = null
                        mLayout.lowerSegmentInputLayout.isEndIconVisible = false
                    } else {
                        when (lowerToFormatData) {
                            "Decimal" -> {
                                if(mNumberConverter!!.isDecimalNumber(text.toString(), isLowerFractionActive)) {
                                    mLayout.lowerSegmentInputLayout.error = null
                                    mLayout.lowerSegmentInputLayout.isEndIconVisible = true
                                } else {
                                    mLayout.lowerSegmentInputLayout.error = "incorrect ${lowerToFormatData.lowercase(Locale.getDefault())} number format"
                                    mLayout.lowerSegmentInputLayout.isEndIconVisible = false
                                }
                            }
                            "Binary" -> {
                                if(mNumberConverter!!.isBinaryNumber(text.toString(), isLowerFractionActive)) {
                                    mLayout.lowerSegmentInputLayout.error = null
                                    mLayout.lowerSegmentInputLayout.isEndIconVisible = true
                                } else {
                                    mLayout.lowerSegmentInputLayout.error = "incorrect ${lowerToFormatData.lowercase(Locale.getDefault())} number format"
                                    mLayout.lowerSegmentInputLayout.isEndIconVisible = false
                                }
                            }
                            "Octal" -> {
                                if(mNumberConverter!!.isOctalNumber(text.toString(), isLowerFractionActive)) {
                                    mLayout.lowerSegmentInputLayout.error = null
                                    mLayout.lowerSegmentInputLayout.isEndIconVisible = true
                                } else {
                                    mLayout.lowerSegmentInputLayout.error = "incorrect ${lowerToFormatData.lowercase(Locale.getDefault())} number format"
                                    mLayout.lowerSegmentInputLayout.isEndIconVisible = false
                                }
                            }
                            "Hexadecimal" -> {
                                if(mNumberConverter!!.isHexadecimalNumber(text.toString(), isLowerFractionActive)) {
                                    mLayout.lowerSegmentInputLayout.error = null
                                    mLayout.lowerSegmentInputLayout.isEndIconVisible = true
                                } else {
                                    mLayout.lowerSegmentInputLayout.error = "incorrect ${lowerToFormatData.lowercase(Locale.getDefault())} number format"
                                    mLayout.lowerSegmentInputLayout.isEndIconVisible = false
                                }
                            }
                            else -> {
                                Toast.makeText(this@MainActivity, "something went wrong", Toast.LENGTH_SHORT).also {
                                    it.show()
                                }
                            }
                        }
                    }
                } else {
                    Toast.makeText(this@MainActivity, "please select \"to base number\" format first", Toast.LENGTH_SHORT).also {
                        it.show()
                    }
                }
            }
        }

        mLayout.swActive.setOnCheckedChangeListener { _, isChecked ->
            isActive = isChecked
            mLayout.swLowerSegmentFractionActive.isEnabled = isChecked
            mLayout.swUpperSegmentFractionActive.isEnabled = !isChecked
            if (isChecked) {
                this.onUpperSegmentRefresh()
                this.initializeSpinner(onRefreshUpper = true, onRefreshLower = false)
                this.enableUpperSegment(false)
                this.enableLowerSegment(true)
            } else {
                this.onLowerSegmentRefresh()
                this.initializeSpinner(onRefreshUpper = false, onRefreshLower = true)
                this.enableUpperSegment(true)
                this.enableLowerSegment(false)
            }
        }

        mLayout.swUpperSegmentFractionActive.setOnCheckedChangeListener { _, isChecked ->
            isUpperFractionActive = isChecked
        }

        mLayout.swLowerSegmentFractionActive.setOnCheckedChangeListener { _, isChecked ->
            isLowerFractionActive = isChecked
        }

        mLayout.tvFractionSupport.setOnClickListener {
            Intent(this@MainActivity, FractionActivity::class.java).apply {
                startActivity(this)
            }
        }

        mLayout.convertButton.setOnClickListener {

            if (isActive) {
                if (lowerToFormatData.isNotEmpty()) {
                    if (formateList.contains(lowerToFormatData)) {
                        if (lowerFormFormatData.isNotEmpty()) {

                            lowerSegmentInputData = this.removeFirstCharacter(mLayout.lowerSegmentInputData.text.toString(), isZero = true)
                            if (lowerSegmentInputData.isNotEmpty()) {
                                lowerSegmentDataProceed()
                            } else {
                                Toast.makeText(this@MainActivity, "${lowerToFormatData.lowercase(Locale.getDefault())} data field empty, please enter your data", Toast.LENGTH_SHORT).also {
                                    it.show()
                                }
                            }
                        } else {
                            Toast.makeText(this@MainActivity, "please select \"form base number\" format", Toast.LENGTH_SHORT).also {
                                it.show()
                            }
                        }
                    } else {
                        Toast.makeText(this@MainActivity, "please select number format from dropdown list", Toast.LENGTH_SHORT).also {
                            it.show()
                        }
                    }
                } else {
                    Toast.makeText(this@MainActivity, "please select \"to base number\" format", Toast.LENGTH_SHORT).also {
                        it.show()
                    }
                }
            } else {
                if (upperFormatData.isNotEmpty()) {
                    if (formateList.contains(upperFormatData)) {
                        upperSegmentDataProceed()
                    } else {
                        Toast.makeText(this@MainActivity, "please select number format from dropdown list", Toast.LENGTH_SHORT).also {
                            it.show()
                        }
                    }
                } else {
                    Toast.makeText(this@MainActivity, "please select base number format", Toast.LENGTH_SHORT).also {
                        it.show()
                    }
                }
            }
        }

        mLayout.ivCopyDecimalUpperData.setOnClickListener {
            ClipData.newPlainText("decimal text", mLayout.tvDecimalUpperOutputData.text.toString()).also {
                clipboard.setPrimaryClip(it)
            }
            Toast.makeText(this@MainActivity, "decimal data copy to clipboard", Toast.LENGTH_SHORT).also {
                it.show()
            }
        }

        mLayout.ivCopyBinaryUpperData.setOnClickListener {
            ClipData.newPlainText("binary text", mLayout.tvBinaryUpperOutputData.text.toString()).also {
                clipboard.setPrimaryClip(it)
            }
            Toast.makeText(this@MainActivity, "binary data copy to clipboard", Toast.LENGTH_SHORT).also {
                it.show()
            }
        }

        mLayout.ivCopyOctalUpperData.setOnClickListener {
            ClipData.newPlainText("octal text", mLayout.tvOctalUpperOutputData.text.toString()).also {
                clipboard.setPrimaryClip(it)
            }
            Toast.makeText(this@MainActivity, "octal data copy to clipboard", Toast.LENGTH_SHORT).also {
                it.show()
            }
        }

        mLayout.ivCopyHexadecimalUpperData.setOnClickListener {
            ClipData.newPlainText("hexadecimal text", mLayout.tvHexadecimalUpperOutputData.text.toString()).also {
                clipboard.setPrimaryClip(it)
            }
            Toast.makeText(this@MainActivity, "hexadecimal data copy to clipboard", Toast.LENGTH_SHORT).also {
                it.show()
            }
        }

        mLayout.ivCopyLowerOutput.setOnClickListener {
            ClipData.newPlainText("${mLayout.tvLowerOutputDataText.text.toString()} text", mLayout.tvLowerOutputData.text.toString()).also {
                clipboard.setPrimaryClip(it)
            }
            Toast.makeText(this@MainActivity, "${lowerFormFormatData.lowercase(Locale.getDefault())} data copy to clipboard", Toast.LENGTH_SHORT).also {
                it.show()
            }
        }

        mLayout.upperSegmentInputLayout.setEndIconOnClickListener {
            if (!isActive) {
                if (upperFormatData.isNotEmpty()) {
                    val copyText: String = mLayout.upperSegmentInputData.text.toString()
                    if(copyText.isNotEmpty()) {
                        ClipData.newPlainText(upperFormatData, copyText).also {
                            clipboard.setPrimaryClip(it)
                        }
                        Toast.makeText(this@MainActivity, "${upperFormatData.lowercase(Locale.getDefault())} data copy to clipboard", Toast.LENGTH_SHORT).also {
                            it.show()
                        }
                    } else {
                        Toast.makeText(this@MainActivity, "sorry, can't copy content. ${upperFormatData.lowercase(Locale.getDefault())} value is empty", Toast.LENGTH_SHORT).also {
                            it.show()
                        }
                    }
                } else {
                    Toast.makeText(this@MainActivity, "sorry, can't copy content. please select \"number format\" first", Toast.LENGTH_SHORT).also {
                        it.show()
                    }
                }
            }
        }

        mLayout.lowerSegmentInputLayout.setEndIconOnClickListener {
            if (isActive) {
                if (lowerToFormatData.isNotEmpty()) {
                    val copyText: String = mLayout.lowerSegmentInputData.text.toString()
                    if(copyText.isNotEmpty()) {
                        ClipData.newPlainText(lowerToFormatData, copyText).also {
                            clipboard.setPrimaryClip(it)
                        }
                        Toast.makeText(this@MainActivity, "${lowerToFormatData.lowercase(Locale.getDefault())} data copy to clipboard", Toast.LENGTH_SHORT).also {
                            it.show()
                        }
                    } else {
                        Toast.makeText(this@MainActivity, "sorry, can't copy content. ${lowerToFormatData.lowercase(Locale.getDefault())} value is empty", Toast.LENGTH_SHORT).also {
                            it.show()
                        }
                    }
                } else {
                    Toast.makeText(this@MainActivity, "sorry, can't copy content. please select \"to number format\" first", Toast.LENGTH_SHORT).also {
                        it.show()
                    }
                }
            }
        }

        mLayout.tvDecimalUpperOutputData.setOnClickListener {
            mPopupDialogFragment = PopupDialogFragment.getInstance(
                this, "Decimal", mLayout.tvDecimalUpperOutputData.text.toString()
            )
            mPopupDialogFragment!!.show(supportFragmentManager, "DialogFragment")
        }
        mLayout.tvBinaryUpperOutputData.setOnClickListener {
            mPopupDialogFragment = PopupDialogFragment.getInstance(
                this, "Binary", mLayout.tvBinaryUpperOutputData.text.toString()
            )
            mPopupDialogFragment!!.show(supportFragmentManager, "DialogFragment")
        }
        mLayout.tvOctalUpperOutputData.setOnClickListener {
            mPopupDialogFragment = PopupDialogFragment.getInstance(
                this, "Octal", mLayout.tvOctalUpperOutputData.text.toString()
            )
            mPopupDialogFragment!!.show(supportFragmentManager, "DialogFragment")
        }
        mLayout.tvHexadecimalUpperOutputData.setOnClickListener {
            mPopupDialogFragment = PopupDialogFragment.getInstance(
                this, "Hexadecimal", mLayout.tvHexadecimalUpperOutputData.text.toString()
            )
            mPopupDialogFragment!!.show(supportFragmentManager, "DialogFragment")
        }
        mLayout.tvLowerOutputData.setOnClickListener {
            mPopupDialogFragment = PopupDialogFragment.getInstance(
                this, lowerFormFormatData, mLayout.tvLowerOutputData.text.toString()
            )
            mPopupDialogFragment!!.show(supportFragmentManager, "DialogFragment")
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        mNumberConverter = null
        spinnerAdapter = null
    }

    private fun upperSegmentDataProceed() {
        if (upperFormatData == "Decimal") {
            upperSegmentDecimalData = this.removeFirstCharacter(mLayout.upperSegmentInputData.text.toString(), isZero = true)
            if (upperSegmentDecimalData.isNotEmpty()) {
                if(mNumberConverter!!.isDecimalNumber(upperSegmentDecimalData, isUpperFractionActive)) {
                    try {
                        if (isUpperFractionActive) {
                            upperSegmentBinaryData = mNumberConverter!!.decimalToBinary(upperSegmentDecimalData, true)
                            upperSegmentOctalData = mNumberConverter!!.decimalToOctal(upperSegmentDecimalData, true)
                            upperSegmentHexadecimalData = mNumberConverter!!.decimalToHexadecimal(upperSegmentDecimalData, true)
                        } else {
                            upperSegmentBinaryData = mNumberConverter!!.convertDecimalToBinary(upperSegmentDecimalData.toLong())
                            upperSegmentOctalData = mNumberConverter!!.convertDecimalToOctal(upperSegmentDecimalData.toLong())
                            upperSegmentHexadecimalData = mNumberConverter!!.convertDecimalToHexadecimal(upperSegmentDecimalData.toLong())
                        }
                    } catch (ex: Exception) {
                        Toast.makeText(this@MainActivity, "your entered ${upperFormatData.lowercase(Locale.getDefault())} value is cross it's limit, please cheek your value", Toast.LENGTH_SHORT).also {
                            it.show()
                        }
                    }
                    updateUpperSegmentLayoutData(upperFormatData)
                } else {
                    Toast.makeText(this@MainActivity, "incorrect ${upperFormatData.lowercase(Locale.getDefault())} data, please check entered your data", Toast.LENGTH_SHORT).also {
                        it.show()
                    }
                }
            } else {
                Toast.makeText(this@MainActivity, "${upperFormatData.lowercase(Locale.getDefault())} data field empty, please enter your data", Toast.LENGTH_SHORT).also {
                    it.show()
                }
            }
        } else if((upperFormatData == "Binary")) {
            upperSegmentBinaryData = mLayout.upperSegmentInputData.text.toString()
            if (upperSegmentBinaryData.isNotEmpty()) {
                if(mNumberConverter!!.isBinaryNumber(upperSegmentBinaryData, isUpperFractionActive)) {
                    try {
                        if (isUpperFractionActive) {
                            upperSegmentDecimalData = mNumberConverter!!.binaryToDecimal(upperSegmentBinaryData, true)
                            upperSegmentOctalData = mNumberConverter!!.binaryToOctal(upperSegmentBinaryData, true)
                            upperSegmentHexadecimalData = mNumberConverter!!.binaryToHexadecimal(upperSegmentBinaryData, true)
                        } else {
                            upperSegmentDecimalData = mNumberConverter!!.convertBinaryToDecimal(upperSegmentBinaryData.toLong())
                            upperSegmentOctalData = mNumberConverter!!.convertBinaryToOctal(upperSegmentBinaryData.toLong())
                            upperSegmentHexadecimalData = mNumberConverter!!.convertBinaryToHexadecimal(upperSegmentBinaryData.toLong())
                        }
                    } catch (ex: Exception) {
                        Toast.makeText(this@MainActivity, "your entered ${upperFormatData.lowercase(Locale.getDefault())} value is cross it's limit, please cheek your value", Toast.LENGTH_SHORT).also {
                            it.show()
                        }
                    }
                    updateUpperSegmentLayoutData(upperFormatData)
                } else {
                    Toast.makeText(this@MainActivity, "incorrect ${upperFormatData.lowercase(Locale.getDefault())} Data, please check entered your data", Toast.LENGTH_SHORT).also {
                        it.show()
                    }
                }
            } else {
                Toast.makeText(this@MainActivity, "${upperFormatData.lowercase(Locale.getDefault())} data field empty, please enter your data", Toast.LENGTH_SHORT).also {
                    it.show()
                }
            }
        } else if((upperFormatData == "Octal")) {
            upperSegmentOctalData = mLayout.upperSegmentInputData.text.toString()
            if (upperSegmentOctalData.isNotEmpty()) {
                if(mNumberConverter!!.isOctalNumber(upperSegmentOctalData, isUpperFractionActive)) {
                    try {
                        if (isUpperFractionActive) {
                            upperSegmentDecimalData = mNumberConverter!!.octalToDecimal(upperSegmentOctalData, true)
                            upperSegmentBinaryData = mNumberConverter!!.octalToBinary(upperSegmentOctalData, true)
                            upperSegmentHexadecimalData = mNumberConverter!!.octalToHexadecimal(upperSegmentOctalData, true)
                        } else {
                            upperSegmentDecimalData = mNumberConverter!!.convertOctalToDecimal(upperSegmentOctalData.toLong())
                            upperSegmentBinaryData = mNumberConverter!!.convertOctalToBinary(upperSegmentOctalData.toLong())
                            upperSegmentHexadecimalData = mNumberConverter!!.convertOctalToHexadecimal(upperSegmentOctalData.toLong())
                        }
                    } catch (ex: Exception) {
                        Toast.makeText(this@MainActivity, "your entered ${upperFormatData.lowercase(Locale.getDefault())} value is cross it's limit, please cheek your value", Toast.LENGTH_SHORT).also {
                            it.show()
                        }
                    }
                    updateUpperSegmentLayoutData(upperFormatData)
                } else {
                    Toast.makeText(this@MainActivity, "incorrect ${upperFormatData.lowercase(Locale.getDefault())} Data, please check entered your data", Toast.LENGTH_SHORT).also {
                        it.show()
                    }
                }
            } else {
                Toast.makeText(this@MainActivity, "${upperFormatData.lowercase(Locale.getDefault())} data field empty, please enter your data", Toast.LENGTH_SHORT).also {
                    it.show()
                }
            }
        } else if((upperFormatData == "Hexadecimal")) {
            upperSegmentHexadecimalData = mLayout.upperSegmentInputData.text.toString()
            if (upperSegmentHexadecimalData.isNotEmpty()) {
                if(mNumberConverter!!.isHexadecimalNumber(upperSegmentHexadecimalData, isUpperFractionActive)) {
                    try {

                        if (isUpperFractionActive) {
                            upperSegmentDecimalData = mNumberConverter!!.hexadecimalToDecimal(upperSegmentHexadecimalData, true)
                            upperSegmentBinaryData = mNumberConverter!!.hexadecimalToBinary(upperSegmentHexadecimalData, true)
                            upperSegmentOctalData = mNumberConverter!!.hexadecimalToOctal(upperSegmentHexadecimalData, true)
                        } else {
                            upperSegmentDecimalData = mNumberConverter!!.convertHexadecimalToDecimal(upperSegmentHexadecimalData)
                            upperSegmentBinaryData = mNumberConverter!!.convertHexadecimalToBinary(upperSegmentHexadecimalData)
                            upperSegmentOctalData = mNumberConverter!!.convertHexadecimalToOctal(upperSegmentHexadecimalData)
                        }
                    } catch (ex: Exception) {
                        Toast.makeText(this@MainActivity, "your entered ${upperFormatData.lowercase(Locale.getDefault())} value is cross it's limit, please cheek your value", Toast.LENGTH_SHORT).also {
                            it.show()
                        }
                    }

                    updateUpperSegmentLayoutData(upperFormatData)
                } else {
                    Toast.makeText(this@MainActivity, "incorrect ${upperFormatData.lowercase(Locale.getDefault())} Data, please check entered your data", Toast.LENGTH_SHORT).also {
                        it.show()
                    }
                }
            } else {
                Toast.makeText(this@MainActivity, "${upperFormatData.lowercase(Locale.getDefault())} data field empty, please enter your data", Toast.LENGTH_SHORT).also {
                    it.show()
                }
            }
        }else {
            Toast.makeText(this@MainActivity, getString(R.string.went_wrong_restart), Toast.LENGTH_SHORT).also {
                it.show()
            }
            Snackbar.make(mLayout.constraintLayout, getString(R.string.went_wrong), Snackbar.LENGTH_SHORT).also {
                it.setAction("Exit"){
                    Toast.makeText(this@MainActivity, getString(R.string.reset), Toast.LENGTH_SHORT).show()
                    this.finish()
                    exitProcess(0)
                }
                it.show()
            }

        }
    }

    private fun lowerSegmentDataProceed() {

        if (lowerToFormatData == "Decimal") {
            if (lowerFormFormatData == "Decimal") {
                Toast.makeText(this@MainActivity, getString(R.string.same_format), Toast.LENGTH_SHORT).also {
                    it.show()
                }
            } else if(lowerFormFormatData == "Binary") {
                if(mNumberConverter!!.isDecimalNumber(lowerSegmentInputData, isLowerFractionActive)) {
                    try {
                        if (isLowerFractionActive) {
                            mLayout.tvLowerOutputData.text = mNumberConverter!!.decimalToBinary(
                                lowerSegmentInputData, true
                            )
                        } else {
                            mLayout.tvLowerOutputData.text = mNumberConverter!!.convertDecimalToBinary(
                                lowerSegmentInputData.toLong()
                            )
                        }
                    } catch (ex: Exception) {
                        Toast.makeText(this@MainActivity, "your entered ${lowerToFormatData.lowercase(Locale.getDefault())} value is cross it's limit, please cheek your value", Toast.LENGTH_SHORT).also {
                            it.show()
                        }
                    }
                    mLayout.tvLowerOutputDataText.text = "$lowerFormFormatData : "
                    mLayout.constraintLayoutLowerOutput.visibility = View.VISIBLE
                } else {
                    Toast.makeText(this@MainActivity, "incorrect ${lowerToFormatData.lowercase(Locale.getDefault())} number data", Toast.LENGTH_SHORT).also {
                        it.show()
                    }
                }
            } else if(lowerFormFormatData == "Octal") {
                if(mNumberConverter!!.isDecimalNumber(lowerSegmentInputData, isLowerFractionActive)) {
                    try {
                        if (isLowerFractionActive) {
                            mLayout.tvLowerOutputData.text = mNumberConverter!!.decimalToOctal(
                                lowerSegmentInputData, true
                            )
                        } else {
                            mLayout.tvLowerOutputData.text = mNumberConverter!!.convertDecimalToOctal(
                                lowerSegmentInputData.toLong()
                            )
                        }
                    } catch (ex: Exception) {
                        Toast.makeText(this@MainActivity, "your entered ${lowerToFormatData.lowercase(Locale.getDefault())} value is cross it's limit, please cheek your value", Toast.LENGTH_SHORT).also {
                            it.show()
                        }
                    }
                    mLayout.tvLowerOutputDataText.text = "$lowerFormFormatData : "
                    mLayout.constraintLayoutLowerOutput.visibility = View.VISIBLE
                } else {
                    Toast.makeText(this@MainActivity, "incorrect ${lowerToFormatData.lowercase(Locale.getDefault())} number data", Toast.LENGTH_SHORT).also {
                        it.show()
                    }
                }
            } else if(lowerFormFormatData == "Hexadecimal") {
                if(mNumberConverter!!.isDecimalNumber(lowerSegmentInputData, isLowerFractionActive)) {
                    try {
                        if (isLowerFractionActive) {
                            mLayout.tvLowerOutputData.text = mNumberConverter!!.decimalToHexadecimal(
                                lowerSegmentInputData, true
                            )
                        } else {
                            mLayout.tvLowerOutputData.text = mNumberConverter!!.convertDecimalToHexadecimal(
                                lowerSegmentInputData.toLong()
                            )
                        }
                    } catch (ex: Exception) {
                        Toast.makeText(this@MainActivity, "your entered ${lowerToFormatData.lowercase(Locale.getDefault())} value is cross it's limit, please cheek your value", Toast.LENGTH_SHORT).also {
                            it.show()
                        }
                    }
                    mLayout.tvLowerOutputDataText.text = "$lowerFormFormatData : "
                    mLayout.constraintLayoutLowerOutput.visibility = View.VISIBLE
                } else {
                    Toast.makeText(this@MainActivity, "incorrect ${lowerToFormatData.lowercase(Locale.getDefault())} number data", Toast.LENGTH_SHORT).also {
                        it.show()
                    }
                }
            } else {
                Toast.makeText(this@MainActivity, getString(R.string.went_wrong), Toast.LENGTH_SHORT).also {
                    it.show()
                }
            }
        } else if(lowerToFormatData == "Binary") {
            if (lowerFormFormatData == "Decimal") {
                if(mNumberConverter!!.isBinaryNumber(lowerSegmentInputData, isLowerFractionActive)) {
                    try {
                        if (isLowerFractionActive) {
                            mLayout.tvLowerOutputData.text = mNumberConverter!!.binaryToDecimal(
                                lowerSegmentInputData, true
                            )
                        } else {
                            mLayout.tvLowerOutputData.text = mNumberConverter!!.convertBinaryToDecimal(
                                lowerSegmentInputData.toLong()
                            )
                        }
                    } catch (ex: Exception) {
                        Toast.makeText(this@MainActivity, "your entered ${lowerToFormatData.lowercase(Locale.getDefault())} value is cross it's limit, please cheek your value", Toast.LENGTH_SHORT).also {
                            it.show()
                        }
                    }
                    mLayout.tvLowerOutputDataText.text = "$lowerFormFormatData : "
                    mLayout.constraintLayoutLowerOutput.visibility = View.VISIBLE
                } else {
                    Toast.makeText(this@MainActivity, "incorrect ${lowerToFormatData.lowercase(Locale.getDefault())} number data", Toast.LENGTH_SHORT).also {
                        it.show()
                    }
                }
            } else if(lowerFormFormatData == "Binary") {
                Toast.makeText(this@MainActivity, getString(R.string.same_format), Toast.LENGTH_SHORT).also {
                    it.show()
                }
            } else if(lowerFormFormatData == "Octal") {
                if(mNumberConverter!!.isBinaryNumber(lowerSegmentInputData, isLowerFractionActive)) {
                    try {
                        if (isLowerFractionActive) {
                            mLayout.tvLowerOutputData.text = mNumberConverter!!.binaryToOctal(
                                lowerSegmentInputData, true
                            )
                        } else {
                            mLayout.tvLowerOutputData.text = mNumberConverter!!.convertBinaryToOctal(
                                lowerSegmentInputData.toLong()
                            )
                        }
                    } catch (ex: Exception) {
                        Toast.makeText(this@MainActivity, "your entered ${lowerToFormatData.lowercase(Locale.getDefault())} value is cross it's limit, please cheek your value", Toast.LENGTH_SHORT).also {
                            it.show()
                        }
                    }
                    mLayout.tvLowerOutputDataText.text = "$lowerFormFormatData : "
                    mLayout.constraintLayoutLowerOutput.visibility = View.VISIBLE
                } else {
                    Toast.makeText(this@MainActivity, "incorrect ${lowerToFormatData.lowercase(Locale.getDefault())} number data", Toast.LENGTH_SHORT).also {
                        it.show()
                    }
                }
            } else if(lowerFormFormatData == "Hexadecimal") {
                if(mNumberConverter!!.isBinaryNumber(lowerSegmentInputData, isLowerFractionActive)) {
                    try {
                        if (isLowerFractionActive) {
                            mLayout.tvLowerOutputData.text = mNumberConverter!!.binaryToHexadecimal(
                                lowerSegmentInputData, true
                            )
                        } else {
                            mLayout.tvLowerOutputData.text = mNumberConverter!!.convertBinaryToHexadecimal(
                                lowerSegmentInputData.toLong()
                            )
                        }
                    } catch (ex: Exception) {
                        Toast.makeText(this@MainActivity, "your entered ${lowerToFormatData.lowercase(Locale.getDefault())} value is cross it's limit, please cheek your value", Toast.LENGTH_SHORT).also {
                            it.show()
                        }
                    }
                    mLayout.tvLowerOutputDataText.text = "$lowerFormFormatData : "
                    mLayout.constraintLayoutLowerOutput.visibility = View.VISIBLE
                } else {
                    Toast.makeText(this@MainActivity, "incorrect ${lowerToFormatData.lowercase(Locale.getDefault())} number data", Toast.LENGTH_SHORT).also {
                        it.show()
                    }
                }
            } else {
                Toast.makeText(this@MainActivity, getString(R.string.went_wrong), Toast.LENGTH_SHORT).also {
                    it.show()
                }
            }
        } else if(lowerToFormatData == "Octal") {
            if (lowerFormFormatData == "Decimal") {
                if(mNumberConverter!!.isOctalNumber(lowerSegmentInputData, isLowerFractionActive)) {
                    try {
                        if (isLowerFractionActive) {
                            mLayout.tvLowerOutputData.text = mNumberConverter!!.octalToDecimal(
                                lowerSegmentInputData, true
                            )
                        } else {
                            mLayout.tvLowerOutputData.text = mNumberConverter!!.convertOctalToDecimal(
                                lowerSegmentInputData.toLong()
                            )
                        }
                    } catch (ex: Exception) {
                        Toast.makeText(this@MainActivity, "your entered ${lowerToFormatData.lowercase(Locale.getDefault())} value is cross it's limit, please cheek your value", Toast.LENGTH_SHORT).also {
                            it.show()
                        }
                    }
                    mLayout.tvLowerOutputDataText.text = "$lowerFormFormatData : "
                    mLayout.constraintLayoutLowerOutput.visibility = View.VISIBLE
                } else {
                    Toast.makeText(this@MainActivity, "incorrect ${lowerToFormatData.lowercase(Locale.getDefault())} number data", Toast.LENGTH_SHORT).also {
                        it.show()
                    }
                }
            } else if(lowerFormFormatData == "Binary") {
                if(mNumberConverter!!.isOctalNumber(lowerSegmentInputData, isLowerFractionActive)) {
                    try {
                        if (isLowerFractionActive) {
                            mLayout.tvLowerOutputData.text = mNumberConverter!!.octalToBinary(
                                lowerSegmentInputData, true
                            )
                        } else {
                            mLayout.tvLowerOutputData.text = mNumberConverter!!.convertOctalToBinary(
                                lowerSegmentInputData.toLong()
                            )
                        }
                    } catch (ex: Exception) {
                        Toast.makeText(this@MainActivity, "your entered ${lowerToFormatData.lowercase(Locale.getDefault())} value is cross it's limit, please cheek your value", Toast.LENGTH_SHORT).also {
                            it.show()
                        }
                    }
                    mLayout.tvLowerOutputDataText.text = "$lowerFormFormatData : "
                    mLayout.constraintLayoutLowerOutput.visibility = View.VISIBLE
                } else {
                    Toast.makeText(this@MainActivity, "incorrect ${lowerToFormatData.lowercase(Locale.getDefault())} number data", Toast.LENGTH_SHORT).also {
                        it.show()
                    }
                }
            } else if(lowerFormFormatData == "Octal") {
                Toast.makeText(this@MainActivity, getString(R.string.same_format), Toast.LENGTH_SHORT).also {
                    it.show()
                }
            } else if(lowerFormFormatData == "Hexadecimal") {
                if(mNumberConverter!!.isOctalNumber(lowerSegmentInputData, isLowerFractionActive)) {
                    try {
                        if (isLowerFractionActive) {
                            mLayout.tvLowerOutputData.text = mNumberConverter!!.octalToHexadecimal(
                                lowerSegmentInputData, true
                            )
                        } else {
                            mLayout.tvLowerOutputData.text = mNumberConverter!!.convertOctalToHexadecimal(
                                lowerSegmentInputData.toLong()
                            )
                        }
                    } catch (ex: Exception) {
                        Toast.makeText(this@MainActivity, "your entered ${lowerToFormatData.lowercase(Locale.getDefault())} value is cross it's limit, please cheek your value", Toast.LENGTH_SHORT).also {
                            it.show()
                        }
                    }
                    mLayout.tvLowerOutputDataText.text = "$lowerFormFormatData : "
                    mLayout.constraintLayoutLowerOutput.visibility = View.VISIBLE
                } else {
                    Toast.makeText(this@MainActivity, "incorrect ${lowerToFormatData.lowercase(Locale.getDefault())} number data", Toast.LENGTH_SHORT).also {
                        it.show()
                    }
                }
            } else {
                Toast.makeText(this@MainActivity, getString(R.string.went_wrong), Toast.LENGTH_SHORT).also {
                    it.show()
                }
            }
        } else if(lowerToFormatData == "Hexadecimal") {
            if (lowerFormFormatData == "Decimal") {
                if(mNumberConverter!!.isHexadecimalNumber(lowerSegmentInputData, isLowerFractionActive)) {
                    try {
                        if (isLowerFractionActive) {
                            mLayout.tvLowerOutputData.text = mNumberConverter!!.hexadecimalToDecimal(
                                lowerSegmentInputData, true
                            )
                        } else {
                            mLayout.tvLowerOutputData.text = mNumberConverter!!.convertHexadecimalToDecimal(
                                lowerSegmentInputData
                            )
                        }
                    } catch (ex: Exception) {
                        Toast.makeText(this@MainActivity, "your entered ${lowerToFormatData.lowercase(Locale.getDefault())} value is cross it's limit, please cheek your value", Toast.LENGTH_SHORT).also {
                            it.show()
                        }
                    }
                    mLayout.tvLowerOutputDataText.text = "$lowerFormFormatData : "
                    mLayout.constraintLayoutLowerOutput.visibility = View.VISIBLE
                } else {
                    Toast.makeText(this@MainActivity, "incorrect ${lowerToFormatData.lowercase(Locale.getDefault())} number data", Toast.LENGTH_SHORT).also {
                        it.show()
                    }
                }
            } else if(lowerFormFormatData == "Binary") {
                if(mNumberConverter!!.isHexadecimalNumber(lowerSegmentInputData, isLowerFractionActive)) {
                    try {
                        if (isLowerFractionActive) {
                            mLayout.tvLowerOutputData.text = mNumberConverter!!.hexadecimalToBinary(
                                lowerSegmentInputData, true
                            )
                        } else {
                            mLayout.tvLowerOutputData.text = mNumberConverter!!.convertHexadecimalToBinary(
                                lowerSegmentInputData
                            )
                        }
                    } catch (ex: Exception) {
                        Toast.makeText(this@MainActivity, "your entered ${lowerToFormatData.lowercase(Locale.getDefault())} value is cross it's limit, please cheek your value", Toast.LENGTH_SHORT).also {
                            it.show()
                        }
                    }
                    mLayout.tvLowerOutputDataText.text = "$lowerFormFormatData : "
                    mLayout.constraintLayoutLowerOutput.visibility = View.VISIBLE
                } else {
                    Toast.makeText(this@MainActivity, "incorrect ${lowerToFormatData.lowercase(Locale.getDefault())} number data", Toast.LENGTH_SHORT).also {
                        it.show()
                    }
                }
            } else if(lowerFormFormatData == "Octal") {
                if(mNumberConverter!!.isHexadecimalNumber(lowerSegmentInputData, isLowerFractionActive)) {
                    try {
                        if (isLowerFractionActive) {
                            mLayout.tvLowerOutputData.text = mNumberConverter!!.hexadecimalToOctal(
                                lowerSegmentInputData, true
                            )
                        } else {
                            mLayout.tvLowerOutputData.text = mNumberConverter!!.convertHexadecimalToOctal(
                                lowerSegmentInputData
                            )
                        }
                    } catch (ex: Exception) {
                        Toast.makeText(this@MainActivity, "your entered ${lowerToFormatData.lowercase(Locale.getDefault())} value is cross it's limit, please cheek your value", Toast.LENGTH_SHORT).also {
                            it.show()
                        }
                    }
                    mLayout.tvLowerOutputDataText.text = "$lowerFormFormatData : "
                    mLayout.constraintLayoutLowerOutput.visibility = View.VISIBLE
                } else {
                    Toast.makeText(this@MainActivity, "incorrect ${lowerToFormatData.lowercase(Locale.getDefault())} number data", Toast.LENGTH_SHORT).also {
                        it.show()
                    }
                }
            } else if(lowerFormFormatData == "Hexadecimal") {
                Toast.makeText(this@MainActivity, getString(R.string.same_format), Toast.LENGTH_SHORT).also {
                    it.show()
                }
            } else {
                Toast.makeText(this@MainActivity, getString(R.string.went_wrong), Toast.LENGTH_SHORT).also {
                    it.show()
                }
            }
        } else {
            Toast.makeText(this@MainActivity, getString(R.string.went_wrong), Toast.LENGTH_SHORT).also {
                it.show()
            }
        }


    }

    private fun updateUpperSegmentLayoutData(formatData: String) {

        mLayout.tvDecimalUpperOutputData.text = upperSegmentDecimalData
        mLayout.tvBinaryUpperOutputData.text = upperSegmentBinaryData
        mLayout.tvOctalUpperOutputData.text = upperSegmentOctalData
        mLayout.tvHexadecimalUpperOutputData.text = upperSegmentHexadecimalData

        mLayout.constraintLayoutDecimalUpperOutputText.visibility = View.VISIBLE
        mLayout.constraintLayoutBinaryUpperOutputText.visibility = View.VISIBLE
        mLayout.constraintLayoutOctalUpperOutputText.visibility = View.VISIBLE
        mLayout.constraintLayoutHexadecimalUpperOutputText.visibility = View.VISIBLE

        if (formatData == "Decimal") {
            mLayout.constraintLayoutDecimalUpperOutputText.visibility = View.GONE
            // mLayout.tvDecimalUpperOutputData.visibility = View.INVISIBLE
        } else if((formatData == "Binary")) {
            mLayout.constraintLayoutBinaryUpperOutputText.visibility = View.GONE
            // mLayout.tvBinaryUpperOutputData.visibility = View.INVISIBLE
        } else if((formatData == "Octal")) {
            mLayout.constraintLayoutOctalUpperOutputText.visibility = View.GONE
            // mLayout.tvOctalUpperOutputData.visibility = View.INVISIBLE
        } else if((formatData == "Hexadecimal")) {
            mLayout.constraintLayoutHexadecimalUpperOutputText.visibility = View.GONE
            // mLayout.tvHexadecimalUpperOutputData.visibility = View.INVISIBLE
        }else {
            Toast.makeText(this@MainActivity, "Something Went Wrong, Restart Application", Toast.LENGTH_SHORT).also {
                it.show()
            }

            mLayout.constraintLayoutDecimalUpperOutputText.visibility = View.INVISIBLE
            mLayout.constraintLayoutBinaryUpperOutputText.visibility = View.INVISIBLE
            mLayout.constraintLayoutOctalUpperOutputText.visibility = View.INVISIBLE
            mLayout.constraintLayoutHexadecimalUpperOutputText.visibility = View.INVISIBLE
        }
    }

    private fun onUpperSegmentRefresh() {
        // mLayout.upperSegmentInputData.setText("")
        upperSegmentDecimalData = ""
        upperSegmentBinaryData = ""
        upperSegmentOctalData = ""
        upperSegmentHexadecimalData = ""
        /*
        mLayout.tvDecimalUpperOutputData.text = ""
        mLayout.tvBinaryUpperOutputData.text = ""
        mLayout.tvOctalUpperOutputData.text = ""
        mLayout.tvHexadecimalUpperOutputData.text = ""

        mLayout.constraintLayoutDecimalUpperOutputText.visibility = View.INVISIBLE
        mLayout.constraintLayoutBinaryUpperOutputText.visibility = View.INVISIBLE
        mLayout.constraintLayoutOctalUpperOutputText.visibility = View.INVISIBLE
        mLayout.constraintLayoutHexadecimalUpperOutputText.visibility = View.INVISIBLE
        */

        mLayout.upperSegmentInputLayout.error = null
    }

    private fun onLowerSegmentRefresh() {
        // mLayout.lowerSegmentInputData.setText("")
        lowerSegmentInputData = ""
        // mLayout.constraintLayoutLowerOutput.visibility = View.INVISIBLE
        mLayout.lowerSegmentInputLayout.error = null
    }

    private fun initializeSpinner(onRefreshUpper: Boolean = false, onRefreshLower: Boolean = false) {
        if (spinnerAdapter == null) {
            spinnerAdapter = ArrayAdapter<String>(
                this@MainActivity,
                android.R.layout.simple_spinner_dropdown_item,
                formateList
            )

            mLayout.upperSpinner.setAdapter(spinnerAdapter)
            mLayout.lowerToSpinner.setAdapter(spinnerAdapter)
            mLayout.lowerFormSpinner.setAdapter(spinnerAdapter)
        } else {
            if (onRefreshUpper) {
                mLayout.upperSpinner.setAdapter(spinnerAdapter)
            }

            if (onRefreshLower) {
                mLayout.lowerToSpinner.setAdapter(spinnerAdapter)
                mLayout.lowerFormSpinner.setAdapter(spinnerAdapter)
            }
        }
    }

    private fun initializeFirstTime() {
        mLayout.upperSegmentInputLayout.isEndIconVisible = false
        mLayout.lowerSegmentInputLayout.isEndIconVisible = false
        this.enableUpperSegment(true)
        this.enableLowerSegment(false)
    }

    private fun enableUpperSegment(isEnable: Boolean) {
        if (isEnable) {
            mLayout.tvUpperSegmentTextStatus.text = "Enable"
            mLayout.tvUpperSegmentTextStatus.setTextColor(
                ContextCompat.getColor(this@MainActivity, R.color.green)
            )

            mLayout.upperSegmentSpinnerLayout.isEnabled = true
            mLayout.upperSegmentSpinnerLayout.isClickable = true
            mLayout.upperSpinner.isEnabled = true
            mLayout.upperSpinner.isClickable = true

            mLayout.upperSegmentInputLayout.isEnabled = true
            mLayout.upperSegmentInputLayout.isClickable = true
            mLayout.upperSegmentInputData.isEnabled = true
            mLayout.upperSegmentInputData.isClickable = true

            /*
            mLayout.constraintLayoutDecimalUpperOutputText.visibility = View.INVISIBLE
            mLayout.constraintLayoutBinaryUpperOutputText.visibility = View.INVISIBLE
            mLayout.constraintLayoutOctalUpperOutputText.visibility = View.INVISIBLE
            mLayout.constraintLayoutHexadecimalUpperOutputText.visibility = View.INVISIBLE
            */
        } else {
            mLayout.tvUpperSegmentTextStatus.text = "Disable"
            mLayout.tvUpperSegmentTextStatus.setTextColor(
                ContextCompat.getColor(this@MainActivity, R.color.gray)
            )

            mLayout.upperSegmentSpinnerLayout.isEnabled = false
            mLayout.upperSegmentSpinnerLayout.isClickable = false
            mLayout.upperSpinner.isEnabled = false
            mLayout.upperSpinner.isClickable = false

            mLayout.upperSegmentInputLayout.isEnabled = false
            mLayout.upperSegmentInputLayout.isClickable = false
            mLayout.upperSegmentInputData.isEnabled = false
            mLayout.upperSegmentInputData.isClickable = false

            /*
            mLayout.constraintLayoutDecimalUpperOutputText.visibility = View.INVISIBLE
            mLayout.constraintLayoutBinaryUpperOutputText.visibility = View.INVISIBLE
            mLayout.constraintLayoutOctalUpperOutputText.visibility = View.INVISIBLE
            mLayout.constraintLayoutHexadecimalUpperOutputText.visibility = View.INVISIBLE
            */
        }
    }
    private fun enableLowerSegment(isEnable: Boolean) {
        if (isEnable) {
            mLayout.tvLowerSegmentTextStatus.text = "Enable"
            mLayout.tvLowerSegmentTextStatus.setTextColor(
                ContextCompat.getColor(this@MainActivity, R.color.green)
            )

            mLayout.toLowerToSpinnerLayout.isEnabled = true
            mLayout.toLowerToSpinnerLayout.isClickable = true
            mLayout.lowerToSpinner.isEnabled = true
            mLayout.lowerToSpinner.isClickable = true

            mLayout.toLowerFormSpinnerLayout.isEnabled = true
            mLayout.toLowerFormSpinnerLayout.isClickable = true
            mLayout.lowerFormSpinner.isEnabled = true
            mLayout.lowerFormSpinner.isClickable = true

            mLayout.lowerSegmentInputLayout.isEnabled = true
            mLayout.lowerSegmentInputLayout.isClickable = true
            mLayout.lowerSegmentInputData.isEnabled = true
            mLayout.lowerSegmentInputData.isClickable = true

            // mLayout.constraintLayoutLowerOutput.visibility = View.INVISIBLE
        } else {
            mLayout.tvLowerSegmentTextStatus.text = "Disable"
            mLayout.tvLowerSegmentTextStatus.setTextColor(
                ContextCompat.getColor(this@MainActivity, R.color.gray)
            )

            mLayout.toLowerToSpinnerLayout.isEnabled = false
            mLayout.toLowerToSpinnerLayout.isClickable = false
            mLayout.lowerToSpinner.isEnabled = false
            mLayout.lowerToSpinner.isClickable = false

            mLayout.toLowerFormSpinnerLayout.isEnabled = false
            mLayout.toLowerFormSpinnerLayout.isClickable = false
            mLayout.lowerFormSpinner.isEnabled = false
            mLayout.lowerFormSpinner.isClickable = false

            mLayout.lowerSegmentInputLayout.isEnabled = false
            mLayout.lowerSegmentInputLayout.isClickable = false
            mLayout.lowerSegmentInputData.isEnabled = false
            mLayout.lowerSegmentInputData.isClickable = false

            // mLayout.constraintLayoutLowerOutput.visibility = View.INVISIBLE
        }
    }

    private fun removeFirstCharacter(data: String, isZero: Boolean = true) : String {
        return if (data.startsWith("0")) {
            data.substring(1)
        } else data
    }

    override fun onDialogExit() {
        mPopupDialogFragment?.dismiss()
        mPopupDialogFragment = null
    }

    override fun onDialogCancel() {
        mPopupDialogFragment?.dismiss()
        mPopupDialogFragment = null
    }

    override fun onDialogDataCopy(title: String, data: String) {
        ClipData.newPlainText("$title text", data).also {
            clipboard.setPrimaryClip(it)
        }
        Toast.makeText(this@MainActivity, "${title.lowercase(Locale.forLanguageTag("In"))} data copy to clipboard", Toast.LENGTH_SHORT).also {
            it.show()
        }
    }

}