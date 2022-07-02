package com.android.santanu.decimalconverter.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.android.santanu.decimalconverter.R
import com.android.santanu.decimalconverter.data.logic.FractionNumberConverter
import com.android.santanu.decimalconverter.data.logic.GeneralNumberConverter
import com.android.santanu.decimalconverter.data.logic.NumberFormatUtils

class TestActivity : AppCompatActivity() {
    private val TAG: String by lazy { TestActivity::class.java.simpleName }


    private var mFractionNumberConverter: FractionNumberConverter? = null
    private var mGeneralNumberConverter: GeneralNumberConverter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        if(mFractionNumberConverter == null) {
            mFractionNumberConverter = FractionNumberConverter()
        }

        if(mGeneralNumberConverter == null) {
            mGeneralNumberConverter = GeneralNumberConverter()
        }



        Log.d(TAG, "MainActivity{} : onCreate() >>" +
                " [line ${Thread.currentThread().stackTrace[2].lineNumber}] :: ==> Dec[5.55] to Bin : ${mFractionNumberConverter!!.convertFractionDecimalToBinary(5.55)}"
        )
        Log.d(TAG, "MainActivity{} : onCreate() >>" +
                " [line ${Thread.currentThread().stackTrace[2].lineNumber}] :: ==> Bin[101.10001] to Dec : ${mFractionNumberConverter!!.convertBinaryToDecimal(101.10001)}"
        )











        Log.d(TAG, "MainActivity{} : onCreate() >>" +
                " [line ${Thread.currentThread().stackTrace[2].lineNumber}] :: Decimal[15] to Binary : ${mGeneralNumberConverter!!.decimalToOtherNumberFormat(
                    "15", NumberFormatUtils.BINARY_FORMAT
                )}"
        )

        Log.d(TAG, "MainActivity{} : onCreate() >>" +
                " [line ${Thread.currentThread().stackTrace[2].lineNumber}] :: Decimal[15.123] to Binary : ${mGeneralNumberConverter!!.decimalToOtherNumberFormat(
                    "15.123", NumberFormatUtils.BINARY_FORMAT
                )}"
        )
        Log.d(TAG, "MainActivity{} : onCreate() >>" +
                " [line ${Thread.currentThread().stackTrace[2].lineNumber}] :: Decimal[15] to Octal : ${mGeneralNumberConverter!!.decimalToOtherNumberFormat(
                    "15", NumberFormatUtils.OCTAL_FORMAT
                )}"
        )
        Log.d(TAG, "MainActivity{} : onCreate() >>" +
        " [line ${Thread.currentThread().stackTrace[2].lineNumber}] :: Decimal[15.123] to Octal : ${mGeneralNumberConverter!!.decimalToOtherNumberFormat(
            "15.123", NumberFormatUtils.OCTAL_FORMAT
        )}"
        )
        Log.d(TAG, "MainActivity{} : onCreate() >>" +
                " [line ${Thread.currentThread().stackTrace[2].lineNumber}] :: Decimal[15] to Hexadecimal : ${mGeneralNumberConverter!!.decimalToOtherNumberFormat(
                    "15", NumberFormatUtils.HEXADECIMAL_FORMAT
                )}"
        )
        Log.d(TAG, "MainActivity{} : onCreate() >>" +
                " [line ${Thread.currentThread().stackTrace[2].lineNumber}] :: Decimal[15.123] to Hexadecimal : ${mGeneralNumberConverter!!.decimalToOtherNumberFormat(
                    "15.123", NumberFormatUtils.HEXADECIMAL_FORMAT
                )}"
        )



        Log.d(TAG, "MainActivity{} : onCreate() >>" +
                " [line ${Thread.currentThread().stackTrace[2].lineNumber}] :: Binary[1111.0001111101] to Decimal : ${mGeneralNumberConverter!!.otherToDecimalNumberFormat(
                    "1111.0001111101", NumberFormatUtils.BINARY_FORMAT
                )}"
        )
        Log.d(TAG, "MainActivity{} : onCreate() >>" +
                " [line ${Thread.currentThread().stackTrace[2].lineNumber}] :: Binary[1111] to Decimal : ${mGeneralNumberConverter!!.otherToDecimalNumberFormat(
                    "1111", NumberFormatUtils.BINARY_FORMAT
                )}"
        )

        Log.d(TAG, "MainActivity{} : onCreate() >>" +
                " [line ${Thread.currentThread().stackTrace[2].lineNumber}] :: Octal[17.0767635544] to Decimal : ${mGeneralNumberConverter!!.otherToDecimalNumberFormat(
                    "17.0767635544", NumberFormatUtils.OCTAL_FORMAT
                )}"
        )
        Log.d(TAG, "MainActivity{} : onCreate() >>" +
                " [line ${Thread.currentThread().stackTrace[2].lineNumber}] :: Octal[17] to Decimal : ${mGeneralNumberConverter!!.otherToDecimalNumberFormat(
                    "17", NumberFormatUtils.OCTAL_FORMAT
                )}"
        )

        Log.d(TAG, "MainActivity{} : onCreate() >>" +
                " [line ${Thread.currentThread().stackTrace[2].lineNumber}] :: Hexadecimal[F.1F7CED9168] to Decimal : ${mGeneralNumberConverter!!.otherToDecimalNumberFormat(
                    "F.1F7CED9168", NumberFormatUtils.HEXADECIMAL_FORMAT
                )}"
        )
        Log.d(TAG, "MainActivity{} : onCreate() >>" +
                " [line ${Thread.currentThread().stackTrace[2].lineNumber}] :: Hexadecimal[F] to Decimal : ${mGeneralNumberConverter!!.otherToDecimalNumberFormat(
                    "F", NumberFormatUtils.HEXADECIMAL_FORMAT
                )}"
        )
    }
}