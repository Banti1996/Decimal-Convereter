package com.android.santanu.decimalconverter.ui

import android.R.layout
import android.annotation.SuppressLint
import android.app.PendingIntent.getActivity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import com.android.santanu.decimalconverter.R
import com.android.santanu.decimalconverter.data.Converter
import com.android.santanu.decimalconverter.databinding.ActivityMainBinding
import com.android.santanu.decimalconverter.ui.base.BaseActivity
import com.android.santanu.decimalconverter.vm.MainViewModel
import com.google.android.material.snackbar.Snackbar
import kotlin.system.exitProcess


class MainActivity : BaseActivity<ActivityMainBinding>() {
    private val TAG: String = MainActivity::class.java.simpleName

    private var formatData : String? = null

    private var decimalData : String = ""
    private var binaryData : String = ""
    private var octalData : String = ""
    private var hexadecimalData : String = ""

    private var mConverter: Converter? = null

    private lateinit var mLayout: ActivityMainBinding
    private val mMainViewModel: MainViewModel by viewModels<MainViewModel>()

    private val formateList: ArrayList<String> = ArrayList<String>().apply {
        this.add("Decimal")
        this.add("Binary")
        this.add("Octal")
        this.add("Hexadecimal")
    }

    private lateinit var clipboard : ClipboardManager

    override fun getLayoutId(): Int = R.layout.activity_main


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        mLayout = getViewDataBinding().apply {
            this.lifecycleOwner = this@MainActivity
        }
        initSpinner()

        if(mConverter == null) {
            mConverter = Converter()
        }

        clipboard  = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager


        /*
        mMainViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(
            application
        ).create(MainViewModel::class.java)
        */


        mLayout.convertButton.setOnClickListener {
            formatData = mLayout.baseTextView.text.toString() ?: null
            if (formatData != null && formatData!!.isNotEmpty()) {
                if (formateList.contains(formatData)) {
                    proceed()
                } else {
                    Toast.makeText(this@MainActivity, "Please Select Number Format From Dropdown List", Toast.LENGTH_SHORT).also {
                        it.show()
                    }
                }
            } else {
                Toast.makeText(this@MainActivity, "Please Select Number Format", Toast.LENGTH_SHORT).also {
                    it.show()
                }
            }
        }

        mLayout.ivCopyDecimal.setOnClickListener {
            ClipData.newPlainText("decimal text", mLayout.tvTextDataDecimal.text.toString()).also {
                clipboard.setPrimaryClip(it)
            }
            Toast.makeText(this@MainActivity, "Decimal Copy to Clipboard", Toast.LENGTH_SHORT).also {
                it.show()
            }
        }

        mLayout.ivCopyBinary.setOnClickListener {
            ClipData.newPlainText("binary text", mLayout.tvTextDataBinary.text.toString()).also {
                clipboard.setPrimaryClip(it)
            }
            Toast.makeText(this@MainActivity, "Binary Copy to Clipboard", Toast.LENGTH_SHORT).also {
                it.show()
            }
        }

        mLayout.ivCopyOctal.setOnClickListener {
            ClipData.newPlainText("octal text", mLayout.tvTextDataOctal.text.toString()).also {
                clipboard.setPrimaryClip(it)
            }
            Toast.makeText(this@MainActivity, "Octal Copy to Clipboard", Toast.LENGTH_SHORT).also {
                it.show()
            }
        }

        mLayout.ivCopyOctal.setOnClickListener {
            ClipData.newPlainText("hexadecimal text", mLayout.tvTextDataHexadecimal.text.toString()).also {
                clipboard.setPrimaryClip(it)
            }
            Toast.makeText(this@MainActivity, "Hexadecimal Copy to Clipboard", Toast.LENGTH_SHORT).also {
                it.show()
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        mConverter = null
    }

    private fun proceed() {
        if (formatData == "Decimal") {
            decimalData = mLayout.inputData.text.toString()
            if (decimalData.isNotEmpty()) {
                if(mConverter!!.checkDecimalNumberFormat(decimalData)) {
                    binaryData = mConverter!!.decimalToBinary(decimalData.toInt()).toString()
                    octalData = mConverter!!.decimalToOctal(decimalData.toInt()).toString()
                    hexadecimalData = mConverter!!.decimalToHexadecimal(decimalData.toInt())

                    updateLayoutData(formatData!!)
                } else {
                    Toast.makeText(this@MainActivity, "Wrong Format Data", Toast.LENGTH_SHORT).also {
                        it.show()
                    }
                }
            } else {
                Toast.makeText(this@MainActivity, "Decimal Data Empty", Toast.LENGTH_SHORT).also {
                    it.show()
                }
            }
        } else if((formatData == "Binary")) {
            binaryData = mLayout.inputData.text.toString()
            if (binaryData.isNotEmpty()) {
                if(mConverter!!.checkBinaryNumberFormat(binaryData)) {
                    decimalData = mConverter!!.binaryToDecimal(binaryData.toLong()).toString()
                    octalData = mConverter!!.binaryToOctal(binaryData.toLong()).toString()
                    hexadecimalData = mConverter!!.binaryToHexadecimal(binaryData.toLong())

                    updateLayoutData(formatData!!)
                } else {
                    Toast.makeText(this@MainActivity, "Wrong Format Data", Toast.LENGTH_SHORT).also {
                        it.show()
                    }
                }
            } else {
                Toast.makeText(this@MainActivity, "Binary Data Empty", Toast.LENGTH_SHORT).also {
                    it.show()
                }
            }
        } else if((formatData == "Octal")) {
            octalData = mLayout.inputData.text.toString()
            if (octalData.isNotEmpty()) {
                if(mConverter!!.checkOctalNumberFormat(octalData)) {
                    decimalData = mConverter!!.octalToDecimal(octalData.toInt()).toString()
                    binaryData = mConverter!!.octalToBinary(octalData.toInt()).toString()
                    hexadecimalData = mConverter!!.octalToHexadecimal(octalData.toInt())

                    updateLayoutData(formatData!!)
                } else {
                    Toast.makeText(this@MainActivity, "Wrong Format Data", Toast.LENGTH_SHORT).also {
                        it.show()
                    }
                }
            } else {
                Toast.makeText(this@MainActivity, "Octal Data Empty", Toast.LENGTH_SHORT).also {
                    it.show()
                }
            }
        } else if((formatData == "Hexadecimal")) {
            hexadecimalData = mLayout.inputData.text.toString()
            if (hexadecimalData.isNotEmpty()) {
                if(mConverter!!.checkHexadecimalNumberFormat(hexadecimalData)) {
                    decimalData = mConverter!!.hexadecimalToDecimal(hexadecimalData).toString()
                    binaryData = mConverter!!.hexadecimalToBinary(hexadecimalData).toString()
                    octalData = mConverter!!.hexadecimalToOctal(hexadecimalData).toString()

                    updateLayoutData(formatData!!)
                } else {
                    Toast.makeText(this@MainActivity, "Wrong Format Data", Toast.LENGTH_SHORT).also {
                        it.show()
                    }
                }
            } else {
                Toast.makeText(this@MainActivity, "Hexadecimal Data Empty", Toast.LENGTH_SHORT).also {
                    it.show()
                }
            }
        }else {
            Toast.makeText(this@MainActivity, "Something Went Wrong, Restart Application", Toast.LENGTH_LONG).also {
                it.show()
            }
            Snackbar.make(mLayout.constraintLayout,"Something Went Wrong", Snackbar.LENGTH_LONG).also {
                it.setAction("Exit"){
                    Toast.makeText(this@MainActivity,"Restart Application", Toast.LENGTH_SHORT).show()
                    this.finish()
                    exitProcess(0)
                }
                it.show()
            }

        }
    }

    private fun updateLayoutData(formatData: String) {

        mLayout.tvTextDataDecimal.text = decimalData
        mLayout.tvTextDataBinary.text = binaryData
        mLayout.tvTextDataOctal.text = octalData
        mLayout.tvTextDataHexadecimal.text = hexadecimalData

        if (formatData == "Decimal") {
            mLayout.constraintLayoutDecimal.visibility = View.GONE
            // mLayout.tvTextDataDecimal.visibility = View.INVISIBLE
        } else if((formatData == "Binary")) {
            mLayout.constraintLayoutBinary.visibility = View.GONE
            // mLayout.tvTextDataBinary.visibility = View.INVISIBLE
        } else if((formatData == "Octal")) {
            mLayout.constraintLayoutOctal.visibility = View.GONE
            // mLayout.tvTextDataOctal.visibility = View.INVISIBLE
        } else if((formatData == "Hexadecimal")) {
            mLayout.constraintLayoutHexadecimal.visibility = View.GONE
            // mLayout.tvTextDataHexadecimal.visibility = View.INVISIBLE
        }else {
            Toast.makeText(this@MainActivity, "Something Went Wrong, Restart Application", Toast.LENGTH_LONG).also {
                it.show()
            }

            mLayout.constraintLayoutDecimal.visibility = View.GONE
            mLayout.constraintLayoutBinary.visibility = View.GONE
            mLayout.constraintLayoutOctal.visibility = View.GONE
            mLayout.constraintLayoutHexadecimal.visibility = View.GONE
        }
    }

    private fun onRefresh() {
        mLayout.inputData.setText("")
        mLayout.tvTextDataDecimal.text = ""
        mLayout.tvTextDataBinary.text = ""
        mLayout.tvTextDataOctal.text = ""
        mLayout.tvTextDataHexadecimal.text = ""

        decimalData = ""
        binaryData = ""
        octalData = ""
        hexadecimalData = ""

        mLayout.constraintLayoutDecimal.visibility = View.VISIBLE
        mLayout.constraintLayoutBinary.visibility = View.VISIBLE
        mLayout.constraintLayoutOctal.visibility = View.VISIBLE
        mLayout.constraintLayoutHexadecimal.visibility = View.VISIBLE
    }

    private fun initSpinner() {
        val spinnerAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
            this@MainActivity,
            android.R.layout.simple_spinner_dropdown_item,
            formateList
        )

        mLayout.baseTextView.setAdapter(spinnerAdapter)
    }


}