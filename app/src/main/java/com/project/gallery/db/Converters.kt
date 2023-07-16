package com.project.gallery.db

import android.net.Uri
import androidx.room.TypeConverter
import java.math.BigInteger


object Converters {
    @TypeConverter
    @JvmStatic
    fun fromUri(uri: Uri): String {
        return uri.toString()
    }

    @TypeConverter
    @JvmStatic
    fun toUri(uriString: String): Uri {
        return Uri.parse(uriString)
    }
    @TypeConverter
    fun fromBigInteger(value: BigInteger): Long {
        return value.toLong()
    }

    @TypeConverter
    fun toBigInteger(value: Long): BigInteger {
        return BigInteger.valueOf(value)
    }
}