package com.android.santanu.decimalconverter.data

import kotlin.math.pow

class NumberConverter {

    fun isDecimalNumber(decimal: String, checkFraction: Boolean = false): Boolean {
        return if (checkFraction) {
            val regex = "^[0-9]*[.]?[0-9]*\$".toRegex()
            decimal.matches(regex)
        } else {
            val regex = "^[0-9][0-9]*\$".toRegex()
            decimal.matches(regex)
        }
    }
    fun isBinaryNumber(binary: String, checkFraction: Boolean = false): Boolean {
        return if (checkFraction) {
            val regex = "^[0-1]*[.]?[0-1]*\$".toRegex()
            binary.matches(regex)
        } else {
            val regex = "^[0-1][0-1]*\$".toRegex()
            binary.matches(regex)
        }
    }
    fun isOctalNumber(octal: String, checkFraction: Boolean = false): Boolean {
        return if (checkFraction) {
            val regex = "^[0-7]*[.]?[0-7]*\$".toRegex()
            octal.matches(regex)
        } else {
            val regex = "^[0-7][0-7]*\$".toRegex()
            octal.matches(regex)
        }

    }
    fun isHexadecimalNumber(hexadecimal: String, checkFraction: Boolean = false): Boolean {
        return if (checkFraction) {
            val regex = "^[0-9A-Fa-f]*[.]?[0-9A-Fa-f]+\$".toRegex()
            hexadecimal.matches(regex)
        } else {
            val regex = "^[0-9A-Fa-f]+\$".toRegex()
            hexadecimal.matches(regex)
        }

    }

    fun convertDecimalToBinary(decimalData: Long) : String {
        var decimal: Long = decimalData
        val binaryNumber: StringBuilder = StringBuilder()
        var remainder: Int

        while(decimal > 1) {
            remainder = (decimal % 2).toInt()
            decimal /= 2
            binaryNumber.append(remainder.toString())

        }
        binaryNumber.append(decimal.toString())

        return binaryNumber.toString().reversed()
    }
    fun convertDecimalToOctal(decimalData: Long) : String {
        var decimal: Long = decimalData
        val octalNumber: StringBuilder = StringBuilder()
        var remainder: Int

        while(decimal > 7) {
            remainder = (decimal % 8).toInt()
            decimal /= 8
            octalNumber.append(remainder.toString())

        }
        octalNumber.append(decimal.toString())

        return octalNumber.toString().reversed()
    }
    fun convertDecimalToHexadecimal(decimalData: Long) : String {
        var decimal: Long = decimalData
        val hexadecimalNumber: StringBuilder = StringBuilder()
        var remainder: Int

        while(decimal > 15) {
            remainder = (decimal % 16).toInt()
            decimal /= 16
            when (val data: String = remainder.toString()) {
                "15" -> {
                    hexadecimalNumber.append("F")
                }
                "14" -> {
                    hexadecimalNumber.append("E")
                }
                "13" -> {
                    hexadecimalNumber.append("D")
                }
                "12" -> {
                    hexadecimalNumber.append("C")
                }
                "11" -> {
                    hexadecimalNumber.append("B")
                }
                "10" -> {
                    hexadecimalNumber.append("A")
                }
                else -> {
                    hexadecimalNumber.append(data)
                }
            }
        }

        when (val data: String = decimal.toString()) {
            "15" -> {
                hexadecimalNumber.append("F")
            }
            "14" -> {
                hexadecimalNumber.append("E")
            }
            "13" -> {
                hexadecimalNumber.append("D")
            }
            "12" -> {
                hexadecimalNumber.append("C")
            }
            "11" -> {
                hexadecimalNumber.append("B")
            }
            "10" -> {
                hexadecimalNumber.append("A")
            }
            else -> {
                hexadecimalNumber.append(data)
            }
        }

        return hexadecimalNumber.toString().reversed()
    }

    fun convertBinaryToDecimal(binaryData: Long) : String {
        val base: Int = 2
        val binary: String = binaryData.toString().reversed()
        var decimal: Long = 0

        for (i in binary.indices) {
            decimal += (base.toDouble().pow(i.toDouble()).toLong() * binary[i].toString().toLong()).toLong()
        }

        return decimal.toString()
    }
    fun convertBinaryToOctal(binaryData: Long) : String {
        return this.convertDecimalToOctal(
            this.convertBinaryToDecimal(binaryData).toLong()
        )
    }
    fun convertBinaryToHexadecimal(binaryData: Long) : String {
        return this.convertDecimalToHexadecimal(
            this.convertBinaryToDecimal(binaryData).toLong()
        )
    }

    fun convertOctalToDecimal(octalData: Long) : String {
        val base: Int = 8
        val octal: String = octalData.toString().reversed()
        var decimal: Long = 0

        for (i in 0 until octal.length) {
            decimal += (Math.pow(base.toDouble(), i.toDouble()).toLong() * octal[i].toString().toLong()).toLong()
        }

        return decimal.toString()
    }
    fun convertOctalToBinary(octalData: Long) : String {
        return this.convertDecimalToBinary(
            this.convertOctalToDecimal(octalData).toLong()
        )
    }
    fun convertOctalToHexadecimal(octalData: Long) : String {
        return this.convertDecimalToHexadecimal(
            this.convertOctalToDecimal(octalData).toLong()
        )
    }

    fun convertHexadecimalToDecimal(hexadecimalData: String) : String {
        val base: Int = 16
        val hexadecimal: String = hexadecimalData.reversed()
        var decimal: Long = 0
        var data: String = ""

        for (i in hexadecimal.indices) {

            data = hexadecimal[i].toString()

            when (data) {
                "F" -> {
                    decimal += (base.toDouble().pow(i.toDouble()).toLong() * 15L).toLong()
                }
                "E" -> {
                    decimal += (base.toDouble().pow(i.toDouble()).toLong() * 14L).toLong()
                }
                "D" -> {
                    decimal += (base.toDouble().pow(i.toDouble()).toLong() * 13L).toLong()
                }
                "C" -> {
                    decimal += (base.toDouble().pow(i.toDouble()).toLong() * 12L).toLong()
                }
                "B" -> {
                    decimal += (base.toDouble().pow(i.toDouble()).toLong() * 11L).toLong()
                }
                "A" -> {
                    decimal += (base.toDouble().pow(i.toDouble()).toLong() * 10L).toLong()
                }
                else -> {
                    decimal += (base.toDouble().pow(i.toDouble()).toLong() * data.toLong()).toLong()
                }
            }

        }

        return decimal.toString()
    }
    fun convertHexadecimalToBinary(hexadecimalData: String) : String {
        return this.convertDecimalToBinary(
            this.convertHexadecimalToDecimal(hexadecimalData).toLong()
        )
    }
    fun convertHexadecimalToOctal(hexadecimalData: String) : String {
        return this.convertDecimalToOctal(
            this.convertHexadecimalToDecimal(hexadecimalData).toLong()
        )
    }



}