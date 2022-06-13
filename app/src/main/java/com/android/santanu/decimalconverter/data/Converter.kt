package com.android.santanu.decimalconverter.data

import kotlin.math.pow

class Converter {

    fun decimalToBinary(data: Int): Long {
        var decimal = data
        var binaryNumber: Long = 0
        var remainder: Int
        var i = 1
        var step = 1

        while (decimal != 0) {
            remainder = decimal % 2
            System.out.printf("Step %d: %d/2, Remainder = %d, Quotient = %d\n", step++, decimal, remainder, decimal / 2)
            decimal /= 2
            binaryNumber += (remainder * i).toLong()
            i *= 10
        }
        return binaryNumber
    }
    fun decimalToOctal(data: Int): Int {
        var decimal: Int = data
        var octalNumber: Int = 0
        var i = 1

        while (decimal != 0) {
            octalNumber += decimal % 8 * i
            decimal /= 8
            i *= 10
        }

        return octalNumber
    }
    fun decimalToHexadecimal(data: Int) : String {
        return Integer.toHexString(data)
    }

    fun binaryToDecimal(data: Long): Int {
        var binary: Long = data
        var decimalNumber: Int = 0
        var i = 0
        var remainder: Long

        while (binary.toInt() != 0) {
            remainder = binary % 10
            binary /= 10
            decimalNumber += (remainder * 2.0.pow(i.toDouble())).toInt()
            ++i
        }
        return decimalNumber
    }
    fun binaryToOctal(data: Long): Int {
        var binaryNumber = data
        var octalNumber = 0
        var decimalNumber = 0
        var i = 0

        while (binaryNumber.toInt() != 0) {
            decimalNumber += (binaryNumber % 10 * 2.0.pow(i.toDouble())).toInt()
            ++i
            binaryNumber /= 10
        }

        i = 1

        while (decimalNumber != 0) {
            octalNumber += decimalNumber % 8 * i
            decimalNumber /= 8
            i *= 10
        }

        return octalNumber
    }
    fun binaryToHexadecimal(data: Long) : String {
        return Integer.toHexString(this.binaryToDecimal(data))
    }

    fun octalToDecimal(data: Int): Int {
        var octal = data
        var decimalNumber = 0
        var i = 0

        while (octal != 0) {
            decimalNumber += (octal % 10 * 8.0.pow(i.toDouble())).toInt()
            ++i
            octal /= 10
        }

        return decimalNumber
    }
    fun octalToBinary(data: Int): Long {
        var octalNumber = data
        var decimalNumber = 0
        var i = 0
        var binaryNumber: Long = 0

        while (octalNumber != 0) {
            decimalNumber += (octalNumber % 10 * Math.pow(8.0, i.toDouble())).toInt()
            ++i
            octalNumber /= 10
        }

        i = 1

        while (decimalNumber != 0) {
            binaryNumber += (decimalNumber % 2 * i).toLong()
            decimalNumber /= 2
            i *= 10
        }

        return binaryNumber
    }
    fun octalToHexadecimal(data: Int) : String {
        return Integer.toHexString(this.octalToDecimal(data))
    }

    fun hexadecimalToDecimal(data: String): Int {
        val hexValue: String = data
        val len = hexValue.length
        // Initializing base value to 1, i.e 16^0
        var base = 1
        var decimal = 0

        // Extracting characters as digits from last character
        for (i in len - 1 downTo 0) {
            // if character lies in '0'-'9', converting it to integral 0-9 by subtracting 48 from ASCII value
            if (hexValue[i] in '0'..'9') {
                decimal += (hexValue[i].code - 48) * base
                // incrementing base by power
                base *= 16
            } else if (hexValue[i] in 'A'..'F') {
                decimal += (hexValue[i].code - 55) * base

                // incrementing base by power
                base *= 16
            }
        }
        return decimal
    }
    fun hexadecimalToBinary(data: String): Long {
        val decimal: Int = this.hexadecimalToDecimal(data)
        return this.decimalToBinary(decimal)
    }
    fun hexadecimalToOctal(data: String): Int {
        val decimal: Int = this.hexadecimalToDecimal(data)
        return this.decimalToOctal(decimal)
    }

    fun checkDecimalNumberFormat(decimalNumber: String): Boolean {
        var isDecimalNumber = true

        for(charAtPos in decimalNumber) {
            if(!(((charAtPos >= '0') && (charAtPos <= '9')) )) {
                isDecimalNumber = false
                break
            }
        }
        return isDecimalNumber
    }
    fun checkBinaryNumberFormat(binaryNumber: String): Boolean {
        var isBinaryNumber = true

        for(charAtPos in binaryNumber) {
            if(!(((charAtPos == '0') && (charAtPos == '1')) )) {
                isBinaryNumber = false
                break
            }
        }
        return isBinaryNumber
    }
    fun checkOctalNumberFormat(octalNumber: String): Boolean {
        var isOctalNumber = true

        for(charAtPos in octalNumber) {
            if(!(((charAtPos >= '0') && (charAtPos <= '7')) )) {
                isOctalNumber = false
                break
            }
        }
        return isOctalNumber
    }
    fun checkHexadecimalNumberFormat(hexadecimalNumber: String): Boolean {
        var isHexadecimalNumber = true

        for(charAtPos in hexadecimalNumber) {
            if(!(((charAtPos >= '0') && (charAtPos <= '9')) || ((charAtPos >= 'A') && (charAtPos <= 'F')) || ((charAtPos >= 'a') && (charAtPos <= 'f')))) {
                isHexadecimalNumber = false
                break
            }
        }
        return isHexadecimalNumber
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

        for (i in 0 until binary.length) {
            decimal += (Math.pow(base.toDouble(), i.toDouble()).toLong() * binary[i].toString().toLong()).toLong()
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