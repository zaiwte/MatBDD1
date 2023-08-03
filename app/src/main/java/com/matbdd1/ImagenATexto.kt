package com.matbdd1
import android.content.Context
import android.graphics.Bitmap
import com.googlecode.tesseract.android.TessBaseAPI
import com.googlecode.tesseract.android.TessBaseAPI.*
import java.io.File
import java.io.InputStream

class ImagenATexto (val context : Context) {

    private val tess = TessBaseAPI()
    private val folderTessDataName : String = "tessdata"
    private val pathDir = context.getExternalFilesDir(null).toString()

    init {
        val folder = File(pathDir, folderTessDataName)

        if (!folder.exists()){
            folder.mkdir()
        }

        if (folder.exists()){
            addFile ("spa_old.traineddata", R.raw.spa_old)
            addFile ("osd.traineddata", R.raw.osd)
            addFile ("equ.traineddata", R.raw.equ)
            addFile ("enm.traineddata", R.raw.enm)
            addFile ("eng.traineddata", R.raw.eng)
        }

    }

    private fun addFile (name : String, source : Int){
        val file = File ("$pathDir/$folderTessDataName/$name")
        if (!file.exists()){
            val inputStream : InputStream = context.resources.openRawResource(source)
            file.appendBytes(inputStream.readBytes())
            file.createNewFile()
        }
    }

    fun procesarImagen (imagen : Bitmap, leng : String) : String {
        tess.init(pathDir,leng,OEM_LSTM_ONLY,mapOf("--psm" to "11",))
        tess.setImage(imagen)
        return tess.utF8Text
    }

    fun reciclar (){
        tess.recycle()
    }

}