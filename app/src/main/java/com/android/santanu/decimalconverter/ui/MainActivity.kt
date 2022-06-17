package com.android.santanu.decimalconverter.ui

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColor
import androidx.core.widget.doOnTextChanged
import com.android.santanu.decimalconverter.R
import com.android.santanu.decimalconverter.data.Converter
import com.android.santanu.decimalconverter.databinding.ActivityMainBinding
import com.android.santanu.decimalconverter.ui.base.BaseActivity
import com.android.santanu.decimalconverter.vm.MainViewModel
import com.google.android.material.snackbar.Snackbar
import kotlin.system.exitProcess


class MainActivity : BaseActivity<ActivityMainBinding>() {
    private val TAG: String = MainActivity::class.java.simpleName


    private lateinit var mLayout: ActivityMainBinding
    private val mMainViewModel: MainViewModel by viewModels<MainViewModel>()

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

    private var mConverter: Converter? = null

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
        this.initializeSpinner()
        // this.onUpperSegmentRefresh()
        // this.onLowerSegmentRefresh()
        this.initializeFirstTime()

        if(mConverter == null) {
            mConverter = Converter()
        }

        clipboard  = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

        mLayout.upperSpinner.showSoftInputOnFocus = false
        mLayout.lowerToSpinner.showSoftInputOnFocus = false
        mLayout.lowerFormSpinner.showSoftInputOnFocus = false


        /*
        mMainViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(
            application
        ).create(MainViewModel::class.java)
        */

        mLayout.upperSpinner.doOnTextChanged { text, _, _, _ ->
            upperFormatData = text.toString()
            this.onUpperSegmentRefresh()
        }

        mLayout.lowerToSpinner.doOnTextChanged { text, _, _, _ ->
            lowerToFormatData = text.toString()
            this.onLowerSegmentRefresh()
        }

        mLayout.lowerFormSpinner.doOnTextChanged { text, _, _, _ ->
            lowerFormFormatData = text.toString()
            this.onLowerSegmentRefresh()
        }

        mLayout.upperSegmentInputData.doOnTextChanged { text, _, _, _ ->
            if (!isActive) {
                if (upperFormatData.isNotEmpty()) {
                    when (upperFormatData) {
                        "Decimal" -> {
                            if(mConverter!!.isDecimalNumber(text.toString())) {
                                // mLayout.upperSegmentInputLayout.helperText = null
                                mLayout.upperSegmentInputLayout.error = null
                                mLayout.convertButton.isEnabled = true

                            } else {
                                // mLayout.upperSegmentInputLayout.helperText = "Incorrect $upperFormatData Format"
                                mLayout.upperSegmentInputLayout.error = "Incorrect $upperFormatData Format"
                                mLayout.convertButton.isEnabled = false

                            }
                        }
                        "Binary" -> {
                            if(mConverter!!.isBinaryNumber(text.toString())) {
                                // mLayout.upperSegmentInputLayout.helperText = null
                                mLayout.upperSegmentInputLayout.error = null
                                mLayout.convertButton.isEnabled = true

                            } else {
                                // mLayout.upperSegmentInputLayout.helperText = "Incorrect $upperFormatData Format"
                                mLayout.upperSegmentInputLayout.error = "Incorrect $upperFormatData Format"
                                mLayout.convertButton.isEnabled = false

                            }
                        }
                        "Octal" -> {
                            if(mConverter!!.isOctalNumber(text.toString())) {
                                // mLayout.upperSegmentInputLayout.helperText = null
                                mLayout.upperSegmentInputLayout.error = null
                                mLayout.convertButton.isEnabled = true

                            } else {
                                // mLayout.upperSegmentInputLayout.helperText = "Incorrect $upperFormatData Format"
                                mLayout.upperSegmentInputLayout.error = "Incorrect $upperFormatData Format"
                                mLayout.convertButton.isEnabled = false

                            }
                        }
                        "Hexadecimal" -> {
                            if(mConverter!!.isHexadecimalNumber(text.toString())) {
                                // mLayout.upperSegmentInputLayout.helperText = null
                                mLayout.upperSegmentInputLayout.error = null
                                mLayout.convertButton.isEnabled = true

                            } else {
                                // mLayout.upperSegmentInputLayout.helperText = "Incorrect $upperFormatData Format"
                                mLayout.upperSegmentInputLayout.error = "Incorrect $upperFormatData Format"
                                mLayout.convertButton.isEnabled = false

                            }
                        }
                        else -> {
                            Toast.makeText(this@MainActivity, "Something Went Wrong", Toast.LENGTH_SHORT).also {
                                it.show()
                            }
                        }
                    }
                } else {
                    Toast.makeText(this@MainActivity, "Select Base Format First", Toast.LENGTH_SHORT).also {
                        it.show()
                    }
                }
            }
        }

        mLayout.lowerSegmentInputData.doOnTextChanged { text, _, _, _ ->
            if (isActive) {
                if (lowerToFormatData.isNotEmpty() && lowerFormFormatData.isNotEmpty()) {
                    when (lowerToFormatData) {
                        "Decimal" -> {
                            if(mConverter!!.isDecimalNumber(text.toString())) {
                                // mLayout.lowerSegmentInputLayout.helperText = null
                                mLayout.lowerSegmentInputLayout.error = null
                                mLayout.convertButton.isEnabled = true

                            } else {
                                // mLayout.lowerSegmentInputLayout.helperText = "Incorrect $lowerToFormatData Format"
                                mLayout.lowerSegmentInputLayout.error = "Incorrect $lowerToFormatData Format"
                                mLayout.convertButton.isEnabled = false

                            }
                        }
                        "Binary" -> {
                            if(mConverter!!.isBinaryNumber(text.toString())) {
                                // mLayout.lowerSegmentInputLayout.helperText = null
                                mLayout.lowerSegmentInputLayout.error = null
                                mLayout.convertButton.isEnabled = true

                            } else {
                                // mLayout.lowerSegmentInputLayout.helperText = "Incorrect $lowerToFormatData Format"
                                mLayout.lowerSegmentInputLayout.error = "Incorrect $lowerToFormatData Format"
                                mLayout.convertButton.isEnabled = false

                            }
                        }
                        "Octal" -> {
                            if(mConverter!!.isOctalNumber(text.toString())) {
                                // mLayout.lowerSegmentInputLayout.helperText = null
                                mLayout.lowerSegmentInputLayout.error = null
                                mLayout.convertButton.isEnabled = true

                            } else {
                                // mLayout.lowerSegmentInputLayout.helperText = "Incorrect $lowerToFormatData Format"
                                mLayout.lowerSegmentInputLayout.error = "Incorrect $lowerToFormatData Format"
                                mLayout.convertButton.isEnabled = false

                            }
                        }
                        "Hexadecimal" -> {
                            if(mConverter!!.isHexadecimalNumber(text.toString())) {
                                // mLayout.lowerSegmentInputLayout.helperText = null
                                mLayout.lowerSegmentInputLayout.error = null
                                mLayout.convertButton.isEnabled = true

                            } else {
                                // mLayout.lowerSegmentInputLayout.helperText = "Incorrect $lowerToFormatData Format"
                                mLayout.lowerSegmentInputLayout.error = "Incorrect $lowerToFormatData Format"
                                mLayout.convertButton.isEnabled = false

                            }
                        }
                        else -> {
                            Toast.makeText(this@MainActivity, "Something Went Wrong", Toast.LENGTH_SHORT).also {
                                it.show()
                            }
                        }
                    }
                } else {
                    Toast.makeText(this@MainActivity, "Select Base Format First", Toast.LENGTH_SHORT).also {
                        it.show()
                    }
                }
            }
        }

        mLayout.swActive.setOnCheckedChangeListener { _, isChecked ->
            isActive = isChecked
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

        mLayout.convertButton.setOnClickListener {

            if (isActive) {
                if (lowerToFormatData.isNotEmpty()) {
                    if (lowerFormFormatData.isNotEmpty()) {
                        lowerSegmentInputData = this.removeFirstCharacter(mLayout.lowerSegmentInputData.text.toString(), isZero = true)
                        if (lowerSegmentInputData.isNotEmpty()) {
                            lowerSegmentDataProceed()
                        } else {
                            Toast.makeText(this@MainActivity, "please enter $lowerToFormatData your value", Toast.LENGTH_SHORT).also {
                                it.show()
                            }
                        }
                    } else {
                        Toast.makeText(this@MainActivity, "please select form base number format", Toast.LENGTH_SHORT).also {
                            it.show()
                        }
                    }
                } else {
                    Toast.makeText(this@MainActivity, "please select to base number format", Toast.LENGTH_SHORT).also {
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
            Toast.makeText(this@MainActivity, "Decimal Copy to Clipboard", Toast.LENGTH_SHORT).also {
                it.show()
            }
        }

        mLayout.ivCopyBinaryUpperData.setOnClickListener {
            ClipData.newPlainText("binary text", mLayout.tvBinaryUpperOutputData.text.toString()).also {
                clipboard.setPrimaryClip(it)
            }
            Toast.makeText(this@MainActivity, "Binary Copy to Clipboard", Toast.LENGTH_SHORT).also {
                it.show()
            }
        }

        mLayout.ivCopyOctalUpperData.setOnClickListener {
            ClipData.newPlainText("octal text", mLayout.tvOctalUpperOutputData.text.toString()).also {
                clipboard.setPrimaryClip(it)
            }
            Toast.makeText(this@MainActivity, "Octal Copy to Clipboard", Toast.LENGTH_SHORT).also {
                it.show()
            }
        }

        mLayout.ivCopyHexadecimalUpperData.setOnClickListener {
            ClipData.newPlainText("hexadecimal text", mLayout.tvHexadecimalUpperOutputData.text.toString()).also {
                clipboard.setPrimaryClip(it)
            }
            Toast.makeText(this@MainActivity, "Hexadecimal Copy to Clipboard", Toast.LENGTH_SHORT).also {
                it.show()
            }
        }

        mLayout.ivCopyLowerOutput.setOnClickListener {
            ClipData.newPlainText("${mLayout.tvLowerOutputDataText.text.toString()} text", mLayout.tvLowerOutputData.text.toString()).also {
                clipboard.setPrimaryClip(it)
            }
            Toast.makeText(this@MainActivity, "${mLayout.tvLowerOutputDataText.text.toString().replace(":", "")}Copy to Clipboard", Toast.LENGTH_SHORT).also {
                it.show()
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        mConverter = null
        spinnerAdapter = null
    }

    private fun upperSegmentDataProceed() {
        if (upperFormatData == "Decimal") {
            upperSegmentDecimalData = this.removeFirstCharacter(mLayout.upperSegmentInputData.text.toString(), isZero = true)
            if (upperSegmentDecimalData.isNotEmpty()) {
                if(mConverter!!.isDecimalNumber(upperSegmentDecimalData)) {
                    upperSegmentBinaryData = mConverter!!.convertDecimalToBinary(upperSegmentDecimalData.toLong()).toString()
                    upperSegmentOctalData = mConverter!!.convertDecimalToOctal(upperSegmentDecimalData.toLong()).toString()
                    upperSegmentHexadecimalData = mConverter!!.convertDecimalToHexadecimal(upperSegmentDecimalData.toLong())

                    updateUpperSegmentLayoutData(upperFormatData)
                } else {
                    Toast.makeText(this@MainActivity, "incorrect $upperFormatData Data, please check entered your data", Toast.LENGTH_SHORT).also {
                        it.show()
                    }
                }
            } else {
                Toast.makeText(this@MainActivity, "$upperFormatData data field empty, please enter your data", Toast.LENGTH_SHORT).also {
                    it.show()
                }
            }
        } else if((upperFormatData == "Binary")) {
            upperSegmentBinaryData = mLayout.upperSegmentInputData.text.toString()
            if (upperSegmentBinaryData.isNotEmpty()) {
                if(mConverter!!.isBinaryNumber(upperSegmentBinaryData)) {
                    upperSegmentDecimalData = mConverter!!.convertBinaryToDecimal(upperSegmentBinaryData.toLong()).toString()
                    upperSegmentOctalData = mConverter!!.convertBinaryToOctal(upperSegmentBinaryData.toLong()).toString()
                    upperSegmentHexadecimalData = mConverter!!.convertBinaryToHexadecimal(upperSegmentBinaryData.toLong())

                    updateUpperSegmentLayoutData(upperFormatData)
                } else {
                    Toast.makeText(this@MainActivity, "incorrect $upperFormatData Data, please check entered your data", Toast.LENGTH_SHORT).also {
                        it.show()
                    }
                }
            } else {
                Toast.makeText(this@MainActivity, "$upperFormatData data field empty, please enter your data", Toast.LENGTH_SHORT).also {
                    it.show()
                }
            }
        } else if((upperFormatData == "Octal")) {
            upperSegmentOctalData = mLayout.upperSegmentInputData.text.toString()
            if (upperSegmentOctalData.isNotEmpty()) {
                if(mConverter!!.isOctalNumber(upperSegmentOctalData)) {
                    upperSegmentDecimalData = mConverter!!.convertOctalToDecimal(upperSegmentOctalData.toLong()).toString()
                    upperSegmentBinaryData = mConverter!!.convertOctalToBinary(upperSegmentOctalData.toLong()).toString()
                    upperSegmentHexadecimalData = mConverter!!.convertOctalToHexadecimal(upperSegmentOctalData.toLong())

                    updateUpperSegmentLayoutData(upperFormatData)
                } else {
                    Toast.makeText(this@MainActivity, "incorrect $upperFormatData Data, please check entered your data", Toast.LENGTH_SHORT).also {
                        it.show()
                    }
                }
            } else {
                Toast.makeText(this@MainActivity, "$upperFormatData data field empty, please enter your data", Toast.LENGTH_SHORT).also {
                    it.show()
                }
            }
        } else if((upperFormatData == "Hexadecimal")) {
            upperSegmentHexadecimalData = mLayout.upperSegmentInputData.text.toString()
            if (upperSegmentHexadecimalData.isNotEmpty()) {
                if(mConverter!!.isHexadecimalNumber(upperSegmentHexadecimalData)) {
                    upperSegmentDecimalData = mConverter!!.convertHexadecimalToDecimal(upperSegmentHexadecimalData).toString()
                    upperSegmentBinaryData = mConverter!!.convertHexadecimalToBinary(upperSegmentHexadecimalData).toString()
                    upperSegmentOctalData = mConverter!!.convertHexadecimalToOctal(upperSegmentHexadecimalData).toString()

                    updateUpperSegmentLayoutData(upperFormatData)
                } else {
                    Toast.makeText(this@MainActivity, "incorrect $upperFormatData Data, please check entered your data", Toast.LENGTH_SHORT).also {
                        it.show()
                    }
                }
            } else {
                Toast.makeText(this@MainActivity, "$upperFormatData data field empty, please enter your data", Toast.LENGTH_SHORT).also {
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
                if(mConverter!!.isDecimalNumber(lowerSegmentInputData)) {
                    mLayout.constraintLayoutLowerOutput.visibility = View.VISIBLE
                    mLayout.tvLowerOutputDataText.text = "$lowerFormFormatData : "
                    mLayout.tvLowerOutputData.text = mConverter!!.convertDecimalToBinary(
                        lowerSegmentInputData.toLong()
                    )
                } else {
                    Toast.makeText(this@MainActivity, "incorrect $lowerToFormatData Data", Toast.LENGTH_SHORT).also {
                        it.show()
                    }
                }
            } else if(lowerFormFormatData == "Octal") {
                if(mConverter!!.isDecimalNumber(lowerSegmentInputData)) {
                    mLayout.constraintLayoutLowerOutput.visibility = View.VISIBLE
                    mLayout.tvLowerOutputDataText.text = "$lowerFormFormatData : "
                    mLayout.tvLowerOutputData.text = mConverter!!.convertDecimalToOctal(
                        lowerSegmentInputData.toLong()
                    )
                } else {
                    Toast.makeText(this@MainActivity, "incorrect $lowerToFormatData Data", Toast.LENGTH_SHORT).also {
                        it.show()
                    }
                }
            } else if(lowerFormFormatData == "Hexadecimal") {
                if(mConverter!!.isDecimalNumber(lowerSegmentInputData)) {
                    mLayout.constraintLayoutLowerOutput.visibility = View.VISIBLE
                    mLayout.tvLowerOutputDataText.text = "$lowerFormFormatData : "
                    mLayout.tvLowerOutputData.text = mConverter!!.convertDecimalToHexadecimal(
                        lowerSegmentInputData.toLong()
                    )
                } else {
                    Toast.makeText(this@MainActivity, "incorrect $lowerToFormatData Data", Toast.LENGTH_SHORT).also {
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
                if(mConverter!!.isBinaryNumber(lowerSegmentInputData)) {
                    mLayout.constraintLayoutLowerOutput.visibility = View.VISIBLE
                    mLayout.tvLowerOutputDataText.text = "$lowerFormFormatData : "
                    mLayout.tvLowerOutputData.text = mConverter!!.convertBinaryToDecimal(
                        lowerSegmentInputData.toLong()
                    )
                } else {
                    Toast.makeText(this@MainActivity, "incorrect $lowerToFormatData Data", Toast.LENGTH_SHORT).also {
                        it.show()
                    }
                }
            } else if(lowerFormFormatData == "Binary") {
                Toast.makeText(this@MainActivity, getString(R.string.same_format), Toast.LENGTH_SHORT).also {
                    it.show()
                }
            } else if(lowerFormFormatData == "Octal") {
                if(mConverter!!.isBinaryNumber(lowerSegmentInputData)) {
                    mLayout.constraintLayoutLowerOutput.visibility = View.VISIBLE
                    mLayout.tvLowerOutputDataText.text = "$lowerFormFormatData : "
                    mLayout.tvLowerOutputData.text = mConverter!!.convertBinaryToOctal(
                        lowerSegmentInputData.toLong()
                    )
                } else {
                    Toast.makeText(this@MainActivity, "incorrect $lowerToFormatData Data", Toast.LENGTH_SHORT).also {
                        it.show()
                    }
                }
            } else if(lowerFormFormatData == "Hexadecimal") {
                if(mConverter!!.isBinaryNumber(lowerSegmentInputData)) {
                    mLayout.constraintLayoutLowerOutput.visibility = View.VISIBLE
                    mLayout.tvLowerOutputDataText.text = "$lowerFormFormatData : "
                    mLayout.tvLowerOutputData.text = mConverter!!.convertBinaryToHexadecimal(
                        lowerSegmentInputData.toLong()
                    )
                } else {
                    Toast.makeText(this@MainActivity, "incorrect $lowerToFormatData Data", Toast.LENGTH_SHORT).also {
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
                if(mConverter!!.isOctalNumber(lowerSegmentInputData)) {
                    mLayout.constraintLayoutLowerOutput.visibility = View.VISIBLE
                    mLayout.tvLowerOutputDataText.text = "$lowerFormFormatData : "
                    mLayout.tvLowerOutputData.text = mConverter!!.convertOctalToDecimal(
                        lowerSegmentInputData.toLong()
                    )
                } else {
                    Toast.makeText(this@MainActivity, "incorrect $lowerToFormatData Data", Toast.LENGTH_SHORT).also {
                        it.show()
                    }
                }
            } else if(lowerFormFormatData == "Binary") {
                if(mConverter!!.isOctalNumber(lowerSegmentInputData)) {
                    mLayout.constraintLayoutLowerOutput.visibility = View.VISIBLE
                    mLayout.tvLowerOutputDataText.text = "$lowerFormFormatData : "
                    mLayout.tvLowerOutputData.text = mConverter!!.convertOctalToBinary(
                        lowerSegmentInputData.toLong()
                    )
                } else {
                    Toast.makeText(this@MainActivity, "incorrect $lowerToFormatData Data", Toast.LENGTH_SHORT).also {
                        it.show()
                    }
                }
            } else if(lowerFormFormatData == "Octal") {
                Toast.makeText(this@MainActivity, getString(R.string.same_format), Toast.LENGTH_SHORT).also {
                    it.show()
                }
            } else if(lowerFormFormatData == "Hexadecimal") {
                if(mConverter!!.isOctalNumber(lowerSegmentInputData)) {
                    mLayout.constraintLayoutLowerOutput.visibility = View.VISIBLE
                    mLayout.tvLowerOutputDataText.text = "$lowerFormFormatData : "
                    mLayout.tvLowerOutputData.text = mConverter!!.convertOctalToHexadecimal(
                        lowerSegmentInputData.toLong()
                    )
                } else {
                    Toast.makeText(this@MainActivity, "incorrect $lowerToFormatData Data", Toast.LENGTH_SHORT).also {
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
                if(mConverter!!.isHexadecimalNumber(lowerSegmentInputData)) {
                    mLayout.constraintLayoutLowerOutput.visibility = View.VISIBLE
                    mLayout.tvLowerOutputDataText.text = "$lowerFormFormatData : "
                    mLayout.tvLowerOutputData.text = mConverter!!.convertHexadecimalToDecimal(
                        lowerSegmentInputData
                    )
                } else {
                    Toast.makeText(this@MainActivity, "incorrect $lowerToFormatData Data", Toast.LENGTH_SHORT).also {
                        it.show()
                    }
                }
            } else if(lowerFormFormatData == "Binary") {
                if(mConverter!!.isHexadecimalNumber(lowerSegmentInputData)) {
                    mLayout.constraintLayoutLowerOutput.visibility = View.VISIBLE
                    mLayout.tvLowerOutputDataText.text = "$lowerFormFormatData : "
                    mLayout.tvLowerOutputData.text = mConverter!!.convertHexadecimalToBinary(
                        lowerSegmentInputData
                    )
                } else {
                    Toast.makeText(this@MainActivity, "incorrect $lowerToFormatData Data", Toast.LENGTH_SHORT).also {
                        it.show()
                    }
                }
            } else if(lowerFormFormatData == "Octal") {
                if(mConverter!!.isHexadecimalNumber(lowerSegmentInputData)) {
                    mLayout.constraintLayoutLowerOutput.visibility = View.VISIBLE
                    mLayout.tvLowerOutputDataText.text = "$lowerFormFormatData : "
                    mLayout.tvLowerOutputData.text = mConverter!!.convertHexadecimalToOctal(
                        lowerSegmentInputData
                    )
                } else {
                    Toast.makeText(this@MainActivity, "incorrect $lowerToFormatData Data", Toast.LENGTH_SHORT).also {
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
            // mLayout.constraintLayoutDecimalUpperOutputText.visibility = View.GONE
            // mLayout.tvDecimalUpperOutputData.visibility = View.INVISIBLE
        } else if((formatData == "Binary")) {
            // mLayout.constraintLayoutBinaryUpperOutputText.visibility = View.GONE
            // mLayout.tvBinaryUpperOutputData.visibility = View.INVISIBLE
        } else if((formatData == "Octal")) {
            // mLayout.constraintLayoutOctalUpperOutputText.visibility = View.GONE
            // mLayout.tvOctalUpperOutputData.visibility = View.INVISIBLE
        } else if((formatData == "Hexadecimal")) {
            // mLayout.constraintLayoutHexadecimalUpperOutputText.visibility = View.GONE
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
        mLayout.upperSegmentInputData.setText("")
        mLayout.tvDecimalUpperOutputData.text = ""
        mLayout.tvBinaryUpperOutputData.text = ""
        mLayout.tvOctalUpperOutputData.text = ""
        mLayout.tvHexadecimalUpperOutputData.text = ""


        upperSegmentDecimalData = ""
        upperSegmentBinaryData = ""
        upperSegmentOctalData = ""
        upperSegmentHexadecimalData = ""

        mLayout.constraintLayoutDecimalUpperOutputText.visibility = View.INVISIBLE
        mLayout.constraintLayoutBinaryUpperOutputText.visibility = View.INVISIBLE
        mLayout.constraintLayoutOctalUpperOutputText.visibility = View.INVISIBLE
        mLayout.constraintLayoutHexadecimalUpperOutputText.visibility = View.INVISIBLE

        mLayout.upperSegmentInputLayout.error = null
    }

    private fun onLowerSegmentRefresh() {
        mLayout.lowerSegmentInputData.setText("")
        lowerSegmentInputData = ""
        mLayout.constraintLayoutLowerOutput.visibility = View.INVISIBLE
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

}