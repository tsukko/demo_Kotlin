package jp.co.integrityworks.prototype.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import java.io.ByteArrayOutputStream

class Decoder  {

    // base64のStringをBitmap形式の画像に復元する
    fun decodeBase64(input: String): Bitmap {
        val deco = Base64.decode(input, 0)
        return BitmapFactory.decodeByteArray(deco, 0, deco.size)
    }

    fun encodeBase64(input: Bitmap): String {
        val byteArrayOS = ByteArrayOutputStream()
        input.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOS)
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT)
    }
}